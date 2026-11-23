package edu.unal.prestamos.domain;

public interface Notificador {
    void notificar(String estudianteId, String mensaje);
}
