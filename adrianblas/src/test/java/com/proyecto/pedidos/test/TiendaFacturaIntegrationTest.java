package com.proyecto.pedidos.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.proyecto.pedidos.model.Cliente;
import com.proyecto.pedidos.model.Factura;
import com.proyecto.pedidos.model.Pedido;
import com.proyecto.pedidos.model.Tienda;
import com.proyecto.pedidos.model.ProductoDigital;
import com.proyecto.pedidos.model.ProductoFisico;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de Integración y Sistema para Tienda y Factura.
 * Cubre el flujo E2E completo (compra → factura) y pruebas de robustez.
 *
 * Trazabilidad de requisitos:
 * - RF-01: Calcular total pedido con productos mixtos
 * - RF-02: Aplicar descuento de fidelidad según cliente
 * - RF-03: Calcular envío por país
 * - RF-04: Validar pedido vacío
 * - RF-05: Generar factura con desglose correcto
 */
@DisplayName("Tests de Integración y Sistema — Tienda y Factura")
class TiendaFacturaIntegrationTest {

    private Tienda  tienda;
    private Cliente clienteEspana;
    private Cliente clienteVip;
    private Cliente clienteExtranjero;

    @BeforeEach
    void setUp() {
        tienda = new Tienda();

        clienteEspana    = new Cliente("C-01", "Ana García",  "ana@email.com",
                                       "España",  3, false);
        clienteVip       = new Cliente("C-02", "Luis Pérez",  "luis@email.com",
                                       "España",  6, true);
        clienteExtranjero= new Cliente("C-03", "John Smith",  "john@email.com",
                                       "Alemania", 2, false);
    }

    // ─── Tests de Integración Tienda ↔ Pedido ↔ Factura ─────────────────────

    @Test
    @DisplayName("IT-01: realizarVenta genera una Factura no nula")
    void testRealizarVentaGeneraFactura() {
        Pedido pedido = new Pedido(clienteEspana);
        pedido.agregarProducto(new ProductoFisico("Libro", 30.0, 1.5));

        Factura factura = tienda.realizarVenta(clienteEspana, pedido);

        assertNotNull(factura);
    }

    @Test
    @DisplayName("IT-02: la factura tiene código generado automáticamente")
    void testFacturaTieneCodigoAutogenerado() {
        Pedido pedido = new Pedido(clienteEspana);
        pedido.agregarProducto(new ProductoDigital("App", 50.0, "A-01"));

        Factura factura = tienda.realizarVenta(clienteEspana, pedido);

        assertNotNull(factura.getCodigoFactura());
        assertTrue(factura.getCodigoFactura().startsWith("FACT-"));
    }

    @Test
    @DisplayName("IT-03: dos ventas generan códigos de factura distintos")
    void testDosVentasCodigosDiferentes() {
        Pedido p1 = new Pedido(clienteEspana);
        p1.agregarProducto(new ProductoFisico("Libro", 30.0, 1.0));
        Pedido p2 = new Pedido(clienteEspana);
        p2.agregarProducto(new ProductoDigital("App", 50.0, "A-01"));

        Factura f1 = tienda.realizarVenta(clienteEspana, p1);
        Factura f2 = tienda.realizarVenta(clienteEspana, p2);

        assertNotEquals(f1.getCodigoFactura(), f2.getCodigoFactura());
    }

    @Test
    @DisplayName("IT-04: totalFinal es mayor que cero con productos válidos")
    void testTotalFinalPositivo() {
        Pedido pedido = new Pedido(clienteEspana);
        pedido.agregarProducto(new ProductoFisico("Monitor", 200.0, 3.0));

        Factura factura = tienda.realizarVenta(clienteEspana, pedido);

        assertTrue(factura.getTotalFinal() > 0);
    }

    @Test
    @DisplayName("IT-05: cliente VIP con >5 años obtiene mayor descuento que cliente normal")
    void testClienteVipTieneMayorDescuento() {
        Pedido pNormal = new Pedido(clienteEspana);
        pNormal.agregarProducto(new ProductoDigital("App", 100.0, "A-01"));

        Pedido pVip = new Pedido(clienteVip);
        pVip.agregarProducto(new ProductoDigital("App", 100.0, "A-01"));

        Factura fNormal = tienda.realizarVenta(clienteEspana, pNormal);
        Factura fVip    = tienda.realizarVenta(clienteVip,    pVip);

        assertTrue(fVip.getTotalDescuento() > fNormal.getTotalDescuento(),
                "El cliente VIP con más antigüedad debe tener mayor descuento");
    }

    @Test
    @DisplayName("IT-06: cliente en España no paga envío por productos digitales")
    void testProductoDigitalSinEnvio() {
        Pedido pedido = new Pedido(clienteEspana);
        pedido.agregarProducto(new ProductoDigital("Curso", 50.0, "C-01"));

        Factura factura = tienda.realizarVenta(clienteEspana, pedido);

        assertEquals(0.0, factura.getTotalEnvio(), 0.001);
    }

