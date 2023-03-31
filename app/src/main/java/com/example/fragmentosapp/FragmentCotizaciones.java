package com.example.fragmentosapp;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FragmentCotizaciones extends Fragment {

    View rootView;

    static boolean arrayCheck[];
    private static RequestQueue queue;
    RecyclerView recyclerView;
    List<Monedas> monedasList;
    MonedasAdapter adapter;
    boolean[] opcion;
    String fechaHoy;


    public FragmentCotizaciones() {
        // Required empty public constructor
    }

    public static FragmentCotizaciones newInstance(boolean[] opcion) {
        FragmentCotizaciones fragment = new FragmentCotizaciones();
        Bundle args = new Bundle();
        args.putBooleanArray("opcion", opcion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            opcion = getArguments().getBooleanArray("opcion");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cotizaciones, container, false);


        recyclerView = rootView.findViewById(R.id.recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        monedasList = new ArrayList<>();

        adapter = new MonedasAdapter(getActivity(),monedasList);

        recyclerView.setAdapter(adapter);

        Calendar calendario = Calendar.getInstance();
        DateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        fechaHoy = (formateador.format(calendario.getTime()));

        ObtenerDatosVolleyMonedas(opcion);

        return rootView;
    }

    public void ObtenerDatosVolleyMonedas(boolean[] checks){
        queue = Volley.newRequestQueue(getActivity());
        //endpoint que devuelve un JSON Array con objetos dentro
        String url = "https://www.dolarsi.com/api/api.php?type=valoresprincipales";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                monedasList = new ArrayList<>();

                for (int i=0;i<checks.length;i++){
                    if (checks[i]){
                        JSONObject mJsonObject =  response.getJSONObject(i);
                        JSONObject mJsonObject2 =  mJsonObject.getJSONObject("casa");
                        String nom_moneda = mJsonObject2.getString("nombre");
                        String pre_compra = mJsonObject2.getString("compra");
                        String pre_venta = mJsonObject2.getString("venta");
                        String fecha = fechaHoy;
                        Monedas moneda = new Monedas(nom_moneda,pre_compra,pre_venta,fecha);
                        monedasList.add(moneda);
                    }
                }
                MainActivity.monedasList = monedasList;
                adapter = new MonedasAdapter(getActivity(),monedasList);
                recyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });
        queue.add(request);
    }
}