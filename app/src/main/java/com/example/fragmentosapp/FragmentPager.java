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

    Boolean primeringreso = false;
    DateFormat formateadorGuion = new SimpleDateFormat("dd-MM-yyyy");
    DateFormat formateadorBarra = new SimpleDateFormat("dd/MM/yyyy");
    Date datefechaSelec,datefechaSelecdb, datefechaActualizar;


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




        //MostrarCalendario();

        FechaSeleccionada();

        pagerAdapter = new PagerAdapterDolar(getActivity(),cotizacionesEndPoint);
        viewPager.setAdapter(pagerAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);}
            @Override
            public void onPageSelected(int position){
                super.onPageSelected(position);
                //Toast.makeText(MainActivity.this,"fragment nro: "+position,Toast.LENGTH_SHORT).show();

                if (position==cotizacionesEndPoint.size()-1){

                    //diasMenos = diasMenos+7;
                    //diasMas = (diasMas) -8;

                    DateFormat formateadorGuion = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar calendarioAux= Calendar.getInstance();
                    calendarioAux.setTime(datefechaActualizar);
                    calendarioAux.add(Calendar.DAY_OF_YEAR, -7); /// es la variable de los dias que le resta a la fecha que busca el endpopint
                    String fechaMenosSieteDias= (formateadorGuion.format(calendarioAux.getTime()));

                    /*calendarioAux= Calendar.getInstance();
                    calendarioAux.setTime(datefechaActualizar);
                    calendarioAux.add(Calendar.DAY_OF_YEAR, +1); /// es la variable de los dias que le suma a la fecha que busca el endpopint
                    String fechaMasUnDia= (formateadorGuion.format(calendarioAux.getTime()));*/

                    String fechaMenosSieteDias = fechaMax;
                    String fechaMasUnDia = fechaMin;




                    historicos.clear();
                    AdminSQLiteOpenHelper.getInstance(getActivity()).Eliminar();






                    pagerAdapter.notifyDataSetChanged();
                    ObtenerDatos(fechaMenosSieteDias,fechaMasUnDia);
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

        historicos.clear();
        AdminSQLiteOpenHelper.getInstance(getActivity()).Eliminar();


        diasMenos= 7;
        diasMas=1; /// son las variables de los dias anteriores a la fecha que busca el endpopint

        Calendar calendarioAux= Calendar.getInstance();
        calendarioAux.setTime(datefechaSelec);
        calendarioAux.add(Calendar.DAY_OF_YEAR, -diasMenos); /// es la variable de los dias que le resta a la fecha que busca el endpopint
        String fechaMenosSieteDias= (formateadorGuion.format(calendarioAux.getTime()));

        datefechaActualizar = calendarioAux.getTime();

        calendarioAux= Calendar.getInstance();
        calendarioAux.setTime(datefechaSelec);
        calendarioAux.add(Calendar.DAY_OF_YEAR, +diasMas); /// es la variable de los dias que le suma a la fecha que busca el endpopint
        String fechaMasUnDia= (formateadorGuion.format(calendarioAux.getTime()));


        ObtenerDatos(fechaMenosSieteDias,fechaMasUnDia);

    }

    public void ObtenerDatos(String fechaMenosSieteDias, String fechaMasUnDia){ /// son las variables de los dias anteriores a la fecha que busca el endpopint

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
                    cotizacionesEndPoint.add(cotizaciones);
                }
                ///

                pagerAdapter.notifyDataSetChanged();

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

    public void RecibirFecha(String fecha){

        //poner fecha en la variable que quiera o hacer lo que quiera

    }

}