package model;

/**
 * Created by Pablo Arias on 20/04/15.
 */
public class Mes {
    private String mes;
    private String anio;
    private double ingresos;
    private double gastos;

    public Mes(String mes, String anio, double ingresos, double gastos) {
        this.mes = mes;
        this.anio = anio;
        this.ingresos = ingresos;
        this.gastos = gastos;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public void setIngresos(double ingresos) {
        this.ingresos = ingresos;
    }

    public void setGastos(double gastos) {
        this.gastos = gastos;
    }

    public String getMes() {
        return mes;
    }

    public String getAnio() {
        return anio;
    }

    public double getIngresos() {
        return ingresos;
    }

    public double getGastos() {
        return gastos;
    }
}
