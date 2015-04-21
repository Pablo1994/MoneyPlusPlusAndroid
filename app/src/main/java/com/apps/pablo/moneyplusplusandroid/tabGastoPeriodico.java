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

import model.GastoPeriodicoDia;
import model.GastoPeriodicoFecha;

/**
 * Created by pablo on 05/04/15.
 */
public class tabGastoPeriodico extends BaseFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    View rootView;
    EditText editTextMontoDia, editTextDescripcionDia, editTextMontoFecha, editTextDescripcionFecha;
    Spinner spinnerTipoDia, spinnerTipoFecha;
    RadioGroup radioGroupDia;
    Spinner spinnerDia;
    RadioGroup radioGroupFecha;
    EditText editTextFecha;
    EditText editTextFecha2;
    Button regGasto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_gasto_periodico, container, false);
        regGasto = (Button) rootView.findViewById(R.id.buttonGastoPeriodico);
        regGasto.setOnClickListener(this);
        editTextMontoDia = (EditText) rootView.findViewById(R.id.editTextGastoMontoPerDia);
        editTextDescripcionDia = (EditText) rootView.findViewById(R.id.editTextGastoDescPerDia);
        spinnerTipoDia = (Spinner) rootView.findViewById(R.id.spinnerTipoDias);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.array_tipos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerTipoDia.setAdapter(adapter2);
        radioGroupDia = (RadioGroup) rootView.findViewById(R.id.radio_group_gasto_dia);
        spinnerDia = (Spinner) rootView.findViewById(R.id.spinnerGastoDias);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adaptertd = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.array_dias, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adaptertd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerDia.setAdapter(adaptertd);
        spinnerDia.setOnItemSelectedListener(this);

        editTextMontoFecha = (EditText) rootView.findViewById(R.id.editTextGastoPerFecha);
        editTextDescripcionFecha = (EditText) rootView.findViewById(R.id.editTextDescGastoPerFecha);
        spinnerTipoFecha = (Spinner) rootView.findViewById(R.id.spinnerTipoFechas);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adaptertf = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.array_tipos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adaptertf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerTipoFecha.setAdapter(adaptertf);

        editTextFecha = (EditText) rootView.findViewById(R.id.editTextGastoFecPerFecha);

        editTextFecha2 = (EditText) rootView.findViewById(R.id.editTextGastoFecha2PerFecha);

        radioGroupFecha = (RadioGroup) rootView.findViewById(R.id.radio_group_gasto_fecha);
        radioGroupFecha.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                TextView textView;
                EditText editText;
                switch (checkedId){
                    case R.id.radioButtonGastoMensFecha:
                        textView = (TextView) rootView.findViewById(R.id.textViewGastoFecha2PerFecha);
                        editText = (EditText) rootView.findViewById(R.id.editTextGastoFecha2PerFecha);
                        textView.setVisibility(View.GONE);
                        editText.setVisibility(View.GONE);
                        break;
                    case R.id.radioButtonQuinFecha:
                        textView = (TextView) rootView.findViewById(R.id.textViewGastoFecha2PerFecha);
                        editText = (EditText) rootView.findViewById(R.id.editTextGastoFecha2PerFecha);
                        textView.setVisibility(View.VISIBLE);
                        editText.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        //Spiner Modo:
        Spinner spinnerModo = (Spinner) rootView.findViewById(R.id.spinnerModoGasto);

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
        ((IngresaGasto) activity).onSectionAttached(2);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId()) {
            case R.id.spinnerModoGasto:
                String modo = (String) parent.getItemAtPosition(position);
                View layout = null;
                View layout2 = null;
                switch (modo) {
                    case "Por día":
                        layout = rootView.findViewById(R.id.scrollViewDiaGasto);
                        layout2 = rootView.findViewById(R.id.scrollViewFechaGasto);
                        break;
                    case "Por fecha(s)":
                        layout = rootView.findViewById(R.id.scrollViewFechaGasto);
                        layout2 = rootView.findViewById(R.id.scrollViewDiaGasto);
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
        if(v == regGasto){
            if(rootView.findViewById(R.id.scrollViewDiaGasto).getVisibility() == View.VISIBLE){
                double monto = Double.parseDouble(editTextMontoDia.getText().toString());
                String desc = editTextDescripcionDia.getText().toString();
                String tipo = spinnerTipoDia.getSelectedItem().toString();
                String freq = ((RadioButton) rootView.findViewById(radioGroupDia.getCheckedRadioButtonId())).getText().toString();
                String dia = spinnerDia.getSelectedItem().toString();
                GastoPeriodicoDia gasto = new GastoPeriodicoDia(monto,desc,tipo,dia,freq);
                if(IngresaIngreso.manager.insertar(gasto)) {
                    Mensaje(rootView.getContext(), "Insertado correctamente");
                    editTextMontoDia.setText("");
                    editTextDescripcionDia.setText("");
                }
                else
                    Mensaje(rootView.getContext(),"No se pudo insertar, revise los valores e intente de nuevo.");
            } else if(rootView.findViewById(R.id.scrollViewFechaGasto).getVisibility() == View.VISIBLE){
                double monto = Double.parseDouble(editTextMontoFecha.getText().toString());
                String desc = editTextDescripcionFecha.getText().toString();
                String tipo = spinnerTipoFecha.getSelectedItem().toString();
                String freq = ((RadioButton) rootView.findViewById(radioGroupFecha.getCheckedRadioButtonId())).getText().toString();
                String [] fechas = new String[2];
                String fecha1 = editTextFecha.getText().toString();
                fechas[0] = fecha1;
                if(editTextFecha2.getVisibility() == View.VISIBLE){
                    String fecha2 = editTextFecha2.getText().toString();
                    fechas[1] = fecha2;
                }
                GastoPeriodicoFecha gasto = new GastoPeriodicoFecha(monto,desc,tipo, fechas,freq);
                if(IngresaIngreso.manager.insertar(gasto)) {
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


