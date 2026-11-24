package edu.unal.prestamos.data;

import edu.unal.prestamos.domain.EquipoNoDisponible;
import edu.unal.prestamos.domain.EstadoPrestamo;
import edu.unal.prestamos.domain.Notificador;
import edu.unal.prestamos.domain.SolicitarPrestamo;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SolicitarPrestamoIntegracionTest {
    private ServidorHttpDePrueba servidor;
    private PrestamoRepositoryEnMemoria repositorio;
    private Notificador notificador;
    private SolicitarPrestamo casoDeUso;

    @BeforeEach
    void preparar() throws IOException {
        servidor = ServidorHttpDePrueba.iniciar();
        repositorio = new PrestamoRepositoryEnMemoria();
        notificador = mock(Notificador.class);
        casoDeUso = new SolicitarPrestamo(
                new DisponibilidadEquiposHttp(servidor.url()), repositorio, notificador);
    }

    @AfterEach
    void detenerServidor() {
        servidor.detener();
    }

    @Test
    void ejecutar_equipoDisponibleEnLaApi_guardaEnElRepositorioYNotifica() {
        // Arrange
        servidor.responder(200, """
                {"id": 12, "nombre": "Portatil", "estado": "disponible"}""");

        // Act
        var prestamo = casoDeUso.ejecutar("1023", 12);

        // Assert
        assertEquals(1, repositorio.todos().size());
        var guardado = repositorio.todos().get(0);
        assertSame(prestamo, guardado);
        assertEquals(1, guardado.id());
        assertEquals("1023", guardado.estudianteId());
        assertEquals(12, guardado.equipoId());
        assertEquals(EstadoPrestamo.PENDIENTE, guardado.estado());
        assertEquals("/api/v1/equipos/12", servidor.ultimaPeticionRecibida().ruta());
        verify(notificador).notificar("1023", "Solicitud 1 registrada");
    }

    @Test
    void ejecutar_equipoPrestadoEnLaApi_noGuardaNiNotifica() {
        // Arrange
        servidor.responder(200, """
                {"id": 12, "nombre": "Portatil", "estado": "prestado"}""");

        // Act
        assertThrows(EquipoNoDisponible.class, () -> casoDeUso.ejecutar("1023", 12));

        // Assert
        assertTrue(repositorio.todos().isEmpty());
        verifyNoInteractions(notificador);
    }
}
