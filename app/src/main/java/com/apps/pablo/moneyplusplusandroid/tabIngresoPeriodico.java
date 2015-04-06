package com.apps.pablo.moneyplusplusandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by pablo on 05/04/15.
 */
public class tabIngresoPeriodico extends android.support.v4.app.Fragment implements AdapterView.OnItemSelectedListener{
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_ingreso_periodico, container, false);

        //Spiner Modo:
        Spinner spinnerModo = (Spinner) rootView.findViewById(R.id.spinnerModo);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.array_modos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerModo.setAdapter(adapter);
        spinnerModo.setOnItemSelectedListener(this);

        //Spiner Días:
        Spinner spinnerDias = (Spinner) rootView.findViewById(R.id.spinnerDias);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.array_dias, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerDias.setAdapter(adapter2);
        spinnerDias.setOnItemSelectedListener(this);

        return rootView;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((IngresaIngreso) activity).onSectionAttached(2);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId()) {
            case R.id.spinnerModo:
                String modo = (String) parent.getItemAtPosition(position);
                View layout = null;
                View layout2 = null;
                switch (modo) {
                    case "Por día":
                        layout = rootView.findViewById(R.id.scrollViewDia);
                        layout2 = rootView.findViewById(R.id.scrollViewFecha);
                        break;
                    case "Por fecha(s)":
                        layout = rootView.findViewById(R.id.scrollViewFecha);
                        layout2 = rootView.findViewById(R.id.scrollViewDia);
                        break;
                }
                if (layout2 != null) {
                    layout2.setVisibility(View.GONE);
                }
                if (layout != null) {
                    layout.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


