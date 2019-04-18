package com.metinsaritas.phocom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Comminucation extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comminucation);

        /* TODO: Earpiece and Headphone control
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.STREAM_MUSIC);
        audioManager.setSpeakerphoneOn(false);
        */
        String localIpAddress = Utils.getLocalIpAddress(this);

        String[] ipAddressOctet = localIpAddress.split("\\.");
        ipAddressOctet[3] = "255";

        String destination = Utils.join(".",ipAddressOctet);

        RecorderThread recorderThread = new RecorderThread(destination);
        recorderThread.start();

        PlayerThread playerThread = new PlayerThread(localIpAddress);
        playerThread.start();
    }
}
