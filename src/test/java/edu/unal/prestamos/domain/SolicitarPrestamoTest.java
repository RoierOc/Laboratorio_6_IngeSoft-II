package edu.unal.prestamos.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SolicitarPrestamoTest {
    private final DisponibilidadEquipos disponibilidad = mock(DisponibilidadEquipos.class);
    private final PrestamoRepository repositorio = mock(PrestamoRepository.class);
    private final Notificador notificador = mock(Notificador.class);
    private final SolicitarPrestamo casoDeUso = new SolicitarPrestamo(disponibilidad, repositorio, notificador);

    @Test
    void ejecutar_equipoDisponible_guardaYNotifica() {
        // Arrange
        when(disponibilidad.estaDisponible(12)).thenReturn(true);
        when(repositorio.siguienteId()).thenReturn(50);

        // Act
        Prestamo prestamo = casoDeUso.ejecutar("1023", 12);

        // Assert
        assertEquals(EstadoPrestamo.PENDIENTE, prestamo.estado());
        assertEquals(50, prestamo.id());
        var orden = inOrder(repositorio, notificador);
        orden.verify(repositorio).guardar(prestamo);
        orden.verify(notificador).notificar("1023", "Solicitud 50 registrada");
    }

    @Test
    void ejecutar_equipoNoDisponible_noGuardaNiNotifica() {
        // Arrange
        when(disponibilidad.estaDisponible(12)).thenReturn(false);

        // Act
        var error = assertThrows(EquipoNoDisponible.class, () -> casoDeUso.ejecutar("1023", 12));

        // Assert
        assertEquals("Equipo 12 no disponible", error.getMessage());
        verifyNoInteractions(repositorio, notificador);
    }

    @Test
    void ejecutar_falloAlGuardar_noNotifica() {
        // Arrange
        when(disponibilidad.estaDisponible(12)).thenReturn(true);
        when(repositorio.siguienteId()).thenReturn(50);
        var fallo = new IllegalStateException("BD caída");
        doThrow(fallo).when(repositorio).guardar(any(Prestamo.class));

        // Act
        var error = assertThrows(IllegalStateException.class, () -> casoDeUso.ejecutar("1023", 12));

        // Assert
        assertSame(fallo, error);
        verify(repositorio).guardar(any(Prestamo.class));
        verifyNoInteractions(notificador);
    }
}
