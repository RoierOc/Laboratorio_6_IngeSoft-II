package edu.unal.prestamos.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class CalculadoraMulta {
    public int calcular(LocalDate fechaLimite, LocalDate fechaDevolucion, boolean esMonitor) {
        return (int) Math.max(0, ChronoUnit.DAYS.between(fechaLimite, fechaDevolucion)) * 2000;
    }
}
