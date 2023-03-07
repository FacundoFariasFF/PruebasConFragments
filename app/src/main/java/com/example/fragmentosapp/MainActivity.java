package com.example.fragmentosapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.lifecycle.Lifecycle;

import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity{
    private TabLayout tabLayout1;
    private ViewPager2 viewPager2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout1 = findViewById(R.id.tablayout1);
        viewPager2 = findViewById(R.id.viewpager2);

        viewPager2.setAdapter(new AdaptadorFragment(getSupportFragmentManager(),getLifecycle()));

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            //movimiento del tab
            public void onPageSelected(int position) {
               tabLayout1.selectTab(tabLayout1.getTabAt(position));
            }
        });
        // para que con el tab se cambie de fragments
       tabLayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    ////
    private class AdaptadorFragment extends FragmentStateAdapter {

        //constructor de fragments
        public AdaptadorFragment(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {

            switch (position){
                case 8: return FragmentDolarOficial.newInstance("6");
                case 7: return FragmentDolarOficial.newInstance("5");
                case 6: return FragmentDolarOficial.newInstance("4");
                case 5: return FragmentDolarOficial.newInstance("3");
                case 4: return FragmentDolarOficial.newInstance("2");
                case 3: return FragmentDolarOficial.newInstance("1");
                case 2: return FragmentDolarOficial.newInstance("0");
                case 1: return  new FragmentCotizaciones();
                case 0: return  new FragmentWhatsapp();
                default: return  new FragmentDolarOficial();
            }
        }
        @Override
        public int getItemCount() {
            return 9;
        }
    }

    //// menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.menu_back:
                Toast.makeText(MainActivity.this,"Retroceder",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_calendario:
                Toast.makeText(MainActivity.this,"abrir calendario",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_cotizaciones:
                viewPager2.setCurrentItem(1);
                Toast.makeText(MainActivity.this,"abrir cotizaciones",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_whatsapp:
                viewPager2.setCurrentItem(0);
                Toast.makeText(MainActivity.this,"abrir whatsapp",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }
    ///
}