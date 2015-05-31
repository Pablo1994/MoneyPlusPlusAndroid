package com.apps.pablo.model;

/**
 * Created by Pablo Arias on 30/05/15.
 */
public class Ahorro {
    protected double monto;
    protected String descripcion;

    public Ahorro(double monto, String descripcion) {
        this.monto = monto;
        this.descripcion = descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
