package com.apps.pablo.moneyplusplusandroid;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.apps.pablo.model.GastoDiario;
import com.apps.pablo.model.GastoPeriodicoDia;
import com.apps.pablo.model.GastoPeriodicoFecha;
import com.apps.pablo.model.GastoTransporte;
import com.apps.pablo.model.GastoUnico;
import com.apps.pablo.view.SlidingTabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pablo Arias on 31/05/15.
 */
public class FragmentGasto extends BaseFragment {

    private String[] tabs = {"Gasto Único","Transporte", "Gasto Diario", "Gasto Periódico"};
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;

    /**
     * Inflates the {@link View} which will be displayed by this {@link Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gasto, container, false);
    }
    // BEGIN_INCLUDE (fragment_onviewcreated)

    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the content view.
     * <p/>
     * We set the {@link ViewPager}'s adapter to be an instance of {@link SamplePagerAdapter}. The
     * {@link SlidingTabLayout} is then given the {@link ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setTabsNumber(1);
        mSlidingTabLayout.setViewPager(mViewPager);
        // END_INCLUDE (setup_slidingtablayout)
    }
    // END_INCLUDE (fragment_onviewcreated)

    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     * {@link SlidingTabLayout}.
     */
    class SamplePagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 4;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)

        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p/>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
        // END_INCLUDE (pageradapter_getpagetitle)

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = null;
            switch (position) {
                case 0:
                    view = getActivity().getLayoutInflater().inflate(R.layout.tab_gasto_unico,
                            container, false);
                    // Add the newly created View to the ViewPager
                    container.addView(view);
                    final EditText editTextMonto = (EditText) view.findViewById(R.id.editTextMontoGastoUnico);
                    final EditText editTextDesc = (EditText) view.findViewById(R.id.editTextDescripcionGastoUnico);
                    final EditText editTextFecha = (EditText) view.findViewById(R.id.editTextFechaGastoUnico);
                    editTextFecha.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Process to get Current Date
                            final Calendar c = Calendar.getInstance();
                            int mYear = c.get(Calendar.YEAR);
                            int mMonth = c.get(Calendar.MONTH);
                            int mDay = c.get(Calendar.DAY_OF_MONTH);

                            // Launch Date Picker Dialog
                            DatePickerDialog dpd = new DatePickerDialog(view.getContext(),
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            // Display Selected date in textbox
                                            editTextFecha.setText(dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year);

                                        }
                                    }, mYear, mMonth, mDay);
                            dpd.show();
                        }
                    });
                    final Spinner spinnerTipo = (Spinner) view.findViewById(R.id.spinnerGastoUnico);
                    // Create an ArrayAdapter using the string array and a default spinner layout
                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.array_tipos, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinnerTipo.setAdapter(adapter2);
                    Button regIngreso = (Button) view.findViewById(R.id.buttonGastoUnico);
                    regIngreso.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            double monto = Double.parseDouble(editTextMonto.getText().toString());
                            String desc = editTextDesc.getText().toString();
                            String tipo = spinnerTipo.getSelectedItem().toString();
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                            Date fecha = null;
                            try {
                                fecha = format.parse(editTextFecha.getText().toString());
                                System.out.println("Date ->" + fecha);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            GastoUnico gasto = new GastoUnico(monto, desc, tipo, fecha);
                            if (IngresaGasto.manager.insertar(gasto)) {
                                Mensaje(view.getContext(), "Insertado correctamente");
                                editTextMonto.setText("");
                                editTextDesc.setText("");
                                editTextFecha.setText("");
                            } else
                                Mensaje(view.getContext(), "No se pudo insertar, revise los valores e intente de nuevo.");
                        }
                    });
                    break;
                case 2:
                    view = getActivity().getLayoutInflater().inflate(R.layout.tab_gasto_diario,
                            container, false);
                    // Add the newly created View to the ViewPager

                    container.addView(view);
                    final EditText editTextMontoDiario = (EditText) view.findViewById(R.id.editTextGastoDiario);
                    final EditText editTextDescripcionDiario = (EditText) view.findViewById(R.id.editTextDescGastoDiario);
                    final Spinner spinnerTipoDiario = (Spinner) view.findViewById(R.id.spinnerGastoDiario);
                    // Create an ArrayAdapter using the string array and a default spinner layout
                    ArrayAdapter<CharSequence> adapter2Diario = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.array_tipos, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    adapter2Diario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinnerTipoDiario.setAdapter(adapter2Diario);
                    CheckBox checkBoxLunes = (CheckBox) view.findViewById(R.id.checkBoxLunesGasto);
                    CheckBox checkBoxMartes = (CheckBox) view.findViewById(R.id.checkBoxMartesGasto);
                    CheckBox checkBoxMiercoles = (CheckBox) view.findViewById(R.id.checkBoxMiercolesGasto);
                    CheckBox checkBoxJueves = (CheckBox) view.findViewById(R.id.checkBoxJuevesGasto);
                    CheckBox checkBoxViernes = (CheckBox) view.findViewById(R.id.checkBoxViernesGasto);
                    CheckBox checkBoxSabado = (CheckBox) view.findViewById(R.id.checkBoxSabadoGasto);
                    CheckBox checkBoxDomingo = (CheckBox) view.findViewById(R.id.checkBoxDomingoGasto);
                    final CheckBox[] boxes = {checkBoxLunes, checkBoxMartes, checkBoxMiercoles, checkBoxJueves, checkBoxViernes, checkBoxSabado, checkBoxDomingo};
                    Button regGasto = (Button) view.findViewById(R.id.buttonGastoDiario);
                    regGasto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArrayList<String> dias = new ArrayList<>();
                            for (CheckBox cb : boxes) {
                                if (cb.isChecked())
                                    dias.add(cb.getText().toString());
                            }
                            if (!editTextMontoDiario.getText().toString().equals("") && !editTextDescripcionDiario.getText().toString().equals("") && !(dias.isEmpty())) {
                                double monto = Double.parseDouble(editTextMontoDiario.getText().toString());
                                String desc = editTextDescripcionDiario.getText().toString();
                                String tipo = spinnerTipoDiario.getSelectedItem().toString();

                                GastoDiario gasto = new GastoDiario(monto, desc, tipo, dias);
                                if (IngresaGasto.manager.insertar(gasto)) {
                                    Mensaje(v.getContext(), "Insertado correctamente");
                                    editTextMontoDiario.setText("");
                                    editTextDescripcionDiario.setText("");
                                    for (CheckBox cb : boxes) {
                                        cb.setChecked(false);
                                    }
                                } else
                                    Mensaje(v.getContext(), "No se pudo insertar, revise los valores e intente de nuevo.");
                            } else {
                                Mensaje(v.getContext(), "Por favor rellene todos los campos y seleccione al menos un día de la semana.");
                            }
                        }
                    });
                    break;
                case 3:
                    view = getActivity().getLayoutInflater().inflate(R.layout.tab_gasto_periodico,
                            container, false);
                    // Add the newly created View to the ViewPager
                    container.addView(view);

                    final EditText editTextMontoDia = (EditText) view.findViewById(R.id.editTextGastoMontoPerDia);
                    final EditText editTextDescripcionDia = (EditText) view.findViewById(R.id.editTextGastoDescPerDia);
                    final Spinner spinnerTipoDia = (Spinner) view.findViewById(R.id.spinnerTipoDias);
                    // Create an ArrayAdapter using the string array and a default spinner layout
                    ArrayAdapter<CharSequence> adapter2P = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.array_tipos, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    adapter2P.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinnerTipoDia.setAdapter(adapter2P);
                    final RadioGroup radioGroupDia = (RadioGroup) view.findViewById(R.id.radio_group_gasto_dia);
                    final Spinner spinnerDia = (Spinner) view.findViewById(R.id.spinnerGastoDias);
                    // Create an ArrayAdapter using the string array and a default spinner layout
                    ArrayAdapter<CharSequence> adaptertd = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.array_dias, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    adaptertd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinnerDia.setAdapter(adaptertd);

                    final EditText editTextMontoFecha = (EditText) view.findViewById(R.id.editTextGastoPerFecha);
                    final EditText editTextDescripcionFecha = (EditText) view.findViewById(R.id.editTextDescGastoPerFecha);
                    final Spinner spinnerTipoFecha = (Spinner) view.findViewById(R.id.spinnerTipoFechas);
                    // Create an ArrayAdapter using the string array and a default spinner layout
                    ArrayAdapter<CharSequence> adaptertf = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.array_tipos, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    adaptertf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinnerTipoFecha.setAdapter(adaptertf);

                    final EditText editTextFechaP = (EditText) view.findViewById(R.id.editTextGastoFecPerFecha);

                    final EditText editTextFecha2 = (EditText) view.findViewById(R.id.editTextGastoFecha2PerFecha);

                    final RadioGroup radioGroupFecha = (RadioGroup) view.findViewById(R.id.radio_group_gasto_fecha);
                    final View finalView = view;
                    radioGroupFecha.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            // checkedId is the RadioButton selected
                            TextView textView;
                            EditText editText;
                            switch (checkedId) {
                                case R.id.radioButtonGastoMensFecha:
                                    textView = (TextView) finalView.findViewById(R.id.textViewGastoFecha2PerFecha);
                                    editText = (EditText) finalView.findViewById(R.id.editTextGastoFecha2PerFecha);
                                    textView.setVisibility(View.GONE);
                                    editText.setVisibility(View.GONE);
                                    break;
                                case R.id.radioButtonGastoQuinFecha:
                                    Log.i("pablengue","hola");
                                    textView = (TextView) finalView.findViewById(R.id.textViewGastoFecha2PerFecha);
                                    editText = (EditText) finalView.findViewById(R.id.editTextGastoFecha2PerFecha);
                                    textView.setVisibility(View.VISIBLE);
                                    editText.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                    });

                    //Spiner Modo:
                    Spinner spinnerModo = (Spinner) view.findViewById(R.id.spinnerModoGasto);

                    // Create an ArrayAdapter using the string array and a default spinner layout
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.array_modos, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinnerModo.setAdapter(adapter);
                    final View finalView1 = view;
                    spinnerModo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
                            Spinner spinner = (Spinner) adapterView;
                            switch (spinner.getId()) {
                                case R.id.spinnerModoGasto:
                                    String modo = (String) adapterView.getItemAtPosition(i);
                                    View layout = null;
                                    View layout2 = null;
                                    switch (modo) {
                                        case "Por día":
                                            layout = finalView1.findViewById(R.id.scrollViewDiaGasto);
                                            layout2 = finalView1.findViewById(R.id.scrollViewFechaGasto);
                                            break;
                                        case "Por fecha(s)":
                                            layout = finalView1.findViewById(R.id.scrollViewFechaGasto);
                                            layout2 = finalView1.findViewById(R.id.scrollViewDiaGasto);
                                            break;
                                    }
                                    if (layout2 != null) {
                                        layout2.setVisibility(View.GONE);
                                    }
                                    if (layout != null) {
                                        layout.setVisibility(View.VISIBLE);
                                    }
                                    break;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    regGasto = (Button) view.findViewById(R.id.buttonGastoPeriodico);
                    regGasto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (view.findViewById(R.id.scrollViewDiaGasto).getVisibility() == View.VISIBLE) {
                                double monto = Double.parseDouble(editTextMontoDia.getText().toString());
                                String desc = editTextDescripcionDia.getText().toString();
                                String tipo = spinnerTipoDia.getSelectedItem().toString();
                                String freq = ((RadioButton) view.findViewById(radioGroupDia.getCheckedRadioButtonId())).getText().toString();
                                String dia = spinnerDia.getSelectedItem().toString();
                                GastoPeriodicoDia gasto = new GastoPeriodicoDia(monto, desc, tipo, dia, freq);
                                if (IngresaGasto.manager.insertar(gasto)) {
                                    Mensaje(view.getContext(), "Insertado correctamente");
                                    editTextMontoDia.setText("");
                                    editTextDescripcionDia.setText("");
                                } else
                                    Mensaje(view.getContext(), "No se pudo insertar, revise los valores e intente de nuevo.");
                            } else if (view.findViewById(R.id.scrollViewFechaGasto).getVisibility() == View.VISIBLE) {
                                double monto = Double.parseDouble(editTextMontoFecha.getText().toString());
                                String desc = editTextDescripcionFecha.getText().toString();
                                String tipo = spinnerTipoFecha.getSelectedItem().toString();
                                String freq = ((RadioButton) view.findViewById(radioGroupFecha.getCheckedRadioButtonId())).getText().toString();
                                String[] fechas = new String[2];
                                String fecha1 = editTextFechaP.getText().toString();
                                fechas[0] = fecha1;
                                if (editTextFecha2.getVisibility() == View.VISIBLE) {
                                    String fecha2 = editTextFecha2.getText().toString();
                                    fechas[1] = fecha2;
                                }
                                GastoPeriodicoFecha gasto = new GastoPeriodicoFecha(monto, desc, tipo, fechas, freq);
                                if (IngresaGasto.manager.insertar(gasto)) {
                                    Mensaje(view.getContext(), "Insertado correctamente");
                                    editTextMontoFecha.setText("");
                                    editTextDescripcionFecha.setText("");
                                    editTextFechaP.setText("");
                                    editTextFecha2.setText("");
                                } else
                                    Mensaje(view.getContext(), "No se pudo insertar, revise los valores e intente de nuevo.");
                            }
                        }
                    });
                    break;
                case 1:
                    view = getActivity().getLayoutInflater().inflate(R.layout.tab_gasto_transporte,
                            container, false);
                    // Add the newly created View to the ViewPager
                    container.addView(view);
                    final EditText editTextMontoTransporte = (EditText) view.findViewById(R.id.editTextMontoGastoTransporte);
                    final EditText editTextDescTransporte = (EditText) view.findViewById(R.id.editTextDescripcionGastoTransporte);
                    final RadioGroup radioGroupTransporte = (RadioGroup) view.findViewById(R.id.radioGroupGastoTransporte);

                    Button regGastoTransporte = (Button) view.findViewById(R.id.buttonGastoTransporte);
                    final View finalView2 = view;
                    regGastoTransporte.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            double monto = Double.parseDouble(editTextMontoTransporte.getText().toString());
                            String desc = editTextDescTransporte.getText().toString();
                            String medio = ((RadioButton) finalView2.findViewById(radioGroupTransporte.getCheckedRadioButtonId())).getText().toString();

                            GastoTransporte gasto = new GastoTransporte(monto, desc, "Transporte", medio);
                            if (IngresaGasto.manager.insertar(gasto)) {
                                Mensaje(finalView2.getContext(), "Insertado correctamente");
                                editTextMontoTransporte.setText("");
                                editTextDescTransporte.setText("");
                            } else
                                Mensaje(finalView2.getContext(), "No se pudo insertar, revise los valores e intente de nuevo.");
                        }
                    });
                    break;
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            Log.i("hola", "destroyItem() [position: " + position + "]");
        }
    }
}
