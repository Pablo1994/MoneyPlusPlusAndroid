package com.apps.pablo.moneyplusplusandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by pablo on 05/04/15.
 */
public class Inicio extends android.support.v4.app.Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.inicio, container, false);
        Button btnIngresa = (Button) rootView.findViewById(R.id.buttonIngreso);
        btnIngresa.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(rootView.getContext(),IngresaIngreso.class);
                startActivity(intento);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((PantallaPrincipal) activity).onSectionAttached(1);
    }
}
