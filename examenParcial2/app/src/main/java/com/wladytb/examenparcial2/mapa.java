package com.wladytb.examenparcial2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.wladytb.examenparcial2.adaptador.InfoWindowAdapter;
import com.wladytb.examenparcial2.modelo.modeloMarker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class mapa extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mapa;
    android.widget.RadioButton RadioButton;
    String pais = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        getSupportActionBar().hide();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        RadioButton = (RadioButton) findViewById(R.id.rdNormal);
        RadioButton.setChecked(true);
        Bundle parametro = this.getIntent().getExtras();
        if (parametro != null) {
            pais = parametro.getString("pais");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa.getUiSettings().setZoomControlsEnabled(true);

/*
        for (modeloMarker mm : cargarMarker()) {

            mapa.setInfoWindowAdapter(new InfoWindowAdapter(getLayoutInflater()));
        }*/
        consumirGoolgeVolley();
        mapa.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(getApplicationContext(), latLng.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void poligono() {
        try {
            Double Lat = paisObtenido.getJSONArray("GeoPt").getDouble(0);
            Double Lng = paisObtenido.getJSONArray("GeoPt").getDouble(1);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(Lat, Lng), 4);
            mapa.animateCamera(cameraUpdate);
            mapa.moveCamera(cameraUpdate);
            PolylineOptions lines = new PolylineOptions()
                    .add(new LatLng(paisObtenido.getJSONObject("GeoRectangle").getDouble("North"),
                            paisObtenido.getJSONObject("GeoRectangle").getDouble("West")))
                    .add(new LatLng(paisObtenido.getJSONObject("GeoRectangle").getDouble("North"),
                            paisObtenido.getJSONObject("GeoRectangle").getDouble("East")))
                    .add(new LatLng(paisObtenido.getJSONObject("GeoRectangle").getDouble("South"),
                            paisObtenido.getJSONObject("GeoRectangle").getDouble("East")))
                    .add(new LatLng(paisObtenido.getJSONObject("GeoRectangle").getDouble("South"),
                            paisObtenido.getJSONObject("GeoRectangle").getDouble("West")))
                    .add(new LatLng(paisObtenido.getJSONObject("GeoRectangle").getDouble("North"),
                            paisObtenido.getJSONObject("GeoRectangle").getDouble("West")));
            mapa.addPolyline(lines);

            mapa.addMarker(new MarkerOptions().
                    position(new LatLng(paisObtenido.getJSONObject("Capital").getJSONArray("GeoPt").getDouble(0),
                            paisObtenido.getJSONObject("Capital").getJSONArray("GeoPt").getDouble(1))).
                    title(paisObtenido.getString("Name")).
                    snippet("Capital: " + paisObtenido.getJSONObject("Capital").getString("Name") + "; C. país: "
                            + paisObtenido.getJSONObject("CountryCodes").getString("iso2") + "; ;Lat: " +
                            paisObtenido.getJSONArray("GeoPt").getDouble(0) + " , Lng: " +
                            paisObtenido.getJSONArray("GeoPt").getDouble(1) + " ; TelPref: " +
                            paisObtenido.getString("TelPref") + ";http://www.geognos.com/api/en/countries/flag/" +
                            paisObtenido.getJSONObject("CountryCodes").getString("iso2") + ".png"));
            mapa.setInfoWindowAdapter(new InfoWindowAdapter(getLayoutInflater()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void mapaNormal(View view) {
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa.getUiSettings().setZoomControlsEnabled(true);
    }

    public void mapaHybrido(View view) {
        mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mapa.getUiSettings().setZoomControlsEnabled(true);
    }

    public void mapaTerrain(View view) {
        mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mapa.getUiSettings().setZoomControlsEnabled(true);
    }

    public void mapaSatelital(View view) {
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(true);
    }

    public List<modeloMarker> cargarMarker() {
        List<modeloMarker> listaMarker = new ArrayList<>();
        modeloMarker fci = new modeloMarker(
                "Facultad de Ciencias de la Ingeniería ",
                "Ing. Washington Alberto Chiriboga Casanova, M.Sc.",
                "facultadci@uteq.edu.ec",
                "https://www.uteq.edu.ec/images/about/logo_fci.jpg",
                "-1.0125821345064658", "-79.47061004072364"
        );
        modeloMarker fce = new modeloMarker(
                "Facultad de Ciencias Empresariales",
                "Ing. Mariela Susana Andrade Arias, PhD.",
                "facultadce@uteq.edu.ec",
                "https://www.uteq.edu.ec/images/about/logo_fce.jpg",
                "-1.0121446581770701", "-79.47035097530821");

        modeloMarker fca = new modeloMarker(
                "Facultad de Ciencias Agropecuarias",
                "Ing. Leonardo Gonzalo Matute, M.Sc.",
                "decanato_agropecuarias@uteq.edu.ec",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScDRZJ3wT-_O09VEvtMYqordRtqCS0z72Jyw&usqp=CAU",
                "-1.0813624489354845", "-79.50302236394958");
        modeloMarker fcip = new modeloMarker(
                "Facultad de Ciencias de la Industria y Producción",
                "Ing. Sonnia Esther Barzola Miranda, M.Sc.",
                "fcip@uteq.edu.ec",
                "https://3.bp.blogspot.com/-fYlOzJNk0DA/WoSbHJXGzcI/AAAAAAAAA7E/SGC6Hp2qlcEDCbks59AykK3-8EYeSDeEQCLcBGAs/s320/Imagen1.jpg",
                "-1.0126327850989705", "-79.47107283317435");
        listaMarker.add(fci);
        listaMarker.add(fce);
        listaMarker.add(fca);
        listaMarker.add(fcip);
        return listaMarker;
    }

    JSONObject paisObtenido;

    public void consumirGoolgeVolley() {
        System.out.println("entra----------------------------------------------------");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(
                Request.Method.GET, "http://www.geognos.com/api/en/countries/info/all.json",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = response.getJSONObject("Results");
                    Iterator<String> keys = jsonObject.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        JSONObject obj = new JSONObject(jsonObject.get(key).toString());
                        if (obj.getString("Name").toLowerCase().equals(pais.toLowerCase())) {
                            System.out.println("data: " + jsonObject.get(key).toString());
                            paisObtenido = obj;
                            JSONObject ini = obj.getJSONObject("CountryCodes");
                            //getBandera(ini.getString("iso2"));
                            poligono();
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        requestQueue.add(jsObjectRequest);
    }
}