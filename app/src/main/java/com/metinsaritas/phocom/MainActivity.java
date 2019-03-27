package com.metinsaritas.phocom;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        setReferences();
        setEvents();
    }

    private TabLayout amTabLayout;
    private ViewPager amViewPager;

    private void setReferences() {
        amTabLayout = findViewById(R.id.amTabLayout);
        amViewPager = findViewById(R.id.amViewPager);
    }

    private void setEvents() {
        amViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        amTabLayout.setupWithViewPager(amViewPager);
    }


}
