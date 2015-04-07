package com.apps.pablo.moneyplusplusandroid;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

/**
 * Created by pablo on 05/04/15.
 */
public class Base extends ActionBarActivity {
    protected static DBHelper helper = null;
    protected SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(helper == null) {
            helper= new DBHelper(this.getApplicationContext());
        }
        db = helper.getWritableDatabase();
    }
    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
