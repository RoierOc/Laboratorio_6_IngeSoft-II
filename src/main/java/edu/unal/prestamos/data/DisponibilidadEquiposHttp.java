package edu.unal.prestamos.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.unal.prestamos.domain.DisponibilidadEquipos;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public final class DisponibilidadEquiposHttp implements DisponibilidadEquipos {
    private static final int HTTP_OK = 200;
    private static final int HTTP_NOT_FOUND = 404;
    private static final String ESTADO_DISPONIBLE = "disponible";
    private static final Duration TIEMPO_MAXIMO = Duration.ofSeconds(5);

    private final String urlBase;
    private final HttpClient cliente;
    private final ObjectMapper json = new ObjectMapper();

    public DisponibilidadEquiposHttp(String urlBase) {
        this(urlBase, HttpClient.newBuilder().connectTimeout(TIEMPO_MAXIMO).build());
    }

    public DisponibilidadEquiposHttp(String urlBase, HttpClient cliente) {
        this.urlBase = urlBase.endsWith("/") ? urlBase.substring(0, urlBase.length() - 1) : urlBase;
        this.cliente = cliente;
    }

    @Override
    public boolean estaDisponible(int equipoId) {
        var peticion = HttpRequest.newBuilder(URI.create(urlBase + "/api/v1/equipos/" + equipoId))
                .timeout(TIEMPO_MAXIMO)
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            var respuesta = cliente.send(peticion, HttpResponse.BodyHandlers.ofString());
            if (respuesta.statusCode() == HTTP_NOT_FOUND) {
                return false;
            }
            if (respuesta.statusCode() != HTTP_OK) {
                throw new ServicioNoDisponible("HTTP " + respuesta.statusCode());
            }
            var equipo = json.readValue(respuesta.body(), EquipoDto.class);
            return ESTADO_DISPONIBLE.equals(equipo.estado());
        } catch (IOException e) {
            throw new ServicioNoDisponible(mensajeDe(e), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServicioNoDisponible(mensajeDe(e), e);
        }
    }

    private static String mensajeDe(Exception e) {
        return e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record EquipoDto(int id, String nombre, String estado) { }
}
