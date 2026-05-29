package com.proyecto.pedidos.test;

import com.proyecto.pedidos.model.CalculadoraFinanciera;
import com.proyecto.pedidos.model.GestorInventario;
import com.proyecto.pedidos.model.ServicioFacturacion;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Pruebas de Integración - Módulo Facturación")
class SistemaIntegracionTest {

    private GestorInventario      inventario;
    private CalculadoraFinanciera calculadora;
    private ServicioFacturacion   servicio;

    @BeforeEach
    void setUp() {
        inventario  = new GestorInventario();
        calculadora = new CalculadoraFinanciera();
        servicio    = new ServicioFacturacion(inventario, calculadora);
        inventario.darDeAltaProducto("PROD-01", 100);
    }

    @Test
    @DisplayName("Test 1: Integración Exitosa - Verificación de reserva y flujo básico")
    void testFlujoReservaVenta() {
        boolean reservaExitosa = inventario.verificarYReservar("PROD-01", 10);
        assertTrue(reservaExitosa, "El inventario debería permitir la reserva");

        String resultado = servicio.procesarFacturaCompleta("PROD-01", 5, 10.0);
        assertFalse(resultado.contains("ERROR"), "La factura debería haberse generado");
    }

    @Test
    @DisplayName("Test 2: Cálculo de precio con envío")
    void testCalculoPrecioFinalConEnvio() {
        String resultado = servicio.procesarFacturaCompleta("PROD-01", 2, 10.0);
        assertTrue(resultado.contains("32.2"),
                "La factura da un resultado incorrecto. Resultado obtenido: " + resultado);
    }
}
