package com.apps.pablo.moneyplusplusandroid;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class ResumenGastos extends Base {

    String mes;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_gastos);
        mes = getIntent().getStringExtra("mes");
        TextView txtViewGastos = (TextView) findViewById(R.id.labelGastos);
        txtViewGastos.setText(txtViewGastos.getText() + getMes(mes));
        ListView list = (ListView) findViewById(R.id.listViewGastos);
        String [] from = {manager.ID_GASTO_PURO,manager.MONTO,manager.DESCRIPCION,manager.TIPO,manager.FECHA};
        int [] to = {R.id.itemGastoID,R.id.itemGastoMonto,R.id.itemGastoDescripcion,R.id.itemGastoTipo,R.id.itemGastoFecha};
        Cursor cursor = manager.cargaCursorGastoPuro(mes);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),R.layout.item_gastos,cursor,from,to,0);
        if (adapter != null)
            list.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resumen_gastos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
