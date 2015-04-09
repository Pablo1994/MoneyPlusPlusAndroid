package com.apps.pablo.moneyplusplusandroid;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Pablo Arias on 08/04/15.
 */
public class TabIngresosProgramados extends BaseFragment{
    View rootView;
    ListView listaDiario, listaPerDia, listaPerFecha;
    SimpleCursorAdapter adapterDiario, adapterPerDia, adapterPerFecha;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_ingresos_programados, container, false);

        DBManager manager = ResumenDia.manager;

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
        String [] ids = new String[arrIdDiario.size()];
        ids = arrIdDiario.toArray(ids);
        Cursor cursorDiario = manager.cargaCursorIngDiario(ids);
        adapterDiario = new SimpleCursorAdapter(rootView.getContext(),R.layout.item_ingreso_diario,cursorDiario,fromDiario,toDiario,0);
        if(adapterDiario != null)
            listaDiario.setAdapter(adapterDiario);

        /*String [] fromPerDia = new String[]{manager.ID_ING_DIARIO,manager.MONTO,manager.DESCRIPCION,manager.FRECUENCIA};
        int [] toPerDia = new int[]{R.id.itemPerDiaID,R.id.itemPerDiaMonto,R.id.itemPerDiaDescripcion,R.id.itemPerDiaFrecuencia};
        Cursor cursorPerDiaReal = manager.cargaCursorIngPerDia2(getDayOfWeek());
        Cursor cursorPerDia = manager.cargaCursorIngPerDia(getDayOfWeek());
        if (cursorPerDia.moveToFirst()){
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
                        else if(flag.equals("0")){
                            manager.cambiaEstadoIngPerDia(cursorPerDia.getString(cursorPerDia.getColumnIndex(manager.ID_ING_DIA)),"1");
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
                        else if(flag.equals("0")){
                            manager.cambiaEstadoIngPerDia(cursorPerDia.getString(cursorPerDia.getColumnIndex(manager.ID_ING_DIA)),"3");
                        }
                        break;
                }
            }
        }
        adapterPerDia = new SimpleCursorAdapter(rootView.getContext(),R.layout.item_ingreso_per_dia,cursorPerDiaReal,fromPerDia,toPerDia,0);
        listaPerDia.setAdapter(adapterPerDia);*/

        /*String [] fromPerFecha = new String[]{manager.ID_ING_DIARIO,manager.MONTO,manager.DESCRIPCION, manager.FRECUENCIA};
        int [] toPerFecha = new int[]{R.id.itemPerFechaID,R.id.itemPerFechaMonto,R.id.itemPerFechaDescripcion};
        ArrayList<String> arrIdPerFecha = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int fecha = calendar.get(Calendar.DAY_OF_MONTH);
        Cursor cursorFechas = manager.cargaCursorDias(String.valueOf(fecha));
        if (cursorFechas.moveToFirst()){
            while(!cursorFechas.isAfterLast()){
                String data = cursorFechas.getString(cursorFechas.getColumnIndex(manager.FK_ING_DIARIO));
                arrIdPerFecha.add(data);
                cursorDias.moveToNext();
            }
        }
        cursorDias.close();
        String [] ids2 = new String[arrIdPerFecha.size()];
        ids = arrIdPerFecha.toArray(ids2);
        Cursor cursorPerFecha = manager.cargaCursorIngPerFecha(ids2);
        adapterPerFecha = new SimpleCursorAdapter(rootView.getContext(),R.layout.item_ingreso_per_fecha,cursorPerFecha,fromPerFecha,toPerFecha,0);
        listaPerFecha.setAdapter(adapterPerFecha);*/

        return rootView;
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
}
