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


import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity{
    private ViewPager2 viewPager2;
    MenuItem itemMonedas;

    static String fechaSelec,fechaSelecdb = "00/00/0000";
    String fechaHoy,fechaHoyAux;
    static String fecha,compra,venta;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.viewpager2);


    }
    ////
    class AdaptadorFragment extends FragmentStateAdapter {

        //constructor de fragments
        public AdaptadorFragment(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            String fechaselccionada = fechaSelecdb;
            String fechaAux;
            SimpleDateFormat formateadorBarra = new SimpleDateFormat("dd/MM/yyyy");

            Date datefechaSelecdb;
            try {
                datefechaSelecdb = formateadorBarra.parse(fechaselccionada);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Calendar calendarioAux = Calendar.getInstance();
            calendarioAux.set(Calendar.DAY_OF_MONTH, datefechaSelecdb.getDate());

            switch (position) {
                case 6:
                    calendarioAux.add(Calendar.DAY_OF_YEAR, -6);
                    fechaAux= (formateadorBarra.format(calendarioAux.getTime()));
                    AdminSQLiteOpenHelper.getInstance(getApplicationContext()).Buscar(fechaAux);
                    ItemMoneda(fechaAux, fechaHoyAux);
                    return FragmentDolarOficial.newInstance("6", fecha, compra, venta);
                case 5:
                    calendarioAux.add(Calendar.DAY_OF_YEAR, -5);
                    fechaAux= (formateadorBarra.format(calendarioAux.getTime()));
                    AdminSQLiteOpenHelper.getInstance(getApplicationContext()).Buscar(fechaAux);
                    ItemMoneda(fechaAux, fechaHoyAux);
                    return FragmentDolarOficial.newInstance("5", fecha, compra, venta);
                case 4:
                    calendarioAux.add(Calendar.DAY_OF_YEAR, -4);
                    fechaAux= (formateadorBarra.format(calendarioAux.getTime()));
                    AdminSQLiteOpenHelper.getInstance(getApplicationContext()).Buscar(fechaAux);
                    ItemMoneda(fechaAux, fechaHoyAux);
                    return FragmentDolarOficial.newInstance("4", fecha, compra, venta);
                case 3:
                    calendarioAux.add(Calendar.DAY_OF_YEAR, -3);
                    fechaAux= (formateadorBarra.format(calendarioAux.getTime()));
                    AdminSQLiteOpenHelper.getInstance(getApplicationContext()).Buscar(fechaAux);
                    ItemMoneda(fechaAux, fechaHoyAux);
                    return FragmentDolarOficial.newInstance("3", fecha, compra, venta);
                case 2:
                    calendarioAux.add(Calendar.DAY_OF_YEAR, -2);
                    fechaAux= (formateadorBarra.format(calendarioAux.getTime()));
                    AdminSQLiteOpenHelper.getInstance(getApplicationContext()).Buscar(fechaAux);
                    ItemMoneda(fechaAux, fechaHoyAux);
                    return FragmentDolarOficial.newInstance("2", fecha, compra, venta);
                case 1:
                    calendarioAux.add(Calendar.DAY_OF_YEAR, -1);
                    fechaAux= (formateadorBarra.format(calendarioAux.getTime()));;
                    AdminSQLiteOpenHelper.getInstance(getApplicationContext()).Buscar(fechaAux);
                    ItemMoneda(fechaAux, fechaHoyAux);
                    return FragmentDolarOficial.newInstance("1", fecha, compra, venta);
                case 0:
                    ItemMoneda(fechaselccionada, fechaHoyAux);
                    fechaselccionada = (formateadorBarra.format(datefechaSelecdb.getTime()));
                    AdminSQLiteOpenHelper.getInstance(getApplicationContext()).Buscar(fechaselccionada);
                    return FragmentDolarOficial.newInstance("0", fecha, compra, venta);
                default:
                    return new FragmentCotizaciones();
            }
        }
        @Override
        public int getItemCount() {
            return 7;
        }
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
                //DialogFragment dialogFragment = new DialogFragment();
                //dialogFragment.show(getSupportFragmentManager(),"DialogFragment");

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

        fechaHoyAux = (formateadorBarra.format(calendario.getTime()));
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

                ObtenerDatosEndPoint obtenerDatosEndPoint = new ObtenerDatosEndPoint();
                String fechaMenosSieteDias= "01-03-2023";
                obtenerDatosEndPoint.ObtenerDatosVolleyFechas(getApplicationContext(), fechaMenosSieteDias);
                viewPager2.setAdapter(new AdaptadorFragment(getSupportFragmentManager(),getLifecycle()));

            }
        },year,month,dayOfMonth);
        calendario.set(2023, 2, 1);//Year,Mounth -1,Day
        //calendario.set(2002, 3, 9);//Year,Mounth -1,Day
        dialog.getDatePicker().setMinDate(calendario.getTimeInMillis());
        calendario.set(year,month,dayOfMonth);
        dialog.getDatePicker().setMaxDate(calendario.getTimeInMillis());

        dialog.show();
    }
    public void ItemMoneda(@NonNull String fechaSelec, String fechaHoy ){
        if (!fechaSelec.equals(fechaHoyAux)){
            itemMonedas.setEnabled(false);
            itemMonedas.setVisible(false);
        }else{
            itemMonedas.setEnabled(true);
            itemMonedas.setVisible(true);
        }
    }


/////////////
}