package com.proyecto.pedidos.model;
/**
 * Producto tangible que requiere envío físico.
 * El precio final se calcula sumando al precio base el coste
 * de envío basado en el peso del producto.
 */
public class ProductoFisico extends Producto {

    private static final double COSTE_POR_KG = 2.0;

    private double peso;

    /**
     * Constructor de ProductoFisico.
     * @param nombre    Nombre del producto.
     * @param precioBase Precio base sin extras.
     * @param peso      Peso del producto en kg, usado para calcular el envío.
     * @throws IllegalArgumentException si el precio es negativo.
     */
    public ProductoFisico(String nombre, double precioBase, double peso) {
        super(nombre, precioBase);
        this.peso = peso;
    }

    /** @return Peso del producto en kg. */
    public double getPeso() { return peso; }

    /** @param peso Nuevo peso del producto. */
    public void setPeso(double peso) { this.peso = peso; }

    /**
     * Calcula el precio final: precio base + (peso × tarifa por kg).
     * @return Precio final incluyendo gastos de envío por peso.
     */
    @Override
    public double calcularPrecioFinal() {
        return precioBase + (peso * COSTE_POR_KG);
    }

    @Override
    public String toString() {
        return String.format("%s [Físico] - Precio final: %.2f (peso: %.2f kg)",
                super.toString(), calcularPrecioFinal(), peso);
    }
}
