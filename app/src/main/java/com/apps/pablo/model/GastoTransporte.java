package com.apps.pablo.model;

/**
 * Created by Pablo Arias on 30/05/15.
 */
public class GastoTransporte extends Gasto{
    private String medio;

    public GastoTransporte(double monto, String descripcion, String tipo, String medio) {
        super(monto, descripcion, tipo);
        this.medio = medio;
    }

    public String getMedio() {
        return medio;
    }

    public void setMedio(String medio) {
        this.medio = medio;
    }
}
