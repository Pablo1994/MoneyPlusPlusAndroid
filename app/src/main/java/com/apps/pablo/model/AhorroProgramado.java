package com.apps.pablo.model;

/**
 * Created by Pablo Arias on 30/05/15.
 */
public class AhorroProgramado extends Ahorro{
    private String [] fechas;
    private String frecuencia;

    public AhorroProgramado(double monto, String descripcion, String[] fechas, String frecuencia) {
        super(monto, descripcion);
        this.fechas = fechas;
        this.frecuencia = frecuencia;
    }

    public String[] getFechas() {
        return fechas;
    }

    public void setFechas(String[] fechas) {
        this.fechas = fechas;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }
}
