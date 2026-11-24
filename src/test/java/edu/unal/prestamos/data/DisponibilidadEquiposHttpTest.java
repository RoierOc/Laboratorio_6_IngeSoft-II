package edu.unal.prestamos.data;

import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisponibilidadEquiposHttpTest {
    private ServidorHttpDePrueba servidor;
    private DisponibilidadEquiposHttp adaptador;

    @BeforeEach
    void iniciarServidor() throws IOException {
        servidor = ServidorHttpDePrueba.iniciar();
        adaptador = new DisponibilidadEquiposHttp(servidor.url());
    }

    @AfterEach
    void detenerServidor() {
        servidor.detener();
    }

    @Test
    void estaDisponible_equipoDisponible_devuelveVerdaderoYLlamaLaRutaCorrecta() {
        // Arrange
        servidor.responder(200, """
                {"id": 12, "nombre": "Portatil", "estado": "disponible"}""");

        // Act
        boolean disponible = adaptador.estaDisponible(12);

        // Assert
        assertTrue(disponible);
        var peticion = servidor.ultimaPeticionRecibida();
        assertEquals("GET", peticion.metodo());
        assertEquals("/api/v1/equipos/12", peticion.ruta());
    }

    @Test
    void estaDisponible_equipoPrestado_devuelveFalso() {
        // Arrange
        servidor.responder(200, """
                {"id": 12, "nombre": "Portatil", "estado": "prestado"}""");

        // Act
        boolean disponible = adaptador.estaDisponible(12);

        // Assert
        assertFalse(disponible);
    }

    @Test
    void estaDisponible_equipoNoExiste_devuelveFalso() {
        // Arrange
        servidor.responder(404, "");

        // Act
        boolean disponible = adaptador.estaDisponible(99);

        // Assert
        assertFalse(disponible);
        assertEquals("/api/v1/equipos/99", servidor.ultimaPeticionRecibida().ruta());
    }

    @Test
    void estaDisponible_errorDelServidor_lanzaServicioNoDisponible() {
        // Arrange
        servidor.responder(500, "");

        // Act
        var error = assertThrows(ServicioNoDisponible.class, () -> adaptador.estaDisponible(12));

        // Assert
        assertEquals("HTTP 500", error.getMessage());
    }

    @Test
    void estaDisponible_servidorCaido_lanzaServicioNoDisponible() {
        // Arrange
        String urlSinServidor = servidor.url();
        servidor.detener();
        var adaptadorSinServidor = new DisponibilidadEquiposHttp(urlSinServidor);

        // Act
        var error = assertThrows(ServicioNoDisponible.class, () -> adaptadorSinServidor.estaDisponible(12));

        // Assert
        assertNotNull(error.getCause());
        assertFalse(error.getMessage().isBlank());
    }
}
