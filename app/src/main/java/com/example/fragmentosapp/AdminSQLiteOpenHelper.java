package com.example.fragmentosapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


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

    public void Registrar(String dbfecha, String dbcompra, String dbventa) {
        open();
        ContentValues registro = new ContentValues();
        // registramos en la db los campos fecha,compra y venta
        registro.put("fecha", dbfecha);
        registro.put("compra", dbcompra);
        registro.put("venta", dbventa);
        // los insertamos en la "tabla"
        BaseDeDatos.insert("historico", null, registro);
        close();
    }

    public void Buscar(String db_fecha_cal) {

        String fecha;
        String compra;
        String venta;
        //creamos una validacion para que no este el campo fecha vacio
        if (!db_fecha_cal.isEmpty()) {
            open();
            Cursor fila = BaseDeDatos.rawQuery("SELECT compra, venta FROM historico WHERE fecha='" + db_fecha_cal + "'", null);

            // lo siguiente retorna true si encuentra datos dentro de la "tabla" y los muestra en pantalla
            if (fila.moveToFirst()) {
                fecha = (db_fecha_cal);
                compra = (fila.getString(0));
                venta = (fila.getString(1));
            } else {
                fecha = (db_fecha_cal);
                compra = "No hay valores registrados" ;
                venta = "No hay valores registrados";
            }
            FragmentDolarOficial cotizacion = new FragmentDolarOficial();
            cotizacion.MostrarFecha(fecha, compra, venta);

            close();
        }
    }

}
