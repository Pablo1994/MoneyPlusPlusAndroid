package com.apps.pablo.moneyplusplusandroid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by pablo on 05/04/15.
 */
public class Calendario extends BaseFragment implements AdapterView.OnItemSelectedListener{
    View rootView;
    DBManager manager;
    Spinner spinnerMes;
    DatePicker datePick;
    TextView ingresos,gastos,balance;
    Calendar c;
    String mesg;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.calendario, container, false);
        spinnerMes = (Spinner) rootView.findViewById(R.id.spinnerMes);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterMes = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.array_meses, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterMes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerMes.setAdapter(adapterMes);

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1980; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_item, years);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinYear = (Spinner)rootView.findViewById(R.id.spinnerYear);
        spinYear.setAdapter(adapterYear);

        ingresos = (TextView) rootView.findViewById(R.id.textViewIngresos);
        gastos = (TextView) rootView.findViewById(R.id.textViewGastos);

        balance = (TextView) rootView.findViewById(R.id.textViewBalance);
        manager = PantallaPrincipal.manager;
        c = Calendar.getInstance();
        int m = c.get(Calendar.MONTH)+1;
        spinnerMes.setSelection(m-1);
        spinnerMes.setOnItemSelectedListener(this);
        ingresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(rootView.getContext(),ResumenIngresos.class);
                i.putExtra("mes",mesg);
                startActivity(i);
            }
        });
        gastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(rootView.getContext(),ResumenGastos.class);
                i.putExtra("mes",mesg);
                startActivity(i);
            }
        });
        return rootView;
    }

    public void llenaCampos(String mes){
        int ingTotal = 0;
        Cursor ingCursor = manager.cargaCursorIngPuro(mes);
        if(ingCursor.moveToFirst()){
            while(!ingCursor.isAfterLast()){
                ingTotal += ingCursor.getInt(ingCursor.getColumnIndex(manager.MONTO));
                ingCursor.moveToNext();}
        }
        ingresos.setText(String.valueOf(ingTotal));

        int gasTotal = 0;
        Cursor gasCursor = manager.cargaCursorGastoPuro(mes);
        if(gasCursor.moveToFirst()){
            while(!gasCursor.isAfterLast()){
                gasTotal += gasCursor.getInt(gasCursor.getColumnIndex(manager.MONTO));
                gasCursor.moveToNext();}
        }
        gastos.setText(String.valueOf(gasTotal));
        int bal = Integer.parseInt(ingresos.getText().toString()) - Integer.parseInt(gastos.getText().toString());
        if(bal > 0)
            Mensaje(rootView.getContext(),"¡Felicidades, Ha ahorrado un total de " + bal + " colones en " + spinnerMes.getSelectedItem().toString() + "!");
        else if (bal < 0)
            Mensaje(rootView.getContext(), "Su balance es negativo, ¿Olvidó ingresar algún dato?");
        balance.setText(String.valueOf(bal));
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((PantallaPrincipal) activity).onSectionAttached(2);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int m = i+1;
        String mes;
        if(m < 10){
            mesg = "0"+String.valueOf(m);
            mes = "0"+String.valueOf(m);
        } else {
            mesg = String.valueOf(m);
            mes = String.valueOf(m);
        }
        llenaCampos(mes);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
