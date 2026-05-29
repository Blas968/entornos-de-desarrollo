package com.proyecto.pedidos.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.proyecto.pedidos.model.CalculadoraFinanciera;
import com.proyecto.pedidos.model.GestorInventario;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios y de regresión para CalculadoraFinanciera y GestorInventario.
 * Los tests marcados con [REGRESIÓN] verifican comportamiento corregido tras
 * el fallo en la prueba maestra de integración.
 */
@DisplayName("Tests Unitarios — CalculadoraFinanciera y GestorInventario")
class CalculadoraGestorRegressionTest {

    private CalculadoraFinanciera calc;
    private GestorInventario      gestor;

    @BeforeEach
    void setUp() {
        calc   = new CalculadoraFinanciera();
        gestor = new GestorInventario();
    }

    // ─── CalculadoraFinanciera ────────────────────────────────────────────────

    @Test
    @DisplayName("TC-CF-01: IVA GENERAL aplica el 21%")
    void testIvaGeneral() {
        assertEquals(121.0, calc.aplicarIVA(100.0, "GENERAL"), 0.001);
    }

    @Test
    @DisplayName("TC-CF-02: IVA REDUCIDO aplica el 10%")
    void testIvaReducido() {
        assertEquals(110.0, calc.aplicarIVA(100.0, "REDUCIDO"), 0.001);
    }

    @Test
    @DisplayName("TC-CF-03: IVA SUPER aplica el 4%")
    void testIvaSuper() {
        assertEquals(104.0, calc.aplicarIVA(100.0, "SUPER"), 0.001);
    }

    @Test
    @DisplayName("TC-CF-04: IVA tipo desconocido devuelve la base sin cambios")
    void testIvaDesconocidoDevuelveBase() {
        assertEquals(100.0, calc.aplicarIVA(100.0, "OTRO"), 0.001);
    }

    @Test
    @DisplayName("TC-CF-05: descuento fidelidad con 3 años y no VIP → 5%")
    void testDescuentoFidelidadBajoNoVip() {
        double resultado = calc.calcularDescuentoFidelidad(100.0, 3, false);
        assertEquals(95.0, resultado, 0.001);
    }

    @Test
    @DisplayName("TC-CF-06: descuento fidelidad con 6 años y no VIP → 10%")
    void testDescuentoFidelidadAltoNoVip() {
        double resultado = calc.calcularDescuentoFidelidad(100.0, 6, false);
        assertEquals(90.0, resultado, 0.001);
    }

    @Test
    @DisplayName("TC-CF-07: descuento fidelidad con VIP añade 5% extra")
    void testDescuentoFidelidadConVip() {
        double sinVip  = calc.calcularDescuentoFidelidad(100.0, 3, false); // 95.0
        double conVip  = calc.calcularDescuentoFidelidad(100.0, 3, true);  // 90.0
        assertTrue(conVip < sinVip, "VIP debe pagar menos");
    }

    @Test
    @DisplayName("TC-CF-08: envío gratuito si total > 100€")
    void testEnvioGratuitoConTotalAlto() {
        assertEquals(0.0, calc.calcularGastosEnvio(5.0, 150.0, "NACIONAL"), 0.001);
    }

    @Test
    @DisplayName("TC-CF-09: envío INTERNACIONAL aplica tarifa mayor")
    void testEnvioInternacionalMasCaro() {
        double nacional      = calc.calcularGastosEnvio(2.0, 50.0, "NACIONAL");
        double internacional = calc.calcularGastosEnvio(2.0, 50.0, "INTERNACIONAL");
        assertTrue(internacional > nacional);
    }

    @Test
    @DisplayName("TC-CF-10: comisión PAYPAL es 3% del total")
    void testComisionPaypal() {
        assertEquals(3.0, calc.calcularComisionPasarela(100.0, "PAYPAL"), 0.001);
    }

    @Test
    @DisplayName("TC-CF-11: comisión con otro método es fija 1.5€")
    void testComisionFija() {
        assertEquals(1.5, calc.calcularComisionPasarela(500.0, "TRANSFERENCIA"), 0.001);
    }

