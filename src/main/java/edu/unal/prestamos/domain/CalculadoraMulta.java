package edu.unal.prestamos.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class CalculadoraMulta {
    public int calcular(LocalDate fechaLimite, LocalDate fechaDevolucion, boolean esMonitor) {
        long dias = Math.max(0, ChronoUnit.DAYS.between(fechaLimite, fechaDevolucion));
        return (int) Math.min(50000, dias * 2000);
    }
}
