package model;

import java.util.Date;

public class GastoÚnico extends Gasto {

	private Date fecha;

	public GastoÚnico(double monto, String descripcion, Date fecha) {
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
