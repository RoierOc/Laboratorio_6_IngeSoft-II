package edu.unal.prestamos.domain;

import java.time.LocalDate;

public final class Prestamo {
    private final int id;
    private final String estudianteId;
    private final int equipoId;
    private EstadoPrestamo estado = EstadoPrestamo.PENDIENTE;
    private LocalDate fechaLimite;
    private int renovaciones;

    public Prestamo(int id, String estudianteId, int equipoId) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.equipoId = equipoId;
    }

    public int id() { return id; }
    public String estudianteId() { return estudianteId; }
    public int equipoId() { return equipoId; }
    public EstadoPrestamo estado() { return estado; }
    public LocalDate fechaLimite() { return fechaLimite; }
    public int renovaciones() { return renovaciones; }

    public void aprobar(LocalDate hoy) {
        if (estado != EstadoPrestamo.PENDIENTE) {
            throw new IllegalStateException("Solo se aprueba un préstamo pendiente");
        }
        fechaLimite = hoy.plusDays(7);
        estado = EstadoPrestamo.APROBADO;
    }

    public void renovar() {
        if (estado != EstadoPrestamo.APROBADO) {
            throw new IllegalStateException("Solo se renueva un préstamo aprobado");
        }
        if (renovaciones >= 1) {
            throw new IllegalStateException("El préstamo ya fue renovado");
        }
        fechaLimite = fechaLimite.plusDays(7);
        renovaciones++;
    }

    public void devolver() {
        if (estado != EstadoPrestamo.APROBADO) {
            throw new IllegalStateException("Solo se devuelve un préstamo aprobado");
        }
        estado = EstadoPrestamo.DEVUELTO;
    }
}
