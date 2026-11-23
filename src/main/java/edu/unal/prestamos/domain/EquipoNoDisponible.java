package edu.unal.prestamos.domain;

public final class EquipoNoDisponible extends RuntimeException {
    public EquipoNoDisponible(int equipoId) {
        super("Equipo " + equipoId + " no disponible");
    }
}
