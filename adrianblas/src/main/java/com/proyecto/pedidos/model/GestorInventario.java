package com.proyecto.pedidos.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona el inventario de productos mediante listas paralelas.
 * Permite dar de alta productos, verificar disponibilidad,
 * reservar unidades y confirmar ventas.
 */
public class GestorInventario {

    private List<String>  listaIds       = new ArrayList<>();
    private List<Integer> stockReal      = new ArrayList<>();
    private List<Integer> stockReservado = new ArrayList<>();

    private int buscarIndice(String id) {
        for (int i = 0; i < listaIds.size(); i++) {
            if (listaIds.get(i).equals(id)) return i;
        }
        return -1;
    }

    /**
     * Registra un producto en el inventario o aumenta su stock si ya existe.
     * @param id       Identificador del producto.
     * @param cantidad Unidades a añadir.
     */
    public void darDeAltaProducto(String id, int cantidad) {
        int idx = buscarIndice(id);
        if (idx == -1) {
            listaIds.add(id);
            stockReal.add(cantidad);
            stockReservado.add(0);
        } else {
            stockReal.set(idx, stockReal.get(idx) + cantidad);
        }
    }

    /**
     * Verifica si hay stock disponible y lo reserva si es así.
     * @param id       Identificador del producto.
     * @param cantidad Unidades a reservar.
     * @return true si la reserva se completó con éxito, false si no hay stock.
     */
    public boolean verificarYReservar(String id, int cantidad) {
        int idx = buscarIndice(id);
        if (idx != -1) {
            int disponible = stockReal.get(idx) - stockReservado.get(idx);
            if (disponible >= cantidad) {
                stockReservado.set(idx, stockReservado.get(idx) + cantidad);
                return true;
            }
        }
        return false;
    }

    /**
     * Calcula el impacto económico si hay rotura de stock en un producto.
     * @param id     Identificador del producto.
     * @param precio Precio unitario del producto.
     * @return Penalización estimada (5% del precio si el faltante supera 10 uds).
     */
    public double calcularImpactoRotura(String id, double precio) {
        int idx = buscarIndice(id);
        if (idx == -1) return 0;
        int faltante = Math.abs(stockReal.get(idx) - stockReservado.get(idx));
        return (faltante > 10) ? precio * 0.05 : 0;
    }

    /**
     * Confirma la venta descontando las unidades del stock real y reservado.
     * @param id       Identificador del producto.
     * @param cantidad Unidades vendidas.
     */
    public void confirmarVenta(String id, int cantidad) {
        int idx = buscarIndice(id);
        if (idx != -1) {
            stockReal.set(idx,      stockReal.get(idx)      - cantidad);
            stockReservado.set(idx, stockReservado.get(idx) - cantidad);
        }
    }

    /**
     * Indica si el stock de un producto está por debajo del umbral crítico (5 uds).
     * @param id Identificador del producto.
     * @return true si el stock real es menor a 5 unidades.
     */
    public boolean esProductoCritico(String id) {
        int idx = buscarIndice(id);
        return idx != -1 && stockReal.get(idx) < 5;
    }
}
