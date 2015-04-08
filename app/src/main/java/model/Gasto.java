package model;

public class Gasto {
	private double monto;
	private String descripcion;
    private String tipo;

	public Gasto(double monto, String descripcion, String tipo) {
		super();
		this.monto = monto;
		this.descripcion = descripcion;
        this.tipo = tipo;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
