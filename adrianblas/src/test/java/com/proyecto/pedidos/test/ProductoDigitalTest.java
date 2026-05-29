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
    @DisplayName("TC-PD-01: IVA general por defecto (21%) aplicado correctamente")
    void testIvaGeneralPorDefecto() {
        ProductoDigital producto = new ProductoDigital("Antivirus", 100.0, "ABC-123");
        assertEquals(121.0, producto.calcularPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("TC-PD-02: aplicarIVA REDUCIDO calcula el 10%")
    void testIvaReducido() {
        ProductoDigital producto = new ProductoDigital("Software", 200.0, "XYZ-999");
        producto.aplicarIVA("REDUCIDO");
        assertEquals(220.0, producto.calcularPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("TC-PD-03: Precio final es positivo con IVA")
    void testPrecioFinalPositivo() {
        ProductoDigital producto = new ProductoDigital("App", 50.0, "LIC-001");
        assertTrue(producto.calcularPrecioFinal() > 0);
    }

    @Test
    @DisplayName("TC-PD-04: getCodigoLicencia devuelve la licencia asignada")
    void testGetCodigoLicencia() {
        ProductoDigital producto = new ProductoDigital("eBook", 15.0, "EBOOK-42");
        assertEquals("EBOOK-42", producto.getCodigoLicencia());
    }

    @Test
    @DisplayName("TC-PD-05: aplicarIVA SUPER aplica el 4%")
    void testIvaSuperReducido() {
        ProductoDigital producto = new ProductoDigital("Demo", 100.0, "DEMO-00");
        producto.aplicarIVA("SUPER");
        assertEquals(104.0, producto.calcularPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("TC-PD-06: Con IVA, precio final NO es igual al precio base")
    void testPrecioFinalDistintoAlBase() {
        ProductoDigital producto = new ProductoDigital("Plugin", 100.0, "PLG-1");
        assertNotEquals(100.0, producto.calcularPrecioFinal());
    }

    @Test
    @DisplayName("TC-PD-07: Precio final NO es negativo")
    void testPrecioFinalNoEsNegativo() {
        ProductoDigital producto = new ProductoDigital("Tool", 80.0, "T-01");
        assertFalse(producto.calcularPrecioFinal() < 0);
    }

    @Test
    @DisplayName("TC-PD-08: Excepción al crear producto con precio negativo")
    void testExcepcionPrecioNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
            new ProductoDigital("Pirata", -10.0, "HACK-0"));
    }

    @Test
    @DisplayName("TC-PD-10: setCodigoLicencia actualiza la licencia")
    void testSetCodigoLicencia() {
        ProductoDigital producto = new ProductoDigital("App", 50.0, "OLD");
        producto.setCodigoLicencia("NEW-LIC");
        assertEquals("NEW-LIC", producto.getCodigoLicencia());
    }

    @Test
    @DisplayName("TC-PD-11: setImpuestoIVA actualiza el precio final")
    void testSetImpuestoIVA() {
        ProductoDigital producto = new ProductoDigital("App", 100.0, "L1");
        producto.setImpuestoIVA(0.20);
        assertEquals(120.0, producto.calcularPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("TC-PD-12: setNombre actualiza el nombre")
    void testSetNombre() {
        ProductoDigital producto = new ProductoDigital("Viejo", 100.0, "L1");
        producto.setNombre("Nuevo");
        assertEquals("Nuevo", producto.getNombre());
    }

    @Test
    @DisplayName("TC-PD-13: setPrecioBase actualiza el precio base")
    void testSetPrecioBase() {
        ProductoDigital producto = new ProductoDigital("App", 100.0, "L1");
        producto.setPrecioBase(200.0);
        assertEquals(200.0, producto.getPrecioBase(), 0.001);
    }

    @Test
    @DisplayName("TC-PD-14: toString contiene nombre y tipo Digital")
    void testToString() {
        ProductoDigital producto = new ProductoDigital("App", 100.0, "L1");
        assertTrue(producto.toString().contains("App"));
        assertTrue(producto.toString().contains("Digital"));
    }

    @ParameterizedTest(name = "Precio={0}, TipoIVA={1} => Esperado={2}")
    @CsvSource({
        "100.0, GENERAL,  121.0",
        "200.0, REDUCIDO, 220.0",
        "200.0, SUPER,    208.0",
        "50.0,  GENERAL,   60.5",
        "0.0,   GENERAL,    0.0"
    })
    @DisplayName("TC-PD-09: calcularPrecioFinal parametrizado por tipo de IVA")
    void testCalcularPrecioFinalParametrizado(double precio, String tipoIva, double esperado) {
        ProductoDigital producto = new ProductoDigital("Producto", precio, "LIC-X");
        producto.aplicarIVA(tipoIva);
        assertEquals(esperado, producto.calcularPrecioFinal(), 0.001);
    }
}
