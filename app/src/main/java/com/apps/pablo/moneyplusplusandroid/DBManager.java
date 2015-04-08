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
    public static final String DIA = "dia";
    public static final String FRECUENCIA = "frecuencia";

    public static final String TABLA_ING_PURO = "ingreso_puro";
    public static final String ID_ING_PURO = "ingPuroId";

    public static final String INGRESO_PURO = "CREATE TABLE " + TABLA_ING_PURO + " (" + ID_ING_PURO +
            " INTEGER primary key autoincrement, " + MONTO + " INTEGER, " + DESCRIPCION +
            " TEXT, " + FECHA + " TEXT);";

    public static final String TABLA_ING_DIA = "ingreso_per_dia";
    public static final String ID_ING_DIA = "ingPerDiaId";

    public static final String INGRESO_PER_DIA = "CREATE TABLE " + TABLA_ING_DIA + " (" + ID_ING_DIA + " INTEGER primary key autoincrement, " +
            MONTO + " INTEGER, " + DESCRIPCION + " TEXT, " + DIA + " TEXT, " + FRECUENCIA + " TEXT);";

    public static final String TABLA_ING_DIARIO = "ingreso_diario";
    public static final String ID_ING_DIARIO = "ingDiarioId";

    public static final String INGRESO_DIARIO = "CREATE TABLE " + TABLA_ING_DIARIO + " (" + ID_ING_DIARIO + " INTEGER primary key autoincrement, " +
            MONTO + " INTEGER, " + DESCRIPCION + " TEXT);";

    public static final String TABLA_DIAS = "dias";
    public static final String FK_ING_DIARIO = "idIngDiario";

    public static final String DIAS = "CREATE TABLE " + TABLA_DIAS + " (" + DIA + " TEXT, " + FK_ING_DIARIO +
            " INT, FOREIGN KEY(" + FK_ING_DIARIO+ ") REFERENCES " +
            TABLA_ING_DIARIO + "(" + ID_ING_DIARIO + "));";

    public static final String TABLA_ING_FECHA = "ingreso_per_fecha";
    public static final String ID_ING_FECHA = "ingPerFechaId";

    public static final String INGRESO_PER_FECHA = "CREATE TABLE " + TABLA_ING_FECHA + " (" + ID_ING_FECHA + " INTEGER primary key autoincrement, " +
            MONTO + " INTEGER, " + DESCRIPCION + " TEXT, " + FRECUENCIA + " TEXT);";

    public static final String TABLA_FECHAS = "fechas";
    public static final String FK_ING_FECHAS = "idIngPerFec";

    public static final String FECHAS = "CREATE TABLE " + TABLA_FECHAS + " ("+ FECHA + " TEXT, " + FK_ING_FECHAS +
            " INT, FOREIGN KEY(" + FK_ING_FECHAS + ") REFERENCES " + TABLA_ING_FECHA + "(" + FK_ING_FECHAS +"));";
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

    public ContentValues valoresIngresoDiario(IngresoDiario i){
        ContentValues values = new ContentValues();
        values.put(MONTO, i.getMonto());
        values.put(DESCRIPCION, i.getDescripcion());
        return values;
    }
    public boolean insertar(IngresoDiario i){
        long id = db.insert(TABLA_ING_DIARIO, null, valoresIngresoDiario(i));
        if(id != -1){
            ContentValues valoresDias = new ContentValues();
            for(String s : i.getDias()){
                valoresDias.put(DIA,s);
                valoresDias.put(FK_ING_DIARIO,id);
                if(db.insert(DIAS,null,valoresDias) == -1)
                    return false;
            }
            return true;
        }
        else
            return false;
    }

    public ContentValues valoresIngresoPerDia (IngresoPeriodicoDia i){
        ContentValues values = new ContentValues();
        values.put(MONTO, i.getMonto());
        values.put(DESCRIPCION, i.getDescripcion());
        values.put(DIA,i.getDia());
        values.put(FRECUENCIA,i.getFrecuencia());
        return values;
    }
    public boolean insertar(IngresoPeriodicoDia i){

        return db.insert(TABLA_ING_DIA, null, valoresIngresoPerDia(i)) != -1;
    }
    public ContentValues valoresIngresoFechas(IngresoPeriodicoFecha i){
        ContentValues values = new ContentValues();
        values.put(MONTO, i.getMonto());
        values.put(DESCRIPCION, i.getDescripcion());
        values.put(FRECUENCIA, i.getFrecuencia());
        return values;
    }
    public boolean insertar(IngresoPeriodicoFecha i){
        long id = db.insert(TABLA_ING_DIARIO, null, valoresIngresoFechas(i));
        if(id != -1){
            ContentValues valoresFechas = new ContentValues();
            for(String s : i.getFechas()){
                valoresFechas.put(DIA,s);
                valoresFechas.put(FK_ING_DIARIO,id);
                if(db.insert(DIAS,null,valoresFechas) == -1)
                    return false;
            }
            return true;
        }
        else
            return false;
    }
}
