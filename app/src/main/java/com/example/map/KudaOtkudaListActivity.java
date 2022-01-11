package com.example.map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class KudaOtkudaListActivity extends AppCompatActivity implements View.OnTouchListener {


    EditText otkuda_edit,kuda_edit;
    ImageView otkuda_off,otkuda_on,kuda_off,kuda_on;
    LinearLayout taxometr;
    TextView list_title;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kuda_otkuda_list);
        TextView[] search_adresses = {
                findViewById(R.id.search_adress1),findViewById(R.id.search_adress2),findViewById(R.id.search_adress3),findViewById(R.id.search_adress4),
                findViewById(R.id.search_adress5),findViewById(R.id.search_adress6),findViewById(R.id.search_adress7),findViewById(R.id.search_adress8),
                findViewById(R.id.search_adress9),findViewById(R.id.search_adress10),findViewById(R.id.search_adress11),findViewById(R.id.search_adress12),
                findViewById(R.id.search_adress13),findViewById(R.id.search_adress14),findViewById(R.id.search_adress15),findViewById(R.id.search_adress16),
                findViewById(R.id.search_adress17),findViewById(R.id.search_adress18),findViewById(R.id.search_adress19),findViewById(R.id.search_adress20)
        };
        TextView[] search_regions = {
                findViewById(R.id.search_region1),findViewById(R.id.search_region2),findViewById(R.id.search_region3),findViewById(R.id.search_region4),
                findViewById(R.id.search_region5),findViewById(R.id.search_region6),findViewById(R.id.search_region7),findViewById(R.id.search_region8),
                findViewById(R.id.search_region9),findViewById(R.id.search_region10),findViewById(R.id.search_region11),findViewById(R.id.search_region12),
                findViewById(R.id.search_region13),findViewById(R.id.search_region14),findViewById(R.id.search_region15),findViewById(R.id.search_region16),
                findViewById(R.id.search_region17),findViewById(R.id.search_region18),findViewById(R.id.search_region19),findViewById(R.id.search_region20),
        };
        LinearLayout[] search_cards = {
                findViewById(R.id.card1),findViewById(R.id.card2),findViewById(R.id.card3),findViewById(R.id.card4),
                findViewById(R.id.card5),findViewById(R.id.card6),findViewById(R.id.card7),findViewById(R.id.card8),
                findViewById(R.id.card9),findViewById(R.id.card10),findViewById(R.id.card11),findViewById(R.id.card12),
                findViewById(R.id.card13),findViewById(R.id.card14),findViewById(R.id.card15),findViewById(R.id.card16),
                findViewById(R.id.card17),findViewById(R.id.card18),findViewById(R.id.card19),findViewById(R.id.card20)
        };

        taxometr=findViewById(R.id.taxometr);
        otkuda_off=findViewById(R.id.otkuda_off);
        otkuda_on=findViewById(R.id.otkuda_on);
        kuda_off=findViewById(R.id.kuda_off);
        kuda_on=findViewById(R.id.kuda_on);
        list_title=findViewById(R.id.kuda_otkuda_title);

        otkuda_edit = findViewById(R.id.otkuda_edit);
        if(!my.otkuda.equalsIgnoreCase(""))
            otkuda_edit.setText(my.otkuda);
        kuda_edit = findViewById(R.id.kuda_edit);
        if(!my.kuda.equalsIgnoreCase(""))
            kuda_edit.setText(my.kuda);
        if(!my.is_edit_kuda){
            kuda_edit.setFocusableInTouchMode(true);
            kuda_edit.requestFocus();
        }
        else {
            otkuda_edit.setFocusableInTouchMode(true);
            otkuda_edit.requestFocus();
        }

        otkuda_edit.setOnTouchListener(this);
        kuda_edit.setOnTouchListener(this);
        TextWatcher textwatcher = new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String text = s.toString();
                            Thread.sleep(600);

                            if (text.equals(s.toString())) {
                                text = server.mConnect.sendData(("search|" + text).getBytes(), true);
                                if (!text.equals("not found")) {
                                    String[] split = text.split(";");
                                    runOnUiThread(new Runnable() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        public void run() {
                                            for (int i = 0; i < split.length; i++) {
                                                String[] spp = split[i].split(":");
                                                System.out.println(split[i]);
                                                search_adresses[i].setText(spp[2]);
                                                search_regions[i].setText(spp[1]);
                                                search_cards[i].setVisibility(View.VISIBLE);
                                                search_cards[i].setTooltipText(spp[2]+":"+spp[3]+":"+spp[4]);

                                            }
                                            for (int i = split.length; i < 20; i++) {
                                                if (search_cards[i].getVisibility() == View.GONE)
                                                    break;
                                                search_cards[i].setVisibility(View.GONE);
                                            }

                                        }
                                    });
                                }

                            }
                        } catch (Exception e) {
                            Log.e(connection.LOG_TAG, e.getMessage());
                        }
                    }
                });
                if (!s.toString().equals(""))
                    thread.start();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        };
        otkuda_edit.addTextChangedListener(textwatcher);
        kuda_edit.addTextChangedListener(textwatcher);

    }

    public void exitlist(View view) {
        if(my.otkuda_marker!=null)
            my.otkuda_marker.remove();
        if(my.kuda_marker!=null)
            my.kuda_marker.remove();
        finish();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v==kuda_edit){
            list_title.setText("Куда");
            taxometr.setVisibility(View.VISIBLE);
            kuda_off.setVisibility(View.GONE);
            kuda_on.setVisibility(View.VISIBLE);

            otkuda_off.setVisibility(View.VISIBLE);
            otkuda_on.setVisibility(View.GONE);
        }

        else if(v==otkuda_edit){
            list_title.setText("Откуда");
            taxometr.setVisibility(View.GONE);
            otkuda_off.setVisibility(View.GONE);
            otkuda_on.setVisibility(View.VISIBLE);
            kuda_off.setVisibility(View.VISIBLE);
            kuda_on.setVisibility(View.GONE);
        }
        return false;
    }

    public void kuda_map(View view) {
        if(my.kuda_marker!=null)
        {
            my.kuda_marker.remove();
            my.kuda_marker=null;
        }

        MapsActivity.thisactivity.findViewById(R.id.menu2).setVisibility(View.GONE);
        MapsActivity.thisactivity.findViewById(R.id.accept).setVisibility(View.VISIBLE);
        my.camera_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        finish();
    }

    public void otkuda_map(View view) {
        if(my.otkuda_marker!=null){
            my.otkuda_marker.remove();
            my.otkuda_marker=null;
        }
        my.camera_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));


        finish();
    }

    public void kuda_taxometr_OnClick(View view) {
        kuda_edit.setText("Скажу водителю");
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void adress_to_edit(View view) {
        String[] split = view.getTooltipText().toString().split(":");
        if(list_title.getText().equals("Откуда")){
            otkuda_edit.setText(split[0]);
            if(my.otkuda_marker!=null) {
                my.otkuda_marker.remove();
                my.otkuda_marker=null;
            }
            my.otkuda_marker = MapsActivity.mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(split[1]),Double.parseDouble(split[2]))).title(split[0]).snippet("")
                    .icon(MapsActivity.start_marker));
            my.otkuda_marker.showInfoWindow();
            my.otkuda=split[0];
        }
        else
        {
            kuda_edit.setText(split[0]);
            if(my.kuda_marker!=null) {
                my.kuda_marker.remove();
                my.kuda_marker=null;
            }
                my.kuda_marker = MapsActivity.mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(split[1]),Double.parseDouble(split[2]))).title(split[0]).snippet("")
                        .icon(MapsActivity.stop_marker));
                my.kuda_marker.showInfoWindow();
                my.kuda=split[0];
        }

    }

    public void ready_OnClick(View view) {
        if(my.otkuda_marker!=null && my.kuda_marker!=null){
            new FetchURL(MapsActivity.thisactivity).execute(MapsActivity.getUrl(my.otkuda_marker.getPosition(), my.kuda_marker.getPosition(), "driving"), "driving");

            if(my.camera_marker!=null){
                my.camera_marker.remove();
                my.camera_marker=null;
            }


            MapsActivity.menu2.setVisibility(View.GONE);
            MapsActivity.otkuda_view.setText(my.otkuda);
            MapsActivity.kuda_view.setText(my.kuda);
            MapsActivity.menu.setVisibility(View.VISIBLE);
            finish();
        }
    }
}