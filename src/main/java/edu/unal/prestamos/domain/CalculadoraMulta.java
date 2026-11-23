package edu.unal.prestamos.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class CalculadoraMulta {
    public int calcular(LocalDate fechaLimite, LocalDate fechaDevolucion, boolean esMonitor) {
        long dias = Math.max(0, ChronoUnit.DAYS.between(fechaLimite, fechaDevolucion));
        int multa = (int) Math.min(50000, dias * 2000);
        return esMonitor ? multa / 2 : multa;
    }
}
