package com.metinsaritas.phocom;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;


public class PlayerThread extends Thread {

    private static String LOG_TAG = PlayerThread.class.getSimpleName();

    private final static int TRACK_MIN_BUFF = AudioTrack.getMinBufferSize(RecorderThread.SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_CONFIGURATION_MONO, RecorderThread.AUDIO_FORMAT);

    private String localIPAddress;
    private Boolean isServer = false;
    public  PlayerThread (String localIPAddress) {
        this.localIPAddress = localIPAddress;
        if (localIPAddress.equals("192.168.43.255")) {
            isServer = true;
        }
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);

        //AudioManager.STREAM_MUSIC
        final AudioTrack player = new AudioTrack(AudioManager.STREAM_MUSIC, RecorderThread.SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_CONFIGURATION_MONO, RecorderThread.AUDIO_FORMAT, TRACK_MIN_BUFF, AudioTrack.MODE_STREAM);

        player.play();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                 if (player != null) {
                     player.flush();
                 }
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 10000);

        try {
            DatagramSocket socket = new DatagramSocket(12345);
            socket.setBroadcast(true);
            while (true) {
                byte[] buffer = new byte[RecorderThread.RECORD_MIN_BUFF];

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                InetAddress address = packet.getAddress();

                if (isServer) {
                    if (address.getHostAddress().equals("192.168.43.1")) {
                        continue;
                    }
                }

                if (address.getHostAddress().equals(localIPAddress)) {
                    continue;
                }

                player.write(buffer, 0, RecorderThread.RECORD_MIN_BUFF);


                //String log = "Recieved a packet: ("+packet.getAddress()+":"+packet.getPort()+")";
                //Log.d(LOG_TAG, log);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
