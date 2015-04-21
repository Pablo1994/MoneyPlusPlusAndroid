package com.apps.pablo.moneyplusplusandroid;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.GastoUnico;

/**
 * Created by Pablo Arias on 08/04/15.
 */
public class TabGastosProgramados extends BaseFragment implements View.OnCreateContextMenuListener{
    DBManager manager;
    View rootView;
    ListView listaDiario, listaPerDia, listaPerFecha;
    SimpleCursorAdapter adapterDiario, adapterPerDia, adapterPerFecha;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_gastos_programados, container, false);
        manager = ResumenDia.manager;
        listaDiario = (ListView) rootView.findViewById(R.id.listViewGastoDiario);
        listaPerDia = (ListView) rootView.findViewById(R.id.listViewGastoPerDia);
        listaPerFecha = (ListView) rootView.findViewById(R.id.listViewGastoPerFecha);
        listaDiario.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                getActivity().getMenuInflater().inflate(R.menu.menu_gasto_d, menu);
            }
        });
        listaPerDia.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                getActivity().getMenuInflater().inflate(R.menu.menu_gasto_pd, contextMenu);
            }
        });
        listaPerFecha.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                getActivity().getMenuInflater().inflate(R.menu.menu_gasto_pf, contextMenu);
            }
        });
        cargaCursorDiario();

        String [] fromPerDia = new String[]{manager.ID_GASTO_DIARIO,manager.MONTO,manager.TIPO,manager.DESCRIPCION,manager.FRECUENCIA};
        int [] toPerDia = new int[]{R.id.itemGastoPerDiaID,R.id.itemGastoPerDiaMonto,R.id.itemGastoPerDiaTipo,R.id.itemGastoPerDiaDescripcion,R.id.itemGastoPerDiaFrecuencia};
        String dia = this.getDayOfWeek();
        Cursor cursorPerDiaReal = manager.cargaCursorGastoPerDia2(dia);
        Cursor cursorPerDia = manager.cargaCursorGastoPerDia2(dia);
        /*if (cursorPerDia.moveToFirst()){
            while(cursorPerDia.isAfterLast()){
                String freq = cursorPerDia.getString(cursorPerDia.getColumnIndex(manager.FRECUENCIA));
                String flag;
                switch (freq){
                    case "Semanal":
                        break;
                    case "Quicenal":
                        flag = cursorPerDia.getString(cursorPerDia.getColumnIndex(manager.BANDERA));
                        if(flag.equals("1")){
                            manager.cambiaEstadoIngPerDia(cursorPerDia.getString(cursorPerDia.getColumnIndex(manager.ID_GASTO_DIA)),"0");
                        }
                        else if(flag.equals("0")){
                            manager.cambiaEstadoIngPerDia(cursorPerDia.getString(cursorPerDia.getColumnIndex(manager.ID_GASTO_DIA)),"1");
                        }
                        break;
                    case "Mensual":
                        flag = cursorPerDia.getString(cursorPerDia.getColumnIndex(manager.BANDERA));
                        if(flag.equals("3")){
                            manager.cambiaEstadoGastoPerDia(cursorPerDia.getString(cursorPerDia.getColumnIndex(manager.ID_GASTO_DIA)),"2");
                        }else if(flag.equals("2")){
                            manager.cambiaEstadoGastoPerDia(cursorPerDia.getString(cursorPerDia.getColumnIndex(manager.ID_GASTO_DIA)),"1");
                        }else if(flag.equals("1")){
                            manager.cambiaEstadoGastoPerDia(cursorPerDia.getString(cursorPerDia.getColumnIndex(manager.ID_GASTO_DIA)),"0");
                        }
                        else if(flag.equals("0")){
                            manager.cambiaEstadoGastoPerDia(cursorPerDia.getString(cursorPerDia.getColumnIndex(manager.ID_GASTO_DIA)),"3");
                        }
                        break;
                }
            }
        }*/
        adapterPerDia = new SimpleCursorAdapter(rootView.getContext(),R.layout.item_gasto_per_dia,cursorPerDiaReal,fromPerDia,toPerDia,0);
        if(adapterPerDia != null)
            listaPerDia.setAdapter(adapterPerDia);

        String [] fromPerFecha = new String[]{manager.ID_GASTO_DIARIO,manager.MONTO,manager.TIPO,manager.DESCRIPCION, manager.FRECUENCIA};
        int [] toPerFecha = new int[]{R.id.itemGastoPerFechaID,R.id.itemGastoPerFechaMonto,R.id.itemGastoPerFechaTipo,R.id.itemGastoPerFechaDescripcion,R.id.itemGastoPerFechaFrecuencia};
        ArrayList<String> arrIdPerFecha = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int fecha = calendar.get(Calendar.DAY_OF_MONTH);
        Cursor cursorFechas = manager.cargaCursorFechasGasto(String.valueOf(fecha));
        if (cursorFechas.moveToFirst()){
            while(!cursorFechas.isAfterLast()){
                String data = cursorFechas.getString(cursorFechas.getColumnIndex(manager.FK_GASTO_DIARIO));
                arrIdPerFecha.add(data);
                cursorFechas.moveToNext();
            }
        }
        cursorFechas.close();
        String [] ids2 = new String[arrIdPerFecha.size()];
        ids2 = arrIdPerFecha.toArray(ids2);
        Cursor cursorPerFecha = manager.cargaCursorGastoPerFecha(ids2);
        adapterPerFecha = new SimpleCursorAdapter(rootView.getContext(),R.layout.item_gasto_per_fecha,cursorPerFecha,fromPerFecha,toPerFecha,0);
        if(adapterPerFecha != null)
            listaPerFecha.setAdapter(adapterPerFecha);

        return rootView;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void cargaCursorDiario(){
        String [] fromDiario = new String[]{manager.ID_GASTO_DIARIO,manager.MONTO,manager.TIPO, manager.DESCRIPCION};
        int [] toDiario = new int[]{R.id.itemGastoDiarioID,R.id.itemGastoDiarioMonto,R.id.itemGastoDiarioTipo,R.id.itemGastoDiarioDescripcion};
        ArrayList<String> arrIdDiario = new ArrayList<>();
        Cursor cursorDias = manager.cargaCursorDiasGasto(getDayOfWeek());
        if (cursorDias.moveToFirst()){
            while(!cursorDias.isAfterLast()){
                String data = cursorDias.getString(cursorDias.getColumnIndex(manager.FK_GASTO_DIARIO));
                arrIdDiario.add(data);
                cursorDias.moveToNext();
            }
        }
        cursorDias.close();
        String [] ids = new String[arrIdDiario.size()];
        ids = arrIdDiario.toArray(ids);
        Calendar c = Calendar.getInstance();
        String dia = String.valueOf(c.get(Calendar.DATE));
        Cursor cursorDiario = manager.cargaCursorGastoDiario(ids,dia);
        adapterDiario = new SimpleCursorAdapter(rootView.getContext(),R.layout.item_gasto_diario,cursorDiario,fromDiario,toDiario,0);
        if(adapterDiario != null)
            listaDiario.setAdapter(adapterDiario);
    }
    public String getDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day){
            case Calendar.SUNDAY:
                return "Domingo";
            case Calendar.MONDAY:
                return "Lunes";
            case Calendar.TUESDAY:
                return "Martes";
            case Calendar.WEDNESDAY:
                return "Miercoles";
            case Calendar.THURSDAY:
                return "Jueves";
            case Calendar.FRIDAY:
                return "Viernes";
            case Calendar.SATURDAY:
                return "Sabado";
        }
        return "";
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        double monto;
        String desc;
        String tipo;
        Date d;
        CharSequence fecha;
        SimpleDateFormat format;
        GastoUnico gu;
        Calendar c;
        String dia;
        int pos = info.position;
        Cursor o;
        switch(item.getItemId()) {
            case R.id.aplicaGD:
                o = (Cursor) listaDiario.getAdapter().getItem(pos);
                Log.i("itemito", "HOLA!");
                monto = o.getDouble(o.getColumnIndex(manager.MONTO));
                desc = o.getString(o.getColumnIndex(manager.DESCRIPCION));
                tipo = o.getString(o.getColumnIndex(manager.TIPO));
                d = new Date();
                fecha  = DateFormat.format("dd-MM-yyyy", d.getTime());
                format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    d = format.parse(fecha.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                gu = new GastoUnico(monto,desc,tipo,d);
                manager.insertar(gu);
                c = Calendar.getInstance();
                dia = String.valueOf(c.get(Calendar.DATE));
                manager.cambiaEstadoGastoDiario(o.getString(o.getColumnIndex(manager.ID_GASTO_DIARIO)),dia);
                Mensaje(rootView.getContext(),"Gasto aplicado");
                cargaCursorDiario();
                break;
            case R.id.modificaGD:
                o = (Cursor) listaDiario.getAdapter().getItem(pos);
                break;
            case R.id.eliminaGD:
                o = (Cursor) listaDiario.getAdapter().getItem(pos);
                manager.eliminaGastoDiario(o.getString(o.getColumnIndex(manager.ID_GASTO_DIARIO)));
                break;
            case R.id.aplicaGPD:
                o = (Cursor) listaPerDia.getAdapter().getItem(pos);
                Log.i("itemito","HOLA!");
                monto = o.getDouble(o.getColumnIndex(manager.MONTO));
                desc = o.getString(o.getColumnIndex(manager.DESCRIPCION));
                tipo = o.getString(o.getColumnIndex(manager.TIPO));
                d = new Date();
                fecha  = DateFormat.format("dd-MM-yyyy", d.getTime());
                format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    d = format.parse(fecha.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                gu = new GastoUnico(monto,desc,tipo,d);
                manager.insertar(gu);
                c = Calendar.getInstance();
                dia = String.valueOf(c.get(Calendar.DATE));
                if(o.getString(o.getColumnIndex(manager.FRECUENCIA))=="Mensual")
                    manager.cambiaEstadoGastoPerDia(o.getString(o.getColumnIndex(manager.ID_GASTO_DIA)),"3");
                else if(o.getString(o.getColumnIndex(manager.FRECUENCIA))=="Quincenal")
                    manager.cambiaEstadoGastoPerDia(o.getString(o.getColumnIndex(manager.ID_ING_DIA)),"1");
                Mensaje(rootView.getContext(),"Gasto aplicado");
                cargaCursorDiario();
                break;
            case R.id.modificaGPD:
                o = (Cursor) listaPerDia.getAdapter().getItem(pos);
                break;
            case R.id.eliminaGPD:
                o = (Cursor) listaPerDia.getAdapter().getItem(pos);
                manager.eliminaGastoPerDia(o.getString(o.getColumnIndex(manager.ID_GASTO_DIA)));
                break;
            case R.id.aplicaGPF:
                o = (Cursor) listaPerFecha.getAdapter().getItem(pos);
                Log.i("itemito","HOLA!");
                monto = o.getDouble(o.getColumnIndex(manager.MONTO));
                desc = o.getString(o.getColumnIndex(manager.DESCRIPCION));
                tipo = o.getString(o.getColumnIndex(manager.TIPO));
                d = new Date();
                fecha  = DateFormat.format("dd-MM-yyyy", d.getTime());
                format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    d = format.parse(fecha.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                gu = new GastoUnico(monto,desc,tipo,d);
                manager.insertar(gu);
                c = Calendar.getInstance();
                dia = String.valueOf(c.get(Calendar.DATE));
                Mensaje(rootView.getContext(),"Gasto aplicado");
                cargaCursorDiario();
                break;
            case R.id.modificaGPF:
                o = (Cursor) listaPerFecha.getAdapter().getItem(pos);
                break;
            case R.id.eliminaGPF:
                o = (Cursor) listaPerFecha.getAdapter().getItem(pos);
                manager.eliminaGastoPerFecha(o.getString(o.getColumnIndex(manager.ID_GASTO_FECHA)));
                break;
        }
        return super.onContextItemSelected(item);
    }
}
