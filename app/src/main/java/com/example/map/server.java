package com.example.map;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class server {
    static boolean is_busy=false;
    static connection  mConnect  = null;
    public static void connect(boolean wait)
    {
        // Создание подключения
        mConnect = new connection("83.220.174.210", 7771);
        // Открытие сокета в отдельном потоке
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mConnect.openConnection();
                    // Разблокирование кнопок в UI потоке
                    Log.d(connection.LOG_TAG, "Соединение установлено");
                    Log.d(connection.LOG_TAG, "(mConnect != null) = " + (mConnect != null));
                } catch (Exception e) {
                    Log.e(connection.LOG_TAG, e.getMessage());
                    mConnect = null;
                }
            }
        });
                thread.start();
                if(wait){
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

    }
    public static String send(String text,boolean accept,boolean wait)
    {
        final String[] data = new String[1];
        data[0]="";
        if (mConnect == null) {
            Log.d(connection.LOG_TAG, "Соединение не установлено");
            server.connect(true);
        }  else {
            Log.d(connection.LOG_TAG, "Отправка сообщения");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                         data[0] = mConnect.sendData(text.getBytes(),accept);

                    } catch (Exception e) {
                        Log.e(connection.LOG_TAG, e.getMessage());
                    }
                }
            });
            thread.start();
            if(wait){
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
        return data[0];
    }

    public static void close()
    {
        // Закрытие соединения
        mConnect.closeConnection();
        Log.d(connection.LOG_TAG, "Соединение закрыто");
    }
}
