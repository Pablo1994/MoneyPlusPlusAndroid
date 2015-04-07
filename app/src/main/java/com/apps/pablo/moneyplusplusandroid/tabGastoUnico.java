package com.apps.pablo.moneyplusplusandroid;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by pablo on 05/04/15.
 */
public class tabGastoUnico extends android.support.v4.app.Fragment implements View.OnClickListener{
    EditText editTextMonto, editTextDesc, editTextFecha;
    private int mYear,mMonth,mDay;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_gasto_unico, container, false);
        editTextMonto = (EditText) rootView.findViewById(R.id.editTextMonto);
        editTextDesc = (EditText) rootView.findViewById(R.id.editTextDescripcion);
        editTextFecha = (EditText) rootView.findViewById(R.id.editTextFecha);
        editTextFecha.setOnClickListener(this);

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
            Log.i("Fechita", "LO HICIMOS, SÍ!");
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
    }
}
