package com.apps.pablo.moneyplusplusandroid;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.apps.pablo.model.AhorroProgramado;
import com.apps.pablo.model.AhorroUnico;
import com.apps.pablo.model.IngresoPeriodicoFecha;
import com.apps.pablo.view.SlidingTabLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pablo Arias on 31/05/15.
 */
public class FragmentAhorro extends BaseFragment {

    private String[] tabs = {"Ahorro Único", "Ahorro Programado"};
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
        return inflater.inflate(R.layout.fragment_ahorro, container, false);
    }
    // BEGIN_INCLUDE (fragment_onviewcreated)

    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the content view.
     * <p>
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
        mSlidingTabLayout.setTabsNumber(2);
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
            return 2;
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
         * <p>
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
            // Inflate a new layout from our resources
            View view = null;
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (position) {
                case 0:
                    view = getActivity().getLayoutInflater().inflate(R.layout.tab_ahorro_unico,
                            container, false);
                    // Add the newly created View to the ViewPager
                    container.addView(view);

                    final EditText editTextMonto = (EditText) view.findViewById(R.id.editTextMontoAU);
                    final EditText editTextDesc = (EditText) view.findViewById(R.id.editTextDescripcionAU);
                    final EditText editTextFecha = (EditText) view.findViewById(R.id.editTextFechaAU);
                    editTextFecha.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view1) {
                            // Process to get Current Date
                            final Calendar c = Calendar.getInstance();
                            int mYear = c.get(Calendar.YEAR);
                            int mMonth = c.get(Calendar.MONTH);
                            int mDay = c.get(Calendar.DAY_OF_MONTH);

                            // Launch Date Picker Dialog
                            DatePickerDialog dpd = new DatePickerDialog(view1.getContext(),
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view2, int year, int monthOfYear, int dayOfMonth) {
                                            // Display Selected date in textbox
                                            editTextFecha.setText(dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year);

                                        }
                                    }, mYear, mMonth, mDay);
                            dpd.show();
                        }
                    });
                    Button regAhorro = (Button) view.findViewById(R.id.buttonAhorroUnico);
                    regAhorro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view1) {
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
                            AhorroUnico ahorro = new AhorroUnico(monto, desc, fecha);
                            if (IngresaAhorro.manager.insertar(ahorro)) {
                                Mensaje(view1.getContext(), "Insertado correctamente");
                                editTextMonto.setText("");
                                editTextDesc.setText("");
                                editTextFecha.setText("");
                            } else
                                Mensaje(view1.getContext(), "No se pudo insertar, revise los valores e intente de nuevo.");
                        }
                    });
                    break;
                case 1:
                    view = getActivity().getLayoutInflater().inflate(R.layout.tab_ahorro_programado,
                            container, false);
                    // Add the newly created View to the ViewPager
                    container.addView(view);
                    final EditText editTextMontoFecha = (EditText) view.findViewById(R.id.editTextMontoAhorroProg);
                    final EditText editTextDescripcionFecha = (EditText) view.findViewById(R.id.editTextDescAhorroProg);


                    final EditText editTextFechaP = (EditText) view.findViewById(R.id.editTextFecAhorroProg);

                    final EditText editTextFechaP2 = (EditText) view.findViewById(R.id.editTextFecha2AhorroProg);

                    final RadioGroup radioGroupFecha = (RadioGroup) view.findViewById(R.id.radio_group_ahorro_prog);
                    final View finalView = view;
                    radioGroupFecha.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            // checkedId is the RadioButton selected
                            TextView textView;
                            EditText editText;
                            switch (checkedId) {
                                case R.id.radioButtonMensFecha:
                                    textView = (TextView) finalView.findViewById(R.id.textViewFecha2AhorroProg);
                                    editText = (EditText) finalView.findViewById(R.id.editTextFecha2AhorroProg);
                                    textView.setVisibility(View.GONE);
                                    editText.setVisibility(View.GONE);
                                    break;
                                case R.id.radioButtonQuinFecha:
                                    textView = (TextView) finalView.findViewById(R.id.textViewFecha2AhorroProg);
                                    editText = (EditText) finalView.findViewById(R.id.editTextFecha2AhorroProg);
                                    textView.setVisibility(View.VISIBLE);
                                    editText.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                    });
                    Button regIngreso = (Button) view.findViewById(R.id.buttonAhorroProgramado);
                    regIngreso.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view1) {
                            double monto = Double.parseDouble(editTextMontoFecha.getText().toString());
                            String desc = editTextDescripcionFecha.getText().toString();
                            String freq = ((RadioButton) view1.findViewById(radioGroupFecha.getCheckedRadioButtonId())).getText().toString();
                            String[] fechas = new String[2];
                            String fecha1 = editTextFechaP.getText().toString();
                            fechas[0] = fecha1;
                            if (editTextFechaP2.getVisibility() == View.VISIBLE) {
                                String fecha2 = editTextFechaP2.getText().toString();
                                fechas[1] = fecha2;
                            }
                            AhorroProgramado ahorro = new AhorroProgramado(monto, desc, fechas, freq);
                            if (IngresaIngreso.manager.insertar(ahorro)) {
                                Mensaje(view1.getContext(), "Insertado correctamente");
                                editTextMontoFecha.setText("");
                                editTextDescripcionFecha.setText("");
                                editTextFechaP.setText("");
                                editTextFechaP2.setText("");
                            } else
                                Mensaje(view1.getContext(), "No se pudo insertar, revise los valores e intente de nuevo.");
                        }
                    });
                    break;
            }


            Log.i("hola", "instantiateItem() [position: " + position + "]");

            // Return the View
            return view;
        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            Log.i("hola", "destroyItem() [position: " + position + "]");
        }

    }
}
