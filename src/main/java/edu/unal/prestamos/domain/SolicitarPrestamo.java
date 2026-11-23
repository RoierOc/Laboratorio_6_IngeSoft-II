package edu.unal.prestamos.domain;

public final class SolicitarPrestamo {
    private final DisponibilidadEquipos disponibilidad;
    private final PrestamoRepository repositorio;
    private final Notificador notificador;

    public SolicitarPrestamo(DisponibilidadEquipos disponibilidad, PrestamoRepository repositorio, Notificador notificador) {
        this.disponibilidad = disponibilidad;
        this.repositorio = repositorio;
        this.notificador = notificador;
    }

    public Prestamo ejecutar(String estudianteId, int equipoId) {
        if (!disponibilidad.estaDisponible(equipoId)) {
            throw new EquipoNoDisponible(equipoId);
        }
        var prestamo = new Prestamo(repositorio.siguienteId(), estudianteId, equipoId);
        repositorio.guardar(prestamo);
        notificador.notificar(estudianteId, "Solicitud " + prestamo.id() + " registrada");
        return prestamo;
    }
}
