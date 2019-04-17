package com.metinsaritas.phocom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.metinsaritas.phocom.Types.Wifi;

public class ConnectedActivity extends AppCompatActivity {

    private Wifi wifi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);

        String wifiJson = getIntent().getStringExtra(MainConnectFragment.WIFI_JSON);
        wifi = new Gson().fromJson(wifiJson, Wifi.class);
        RecorderThread recorderThread = new RecorderThread(wifi.getIp());
        recorderThread.start();
    }
}
