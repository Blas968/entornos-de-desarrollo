package com.example.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un pedido de compra.
 * Actúa como contenedor de productos y calcula el importe total.
 * Un pedido puede crearse sin cliente (flujo básico) o asociarse
 * a un Cliente para aplicar descuentos y envíos personalizados.
 */
public class Pedido {

    private String idPedido;
    private Cliente cliente;
    private List<Producto> productos;

    /**
     * Constructor sin argumentos requerido por los tests de integración.
     * Inicializa la lista de productos vacía.
     */
    public Pedido() {
        this.productos = new ArrayList<>();
    }

    /**
     * Constructor con cliente asociado.
     * @param cliente Cliente al que pertenece este pedido.
     */
    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.productos = new ArrayList<>();
    }

    /**
     * Constructor completo con ID y cliente.
     * @param idPedido Identificador único del pedido.
     * @param cliente  Cliente asociado al pedido.
     */
    public Pedido(String idPedido, Cliente cliente) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.productos = new ArrayList<>();
    }

    /**
     * Añade un producto al pedido. Ignora valores nulos.
     * @param p Producto a añadir.
     */
    public void agregarProducto(Producto p) {
        if (p != null) {
            productos.add(p);
        }
    }

    /**
     * Elimina un producto del pedido si existe.
     * @param p Producto a eliminar.
     */
    public void eliminarProducto(Producto p) {
        productos.remove(p);
    }

    /**
     * Calcula el total sumando el precio final de cada producto.
     * @return Total del pedido.
     */
    public double calcularTotalPedido() {
        double total = 0.0;
        for (Producto p : productos) {
            total += p.calcularPrecioFinal();
        }
        return total;
    }

    /**
     * Verifica que el pedido tenga al menos un producto antes de procesarlo.
     * @throws IllegalStateException si el pedido está vacío.
     */
    public void validarPedido() {
        if (productos.isEmpty()) {
            throw new IllegalStateException("No se puede procesar un pedido vacío");
        }
    }

    /**
     * Genera un resumen legible del pedido.
     * @return Cadena con cliente, productos y total.
     */
    public String mostrarResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Resumen del pedido ===\n");
        if (cliente != null) {
            sb.append("Cliente: ").append(cliente.toString()).append("\n");
        }
        sb.append("Productos:\n");
        for (Producto p : productos) {
            sb.append(" - ").append(p.toString()).append("\n");
        }
        sb.append(String.format("Total: %.2f€\n", calcularTotalPedido()));
        return sb.toString();
    }

    public String   getIdPedido()              { return idPedido; }
    public Cliente  getCliente()               { return cliente; }
    public void     setCliente(Cliente c)      { this.cliente = c; }
    public List<Producto> getProductos()       { return productos; }
}
