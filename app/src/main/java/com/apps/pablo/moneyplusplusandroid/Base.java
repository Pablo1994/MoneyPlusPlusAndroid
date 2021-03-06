package com.apps.pablo.moneyplusplusandroid;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by pablo on 05/04/15.
 */
public class Base extends ActionBarActivity {
    public static DBManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new DBManager(this.getApplicationContext());
    }
    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    public String getDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day){
            case Calendar.SUNDAY:
                return "Domingo";
            case Calendar.MONDAY:
                return "Lunes";
            case Calendar.TUESDAY:
                return "Martes";
            case Calendar.WEDNESDAY:
                return "Miercoles";
            case Calendar.THURSDAY:
                return "Jueves";
            case Calendar.FRIDAY:
                return "Viernes";
            case Calendar.SATURDAY:
                return "Sabado";
        }
        return "";
    }
    public String getMes(String mes){
        String mess = "";
        switch(mes){
            case "01":
                mess = "Enero";
                break;
            case "02":
                mess = "Febrero";
                break;
            case "03":
                mess = "Marzo";
                break;
            case "04":
                mess = "Abril";
                break;
            case "05":
                mess = "Mayo";
                break;
            case "06":
                mess = "Junio";
                break;
            case "07":
                mess = "Julio";
                break;
            case "08":
                mess = "Agosto";
                break;
            case "09":
                mess = "Septiembre";
                break;
            case "10":
                mess = "Octubre";
                break;
            case "11":
                mess = "Noviembre";
                break;
            case "12":
                mess = "Diciembre";
                break;
        }
        return mess;
    }
}
