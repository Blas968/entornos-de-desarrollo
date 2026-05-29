package com.proyecto.pedidos.model;

/**
 * Representa a un cliente del sistema.
 * Sus atributos de antigüedad y VIP determinan el descuento
 * de fidelidad que la Tienda aplicará sobre el total del pedido.
 * Su país determina los gastos de envío aplicables.
 */
public class Cliente {

    private String id;
    private String nombre;
    private String correo;
    private String pais;
    private int añosAntiguedad;
    private boolean esVip;

    /**
     * Constructor completo de Cliente.
     * @param id              Identificador único del cliente.
     * @param nombre          Nombre completo.
     * @param correo          Correo electrónico.
     * @param pais            País de residencia (usado para calcular envíos).
     * @param añosAntiguedad  Años como cliente del sistema.
     * @param esVip           Indica si el cliente tiene estado VIP.
     */
    public Cliente(String id, String nombre, String correo,
                   String pais, int añosAntiguedad, boolean esVip) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.pais = pais;
        this.añosAntiguedad = añosAntiguedad;
        this.esVip = esVip;
    }

    public String getId()               { return id; }
    public String getNombre()           { return nombre; }
    public void   setNombre(String n)   { this.nombre = n; }
    public String getCorreo()           { return correo; }
    public void   setCorreo(String c)   { this.correo = c; }
    public String getPais()             { return pais; }
    public void   setPais(String p)     { this.pais = p; }
    public int    getAñosAntiguedad()   { return añosAntiguedad; }
    public void   setAñosAntiguedad(int a) { this.añosAntiguedad = a; }
    public boolean isEsVip()            { return esVip; }
    public void    setEsVip(boolean v)  { this.esVip = v; }

    @Override
    public String toString() {
        return String.format("%s [%s] - %s - VIP: %s - Antigüedad: %d años",
                nombre, id, correo, esVip, añosAntiguedad);
    }
}
