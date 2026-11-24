package edu.unal.prestamos.data;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/** Servidor HTTP real en localhost (HttpServer del JDK) para las pruebas de integración. */
final class ServidorHttpDePrueba {
    record PeticionRecibida(String metodo, String ruta) { }

    private final HttpServer servidor;
    private volatile int codigo = 200;
    private volatile String cuerpo = "";
    private volatile PeticionRecibida ultimaPeticion;

    private ServidorHttpDePrueba(HttpServer servidor) {
        this.servidor = servidor;
    }

    static ServidorHttpDePrueba iniciar() throws IOException {
        var http = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        var prueba = new ServidorHttpDePrueba(http);
        http.createContext("/", intercambio -> {
            prueba.ultimaPeticion = new PeticionRecibida(
                    intercambio.getRequestMethod(), intercambio.getRequestURI().getPath());
            byte[] bytes = prueba.cuerpo.getBytes(StandardCharsets.UTF_8);
            intercambio.getResponseHeaders().add("Content-Type", "application/json");
            intercambio.sendResponseHeaders(prueba.codigo, bytes.length == 0 ? -1 : bytes.length);
            try (var salida = intercambio.getResponseBody()) {
                salida.write(bytes);
            }
        });
        http.start();
        return prueba;
    }

    void responder(int codigo, String cuerpo) {
        this.codigo = codigo;
        this.cuerpo = cuerpo;
    }

    String url() {
        return "http://localhost:" + servidor.getAddress().getPort();
    }

    PeticionRecibida ultimaPeticionRecibida() {
        return ultimaPeticion;
    }

    void detener() {
        servidor.stop(0);
    }
}
