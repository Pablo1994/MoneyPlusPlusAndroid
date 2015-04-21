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

import model.IngresoUnico;

/**
 * Created by Pablo Arias on 08/04/15.
 */
public class TabIngresosProgramados extends BaseFragment implements View.OnCreateContextMenuListener{
    DBManager manager;
    Cursor cursorDiario;
    View rootView;
    ListView listaDiario, listaPerDia, listaPerFecha;
    SimpleCursorAdapter adapterDiario, adapterPerDia, adapterPerFecha;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_ingresos_programados, container, false);

        manager = ResumenDia.manager;
        listaDiario = (ListView) rootView.findViewById(R.id.listViewDiario);
        listaPerDia = (ListView) rootView.findViewById(R.id.listViewPerDia);
        listaPerFecha = (ListView) rootView.findViewById(R.id.listViewPerFecha);
        listaDiario.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                getActivity().getMenuInflater().inflate(R.menu.menu_ingreso_d, menu);
            }
        });
        listaPerDia.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                getActivity().getMenuInflater().inflate(R.menu.menu_ingreso_pd, contextMenu);
            }
        });
        listaPerFecha.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                getActivity().getMenuInflater().inflate(R.menu.menu_ingreso_pf, contextMenu);
            }
        });
        cargaCursorDiario();

        String [] fromPerDia = new String[]{manager.ID_ING_DIARIO,manager.MONTO,manager.DESCRIPCION,manager.FRECUENCIA};
        int [] toPerDia = new int[]{R.id.itemPerDiaID,R.id.itemPerDiaMonto,R.id.itemPerDiaDescripcion,R.id.itemPerDiaFrecuencia};
        Cursor cursorPerDiaReal = manager.cargaCursorIngPerDia2(this.getDayOfWeek());
        //Cursor cursorPerDia = manager.cargaCursorIngPerDia(getDayOfWeek());
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
                            manager.cambiaEstadoIngPerDia(cursorPerDia.getString(cursorPerDia.getColumnIndex(manager.ID_ING_DIA)),"0");
                        }
                        break;
                    case "Mensual":
                        flag = cursorPerDia.getString(cursorPerDia.getColumnIndex(manager.BANDERA));
                        if(flag.equals("3")){
                            manager.cambiaEstadoIngPerDia(cursorPerDia.getString(cursorPerDia.getColumnIndex(manager.ID_ING_DIA)),"2");
                        }else if(flag.equals("2")){
                            manager.cambiaEstadoIngPerDia(cursorPerDia.getString(cursorPerDia.getColumnIndex(manager.ID_ING_DIA)),"1");
                        }else if(flag.equals("1")){
                            manager.cambiaEstadoIngPerDia(cursorPerDia.getString(cursorPerDia.getColumnIndex(manager.ID_ING_DIA)),"0");
                        }
                        break;
                }
            }
        }*/
        adapterPerDia = new SimpleCursorAdapter(rootView.getContext(),R.layout.item_gasto_per_dia,cursorPerDiaReal,fromPerDia,toPerDia,0);
        if(adapterPerDia != null)
            listaPerDia.setAdapter(adapterPerDia);

        String [] fromPerFecha = new String[]{manager.ID_ING_DIARIO,manager.MONTO,manager.DESCRIPCION, manager.FRECUENCIA};
        int [] toPerFecha = new int[]{R.id.itemPerFechaID,R.id.itemPerFechaMonto,R.id.itemPerFechaDescripcion};
        ArrayList<String> arrIdPerFecha = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Cursor cursorFechas = null;
        int fecha = calendar.get(Calendar.DATE);
        if(fecha == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            switch (fecha - calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                case 1:
                    cursorFechas = manager.cargaCursorFechas(new String []{String.valueOf(fecha), String.valueOf(fecha + 1)});
                    break;
                case 2:
                    cursorFechas = manager.cargaCursorFechas(new String []{String.valueOf(fecha), String.valueOf(fecha + 1), String.valueOf(fecha + 2)});
                    break;
                case 3:
                    cursorFechas = manager.cargaCursorFechas(new String []{String.valueOf(fecha), String.valueOf(fecha + 1), String.valueOf(fecha + 2), String.valueOf(fecha + 3)});
                    break;
            }
        } else {
            cursorFechas = manager.cargaCursorFechas(String.valueOf(fecha));
        }
        if (cursorFechas.moveToFirst()){
            while(!cursorFechas.isAfterLast()){
                String data = cursorFechas.getString(cursorFechas.getColumnIndex(manager.FK_ING_FECHAS));
                arrIdPerFecha.add(data);
                cursorFechas.moveToNext();
            }
        }
        cursorFechas.close();
        String [] ids2 = new String[arrIdPerFecha.size()];
        ids2 = arrIdPerFecha.toArray(ids2);
        Cursor cursorPerFecha = manager.cargaCursorIngPerFecha(ids2);
        adapterPerFecha = new SimpleCursorAdapter(rootView.getContext(),R.layout.item_ingreso_per_fecha,cursorPerFecha,fromPerFecha,toPerFecha,0);
        if(adapterPerFecha != null)
            listaPerFecha.setAdapter(adapterPerFecha);

        return rootView;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void cargaCursorDiario(){
        String [] fromDiario = new String[]{manager.ID_ING_DIARIO,manager.MONTO,manager.DESCRIPCION};
        int [] toDiario = new int[]{R.id.itemDiarioID,R.id.itemDiarioMonto,R.id.itemDiarioDescripcion};
        ArrayList<String> arrIdDiario = new ArrayList<>();
        Cursor cursorDias = manager.cargaCursorDias(getDayOfWeek());
        if (cursorDias.moveToFirst()){
            while(!cursorDias.isAfterLast()){
                String data = cursorDias.getString(cursorDias.getColumnIndex(manager.FK_ING_DIARIO));
                arrIdDiario.add(data);
                cursorDias.moveToNext();
            }
        }
        cursorDias.close();
        String [] idsDiario = new String[arrIdDiario.size()];
        idsDiario = arrIdDiario.toArray(idsDiario);
        Calendar c = Calendar.getInstance();
        String dia = String.valueOf(c.get(Calendar.DATE));
        cursorDiario = manager.cargaCursorIngDiario(idsDiario,dia);
        adapterDiario = new SimpleCursorAdapter(rootView.getContext(),R.layout.item_ingreso_diario,cursorDiario,fromDiario,toDiario,0);
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
        Date d;
        CharSequence fecha;
        SimpleDateFormat format;
        IngresoUnico iu;
        Calendar c;
        String dia;
        int pos = info.position;
        Cursor o;
        switch(item.getItemId()) {
            case R.id.aplicaID:
                o = (Cursor) listaDiario.getAdapter().getItem(pos);
                Log.i("itemito","HOLA!");
                monto = o.getDouble(o.getColumnIndex(manager.MONTO));
                desc = o.getString(o.getColumnIndex(manager.DESCRIPCION));
                d = new Date();
                fecha  = DateFormat.format("dd-MM-yyyy", d.getTime());
                format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    d = format.parse(fecha.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                iu = new IngresoUnico(monto,desc,d);
                manager.insertar(iu);
                c = Calendar.getInstance();
                dia = String.valueOf(c.get(Calendar.DATE));
                manager.cambiaEstadoIngDiario(o.getString(o.getColumnIndex(manager.ID_ING_DIARIO)),dia);
                Mensaje(rootView.getContext(),"Ingreso aplicado");
                cargaCursorDiario();
                break;
            case R.id.modificaID:
                o = (Cursor) listaDiario.getAdapter().getItem(pos);
                break;
            case R.id.eliminaID:
                o = (Cursor) listaDiario.getAdapter().getItem(pos);
                manager.eliminaIngDiario(o.getString(o.getColumnIndex(manager.ID_ING_DIARIO)));
                break;
            case R.id.aplicaIPD:
                o = (Cursor) listaPerDia.getAdapter().getItem(pos);
                Log.i("itemito","HOLA!");
                monto = o.getDouble(o.getColumnIndex(manager.MONTO));
                desc = o.getString(o.getColumnIndex(manager.DESCRIPCION));
                d = new Date();
                fecha  = DateFormat.format("dd-MM-yyyy", d.getTime());
                format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    d = format.parse(fecha.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                iu = new IngresoUnico(monto,desc,d);
                manager.insertar(iu);
                c = Calendar.getInstance();
                dia = String.valueOf(c.get(Calendar.DATE));
                if(o.getString(o.getColumnIndex(manager.FRECUENCIA))=="Mensual")
                    manager.cambiaEstadoIngPerDia(o.getString(o.getColumnIndex(manager.ID_ING_DIA)),"3");
                else if(o.getString(o.getColumnIndex(manager.FRECUENCIA))=="Quincenal")
                    manager.cambiaEstadoIngPerDia(o.getString(o.getColumnIndex(manager.ID_ING_DIA)),"1");
                Mensaje(rootView.getContext(),"Ingreso aplicado");
                cargaCursorDiario();
                break;
            case R.id.modificaIPD:
                o = (Cursor) listaPerDia.getAdapter().getItem(pos);
                break;
            case R.id.eliminaIPD:
                o = (Cursor) listaPerDia.getAdapter().getItem(pos);
                manager.eliminaIngPerDia(o.getString(o.getColumnIndex(manager.ID_ING_DIA)));
                break;
            case R.id.aplicaIPF:
                o = (Cursor) listaPerFecha.getAdapter().getItem(pos);
                Log.i("itemito","HOLA!");
                monto = o.getDouble(o.getColumnIndex(manager.MONTO));
                desc = o.getString(o.getColumnIndex(manager.DESCRIPCION));
                d = new Date();
                fecha  = DateFormat.format("dd-MM-yyyy", d.getTime());
                format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    d = format.parse(fecha.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                iu = new IngresoUnico(monto,desc,d);
                manager.insertar(iu);
                c = Calendar.getInstance();
                dia = String.valueOf(c.get(Calendar.DATE));
                Mensaje(rootView.getContext(),"Ingreso aplicado");
                cargaCursorDiario();
                break;
            case R.id.modificaIPF:
                o = (Cursor) listaPerFecha.getAdapter().getItem(pos);
                break;
            case R.id.eliminaIPF:
                o = (Cursor) listaPerFecha.getAdapter().getItem(pos);
                manager.eliminaIngPerFecha(o.getString(o.getColumnIndex(manager.ID_ING_FECHA)));
                break;
        }
        return super.onContextItemSelected(item);
    }
}
