package model;

public class GastoPeriodicoDia extends GastoPeriodico {
	private String dia;
	private String frecuencia;

	public GastoPeriodicoDia(double monto, String descripcion, String dia,
			String frecuencia) {
		super(monto, descripcion);
		this.dia = dia;
		this.frecuencia = frecuencia;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

}
