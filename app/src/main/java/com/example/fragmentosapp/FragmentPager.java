package com.example.fragmentosapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class FragmentPager extends Fragment {

    View rootView;
    private ViewPager2 viewPager;

    private FragmentStateAdapter pagerAdapter;

    static ArrayList<DolarOficial> historicos = new ArrayList();
    static String fechaSelec,fechaSelecdb = "00/00/0000";
    String fechaHoy;

    Boolean primeringreso = false;

    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;*/

    public FragmentPager() {
        // Required empty public constructor
    }

    public static FragmentPager newInstance() {
        FragmentPager fragment = new FragmentPager();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pager, container, false);

        viewPager =rootView.findViewById(R.id.viewpager2);


        MostrarCalendario();
        /*Calendar calendario = Calendar.getInstance();
        DateFormat formateadorGuion = new SimpleDateFormat("dd-M-yyyy");
        fechaHoy = (formateadorGuion.format(calendario.getTime()));
        //fechaSelec = "16-3-2023";*/




        return rootView;
    }
   /* @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {

            super.onBackPressed();
        } else {

            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }*/
    public void MostrarCalendario(){

        Calendar calendario = Calendar.getInstance();
        int year = calendario.get(Calendar.YEAR);
        int month = calendario.get(Calendar.MONTH);
        int dayOfMonth = calendario.get(Calendar.DAY_OF_MONTH);

        /*DateFormat formateadorGuion = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat formateadorBarra = new SimpleDateFormat("dd/MM/yyyy");

        fechaHoy = (formateadorGuion.format(calendario.getTime()));*/
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
               // Date datefechaSelec,datefechaSelecdb;

                fechaSelec = dayOfMonth + "-" + (month + 1) + "-" + year;
                FechaSeleccionada();
/*
                try {
                    datefechaSelec = formateadorGuion.parse(fechaSelec); //pasamos String a date para poder cambiarle el formato porq el string tiene un solo digito
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                fechaSelec = (formateadorGuion.format(datefechaSelec.getTime())); //cambiamos el formato de la fecha y lo ponemos en un string "dd-MM-yyyy"
                fechaSelecdb = fechaSelec;
                try {
                    datefechaSelecdb = formateadorGuion.parse(fechaSelecdb);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                fechaSelecdb = (formateadorBarra.format(datefechaSelecdb.getTime()));

                //Calendar calendarioAux= Calendar.getInstance(TimeZone.getTimeZone(fechaSelec));
                Calendar calendarioAux= Calendar.getInstance();
                calendarioAux.setTime(datefechaSelec);
                calendarioAux.add(Calendar.DAY_OF_YEAR, -7);
                String fechaMenosSieteDias= (formateadorGuion.format(calendarioAux.getTime()));

                calendarioAux= Calendar.getInstance();
                calendarioAux.setTime(datefechaSelec);
                calendarioAux.add(Calendar.DAY_OF_YEAR, +1);
                String fechaMasUnDia= (formateadorGuion.format(calendarioAux.getTime()));


                //fechaSelec 01-01-2023 //fechaSelecdb 01/01/2023

                historicos.clear();
                AdminSQLiteOpenHelper.getInstance(getActivity()).Eliminar();


// Obtener Datos Del EndPoint
                ArrayList<DolarOficial> cotizacionesEndPoint = new ArrayList();
                RequestQueue queue;
                queue = Volley.newRequestQueue(getActivity());
                //al final del url se puede modificar la fecha para obtener menos rango de datos
                // Ejemplo: (https://mercados.ambito.com//dolar/formal/historico-general/03-01-2023/06-01-2023)

                String fechaMin= fechaMenosSieteDias;
                String fechaMax = fechaMasUnDia;
                //String fechaMin= "01-01-2023";
                //String fechaMax = "01-01-2030";
                String url = "https://mercados.ambito.com//dolar/formal/historico-general/"+fechaMin+"/"+fechaMax;
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
                    try {
                        DolarOficial cotizaciones;
                        String fecha ;
                        String compra;
                        String venta;
                        for (int i = 1; i<response.length(); i++) {
                            JSONArray mJsonArray = response.getJSONArray(i);
                            fecha = mJsonArray.getString(0);
                            compra = mJsonArray.getString(1);
                            venta = mJsonArray.getString(2);

                            //recorremos el JSON y enviamos los datos al ArrayList cotizaciones para luego cargar la Base de Datos.
                            //AdminSQLiteOpenHelper.getInstance(context).Registrar(fecha,compra,venta);
                            cotizaciones = new DolarOficial(fecha,compra,venta);
                            cotizacionesEndPoint.add(cotizaciones);
                        }
                        ///
                        AdminSQLiteOpenHelper.getInstance(getActivity()).Registrar(cotizacionesEndPoint,datefechaSelecdb);
                        historicos = AdminSQLiteOpenHelper.getInstance(getActivity()).Buscar(fechaSelecdb);

                        pagerAdapter = new PagerAdapterDolar(getActivity(),historicos);
                        viewPager.setAdapter(pagerAdapter);
                        ///
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                });
                queue.add(request);*/

// Fin Obtencion Datos del EndPoint

            }
        },year,month,dayOfMonth);
        //calendario.set(2023, 2, 1);//Year,Mounth -1,Day
        calendario.set(2002, 3, 9);//Year,Mounth -1,Day
        dialog.getDatePicker().setMinDate(calendario.getTimeInMillis());
        calendario.set(year,month,dayOfMonth);
        dialog.getDatePicker().setMaxDate(calendario.getTimeInMillis());

        dialog.show();

    }

    public void FechaSeleccionada(){
        Calendar calendario = Calendar.getInstance();
        DateFormat formateadorGuion = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat formateadorBarra = new SimpleDateFormat("dd/MM/yyyy");
        Date datefechaSelec,datefechaSelecdb;

        fechaHoy = (formateadorGuion.format(calendario.getTime()));

        try {
            datefechaSelec = formateadorGuion.parse(fechaSelec); //pasamos String a date para poder cambiarle el formato porq el string tiene un solo digito
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        fechaSelec = (formateadorGuion.format(datefechaSelec.getTime())); //cambiamos el formato de la fecha y lo ponemos en un string "dd-MM-yyyy"
        fechaSelecdb = fechaSelec;
        try {
            datefechaSelecdb = formateadorGuion.parse(fechaSelecdb);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        fechaSelecdb = (formateadorBarra.format(datefechaSelecdb.getTime()));

        //Calendar calendarioAux= Calendar.getInstance(TimeZone.getTimeZone(fechaSelec));
        Calendar calendarioAux= Calendar.getInstance();
        calendarioAux.setTime(datefechaSelec);
        calendarioAux.add(Calendar.DAY_OF_YEAR, -7);
        String fechaMenosSieteDias= (formateadorGuion.format(calendarioAux.getTime()));

        calendarioAux= Calendar.getInstance();
        calendarioAux.setTime(datefechaSelec);
        calendarioAux.add(Calendar.DAY_OF_YEAR, +1);
        String fechaMasUnDia= (formateadorGuion.format(calendarioAux.getTime()));


        //fechaSelec 01-01-2023 //fechaSelecdb 01/01/2023

        historicos.clear();
        AdminSQLiteOpenHelper.getInstance(getActivity()).Eliminar();


// Obtener Datos Del EndPoint
        ArrayList<DolarOficial> cotizacionesEndPoint = new ArrayList();
        RequestQueue queue;
        queue = Volley.newRequestQueue(getActivity());
        //al final del url se puede modificar la fecha para obtener menos rango de datos
        // Ejemplo: (https://mercados.ambito.com//dolar/formal/historico-general/03-01-2023/06-01-2023)

        String fechaMin= fechaMenosSieteDias;
        String fechaMax = fechaMasUnDia;
        //String fechaMin= "01-01-2023";
        //String fechaMax = "01-01-2030";
        String url = "https://mercados.ambito.com//dolar/formal/historico-general/"+fechaMin+"/"+fechaMax;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                DolarOficial cotizaciones;
                String fecha ;
                String compra;
                String venta;
                for (int i = 1; i<response.length(); i++) {
                    JSONArray mJsonArray = response.getJSONArray(i);
                    fecha = mJsonArray.getString(0);
                    compra = mJsonArray.getString(1);
                    venta = mJsonArray.getString(2);

                    //recorremos el JSON y enviamos los datos al ArrayList cotizaciones para luego cargar la Base de Datos.
                    //AdminSQLiteOpenHelper.getInstance(context).Registrar(fecha,compra,venta);
                    cotizaciones = new DolarOficial(fecha,compra,venta);
                    cotizacionesEndPoint.add(cotizaciones);
                }
                ///
                AdminSQLiteOpenHelper.getInstance(getActivity()).Registrar(cotizacionesEndPoint,datefechaSelecdb);
                historicos = AdminSQLiteOpenHelper.getInstance(getActivity()).Buscar(fechaSelecdb);

                pagerAdapter = new PagerAdapterDolar(getActivity(),historicos);
                viewPager.setAdapter(pagerAdapter);
                ///
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });
        queue.add(request);

// Fin Obtencion Datos del EndPoint

    }

}