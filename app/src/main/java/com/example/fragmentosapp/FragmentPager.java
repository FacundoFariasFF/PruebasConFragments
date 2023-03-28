package com.example.fragmentosapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

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
    ArrayList<DolarOficial> cotizacionesEndPoint = new ArrayList();
    DolarOficial cotizaciones;
    static String fechaSelec,fechaSelecdb = "00/00/0000";
    String fechaMin, fechaMax;
    String fechaHoy;
    int diasMenos, diasMas;
    int nroFragment=0;
    Boolean top= false;
    DateFormat formateadorGuion = new SimpleDateFormat("dd-MM-yyyy");
    DateFormat formateadorBarra = new SimpleDateFormat("dd/MM/yyyy");
    Date datefechaSelec,datefechaSelecdb, datefechaFragment;






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


        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.DAY_OF_YEAR,+1);
        DateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
        fechaMax = (formateador.format(calendario.getTime()));


        //MostrarCalendario();

        FechaSeleccionada();

        pagerAdapter = new PagerAdapterDolar(getActivity(),cotizacionesEndPoint);
        viewPager.setAdapter(pagerAdapter);



        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            @Override
            public void onPageSelected(int position){
                super.onPageSelected(position);
                //Toast.makeText(MainActivity.this,"fragment nro: "+position,Toast.LENGTH_SHORT).show();

                if (position==cotizacionesEndPoint.size()-1){
                    //muestra en pantalla la fecha menor final (esto no va en la app final)
                    Toast.makeText(getContext(),cotizacionesEndPoint.get(position).getDolarFecha(),Toast.LENGTH_SHORT).show();

                    // contine la fecha que hay en fragment actual
                    String fechaFragment= cotizacionesEndPoint.get(position).getDolarFecha();

                    //// como la fecha viene "00/00/0000" la convierte a "00-00-0000" que es lo necesita el endpoint
                    try {
                        datefechaFragment = formateadorBarra.parse(fechaFragment);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    fechaFragment= (formateadorGuion.format(datefechaFragment.getTime()));
                    try {
                        datefechaFragment = formateadorGuion.parse(fechaFragment);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    //// datefechaFragment es un date con formato "00-00-0000"

                    //seteamos el datefechaFragment al Calendar para restarle 7 dias y poner esa fecha en un string
                    Calendar calendarioAux;
                    calendarioAux= Calendar.getInstance();
                    calendarioAux.setTime(datefechaFragment);
                    calendarioAux.add(Calendar.DAY_OF_YEAR, -7);
                    //string que contiene la fecha de 7 dias antes
                    String fechaMenosSieteDias= (formateadorGuion.format(calendarioAux.getTime()));
                    // String que tiene el dia del fragment actual para que funcione como cota superior en el rango de fechas
                    String fechaMasUnDia= (formateadorGuion.format(datefechaFragment.getTime()));

                    //historicos.clear();
                    //AdminSQLiteOpenHelper.getInstance(getActivity()).Eliminar();

                    pagerAdapter.notifyDataSetChanged();

                    ObtenerDatos(fechaMenosSieteDias,fechaMasUnDia); // enviamos la fecha minima y maxima
                } else if(position==0){
                    Toast.makeText(getContext(),cotizacionesEndPoint.get(position).getDolarFecha(),Toast.LENGTH_SHORT).show();

                    // contine la fecha que hay en fragment actual
                    String fechaFragment= cotizacionesEndPoint.get(position).getDolarFecha();

                    //// como la fecha viene "00/00/0000" la convierte a "00-00-0000" que es lo necesita el endpoint
                    try {
                        datefechaFragment = formateadorBarra.parse(fechaFragment);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    fechaFragment= (formateadorGuion.format(datefechaFragment.getTime()));
                    try {
                        datefechaFragment = formateadorGuion.parse(fechaFragment);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    //// datefechaFragment es un date con formato "00-00-0000"

                    //seteamos el datefechaFragment al Calendar para restarle 7 dias y poner esa fecha en un string
                    Calendar calendarioAux;
                    calendarioAux= Calendar.getInstance();
                    calendarioAux.setTime(datefechaFragment);
                    calendarioAux.add(Calendar.DAY_OF_YEAR, +7);
                    //string que contiene la fecha de 7 dias antes
                    String fechaMenosSieteDias= (formateadorGuion.format(datefechaFragment.getTime()));
                    // String que tiene el dia del fragment actual para que funcione como cota superior en el rango de fechas
                    String fechaMasUnDia= (formateadorGuion.format(calendarioAux.getTime()));

                    //historicos.clear();
                    //AdminSQLiteOpenHelper.getInstance(getActivity()).Eliminar();
                    top=true;

                    pagerAdapter.notifyDataSetChanged();

                    ObtenerDatos(fechaMenosSieteDias,fechaMasUnDia); // enviamos la fecha minima y maxima
                }


            }
            @Override
            public void onPageScrollStateChanged(int state){
                super.onPageScrollStateChanged(state);
            }
        });

        return rootView;
    }




    public void FechaSeleccionada(){

        //ponemos la fecha que viene de la selccion del usuario en el calendario.
        //en caso de ser el inicio de la app "DialogCalendario.fechaSelec" trae la fecha actual.
        fechaSelec = DialogCalendario.fechaSelec;

        Calendar calendario = Calendar.getInstance();
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
        //fechaSelec 01-01-2023 //fechaSelecdb 01/01/2023

        //historicos.clear();
        //AdminSQLiteOpenHelper.getInstance(getActivity()).Eliminar();

        diasMenos= 7;
        diasMas=7; /// son las variables de los dias anteriores a la fecha que busca el endpopint
        Calendar calendarioAux;
        calendarioAux= Calendar.getInstance();
        calendarioAux.setTime(datefechaSelec);
        calendarioAux.add(Calendar.DAY_OF_YEAR, -diasMenos); /// es la variable de los dias que le resta a la fecha que busca el endpopint
        String fechaMenosSieteDias= (formateadorGuion.format(calendarioAux.getTime()));


        calendarioAux= Calendar.getInstance();
        calendarioAux.setTime(datefechaSelec);
        calendarioAux.add(Calendar.DAY_OF_YEAR, +diasMas); /// es la variable de los dias que le suma a la fecha que busca el endpopint
        String fechaMasUnDia= (formateadorGuion.format(calendarioAux.getTime()));


        // en el primer ingreso a la app o en caso de busqueda por el usuario
        //buscamos datos de 7 dias antes y 7 dias despues de la fecha
        ObtenerDatos(fechaMenosSieteDias,fechaMasUnDia);

    }

    public void ObtenerDatos(String fechaMenosSieteDias, String fechaMasUnDia){ /// son las variables de los dias anteriores y posteriores a la fecha que busca el endpopint

// Obtener Datos Del EndPoint
        //ArrayList<DolarOficial> cotizacionesEndPoint = new ArrayList();
        RequestQueue queue;
        queue = Volley.newRequestQueue(getActivity());
        //al final del url se puede modificar la fecha para obtener menos rango de datos
        // Ejemplo: (https://mercados.ambito.com//dolar/formal/historico-general/03-01-2023/06-01-2023)

        fechaMin= fechaMenosSieteDias;
        fechaMax = fechaMasUnDia;
        //String fechaMin= "01-01-2023";
        //String fechaMax = "01-01-2030";
        String url = "https://mercados.ambito.com//dolar/formal/historico-general/"+fechaMin+"/"+fechaMax;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                //DolarOficial cotizaciones;
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
                    if (top){
                        cotizacionesEndPoint.add(i-1,cotizaciones);
                    }else {
                    cotizacionesEndPoint.add(cotizaciones);
                    }
                }
                ///

                pagerAdapter.notifyDataSetChanged();

                // fechaI tiene la fecha del fragment maximo
                String fechaI=cotizacionesEndPoint.get(nroFragment).getDolarFecha();
                // si la fecha seleccionada es distinta a la fecha del fragment actual
                if (!fechaSelecdb.equals(fechaI)) {
                    nroFragment=0;
                    //mientras se mantenga esa desigualdad
                    while (!fechaSelecdb.equals(fechaI)) {
                        //avanzan los fragments en busqueda del fragment que contiene la fecha igual a la seleccionada
                        nroFragment++;
                        fechaI = cotizacionesEndPoint.get(nroFragment).getDolarFecha();
                    }
                    //muestra el fragment que corresponde, el que tiene la fecha correcta.
                    viewPager.setCurrentItem(nroFragment, false);
                }


                //cambiar las fechas usar en la base dechas

                //AdminSQLiteOpenHelper.getInstance(getActivity()).Registrar(cotizacionesEndPoint,datefechaSelecdb);
                //historicos = AdminSQLiteOpenHelper.getInstance(getActivity()).Buscar(fechaSelecdb);

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