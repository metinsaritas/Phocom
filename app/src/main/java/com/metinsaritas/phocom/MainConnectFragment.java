package com.metinsaritas.phocom;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.metinsaritas.phocom.Types.FragmentWithTitle;
import com.metinsaritas.phocom.Types.Wifi;

import java.io.IOException;

public class MainConnectFragment extends FragmentWithTitle implements SurfaceHolder.Callback, Detector.Processor<Barcode> {

    private static final String LOG_TAG = MainConnectFragment.class.getSimpleName();
    public static final String WIFI_JSON = "wifi_json";

    final int RequestCameraPermissionID = 1001;
    private CameraSource cameraSource;
    private TextView fmcTvMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_connect, container, false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        SurfaceView fmcSvQrCamera = v.findViewById(R.id.fmcSvQrCamera);
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        barcodeDetector.setProcessor(this);
        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(width, height)
                .build();

        fmcTvMessage = v.findViewById(R.id.fmcTvMessage);
        fmcSvQrCamera.getHolder().addCallback(this);
        return v;
    }

    /* SurfaceHolder.Callback implementation */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA
            }, RequestCameraPermissionID);
            return;
        }

        try {
            cameraSource.start(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (cameraSource != null)
            cameraSource.stop();
    }

    /* Detector.Processor<Barcode> implementation */
    @Override
    public void release() {

    }

    private boolean foundQR = false;
    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections) {
        SparseArray<Barcode> qrcodes = detections.getDetectedItems();
        if (qrcodes.size() <= 0) return;

        String result = qrcodes.valueAt(0).rawValue;
        Wifi wifi = new Gson().fromJson(result, Wifi.class);
        if (!wifi.verify()) {
            fmcTvMessage.post(new Runnable() {
                @Override
                public void run() {
                    fmcTvMessage.setText("The QRCode is invalid");
                }
            });
            return;
        }

        if (foundQR) return;
        foundQR = true;

        Intent intent = new Intent(getContext(), ConnectNetworkActivity.class);
        intent.putExtra(WIFI_JSON, result);
        startActivity(intent);
        Log.d(LOG_TAG, "Test");
    }

    @Override
    public void onResume() {
        super.onResume();

        foundQR = false;
    }
}
