package com.proyecto.pedidos.model;

public class ProductoDigital extends Producto {
    private String licencia;
    private double descuento; // fracción, p.ej. 0.10 para 10% descuento
    private static final double IVA = 0.0; // asumimos sin IVA o reducido según el caso

    public ProductoDigital(String nombre, double precio, String licencia, double descuento) {
        super(nombre, precio);
        this.licencia = licencia;
        this.descuento = descuento;
    }

    public String getLicencia() { return licencia; }
    public void setLicencia(String licencia) { this.licencia = licencia; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    @Override
    public double calcularPrecioFinal() {
        double base = getPrecio();
        double precioConDescuento = base * (1 - descuento);
        // Si hay IVA distinto para digitales se aplicaría aquí; usamos 0.
        return precioConDescuento * (1 + IVA);
    }

    @Override
    public String toString() {
        return String.format("%s [Digital] - Precio final: %.2f (licencia: %s)",
                super.toString(), calcularPrecioFinal(), licencia);
    }
}
