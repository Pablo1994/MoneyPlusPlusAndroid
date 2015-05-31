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

import com.apps.pablo.model.IngresoDiario;
import com.apps.pablo.model.IngresoPeriodicoDia;
import com.apps.pablo.model.IngresoPeriodicoFecha;
import com.apps.pablo.model.IngresoUnico;
import com.apps.pablo.view.SlidingTabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pablo Arias on 31/05/15.
 */
public class FragmentIngreso extends BaseFragment {

    private String[] tabs = {"Ingreso Único", "Ingreso Diario", "Ingreso Periódico"};
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
        return inflater.inflate(R.layout.fragment_ingreso, container, false);
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
        mSlidingTabLayout.setTabsNumber(3);
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
            return 3;
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
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            switch (position) {
                case 0:
                    view = getActivity().getLayoutInflater().inflate(R.layout.tab_ingreso_unico,
                            container, false);
                    // Add the newly created View to the ViewPager
                    container.addView(view);
                    final EditText editTextMonto = (EditText) view.findViewById(R.id.editTextMonto);
                    final EditText editTextDesc = (EditText) view.findViewById(R.id.editTextDescripcion);
                    final EditText editTextFecha = (EditText) view.findViewById(R.id.editTextFecha);
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
                    Button regIngreso = (Button) view.findViewById(R.id.buttonIngresoUnico);
                    regIngreso.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            double monto = Double.parseDouble(editTextMonto.getText().toString());
                            String desc = editTextDesc.getText().toString();
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                            Date fecha = null;
                            try {
                                fecha = format.parse(editTextFecha.getText().toString());
                                System.out.println("Date ->" + fecha);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            IngresoUnico ingreso = new IngresoUnico(monto, desc, fecha);
                            if (IngresaIngreso.manager.insertar(ingreso)) {
                                Mensaje(view.getContext(), "Insertado correctamente");
                                editTextMonto.setText("");
                                editTextDesc.setText("");
                                editTextFecha.setText("");
                            } else
                                Mensaje(view.getContext(), "No se pudo insertar, revise los valores e intente de nuevo.");
                        }
                    });
                    break;
                case 1:
                    view = getActivity().getLayoutInflater().inflate(R.layout.tab_ingreso_diario,
                            container, false);
                    // Add the newly created View to the ViewPager
                    container.addView(view);
                    final EditText editTextMontoDiario = (EditText) view.findViewById(R.id.editTextMontoDiario);
                    final EditText editTextDescripcionDiario = (EditText) view.findViewById(R.id.editTextDescDiario);
                    CheckBox checkBoxLunes = (CheckBox) view.findViewById(R.id.checkBoxLunes);
                    CheckBox checkBoxMartes = (CheckBox) view.findViewById(R.id.checkBoxMartes);
                    CheckBox checkBoxMiercoles = (CheckBox) view.findViewById(R.id.checkBoxMiercoles);
                    CheckBox checkBoxJueves = (CheckBox) view.findViewById(R.id.checkBoxJueves);
                    CheckBox checkBoxViernes = (CheckBox) view.findViewById(R.id.checkBoxViernes);
                    CheckBox checkBoxSabado = (CheckBox) view.findViewById(R.id.checkBoxSabado);
                    CheckBox checkBoxDomingo = (CheckBox) view.findViewById(R.id.checkBoxDomingo);
                    final CheckBox[] boxes = {checkBoxLunes, checkBoxMartes, checkBoxMiercoles, checkBoxJueves, checkBoxViernes, checkBoxSabado, checkBoxDomingo};
                    regIngreso = (Button) view.findViewById(R.id.buttonIngresoDiario);
                    regIngreso.setOnClickListener(new View.OnClickListener() {
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

                                IngresoDiario ingreso = new IngresoDiario(monto, desc, dias);
                                if (IngresaIngreso.manager.insertar(ingreso)) {
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
                case 2:
                    view = getActivity().getLayoutInflater().inflate(R.layout.tab_ingreso_periodico,
                            container, false);
                    // Add the newly created View to the ViewPager
                    container.addView(view);

                    final EditText editTextMontoDia = (EditText) view.findViewById(R.id.editTextMontoPerDia);
                    final EditText editTextDescripcionDia = (EditText) view.findViewById(R.id.editTextDescPerDia);
                    final RadioGroup radioGroupDia = (RadioGroup) view.findViewById(R.id.radio_group_dia);
                    final Spinner spinnerDia = (Spinner) view.findViewById(R.id.spinnerDias);
                    // Create an ArrayAdapter using the string array and a default spinner layout
                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(view.getContext(),
                            R.array.array_dias, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinnerDia.setAdapter(adapter2);

                    final EditText editTextMontoFecha = (EditText) view.findViewById(R.id.editTextMontoPerFecha);
                    final EditText editTextDescripcionFecha = (EditText) view.findViewById(R.id.editTextDescPerFecha);


                    final EditText editTextFechaP = (EditText) view.findViewById(R.id.editTextFecPerFecha);

                    final EditText editTextFechaP2 = (EditText) view.findViewById(R.id.editTextFecha2PerFecha);

                    final RadioGroup radioGroupFecha = (RadioGroup) view.findViewById(R.id.radio_group_fecha);
                    final View finalView = view;
                    radioGroupFecha.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            // checkedId is the RadioButton selected
                            TextView textView;
                            EditText editText;
                            switch (checkedId) {
                                case R.id.radioButtonMensFecha:
                                    textView = (TextView) finalView.findViewById(R.id.textViewFecha2PerFecha);
                                    editText = (EditText) finalView.findViewById(R.id.editTextFecha2PerFecha);
                                    textView.setVisibility(View.GONE);
                                    editText.setVisibility(View.GONE);
                                    break;
                                case R.id.radioButtonQuinFecha:
                                    textView = (TextView) finalView.findViewById(R.id.textViewFecha2PerFecha);
                                    editText = (EditText) finalView.findViewById(R.id.editTextFecha2PerFecha);
                                    textView.setVisibility(View.VISIBLE);
                                    editText.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                    });

                    //Spiner Modo:
                    Spinner spinnerModo = (Spinner) view.findViewById(R.id.spinnerModo);

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
                                                                      case R.id.spinnerModo:
                                                                          String modo = (String) adapterView.getItemAtPosition(i);
                                                                          View layout = null;
                                                                          View layout2 = null;
                                                                          switch (modo) {
                                                                              case "Por día":
                                                                                  layout = finalView1.findViewById(R.id.scrollViewDia);
                                                                                  layout2 = finalView1.findViewById(R.id.scrollViewFecha);
                                                                                  break;
                                                                              case "Por fecha(s)":
                                                                                  layout = finalView1.findViewById(R.id.scrollViewFecha);
                                                                                  layout2 = finalView1.findViewById(R.id.scrollViewDia);
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
                                                          }
                    );
                    Button regIngresoP = (Button) view.findViewById(R.id.buttonIngresoPeriodico);
                    final View finalView2 = view;
                    regIngresoP.setOnClickListener(new View.OnClickListener()

                                                   {
                                                       @Override
                                                       public void onClick(View v) {
                                                           if (finalView2.findViewById(R.id.scrollViewDia).getVisibility() == View.VISIBLE) {
                                                               double monto = Double.parseDouble(editTextMontoDia.getText().toString());
                                                               String desc = editTextDescripcionDia.getText().toString();
                                                               String freq = ((RadioButton) finalView2.findViewById(radioGroupDia.getCheckedRadioButtonId())).getText().toString();
                                                               String dia = spinnerDia.getSelectedItem().toString();
                                                               IngresoPeriodicoDia ingreso = new IngresoPeriodicoDia(monto, desc, dia, freq);
                                                               if (IngresaIngreso.manager.insertar(ingreso)) {
                                                                   Mensaje(finalView2.getContext(), "Insertado correctamente");
                                                                   editTextMontoDia.setText("");
                                                                   editTextDescripcionDia.setText("");
                                                               } else
                                                                   Mensaje(finalView2.getContext(), "No se pudo insertar, revise los valores e intente de nuevo.");
                                                           } else if (finalView2.findViewById(R.id.scrollViewFecha).getVisibility() == View.VISIBLE) {
                                                               double monto = Double.parseDouble(editTextMontoFecha.getText().toString());
                                                               String desc = editTextDescripcionFecha.getText().toString();
                                                               String freq = ((RadioButton) finalView2.findViewById(radioGroupFecha.getCheckedRadioButtonId())).getText().toString();
                                                               String[] fechas = new String[2];
                                                               String fecha1 = editTextFechaP.getText().toString();
                                                               fechas[0] = fecha1;
                                                               if (editTextFechaP2.getVisibility() == View.VISIBLE) {
                                                                   String fecha2 = editTextFechaP2.getText().toString();
                                                                   fechas[1] = fecha2;
                                                               }
                                                               IngresoPeriodicoFecha ingreso = new IngresoPeriodicoFecha(monto, desc, fechas, freq);
                                                               if (IngresaIngreso.manager.insertar(ingreso)) {
                                                                   Mensaje(finalView2.getContext(), "Insertado correctamente");
                                                                   editTextMontoFecha.setText("");
                                                                   editTextDescripcionFecha.setText("");
                                                                   editTextFechaP.setText("");
                                                                   editTextFechaP2.setText("");
                                                               } else
                                                                   Mensaje(finalView2.getContext(), "No se pudo insertar, revise los valores e intente de nuevo.");
                                                           }
                                                       }
                                                   }

                    );
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
