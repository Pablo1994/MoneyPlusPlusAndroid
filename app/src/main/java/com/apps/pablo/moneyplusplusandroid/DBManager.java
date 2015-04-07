package com.apps.pablo.moneyplusplusandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;

import model.IngresoDiario;
import model.IngresoPeriodicoDia;
import model.IngresoPeriodicoFecha;
import model.IngresoUnico;

/**
 * Created by pablo on 06/04/15.
 */
public class DBManager {
    public static final String MONTO = "monto";
    public static final String DESCRIPCION = "descripcion";
    public static final String FECHA = "fecha";

    public static final String TABLA_ING_PURO = "ingreso_puro";
    public static final String ID_ING_PURO = "ingPuroId";

    public static final String INGRESO_PURO = "CREATE TABLE " + TABLA_ING_PURO + " (" + ID_ING_PURO +
            " INTEGER primary key autoincrement, " + MONTO + " INTEGER, " + DESCRIPCION +
            " TEXT, " + FECHA + " TEXT);";
    public static final String INGRESO_PER_DIA = "CREATE TABLE ingreso_per_dia (ingPerDiaId INTEGER primary key autoincrement, monto INTEGER, descripcion TEXT, dia TEXT, frecuencia TEXT);";
    public static final String INGRESO_DIARIO = "CREATE TABLE ingreso_diario (ingDiarioId INTEGER primary key autoincrement, monto INTEGER, descripcion TEXT);";
    public static final String DIAS = "CREATE TABLE dias (dia TEXT, idIngDiario INT, FOREIGN KEY(idIngDiario) REFERENCES ingreso_diario(ingDiarioId));";
    public static final String INGRESO_PER_FECHA = "CREATE TABLE ingreso_per_fecha (ingPerFechaId INTEGER primary key autoincrement, monto INTEGER, descripcion TEXT, frecuencia TEXT);";
    public static final String FECHAS = "CREATE TABLE fechas (fecha TEXT, idIngPerFec INT, FOREIGN KEY(idIngPerFec) REFERENCES ingreso_per_fecha(ingPerFechaId));";
    public static String ingresos [] = {INGRESO_PURO,INGRESO_DIARIO,INGRESO_PER_DIA,INGRESO_PER_FECHA,DIAS,FECHAS};

    protected static DBHelper helper = null;
    protected SQLiteDatabase db;

    public DBManager(Context context) {
        helper= new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public ContentValues valoresIngresoPuro(IngresoUnico i){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(i.getFecha());
        ContentValues values = new ContentValues();
        values.put(MONTO,i.getMonto());
        values.put(DESCRIPCION,i.getDescripcion());
        values.put(FECHA,date);
        return values;
    }
    public boolean insertar(IngresoUnico i){
        return db.insert(TABLA_ING_PURO, null, valoresIngresoPuro(i)) != -1;
    }

    public void insertar(IngresoDiario i){

    }
    public void insertar(IngresoPeriodicoDia i){

    }
    public void insertar(IngresoPeriodicoFecha i){

    }
}