    @Test
    @DisplayName("IT-07: cliente en Alemania paga 10€ de envío por producto físico")
    void testEnvioExtranjeroLejano() {
        Pedido pedido = new Pedido(clienteExtranjero);
        pedido.agregarProducto(new ProductoFisico("Teclado", 80.0, 1.0));

        Factura factura = tienda.realizarVenta(clienteExtranjero, pedido);

        assertEquals(10.0, factura.getTotalEnvio(), 0.001);
    }

    @Test
    @DisplayName("IT-08: cliente en España paga 0€ de envío por producto físico")
    void testEnvioEspana() {
        Pedido pedido = new Pedido(clienteEspana);
        pedido.agregarProducto(new ProductoFisico("Libro", 30.0, 1.5));

        Factura factura = tienda.realizarVenta(clienteEspana, pedido);

        assertEquals(0.0, factura.getTotalEnvio(), 0.001);
    }

    @Test
    @DisplayName("IT-09: la factura incluye fecha de emisión")
    void testFacturaTieneFechaEmision() {
        Pedido pedido = new Pedido(clienteEspana);
        pedido.agregarProducto(new ProductoDigital("eBook", 20.0, "E-01"));

        Factura factura = tienda.realizarVenta(clienteEspana, pedido);

        assertNotNull(factura.getFechaEmision());
    }

    @Test
    @DisplayName("IT-10: desglose de factura contiene TOTAL FINAL")
    void testDesgloseTieneContenidoCorrecto() {
        Pedido pedido = new Pedido(clienteEspana);
        pedido.agregarProducto(new ProductoFisico("Silla", 150.0, 2.0));

        Factura factura = tienda.realizarVenta(clienteEspana, pedido);

        assertTrue(factura.generarDesglose().contains("TOTAL FINAL"));
        assertTrue(factura.generarDesglose().contains("FACT-"));
    }

    // ─── Test E2E: flujo completo compra → factura ────────────────────────────

    @Test
    @DisplayName("E2E-01: flujo completo — cliente realiza pedido mixto y recibe factura")
    void testFlujoE2ECompleto() {
        // 1. Cliente
        Cliente cliente = new Cliente("C-99", "María López", "maria@email.com",
                                      "Francia", 7, true);
        // 2. Pedido con productos mixtos
        Pedido pedido = new Pedido(cliente);
        pedido.agregarProducto(new ProductoFisico("Libro Java", 30.0, 1.5));
        pedido.agregarProducto(new ProductoDigital("Curso Online", 50.0, "CUR-01"));
        pedido.agregarProducto(new ProductoFisico("Ratón", 25.0, 0.3));

        // 3. Tienda procesa la venta
        Factura factura = tienda.realizarVenta(cliente, pedido);

        // 4. Verificaciones de integridad
        assertNotNull(factura);
        assertTrue(factura.getCodigoFactura().startsWith("FACT-"));
        assertNotNull(factura.getFechaEmision());
        assertTrue(factura.getTotalNeto() > 0,     "totalNeto debe ser positivo");
        assertTrue(factura.getTotalIva() > 0,      "totalIva debe ser positivo");
        assertTrue(factura.getTotalDescuento() > 0,"VIP+7años debe tener descuento");
        assertEquals(5.0, factura.getTotalEnvio(), 0.001); // Francia → 5€
        assertTrue(factura.getTotalFinal() > 0,    "totalFinal debe ser positivo");
        assertTrue(factura.generarDesglose().contains("TOTAL FINAL"));
    }

    // ─── Tests de Robustez ───────────────────────────────────────────────────

    @Test
    @DisplayName("RB-01: realizarVenta lanza excepción si el pedido está vacío")
    void testRobustezPedidoVacio() {
        Pedido pedido = new Pedido(clienteEspana);
        assertThrows(IllegalStateException.class,
                () -> tienda.realizarVenta(clienteEspana, pedido));
    }

    @Test
    @DisplayName("RB-02: crear ProductoFisico con precio negativo lanza excepción")
    void testRobustezPrecioNegativoFisico() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProductoFisico("Malo", -100.0, 1.0));
    }

    @Test
    @DisplayName("RB-03: crear ProductoDigital con precio negativo lanza excepción")
    void testRobustezPrecioNegativoDigital() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProductoDigital("Malo", -50.0, "X"));
    }

    @Test
    @DisplayName("RB-04: agregar null al pedido no lanza excepción")
    void testRobustezAgregarNullNoPeta() {
        Pedido pedido = new Pedido(clienteEspana);
        assertDoesNotThrow(() -> pedido.agregarProducto(null));
    }

    @Test
    @DisplayName("RB-05: totalFinal nunca es negativo aunque haya descuento grande")
    void testRobustezTotalNoNegativo() {
        Cliente clienteSuperVip = new Cliente("C-MAX", "Super VIP", "super@email.com",
                                              "España", 100, true);
        Pedido pedido = new Pedido(clienteSuperVip);
        pedido.agregarProducto(new ProductoDigital("App", 1.0, "A-01"));

        Factura factura = tienda.realizarVenta(clienteSuperVip, pedido);

        assertFalse(factura.getTotalFinal() < 0,
                "El total final no puede ser negativo");
    }
}
