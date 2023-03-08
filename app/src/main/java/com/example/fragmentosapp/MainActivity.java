package com.example.fragmentosapp;

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
import android.widget.DatePicker;
import android.widget.Toast;


import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity{
    private ViewPager2 viewPager2;

    private DatePickerDialog.OnDateSetListener listener;

    MenuItem itemMonedas;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       viewPager2 = findViewById(R.id.viewpager2);

        viewPager2.setAdapter(new AdaptadorFragment(getSupportFragmentManager(),getLifecycle()));

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
            switch (position){
                case 6: return FragmentDolarOficial.newInstance("6");
                case 5: return FragmentDolarOficial.newInstance("5");
                case 4: return FragmentDolarOficial.newInstance("4");
                case 3: return FragmentDolarOficial.newInstance("3");
                case 2: return FragmentDolarOficial.newInstance("2");
                case 1: return FragmentDolarOficial.newInstance("1");
                case 0: return FragmentDolarOficial.newInstance("0");
               // case 1: return  new FragmentCotizaciones();
                //case 0: return  new FragmentWhatsapp();
                default: return  new FragmentCotizaciones();
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
                Toast.makeText(MainActivity.this,"Retroceder",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_calendario:
                MostrarCalendario();
                Toast.makeText(MainActivity.this,"abrir calendario",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_cotizaciones:
                //viewPager2.setCurrentItem(1);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new FragmentCotizaciones()).commit();
                Toast.makeText(MainActivity.this,"abrir cotizaciones",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_whatsapp:
                viewPager2.setCurrentItem(0);
                Toast.makeText(MainActivity.this,"abrir whatsapp",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }
    ///
    // calendario
    public void MostrarCalendario(){
        String fechaHoy;
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
                String fechaSelec,fechaSelecdb;
                Date datefechaSelec,datefechaSelecdb;

                fechaSelec = dayOfMonth + "-" + (month+1) + "-" + year;
                try {
                    datefechaSelec = formateadorGuion.parse(fechaSelec); //pasamos String a date para poder cambiarle el formato porq el string tiene un solo digito
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                fechaSelec= (formateadorGuion.format(datefechaSelec.getTime())); //cambiamos el formato de la fecha y lo ponemos en un string "dd-MM-yyyy"

                fechaSelecdb= fechaSelec;
                try {
                    datefechaSelecdb = formateadorGuion.parse(fechaSelecdb);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                fechaSelecdb= (formateadorBarra.format(datefechaSelecdb.getTime()));


                Toast.makeText(MainActivity.this,fechaSelec +"y "+ fechaSelecdb,Toast.LENGTH_SHORT).show();
                if (!fechaSelec.equals(fechaHoy)){
                    itemMonedas.setVisible(false);
                }else{
                    itemMonedas.setVisible(true);
                }

                ObtenerDatosEndPoint obtenerDatosEndPoint = new ObtenerDatosEndPoint();
                String fechaMenosSieteDias= "01-01-2023";
                obtenerDatosEndPoint.ObtenerDatosVolleyFechas(getApplicationContext(), fechaMenosSieteDias);
                AdminSQLiteOpenHelper.getInstance(getApplicationContext()).Buscar(fechaSelecdb);

            }
        },year,month,dayOfMonth);

        calendario.set(2002, 3, 9);//Year,Mounth -1,Day
        dialog.getDatePicker().setMinDate(calendario.getTimeInMillis());
        calendario.set(year,month,dayOfMonth);
        dialog.getDatePicker().setMaxDate(calendario.getTimeInMillis());

        dialog.show();
    }
/////////////
}