package com.metinsaritas.phocom;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

class SignalThread extends Thread {

    private WifiManager mWifiManager;
    private final int level = 100;
    private MainCreateFragment mMainCreateFragment;
    SignalThread (MainCreateFragment mainCreateFragment) {
        mMainCreateFragment = mainCreateFragment;
        mWifiManager = (WifiManager) mainCreateFragment.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public void run() {
        final LinearLayout fmcLlSignal = mMainCreateFragment.getSignalView();
        final TextView fmcTxtStatus = mMainCreateFragment.getStatusView();
        final int[] colorSignals = mMainCreateFragment.getResources().getIntArray(R.array.colorSignals);

        int signalLevel = 0;

        while (true) {
           try {
                WifiInfo wi = mWifiManager.getConnectionInfo();
                signalLevel = WifiManager.calculateSignalLevel(wi.getRssi(), level) / 16;
                Log.d("SignalLevel", signalLevel+"");
                sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
                signalLevel = 0;
            }

            final int finalSignalLevel = signalLevel;
            mMainCreateFragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String ip = Utils.getLocalIpAddress(mMainCreateFragment.getActivity());
                    if (ip.equals(Utils.TETHERING_IP)) {
                        ip = "Server";
                        fmcLlSignal.setBackgroundColor(colorSignals[5]);
                    }
                    else
                        fmcLlSignal.setBackgroundColor(colorSignals[finalSignalLevel]);

                    fmcTxtStatus.setText(ip);
                }
            });
        }
    }
}
