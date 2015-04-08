package model;

import java.util.Date;

public class GastoUnico extends Gasto {

	private Date fecha;

	public GastoUnico(double monto, String descripcion, String tipo, Date fecha) {
		super(monto, descripcion, tipo);
		this.fecha = fecha;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

}
