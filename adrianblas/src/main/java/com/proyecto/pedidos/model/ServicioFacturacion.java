package com.proyecto.pedidos.model;

import java.util.UUID;

/**
 * Orquesta el proceso completo de facturación integrando
 * el inventario y los cálculos financieros.
 */
public class ServicioFacturacion {

    private GestorInventario    inventario;
    private CalculadoraFinanciera calculadora;

    /**
     * Constructor del servicio de facturación.
     * @param inv  Gestor de inventario del sistema.
     * @param calc Calculadora financiera del sistema.
     */
    public ServicioFacturacion(GestorInventario inv, CalculadoraFinanciera calc) {
        this.inventario   = inv;
        this.calculadora  = calc;
    }

    /**
     * Procesa una factura completa para un producto dado.
     * Verifica stock, aplica IVA, calcula descuentos y gastos de envío.
     * @param idProd    Identificador del producto a facturar.
     * @param cant      Cantidad de unidades.
     * @param precioUnit Precio unitario base.
     * @return Cadena con el código de factura y el total, o mensaje de error.
     */
    public String procesarFacturaCompleta(String idProd, int cant, double precioUnit) {
        if (!inventario.verificarYReservar(idProd, cant)) {
            return "ERROR: Stock insuficiente";
        }

        double base          = precioUnit * cant;
        double conIva        = calculadora.aplicarIVA(base, "GENERAL");
        double montoDescuento = conIva - calculadora.calcularDescuentoFidelidad(conIva, 2, false);
        double gastosEnvio   = calculadora.calcularGastosEnvio(2.0, base, "NACIONAL");

        double total = calculadora.obtenerPrecioFinalIntegrado(conIva, gastosEnvio, montoDescuento);

        inventario.confirmarVenta(idProd, cant);
        return "FACT-" + UUID.randomUUID().toString().substring(0, 5) + " | Total: " + total + "€";
    }

    /**
     * Valida que la suma de subtotal e IVA coincide con el total (margen 0.01).
     * @param subtotal Importe neto.
     * @param iva      IVA aplicado.
     * @param total    Total esperado.
     * @return true si la integridad es correcta.
     */
    public boolean validarIntegridad(double subtotal, double iva, double total) {
        return Math.abs((subtotal + iva) - total) < 0.01;
    }

    /**
     * Genera una nota de crédito a partir de una factura original.
     * @param facturaOriginal Código de la factura a anular.
     * @return Código de la nota de crédito generada.
     */
    public String emitirNotaCredito(String facturaOriginal) {
        return "NC-" + facturaOriginal;
    }

    /**
     * Estima el margen de beneficio descontando producción e IVA.
     * @param total           Precio de venta.
     * @param costeProduccion Coste de producción.
     * @return Margen estimado.
     */
    public double estimarMargenBeneficio(double total, double costeProduccion) {
        return total - costeProduccion - (total * 0.21);
    }

    /**
     * Archiva una factura por su identificador.
     * @param id Código de la factura a archivar.
     */
    public void archivarFactura(String id) {
        System.out.println("Documento " + id + " almacenado.");
    }
}
