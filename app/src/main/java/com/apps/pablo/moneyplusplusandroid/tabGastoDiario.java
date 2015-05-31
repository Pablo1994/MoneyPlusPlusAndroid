package com.apps.pablo.moneyplusplusandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import com.apps.pablo.model.GastoDiario;

/**
 * Created by pablo on 05/04/15.
 */
public class tabGastoDiario extends BaseFragment {
    EditText editTextMonto, editTextDescripcion;
    Spinner spinnerTipo;
    CheckBox checkBoxLunes, checkBoxMartes, checkBoxMiercoles, checkBoxJueves, checkBoxViernes, checkBoxSabado, checkBoxDomingo;
    Button regGasto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_gasto_diario, container, false);
        editTextMonto = (EditText) rootView.findViewById(R.id.editTextGastoDiario);
        editTextDescripcion = (EditText) rootView.findViewById(R.id.editTextDescGastoDiario);
        spinnerTipo = (Spinner) rootView.findViewById(R.id.spinnerGastoDiario);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.array_tipos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerTipo.setAdapter(adapter2);
        checkBoxLunes = (CheckBox) rootView.findViewById(R.id.checkBoxLunesGasto);
        checkBoxMartes = (CheckBox) rootView.findViewById(R.id.checkBoxMartesGasto);
        checkBoxMiercoles = (CheckBox) rootView.findViewById(R.id.checkBoxMiercolesGasto);
        checkBoxJueves = (CheckBox) rootView.findViewById(R.id.checkBoxJuevesGasto);
        checkBoxViernes = (CheckBox) rootView.findViewById(R.id.checkBoxViernesGasto);
        checkBoxSabado = (CheckBox) rootView.findViewById(R.id.checkBoxSabadoGasto);
        checkBoxDomingo = (CheckBox) rootView.findViewById(R.id.checkBoxDomingoGasto);
        final CheckBox [] boxes = {checkBoxLunes,checkBoxMartes,checkBoxMiercoles,checkBoxJueves,checkBoxViernes,checkBoxSabado,checkBoxDomingo};
        regGasto = (Button) rootView.findViewById(R.id.buttonGastoDiario);
        regGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> dias = new ArrayList<>();
                for (CheckBox cb : boxes) {
                    if (cb.isChecked())
                        dias.add(cb.getText().toString());
                }
                if (!editTextMonto.getText().toString().equals("") && !editTextDescripcion.getText().toString().equals("") && !(dias.isEmpty())) {
                    double monto = Double.parseDouble(editTextMonto.getText().toString());
                    String desc = editTextDescripcion.getText().toString();
                    String tipo = spinnerTipo.getSelectedItem().toString();

                    GastoDiario gasto = new GastoDiario(monto, desc, tipo, dias);
                    if (IngresaGasto.manager.insertar(gasto)) {
                        Mensaje(rootView.getContext(), "Insertado correctamente");
                        editTextMonto.setText("");
                        editTextDescripcion.setText("");
                        for (CheckBox cb : boxes) {
                            cb.setChecked(false);
                        }
                    } else
                        Mensaje(rootView.getContext(), "No se pudo insertar, revise los valores e intente de nuevo.");
                } else {
                    Mensaje(rootView.getContext(), "Por favor rellene todos los campos y seleccione al menos un día de la semana.");
                }
            }
        });
        return rootView;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((IngresaGasto) activity).onSectionAttached(2);
    }
}
