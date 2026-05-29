package com.proyecto.pedidos.test;

// MEJORA: Se añade declaración de package y los imports necesarios porque el archivo
// original del test maestro carecía de package, lo que impedía su compilación
// en un proyecto Maven con estructura de paquetes estándar (bug de inconsistencia).

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.proyecto.pedidos.model.Pedido;
import com.proyecto.pedidos.model.Producto;
import com.proyecto.pedidos.model.ProductoDigital;
import com.proyecto.pedidos.model.ProductoFisico;

@DisplayName("Tests de Integración: Flujo de Pedidos y Productos")
class GestionPedidosIntegrationTest {

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
    }

    @Test
    @DisplayName("Integración: Cálculo de total con múltiples tipos de productos")
    void deberiaCalcularTotalConProductosMixtos() {
        // 1. Arrange
        Producto libro = new ProductoFisico("Libro Java", 30.0, 1.5);  // 30 + (1.5*2) = 33.0
        Producto curso = new ProductoDigital("Curso Online", 50.0, "C-202"); // 50 + (50*0.21) = 60.5

        // 2. Act
        pedido.agregarProducto(libro);
        pedido.agregarProducto(curso);
        double totalObtenido = pedido.calcularTotalPedido();

        // 3. Assert
        double totalEsperado = 33.0 + 60.5;
        assertEquals(totalEsperado, totalObtenido,
                "El pedido no integró correctamente los precios finales de los productos");
    }

    @Test
    @DisplayName("Integración: Pedido vacío y persistencia de estado")
    void deberiaDarCeroSiNoHayProductos() {
        assertEquals(0.0, pedido.calcularTotalPedido(),
                "Un pedido recién creado debería tener un total de 0.0");
    }

    @Test
    @DisplayName("Integración: Robustez ante productos nulos")
    void noDeberiaFallarSiSeAgregaProductoNulo() {
        assertDoesNotThrow(() -> {
            pedido.agregarProducto(null);
            pedido.calcularTotalPedido();
        }, "El sistema debería filtrar productos nulos en la integración");
    }
}
