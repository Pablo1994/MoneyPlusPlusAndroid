package model;

import java.util.ArrayList;

public class IngresoDiario extends Ingreso {
	private ArrayList<String> dias; // Cuáles días de la semana recibe dinero?

	public IngresoDiario(double monto, String descripcion,
			ArrayList<String> dias) {
		super(monto, descripcion);
		this.dias = dias;
	}

	public ArrayList<String> getDias() {
		return dias;
	}

	public void setDias(ArrayList<String> dias) {
		this.dias = dias;
	}

}
