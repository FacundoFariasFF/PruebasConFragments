package com.example.fragmentosapp;

import static java.time.temporal.ChronoUnit.DAYS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.lifecycle.Lifecycle;

import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity{

    private ViewPager2 viewPager;

    private FragmentStateAdapter pagerAdapter;

    static ArrayList<DolarOficial> historicos = new ArrayList();

    MenuItem itemMonedas;

    static String fechaSelec,fechaSelecdb = "00/00/0000";
    String fechaHoy;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       /* ObtenerDatosEndPoint obtenerDatosEndPoint = new ObtenerDatosEndPoint();
        String fechaMenosSieteDias= "01-03-2023";
        obtenerDatosEndPoint.ObtenerDatosVolleyFechas(getApplicationContext(), fechaMenosSieteDias);

        AdminSQLiteOpenHelper.getInstance(getApplicationContext()).Buscar();

        viewPager = findViewById(R.id.viewpager2);
        pagerAdapter = new PagerAdapterDolar(this,historicos);
        viewPager.setAdapter(pagerAdapter);*/

    }
    //// menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        itemMonedas = menu.findItem(R.id.menu_cotizaciones);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.menu_back:
                System.exit(0);
                Toast.makeText(MainActivity.this,"Retroceder",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_calendario:
                MostrarCalendario();
                Toast.makeText(MainActivity.this,"abrir calendario",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_cotizaciones:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new FragmentCotizaciones()).commit();
                Toast.makeText(MainActivity.this,"abrir cotizaciones",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_whatsapp:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new FragmentWhatsapp()).commit();
                Toast.makeText(MainActivity.this,"abrir whatsapp",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }
    /// calendario
    public void MostrarCalendario(){

        Calendar calendario = Calendar.getInstance();
        int year = calendario.get(Calendar.YEAR);
        int month = calendario.get(Calendar.MONTH);
        int dayOfMonth = calendario.get(Calendar.DAY_OF_MONTH);

        DateFormat formateadorGuion = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat formateadorBarra = new SimpleDateFormat("dd/MM/yyyy");

        fechaHoy = (formateadorGuion.format(calendario.getTime()));
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Date datefechaSelec,datefechaSelecdb;

                fechaSelec = dayOfMonth + "-" + (month + 1) + "-" + year;

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
                AdminSQLiteOpenHelper.getInstance(getApplicationContext()).Eliminar();


// Obtener Datos Del EndPoint

                ArrayList<DolarOficial> cotizacionesEndPoint = new ArrayList();
                RequestQueue queue;
                queue = Volley.newRequestQueue(getApplicationContext());
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
                        for (int i = 1; i<response.length(); i++){
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
                        AdminSQLiteOpenHelper.getInstance(getApplicationContext()).Registrar(cotizacionesEndPoint);
                        historicos = AdminSQLiteOpenHelper.getInstance(getApplicationContext()).Buscar(fechaSelecdb);

                        viewPager = findViewById(R.id.viewpager2);
                        pagerAdapter = new PagerAdapterDolar(MainActivity.this,historicos);
                        viewPager.setAdapter(pagerAdapter);
                        ///
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                });
                queue.add(request);

// Fin Obtencion Datos del EndPoitn


                //historicos = AdminSQLiteOpenHelper.getInstance(getApplicationContext()).Buscar(fechaSelecdb);


                /*viewPager = findViewById(R.id.viewpager2);
                pagerAdapter = new PagerAdapterDolar(MainActivity.this,historicos);
                viewPager.setAdapter(pagerAdapter);*/

            }
        },year,month,dayOfMonth);
        //calendario.set(2023, 2, 1);//Year,Mounth -1,Day
        calendario.set(2002, 3, 9);//Year,Mounth -1,Day
        dialog.getDatePicker().setMinDate(calendario.getTimeInMillis());
        calendario.set(year,month,dayOfMonth);
        dialog.getDatePicker().setMaxDate(calendario.getTimeInMillis());

        dialog.show();

    }

    public void ItemMoneda(@NonNull String fechaSelec, String fechaHoy ){
        if (!fechaSelec.equals(fechaHoy)){
            itemMonedas.setEnabled(false);
            itemMonedas.setVisible(false);
        }else{
            itemMonedas.setEnabled(true);
            itemMonedas.setVisible(true);
        }
    }

/////////////
}