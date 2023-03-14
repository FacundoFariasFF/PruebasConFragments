package com.example.fragmentosapp;

import static com.example.fragmentosapp.MainActivity.historicos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


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

    //public void Buscar(String db_fecha_cal) {
    public void Buscar() {

        String fecha;
        String compra;
        String venta;
        //creamos una validacion para que no este el campo fecha vacio
        open();
        Cursor fila = BaseDeDatos.rawQuery("SELECT fecha, compra, venta FROM historico", null);

        DolarOficial dolar;

        // lo siguiente retorna true si encuentra datos dentro de la "tabla" y los muestra en pantalla
        if (fila.moveToFirst()) {
            do{
                fecha = (fila.getString(0));
                compra = (fila.getString(1));
                venta = (fila.getString(2));
                dolar = new DolarOficial(fecha,compra,venta);
                MainActivity.historicos.add(dolar);
            }while (fila.moveToNext());
        } else {
            //fecha = (db_fecha_cal);
            compra = "No hay valores registrados" ;
            venta = "No hay valores registrados";
        }
        close();
    }

}
