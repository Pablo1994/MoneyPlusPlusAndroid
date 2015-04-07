package com.apps.pablo.moneyplusplusandroid;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by pablo on 07/04/15.
 */
public class BaseFragment extends Fragment{
    public void Mensaje(Context ctx, String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
}
