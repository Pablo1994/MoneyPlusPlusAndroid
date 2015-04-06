package com.apps.pablo.moneyplusplusandroid;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;

/**
 * Created by pablo on 05/04/15.
 */
public class Base extends ActionBarActivity {
    protected Manager manager = null;
    protected static Database db = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String TAG = "MoneyPlus";
        if(db == null) {
            try {
                manager = new Manager(new AndroidContext(getApplicationContext()), Manager.DEFAULT_OPTIONS);
                db = manager.getDatabase("money_db");
                Log.i(TAG, "Database created!");
            } catch (IOException e) {
                Log.e(TAG, "Cannot create database", e);
                return;
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
