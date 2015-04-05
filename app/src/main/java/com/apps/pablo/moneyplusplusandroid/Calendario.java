package com.apps.pablo.moneyplusplusandroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by pablo on 05/04/15.
 */
public class Calendario extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calendario, container, false);
        return rootView;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((PantallaPrincipal) activity).onSectionAttached(2);
    }
}
