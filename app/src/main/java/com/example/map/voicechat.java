package com.example.map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static com.example.map.connection.LOG_TAG;

public class voicechat extends AppCompatActivity {
    int myBufferSize = 1400;
    AudioRecord audioRecord;

//test
    private boolean status = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voicechat);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1234);
        }
        else
            startStreaming();


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1234: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startStreaming();

                } else {
                    Log.d("TAG", "permission denied by user");
                }
                return;
            }
        }
    }
    public void startStreaming() {
        createAudioRecorder();
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(9987);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        DatagramSocket finalSocket = socket;


        AudioTrack audio = new AudioTrack(AudioManager.STREAM_VOICE_CALL, 8000, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT, 3000 /* 1 second buffer */,
                AudioTrack.MODE_STREAM);
        audio.play();

        Thread streamThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramPacket packet;
                    final InetAddress destination = InetAddress.getByName("83.220.174.210");
                    audioRecord.startRecording();
                    byte data[] = new byte[myBufferSize];
                    byte[] myBuffer = new byte[myBufferSize];
                    int readCount = 0;
                    while (status) {
                        readCount = audioRecord.read(myBuffer, 0, myBufferSize);
                        Log.d(TAG, "readCount = " + readCount);
                        packet = new DatagramPacket (myBuffer,myBuffer.length,destination,9987);
                        finalSocket.send(packet);
                        audio.write(myBuffer,0,myBuffer.length);
                    }


/*
                    byte[] buffer = new byte[1024];

                    recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRate,channelConfig,audioFormat,minBufSize*10);
                    recorder.startRecording();
                    byte[] bf = "test|".getBytes();
                    ByteArrayOutputStream output;
                    while(status) {
                        try {
                        minBufSize = recorder.read(buffer, 0, buffer.length);
                        output = new ByteArrayOutputStream();
                        output.write(bf);
                        output.write(buffer);
                        byte[] out = output.toByteArray();
                        packet = new DatagramPacket (out,out.length,destination,9987);
                        finalSocket.send(packet);

                        } catch(UnknownHostException e) {
                            Log.e("VS", "UnknownHostException");
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("VS", "IOException");
                        }
                    }
                    */
            } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }});
        streamThread.start();
        DatagramSocket finalSocket1 = socket;
        DatagramSocket finalSocket2 = socket;

        Thread recieveThread = new Thread(new Runnable() {
            @Override
            public void run() {

                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                byte [] noiseData = new byte[1000];
                Random rnd = new Random();


                    while (status) {
                        try {

                            byte[] message = new byte[1024];
                            DatagramPacket packet = new DatagramPacket(message,message.length);
                            Log.i("UDP client: ", "about to wait to receive");
                            finalSocket1.receive(packet);
                            //String text = new String(message, 0, packet.getLength());
                            String str = new String(packet.getData(), StandardCharsets.UTF_8).replaceAll("\u0000.*", "");

                            audio.write(str.getBytes(), 0, str.getBytes().length);

                        }catch (IOException e) {
                            //status = false;
                        }
                    }
                finalSocket2.disconnect();
                finalSocket2.close();
                audio.stop();
                audio.flush();
                audio.release();
            }
        });
        //recieveThread.start();

    }
    private void createAudioRecorder() {
        int sampleRate = 16000;
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

        int minInternalBufferSize = AudioRecord.getMinBufferSize(sampleRate,
                channelConfig, audioFormat);
        int internalBufferSize = minInternalBufferSize * 4;
        Log.d(TAG, "minInternalBufferSize = " + minInternalBufferSize
                + ", internalBufferSize = " + internalBufferSize
                + ", myBufferSize = " + myBufferSize);

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRate, channelConfig, audioFormat, internalBufferSize);
    }
}
