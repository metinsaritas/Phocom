package com.metinsaritas.phocom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Comminucation extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comminucation);

        RecorderThread recorderThread = new RecorderThread("255.255.255.255");
        recorderThread.start();

        PlayerThread playerThread = new PlayerThread(Utils.getLocalIpAddress(this));
        playerThread.start();
    }
}
