package com.metinsaritas.phocom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.metinsaritas.phocom.Types.Wifi;

import java.util.Timer;
import java.util.TimerTask;

public class ConnectNetworkActivity extends AppCompatActivity {

    private Wifi wifi;
    private String wifiJson = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_network);

        wifiJson = getIntent().getStringExtra(MainConnectFragment.WIFI_JSON);
        wifi = new Gson().fromJson(wifiJson, Wifi.class);

        //Toast.makeText(this, wifi.getSsid() + " " +wifi.getPassword(), Toast.LENGTH_SHORT).show();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        final WifiReceiver receiver = new WifiReceiver();
        registerReceiver(receiver, intentFilter);
        //connectWifi(); TODO: don't forget this

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            unregisterReceiver(receiver);
                        } catch (IllegalArgumentException e) {
                            Log.d("Err", e.getMessage());
                        }

                        if (!connected) {
                            Toast.makeText(ConnectNetworkActivity.this, "Couldn't connect to the network", Toast.LENGTH_SHORT).show();
                            ConnectNetworkActivity.this.finish();
                        }
                    }
                });
            }
        }, 3000);
    }

    private boolean connected = false;

    private void connectWifi () {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = "\""+ wifi.getSsid() +"\"";
        //wifiConfiguration.preSharedKey = "\""+ wifi.getPasswordd() +"\"";
        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
        int netId = wifiManager.addNetwork(wifiConfiguration);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }

    private void connectedActivity () {
        Intent intent = new Intent(this, ConnectedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(MainConnectFragment.WIFI_JSON, wifiJson);
        startActivity(intent);
        finish();
    }

    public class WifiReceiver extends BroadcastReceiver {

        private final String LOG_TAG = WifiReceiver.class.getSimpleName();
        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(info != null && info.isConnected()) {
                WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ssid = wifiInfo.getSSID();
                if (ssid.equals("<unknown ssid>")) return;

                if (ssid.equals("\"".concat(wifi.getSsid()).concat("\""))) {
                    Log.d(LOG_TAG, ssid+ " and "+ wifi.getSsid());
                    connected = true;
                    connectedActivity();
                } else {
                    Toast.makeText(context, "Info: "+ssid, Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, ssid);
                }
            }
        }
    }
}
