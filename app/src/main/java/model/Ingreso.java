package model;

public class Ingreso {
	protected double monto;
	protected String descripcion;

	public Ingreso(double monto, String descripcion) {
		super();
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
