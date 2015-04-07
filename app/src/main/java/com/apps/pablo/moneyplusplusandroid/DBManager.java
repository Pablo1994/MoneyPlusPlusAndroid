package com.apps.pablo.moneyplusplusandroid;

/**
 * Created by pablo on 06/04/15.
 */
public class DBManager {
    public static final String INGRESO_PURO = "CREATE TABLE ingreso_puro (ingPuroId INTEGER primary key autoincrement, monto INTEGER, descripcion TEXT, fecha TEXT);";
    public static final String INGRESO_PER_DIA = "CREATE TABLE ingreso_per_dia (ingPerDiaId INTEGER primary key autoincrement, monto INTEGER, descripcion TEXT, dia TEXT, frecuencia TEXT);";
    public static final String INGRESO_DIARIO = "CREATE TABLE ingreso_diario (ingDiarioId INTEGER primary key autoincrement, monto INTEGER, descripcion TEXT);";
    public static final String DIAS = "CREATE TABLE dias (dia TEXT, idIngDiario INT, FOREIGN KEY(idIngDiario) REFERENCES ingreso_diario(ingDiarioId));";
    public static final String INGRESO_PER_FECHA = "CREATE TABLE ingreso_per_fecha (ingPerFechaId INTEGER primary key autoincrement, monto INTEGER, descripcion TEXT, frecuencia TEXT);";
    public static final String FECHAS = "CREATE TABLE fechas (fecha TEXT, idIngPerFec INT, FOREIGN KEY(idIngPerFec) REFERENCES ingreso_per_fecha(ingPerFechaId));";
    public static String ingresos [] = {INGRESO_PURO,INGRESO_DIARIO,INGRESO_PER_DIA,INGRESO_PER_FECHA,DIAS,FECHAS};
}
