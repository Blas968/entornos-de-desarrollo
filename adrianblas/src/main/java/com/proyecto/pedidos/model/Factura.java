package com.proyecto.pedidos.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Documento de salida que certifica la finalización de una transacción.
 * Generada automáticamente por la Tienda tras completar una venta.
 * Incluye un desglose detallado de todos los conceptos aplicados.
 */
public class Factura {

    private final String        codigoFactura;
    private final LocalDateTime fechaEmision;
    private final double        totalNeto;
    private final double        totalIva;
    private final double        totalEnvio;
    private final double        totalDescuento;
    private final double        totalFinal;

    /**
     * Constructor de Factura. El código y la fecha se generan automáticamente.
     * @param totalNeto     Base imponible antes de IVA y extras.
     * @param totalIva      Importe de IVA aplicado.
     * @param totalEnvio    Gastos de envío aplicados.
     * @param totalDescuento Descuento de fidelidad aplicado.
     * @param totalFinal    Importe final a pagar por el cliente.
     */
    public Factura(double totalNeto, double totalIva,
                   double totalEnvio, double totalDescuento,
                   double totalFinal) {
        this.codigoFactura  = "FACT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.fechaEmision   = LocalDateTime.now();
        this.totalNeto      = totalNeto;
        this.totalIva       = totalIva;
        this.totalEnvio     = totalEnvio;
        this.totalDescuento = totalDescuento;
        this.totalFinal     = totalFinal;
    }

    /** @return Código único de la factura. */
    public String getCodigoFactura()   { return codigoFactura; }

    /** @return Fecha y hora de emisión de la factura. */
    public LocalDateTime getFechaEmision() { return fechaEmision; }

    /** @return Base imponible neta. */
    public double getTotalNeto()       { return totalNeto; }

    /** @return Importe de IVA. */
    public double getTotalIva()        { return totalIva; }

    /** @return Gastos de envío. */
    public double getTotalEnvio()      { return totalEnvio; }

    /** @return Descuento aplicado. */
    public double getTotalDescuento()  { return totalDescuento; }

    /** @return Total final a pagar. */
    public double getTotalFinal()      { return totalFinal; }

    /**
     * Genera un desglose detallado de la factura con todos los conceptos.
     * @return Cadena con el resumen completo de la factura.
     */
    public String generarDesglose() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format(
            "═══════════════════════════════════\n" +
            "  FACTURA: %s\n" +
            "  Fecha:   %s\n" +
            "═══════════════════════════════════\n" +
            "  Base neta:       %8.2f €\n" +
            "  IVA:             %8.2f €\n" +
            "  Envío:           %8.2f €\n" +
            "  Descuento:      -%8.2f €\n" +
            "───────────────────────────────────\n" +
            "  TOTAL FINAL:     %8.2f €\n" +
            "═══════════════════════════════════\n",
            codigoFactura, fechaEmision.format(fmt),
            totalNeto, totalIva, totalEnvio, totalDescuento, totalFinal
        );
    }

    @Override
    public String toString() {
        return generarDesglose();
    }
}
