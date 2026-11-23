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

    @Test
    void calcular_devueltoElDiaLimite_noHayMulta() {
        LocalDate fechaDevolucion = LIMITE;

        int multa = calculadora.calcular(LIMITE, fechaDevolucion, false);

        assertEquals(0, multa);
    }

    @Test
    void calcular_unDiaDeRetraso_cobraDosMil() {
        LocalDate fechaDevolucion = LIMITE.plusDays(1);

        int multa = calculadora.calcular(LIMITE, fechaDevolucion, false);

        assertEquals(2000, multa);
    }

    @Test
    void calcular_veinticincoDias_alcanzaElTope() {
        LocalDate fechaDevolucion = LIMITE.plusDays(25);

        int multa = calculadora.calcular(LIMITE, fechaDevolucion, false);

        assertEquals(50000, multa);
    }

    @Test
    void calcular_cuarentaDias_aplicaElTope() {
        LocalDate fechaDevolucion = LIMITE.plusDays(40);

        int multa = calculadora.calcular(LIMITE, fechaDevolucion, false);

        assertEquals(50000, multa);
    }

    @Test
    void calcular_monitorConTresDias_pagaLaMitad() {
        LocalDate fechaDevolucion = LIMITE.plusDays(3);

        int multa = calculadora.calcular(LIMITE, fechaDevolucion, true);

        assertEquals(3000, multa);
    }

    @Test
    void calcular_monitorConCuarentaDias_descuentaDespuesDelTope() {
        LocalDate fechaDevolucion = LIMITE.plusDays(40);

        int multa = calculadora.calcular(LIMITE, fechaDevolucion, true);

        assertEquals(25000, multa);
    }
}
