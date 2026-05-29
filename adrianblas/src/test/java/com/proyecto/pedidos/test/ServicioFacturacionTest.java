package com.proyecto.pedidos.test;

import com.proyecto.pedidos.model.CalculadoraFinanciera;
import com.proyecto.pedidos.model.GestorInventario;
import com.proyecto.pedidos.model.ServicioFacturacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios de los métodos auxiliares de ServicioFacturacion
 * y GestorInventario que no quedaban cubiertos por el resto de la suite.
 * Su objetivo es completar la cobertura de código (>85%).
 */
@DisplayName("Tests Unitarios — ServicioFacturacion y métodos auxiliares de inventario")
class ServicioFacturacionTest {

    private GestorInventario      gestor;
    private CalculadoraFinanciera calc;
    private ServicioFacturacion   servicio;

    @BeforeEach
    void setUp() {
        gestor   = new GestorInventario();
        calc     = new CalculadoraFinanciera();
        servicio = new ServicioFacturacion(gestor, calc);
    }

    // ─── ServicioFacturacion ──────────────────────────────────────────────────

    @Test
    @DisplayName("TC-SF-01: validarIntegridad true cuando subtotal + IVA = total")
    void testValidarIntegridadCorrecta() {
        assertTrue(servicio.validarIntegridad(100.0, 21.0, 121.0));
    }

    @Test
    @DisplayName("TC-SF-02: validarIntegridad false cuando los importes no cuadran")
    void testValidarIntegridadIncorrecta() {
        assertFalse(servicio.validarIntegridad(100.0, 21.0, 200.0));
    }

    @Test
    @DisplayName("TC-SF-03: emitirNotaCredito antepone el prefijo NC-")
    void testEmitirNotaCredito() {
        assertEquals("NC-FACT-123", servicio.emitirNotaCredito("FACT-123"));
    }

    @Test
    @DisplayName("TC-SF-04: estimarMargenBeneficio descuenta coste e IVA")
    void testEstimarMargenBeneficio() {
        // 100 - 30 - (100 * 0.21) = 49.0
        assertEquals(49.0, servicio.estimarMargenBeneficio(100.0, 30.0), 0.001);
    }

    @Test
    @DisplayName("TC-SF-05: archivarFactura se ejecuta sin lanzar excepción")
    void testArchivarFactura() {
        assertDoesNotThrow(() -> servicio.archivarFactura("FACT-999"));
    }

    @Test
    @DisplayName("TC-SF-06: procesarFacturaCompleta devuelve ERROR si no hay stock")
    void testProcesarFacturaSinStock() {
        String resultado = servicio.procesarFacturaCompleta("NO-EXISTE", 5, 10.0);
        assertTrue(resultado.contains("ERROR"));
    }

    // ─── GestorInventario.calcularImpactoRotura ───────────────────────────────

    @Test
    @DisplayName("TC-GI-08: calcularImpactoRotura devuelve 0 para producto inexistente")
    void testImpactoRoturaInexistente() {
        assertEquals(0.0, gestor.calcularImpactoRotura("NO-EXISTE", 100.0), 0.001);
    }

    @Test
    @DisplayName("TC-GI-09: calcularImpactoRotura aplica 5% si el faltante supera 10 uds")
    void testImpactoRoturaAlto() {
        gestor.darDeAltaProducto("P-02", 20);
        gestor.verificarYReservar("P-02", 5); // faltante = |20-5| = 15 > 10
        assertEquals(5.0, gestor.calcularImpactoRotura("P-02", 100.0), 0.001);
    }

    @Test
    @DisplayName("TC-GI-10: calcularImpactoRotura devuelve 0 si el faltante no supera 10 uds")
    void testImpactoRoturaBajo() {
        gestor.darDeAltaProducto("P-03", 10);
        gestor.verificarYReservar("P-03", 5); // faltante = |10-5| = 5 <= 10
        assertEquals(0.0, gestor.calcularImpactoRotura("P-03", 100.0), 0.001);
    }
}
