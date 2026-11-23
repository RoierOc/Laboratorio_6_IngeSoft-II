package edu.unal.prestamos.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class CalculadoraMulta {
    private static final int MULTA_POR_DIA = 2_000;
    private static final int MULTA_MAXIMA = 50_000;
    private static final int DIVISOR_MONITOR = 2;

    public int calcular(LocalDate fechaLimite, LocalDate fechaDevolucion, boolean esMonitor) {
        long dias = Math.max(0, ChronoUnit.DAYS.between(fechaLimite, fechaDevolucion));
        int multa = (int) Math.min(MULTA_MAXIMA, dias * MULTA_POR_DIA);
        return esMonitor ? multa / DIVISOR_MONITOR : multa;
    }
}
