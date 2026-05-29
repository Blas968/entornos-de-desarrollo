package com.proyecto.pedidos.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.proyecto.pedidos.model.Cliente;
import com.proyecto.pedidos.model.Pedido;
import com.proyecto.pedidos.model.Producto;
import com.proyecto.pedidos.model.ProductoDigital;
import com.proyecto.pedidos.model.ProductoFisico;

@DisplayName("Tests de Integración: Flujo de Pedidos y Productos")
class GestionPedidosIntegrationTest {

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        // Inicializamos el escenario: Un pedido vacío antes de cada test
        Cliente cliente = new Cliente("Test Cliente", "test@example.com", "Calle Falsa 123");
        pedido = new Pedido(cliente);
    }

    @Test
    @DisplayName("Integración: Cálculo de total con múltiples tipos de productos")
    void deberiaCalcularTotalConProductosMixtos() {
        // 1. Arrange (Preparar)
        // Creamos productos reales para ver cómo interactúan con el pedido
        Producto libro = new ProductoFisico("Libro Java", 30.0, 1.5); // 30 * 1.21 + 1.5 = 37.8
        Producto curso = new ProductoDigital("Curso Online", 50.0, "C-202", 0.0); // 50.0 (sin descuento, IVA=0)

        // 2. Act (Actuar)
        // Aquí ocurre la integración: Pedido recibe y almacena objetos Producto
        pedido.agregarProducto(libro);
        pedido.agregarProducto(curso);
        double totalObtenido = pedido.calcularTotal();

        // 3. Assert (Verificar)
        // El total esperado es la suma de los cálculos internos de cada producto
        double totalEsperado = 37.8 + 50.0;
        
        assertEquals(totalEsperado, totalObtenido, "El pedido no integró correctamente los precios finales de los productos");
    }

    @Test
    @DisplayName("Integración: Pedido vacío y persistencia de estado")
    void deberiaDarCeroSiNoHayProductos() {
        assertEquals(0.0, pedido.calcularTotal(), 
            "Un pedido recién creado debería tener un total de 0.0");
    }

    @Test
    @DisplayName("Integración: Robustez ante productos nulos")
    void noDeberiaFallarSiSeAgregaProductoNulo() {
        // Test de integración para asegurar que el sistema no "explota" (NullPointerException)
        // al interactuar con datos erróneos.
        assertDoesNotThrow(() -> {
            pedido.agregarProducto(null);
            pedido.calcularTotal();
        }, "El sistema debería filtrar productos nulos en la integración");
    }
}