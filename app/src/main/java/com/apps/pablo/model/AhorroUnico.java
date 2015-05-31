package com.apps.pablo.model;

import java.util.Date;

/**
 * Created by Pablo Arias on 30/05/15.
 */
public class AhorroUnico extends Ahorro{
    private Date fecha;


    public AhorroUnico(double monto, String descripcion, Date fecha) {
        super(monto, descripcion);
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
