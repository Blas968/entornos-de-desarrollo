package com.proyecto.pedidos.model;

/**
 * Clase orquestadora del sistema de ventas.
 * Coordina clientes, pedidos, inventario y facturación
 * en un único punto de entrada.
 */
public class Tienda {

    private static final int    UMBRAL_ANTIGUEDAD_ALTA = 5;
    private static final double DESCUENTO_BAJA         = 0.05;
    private static final double DESCUENTO_ALTA         = 0.10;
    private static final double DESCUENTO_VIP_EXTRA    = 0.05;

    /**
     * Realiza una venta completa: valida el pedido, aplica el descuento
     * de fidelidad del cliente y genera la factura resultante.
     *
     * @param cliente Cliente que realiza la compra.
     * @param pedido  Pedido con los productos a comprar.
     * @return Factura generada con el desglose completo.
     * @throws IllegalStateException si el pedido está vacío.
     */
    public Factura realizarVenta(Cliente cliente, Pedido pedido) {
        pedido.validarPedido();

        double totalNeto      = pedido.calcularTotalPedido();
        double porcentajeIva  = 0.21;
        double totalIva       = totalNeto * porcentajeIva;
        double totalConIva    = totalNeto + totalIva;

        double descuento      = calcularDescuentoFidelidad(cliente, totalConIva);
        double totalEnvio     = calcularEnvioPorPais(cliente.getPais(), pedido);
        double totalFinal     = totalConIva - descuento + totalEnvio;

        return new Factura(totalNeto, totalIva, totalEnvio, descuento, totalFinal);
    }

    /**
     * Calcula el descuento de fidelidad aplicable según el perfil del cliente.
     * El descuento base es del 5%; sube al 10% si supera 5 años de antigüedad.
     * Se añade un 5% extra si el cliente tiene estado VIP.
     *
     * @param cliente    Cliente cuyo descuento se calcula.
     * @param totalBase  Importe sobre el que se aplica el descuento.
     * @return Importe del descuento (no el total resultante).
     */
    private double calcularDescuentoFidelidad(Cliente cliente, double totalBase) {
        double porcentaje = (cliente.getAñosAntiguedad() > UMBRAL_ANTIGUEDAD_ALTA)
                ? DESCUENTO_ALTA
                : DESCUENTO_BAJA;
        if (cliente.isEsVip()) {
            porcentaje += DESCUENTO_VIP_EXTRA;
        }
        return totalBase * porcentaje;
    }

    /**
     * Determina los gastos de envío físico según el país del cliente.
     * Solo aplica a productos físicos del pedido.
     * Reglas: España → 0€, Francia/Italia/Portugal → 5€, resto → 10€.
     *
     * @param pais   País de destino del envío.
     * @param pedido Pedido con los productos (solo se evalúan los físicos).
     * @return Coste de envío total para el pedido.
     */
    private double calcularEnvioPorPais(String pais, Pedido pedido) {
        boolean tieneProductosFisicos = pedido.getProductos().stream()
                .anyMatch(p -> p instanceof ProductoFisico);

        if (!tieneProductosFisicos) return 0.0;

        switch (pais.toUpperCase()) {
            case "ESPAÑA":
            case "SPAIN":
                return 0.0;
            case "FRANCIA":
            case "FRANCE":
            case "ITALIA":
            case "ITALY":
            case "PORTUGAL":
                return 5.0;
            default:
                return 10.0;
        }
    }
}
