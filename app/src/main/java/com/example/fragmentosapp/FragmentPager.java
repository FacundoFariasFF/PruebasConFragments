package com.example.fragmentosapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.core.view.OneShotPreDrawListener;
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
    LocalDate fechaActual= LocalDate.now();;
    String fechaSelecdb;
    Boolean topFragment= false, endFragment= false;

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

        FechaSeleccionada();
        //pagerAdapter = new PagerAdapterDolar(getActivity(),endPointCotizaciones);
        //viewPager.setAdapter(pagerAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            @Override
            public void onPageSelected(int position){
                super.onPageSelected(position);
                LocalDate dateFechaFragment, fechaDiaMenor,fechaDiaMayor;
                //Toast.makeText(MainActivity.this,"fragment nro: "+position,Toast.LENGTH_SHORT).show();
                if (position==endPointCotizaciones.size()-1){
                    //muestra en pantalla la fecha menor final (esto no va en la app final)
                    //Toast.makeText(getContext(),String.valueOf(endPointCotizaciones.get(position).getDolarFecha()),Toast.LENGTH_SHORT).show();
                    // contine la fecha que hay en fragment actual
                    dateFechaFragment= endPointCotizaciones.get(position).getDolarFecha();
                    fechaDiaMenor = dateFechaFragment.minusDays(7); //restamos 7 dias a la fecha seleccionada.
                    fechaDiaMayor= dateFechaFragment;

                    //historicos.clear();
                    //AdminSQLiteOpenHelper.getInstance(getActivity()).Eliminar();

                    endFragment=true;
                    topFragment=false;
                    //pagerAdapter.notifyDataSetChanged();
                    ObtenerDatos(fechaDiaMenor,fechaDiaMayor);
                } else if(position==0){
                    //muestra en pantalla la fecha mayor final (esto no va en la app final)
                    //Toast.makeText(getContext(),String.valueOf(endPointCotizaciones.get(position).getDolarFecha()),Toast.LENGTH_SHORT).show();
                    // contine la fecha que hay en fragment actual
                    dateFechaFragment= endPointCotizaciones.get(position).getDolarFecha();
                    fechaDiaMenor = dateFechaFragment;
                    fechaDiaMayor= dateFechaFragment.plusDays(7); //sumamos 7 dias a la fecha seleccionada.

                    //historicos.clear();
                    //AdminSQLiteOpenHelper.getInstance(getActivity()).Eliminar();

                    topFragment=true;
                    endFragment=false;
                    //el siguiente if valida que si esta en el dia actual no debe buscar mas datos (porque logicamente no hay mas)
                    if (!fechaDiaMenor.isEqual(fechaActual)) {
                        ObtenerDatos(fechaDiaMenor, fechaDiaMayor);
                    }
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
                    endPointCotizacionesTop.remove(endPointCotizacionesTop.size()-1); //quitamos el valor porque esta repetido.
                    endPointCotizacionesTop.addAll(endPointCotizaciones);// lo agregamos a la otra lista.
                    endPointCotizaciones= (ArrayList<DolarHistorico>) endPointCotizacionesTop.clone();//clonamos la lsita para manjarlas como objetos diferentes
                    endPointCotizacionesTop.clear();// limpiamos la lista de dias mayores
                }
                //Toast.makeText(getContext(),"cantidad de valores en la lista endpoint: "+ String.valueOf(endPointCotizaciones.size()),Toast.LENGTH_SHORT).show();


                int positionFragment=0;
                LocalDate fechaFragmentInicial = null;
                //preguntamos si esstamos en el final del fragment o en el principio.
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
                //fechaFragmentInicial es la fecha que debe mostrar el pager al modificarse.
                //el while recorre todos los fragments hasta encontrar el correcto y luego lo muestra.
                while (!fechaFragmentInicial.equals(endPointCotizaciones.get(positionFragment).getDolarFecha())) {
                    positionFragment++;
                }
                pagerAdapter = new PagerAdapterDolar(getActivity(),endPointCotizaciones);
                viewPager.setAdapter(pagerAdapter);
                //pagerAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(positionFragment, false);

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