package edu.unal.prestamos.domain;

public interface PrestamoRepository {
    int siguienteId();
    void guardar(Prestamo prestamo);
}
