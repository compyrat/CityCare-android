package com.albertribas_ericcaballero_albertmarlet.proyecto_final;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private TextView StreetNameMaps;

    private Circle mapCircle;
    private Marker marker;

    private String addressLocation = "";
    private Button bt_selectLocation;
    private Button bt_cancelLocation;
    private FloatingActionButton FAB_maps;

    private Geocoder geocoder;

    private double lat = 0;
    private double lng = 0;
    private double finallat = 0;
    private double finallng = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);
        getBundlefromActivity();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        references();
        getBundlefromActivity();
        BTMethods();
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                cameraChanged();
            }
        }

    }

    private void getBundlefromActivity(){
        Bundle bundle = getIntent().getExtras();
        lat = bundle.getDouble("lat", 0);
        lng = bundle.getDouble("lng", 0);
        if (lat == 0 && lng == 0){
            lat = 41.403505;
            lng = 2.1744417;
        }
    }

    private void BTMethods(){
        bt_cancelLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish();

            }
        });
        bt_selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressLocation.equals("")){

                }else {
                    IncidenceQuery.mapStreet = addressLocation;
                    IncidenceQuery.latMap = finallat;
                    IncidenceQuery.lngMap = finallng;
                    IncidenceQuery.setVisibleLocation();
                    onFinish();

                }
            }
        });
        FAB_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), 16));
                String gpsstats;
                if (lat!=41.403505 && lng!=2.1744417){
                    gpsstats = "Su posición marcada por el GPS durante la foto";
                }else{
                    gpsstats = "El GPS no pudo rastrear tu posición";
                }
                //Snackbar.make(v, gpsstats, Snackbar.LENGTH_SHORT).setActionTextColor(Color.WHITE).show();
                Snackbar snack = Snackbar.make(v, gpsstats, Snackbar.LENGTH_SHORT);
                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snack.show();
            }
        });
    }
    private void references(){
        StreetNameMaps = (TextView) findViewById(R.id.StreetNameMaps);
        bt_selectLocation = (Button) findViewById(R.id.bt_selectLocation);
        bt_cancelLocation = (Button) findViewById(R.id.bt_cancelLocation);
        FAB_maps = (FloatingActionButton) findViewById(R.id.FAB_maps);
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
    }

    private void cameraChanged(){
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (mapCircle != null) {
                    mapCircle.remove();
                    marker.remove();
                    //borraría los círculos y marker de la posición del usuario
                }
                CircleOptions circleOptions = new CircleOptions();
                circleOptions.radius(4);
                circleOptions.center(cameraPosition.target);
                circleOptions.strokeColor(Color.parseColor("#2065CE"));
                mapCircle = mMap.addCircle(circleOptions);
                //Añade un círculo semitransparente para indica la zona de radio de búsqueda
                marker = mMap.addMarker(new MarkerOptions().position(cameraPosition.target).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                StreetNameMaps.setText("Calle Falsa 12346543756437u658468");

                try {
                    finallat = cameraPosition.target.latitude;
                    finallng = cameraPosition.target.longitude;
                    if (cameraPosition.target.latitude != 0 && cameraPosition.target.longitude != 0) {
                        List<Address> list = geocoder.getFromLocation(cameraPosition.target.latitude, cameraPosition.target.longitude, 1);
                        Address address = list.get(0);
                        addressLocation = address.getAddressLine(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                StreetNameMaps.setText(addressLocation);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat,lng);
        mMap.addMarker(new MarkerOptions().position(sydney).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
    }

    public void onFinish(){
        try {
            geocoder = null;
            marker.remove();
            mapCircle.remove();
            mMap.clear();
        }catch(NullPointerException npe){
            Log.e("NullOnFinish", "Marker / Map null");
        }finally{
            marker = null;
            mMap = null;
        }
        finish();
    }
}
