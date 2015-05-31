package com.apps.pablo.moneyplusplusandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;

import com.apps.pablo.model.AhorroProgramado;
import com.apps.pablo.model.AhorroUnico;
import com.apps.pablo.model.GastoDiario;
import com.apps.pablo.model.GastoPeriodicoDia;
import com.apps.pablo.model.GastoPeriodicoFecha;
import com.apps.pablo.model.GastoUnico;
import com.apps.pablo.model.IngresoDiario;
import com.apps.pablo.model.IngresoPeriodicoDia;
import com.apps.pablo.model.IngresoPeriodicoFecha;
import com.apps.pablo.model.IngresoUnico;
import com.apps.pablo.model.Mes;

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
    public static final String BANDERA = "flag"; // Esto es para saber si el ingreso ya fué aprobado para el día, o bien para saber cuantas semanas faltan para que se tenga que aplicar el ingreso de nuevo en los casos de mensual y quincenal.

    public String makePlaceholders(int len) {
        String cadena = "";
        for (int i = 1; i < len; i++) {
            cadena += "?, ";
        }
        cadena += "?";
        return cadena;
    }

    public static final String TABLA_RESUMEN_MES = "meses";
    public static final String MES = "mes";
    public static final String ANIO = "anio";
    public static final String INGRESOS = "ingresos";
    public static final String GASTOS = "gastos";

    public static final String RESUMEN_MES = "CREATE TABLE " + TABLA_RESUMEN_MES + " (" + MES + " TEXT, " + ANIO + " TEXT,"
            + INGRESOS + " TEXT, " + GASTOS + " TEXT, PRIMARY KEY(" + MES + ", " + ANIO + "));";

    public ContentValues valoresResumenMes(Mes m) {
        ContentValues values = new ContentValues();
        values.put(MES, m.getMes());
        values.put(ANIO, m.getAnio());
        values.put(INGRESOS, m.getIngresos());
        values.put(GASTOS, m.getGastos());
        return values;
    }

    public boolean insertar(Mes m) {
        return db.insert(TABLA_ING_PURO, null, valoresResumenMes(m)) != -1;
    }

    // ------------------- TABLAS DE INGRESOS ---------------------- //

    public static final String TABLA_ING_PURO = "ingreso_puro";
    public static final String ID_ING_PURO = "_id";

    public static final String INGRESO_PURO = "CREATE TABLE " + TABLA_ING_PURO + " (" + ID_ING_PURO +
            " INTEGER primary key autoincrement, " + MONTO + " INTEGER, " + DESCRIPCION +
            " TEXT, " + FECHA + " TEXT);";

    public static final String TABLA_ING_DIA = "ingreso_per_dia";
    public static final String ID_ING_DIA = "_id";

    public static final String INGRESO_PER_DIA = "CREATE TABLE " + TABLA_ING_DIA + " (" + ID_ING_DIA + " INTEGER primary key autoincrement, " +
            MONTO + " INTEGER, " + DESCRIPCION + " TEXT, " + DIA + " TEXT, " + FRECUENCIA + " TEXT, " + BANDERA + " TEXT);";

    public static final String TABLA_ING_DIARIO = "ingreso_diario";
    public static final String ID_ING_DIARIO = "_id";

    public static final String INGRESO_DIARIO = "CREATE TABLE " + TABLA_ING_DIARIO + " ( " + ID_ING_DIARIO + " INTEGER primary key autoincrement, " +
            MONTO + " INTEGER, " + DESCRIPCION + " TEXT, " + BANDERA + " TEXT);";

    public static final String TABLA_DIAS = "dias";
    public static final String FK_ING_DIARIO = "idIngDiario";

    public static final String DIAS = "CREATE TABLE " + TABLA_DIAS + " ( _id INTEGER primary key autoincrement," + DIA + " TEXT, " + FK_ING_DIARIO +
            " INT, FOREIGN KEY(" + FK_ING_DIARIO + ") REFERENCES " +
            TABLA_ING_DIARIO + "(" + ID_ING_DIARIO + "));";

    public static final String TABLA_ING_FECHA = "ingreso_per_fecha";
    public static final String ID_ING_FECHA = "_id";

    public static final String INGRESO_PER_FECHA = "CREATE TABLE " + TABLA_ING_FECHA + " (" + ID_ING_FECHA + " INTEGER primary key autoincrement, " +
            MONTO + " INTEGER, " + DESCRIPCION + " TEXT, " + FRECUENCIA + " TEXT, " + BANDERA + " TEXT);";

    public static final String TABLA_FECHAS = "fechas";
    public static final String FK_ING_FECHAS = "idIngPerFec";

    public static final String FECHAS = "CREATE TABLE " + TABLA_FECHAS + " ( _id INTEGER primary key autoincrement, " + FECHA + " TEXT, " + FK_ING_FECHAS +
            " INT, FOREIGN KEY(" + FK_ING_FECHAS + ") REFERENCES " + TABLA_ING_FECHA + "(" + ID_ING_FECHA + "));";

    public static String ingresos[] = {INGRESO_PURO, INGRESO_DIARIO, INGRESO_PER_DIA, INGRESO_PER_FECHA, DIAS, FECHAS};

    protected static DBHelper helper = null;
    protected SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public ContentValues valoresIngresoPuro(IngresoUnico i) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(i.getFecha());
        ContentValues values = new ContentValues();
        values.put(MONTO, i.getMonto());
        values.put(DESCRIPCION, i.getDescripcion());
        values.put(FECHA, date);
        return values;
    }

    public boolean insertar(IngresoUnico i) {
        return db.insert(TABLA_ING_PURO, null, valoresIngresoPuro(i)) != -1;
    }

    public ContentValues valoresIngresoDiario(IngresoDiario i) {
        ContentValues values = new ContentValues();
        values.put(MONTO, i.getMonto());
        values.put(DESCRIPCION, i.getDescripcion());
        values.put(BANDERA, "false");
        return values;
    }

    public boolean insertar(IngresoDiario i) {
        long id = db.insert(TABLA_ING_DIARIO, null, valoresIngresoDiario(i));
        if (id != -1) {
            ContentValues valoresDias = new ContentValues();
            for (String s : i.getDias()) {
                valoresDias.put(DIA, s);
                valoresDias.put(FK_ING_DIARIO, id);
                if (db.insert(TABLA_DIAS, null, valoresDias) == -1)
                    return false;
            }
            return true;
        } else
            return false;
    }

    public ContentValues valoresIngresoPerDia(IngresoPeriodicoDia i) {
        ContentValues values = new ContentValues();
        values.put(MONTO, i.getMonto());
        values.put(DESCRIPCION, i.getDescripcion());
        values.put(DIA, i.getDia());
        values.put(FRECUENCIA, i.getFrecuencia());
        values.put(BANDERA, "0");
        return values;
    }

    public boolean insertar(IngresoPeriodicoDia i) {
        return db.insert(TABLA_ING_DIA, null, valoresIngresoPerDia(i)) != -1;
    }

    public ContentValues valoresIngresoFechas(IngresoPeriodicoFecha i) {
        ContentValues values = new ContentValues();
        values.put(MONTO, i.getMonto());
        values.put(DESCRIPCION, i.getDescripcion());
        values.put(FRECUENCIA, i.getFrecuencia());
        values.put(BANDERA, "0");
        return values;
    }

    public boolean insertar(IngresoPeriodicoFecha i) {
        long id = db.insert(TABLA_ING_FECHA, null, valoresIngresoFechas(i));
        if (id != -1) {
            Log.i("pablengue", "entró aqui");
            ContentValues valoresFechas = new ContentValues();
            for (String s : i.getFechas()) {
                valoresFechas.put(FECHA, s);
                valoresFechas.put(FK_ING_FECHAS, id);
                if (db.insert(TABLA_FECHAS, null, valoresFechas) == -1)
                    return false;
            }
            return true;
        } else
            return false;
    }

    public boolean eliminaIngUnico(String id) {
        return db.delete(TABLA_ING_PURO, ID_ING_PURO + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean eliminaIngDiario(String id) {
        return db.delete(TABLA_ING_DIARIO, ID_ING_DIARIO + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean eliminaIngPerFecha(String id) {
        return db.delete(TABLA_ING_FECHA, ID_ING_FECHA + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean eliminaIngPerDia(String id) {
        return db.delete(TABLA_ING_DIA, ID_ING_DIA + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public Cursor insertCursorIngPuro() {
        String columnas[] = new String[]{ID_ING_PURO, MONTO, DESCRIPCION, FECHA};
        return db.query(TABLA_ING_PURO, columnas, null, null, null, null, null);
    }

    public Cursor cargaCursorIngPuro(String mes) {
        String columnas[] = new String[]{ID_ING_PURO, MONTO, DESCRIPCION, FECHA};
        return db.query(TABLA_ING_PURO, columnas, "strftime('%m', " + FECHA + ")=?", new String[]{mes}, null, null, null);
    }

    public Cursor cargaCursorDias(String dia) {
        String columnas[] = new String[]{FK_ING_DIARIO, DIA};
        return db.query(TABLA_DIAS, columnas, DIA + "=?", new String[]{dia}, null, null, null);
    }

    public Cursor cargaCursorIngDiario(String[] id, String fecha) {
        String columnas[] = new String[]{ID_ING_DIARIO, MONTO, DESCRIPCION};
        String arrFecha[] = new String[]{fecha};
        String arreglo[] = new String[id.length + arrFecha.length];
        System.arraycopy(id, 0, arreglo, 0, id.length);
        System.arraycopy(arrFecha, 0, arreglo, id.length, arrFecha.length);
        return db.query(TABLA_ING_DIARIO, columnas, ID_ING_DIARIO + " IN(" + makePlaceholders(id.length) + ")" + " AND " + BANDERA + "!=?", arreglo, null, null, null);
    }

    public void cambiaEstadoIngPerDia(String id, String flag) {
        ContentValues values = new ContentValues();
        values.put(BANDERA, flag);
        db.update(TABLA_ING_DIA, values, ID_ING_DIA + "=?", new String[]{id});
    }

    public void cambiaEstadoIngDiario(String id, String flag) {
        ContentValues values = new ContentValues();
        values.put(BANDERA, flag);
        db.update(TABLA_ING_DIARIO, values, ID_ING_DIARIO + "=?", new String[]{id});
    }

    public Cursor cargaCursorIngPerDia(String dia) {
        String columnas[] = new String[]{ID_ING_DIA, MONTO, DESCRIPCION, DIA, FRECUENCIA};
        return db.query(TABLA_ING_DIA, columnas, DIA + "=?", new String[]{dia}, null, null, null);
    }

    public Cursor cargaCursorIngPerDia2(String dia) {
        String columnas[] = new String[]{ID_ING_DIA, MONTO, DESCRIPCION, DIA, FRECUENCIA};
        return db.query(TABLA_ING_DIA, columnas, DIA + "=?" + " AND " + BANDERA + "=?", new String[]{dia, "0"}, null, null, null);
    }

    public Cursor cargaCursorFechas(String fecha) {
        String columnas[] = new String[]{FK_ING_FECHAS, FECHA};
        return db.query(TABLA_FECHAS, columnas, FECHA + "=?", new String[]{fecha}, null, null, null);
    }

    public Cursor cargaCursorFechas(String[] fechas) {
        String columnas[] = new String[]{FK_ING_FECHAS, FECHA};
        return db.query(TABLA_FECHAS, columnas, FECHA + "IN(" + makePlaceholders(fechas.length) + ")", fechas, null, null, null);
    }

    public Cursor cargaCursorIngPerFecha(String[] id) {
        String columnas[] = new String[]{ID_ING_PURO, MONTO, DESCRIPCION, FRECUENCIA};
        return db.query(TABLA_ING_FECHA, columnas, ID_ING_FECHA + " IN(" + makePlaceholders(id.length) + ")", id, null, null, null);
    }


    // ------------------- TABLAS DE GASTOS ------------------------ //
    public static final String TABLA_GASTO_PURO = "gasto_puro";
    public static final String ID_GASTO_PURO = "_id";

    public static final String GASTO_PURO = "CREATE TABLE " + TABLA_GASTO_PURO + " (" + ID_GASTO_PURO +
            " INTEGER primary key autoincrement, " + MONTO + " INTEGER, " + TIPO + " TEXT, " + DESCRIPCION +
            " TEXT, " + FECHA + " TEXT);";

    public static final String TABLA_GASTO_DIA = "gasto_per_dia";
    public static final String ID_GASTO_DIA = "_id";

    public static final String GASTO_PER_DIA = "CREATE TABLE " + TABLA_GASTO_DIA + " (" + ID_GASTO_DIA + " INTEGER primary key autoincrement, " +
            MONTO + " INTEGER, " + TIPO + " TEXT, " + DESCRIPCION + " TEXT, " + DIA + " TEXT, " + FRECUENCIA + " TEXT, " + BANDERA + " TEXT);";

    public static final String TABLA_GASTO_DIARIO = "gasto_diario";
    public static final String ID_GASTO_DIARIO = "_id";

    public static final String GASTO_DIARIO = "CREATE TABLE " + TABLA_GASTO_DIARIO + " (" + ID_GASTO_DIARIO + " INTEGER primary key autoincrement, " +
            MONTO + " INTEGER, " + TIPO + " TEXT, " + DESCRIPCION + " TEXT, " + BANDERA + " TEXT);";

    public static final String TABLA_DIAS_GASTOS = "dias_gastos";
    public static final String FK_GASTO_DIARIO = "idGastoDiario";

    public static final String DIAS_GASTOS = "CREATE TABLE " + TABLA_DIAS_GASTOS + " ( _id INTEGER primary key autoincrement, " + DIA + " TEXT, " + FK_GASTO_DIARIO +
            " INT, FOREIGN KEY(" + FK_GASTO_DIARIO + ") REFERENCES " +
            TABLA_GASTO_DIARIO + "(" + ID_GASTO_DIARIO + "));";

    public static final String TABLA_GASTO_FECHA = "gasto_per_fecha";
    public static final String ID_GASTO_FECHA = "_id";

    public static final String GASTO_PER_FECHA = "CREATE TABLE " + TABLA_GASTO_FECHA + " (" + ID_GASTO_FECHA + " INTEGER primary key autoincrement, " +
            MONTO + " INTEGER, " + TIPO + " TEXT, " + DESCRIPCION + " TEXT, " + FRECUENCIA + " TEXT);";

    public static final String TABLA_FECHAS_GASTOS = "fechas_gastos";
    public static final String FK_GASTO_FECHAS = "idGastoPerFec";

    public static final String FECHAS_GASTOS = "CREATE TABLE " + TABLA_FECHAS_GASTOS + " ( _id INTEGER primary key autoincrement, " + FECHA + " TEXT, " + FK_GASTO_FECHAS +
            " INT, FOREIGN KEY(" + FK_GASTO_FECHAS + ") REFERENCES " + TABLA_GASTO_FECHA + "(" + ID_GASTO_FECHA + "));";
    public static String gastos[] = {GASTO_PURO, GASTO_DIARIO, GASTO_PER_DIA, GASTO_PER_FECHA, DIAS_GASTOS, FECHAS_GASTOS};


    public ContentValues valoresGastoPuro(GastoUnico i) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(i.getFecha());
        ContentValues values = new ContentValues();
        values.put(MONTO, i.getMonto());
        values.put(DESCRIPCION, i.getDescripcion());
        values.put(TIPO, i.getTipo());
        values.put(FECHA, date);
        return values;
    }

    public boolean insertar(GastoUnico i) {
        return db.insert(TABLA_GASTO_PURO, null, valoresGastoPuro(i)) != -1;
    }

    public ContentValues valoresGastoDiario(GastoDiario i) {
        ContentValues values = new ContentValues();
        values.put(MONTO, i.getMonto());
        values.put(DESCRIPCION, i.getDescripcion());
        values.put(TIPO, i.getTipo());
        values.put(BANDERA, "false");
        return values;
    }

    public boolean insertar(GastoDiario i) {
        long id = db.insert(TABLA_GASTO_DIARIO, null, valoresGastoDiario(i));
        if (id != -1) {
            ContentValues valoresDias = new ContentValues();
            for (String s : i.getDias()) {
                valoresDias.put(DIA, s);
                valoresDias.put(FK_GASTO_DIARIO, id);
                if (db.insert(TABLA_DIAS_GASTOS, null, valoresDias) == -1)
                    return false;
            }
            return true;
        } else
            return false;
    }

    public ContentValues valoresGastoPerDia(GastoPeriodicoDia i) {
        ContentValues values = new ContentValues();
        values.put(MONTO, i.getMonto());
        values.put(DESCRIPCION, i.getDescripcion());
        values.put(TIPO, i.getTipo());
        values.put(DIA, i.getDia());
        values.put(FRECUENCIA, i.getFrecuencia());
        values.put(BANDERA, "0");
        return values;
    }

    public boolean insertar(GastoPeriodicoDia i) {

        return db.insert(TABLA_GASTO_DIA, null, valoresGastoPerDia(i)) != -1;
    }

    public ContentValues valoresGastoFechas(GastoPeriodicoFecha i) {
        ContentValues values = new ContentValues();
        values.put(MONTO, i.getMonto());
        values.put(DESCRIPCION, i.getDescripcion());
        values.put(TIPO, i.getTipo());
        values.put(FRECUENCIA, i.getFrecuencia());
        return values;
    }

    public boolean insertar(GastoPeriodicoFecha i) {
        long id = db.insert(TABLA_GASTO_FECHA, null, valoresGastoFechas(i));
        if (id != -1) {
            ContentValues valoresFechas = new ContentValues();
            for (String s : i.getFechas()) {
                valoresFechas.put(FECHA, s);
                valoresFechas.put(FK_GASTO_FECHAS, id);
                if (db.insert(TABLA_FECHAS_GASTOS, null, valoresFechas) == -1)
                    return false;
            }
            return true;
        } else
            return false;
    }

    public boolean eliminaGastoUnico(String id) {
        return db.delete(TABLA_GASTO_PURO, ID_GASTO_PURO + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean eliminaGastoDiario(String id) {
        return db.delete(TABLA_GASTO_DIARIO, ID_GASTO_DIARIO + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean eliminaGastoPerFecha(String id) {
        return db.delete(TABLA_GASTO_FECHA, ID_GASTO_FECHA + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean eliminaGastoPerDia(String id) {
        return db.delete(TABLA_GASTO_DIA, ID_GASTO_DIA + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public Cursor cargaCursorGastoPuro() {
        String columnas[] = new String[]{ID_ING_PURO, MONTO, TIPO, DESCRIPCION, FECHA};
        return db.query(TABLA_ING_PURO, columnas, null, null, null, null, null);
    }

    public Cursor cargaCursorGastoPuro(String mes) {
        String columnas[] = new String[]{ID_GASTO_PURO, MONTO, TIPO, DESCRIPCION, FECHA};
        return db.query(TABLA_GASTO_PURO, columnas, "strftime('%m', " + FECHA + ")=?", new String[]{mes}, null, null, null);
    }

    public Cursor cargaCursorDiasGasto(String dia) {
        String columnas[] = new String[]{FK_GASTO_DIARIO, DIA};
        return db.query(TABLA_DIAS_GASTOS, columnas, DIA + "=?", new String[]{dia}, null, null, null);
    }

    public Cursor cargaCursorGastoDiario(String[] id, String fecha) {
        String columnas[] = new String[]{ID_GASTO_DIARIO, MONTO, TIPO, DESCRIPCION};
        String arrFecha[] = new String[]{fecha};
        String arreglo[] = new String[id.length + arrFecha.length];
        System.arraycopy(id, 0, arreglo, 0, id.length);
        System.arraycopy(arrFecha, 0, arreglo, id.length, arrFecha.length);
        return db.query(TABLA_GASTO_DIARIO, columnas, ID_GASTO_DIARIO + " IN(" + makePlaceholders(id.length) + ")" + " AND " + BANDERA + "!=?", arreglo, null, null, null);
    }

    public void cambiaEstadoGastoPerDia(String id, String flag) {
        ContentValues values = new ContentValues();
        values.put(BANDERA, flag);
        db.update(TABLA_GASTO_DIA, values, ID_GASTO_DIA + "=?", new String[]{id});
    }

    public void cambiaEstadoGastoDiario(String id, String flag) {
        ContentValues values = new ContentValues();
        values.put(BANDERA, flag);
        db.update(TABLA_GASTO_DIARIO, values, ID_GASTO_DIARIO + "=?", new String[]{id});
    }

    public Cursor cargaCursorGastoPerDia(String dia) {
        String columnas[] = new String[]{ID_GASTO_DIA, MONTO, TIPO, DESCRIPCION, DIA, FRECUENCIA};
        return db.query(TABLA_GASTO_DIA, columnas, DIA + "=?", new String[]{dia}, null, null, null);
    }

    public Cursor cargaCursorGastoPerDia2(String dia) {
        String columnas[] = new String[]{ID_ING_DIA, MONTO, TIPO, DESCRIPCION, DIA, FRECUENCIA};
        return db.query(TABLA_GASTO_DIA, columnas, DIA + "=?" + " AND " + BANDERA + "=?", new String[]{dia, "0"}, null, null, null);
    }

    public Cursor cargaCursorFechasGasto(String fecha) {
        String columnas[] = new String[]{FK_GASTO_FECHAS, FECHA};
        return db.query(TABLA_FECHAS_GASTOS, columnas, FECHA + "=?", new String[]{fecha}, null, null, null);
    }

    public Cursor cargaCursorGastoPerFecha(String[] id) {
        String columnas[] = new String[]{ID_ING_PURO, MONTO, TIPO, DESCRIPCION, FRECUENCIA};
        return db.query(TABLA_GASTO_FECHA, columnas, ID_GASTO_FECHA + " IN(" + makePlaceholders(id.length) + ")", id, null, null, null);
    }

    // ------------------- TABLAS DE GASTOS ------------------------ //
    public static final String TABLA_AHORRO_PURO = "ahorro_puro";
    public static final String ID_AHORRO_PURO = "_id";

    public static final String AHORRO_PURO = "CREATE TABLE " + TABLA_AHORRO_PURO + " (" + ID_AHORRO_PURO +
            " INTEGER primary key autoincrement, " + MONTO + " INTEGER, " + DESCRIPCION +
            " TEXT, " + FECHA + " TEXT);";

    public static final String TABLA_AHORRO_PROGRAMADO = "ahorro_programado";
    public static final String ID_AHORRO_PROGRAMADO = "_id";

    public static final String AHORRO_PROGRAMADO = "CREATE TABLE " + TABLA_AHORRO_PROGRAMADO + " (" + ID_AHORRO_PROGRAMADO + " INTEGER primary key autoincrement, " +
            MONTO + " INTEGER, " + DESCRIPCION + " TEXT, " + FRECUENCIA + " TEXT);";

    public static final String TABLA_FECHAS_AHORROS = "fechas_ahorros";
    public static final String FK_AHORRO_FECHA = "idAhorroProgram";

    public static final String FECHAS_AHORROS = "CREATE TABLE " + TABLA_FECHAS_AHORROS + " ( _id INTEGER primary key autoincrement, " + FECHA + " TEXT, " + FK_AHORRO_FECHA +
            " INT, FOREIGN KEY(" + FK_AHORRO_FECHA + ") REFERENCES " + TABLA_AHORRO_PROGRAMADO + "(" + ID_AHORRO_PROGRAMADO + "));";

    public ContentValues valoresAhorroPuro(AhorroUnico a) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(a.getFecha());
        ContentValues values = new ContentValues();
        values.put(MONTO, a.getMonto());
        values.put(DESCRIPCION, a.getDescripcion());
        values.put(FECHA, date);
        return values;
    }

    public boolean insertar(AhorroUnico a) {
        return db.insert(TABLA_AHORRO_PURO, null, valoresAhorroPuro(a)) != -1;
    }

    public boolean eliminaAhorroUnico(String id) {
        return db.delete(TABLA_AHORRO_PURO, ID_AHORRO_PURO + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public ContentValues valoresAhorroProgramado(AhorroProgramado a) {
        ContentValues values = new ContentValues();
        values.put(MONTO, a.getMonto());
        values.put(DESCRIPCION, a.getDescripcion());
        values.put(FRECUENCIA, a.getFrecuencia());
        return values;
    }

    public boolean insertar(AhorroProgramado a) {
        long id = db.insert(TABLA_ING_FECHA, null, valoresAhorroProgramado(a));
        if (id != -1) {
            Log.i("pablengue", "entró aqui");
            ContentValues valoresFechas = new ContentValues();
            for (String s : a.getFechas()) {
                valoresFechas.put(FECHA, s);
                valoresFechas.put(FK_AHORRO_FECHA, id);
                if (db.insert(TABLA_FECHAS_AHORROS, null, valoresFechas) == -1)
                    return false;
            }
            return true;
        } else
            return false;
    }

    public boolean eliminaAhorroProgramado(String id) {
        String [] ids = new String[]{String.valueOf(id)};
        db.delete(TABLA_FECHAS_AHORROS,FK_AHORRO_FECHA + "=?", ids);
        return db.delete(TABLA_AHORRO_PROGRAMADO, ID_AHORRO_PROGRAMADO + "=?", ids) > 0;
    }

    public Cursor cargaCursorAhorroPuro(String mes) {
        String columnas[] = new String[]{ID_AHORRO_PURO, MONTO, DESCRIPCION, FECHA};
        return db.query(TABLA_AHORRO_PURO, columnas, "strftime('%m', " + FECHA + ")=?", new String[]{mes}, null, null, null);
    }

    public Cursor cargaCursorFechasAhorro(String fecha) {
        String columnas[] = new String[]{FK_AHORRO_FECHA, FECHA};
        return db.query(TABLA_FECHAS_AHORROS, columnas, FECHA + "=?", new String[]{fecha}, null, null, null);
    }

    public Cursor cargaCursorAhorroProgramado(String[] id) {
        String columnas[] = new String[]{ID_AHORRO_PROGRAMADO, MONTO, DESCRIPCION, FRECUENCIA};
        return db.query(TABLA_AHORRO_PROGRAMADO, columnas, ID_AHORRO_PROGRAMADO + " IN(" + makePlaceholders(id.length) + ")", id, null, null, null);
    }

    public static String ahorros[] = {AHORRO_PURO, AHORRO_PROGRAMADO, FECHAS_AHORROS};
}
