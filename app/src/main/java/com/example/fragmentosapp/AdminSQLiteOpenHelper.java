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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


//esta class administra la db
public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    private static AdminSQLiteOpenHelper instance;
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

    public void Registrar(ArrayList<DolarHistorico> cotizaciones) {
        open();
        for (int j=0; j<cotizaciones.size();j++) {
            ContentValues registro = new ContentValues();
            // registramos en la db los campos fecha,compra y venta
            registro.put("fecha", String.valueOf(cotizaciones.get(j).getDolarFecha()));
            registro.put("compra", cotizaciones.get(j).getDolarCompra());
            registro.put("venta", cotizaciones.get(j).getDolarVenta());
            // los insertamos en la "tabla"
            BaseDeDatos.insert("historico", null, registro);
        }
        close();
    }
    /*public void Registrar(ArrayList<DolarOficial> cotizaciones, Date datefechaSelecdb) {
        String fecha="", compra="", venta="", fechaAux,fechaMenosDias, fechaRepetida="";
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

                    if (fecha.equals(fechaRepetida)){
                        cantDias--;
                    }

                    compra = String.valueOf(cotizaciones.get(j).getDolarCompra());
                    venta = String.valueOf(cotizaciones.get(j).getDolarVenta());
                } else {
                    if (datefechaEndpoint.equals(dateFechaMenosDias)) { //si la fecha del endpoint es igual a la fecha anterior de la seleccionada
                        fecha = String.valueOf(cotizaciones.get(j).getDolarFecha());
                        if (fecha.equals(fechaRepetida)){
                            cantDias--;
                        }
                        compra = String.valueOf(cotizaciones.get(j).getDolarCompra());
                        venta = String.valueOf(cotizaciones.get(j).getDolarVenta());
                    } else {
                        if (fechaAux.equals(fechaRepetida)){
                            cantDias=cantDias-2;
                            fecha = fechaRepetida;
                        }
                        else{
                        fecha = fechaMenosDias;
                        }
                        compra = "No hay valores registrados.";
                        venta = "No hay valores registrados.";
                        j = j - 1;
                    }
                }
                cantDias++; //cant dias es el numero de dias que se resta a la fecha seleccionada por el usuario
                fechaRepetida = fecha;
                ContentValues registro = new ContentValues();
                registro.put("fecha", fecha);
                registro.put("compra", compra);
                registro.put("venta", venta);
                //BaseDeDatos.execSQL("INSERT OR REPLACE ");
                BaseDeDatos.insert("historico", null, registro);
            }
        close();
    }*/
    public ArrayList<DolarHistorico> Buscar() {
        LocalDate dateFecha,dateFechaDB;
        String fecha;
        String compra;
        String venta;

        ArrayList<DolarHistorico> historialCotizaciones = new ArrayList();

        open();

        Cursor fila = BaseDeDatos.rawQuery("SELECT fecha, compra, venta FROM historico", null);
        //Cursor fila = BaseDeDatos.rawQuery("SELECT compra, venta FROM historico WHERE fecha='" + db_fecha_cal + "'", null);
        DolarHistorico dolar;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (fila.moveToFirst()) { //retorna true si encuentra datos dentro de la "tabla" y los muestra en pantalla
            dateFechaDB = LocalDate.parse((fila.getString(0)), formatter);
            do {
                //if (dateFechaDB.equals(datefechaSelec)) {
                fecha = (fila.getString(0));
                dateFecha = LocalDate.parse(fecha, formatter);
                compra = (fila.getString(1));
                venta = (fila.getString(2));

                dolar = new DolarHistorico(dateFecha,compra,venta);
                historialCotizaciones.add(dolar);
                //}
            } while (fila.moveToNext());
        }
        close();
        return historialCotizaciones;
    }

    //metodo para elimiar
    public void Eliminar() {
        open();
        //int cantEli = BaseDeDatos.delete("historico");
        BaseDeDatos.delete("historico", null, null);
        close();
    }

}
