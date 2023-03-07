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
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FragmentDolarOficial extends Fragment {


    private String nroDiaFragment;
    private TextView tvNroFragment;
    private TextView tvDia;

    String [] dias = {"lunes","martes","miercoles", "jueves","viernes", "sabado", "domingo"};
    List<String> list = Arrays.asList(dias);



    public FragmentDolarOficial() {
        // Required empty public constructor
    }

    public static FragmentDolarOficial newInstance(String nroFragment){
        FragmentDolarOficial fragment = new FragmentDolarOficial();
        Bundle args = new Bundle();
        args.putString("nroFragment", nroFragment);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            nroDiaFragment = getArguments().getString("nroFragment");
        }

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dolar_oficial, container, false);

        tvNroFragment = rootView.findViewById(R.id.txt_fragmento_dolar_oficial);
        tvNroFragment.setText("El nro de dia es "+nroDiaFragment);


        tvDia = rootView.findViewById(R.id.txt_fragmento_dia);
        tvDia.setText(String.valueOf(list.get(Integer.parseInt(nroDiaFragment))));

        //String resultados = String.valueOf(list.get(6));
       // txt_fragmentDolarOficial.setText(resultados);



        return rootView;
    }


}