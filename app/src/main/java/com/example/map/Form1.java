package com.example.map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

public class Form1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form1);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.connect(true);

                    String message = server.send("class_avto|"+my.RAYON,true,true);
                    String[] split=message.split(";");
                    for (String s:split) {
                        String[] sp = s.split(":");
                        if(sp[0].equalsIgnoreCase("Эконом")){
                            my.tarif_Econom_minsum = sp[1];
                            my.tarif_Econom_km_summ = sp[2];
                            my.tarif_Econom_km2_summ = sp[3];
                            my.tarif_Econom_kmout_summ = sp[4];
                            my.tarif_Econom_wait_summ = sp[5];
                            my.tarif_Econom_freewait = sp[6];
                        }
                        else if(sp[0].equalsIgnoreCase("Комфорт")){
                            my.tarif_Comfort_minsum = sp[1];
                            my.tarif_Comfort_km_summ = sp[2];
                            my.tarif_Comfort_km2_summ = sp[3];
                            my.tarif_Comfort_kmout_summ = sp[4];
                            my.tarif_Comfort_wait_summ = sp[5];
                            my.tarif_Comfort_freewait = sp[6];
                        }
                        else if(sp[0].equalsIgnoreCase("Комфорт+")){
                            my.tarif_Comfortp_minsum = sp[1];
                            my.tarif_Comfortp_km_summ = sp[2];
                            my.tarif_Comfortp_km2_summ = sp[3];
                            my.tarif_Comfortp_kmout_summ = sp[4];
                            my.tarif_Comfortp_wait_summ = sp[5];
                            my.tarif_Comfortp_freewait = sp[6];
                        }
                        else if(sp[0].equalsIgnoreCase("Бизнес")){
                            my.tarif_Business_minsum = sp[1];
                            my.tarif_Business_km_summ = sp[2];
                            my.tarif_Business_km2_summ = sp[3];
                            my.tarif_Business_kmout_summ = sp[4];
                            my.tarif_Business_wait_summ = sp[5];
                            my.tarif_Business_freewait = sp[6];
                        }
                    }
                    Thread.sleep(1000);
                    Intent intent = new Intent(Form1.this, Form2.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Log.e(connection.LOG_TAG, e.getMessage());
                }
            }
        });
        /*
        thread.start();
         */
        Intent intent = new Intent(Form1.this, voicechat.class);
        startActivity(intent);
        finish();

    }

}