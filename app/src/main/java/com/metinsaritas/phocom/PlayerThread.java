package com.metinsaritas.phocom;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class PlayerThread extends Thread {

    private static String LOG_TAG = PlayerThread.class.getSimpleName();

    private final static int TRACK_MIN_BUFF = AudioTrack.getMinBufferSize(RecorderThread.SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_CONFIGURATION_MONO, RecorderThread.AUDIO_FORMAT);

    private AudioTrack player;

    private String localIPAddress;
    public  PlayerThread (String localIPAddress) {
        this.localIPAddress = localIPAddress;
    }

    @Override
    public void run() {
        player = new AudioTrack(AudioManager.STREAM_MUSIC, RecorderThread.SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_CONFIGURATION_MONO, RecorderThread.AUDIO_FORMAT, TRACK_MIN_BUFF, AudioTrack.MODE_STREAM);
        player.play();

        try {
            DatagramSocket socket = new DatagramSocket(12345);
            socket.setBroadcast(true);
            while (true) {
                byte[] buffer = new byte[RecorderThread.RECORD_MIN_BUFF];

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                InetAddress address = packet.getAddress();

                if (address.getHostAddress().equals(localIPAddress)) {
                    continue;
                }

                player.write(buffer, 0, RecorderThread.RECORD_MIN_BUFF);


                String log = "Recieved a packet: ("+packet.getAddress()+":"+packet.getPort()+")";
                Log.d(LOG_TAG, log);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
