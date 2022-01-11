package com.example.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener, TaskLoadedCallback {

    private Polyline currentPolyline;
    static LinearLayout menu;
    static ImageView menu2;
    ImageView cancel_all;
    Button accept_ok;
    LinearLayout main_menu;
    LinearLayout main_menu_help;
    static GoogleMap mMap;
    static MapsActivity thisactivity;

    static TextView otkuda_view,kuda_view;

    BitmapDescriptor gl_marker;
    BitmapDescriptor gl_marker_up;
    static BitmapDescriptor start_marker;
    static BitmapDescriptor stop_marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        main_menu = findViewById(R.id.main_menu);
        main_menu_help = findViewById(R.id.main_menu_help);
        menu = findViewById(R.id.menu);
        menu2 = findViewById(R.id.menu2);
        cancel_all=findViewById(R.id.cancel);
        otkuda_view = findViewById(R.id.otkuda_view);
        kuda_view = findViewById(R.id.kuda_view);
        accept_ok=findViewById(R.id.accept);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.gl_marker);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 84, false);
        gl_marker = BitmapDescriptorFactory.fromBitmap(smallMarker);

        @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapdraw2 = (BitmapDrawable)getResources().getDrawable(R.drawable.gl_marker_up);
        Bitmap b2 = bitmapdraw2.getBitmap();
        Bitmap smallMarker2 = Bitmap.createScaledBitmap(b2, 50, 112, false);
        gl_marker_up = BitmapDescriptorFactory.fromBitmap(smallMarker2);
        @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapdraw3 = (BitmapDrawable)getResources().getDrawable(R.drawable.yellow);
        Bitmap b3 = bitmapdraw3.getBitmap();
        Bitmap smallMarker3 = Bitmap.createScaledBitmap(b3, 50, 69, false);
        start_marker = BitmapDescriptorFactory.fromBitmap(smallMarker3);

        @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapdraw4 = (BitmapDrawable)getResources().getDrawable(R.drawable.green);
        Bitmap b4 = bitmapdraw4.getBitmap();
        Bitmap smallMarker4 = Bitmap.createScaledBitmap(b4, 50, 69, false);
        stop_marker = BitmapDescriptorFactory.fromBitmap(smallMarker4);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.setMinZoomPreference(15f);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10001);
        }else {
            mMap.setMyLocationEnabled(true);
        }
mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setBuildingsEnabled(true);
        my.camera_marker= mMap.addMarker(new MarkerOptions().position(my.rayon_latlng)
                .icon(gl_marker));
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(my.rayon_latlng));
        thisactivity = this;

       start_get_cars();
    }
    Marker m;
    LatLng latLng;
    String lls;
    String[] split,sp;
    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 5000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed /
                        duration);
                double lng = t * toPosition.longitude + (1 - t) *
                        startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t) *
                        startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }
