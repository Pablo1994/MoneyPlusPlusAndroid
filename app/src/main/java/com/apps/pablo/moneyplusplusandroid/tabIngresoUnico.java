package com.apps.pablo.moneyplusplusandroid;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.apps.pablo.model.IngresoUnico;

/**
 * Created by Pablo Arias on 05/04/15.
 */
public class tabIngresoUnico extends BaseFragment implements View.OnClickListener{
    EditText editTextMonto, editTextDesc, editTextFecha;
    Button regIngreso;
    private int mYear,mMonth,mDay;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_ingreso_unico, container, false);
        editTextMonto = (EditText) rootView.findViewById(R.id.editTextMonto);
        editTextDesc = (EditText) rootView.findViewById(R.id.editTextDescripcion);
        editTextFecha = (EditText) rootView.findViewById(R.id.editTextFecha);
        editTextFecha.setOnClickListener(this);
        regIngreso = (Button) rootView.findViewById(R.id.buttonIngresoUnico);
        regIngreso.setOnClickListener(this);
        return rootView;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((IngresaIngreso) activity).onSectionAttached(1);
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
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date fecha = null;
            try {
                fecha = format.parse(editTextFecha.getText().toString());
                System.out.println("Date ->" + fecha);
            } catch (Exception e) {
                e.printStackTrace();
            }
            IngresoUnico ingreso = new IngresoUnico(monto,desc,fecha);
            if(IngresaIngreso.manager.insertar(ingreso)) {
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
