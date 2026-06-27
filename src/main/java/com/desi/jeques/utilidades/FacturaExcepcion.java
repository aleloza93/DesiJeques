package com.desi.jeques.utilidades;

public class FacturaExcepcion extends Exception {

    private String s;

    public FacturaExcepcion(String mensaje) {
        super(mensaje);
        this.s = null;
    }

    public FacturaExcepcion(String atributo, String mensaje) {
        super(mensaje);
        this.s = atributo;
    }

    public String getAtributo() {
        return s;
    }
}