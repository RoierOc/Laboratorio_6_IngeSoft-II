package edu.unal.prestamos.domain;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraMultaTest {
    private static final LocalDate LIMITE = LocalDate.of(2026, 11, 25);
    private final CalculadoraMulta calculadora = new CalculadoraMulta();

    @Test
    void calcular_devueltoAntesDelLimite_noHayMulta() {
        LocalDate fechaDevolucion = LIMITE.minusDays(1);

        int multa = calculadora.calcular(LIMITE, fechaDevolucion, false);

        assertEquals(0, multa);
    }
}
