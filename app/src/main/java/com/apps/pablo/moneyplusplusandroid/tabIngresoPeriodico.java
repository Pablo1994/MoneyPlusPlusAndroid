package com.apps.pablo.moneyplusplusandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.apps.pablo.model.IngresoPeriodicoDia;
import com.apps.pablo.model.IngresoPeriodicoFecha;

/**
 * Created by pablo on 05/04/15.
 */
public class tabIngresoPeriodico extends BaseFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    View rootView;
    EditText editTextMontoDia, editTextDescripcionDia, editTextMontoFecha, editTextDescripcionFecha;
    RadioGroup radioGroupDia;
    Spinner spinnerDia;
    RadioGroup radioGroupFecha;
    EditText editTextFecha;
    EditText editTextFecha2;
    Button regIngreso;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_ingreso_periodico, container, false);
        regIngreso = (Button) rootView.findViewById(R.id.buttonIngresoPeriodico);
        regIngreso.setOnClickListener(this);
        editTextMontoDia = (EditText) rootView.findViewById(R.id.editTextMontoPerDia);
        editTextDescripcionDia = (EditText) rootView.findViewById(R.id.editTextDescPerDia);
        radioGroupDia = (RadioGroup) rootView.findViewById(R.id.radio_group_dia);
        spinnerDia = (Spinner) rootView.findViewById(R.id.spinnerDias);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.array_dias, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerDia.setAdapter(adapter2);
        spinnerDia.setOnItemSelectedListener(this);

        editTextMontoFecha = (EditText) rootView.findViewById(R.id.editTextMontoPerFecha);
        editTextDescripcionFecha = (EditText) rootView.findViewById(R.id.editTextDescPerFecha);


        editTextFecha = (EditText) rootView.findViewById(R.id.editTextFecPerFecha);

        editTextFecha2 = (EditText) rootView.findViewById(R.id.editTextFecha2PerFecha);

        radioGroupFecha = (RadioGroup) rootView.findViewById(R.id.radio_group_fecha);
        radioGroupFecha.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                TextView textView;
                EditText editText;
                switch (checkedId) {
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

    @Override
    public void onClick(View v) {
        if(v == regIngreso){
            if(rootView.findViewById(R.id.scrollViewDia).getVisibility() == View.VISIBLE){
                double monto = Double.parseDouble(editTextMontoDia.getText().toString());
                String desc = editTextDescripcionDia.getText().toString();
                String freq = ((RadioButton) rootView.findViewById(radioGroupDia.getCheckedRadioButtonId())).getText().toString();
                String dia = spinnerDia.getSelectedItem().toString();
                IngresoPeriodicoDia ingreso = new IngresoPeriodicoDia(monto,desc,dia,freq);
                if(IngresaIngreso.manager.insertar(ingreso)) {
                    Mensaje(rootView.getContext(), "Insertado correctamente");
                    editTextMontoDia.setText("");
                    editTextDescripcionDia.setText("");
                }
                else
                    Mensaje(rootView.getContext(),"No se pudo insertar, revise los valores e intente de nuevo.");
            } else if(rootView.findViewById(R.id.scrollViewFecha).getVisibility() == View.VISIBLE){
                double monto = Double.parseDouble(editTextMontoFecha.getText().toString());
                String desc = editTextDescripcionFecha.getText().toString();
                String freq = ((RadioButton) rootView.findViewById(radioGroupFecha.getCheckedRadioButtonId())).getText().toString();
                String [] fechas = new String[2];
                String fecha1 = editTextFecha.getText().toString();
                fechas[0] = fecha1;
                if(editTextFecha2.getVisibility() == View.VISIBLE){
                    String fecha2 = editTextFecha2.getText().toString();
                    fechas[1] = fecha2;
                }
                IngresoPeriodicoFecha ingreso = new IngresoPeriodicoFecha(monto,desc,fechas,freq);
                if(IngresaIngreso.manager.insertar(ingreso)) {
                    Mensaje(rootView.getContext(), "Insertado correctamente");
                    editTextMontoFecha.setText("");
                    editTextDescripcionFecha.setText("");
                    editTextFecha.setText("");
                    editTextFecha2.setText("");
                }
                else
                    Mensaje(rootView.getContext(),"No se pudo insertar, revise los valores e intente de nuevo.");
            }
        }
    }
}


