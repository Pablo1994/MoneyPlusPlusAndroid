package com.apps.pablo.moneyplusplusandroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by pablo on 05/04/15.
 */
public class Estadisticas extends android.support.v4.app.Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.estadisticas, container, false);
        return rootView;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((PantallaPrincipal) activity).onSectionAttached(3);
    }
}
