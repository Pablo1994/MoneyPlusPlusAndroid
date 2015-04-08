package model;

public class IngresoPeriodicoFecha extends Ingreso {
	private String[] fechas;
	private String frecuencia;

	public IngresoPeriodicoFecha(double monto, String descripcion,
			String[] fechas, String frecuencia) {
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
