package com.proyecto.pedidos.model;

/**
 * Producto descargable o virtual que no requiere envío físico.
 * El precio final se calcula aplicando IVA al precio base.
 * El IVA por defecto es el 21% (tipo general).
 */
public class ProductoDigital extends Producto {

    private static final double IVA_GENERAL  = 0.21;
    private static final double IVA_REDUCIDO = 0.10;
    private static final double IVA_SUPER    = 0.04;

    private String codigoLicencia;
    private double impuestoIVA;

    /**
     * Constructor de ProductoDigital.
     * Aplica por defecto el IVA general (21%).
     * @param nombre         Nombre del producto digital.
     * @param precioBase     Precio base sin impuestos.
     * @param codigoLicencia Código único de licencia del producto.
     * @throws IllegalArgumentException si el precio es negativo.
     */
    public ProductoDigital(String nombre, double precioBase, String codigoLicencia) {
        super(nombre, precioBase);
        this.codigoLicencia = codigoLicencia;
        this.impuestoIVA = IVA_GENERAL;
    }

    /**
     * Cambia el tipo de IVA aplicado al producto.
     * @param tipoIva "GENERAL" (21%), "REDUCIDO" (10%) o "SUPER" (4%).
     *                Cualquier otro valor mantiene el IVA actual sin cambios.
     */
    public void aplicarIVA(String tipoIva) {
        switch (tipoIva.toUpperCase()) {
            case "GENERAL":  this.impuestoIVA = IVA_GENERAL;  break;
            case "REDUCIDO": this.impuestoIVA = IVA_REDUCIDO; break;
            case "SUPER":    this.impuestoIVA = IVA_SUPER;    break;
            default: break;
        }
    }

    /**
     * Permite asignar un IVA personalizado en decimal (ej. 0.07 para 7%).
     * Solo acepta valores no negativos.
     * @param nuevoIva Nuevo valor de IVA como fracción decimal.
     */
    public void setImpuestoIVA(double nuevoIva) {
        if (nuevoIva >= 0) {
            this.impuestoIVA = nuevoIva;
        }
    }

    /** @return IVA actual aplicado, como fracción decimal. */
    public double getImpuestoIVA() { return impuestoIVA; }

    /** @return Código de licencia del producto. */
    public String getCodigoLicencia() { return codigoLicencia; }

    /** @param codigoLicencia Nuevo código de licencia. */
    public void setCodigoLicencia(String codigoLicencia) {
        this.codigoLicencia = codigoLicencia;
    }

    /**
     * Calcula el precio final: precio base + (precio base × IVA).
     * @return Precio final con IVA incluido.
     */
    @Override
    public double calcularPrecioFinal() {
        return precioBase + (precioBase * impuestoIVA);
    }

    @Override
    public String toString() {
        return String.format("%s [Digital] - Precio final: %.2f (licencia: %s)",
                super.toString(), calcularPrecioFinal(), codigoLicencia);
    }
}
