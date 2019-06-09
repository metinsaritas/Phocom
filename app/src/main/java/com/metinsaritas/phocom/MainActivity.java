package com.metinsaritas.phocom;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        checkRecordPermission();

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

    private void checkRecordPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    123);
        }
    }


}
