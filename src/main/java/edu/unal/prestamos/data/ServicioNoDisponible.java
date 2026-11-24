package edu.unal.prestamos.data;

public final class ServicioNoDisponible extends RuntimeException {
    public ServicioNoDisponible(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public ServicioNoDisponible(String mensaje) {
        super(mensaje);
    }
}
