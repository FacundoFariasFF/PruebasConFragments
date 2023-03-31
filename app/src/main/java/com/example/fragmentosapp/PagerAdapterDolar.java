package com.example.fragmentosapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

class PagerAdapterDolar extends FragmentStateAdapter{
    static ArrayList<DolarHistorico> dolarHistoricos;

    public PagerAdapterDolar(FragmentActivity fa, ArrayList<DolarHistorico> historicos) {
        super(fa);
        dolarHistoricos = historicos;
    }

    @Override
    public Fragment createFragment(int position) {
        return FragmentDolar.newInstance(dolarHistoricos.get(position));
    }

    @Override
    public int getItemCount() {
            return dolarHistoricos.size();
    }
}