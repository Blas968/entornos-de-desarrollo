package com.proyecto.pedidos.test;

import com.proyecto.pedidos.model.ProductoFisico;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de ProductoFisico")
public class ProductoFisicoTest {

    @Test
    @DisplayName("TC-PF-01: Precio final incluye IVA del 21% y coste de envío")
    void testPrecioFinalConIVAyEnvio() {
        ProductoFisico producto = new ProductoFisico("Teclado", 100.0, 5.0);
        assertEquals(126.0, producto.calcularPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("TC-PF-02: Precio final con coste de envío cero solo aplica IVA")
    void testPrecioFinalSinEnvio() {
        ProductoFisico producto = new ProductoFisico("Ratón", 50.0, 0.0);
        assertEquals(60.5, producto.calcularPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("TC-PF-03: getCosteEnvio devuelve el valor asignado")
    void testGetCosteEnvio() {
        ProductoFisico producto = new ProductoFisico("Monitor", 300.0, 12.50);
        assertEquals(12.50, producto.getCosteEnvio(), 0.001);
    }

    @Test
    @DisplayName("TC-PF-04: Precio final es mayor que el precio base")
    void testPrecioFinalMayorQueBase() {
        ProductoFisico producto = new ProductoFisico("Silla", 150.0, 10.0);
        assertTrue(producto.calcularPrecioFinal() > producto.getPrecio());
    }

    @Test
    @DisplayName("TC-PF-05: Precio final NO es igual al precio base")
    void testPrecioFinalDistintoAlBase() {
        ProductoFisico producto = new ProductoFisico("Auriculares", 80.0, 0.0);
        assertNotEquals(80.0, producto.calcularPrecioFinal());
    }

    @Test
    @DisplayName("TC-PF-06: Precio final NO es negativo")
    void testPrecioFinalNoNegativo() {
        ProductoFisico producto = new ProductoFisico("Cable", 5.0, 2.0);
        assertFalse(producto.calcularPrecioFinal() < 0);
    }

    @Test
    @DisplayName("TC-PF-07: Excepción al crear producto con precio negativo")
    void testExcepcionPrecioNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
            new ProductoFisico("Ilegal", -50.0, 5.0));
    }

    @Test
    @DisplayName("TC-PF-09: setCosteEnvio actualiza el coste de envío")
    void testSetCosteEnvio() {
        ProductoFisico producto = new ProductoFisico("Caja", 100.0, 5.0);
        producto.setCosteEnvio(10.0);
        assertEquals(10.0, producto.getCosteEnvio(), 0.001);
    }

    @Test
    @DisplayName("TC-PF-10: setNombre actualiza el nombre")
    void testSetNombre() {
        ProductoFisico producto = new ProductoFisico("Viejo", 100.0, 5.0);
        producto.setNombre("Nuevo");
        assertEquals("Nuevo", producto.getNombre());
    }

    @Test
    @DisplayName("TC-PF-11: setPrecio actualiza el precio base")
    void testSetPrecio() {
        ProductoFisico producto = new ProductoFisico("Item", 100.0, 0.0);
        producto.setPrecio(200.0);
        assertEquals(200.0, producto.getPrecio(), 0.001);
    }

    @Test
    @DisplayName("TC-PF-12: toString contiene nombre y precio final")
    void testToString() {
        ProductoFisico producto = new ProductoFisico("Teclado", 100.0, 5.0);
        assertTrue(producto.toString().contains("Teclado"));
        assertTrue(producto.toString().contains("Físico"));
    }

    @ParameterizedTest(name = "Precio={0}, Envío={1} => Esperado={2}")
    @CsvSource({
        "100.0, 0.0,  121.0",
        "100.0, 5.0,  126.0",
        "200.0, 10.0, 252.0",
        "50.0,  3.0,   63.5",
        "0.0,   0.0,    0.0"
    })
    @DisplayName("TC-PF-08: calcularPrecioFinal parametrizado")
    void testCalcularPrecioFinalParametrizado(double precio, double envio, double esperado) {
        ProductoFisico producto = new ProductoFisico("Producto", precio, envio);
        assertEquals(esperado, producto.calcularPrecioFinal(), 0.001);
    }
}
