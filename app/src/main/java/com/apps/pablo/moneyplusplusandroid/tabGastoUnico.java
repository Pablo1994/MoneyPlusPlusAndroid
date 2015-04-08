package com.apps.pablo.moneyplusplusandroid;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.GastoUnico;

/**
 * Created by pablo on 05/04/15.
 */
public class tabGastoUnico extends BaseFragment implements View.OnClickListener{
    EditText editTextMonto, editTextDesc, editTextFecha;
    Spinner spinnerTipo;
    View rootView;
    Button regIngreso;
    private int mYear,mMonth,mDay;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_gasto_unico, container, false);
        editTextMonto = (EditText) rootView.findViewById(R.id.editTextMontoGastoUnico);
        editTextDesc = (EditText) rootView.findViewById(R.id.editTextDescripcionGastoUnico);
        editTextFecha = (EditText) rootView.findViewById(R.id.editTextFechaGastoUnico);
        editTextFecha.setOnClickListener(this);
        spinnerTipo = (Spinner) rootView.findViewById(R.id.spinnerGastoUnico);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.array_tipos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerTipo.setAdapter(adapter2);
        regIngreso = (Button) rootView.findViewById(R.id.buttonGastoUnico);
        regIngreso.setOnClickListener(this);
        return rootView;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((IngresaGasto) activity).onSectionAttached(1);
    }

    @Override
    public void onClick(View v) {
        if(v == editTextFecha){
            // Process to get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(v.getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // Display Selected date in textbox
                            editTextFecha.setText(dayOfMonth + "-"
                                    + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            dpd.show();
        }
        if(v == regIngreso){
            double monto = Double.parseDouble(editTextMonto.getText().toString());
            String desc = editTextDesc.getText().toString();
            String tipo = spinnerTipo.getSelectedItem().toString();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date fecha = null;
            try {
                fecha = format.parse(editTextFecha.getText().toString());
                System.out.println("Date ->" + fecha);
            } catch (Exception e) {
                e.printStackTrace();
            }
            GastoUnico gasto = new GastoUnico(monto,desc,tipo, fecha);
            if(IngresaIngreso.manager.insertar(gasto)) {
                Mensaje(rootView.getContext(), "Insertado correctamente");
                editTextMonto.setText("");
                editTextDesc.setText("");
                editTextFecha.setText("");
            }
            else
                Mensaje(rootView.getContext(),"No se pudo insertar, revise los valores e intente de nuevo.");
        }
    }
}
