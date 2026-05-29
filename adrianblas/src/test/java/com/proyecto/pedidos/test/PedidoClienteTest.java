package com.proyecto.pedidos.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.proyecto.pedidos.model.Cliente;
import com.proyecto.pedidos.model.Pedido;
import com.proyecto.pedidos.model.ProductoDigital;
import com.proyecto.pedidos.model.ProductoFisico;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests Unitarios — Pedido y Cliente")
class PedidoClienteTest {

    private Cliente cliente;
    private Pedido  pedido;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("C-01", "Adrian Blas", "adrian@email.com",
                              "España", 3, false);
        pedido  = new Pedido(cliente);
    }

    // ─── Pedido ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TC-PE-01: pedido vacío tiene total cero")
    void testPedidoVacioTotalCero() {
        assertEquals(0.0, pedido.calcularTotalPedido(), 0.001);
    }

    @Test
    @DisplayName("TC-PE-02: total con un ProductoFisico")
    void testTotalConFisico() {
        pedido.agregarProducto(new ProductoFisico("Libro", 30.0, 1.5));
        assertEquals(33.0, pedido.calcularTotalPedido(), 0.001);
    }

    @Test
    @DisplayName("TC-PE-03: total con un ProductoDigital")
    void testTotalConDigital() {
        pedido.agregarProducto(new ProductoDigital("Curso", 50.0, "C-01"));
        assertEquals(60.5, pedido.calcularTotalPedido(), 0.001);
    }

    @Test
    @DisplayName("TC-PE-04: total con productos mixtos")
    void testTotalMixto() {
        pedido.agregarProducto(new ProductoFisico("Libro", 30.0, 1.5));
        pedido.agregarProducto(new ProductoDigital("Curso", 50.0, "C-01"));
        assertEquals(93.5, pedido.calcularTotalPedido(), 0.001);
    }

    @Test
    @DisplayName("TC-PE-05: agregarProducto null no rompe el pedido")
    void testAgregarNullNoExplota() {
        assertDoesNotThrow(() -> {
            pedido.agregarProducto(null);
            pedido.calcularTotalPedido();
        });
    }

    @Test
    @DisplayName("TC-PE-06: eliminarProducto quita el producto del total")
    void testEliminarProducto() {
        ProductoFisico libro = new ProductoFisico("Libro", 30.0, 1.5);
        pedido.agregarProducto(libro);
        pedido.eliminarProducto(libro);
        assertEquals(0.0, pedido.calcularTotalPedido(), 0.001);
    }

    @Test
    @DisplayName("TC-PE-07: total no es negativo con productos válidos")
    void testTotalNoNegativo() {
        pedido.agregarProducto(new ProductoDigital("App", 10.0, "A-01"));
        assertFalse(pedido.calcularTotalPedido() < 0);
    }

    @Test
    @DisplayName("TC-PE-08: total con producto NO es cero")
    void testTotalConProductoNoEsCero() {
        pedido.agregarProducto(new ProductoFisico("Monitor", 200.0, 3.0));
        assertNotEquals(0.0, pedido.calcularTotalPedido());
    }

    @Test
    @DisplayName("TC-PE-09: validarPedido lanza excepción con lista vacía")
    void testValidarPedidoVacioLanzaExcepcion() {
        assertThrows(IllegalStateException.class, () -> pedido.validarPedido());
    }

    @Test
    @DisplayName("TC-PE-10: validarPedido no lanza excepción con productos")
    void testValidarPedidoConProductosNoLanza() {
        pedido.agregarProducto(new ProductoFisico("Item", 10.0, 0.5));
        assertDoesNotThrow(() -> pedido.validarPedido());
    }

    @Test
    @DisplayName("TC-PE-11: mostrarResumen contiene el nombre del cliente")
    void testResumenContieneCliente() {
        assertTrue(pedido.mostrarResumen().contains("Adrian Blas"));
    }

    @Test
    @DisplayName("TC-PE-12: mostrarResumen contiene el nombre del producto")
    void testResumenContieneProducto() {
        pedido.agregarProducto(new ProductoFisico("Teclado", 80.0, 1.0));
        assertTrue(pedido.mostrarResumen().contains("Teclado"));
    }

    @ParameterizedTest(name = "baseF={0}, pesoF={1}, baseD={2} → esperado={3}")
    @CsvSource({
        "30.0, 1.5, 50.0,  93.5",
        "0.0,  0.0, 100.0, 121.0",
        "100.0, 2.0, 0.0,  104.0",
        "50.0, 0.0, 200.0, 292.0",
        "0.0,  0.0,   0.0,   0.0"
    })
    @DisplayName("TC-PE-13: calcularTotalPedido parametrizado")
    void testTotalParametrizado(double baseF, double pesoF, double baseD, double esperado) {
        if (baseF > 0 || pesoF > 0)
            pedido.agregarProducto(new ProductoFisico("Físico", baseF, pesoF));
        if (baseD > 0)
            pedido.agregarProducto(new ProductoDigital("Digital", baseD, "D-01"));
        assertEquals(esperado, pedido.calcularTotalPedido(), 0.001);
    }

    // ─── Cliente ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TC-CL-01: getNombre devuelve el nombre correcto")
    void testClienteGetNombre() {
        assertEquals("Adrian Blas", cliente.getNombre());
    }

    @Test
    @DisplayName("TC-CL-02: getCorreo devuelve el correo correcto")
    void testClienteGetCorreo() {
        assertEquals("adrian@email.com", cliente.getCorreo());
    }

    @Test
    @DisplayName("TC-CL-03: getPais devuelve el país correcto")
    void testClienteGetPais() {
        assertEquals("España", cliente.getPais());
    }

    @Test
    @DisplayName("TC-CL-04: isEsVip devuelve false para cliente sin VIP")
    void testClienteNoVip() {
        assertFalse(cliente.isEsVip());
    }

    @Test
    @DisplayName("TC-CL-05: getAñosAntiguedad devuelve el valor asignado")
    void testClienteAntiguedad() {
        assertEquals(3, cliente.getAñosAntiguedad());
    }

    @Test
    @DisplayName("TC-CL-06: setEsVip actualiza el estado VIP")
    void testClienteSetVip() {
        cliente.setEsVip(true);
        assertTrue(cliente.isEsVip());
    }

    @Test
    @DisplayName("TC-CL-07: toString contiene nombre e id")
    void testClienteToString() {
        String s = cliente.toString();
        assertTrue(s.contains("Adrian Blas"));
        assertTrue(s.contains("C-01"));
    }
}
