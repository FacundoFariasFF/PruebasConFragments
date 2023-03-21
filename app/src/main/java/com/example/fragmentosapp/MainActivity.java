package com.example.fragmentosapp;

import static java.time.temporal.ChronoUnit.DAYS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import androidx.lifecycle.Lifecycle;

import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
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



    MenuItem itemMonedas;
    boolean[] opciones = new boolean[7];

    static String compartirNombre="", compartirFecha="", compartirCompra="", compartirVenta="";
    static String fragmentActivo="";
    static List<Monedas> monedasList;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new FragmentPager()).commit();

        opciones[0]=true;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentCotizaciones.newInstance(opciones)).commit();

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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new FragmentPager()).commit();
                Toast.makeText(MainActivity.this,"abrir calendario",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_cotizaciones:
                Monedas();
                Toast.makeText(MainActivity.this,"abrir cotizaciones",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_whatsapp:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new FragmentWhatsapp()).commit();
                Compartir();
                Toast.makeText(MainActivity.this,"abrir whatsapp",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }
    //// Dialog fragment cotizaciones de monedas
    public void Monedas(){
        DialogFragment dialogo = new DialogFragment();
        dialogo.show(getSupportFragmentManager(), "dialogo");
        dialogo.ProcesarRespuesta(new DialogFragment.Respuestas() {
            @Override
            public void confirmar(DialogFragment dialog) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentCotizaciones.newInstance(dialogo.checks)).commit();
            }
            @Override
            public void cancelar(DialogFragment dialog) {
            }
        });
    }
//// metodo para compartir las cotizaciones que aparezcan en  pantalla
    public void Compartir(){

        DialogFragmentCompartir dialogCompartir = new DialogFragmentCompartir("Compartir Cotizacion");
        dialogCompartir.show(getSupportFragmentManager(),"dialogo");

        dialogCompartir.ProcesarRespuestaCompartir(new DialogFragmentCompartir.RespuestasCompartir() {
            @Override
            public void Confirmar(DialogFragment dialog) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                if (fragmentActivo.equals("HistorialDolarOficial")){ //preguntamos en que fragment estamos para saber que datos compartir
                    sendIntent.putExtra(Intent.EXTRA_TEXT, compartirNombre + ". " +compartirFecha+". Precio de compra: "+compartirCompra+ ". Precio de venta: "+compartirVenta);
                }else if (fragmentActivo.equals("CotizacionesHoy")){
                    String text ="";
                    for ( Monedas monedas : monedasList){
                        text+= monedas.getNombre()+", "+monedas.getFecha()+", Precio de compra: "+monedas.getCompra()+", Precio de venta: "+monedas.getVenta()+". ";
                    }
                    sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                }
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
            @Override
            public void Cancelar(DialogFragment dialog) {

            }
        });
    }
/////

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