package model;

import java.util.ArrayList;

public class GastoDiario extends Gasto {
	private ArrayList<String> dias;

	public GastoDiario(double monto, String descripcion, String tipo, ArrayList<String> dias) {
		super(monto, descripcion, tipo);
		this.dias = dias;
	}

	public ArrayList<String> getDias() {
		return dias;
	}

	public void setDias(ArrayList<String> dias) {
		this.dias = dias;
	}
	
}
