package com.metinsaritas.phocom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metinsaritas.phocom.Types.FragmentWithTitle;

public class MainCreateFragment extends FragmentWithTitle implements CompoundButton.OnCheckedChangeListener, View.OnTouchListener {

    private LinearLayout fmcLlSignal;
    private CheckBox fmcCbAuto;
    private TextView fmcTxtStatus;
    private Button fmcBtnPush;
    private String initialBtnText = "";

    public LinearLayout getSignalView() {
        return fmcLlSignal;
    }

    public TextView getStatusView() {
        return fmcTxtStatus;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_create, container, false);
        fmcLlSignal = view.findViewById(R.id.fmcLlSignal);
        fmcCbAuto = view.findViewById(R.id.fmcCbAuto);
        fmcTxtStatus = view.findViewById(R.id.fmcTxtStatus);
        fmcBtnPush = view.findViewById(R.id.fmcBtnPush);
        initialBtnText = fmcBtnPush.getText().toString();

        fmcCbAuto.setOnCheckedChangeListener(this);
        fmcBtnPush.setOnTouchListener(this);

        String localIpAddress = Utils.getLocalIpAddress(getActivity());

        String[] ipAddressOctet = localIpAddress.split("\\.");
        ipAddressOctet[3] = "255";

        String destination = Utils.join(".",ipAddressOctet);

        RecorderThread recorderThread = new RecorderThread(destination);
        recorderThread.start();

        PlayerThread playerThread = new PlayerThread(localIpAddress);
        playerThread.start();

        SignalThread signalThread = new SignalThread(this);
        signalThread.start();

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        fmcBtnPush.setEnabled(!isChecked);
        RecorderThread.canSendAudio = isChecked;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            RecorderThread.canSendAudio = true;
            fmcBtnPush.setText("Sending");
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            RecorderThread.canSendAudio = false;
            fmcBtnPush.setText(initialBtnText);
        }
        return true;
    }
}
