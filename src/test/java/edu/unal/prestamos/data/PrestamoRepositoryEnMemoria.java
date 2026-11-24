package edu.unal.prestamos.data;

import edu.unal.prestamos.domain.Prestamo;
import edu.unal.prestamos.domain.PrestamoRepository;
import java.util.ArrayList;
import java.util.List;

/** Fake: implementación simple que funciona, con una lista y un contador. */
final class PrestamoRepositoryEnMemoria implements PrestamoRepository {
    private final List<Prestamo> prestamos = new ArrayList<>();
    private int contador = 0;

    @Override
    public int siguienteId() {
        return ++contador;
    }

    @Override
    public void guardar(Prestamo prestamo) {
        prestamos.add(prestamo);
    }

    List<Prestamo> todos() {
        return List.copyOf(prestamos);
    }
}
