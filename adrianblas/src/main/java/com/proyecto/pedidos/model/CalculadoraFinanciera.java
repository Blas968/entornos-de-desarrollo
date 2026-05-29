package com.proyecto.pedidos.model;

public class CalculadoraFinanciera {
    
    public double aplicarIVA(double base, String tipoIva) {
        double iva = 1.0;
        switch (tipoIva.toUpperCase()) {
            case "GENERAL":
                iva = 1.21;
                break;
            case "REDUCIDO":
                iva = 1.10;
                break;
            case "SUPER":
                iva = 1.04;
                break;
            default:
                iva = 1.0;
                break;
        }
        return base * iva;
    }

    public double calcularDescuentoFidelidad(double total, int añosAntiguedad, boolean esVip) {
        double desc = (añosAntiguedad > 5) ? 0.10 : 0.05;
        if (esVip) desc += 0.05;
        return total * (1 - desc);
    }

    public double calcularGastosEnvio(double peso, double totalPedido, String zona) {
        if (totalPedido > 100.0) return 0.0;
        double tasaZona = zona.equals("INTERNACIONAL") ? 15.0 : 5.0;
        return tasaZona + (peso * 1.2);
    }

    public double calcularComisionPasarela(double total, String metodo) {
        return metodo.equals("PAYPAL") ? total * 0.03 : 1.5;
    }

    public double obtenerPrecioFinalIntegrado(double base, double envio, double descuentoAcumulado) {
        double precioConDescuento = base - descuentoAcumulado;
        double precioConEnvio = precioConDescuento + envio;
        System.out.println("DEBUG: Precio con descuento y envío: " + precioConEnvio);
        return precioConEnvio;
    }
}