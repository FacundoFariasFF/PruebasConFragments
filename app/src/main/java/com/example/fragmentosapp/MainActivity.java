package com.example.fragmentosapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    static MenuItem itemMonedas;
    static String compartirNombre="", compartirFecha="", compartirCompra="", compartirVenta="";
    static String fragmentActivo="";
    static List<Monedas> monedasList;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DialogCalendario.fechaSeleccionada = LocalDate.now();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new FragmentPager()).commit();

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
                Calendario();
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new FragmentPager()).commit();
                Toast.makeText(MainActivity.this,"abrir calendario",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_cotizaciones:
                Monedas();
                Toast.makeText(MainActivity.this,"abrir cotizaciones",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_compartir:
                Compartir();
                Toast.makeText(MainActivity.this,"abrir whatsapp",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }
////
    static public void ItemMoneda(@NonNull LocalDate fecha){
        LocalDate fechaDiaDeHoy = LocalDate.now();
        if (!fechaDiaDeHoy.equals(fecha)){
            itemMonedas.setEnabled(false);
            itemMonedas.setVisible(false);
        }else{
            itemMonedas.setEnabled(true);
            itemMonedas.setVisible(true);
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
//// metodo para compartir las cotizaciones que aparezcan en pantalla
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
    public void Calendario(){
        DialogCalendario dialogCalendario= new DialogCalendario();
        dialogCalendario.show(getSupportFragmentManager(),"dialogo");
        dialogCalendario.ProcesarRespuestaCalendario(new DialogCalendario.RespuestasCalendario() {
            @Override
            public void ConfirmarCalendario(DialogFragment dialogCalendario) {

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new FragmentPager()).commit();


            }
            @Override
            public void CancelarCalendario(DialogFragment dialogCalendario) {
            }
        });
    }
/////
}