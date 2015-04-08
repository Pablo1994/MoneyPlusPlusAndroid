package com.apps.pablo.moneyplusplusandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

import model.IngresoDiario;

/**
 * Created by pablo on 05/04/15.
 */
public class tabIngresoDiario extends BaseFragment {
    EditText editTextMonto, editTextDescripcion;
    CheckBox checkBoxLunes, checkBoxMartes, checkBoxMiercoles, checkBoxJueves, checkBoxViernes, checkBoxSabado, checkBoxDomingo;
    Button regIngreso;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_ingreso_diario, container, false);
        editTextMonto = (EditText) rootView.findViewById(R.id.editTextMontoDiario);
        editTextDescripcion = (EditText) rootView.findViewById(R.id.editTextDescDiario);
        checkBoxLunes = (CheckBox) rootView.findViewById(R.id.checkBoxLunes);
        checkBoxMartes = (CheckBox) rootView.findViewById(R.id.checkBoxMartes);
        checkBoxMiercoles = (CheckBox) rootView.findViewById(R.id.checkBoxMiercoles);
        checkBoxJueves = (CheckBox) rootView.findViewById(R.id.checkBoxJueves);
        checkBoxViernes = (CheckBox) rootView.findViewById(R.id.checkBoxViernes);
        checkBoxSabado = (CheckBox) rootView.findViewById(R.id.checkBoxSabado);
        checkBoxDomingo = (CheckBox) rootView.findViewById(R.id.checkBoxDomingo);
        final CheckBox [] boxes = {checkBoxLunes,checkBoxMartes,checkBoxMiercoles,checkBoxJueves,checkBoxViernes,checkBoxSabado,checkBoxDomingo};
        regIngreso = (Button) rootView.findViewById(R.id.buttonIngresoDiario);
        regIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> dias = new ArrayList<>();
                for (CheckBox cb : boxes) {
                    if (cb.isChecked())
                        dias.add(cb.getText().toString());
                }
                if(!editTextMonto.getText().toString().equals("") && !editTextDescripcion.getText().toString().equals("") && !(dias.isEmpty())) {
                    double monto = Double.parseDouble(editTextMonto.getText().toString());
                    String desc = editTextDescripcion.getText().toString();

                    IngresoDiario ingreso = new IngresoDiario(monto, desc, dias);
                    if (IngresaIngreso.manager.insertar(ingreso)) {
                        Mensaje(rootView.getContext(), "Insertado correctamente");
                        editTextMonto.setText("");
                        editTextDescripcion.setText("");
                        for (CheckBox cb : boxes) {
                            cb.setChecked(false);
                        }
                    } else
                        Mensaje(rootView.getContext(), "No se pudo insertar, revise los valores e intente de nuevo.");
                } else {
                    Mensaje(rootView.getContext(),"Por favor rellene todos los campos y seleccione al menos un día de la semana.");
                }
            }
        });
        return rootView;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((IngresaIngreso) activity).onSectionAttached(2);
    }
}