    @Test
    @DisplayName("[REGRESIÓN] TC-CF-12: obtenerPrecioFinalIntegrado devuelve base + envio")
    void testObtenerPrecioFinalIntegradoCorregido() {
        // Antes devolvía precioConDescuento (BUG). Ahora debe devolver base + envio.
        double resultado = calc.obtenerPrecioFinalIntegrado(24.2, 8.0, 1.21);
        assertEquals(32.2, resultado, 0.001,
                "REGRESIÓN: el método debe devolver base+envio=32.2, no 22.99");
    }

    @Test
    @DisplayName("[REGRESIÓN] TC-CF-13: envío NACIONAL con peso 2kg = 8.0€")
    void testMultiplicadorPesoCorregido() {
        // Antes MULTIPLICADOR_PESO=1.2 daba 7.4. Ahora con 1.5 debe dar 8.0.
        double envio = calc.calcularGastosEnvio(2.0, 50.0, "NACIONAL");
        assertEquals(8.0, envio, 0.001,
                "REGRESIÓN: multiplicador corregido a 1.5, envío debe ser 8.0");
    }

    @ParameterizedTest(name = "base={0}, tipo={1} → esperado={2}")
    @CsvSource({
        "100.0, GENERAL,  121.0",
        "200.0, REDUCIDO, 220.0",
        "50.0,  SUPER,     52.0",
        "0.0,   GENERAL,    0.0",
        "75.0,  REDUCIDO,  82.5"
    })
    @DisplayName("TC-CF-14: aplicarIVA parametrizado")
    void testAplicarIvaParametrizado(double base, String tipo, double esperado) {
        assertEquals(esperado, calc.aplicarIVA(base, tipo), 0.001);
    }

    // ─── GestorInventario ────────────────────────────────────────────────────

    @Test
    @DisplayName("TC-GI-01: darDeAltaProducto permite luego reservar")
    void testDarDeAltaYReservar() {
        gestor.darDeAltaProducto("PROD-01", 10);
        assertTrue(gestor.verificarYReservar("PROD-01", 5));
    }

    @Test
    @DisplayName("TC-GI-02: no se puede reservar si no hay stock suficiente")
    void testReservarSinStock() {
        gestor.darDeAltaProducto("PROD-02", 3);
        assertFalse(gestor.verificarYReservar("PROD-02", 10));
    }

    @Test
    @DisplayName("TC-GI-03: no se puede reservar un producto no registrado")
    void testReservarProductoInexistente() {
        assertFalse(gestor.verificarYReservar("NO-EXISTE", 1));
    }

    @Test
    @DisplayName("TC-GI-04: confirmarVenta reduce el stock correctamente")
    void testConfirmarVentaReduceStock() {
        gestor.darDeAltaProducto("PROD-03", 10);
        gestor.verificarYReservar("PROD-03", 5);
        gestor.confirmarVenta("PROD-03", 5);
        // Después de confirmar 5, solo quedan 5 → 5 es suficiente
        assertTrue(gestor.verificarYReservar("PROD-03", 4));
    }

    @Test
    @DisplayName("TC-GI-05: esProductoCritico devuelve true si stock < 5")
    void testEsProductoCritico() {
        gestor.darDeAltaProducto("PROD-04", 4);
        assertTrue(gestor.esProductoCritico("PROD-04"));
    }

    @Test
    @DisplayName("TC-GI-06: esProductoCritico devuelve false si stock >= 5")
    void testNoEsProductoCritico() {
        gestor.darDeAltaProducto("PROD-05", 10);
        assertFalse(gestor.esProductoCritico("PROD-05"));
    }

    @Test
    @DisplayName("TC-GI-07: darDeAlta dos veces suma el stock")
    void testDarDeAltaAcumula() {
        gestor.darDeAltaProducto("PROD-06", 5);
        gestor.darDeAltaProducto("PROD-06", 5);
        assertTrue(gestor.verificarYReservar("PROD-06", 9));
    }
}
