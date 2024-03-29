package com.example.fragmentosapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentDolar extends Fragment {

    View rootView;

    static TextView tv_fecha;
    static TextView tv_compra;
    static TextView tv_venta;
    private String fecha, compra, venta;


    public FragmentDolar() {
        // Required empty public constructor
    }

    //public static FragmentDolar newInstance(DolarOficial fecha) {
    public static FragmentDolar newInstance(DolarHistorico fecha) {
        FragmentDolar fragment = new FragmentDolar();
        Bundle args = new Bundle();
        args.putString("fecha", String.valueOf(fecha.getDolarFecha()));
        args.putString("compra", String.valueOf(fecha.getDolarCompra()));
        args.putString("venta", String.valueOf(fecha.getDolarVenta()));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fecha = getArguments().getString("fecha");
            compra = getArguments().getString("compra");
            venta = getArguments().getString("venta");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dolar, container, false);

        tv_fecha = rootView.findViewById(R.id.txt_fecha_oficial);
        tv_compra = rootView.findViewById(R.id.txt_compra_oficial);
        tv_venta = rootView.findViewById(R.id.txt_venta_ofical);


        MainActivity.fragmentActivo ="HistorialDolarOficial";
        MainActivity.compartirNombre= "Dolar Oficial";
        MainActivity.compartirFecha = fecha;
        MainActivity.compartirCompra = compra;
        MainActivity.compartirVenta = venta;

        //MainActivity.ItemMoneda(fecha);

        tv_fecha.setText("Fecha: "+fecha);
        tv_compra.setText("Precio de Compra: "+compra);
        tv_venta.setText("Precio de Venta: "+venta);

        return rootView;
    }

}