package com.apps.pablo.moneyplusplusandroid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/**
 * Created by pablo on 05/04/15.
 */
public class Estadisticas extends android.support.v4.app.Fragment {
    WebView webView;
    int num1, num2, num3, num4, num5;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.estadisticas, container, false);


        webView = (WebView) rootView.findViewById(R.id.webView);
        webView.addJavascriptInterface(new WebAppInterface(), "Android");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/chart.html");
        return rootView;
    }

    public class WebAppInterface {

        @JavascriptInterface
        public int getAhorros(String mes) {
            int ahoTotal = 0;
            Cursor ahoCursor = PantallaPrincipal.manager.cargaCursorAhorroPuro(mes);
            if (ahoCursor.moveToFirst()) {
                while (!ahoCursor.isAfterLast()) {
                    ahoTotal += ahoCursor.getInt(ahoCursor.getColumnIndex(PantallaPrincipal.manager.MONTO));
                    ahoCursor.moveToNext();
                }
            }
            return ahoTotal;
        }

        @JavascriptInterface
        public int getIngresos(String mes) {
            int ahoTotal = 0;
            Cursor ahoCursor = PantallaPrincipal.manager.cargaCursorIngPuro(mes);
            if (ahoCursor.moveToFirst()) {
                while (!ahoCursor.isAfterLast()) {
                    ahoTotal += ahoCursor.getInt(ahoCursor.getColumnIndex(PantallaPrincipal.manager.MONTO));
                    ahoCursor.moveToNext();
                }
            }
            return ahoTotal;
        }

        @JavascriptInterface
        public int getGastos(String mes) {
            int ahoTotal = 0;
            Cursor ahoCursor = PantallaPrincipal.manager.cargaCursorGastoPuro(mes);
            if (ahoCursor.moveToFirst()) {
                while (!ahoCursor.isAfterLast()) {
                    ahoTotal += ahoCursor.getInt(ahoCursor.getColumnIndex(PantallaPrincipal.manager.MONTO));
                    ahoCursor.moveToNext();
                }
            }
            return ahoTotal;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((PantallaPrincipal) activity).onSectionAttached(3);
    }
}