void start_get_cars(){
    final double[] rotation = {0};
    @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.car);
    Bitmap b = bitmapdraw.getBitmap();
    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 35, 70, false);
    BitmapDescriptor c = BitmapDescriptorFactory.fromBitmap(smallMarker);

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {

                while (true){

                    lls= server.mConnect.sendData(("get_drivers|"+my.RAYON).getBytes(),true);

                    MapsActivity.thisactivity.runOnUiThread(new Runnable() {
                        public void run() {
                            split = lls.split(";");
                            for(int i=0;i<split.length-1;i++){
                                sp = split[i].split(":");
                                latLng = new LatLng(Double.parseDouble(sp[1]),Double.parseDouble(sp[2]));
                                m = my.cars_marker.get(Integer.parseInt(sp[0]));
                                if(m!=null)
                                {
                                    Location l1=new Location("l1");
                                    Location l2=new Location("l2");
                                    l1.setLatitude(m.getPosition().latitude);
                                    l1.setLongitude(m.getPosition().longitude);
                                    l2.setLatitude(latLng.latitude);
                                    l2.setLongitude(latLng.longitude);
                                    rotation[0] = Double.parseDouble(sp[3]);
                                    m.setRotation((float)rotation[0]);
                                    if(l1.distanceTo(l2)<50){
                                        animateMarker(m,latLng,false);
                                    }
                                }
                                else
                                {
                                    rotation[0] = Double.parseDouble(sp[3]);
                                    my.cars_marker.put(Integer.parseInt(sp[0]),MapsActivity.mMap.addMarker(new MarkerOptions().position(latLng).icon(c).rotation((float) rotation[0])));

                                }
                            } }});
                            Thread.sleep(5000);

                }

            } catch (Exception e) {
                Log.e(connection.LOG_TAG, e.getMessage());
            }

        }
    });
    thread.start();
}

    @Override
    public void onCameraMove() {
        if(my.camera_marker!=null){
            my.camera_marker.setPosition(mMap.getCameraPosition().target);
        }
    }

    @Override
    public void onCameraIdle() {
        if(my.camera_marker!=null){
            my.camera_marker.setIcon(gl_marker);
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(50);
            }
            LatLng latLng = my.camera_marker.getPosition();
            my.place = server.send("place|"+latLng.latitude+"|"+latLng.longitude,true,true);
            if(!my.place.equals("not found") && !my.place.equals("")){
                my.camera_marker.setTitle(my.place.split(":")[1]);
                my.camera_marker.showInfoWindow();
            }
            else{
                my.camera_marker.setTitle("");
                my.camera_marker.hideInfoWindow();
            }

        }
    }

    public void ok(View view) {
        cancel_all.setVisibility(View.VISIBLE);
        LatLng ll = my.camera_marker.getPosition();
        my.place = server.send("place|"+ll.latitude+"|"+ll.longitude,true,true);
        if(!my.place.equals("not found") && !my.place.equals("")){
            my.camera_marker.setTitle(my.place.split(":")[1]);
            my.camera_marker.showInfoWindow();
        }
        else
            return;

        if(my.otkuda_marker!=null){

            my.kuda_marker = mMap.addMarker(new MarkerOptions().position(ll).title(my.place.split(":")[1]).snippet("")
                    .icon(stop_marker));
            my.kuda_marker.showInfoWindow();
            my.kuda=my.place.split(":")[1];
        }
        else{
            my.otkuda_marker = mMap.addMarker(new MarkerOptions().position(ll).title(my.place.split(":")[1]).snippet("")
                    .icon(start_marker));
            my.otkuda_marker.showInfoWindow();
            my.otkuda=my.place.split(":")[1];
        }
        if(my.otkuda_marker!=null && my.kuda_marker!=null){
            new FetchURL(MapsActivity.this).execute(getUrl(my.otkuda_marker.getPosition(), my.kuda_marker.getPosition(), "driving"), "driving");

            if(my.camera_marker!=null){
                my.camera_marker.remove();
                my.camera_marker=null;
            }


            view.setVisibility(View.GONE);
            otkuda_view.setText(my.otkuda);
            kuda_view.setText(my.kuda);
            menu.setVisibility(View.VISIBLE);

        }
        else{
            Intent intent = new Intent(MapsActivity.this, KudaOtkudaListActivity.class);
            startActivity(intent);
        }
    }
    static String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyBg7y7QGcrn2D4Q-D5hZfRJ7GOztM3cplQ";
        return url;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        currentPolyline.setWidth(4);

        double km=0;
        List<LatLng> ll_list = currentPolyline.getPoints();
        for (int i=0;i<ll_list.size()-1;i++) {
            Location l1=new Location("l1");
            Location l2=new Location("l2");

            l1.setLatitude(ll_list.get(i).latitude);
            l1.setLongitude(ll_list.get(i).longitude);

            l2.setLatitude(ll_list.get(i+1).latitude);
            l2.setLongitude(ll_list.get(i+1).longitude);
            km+=l1.distanceTo(l2)/1000;
        }
        System.out.println("KM KM "+km);
        double econom_cost= Double.parseDouble(my.tarif_Econom_minsum);
        double comfort_cost= Double.parseDouble(my.tarif_Comfort_minsum);
        double comfortp_cost= Double.parseDouble(my.tarif_Comfortp_minsum);
        double business_cost= Double.parseDouble(my.tarif_Business_minsum);
        if(km>2){
            econom_cost+=((km-2)*Double.parseDouble(my.tarif_Econom_km_summ));
        }
        if(km>2){
            comfort_cost+=((km-2)*Double.parseDouble(my.tarif_Comfort_km_summ));
        }
        if(km>2){
            comfortp_cost+=((km-2)*Double.parseDouble(my.tarif_Comfortp_km_summ));
        }
        if(km>2){
            business_cost+=((km-2)*Double.parseDouble(my.tarif_Business_km_summ));
        }
        ((TextView)findViewById(R.id.econom_cost)).setText(String.valueOf(econom_cost).split("\\.")[0]);
        ((TextView)findViewById(R.id.comfort_cost)).setText(String.valueOf(comfort_cost).split("\\.")[0]);
        ((TextView)findViewById(R.id.comfortp_cost)).setText(String.valueOf(comfortp_cost).split("\\.")[0]);
        ((TextView)findViewById(R.id.business_cost)).setText(String.valueOf(business_cost).split("\\.")[0]);
        System.out.println(ll_list.size());
    }

    @Override
    public void onCameraMoveStarted(int i) {
        if(my.camera_marker!=null){
            if(my.camera_marker.isInfoWindowShown())
                my.camera_marker.hideInfoWindow();
            my.camera_marker.setIcon(gl_marker_up);
        }
    }


    public void cancel(View view) {
        view.setVisibility(View.GONE);
        if (currentPolyline != null)
            currentPolyline.remove();
        if(my.otkuda_marker!=null)
            my.otkuda_marker.remove();
        if(my.kuda_marker!=null)
            my.kuda_marker.remove();
        my.otkuda_marker=null;
        my.kuda_marker=null;
        my.otkuda="";
        my.kuda="";
        my.place="";
        menu.setVisibility(View.GONE);
        menu2.setVisibility(View.VISIBLE);
        accept_ok.setVisibility(View.GONE);
        if(my.camera_marker!=null){
            my.camera_marker.remove();
            my.camera_marker=null;
        }

        my.camera_marker= mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target)
                .icon(gl_marker));

    }

    @Override
    protected void onDestroy() {
        server.send("c",false,true);
        server.close();
        super.onDestroy();
    }

    public void edit_otkuda(View view) {
        my.is_edit_kuda=false;
        Intent intent = new Intent(MapsActivity.this, KudaOtkudaListActivity.class);
        startActivity(intent);
    }

    public void edit_kuda(View view) {
        my.is_edit_kuda=true;
        Intent intent = new Intent(MapsActivity.this, KudaOtkudaListActivity.class);
        startActivity(intent);
    }

    public void main_menu_on_click(View view) {
        main_menu.setVisibility(View.VISIBLE);
        main_menu_help.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
        menu2.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if(main_menu.getVisibility()==View.VISIBLE){
            main_menu.setVisibility(View.GONE);
            main_menu_help.setVisibility(View.GONE);
            ImageView imageView = findViewById(R.id.main_menu_icon);
            imageView.setVisibility(View.VISIBLE);
            menu2.setVisibility(View.VISIBLE);
        }
    }

    public void my_location_onclick(View view) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude())));
    }

    public void hide_menu(View view) {
        onBackPressed();
    }

    public void driver(View view) {
        Intent intent = new Intent(MapsActivity.this, driver.class);
        startActivity(intent);
        onBackPressed();
    }

    public void okno_2(View view) {
        Intent intent = new Intent(MapsActivity.this, okno_2.class);
        startActivity(intent);
        onBackPressed();
    }

    public void okno_3(View view) {
        Intent intent = new Intent(MapsActivity.this, okno_3.class);
        startActivity(intent);
        onBackPressed();
    }

    public void okno_4(View view) {
        Intent intent = new Intent(MapsActivity.this, okno_4.class);
        startActivity(intent);
        onBackPressed();
    }

    public void okno_5(View view) {
        Intent intent = new Intent(MapsActivity.this, okno_5.class);
        startActivity(intent);
        onBackPressed();
    }

    public void okno_6(View view) {
        Intent intent = new Intent(MapsActivity.this, okno_6.class);
        startActivity(intent);
        onBackPressed();
    }

    public void okno_7(View view) {
        Intent intent = new Intent(MapsActivity.this, okno_7.class);
        startActivity(intent);
        onBackPressed();
    }

    public void okno_8(View view) {
        Intent intent = new Intent(MapsActivity.this, okno_8.class);
        startActivity(intent);
        onBackPressed();
    }
}