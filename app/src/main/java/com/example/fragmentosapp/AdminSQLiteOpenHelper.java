package com.example.fragmentosapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


//esta class administra la db
public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {




    private static AdminSQLiteOpenHelper instance;
    //AdminSQLiteOpenHelper admin;
    AdminSQLiteOpenHelper admin;
    SQLiteDatabase BaseDeDatos;

    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase BaseDeDatos) {
        //creamos una tabla llamada historico con columnas (fecha, compra, venta)
        BaseDeDatos.execSQL("create table historico(fecha text primary key, compra text, venta text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS  " + "historico");
        onCreate(sqLiteDatabase);
    }

    //este metodo regresa una instancia unica del objeto
    public static AdminSQLiteOpenHelper getInstance(Context context) {
        //si instance es null significa que aun no se creo una instacia de este objeto, y debemos crearla
        if (instance == null) {
            instance = new AdminSQLiteOpenHelper(context.getApplicationContext(), "dbdolar.db", null, 1);
        }
        return instance;
    }

    public void open() {
        admin = AdminSQLiteOpenHelper.instance;
        BaseDeDatos = admin.getWritableDatabase();
    }

    public void close() {

        BaseDeDatos.close();
    }

    public void Registrar(ArrayList<DolarOficial> cotizaciones, Date datefechaSelecdb) {
        String fecha="", compra="", venta="", fechaAux,fechaMenosDias;
        Date dateFechaMenosDias,datefechaEndpoint;
        int cantDias =0;
        DateFormat formateadorBarra = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendarioAux= Calendar.getInstance();
        open();
            for (int j=0; j<cotizaciones.size();j++) {
                fechaAux = String.valueOf(cotizaciones.get(j).getDolarFecha()); // obtenemos la primer fecha del endpoint
                try {
                    datefechaEndpoint = formateadorBarra.parse(fechaAux); // convertimos la primer fecha del enpoint a date
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                calendarioAux.setTime(datefechaSelecdb);      // seteamos al calendario la fecha seleccionada
                calendarioAux.add(Calendar.DAY_OF_YEAR, -cantDias); //le restamos x dias (para ir comparando luego)
                fechaMenosDias= (formateadorBarra.format(calendarioAux.getTime())); //pasamos el calendario a string
                try {
                    dateFechaMenosDias = formateadorBarra.parse(fechaMenosDias); //pasamos de string a date(con formato correcto)la fecha menos x dias
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                if (datefechaEndpoint.equals(datefechaSelecdb)) { //si la fecha del endpoint es igual a la seleccionada por el usuario
                    fecha = String.valueOf(cotizaciones.get(j).getDolarFecha());
                    compra = String.valueOf(cotizaciones.get(j).getDolarCompra());
                    venta = String.valueOf(cotizaciones.get(j).getDolarVenta());
                } else {
                    if (datefechaEndpoint.equals(dateFechaMenosDias)) { //si la fecha del endpoint es igual a la fecha anterior de la seleccionada
                        fecha = String.valueOf(cotizaciones.get(j).getDolarFecha());
                        compra = String.valueOf(cotizaciones.get(j).getDolarCompra());
                        venta = String.valueOf(cotizaciones.get(j).getDolarVenta());
                    } else {
                        fecha = fechaMenosDias;
                        compra = "No hay valores registrados.";
                        venta = "No hay valores registrados.";
                        j=j-1;
                    }
                }
                cantDias++; //cant dias es el numero de dias que se resta a la fecha seleccionada por el usuario
                ContentValues registro = new ContentValues();
                registro.put("fecha", fecha);
                registro.put("compra", compra);
                registro.put("venta", venta);
                BaseDeDatos.insert("historico", null, registro);
            }
        close();
    }
    public ArrayList<DolarOficial> Buscar(String db_fecha_cal) {
        //public void Buscar() {
        String fecha;
        String compra;
        String venta;
        ArrayList<DolarOficial> historicos = new ArrayList();

        //creamos una validacion para que no este el campo fecha vacio
        open();

        Cursor fila = BaseDeDatos.rawQuery("SELECT fecha, compra, venta FROM historico", null);
        //Cursor fila = BaseDeDatos.rawQuery("SELECT compra, venta FROM historico WHERE fecha='" + db_fecha_cal + "'", null);
        DolarOficial dolar;
        // lo siguiente retorna true si encuentra datos dentro de la "tabla" y los muestra en pantalla

        Date datefechaSelec,datefechadb;
        String fechadb;
        DateFormat formateadorBarra = new SimpleDateFormat("dd/MM/yyyy");

        if (fila.moveToFirst()) {
            fechadb=(fila.getString(0));

            try {
                datefechadb = formateadorBarra.parse(fechadb);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            try {
                datefechaSelec = formateadorBarra.parse(db_fecha_cal);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            do {
                if (datefechadb.equals(datefechaSelec)) {
                    fecha = (fila.getString(0));
                    compra = (fila.getString(1));
                    venta = (fila.getString(2));

                    dolar = new DolarOficial(fecha,compra,venta);
                    historicos.add(dolar);
                }
            } while (fila.moveToNext()); // hay que hacer que pare en 7 dias
        }
        close();
        return historicos;
    }

    //metodo para elimiar
    public void Eliminar() {
        open();
        //int cantEli = BaseDeDatos.delete("historico");
        BaseDeDatos.delete("historico", null, null);
        close();
    }

}
