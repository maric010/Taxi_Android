package com.example.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class my {
    static boolean is_edit_kuda=false;
    static String tarif_Econom_freewait,tarif_Econom_wait_summ,tarif_Econom_km2_summ,tarif_Econom_kmout_summ,tarif_Econom_km_summ,tarif_Econom_minsum;
    static String tarif_Comfort_freewait,tarif_Comfort_wait_summ,tarif_Comfort_km2_summ,tarif_Comfort_kmout_summ,tarif_Comfort_km_summ,tarif_Comfort_minsum;
    static String tarif_Comfortp_freewait,tarif_Comfortp_wait_summ,tarif_Comfortp_km2_summ,tarif_Comfortp_kmout_summ,tarif_Comfortp_km_summ,tarif_Comfortp_minsum;
    static String tarif_Business_freewait,tarif_Business_wait_summ,tarif_Business_km2_summ,tarif_Business_kmout_summ,tarif_Business_km_summ,tarif_Business_minsum;

    static String phone = "";
    static String code_from_sms = "";

    static String RAYON = "Учкурган1223";
    static String place="not found";
    static String otkuda="";
    static String kuda="";
    static ArrayList<LatLng> otkuda_kuda_points=new ArrayList<>();
    static Map<Integer,Marker> cars_marker =  new HashMap<Integer,Marker>();
    static Marker otkuda_marker, kuda_marker,camera_marker;
    static LatLng rayon_latlng = new LatLng(41.115535,72.0746892);
    static Marker my_marker;

}
