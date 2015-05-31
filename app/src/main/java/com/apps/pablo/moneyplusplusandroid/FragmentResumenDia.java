package com.apps.pablo.moneyplusplusandroid;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.apps.pablo.model.GastoUnico;
import com.apps.pablo.model.IngresoUnico;
import com.apps.pablo.view.SlidingTabLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pablo Arias on 31/05/15.
 */
public class FragmentResumenDia extends BaseFragment {
    private String[] tabs = {"Ingresos", "Gastos", "Ahorros"};
    private SlidingTabLayout mSlidingTabLayout;
    DBManager manager = ResumenDia.manager;
    Cursor cursorDiario;
    ListView listaDiario, listaPerDia, listaPerFecha, listaGastoDiario, listaGastoPerDia, listaGastoPerFecha;
    SimpleCursorAdapter adapterDiario, adapterPerDia, adapterPerFecha, adapterGastoDiario, adapterGastoPerDia, adapterGastoPerFecha;

    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;

    /**
     * Inflates the {@link View} which will be displayed by this {@link Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resumen_dia, container, false);

    }
    // BEGIN_INCLUDE (fragment_onviewcreated)

    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the content view.
     * <p/>
     * We set the {@link ViewPager}'s adapter to be an instance of {@link SamplePagerAdapter}. The
     * {@link SlidingTabLayout} is then given the {@link ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setTabsNumber(3);
        mSlidingTabLayout.setViewPager(mViewPager);
        // END_INCLUDE (setup_slidingtablayout)
    }

    // END_INCLUDE (fragment_onviewcreated)
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
        IngresoUnico iu;
        Calendar c;
        String dia;
        int pos = info.position;
        Cursor o;
        switch (item.getItemId()) {
            case R.id.aplicaID:
                o = (Cursor) listaDiario.getAdapter().getItem(pos);
                Log.i("itemito", "HOLA!");
                monto = o.getDouble(o.getColumnIndex(manager.MONTO));
                desc = o.getString(o.getColumnIndex(manager.DESCRIPCION));
                d = new Date();
                fecha = DateFormat.format("dd-MM-yyyy", d.getTime());
                format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    d = format.parse(fecha.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                iu = new IngresoUnico(monto, desc, d);
                manager.insertar(iu);
                c = Calendar.getInstance();
                dia = String.valueOf(c.get(Calendar.DATE));
                manager.cambiaEstadoIngDiario(o.getString(o.getColumnIndex(manager.ID_ING_DIARIO)), dia);
                Mensaje(getActivity().getApplicationContext(), "Ingreso aplicado");
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
                Log.i("itemito", "HOLA!");
                monto = o.getDouble(o.getColumnIndex(manager.MONTO));
                desc = o.getString(o.getColumnIndex(manager.DESCRIPCION));
                d = new Date();
                fecha = DateFormat.format("dd-MM-yyyy", d.getTime());
                format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    d = format.parse(fecha.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                iu = new IngresoUnico(monto, desc, d);
                manager.insertar(iu);
                c = Calendar.getInstance();
                dia = String.valueOf(c.get(Calendar.DATE));
                if (o.getString(o.getColumnIndex(manager.FRECUENCIA)) == "Mensual")
                    manager.cambiaEstadoIngPerDia(o.getString(o.getColumnIndex(manager.ID_ING_DIA)), "3");
                else if (o.getString(o.getColumnIndex(manager.FRECUENCIA)) == "Quincenal")
                    manager.cambiaEstadoIngPerDia(o.getString(o.getColumnIndex(manager.ID_ING_DIA)), "1");
                Mensaje(getActivity().getApplicationContext(), "Ingreso aplicado");
                cargaCursorDiario();
                break;
            case R.id.modificaIPD:
                o = (Cursor) listaPerDia.getAdapter().getItem(pos);
                break;
            case R.id.eliminaIPD:
                o = (Cursor) listaPerDia.getAdapter().getItem(pos);
                manager.eliminaIngPerDia(o.getString(o.getColumnIndex(manager.ID_ING_DIA)));
                cargaCursorDiario();
                break;
            case R.id.aplicaIPF:
                o = (Cursor) listaPerFecha.getAdapter().getItem(pos);
                Log.i("itemito", "HOLA!");
                monto = o.getDouble(o.getColumnIndex(manager.MONTO));
                desc = o.getString(o.getColumnIndex(manager.DESCRIPCION));
                d = new Date();
                fecha = DateFormat.format("dd-MM-yyyy", d.getTime());
                format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    d = format.parse(fecha.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                iu = new IngresoUnico(monto, desc, d);
                manager.insertar(iu);
                c = Calendar.getInstance();
                dia = String.valueOf(c.get(Calendar.DATE));
                Mensaje(getActivity().getApplicationContext(), "Ingreso aplicado");
                cargaCursorFechas();
                break;
            case R.id.modificaIPF:
                o = (Cursor) listaPerFecha.getAdapter().getItem(pos);
                break;
            case R.id.eliminaIPF:
                o = (Cursor) listaPerFecha.getAdapter().getItem(pos);
                boolean r = manager.eliminaIngPerFecha(o.getString(o.getColumnIndex(manager.ID_ING_FECHA)));
                cargaCursorFechas();
                break;
            case R.id.aplicaGD:
                o = (Cursor) listaGastoDiario.getAdapter().getItem(pos);
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
                Mensaje(getActivity().getApplicationContext(),"Gasto aplicado");
                cargaCursorGastoDiario();
                break;
            case R.id.modificaGD:
                o = (Cursor) listaGastoDiario.getAdapter().getItem(pos);
                break;
            case R.id.eliminaGD:
                o = (Cursor) listaGastoDiario.getAdapter().getItem(pos);
                manager.eliminaGastoDiario(o.getString(o.getColumnIndex(manager.ID_GASTO_DIARIO)));
                break;
            case R.id.aplicaGPD:
                o = (Cursor) listaGastoPerDia.getAdapter().getItem(pos);
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
                Mensaje(getActivity().getApplicationContext(),"Gasto aplicado");
                cargaCursorGastoDiario();
                break;
            case R.id.modificaGPD:
                o = (Cursor) listaGastoPerDia.getAdapter().getItem(pos);
                break;
            case R.id.eliminaGPD:
                o = (Cursor) listaGastoPerDia.getAdapter().getItem(pos);
                manager.eliminaGastoPerDia(o.getString(o.getColumnIndex(manager.ID_GASTO_DIA)));
                break;
            case R.id.aplicaGPF:
                o = (Cursor) listaGastoPerFecha.getAdapter().getItem(pos);
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
                Mensaje(getActivity().getApplicationContext(),"Gasto aplicado");
                cargaCursorGastoFechas();
                break;
            case R.id.modificaGPF:
                o = (Cursor) listaGastoPerFecha.getAdapter().getItem(pos);
                break;
            case R.id.eliminaGPF:
                o = (Cursor) listaGastoPerFecha.getAdapter().getItem(pos);
                manager.eliminaGastoPerFecha(o.getString(o.getColumnIndex(manager.ID_GASTO_FECHA)));
                cargaCursorGastoFechas();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void cargaCursorDiario() {
        String[] fromDiario = new String[]{manager.ID_ING_DIARIO, manager.MONTO, manager.DESCRIPCION};
        int[] toDiario = new int[]{R.id.itemDiarioID, R.id.itemDiarioMonto, R.id.itemDiarioDescripcion};
        ArrayList<String> arrIdDiario = new ArrayList<>();
        Cursor cursorDias = manager.cargaCursorDias(getDayOfWeek());
        if (cursorDias.moveToFirst()) {
            while (!cursorDias.isAfterLast()) {
                String data = cursorDias.getString(cursorDias.getColumnIndex(manager.FK_ING_DIARIO));
                arrIdDiario.add(data);
                cursorDias.moveToNext();
            }
        }
        cursorDias.close();
        String[] idsDiario = new String[arrIdDiario.size()];
        idsDiario = arrIdDiario.toArray(idsDiario);
        Calendar c = Calendar.getInstance();
        String dia = String.valueOf(c.get(Calendar.DATE));
        cursorDiario = manager.cargaCursorIngDiario(idsDiario, dia);
        adapterDiario = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.item_ingreso_diario, cursorDiario, fromDiario, toDiario, 0);
        if (adapterDiario != null)
            listaDiario.setAdapter(adapterDiario);

    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void cargaCursorGastoDiario(){
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
        Cursor cursorDiario = manager.cargaCursorGastoDiario(ids, dia);
        adapterGastoDiario = new SimpleCursorAdapter(getActivity().getApplicationContext(),R.layout.item_gasto_diario,cursorDiario,fromDiario,toDiario,0);
        if(adapterGastoDiario != null)
            listaGastoDiario.setAdapter(adapterGastoDiario);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void cargaCursorFechas(){
        String[] fromPerFecha = new String[]{manager.ID_ING_DIARIO, manager.MONTO, manager.DESCRIPCION, manager.FRECUENCIA};
        int[] toPerFecha = new int[]{R.id.itemPerFechaID, R.id.itemPerFechaMonto, R.id.itemPerFechaDescripcion};
        ArrayList<String> arrIdPerFecha = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Cursor cursorFechas = null;
        int fecha = calendar.get(Calendar.DATE);
        if (fecha < 31 && fecha == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            Log.i("pablito", "entró aquí en " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            switch (fecha) {
                case 30:
                    cursorFechas = manager.cargaCursorFechas(new String[]{String.valueOf(fecha), String.valueOf(fecha + 1)});
                    break;
                case 29:
                    cursorFechas = manager.cargaCursorFechas(new String[]{String.valueOf(fecha), String.valueOf(fecha + 1), String.valueOf(fecha + 2)});
                    break;
                case 28:
                    cursorFechas = manager.cargaCursorFechas(new String[]{String.valueOf(fecha), String.valueOf(fecha + 1), String.valueOf(fecha + 2), String.valueOf(fecha + 3)});
                    break;
            }
        } else {
            cursorFechas = manager.cargaCursorFechas(String.valueOf(fecha));
        }
        if (cursorFechas.moveToFirst()) {
            while (!cursorFechas.isAfterLast()) {
                String data = cursorFechas.getString(cursorFechas.getColumnIndex(manager.FK_ING_FECHAS));
                arrIdPerFecha.add(data);
                cursorFechas.moveToNext();
            }
        }
        cursorFechas.close();
        String[] ids2 = new String[arrIdPerFecha.size()];
        ids2 = arrIdPerFecha.toArray(ids2);
        Cursor cursorPerFecha = manager.cargaCursorIngPerFecha(ids2);
        adapterPerFecha = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.item_ingreso_per_fecha, cursorPerFecha, fromPerFecha, toPerFecha, 0);
        if (adapterPerFecha != null)
            listaPerFecha.setAdapter(adapterPerFecha);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void cargaCursorGastoFechas(){
        String [] fromGastoPerDia = new String[]{manager.ID_GASTO_DIARIO,manager.MONTO,manager.TIPO,manager.DESCRIPCION,manager.FRECUENCIA};
        int [] toGastoPerDia = new int[]{R.id.itemGastoPerDiaID,R.id.itemGastoPerDiaMonto,R.id.itemGastoPerDiaTipo,R.id.itemGastoPerDiaDescripcion,R.id.itemGastoPerDiaFrecuencia};
        String dia = getDayOfWeek();
        Cursor cursorGastoPerDiaReal = manager.cargaCursorGastoPerDia2(dia);
        Cursor cursorGastoPerDia = manager.cargaCursorGastoPerDia2(dia);
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
        adapterGastoPerDia = new SimpleCursorAdapter(getActivity().getApplicationContext(),R.layout.item_gasto_per_dia,cursorGastoPerDiaReal,fromGastoPerDia,toGastoPerDia,0);
        if(adapterGastoPerDia != null)
            listaGastoPerDia.setAdapter(adapterGastoPerDia);

        String [] fromGastoPerFecha = new String[]{manager.ID_GASTO_DIARIO,manager.MONTO,manager.TIPO,manager.DESCRIPCION, manager.FRECUENCIA};
        int [] toGastoPerFecha = new int[]{R.id.itemGastoPerFechaID,R.id.itemGastoPerFechaMonto,R.id.itemGastoPerFechaTipo,R.id.itemGastoPerFechaDescripcion,R.id.itemGastoPerFechaFrecuencia};
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
        Cursor cursorGastoPerFecha = manager.cargaCursorGastoPerFecha(ids2);
        adapterGastoPerFecha = new SimpleCursorAdapter(getActivity().getApplicationContext(),R.layout.item_gasto_per_fecha,cursorGastoPerFecha,fromGastoPerFecha,toGastoPerFecha,0);
        if(adapterGastoPerFecha != null)
            listaGastoPerFecha.setAdapter(adapterGastoPerFecha);
    }
    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     * {@link SlidingTabLayout}.
     */
    class SamplePagerAdapter extends PagerAdapter implements OnCreateContextMenuListener {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 3;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)

        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p/>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
        // END_INCLUDE (pageradapter_getpagetitle)




        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new layout from our resources
            View view = null;
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (position) {
                case 0:
                    view = getActivity().getLayoutInflater().inflate(R.layout.tab_ingresos_programados,
                            container, false);
                    // Add the newly created View to the ViewPager
                    container.addView(view);
                    listaDiario = (ListView) view.findViewById(R.id.listViewDiario);
                    listaPerDia = (ListView) view.findViewById(R.id.listViewPerDia);
                    listaPerFecha = (ListView) view.findViewById(R.id.listViewPerFecha);
                    listaDiario.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu menu, View v,
                                                        ContextMenu.ContextMenuInfo menuInfo) {
                            getActivity().getMenuInflater().inflate(R.menu.menu_ingreso_d, menu);
                        }
                    });
                    listaPerDia.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                            getActivity().getMenuInflater().inflate(R.menu.menu_ingreso_pd, contextMenu);
                        }
                    });
                    listaPerFecha.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                            getActivity().getMenuInflater().inflate(R.menu.menu_ingreso_pf, contextMenu);
                        }
                    });
                    cargaCursorDiario();

                    String[] fromPerDia = new String[]{manager.ID_ING_DIARIO, manager.MONTO, manager.DESCRIPCION, manager.FRECUENCIA};
                    int[] toPerDia = new int[]{R.id.itemPerDiaID, R.id.itemPerDiaMonto, R.id.itemPerDiaDescripcion, R.id.itemPerDiaFrecuencia};
                    Cursor cursorPerDiaReal = manager.cargaCursorIngPerDia2(getDayOfWeek());
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
                    adapterPerDia = new SimpleCursorAdapter(view.getContext(), R.layout.item_gasto_per_dia, cursorPerDiaReal, fromPerDia, toPerDia, 0);
                    if (adapterPerDia != null)
                        listaPerDia.setAdapter(adapterPerDia);

                    cargaCursorFechas();

                    break;
                case 1:
                    view = getActivity().getLayoutInflater().inflate(R.layout.tab_gastos_programados,
                            container, false);
                    // Add the newly created View to the ViewPager
                    container.addView(view);
                    listaGastoDiario = (ListView) view.findViewById(R.id.listViewGastoDiario);
                    listaGastoPerDia = (ListView) view.findViewById(R.id.listViewGastoPerDia);
                    listaGastoPerFecha = (ListView) view.findViewById(R.id.listViewGastoPerFecha);
                    listaGastoDiario.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu menu, View v,
                                                        ContextMenu.ContextMenuInfo menuInfo) {
                            getActivity().getMenuInflater().inflate(R.menu.menu_gasto_d, menu);
                        }
                    });
                    listaGastoPerDia.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                            getActivity().getMenuInflater().inflate(R.menu.menu_gasto_pd, contextMenu);
                        }
                    });
                    listaGastoPerFecha.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                            getActivity().getMenuInflater().inflate(R.menu.menu_gasto_pf, contextMenu);
                        }
                    });
                    cargaCursorGastoDiario();

                    cargaCursorGastoFechas();
                    break;
                case 2:
                    view = getActivity().getLayoutInflater().inflate(R.layout.tab_ahorros_programados,
                            container, false);
                    // Add the newly created View to the ViewPager
                    container.addView(view);
                    break;
            }


            Log.i("hola", "instantiateItem() [position: " + position + "]");

            // Return the View
            return view;
        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            Log.i("hola", "destroyItem() [position: " + position + "]");
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        }
    }

}
