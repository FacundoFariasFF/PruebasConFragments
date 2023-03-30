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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
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


    static ArrayList<DolarHistorico> historialCotizaciones = new ArrayList();
    ArrayList<DolarHistorico> endPointCotizaciones = new ArrayList();
    ArrayList<DolarHistorico> endPointCotizacionesTop = new ArrayList();
    DolarHistorico cotizacion;


    LocalDate fechaSelec;
    LocalDate fechaActual;
    String fechaSelecdb;

    String fechaHoy;

    String fechaFragmentFinal;
    int diasMenos, diasMas;
    int nroFragment=0;
    Boolean topFragment= false;
    Boolean endFragment= false;
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


        /*Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.DAY_OF_YEAR,+1);
        DateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
        fechaMax = (formateador.format(calendario.getTime()));*/

        fechaActual= LocalDate.now();

        //MostrarCalendario();
        FechaSeleccionada();

        pagerAdapter = new PagerAdapterDolar(getActivity(),endPointCotizaciones);
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
                if (position==endPointCotizaciones.size()-1){
                    //muestra en pantalla la fecha menor final (esto no va en la app final)
                    Toast.makeText(getContext(),String.valueOf(endPointCotizaciones.get(position).getDolarFecha()),Toast.LENGTH_SHORT).show();
                    // contine la fecha que hay en fragment actual
                    LocalDate dateFechaFragment= endPointCotizaciones.get(position).getDolarFecha();
                    LocalDate fechaDiaMenor = dateFechaFragment.minusDays(7); //restamos 7 dias a la fecha seleccionada.
                    LocalDate fechaDiaMayor= dateFechaFragment;

                    //historicos.clear();
                    //AdminSQLiteOpenHelper.getInstance(getActivity()).Eliminar();

                    endFragment=true;
                    topFragment=false;
                    //pagerAdapter.notifyDataSetChanged();
                    ObtenerDatos(fechaDiaMenor,fechaDiaMayor);
                } else if(position==0){
                    //muestra en pantalla la fecha mayor final (esto no va en la app final)
                    Toast.makeText(getContext(),String.valueOf(endPointCotizaciones.get(position).getDolarFecha()),Toast.LENGTH_SHORT).show();
                    // contine la fecha que hay en fragment actual
                    LocalDate dateFechaFragment= endPointCotizaciones.get(position).getDolarFecha();
                    LocalDate fechaDiaMenor = dateFechaFragment;
                    LocalDate fechaDiaMayor= dateFechaFragment.plusDays(7); //sumamos 7 dias a la fecha seleccionada.

                    //historicos.clear();
                    //AdminSQLiteOpenHelper.getInstance(getActivity()).Eliminar();

                    topFragment=true;
                    endFragment=false;
                    //pagerAdapter.notifyDataSetChanged();
                    ObtenerDatos(fechaDiaMenor,fechaDiaMayor);
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
        fechaSelec = DialogCalendario.fechaSeleccionada; //fechaSeleccionada es tipo LocalDate "yyyy-MM-dd".

        DateTimeFormatter formatoBarra = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        fechaSelecdb = fechaSelec.format(formatoBarra); // pasamos de date a string.
        //LocalDate fechaSelec yyyy-MM-dd //String fechaSelecdb dd/MM/yyyy.

        LocalDate fechaMin = fechaSelec.minusDays(7); //restamos 7 dias a la fecha seleccionada.
        LocalDate fechaMax= fechaSelec.plusDays(8); //sumamos 7 dias a la fecha seleccionada.

        //historicos.clear();
        //AdminSQLiteOpenHelper.getInstance(getActivity()).Eliminar();

        ObtenerDatos(fechaMin,fechaMax);
    }
    public void ObtenerDatos(LocalDate fechaMin, LocalDate fechaMax){ /// son las variables de los dias anteriores y posteriores a la fecha que busca el endpopint.
        DateTimeFormatter formatoGuion = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fechaDiaMenor = fechaMin.format(formatoGuion); // pasamos de date a string.
        String fechaDiaMayor = fechaMax.format(formatoGuion);

        RequestQueue queue;
        queue = Volley.newRequestQueue(getActivity());
        //al final del url se puede modificar la fecha para obtener menos rango de datos.
        // Ejemplo: (https://mercados.ambito.com//dolar/formal/historico-general/01-01-2023/10-01-2023).
        //String fechaMin= "01-01-2023"; String fechaMax = "01-01-2030";
        String url = "https://mercados.ambito.com//dolar/formal/historico-general/"+fechaDiaMenor+"/"+fechaDiaMayor;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                //DolarOficial cotizaciones;
                LocalDate dateFecha,dateFechaAux, dateFechaRepetida;
                LocalDate fechaMaxAux = fechaMax.minusDays(1);//restamos un dia ya que fechaMax es un dia mayor porque el endpoint lo requiere.
                String fecha ;
                String compra;
                String venta;

                int dias=0;
                for (int i = 1; i<response.length(); i++) {
                    JSONArray mJsonArray = response.getJSONArray(i);
                    fecha = mJsonArray.getString(0); //fecha contiene la fecha del endpoint mediante su indice.

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    dateFechaAux = LocalDate.parse(fecha,formatter); // dateFechaAux tiene la fecha parseada a LocalDate para hacer calculos.
                    //como el endpoitn trae x cantidad de dias posteriores a la fecha seleccionada tenemos que operar con "fechaMaxAux".
                    while ((fechaMaxAux.isAfter(fechaActual))){ // fechaMaxAux es la fecha maxima de la seleccionada por el usuario()
                        fechaMaxAux = fechaMaxAux.minusDays(1);
                    }
                    //si la fecha del endpoint es igual a la maxima menos x dias, significa que la fecha es correcta y se ingresa a la lista.
                    if (dateFechaAux.isEqual(fechaMaxAux.minusDays(dias))){
                        dateFecha = dateFechaAux;
                        compra = mJsonArray.getString(1);
                        venta = mJsonArray.getString(2);
                        cotizacion = new DolarHistorico(dateFecha,compra,venta);
                        //endPointCotizaciones.add(cotizacion);
                        if (topFragment){
                            endPointCotizacionesTop.add(cotizacion);
                        }else {
                            endPointCotizaciones.add(cotizacion);
                        }
                        dias++;
                    }else{
                        dateFechaRepetida = dateFechaAux; //dateFechaRepetida tiene la fecha del endpoint.
                        //si dateFechaRepetida es distinta de fecha maxima menos "dias-1" dias (para consultar por el dia anterior al que esta en ejecucion).
                        if (!(dateFechaRepetida.isEqual(fechaMaxAux.minusDays(dias-1)))){
                            // mientras la fecha del endpoint sea distinta a la fecha que deberia ser.
                            // significa que el endpoint no trae ese dia y por lo tanto no hay valores.
                            while (!(dateFechaAux.isEqual(fechaMaxAux.minusDays(dias)))) {
                                dateFecha = fechaMaxAux.minusDays(dias);
                                compra = "No hay valores registrados.";
                                venta = "No hay valores registrados.";
                                cotizacion = new DolarHistorico(dateFecha,compra,venta);
                                //endPointCotizaciones.add(cotizacion);
                                if (topFragment){
                                    endPointCotizacionesTop.add(cotizacion);
                                }else {
                                    endPointCotizaciones.add(cotizacion);
                                }

                                dias++;
                            } // cuando la fecha es igual a la que deberia, sale del While y carga la fecha en la lista.
                            dateFecha = dateFechaAux;
                            compra = mJsonArray.getString(1);
                            venta = mJsonArray.getString(2);
                            cotizacion = new DolarHistorico(dateFecha,compra,venta);
                            //endPointCotizaciones.add(cotizacion);
                            if (topFragment){
                                endPointCotizacionesTop.add(cotizacion);
                            }else {
                                endPointCotizaciones.add(cotizacion);
                            }
                            dias++;
                        }
                    }
                    //recorremos el JSON y enviamos los datos al ArrayList endPointCotizaciones para luego cargar la Base de Datos.
                    //AdminSQLiteOpenHelper.getInstance(context).Registrar(fecha,compra,venta);
                }
                if (topFragment){
                    //int top= endPointCotizacionesTop.size()-1;
                    endPointCotizacionesTop.remove(endPointCotizacionesTop.size()-1);
                    endPointCotizacionesTop.addAll(endPointCotizaciones);
                    endPointCotizaciones = endPointCotizacionesTop;
                }
                ///
                Toast.makeText(getContext(),"cantidad de valores en la lista endpoint: "+ String.valueOf(endPointCotizaciones.size()),Toast.LENGTH_SHORT).show();
                ///


                int positionFragment=0;
                LocalDate fechaFragmentInicial = null;
                if (endFragment || topFragment){
                    if (endFragment){
                        fechaFragmentInicial = fechaMax;
                    }
                    else if (topFragment){
                        fechaFragmentInicial = fechaMin;
                    }
                }else {
                    fechaFragmentInicial = fechaSelec;
                }
                while (!fechaFragmentInicial.equals(endPointCotizaciones.get(positionFragment).getDolarFecha())) {
                    positionFragment++;
                }
                pagerAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(positionFragment, false);



                /*pagerAdapter.notifyDataSetChanged();
                // fechaI tiene la fecha del fragment maximo
                LocalDate fechaI=endPointCotizaciones.get(nroFragment).getDolarFecha();
                if (!fechaSelec.equals(fechaI)) {
                    nroFragment = 0;
                    //mientras se mantenga esa desigualdad
                    while (!fechaSelec.equals(fechaI)) {
                        //avanzan los fragments en busqueda del fragment que contiene la fecha igual a la seleccionada
                        nroFragment++;
                        fechaI = endPointCotizaciones.get(nroFragment).getDolarFecha();
                    }
                    //muestra el fragment que corresponde, el que tiene la fecha correcta.
                    viewPager.setCurrentItem(nroFragment, false);
                }*/




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
    /*public void ObtenerDatos(String fechaMin, String fechaMax){ /// son las variables de los dias anteriores y posteriores a la fecha que busca el endpopint

// Obtener Datos Del EndPoint
        //ArrayList<DolarOficial> cotizacionesEndPoint = new ArrayList();
        RequestQueue queue;
        queue = Volley.newRequestQueue(getActivity());
        //al final del url se puede modificar la fecha para obtener menos rango de datos
        // Ejemplo: (https://mercados.ambito.com//dolar/formal/historico-general/03-01-2023/06-01-2023)
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




                *//*pagerAdapter.notifyDataSetChanged();
                // fechaI tiene la fecha del fragment maximo
                String fechaI=cotizacionesEndPoint.get(nroFragment).getDolarFecha();
                    // si la fecha seleccionada es distinta a la fecha del fragment actual
                    if (!fechaSelecdb.equals(fechaI)) {
                        nroFragment = 0;
                        //mientras se mantenga esa desigualdad
                        while (!fechaSelecdb.equals(fechaI)) {
                            //avanzan los fragments en busqueda del fragment que contiene la fecha igual a la seleccionada
                            nroFragment++;
                            fechaI = cotizacionesEndPoint.get(nroFragment).getDolarFecha();
                        }
                        //muestra el fragment que corresponde, el que tiene la fecha correcta.
                        viewPager.setCurrentItem(nroFragment, false);
                    }*//*


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

    }*/

}