package com.proyecto.pedidos.test;

import com.proyecto.pedidos.model.Cliente;
import com.proyecto.pedidos.model.Pedido;
import com.proyecto.pedidos.model.ProductoDigital;
import com.proyecto.pedidos.model.ProductoFisico;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests Unitarios — Producto, ProductoFisico y ProductoDigital")
class ProductoTest {

    // ─── ProductoFisico ──────────────────────────────────────────────────────

    @Test
    @DisplayName("TC-PF-01: precio final = precioBase + peso*2.0")
    void testProductoFisicoFormula() {
        ProductoFisico p = new ProductoFisico("Libro Java", 30.0, 1.5);
        assertEquals(33.0, p.calcularPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("TC-PF-02: precio final con peso cero es igual al precio base")
    void testProductoFisicoSinPeso() {
        ProductoFisico p = new ProductoFisico("Folleto", 10.0, 0.0);
        assertEquals(10.0, p.calcularPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("TC-PF-03: precio final es mayor que el precio base con peso > 0")
    void testProductoFisicoFinalMayorQueBase() {
        ProductoFisico p = new ProductoFisico("Monitor", 300.0, 5.0);
        assertTrue(p.calcularPrecioFinal() > p.getPrecioBase());
    }

    @Test
    @DisplayName("TC-PF-04: excepción al crear con precio negativo")
    void testProductoFisicoPrecioNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProductoFisico("Ilegal", -10.0, 1.0));
    }

    @Test
    @DisplayName("TC-PF-05: precio final nunca es negativo")
    void testProductoFisicoFinalNoNegativo() {
        ProductoFisico p = new ProductoFisico("Cable", 5.0, 1.0);
        assertFalse(p.calcularPrecioFinal() < 0);
    }

    @Test
    @DisplayName("TC-PF-06: getPeso devuelve el valor asignado")
    void testProductoFisicoGetPeso() {
        ProductoFisico p = new ProductoFisico("Caja", 50.0, 3.5);
        assertEquals(3.5, p.getPeso(), 0.001);
    }

    @Test
    @DisplayName("TC-PF-07: setPeso actualiza el coste final")
    void testProductoFisicoSetPeso() {
        ProductoFisico p = new ProductoFisico("Item", 100.0, 1.0);
        p.setPeso(5.0);
        assertEquals(110.0, p.calcularPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("TC-PF-08: toString contiene nombre y tipo Físico")
    void testProductoFisicoToString() {
        ProductoFisico p = new ProductoFisico("Teclado", 80.0, 0.5);
        assertTrue(p.toString().contains("Teclado"));
        assertTrue(p.toString().contains("Físico"));
    }

    @ParameterizedTest(name = "precioBase={0}, peso={1} → esperado={2}")
    @CsvSource({
        "30.0, 1.5,  33.0",
        "50.0, 0.0,  50.0",
        "100.0, 2.0, 104.0",
        "0.0,   5.0,  10.0",
        "200.0, 3.0, 206.0"
    })
    @DisplayName("TC-PF-09: calcularPrecioFinal parametrizado")
    void testProductoFisicoParametrizado(double base, double peso, double esperado) {
        ProductoFisico p = new ProductoFisico("Producto", base, peso);
        assertEquals(esperado, p.calcularPrecioFinal(), 0.001);
    }

    // ─── ProductoDigital ─────────────────────────────────────────────────────

    @Test
    @DisplayName("TC-PD-01: IVA general por defecto (21%)")
    void testProductoDigitalIvaGeneralPorDefecto() {
        ProductoDigital p = new ProductoDigital("Curso Online", 50.0, "C-202");
        assertEquals(60.5, p.calcularPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("TC-PD-02: aplicarIVA REDUCIDO → 10%")
    void testProductoDigitalIvaReducido() {
        ProductoDigital p = new ProductoDigital("eBook", 100.0, "E-01");
        p.aplicarIVA("REDUCIDO");
        assertEquals(110.0, p.calcularPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("TC-PD-03: aplicarIVA SUPER → 4%")
    void testProductoDigitalIvaSuper() {
        ProductoDigital p = new ProductoDigital("App", 100.0, "A-01");
        p.aplicarIVA("SUPER");
        assertEquals(104.0, p.calcularPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("TC-PD-04: aplicarIVA con tipo desconocido no cambia el IVA")
    void testProductoDigitalIvaDesconocido() {
        ProductoDigital p = new ProductoDigital("Software", 100.0, "S-01");
        double antes = p.calcularPrecioFinal();
        p.aplicarIVA("INVALIDO");
        assertEquals(antes, p.calcularPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("TC-PD-05: setImpuestoIVA negativo no cambia el IVA")
    void testProductoDigitalSetIvaNoNegativo() {
        ProductoDigital p = new ProductoDigital("Plugin", 100.0, "P-01");
        double antes = p.getImpuestoIVA();
        p.setImpuestoIVA(-0.5);
        assertEquals(antes, p.getImpuestoIVA(), 0.001);
    }

    @Test
    @DisplayName("TC-PD-06: precio final con IVA es mayor que precio base")
    void testProductoDigitalFinalMayorQueBase() {
        ProductoDigital p = new ProductoDigital("Licencia", 80.0, "L-01");
        assertTrue(p.calcularPrecioFinal() > p.getPrecioBase());
    }

    @Test
    @DisplayName("TC-PD-07: precio final no es igual al precio base (IVA modifica precio)")
    void testProductoDigitalFinalDistintoDeBase() {
        ProductoDigital p = new ProductoDigital("Video", 50.0, "V-01");
        assertNotEquals(50.0, p.calcularPrecioFinal());
    }

    @Test
    @DisplayName("TC-PD-08: excepción al crear con precio negativo")
    void testProductoDigitalPrecioNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProductoDigital("Pirata", -5.0, "HACK"));
    }

    @Test
    @DisplayName("TC-PD-09: getCodigoLicencia devuelve el valor asignado")
    void testProductoDigitalGetLicencia() {
        ProductoDigital p = new ProductoDigital("Tool", 30.0, "T-999");
        assertEquals("T-999", p.getCodigoLicencia());
    }

    @Test
    @DisplayName("TC-PD-10: toString contiene nombre y tipo Digital")
    void testProductoDigitalToString() {
        ProductoDigital p = new ProductoDigital("IDE", 120.0, "IDE-01");
        assertTrue(p.toString().contains("IDE"));
        assertTrue(p.toString().contains("Digital"));
    }

    @ParameterizedTest(name = "precioBase={0}, IVA={1} → esperado={2}")
    @CsvSource({
        "100.0, GENERAL,  121.0",
        "100.0, REDUCIDO, 110.0",
        "100.0, SUPER,    104.0",
        "200.0, GENERAL,  242.0",
        "50.0,  REDUCIDO,  55.0"
    })
    @DisplayName("TC-PD-11: calcularPrecioFinal parametrizado por tipo de IVA")
    void testProductoDigitalParametrizado(double base, String iva, double esperado) {
        ProductoDigital p = new ProductoDigital("Producto", base, "LIC-X");
        p.aplicarIVA(iva);
        assertEquals(esperado, p.calcularPrecioFinal(), 0.001);
    }
}
