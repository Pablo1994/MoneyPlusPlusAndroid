package model;

public class IngresoPeriodicoFecha extends Ingreso {
	private int[] fechas;
	private String frecuencia;

	public IngresoPeriodicoFecha(double monto, String descripcion,
			int[] fechas, String frecuencia) {
		super(monto, descripcion);
		this.fechas = fechas;
		this.frecuencia = frecuencia;
	}

	public int[] getFechas() {
		return fechas;
	}

	public void setFechas(int[] fechas) {
		this.fechas = fechas;
	}

	public String getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

}
