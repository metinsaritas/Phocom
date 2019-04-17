package com.metinsaritas.phocom;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class RecorderThread extends Thread {

    private static String LOG_TAG = RecorderThread.class.getSimpleName();
    private static int port = 12345;

    public final static int SAMPLE_RATE_IN_HZ = 16000; // 44100 for music
    public final static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    public static int RECORD_MIN_BUFF = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_CONFIGURATION_MONO, AUDIO_FORMAT);

    private boolean status = true;
    private AudioRecord recorder;
    private InetAddress address;
    public RecorderThread (String destination) {
        try {
            this.address = InetAddress.getByName(destination);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);

            byte[] buffer = new byte[RECORD_MIN_BUFF];
            DatagramPacket packet;

            recorder = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_CONFIGURATION_MONO, AUDIO_FORMAT, RECORD_MIN_BUFF);
            recorder.startRecording();

            while (status == true) {

                RECORD_MIN_BUFF = recorder.read(buffer, 0, buffer.length);
                packet = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(packet);
            }

        } catch (UnknownHostException e) {
            Log.e(LOG_TAG, "UnknownHostException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "IOException");
        }
    }
}
