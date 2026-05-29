package com.proyecto.pedidos.model;

/**
 * Clase abstracta base que representa cualquier producto del sistema.
 * Define los atributos comunes y obliga a las subclases a implementar
 * el cálculo del precio final.
 */
public abstract class Producto {

    protected String nombre;
    protected double precioBase;

    /**
     * Constructor de Producto.
     * @param nombre    Nombre del producto.
     * @param precioBase Precio base sin impuestos ni extras.
     * @throws IllegalArgumentException si el precio es negativo.
     */
    public Producto(String nombre, double precioBase) {
        if (precioBase < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.nombre = nombre;
        this.precioBase = precioBase;
    }

    /** @return Nombre del producto. */
    public String getNombre() { return nombre; }

    /** @param nombre Nuevo nombre del producto. */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /** @return Precio base del producto. */
    public double getPrecioBase() { return precioBase; }

    /** @param precioBase Nuevo precio base. */
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }

    /**
     * Calcula el precio final del producto aplicando impuestos y/o extras.
     * Debe ser implementado por cada subclase.
     * @return Precio final a pagar por el producto.
     */
    public abstract double calcularPrecioFinal();

    @Override
    public String toString() {
        return String.format("%s (precio base: %.2f)", nombre, precioBase);
    }
}
