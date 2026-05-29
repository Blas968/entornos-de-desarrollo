package com.proyecto.pedidos.test;

import com.proyecto.pedidos.model.ProductoDigital;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de ProductoDigital")
public class ProductoDigitalTest {

    @Test
    @DisplayName("TC-PD-01: Sin descuento, precio final igual al base")
    void testSinDescuentoPrecioFinalIgualAlBase() {
        ProductoDigital producto = new ProductoDigital("Antivirus", 100.0, "ABC-123", 0.0);
        assertEquals(100.0, producto.calcularPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("TC-PD-02: Descuento del 10% aplicado correctamente")
    void testDescuentoDelDiezPorCiento() {
        ProductoDigital producto = new ProductoDigital("Software", 200.0, "XYZ-999", 0.10);
        assertEquals(180.0, producto.calcularPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("TC-PD-03: Precio final es positivo con descuento parcial")
    void testPrecioFinalPositivo() {
        ProductoDigital producto = new ProductoDigital("App", 50.0, "LIC-001", 0.50);
        assertTrue(producto.calcularPrecioFinal() > 0);
    }

    @Test
    @DisplayName("TC-PD-04: getLicencia devuelve la licencia asignada")
    void testGetLicencia() {
        ProductoDigital producto = new ProductoDigital("eBook", 15.0, "EBOOK-42", 0.0);
        assertEquals("EBOOK-42", producto.getLicencia());
    }

    @Test
    @DisplayName("TC-PD-05: Descuento del 100% resulta en precio cero")
    void testDescuentoTotal() {
        ProductoDigital producto = new ProductoDigital("Demo", 99.0, "DEMO-00", 1.0);
        assertEquals(0.0, producto.calcularPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("TC-PD-06: Con descuento, precio final NO es igual al precio base")
    void testConDescuentoPrecioFinalDistintoAlBase() {
        ProductoDigital producto = new ProductoDigital("Plugin", 100.0, "PLG-1", 0.25);
        assertNotEquals(100.0, producto.calcularPrecioFinal());
    }

    @Test
    @DisplayName("TC-PD-07: Precio final NO es negativo con descuento válido")
    void testPrecioFinalNoEsNegativo() {
        ProductoDigital producto = new ProductoDigital("Tool", 80.0, "T-01", 0.99);
        assertFalse(producto.calcularPrecioFinal() < 0);
    }

    @Test
    @DisplayName("TC-PD-08: Excepción al crear producto con precio negativo")
    void testExcepcionPrecioNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
            new ProductoDigital("Pirata", -10.0, "HACK-0", 0.0));
    }

    @Test
    @DisplayName("TC-PD-10: setLicencia actualiza la licencia")
    void testSetLicencia() {
        ProductoDigital producto = new ProductoDigital("App", 50.0, "OLD", 0.0);
        producto.setLicencia("NEW-LIC");
        assertEquals("NEW-LIC", producto.getLicencia());
    }

    @Test
    @DisplayName("TC-PD-11: setDescuento actualiza el descuento")
    void testSetDescuento() {
        ProductoDigital producto = new ProductoDigital("App", 100.0, "L1", 0.0);
        producto.setDescuento(0.20);
        assertEquals(80.0, producto.calcularPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("TC-PD-12: setNombre actualiza el nombre")
    void testSetNombre() {
        ProductoDigital producto = new ProductoDigital("Viejo", 100.0, "L1", 0.0);
        producto.setNombre("Nuevo");
        assertEquals("Nuevo", producto.getNombre());
    }

    @Test
    @DisplayName("TC-PD-13: setPrecio actualiza el precio base")
    void testSetPrecio() {
        ProductoDigital producto = new ProductoDigital("App", 100.0, "L1", 0.0);
        producto.setPrecio(200.0);
        assertEquals(200.0, producto.getPrecio(), 0.001);
    }

    @Test
    @DisplayName("TC-PD-14: toString contiene nombre y precio final")
    void testToString() {
        ProductoDigital producto = new ProductoDigital("App", 100.0, "L1", 0.0);
        assertTrue(producto.toString().contains("App"));
        assertTrue(producto.toString().contains("Digital"));
    }

    @ParameterizedTest(name = "Precio={0}, Descuento={1} => Esperado={2}")
    @CsvSource({
        "100.0, 0.0,  100.0",
        "100.0, 0.10,  90.0",
        "200.0, 0.25, 150.0",
        "50.0,  0.50,  25.0",
        "0.0,   0.0,    0.0"
    })
    @DisplayName("TC-PD-09: calcularPrecioFinal parametrizado con distintos descuentos")
    void testCalcularPrecioFinalParametrizado(double precio, double descuento, double esperado) {
        ProductoDigital producto = new ProductoDigital("Producto", precio, "LIC-X", descuento);
        assertEquals(esperado, producto.calcularPrecioFinal(), 0.001);
    }
}
