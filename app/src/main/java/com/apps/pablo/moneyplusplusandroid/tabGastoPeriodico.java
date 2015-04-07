package com.apps.pablo.moneyplusplusandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by pablo on 05/04/15.
 */
public class tabGastoPeriodico extends android.support.v4.app.Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    View rootView;
    EditText editTextFecha;
    EditText editTextFecha2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_gasto_periodico, container, false);

        editTextFecha = (EditText) rootView.findViewById(R.id.editTextFecPerFecha);

        editTextFecha2 = (EditText) rootView.findViewById(R.id.editTextFecha2PerFecha);

        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radio_group_fecha);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                TextView textView;
                EditText editText;
                switch (checkedId){
                    case R.id.radioButtonMensFecha:
                        textView = (TextView) rootView.findViewById(R.id.textViewFecha2PerFecha);
                        editText = (EditText) rootView.findViewById(R.id.editTextFecha2PerFecha);
                        textView.setVisibility(View.GONE);
                        editText.setVisibility(View.GONE);
                        break;
                    case R.id.radioButtonQuinFecha:
                        textView = (TextView) rootView.findViewById(R.id.textViewFecha2PerFecha);
                        editText = (EditText) rootView.findViewById(R.id.editTextFecha2PerFecha);
                        textView.setVisibility(View.VISIBLE);
                        editText.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

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
        ((IngresaGasto) activity).onSectionAttached(2);
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

    @Override
    public void onClick(View v) {

    }
}


