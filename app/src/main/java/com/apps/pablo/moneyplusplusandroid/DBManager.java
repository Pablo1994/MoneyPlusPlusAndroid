package com.apps.pablo.moneyplusplusandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;

import model.GastoDiario;
import model.GastoPeriodicoDia;
import model.GastoPeriodicoFecha;
import model.GastoUnico;
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
    public static final String TIPO = "tipo";
    public static final String BANDERA = "flag";

    public String makePlaceholders(int len){
        String cadena = "";
        for(int i=1;i<len;i++){
            cadena += "?, ";
        }
        cadena += "?";
        return cadena;
    }
    // ------------------- TABLAS DE INGRESOS ---------------------- //

    public static final String TABLA_ING_PURO = "ingreso_puro";
    public static final String ID_ING_PURO = "ingPuroId";

    public static final String INGRESO_PURO = "CREATE TABLE " + TABLA_ING_PURO + " (" + ID_ING_PURO +
            " INTEGER primary key autoincrement, " + MONTO + " INTEGER, " + DESCRIPCION +
            " TEXT, " + FECHA + " TEXT);";

    public static final String TABLA_ING_DIA = "ingreso_per_dia";
    public static final String ID_ING_DIA = "ingPerDiaId";

    public static final String INGRESO_PER_DIA = "CREATE TABLE " + TABLA_ING_DIA + " (" + ID_ING_DIA + " INTEGER primary key autoincrement, " +
            MONTO + " INTEGER, " + DESCRIPCION + " TEXT, " + DIA + " TEXT, " + FRECUENCIA + " TEXT, " + BANDERA + " TEXT);";

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
        Log.i("inserta","id " + id);
        if(id != -1){
            ContentValues valoresDias = new ContentValues();
            for(String s : i.getDias()){
                valoresDias.put(DIA,s);
                valoresDias.put(FK_ING_DIARIO,id);
                if(db.insert(TABLA_DIAS,null,valoresDias) == -1)
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
        values.put(BANDERA,"0");
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
        long id = db.insert(TABLA_ING_FECHA, null, valoresIngresoFechas(i));
        if(id != -1){
            ContentValues valoresFechas = new ContentValues();
            for(String s : i.getFechas()){
                valoresFechas.put(FECHA,s);
                valoresFechas.put(FK_ING_FECHAS,id);
                if(db.insert(FECHAS,null,valoresFechas) == -1)
                    return false;
            }
            return true;
        }
        else
            return false;
    }

    public boolean eliminaIngUnico(int id){
        return db.delete(TABLA_ING_PURO,ID_ING_PURO + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean eliminaIngDiario(int id){
        return db.delete(TABLA_ING_PURO,ID_ING_DIARIO+"=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean eliminaIngPerFecha(int id){
        return db.delete(TABLA_ING_PURO,ID_ING_FECHA+"=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean eliminaIngPerDia(int id){
        return db.delete(TABLA_ING_PURO,ID_ING_DIA+"=?", new String[]{String.valueOf(id)}) > 0;
    }

    public Cursor cargaCursorIngPuro(){
        String columnas [] = new String[]{ID_ING_PURO,MONTO,DESCRIPCION,FECHA};
        return db.query(TABLA_ING_PURO, columnas,null,null,null,null,null);
    }

    public Cursor cargaCursorDias(String dia){
        String columnas [] = new String[]{FK_ING_DIARIO,DIA};
        return db.query(TABLA_DIAS,columnas,DIA + "=?",new String[]{dia},null,null,null);
    }

    public Cursor cargaCursorIngDiario(String [] id){
        String columnas [] = new String[]{ID_ING_DIARIO,MONTO,DESCRIPCION};
        return db.query(TABLA_ING_DIARIO, columnas,ID_ING_DIARIO + "IN(" + makePlaceholders(id.length) + ")",id,null,null,null);
    }

    public void cambiaEstadoIngPerDia(String id,String flag){
        ContentValues values = new ContentValues();
        values.put(BANDERA,flag);
        db.update(TABLA_ING_DIA,values,ID_ING_DIA+"=?",new String[]{id});
    }
    public Cursor cargaCursorIngPerDia(String dia){
        String columnas [] = new String[]{ID_ING_DIA,MONTO,DESCRIPCION,DIA,FRECUENCIA};
        return db.query(TABLA_ING_DIA, columnas,DIA + "=?",new String[]{dia},null,null,null);
    }

    public Cursor cargaCursorIngPerDia2(String dia){
        String columnas [] = new String[]{ID_ING_DIA,MONTO,DESCRIPCION,DIA,FRECUENCIA};
        return db.query(TABLA_ING_DIA, columnas,DIA + "=?" + "AND " + BANDERA + "=?",new String[]{dia,"0"},null,null,null);
    }

    public Cursor cargaCursorFechas(String fecha){
        String columnas [] = new String[]{FK_ING_FECHAS,FECHA};
        return db.query(TABLA_FECHAS,columnas,FECHA + "=?",new String[]{fecha},null,null,null);
    }

    public Cursor cargaCursorIngPerFecha(String [] id){
        String columnas [] = new String[]{ID_ING_PURO,MONTO,DESCRIPCION,FRECUENCIA};
        return db.query(TABLA_ING_FECHA, columnas,ID_ING_FECHA + "IN(" + makePlaceholders(id.length) + ")",id,null,null,null);
    }


    // ------------------- TABLAS DE GASTOS ------------------------ //
    public static final String TABLA_GASTO_PURO = "gasto_puro";
    public static final String ID_GASTO_PURO = "gastoPuroId";

    public static final String GASTO_PURO = "CREATE TABLE " + TABLA_GASTO_PURO + " (" + ID_GASTO_PURO +
            " INTEGER primary key autoincrement, " + MONTO + " INTEGER, " + DESCRIPCION +
            " TEXT, "+ FECHA + " TEXT);";

    public static final String TABLA_GASTO_DIA = "gasto_per_dia";
    public static final String ID_GASTO_DIA = "gastoPerDiaId";

    public static final String GASTO_PER_DIA = "CREATE TABLE " + TABLA_GASTO_DIA + " (" + ID_GASTO_DIA + " INTEGER primary key autoincrement, " +
            MONTO + " INTEGER, " + DESCRIPCION +  " TEXT, " + DIA + " TEXT, " + FRECUENCIA + " TEXT);";

    public static final String TABLA_GASTO_DIARIO = "gasto_diario";
    public static final String ID_GASTO_DIARIO = "gastoDiarioId";

    public static final String GASTO_DIARIO = "CREATE TABLE " + TABLA_GASTO_DIARIO + " (" + ID_GASTO_DIARIO + " INTEGER primary key autoincrement, " +
            MONTO + " INTEGER, " + DESCRIPCION + " TEXT, " + TIPO +" TEXT);";

    public static final String TABLA_DIAS_GASTOS = "dias_gastos";
    public static final String FK_GASTO_DIARIO = "idGastoDiario";

    public static final String DIAS_GASTOS = "CREATE TABLE " + TABLA_DIAS_GASTOS + " (" + DIA + " TEXT, " + FK_GASTO_DIARIO +
            " INT, FOREIGN KEY(" + FK_GASTO_DIARIO+ ") REFERENCES " +
            TABLA_GASTO_DIARIO + "(" + ID_GASTO_DIARIO + "));";

    public static final String TABLA_GASTO_FECHA = "gasto_per_fecha";
    public static final String ID_GASTO_FECHA = "gastoPerFechaId";

    public static final String GASTO_PER_FECHA = "CREATE TABLE " + TABLA_GASTO_FECHA + " (" + ID_GASTO_FECHA + " INTEGER primary key autoincrement, " +
            MONTO + " INTEGER, " + DESCRIPCION + " TEXT, " + TIPO + " TEXT, " + FRECUENCIA + " TEXT);";

    public static final String TABLA_FECHAS_GASTOS = "fechas_gastos";
    public static final String FK_GASTO_FECHAS = "idGastoPerFec";

    public static final String FECHAS_GASTOS = "CREATE TABLE " + TABLA_FECHAS_GASTOS + " ("+ FECHA + " TEXT, " + FK_GASTO_FECHAS +
            " INT, FOREIGN KEY(" + FK_GASTO_FECHAS + ") REFERENCES " + TABLA_GASTO_FECHA + "(" + FK_GASTO_FECHAS +"));";
    public static String gastos [] = {GASTO_PURO,GASTO_DIARIO,GASTO_PER_DIA,GASTO_PER_FECHA,DIAS_GASTOS,FECHAS_GASTOS};


    public ContentValues valoresGastoPuro(GastoUnico i){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(i.getFecha());
        ContentValues values = new ContentValues();
        values.put(MONTO,i.getMonto());
        values.put(DESCRIPCION,i.getDescripcion());
        values.put(TIPO,i.getTipo());
        values.put(FECHA,date);
        return values;
    }
    public boolean insertar(GastoUnico i){
        return db.insert(TABLA_GASTO_PURO, null, valoresGastoPuro(i)) != -1;
    }

    public ContentValues valoresGastoDiario(GastoDiario i){
        ContentValues values = new ContentValues();
        values.put(MONTO, i.getMonto());
        values.put(DESCRIPCION, i.getDescripcion());
        values.put(TIPO,i.getTipo());
        return values;
    }
    public boolean insertar(GastoDiario i){
        long id = db.insert(TABLA_GASTO_DIARIO, null, valoresGastoDiario(i));
        if(id != -1){
            ContentValues valoresDias = new ContentValues();
            for(String s : i.getDias()){
                valoresDias.put(DIA,s);
                valoresDias.put(FK_GASTO_DIARIO,id);
                if(db.insert(DIAS_GASTOS,null,valoresDias) == -1)
                    return false;
            }
            return true;
        }
        else
            return false;
    }

    public ContentValues valoresGastoPerDia (GastoPeriodicoDia i){
        ContentValues values = new ContentValues();
        values.put(MONTO, i.getMonto());
        values.put(DESCRIPCION, i.getDescripcion());
        values.put(TIPO,i.getTipo());
        values.put(DIA,i.getDia());
        values.put(FRECUENCIA,i.getFrecuencia());
        return values;
    }
    public boolean insertar(GastoPeriodicoDia i){

        return db.insert(TABLA_GASTO_DIA, null, valoresGastoPerDia(i)) != -1;
    }
    public ContentValues valoresGastoFechas(GastoPeriodicoFecha i){
        ContentValues values = new ContentValues();
        values.put(MONTO, i.getMonto());
        values.put(DESCRIPCION, i.getDescripcion());
        values.put(TIPO,i.getTipo());
        values.put(FRECUENCIA, i.getFrecuencia());
        return values;
    }
    public boolean insertar(GastoPeriodicoFecha i){
        long id = db.insert(TABLA_GASTO_DIARIO, null, valoresGastoFechas(i));
        if(id != -1){
            ContentValues valoresFechas = new ContentValues();
            for(String s : i.getFechas()){
                valoresFechas.put(FECHA,s);
                valoresFechas.put(FK_GASTO_DIARIO,id);
                if(db.insert(FECHAS_GASTOS,null,valoresFechas) == -1)
                    return false;
            }
            return true;
        }
        else
            return false;
    }
    public boolean eliminaGastoUnico(int id){
        return db.delete(TABLA_GASTO_PURO,ID_GASTO_PURO + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean eliminaGastoDiario(int id){
        return db.delete(TABLA_GASTO_PURO,ID_GASTO_DIARIO+"=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean eliminaGastoPerFecha(int id){
        return db.delete(TABLA_GASTO_PURO,ID_GASTO_FECHA+"=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean eliminaGastoPerDia(int id){
        return db.delete(TABLA_GASTO_PURO,ID_GASTO_DIA+"=?", new String[]{String.valueOf(id)}) > 0;
    }

    public Cursor cargaCursorGastoPuro(){
        String columnas [] = new String[]{ID_ING_PURO,MONTO,DESCRIPCION,FECHA};
        return db.query(TABLA_ING_PURO, columnas,null,null,null,null,null);
    }

    public Cursor cargaCursorDiasGasto(String dia){
        String columnas [] = new String[]{FK_GASTO_DIARIO,DIA};
        return db.query(TABLA_DIAS_GASTOS,columnas,DIA + "=?",new String[]{dia},null,null,null);
    }

    public Cursor cargaCursorGastoDiario(String id){
        String columnas [] = new String[]{ID_GASTO_DIARIO,MONTO,DESCRIPCION};
        return db.query(TABLA_GASTO_DIARIO, columnas,ID_GASTO_DIARIO + "=?",new String[]{id},null,null,null);
    }

    public void cambiaEstadoGastoPerDia(String id,String flag){
        ContentValues values = new ContentValues();
        values.put(BANDERA,flag);
        db.update(TABLA_GASTO_DIA,values,ID_GASTO_DIA+"=?",new String[]{id});
    }
    public Cursor cargaCursorGastoPerDia(String dia){
        String columnas [] = new String[]{ID_GASTO_DIA,MONTO,DESCRIPCION,DIA,FRECUENCIA};
        return db.query(TABLA_GASTO_DIA, columnas,DIA + "=?",new String[]{dia},null,null,null);
    }

    public Cursor cargaCursorFechasGasto(String fecha){
        String columnas [] = new String[]{FK_GASTO_FECHAS,FECHA};
        return db.query(TABLA_FECHAS_GASTOS,columnas,FECHA + "=?",new String[]{fecha},null,null,null);
    }

    public Cursor cargaCursorGastoPerFecha(String id){
        String columnas [] = new String[]{ID_ING_PURO,MONTO,DESCRIPCION,FRECUENCIA};
        return db.query(TABLA_GASTO_FECHA, columnas,ID_GASTO_FECHA + "=?",new String[]{id},null,null,null);
    }
}
