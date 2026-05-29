package com.proyecto.pedidos.test;

import com.proyecto.pedidos.model.Cliente;
import com.proyecto.pedidos.model.Pedido;
import com.proyecto.pedidos.model.ProductoDigital;
import com.proyecto.pedidos.model.ProductoFisico;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de Pedido y Cliente")
public class PedidoTest {

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("CL-01", "Adrian Blas", "adrian@email.com", "España", 0, false);
    }

    // ===================== PEDIDO =====================

    @Test
    @DisplayName("TC-PE-01: calcularTotal con un ProductoFisico")
    void testTotalConProductoFisico() {
        Pedido pedido = new Pedido(cliente);
        pedido.agregarProducto(new ProductoFisico("Teclado", 100.0, 5.0));
        assertEquals(110.0, pedido.calcularTotalPedido(), 0.001);
    }

    @Test
    @DisplayName("TC-PE-02: calcularTotal con un ProductoDigital")
    void testTotalConProductoDigital() {
        Pedido pedido = new Pedido(cliente);
        pedido.agregarProducto(new ProductoDigital("Software", 200.0, "LIC-1"));
        assertEquals(242.0, pedido.calcularTotalPedido(), 0.001);
    }

    @Test
    @DisplayName("TC-PE-03: calcularTotal con productos mixtos")
    void testTotalConProductosMixtos() {
        Pedido pedido = new Pedido(cliente);
        pedido.agregarProducto(new ProductoFisico("Ratón", 50.0, 0.0));
        pedido.agregarProducto(new ProductoDigital("App", 100.0, "A1"));
        assertEquals(171.0, pedido.calcularTotalPedido(), 0.001);
    }

    @Test
    @DisplayName("TC-PE-04: Pedido vacío tiene total cero")
    void testTotalPedidoVacio() {
        Pedido pedido = new Pedido(cliente);
        assertEquals(0.0, pedido.calcularTotalPedido(), 0.001);
    }

    @Test
    @DisplayName("TC-PE-05: agregarProducto null no rompe el pedido")
    void testAgregarProductoNullNoLanzaExcepcion() {
        Pedido pedido = new Pedido(cliente);
        assertDoesNotThrow(() -> pedido.agregarProducto(null));
        assertEquals(0.0, pedido.calcularTotalPedido(), 0.001);
    }

    @Test
    @DisplayName("TC-PE-06: Total con producto NO es igual a cero")
    void testTotalConProductoNoEsCero() {
        Pedido pedido = new Pedido(cliente);
        pedido.agregarProducto(new ProductoFisico("Monitor", 200.0, 15.0));
        assertNotEquals(0.0, pedido.calcularTotalPedido());
    }

    @Test
    @DisplayName("TC-PE-07: Total no es negativo")
    void testTotalNuncaEsNegativo() {
        Pedido pedido = new Pedido(cliente);
        pedido.agregarProducto(new ProductoDigital("Tool", 30.0, "T1"));
        assertFalse(pedido.calcularTotalPedido() < 0);
    }

    @Test
    @DisplayName("TC-PE-08: mostrarResumen contiene el nombre del cliente")
    void testResumenContieneNombreCliente() {
        Pedido pedido = new Pedido(cliente);
        assertTrue(pedido.mostrarResumen().contains("Adrian Blas"));
    }

    @Test
    @DisplayName("TC-PE-10: mostrarResumen contiene el nombre del producto")
    void testResumenContieneProducto() {
        Pedido pedido = new Pedido(cliente);
        pedido.agregarProducto(new ProductoFisico("Teclado", 100.0, 5.0));
        assertTrue(pedido.mostrarResumen().contains("Teclado"));
    }

    @ParameterizedTest(name = "PrecioF={0}, EnvioF={1}, PrecioD={2} => Total={3}")
    @CsvSource({
        "100.0, 5.0,   0.0, 110.0",
        "0.0,   0.0,  200.0, 242.0",
        "100.0, 5.0,  100.0, 231.0",
        "50.0,  3.0,   50.0, 116.5",
        "0.0,   0.0,    0.0,   0.0"
    })
    @DisplayName("TC-PE-09: calcularTotal parametrizado")
    void testCalcularTotalParametrizado(double precioF, double envioF,
                                        double precioD,
                                        double esperado) {
        Pedido pedido = new Pedido(cliente);
        if (precioF > 0 || envioF > 0)
            pedido.agregarProducto(new ProductoFisico("Físico", precioF, envioF));
        if (precioD > 0)
            pedido.agregarProducto(new ProductoDigital("Digital", precioD, "LIC"));
        assertEquals(esperado, pedido.calcularTotalPedido(), 0.001);
    }

    // ===================== CLIENTE =====================

    @Test
    @DisplayName("TC-CL-01: Cliente devuelve nombre correcto")
    void testClienteNombre() {
        assertEquals("Adrian Blas", cliente.getNombre());
    }

    @Test
    @DisplayName("TC-CL-02: Cliente devuelve correo correcto")
    void testClienteCorreo() {
        assertEquals("adrian@email.com", cliente.getCorreo());
    }

    @Test
    @DisplayName("TC-CL-03: toString de cliente contiene los tres campos principales")
    void testClienteToString() {
        String resultado = cliente.toString();
        assertTrue(resultado.contains("Adrian Blas"));
        assertTrue(resultado.contains("adrian@email.com"));
        assertTrue(resultado.contains("CL-01"));
    }

    @Test
    @DisplayName("TC-CL-04: setNombre actualiza el nombre")
    void testSetNombre() {
        cliente.setNombre("Nuevo Nombre");
        assertNotEquals("Adrian Blas", cliente.getNombre());
        assertEquals("Nuevo Nombre", cliente.getNombre());
    }

    @Test
    @DisplayName("TC-CL-05: setCorreo actualiza el correo")
    void testSetCorreo() {
        cliente.setCorreo("nuevo@email.com");
        assertEquals("nuevo@email.com", cliente.getCorreo());
    }

    @Test
    @DisplayName("TC-CL-06: setPais actualiza el país")
    void testSetPais() {
        cliente.setPais("Francia");
        assertEquals("Francia", cliente.getPais());
    }

    @Test
    @DisplayName("TC-CL-07: getPais devuelve el país correcto")
    void testGetPais() {
        assertEquals("España", cliente.getPais());
    }
}
