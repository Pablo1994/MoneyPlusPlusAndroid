package com.apps.pablo.moneyplusplusandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by pablo on 05/04/15.
 */
public class tabGastoDiario extends BaseFragment {
    View rootView;
    EditText editTextMonto, editTextDescripcion;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_gasto_diario, container, false);

        return rootView;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((IngresaGasto) activity).onSectionAttached(2);
    }
}
