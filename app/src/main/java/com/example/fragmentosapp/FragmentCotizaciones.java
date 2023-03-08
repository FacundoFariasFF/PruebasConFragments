package com.example.fragmentosapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentCotizaciones extends Fragment {

    View rootView;
    private RequestQueue queue;
    RecyclerView recyclerView;
    List<Monedas> monedasList;
    MonedasAdapter adapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public FragmentCotizaciones() {
        // Required empty public constructor
    }

    public static FragmentCotizaciones newInstance(String param1, String param2) {
        FragmentCotizaciones fragment = new FragmentCotizaciones();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
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

        queue = Volley.newRequestQueue(getActivity());
        ObtenerDatosVolleyMonedas();


        return rootView;
    }


    public void ObtenerDatosVolleyMonedas(){
        //endpoint que devuelve un JSON Array con objetos dentro
        String url = "https://www.dolarsi.com/api/api.php?type=valoresprincipales";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                monedasList = new ArrayList<>();
                for (int i=0;i<response.length()-2;i++){
                    JSONObject mJsonObject =  response.getJSONObject(i);
                    JSONObject mJsonObject2 =  mJsonObject.getJSONObject("casa");
                    String nom_moneda = mJsonObject2.getString("nombre");
                    String pre_compra = mJsonObject2.getString("compra");
                    String pre_venta = mJsonObject2.getString("venta");
                    String val_variacion = mJsonObject2.has("variacion") ? mJsonObject2.getString("variacion") : "";

                    Monedas moneda = new Monedas(nom_moneda,pre_compra,pre_venta,val_variacion);
                    monedasList.add(moneda);
                }
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