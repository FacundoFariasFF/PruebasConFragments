package com.example.fragmentosapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.lifecycle.Lifecycle;

import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import android.os.Bundle;


import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {
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
            public void onPageSelected(int position) {
                tabLayout1.selectTab(tabLayout1.getTabAt(position));
            }
        });
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
    private class AdaptadorFragment extends FragmentStateAdapter {


        public AdaptadorFragment(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0: return  new FragmentA();
                case 1: return  new FragmentB();
                default: return  new FragmentC();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}