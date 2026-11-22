package edu.unal.prestamos.domain;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrestamoTest {
    private static final LocalDate HOY = LocalDate.of(2026, 11, 18);

    @Test
    void aprobar_pendiente_asignaLimiteEnSieteDias() {
        // Arrange
        var prestamo = new Prestamo(1, "1023", 12);

        // Act
        prestamo.aprobar(HOY);

        // Assert
        assertEquals(EstadoPrestamo.APROBADO, prestamo.estado());
        assertEquals(LocalDate.of(2026, 11, 25), prestamo.fechaLimite());
    }

    @Test
    void renovar_segundaVez_lanzaError() {
        // Arrange
        var prestamo = new Prestamo(1, "1023", 12);
        prestamo.aprobar(HOY);
        prestamo.renovar();

        // Act
        var error = assertThrows(IllegalStateException.class, prestamo::renovar);
        // Assert
        assertEquals("El préstamo ya fue renovado", error.getMessage());
    }

    @Test
    void renovar_aprobado_extiendeLimiteSieteDias() {
        // Arrange
        var prestamo = new Prestamo(1, "1023", 12);
        prestamo.aprobar(HOY);

        // Act
        prestamo.renovar();

        // Assert
        assertEquals(LocalDate.of(2026, 12, 2), prestamo.fechaLimite());
        assertEquals(1, prestamo.renovaciones());
    }

    @Test
    void aprobar_yaAprobado_lanzaError() {
        // Arrange
        var prestamo = new Prestamo(1, "1023", 12);
        prestamo.aprobar(HOY);

        // Act
        var error = assertThrows(IllegalStateException.class, () -> prestamo.aprobar(HOY));
        // Assert
        assertEquals("Solo se aprueba un préstamo pendiente", error.getMessage());
    }

    @Test
    void renovar_pendiente_lanzaError() {
        // Arrange
        var prestamo = new Prestamo(1, "1023", 12);

        // Act
        var error = assertThrows(IllegalStateException.class, prestamo::renovar);
        // Assert
        assertEquals("Solo se renueva un préstamo aprobado", error.getMessage());
    }

    @Test
    void devolver_aprobado_cambiaEstado() {
        // Arrange
        var prestamo = new Prestamo(1, "1023", 12);
        prestamo.aprobar(HOY);

        // Act
        prestamo.devolver();

        // Assert
        assertEquals(EstadoPrestamo.DEVUELTO, prestamo.estado());
    }

    @Test
    void devolver_yaDevuelto_lanzaError() {
        // Arrange
        var prestamo = new Prestamo(1, "1023", 12);
        prestamo.aprobar(HOY);
        prestamo.devolver();

        // Act
        var error = assertThrows(IllegalStateException.class, prestamo::devolver);
        // Assert
        assertEquals("Solo se devuelve un préstamo aprobado", error.getMessage());
    }
}
