package com.proyecto.pedidos.model;

public class ProductoFisico extends Producto {
    private double costeEnvio;
    private static final double IVA = 0.21; // 21% IVA

    public ProductoFisico(String nombre, double precio, double costeEnvio) {
        super(nombre, precio);
        this.costeEnvio = costeEnvio;
    }

    public double getCosteEnvio() { return costeEnvio; }
    public void setCosteEnvio(double costeEnvio) { this.costeEnvio = costeEnvio; }

    @Override
    public double calcularPrecioFinal() {
        double base = getPrecio();
        double conIVA = base * (1 + IVA);
        return conIVA + costeEnvio;
    }

    @Override
    public String toString() {
        return String.format("%s [Físico] - Precio final: %.2f (envío: %.2f)",
                super.toString(), calcularPrecioFinal(), costeEnvio);
    }
}
