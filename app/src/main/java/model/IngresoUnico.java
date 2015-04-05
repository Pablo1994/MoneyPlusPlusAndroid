package model;

import java.util.Date;

public class IngresoUnico extends Ingreso {
	private Date fecha;

	public IngresoUnico(double monto, String descripcion, Date fecha) {
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
