package com.example.fragmentosapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class FragmentDolarOficial extends Fragment {

    private String nroFragment, fecha, compra, venta;
    private TextView tvNroFragment, tvDia;

    static TextView tv_fecha;
    static TextView tv_compra;
    static TextView tv_venta;

    //static String fecha, compra, venta;



    public FragmentDolarOficial() {
        // Required empty public constructor
    }

    public static FragmentDolarOficial newInstance(String nroFragment, String fecha, String compra, String venta){
        FragmentDolarOficial fragment = new FragmentDolarOficial();
        Bundle args = new Bundle();
        args.putString("nroFragment", nroFragment);
        args.putString("fecha", fecha);
        args.putString("compra", compra);
        args.putString("venta", venta);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            nroFragment = getArguments().getString("nroFragment");
            fecha = getArguments().getString("fecha");
            compra = getArguments().getString("compra");
            venta = getArguments().getString("venta");
        }

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dolar_oficial, container, false);


        tvNroFragment = rootView.findViewById(R.id.txt_fragmento_dolar_oficial);
        tvNroFragment.setText("El nro de dia es "+nroFragment);


        tv_fecha = rootView.findViewById(R.id.txt_fecha_oficial);
        tv_compra = rootView.findViewById(R.id.txt_compra_oficial);
        tv_venta = rootView.findViewById(R.id.txt_venta_ofical);

        fecha = fecha;
        compra=compra;
        venta=venta;

        if (fecha.equals("30/11/0002")||fecha.equals("29/11/0002")){
            tv_fecha.setText("Fecha: No selecciono una fecha");
        }else {
            tv_fecha.setText("Fecha: "+fecha);
        }
        tv_compra.setText("Precio de Compra: "+compra);
        tv_venta.setText("Precio de Venta: "+venta);


        return rootView;
    }
}