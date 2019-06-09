package com.metinsaritas.phocom;

import android.app.Activity;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Utils {
    private static final String LOG_TAG = Utils.class.getSimpleName();
    public static String TETHERING_IP = "192.168.43.255"; // Static tethering ip, same on every devices

    public static String getLocalIpAddress(Activity activity) {
        /*TODO: x*/
        WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(
                Context.WIFI_SERVICE);
        WifiInfo wifiinfo = wifiManager.getConnectionInfo();
        byte[] array = BigInteger.valueOf(wifiinfo.getIpAddress()).toByteArray();

        for(int i=0; i<array.length/2; i++){
            byte temp = array[i];
            array[i] = array[array.length -i -1];
            array[array.length -i -1] = temp;
        }

        InetAddress myInetIP = null;
        String myIP = TETHERING_IP;
        try {
            myInetIP = InetAddress.getByAddress(array);
            myIP = myInetIP.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return myIP;
    }

    public static String join (String delimeter, String[] arr) {
        String ret = "";
        for (String e:arr) {
            ret += delimeter + e;
        }
        return ret.substring(1);
    }

    //AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_CONFIGURATION_MONO, AUDIO_FORMAT);
    public static int getValidSampleRates() {
        int[] rates = new int[] {44100, 22050, 11025, 16000, 8000};
        for (int rate : rates) {
            int bufferSize = AudioRecord.getMinBufferSize(rate, AudioFormat.CHANNEL_CONFIGURATION_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
            if (bufferSize > 0) {
                return rate;
            }
        }
        return 44100;
    }
}
