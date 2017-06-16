package com.albertribas_ericcaballero_albertmarlet.proyecto_final;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request.NewIncidenceRequest;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request.UploadImageRequest;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Tutorial;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Utils;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.model.IncidenceCategory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IncidenceQuery extends AppCompatActivity {

    private EditText query_name_ET;
    private EditText query_location_ET;
    private Button query_cancel_BT;
    private Button query_send_BT;
    private ImageButton query_clean_IB;
    public static double lon;
    public static double lat;
    private String pictureUrl;
    public static String addressLocation;
    private Spinner incidenceTypeSpinner;
    private Button BT_mapsActivity;
    private LinearLayout LLTutorial4;
    private static EditText getQuery_location_User;
    public static String mapStreet = null;
    public static double lngMap = 0;
    public static double latMap = 0;
    private Bitmap bmap = null;
    private Bitmap bmap2 = null;
    private static Boolean userLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidence_query);
        references();
        events();
    }
    private void events(){
        captureBundle();
        BTMethods();
        IBMethods();
        getStreetGeoCoder();
        setTextOnEditText();
        spinnerListener();
        if (Utils.getIncidenceTutorial()) {
            Utils.setIncidenceTutorial(IncidenceQuery.this, false);
            View[] views = {query_name_ET, incidenceTypeSpinner, LLTutorial4};
            String[] texts = {"Exponga brevemente el motivo de la incidencia", "Seleccione una categoría del desplegable", "Aquí saldrá su calle. Si no es así, pulse el botón a continuación para abrir un mapa y seleccionar su calle"};
            int[] gravitys = {8, 8, 8};
            boolean[] CircOrRect = {false, false, false};
            Tutorial.createTutorials(IncidenceQuery.this, gravitys, CircOrRect, texts, views);
        }
    }

    private void spinnerListener() {
        List list = new ArrayList();
        list.add(IncidenceCategory.DETERIORO.toString());
        list.add(IncidenceCategory.MOBILIARIO.toString());
        list.add(IncidenceCategory.ALUMBRADO.toString());
        list.add(IncidenceCategory.ALCANTARILLADO.toString());
        list.add(IncidenceCategory.LIMPIEZA.toString());
        list.add(IncidenceCategory.SEMAFOROS.toString());
        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incidenceTypeSpinner.setAdapter(dataAdapter);
        incidenceTypeSpinner.setPrompt("Selecciona un tipo de incidencia");
    }
    private void setTextOnEditText(){
        query_location_ET.setText(addressLocation);
    }
    private void getStreetGeoCoder(){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            if (lat != 0 && lon != 0) {
                List<Address> list = geocoder.getFromLocation(lat, lon, 1);
                Address address = list.get(0);
                addressLocation = address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void references(){
        query_name_ET = (EditText) findViewById(R.id.query_name_ET);
        query_location_ET = (EditText) findViewById(R.id.query_location_ET);
        getQuery_location_User = (EditText) findViewById(R.id.query_location_Usuario);

        query_cancel_BT = (Button) findViewById(R.id.query_cancel_BT);
        query_send_BT = (Button) findViewById(R.id.query_send_BT);
        BT_mapsActivity = (Button)  findViewById(R.id.BT_mapsActivity);

        query_clean_IB = (ImageButton) findViewById(R.id.query_clean_IB);
        incidenceTypeSpinner = (Spinner) findViewById(R.id.type_of_incidence_Spinner);

        LLTutorial4 = (LinearLayout)findViewById(R.id.LLTutorial4);
    }
    public static void setVisibleLocation( ){
        getQuery_location_User.setVisibility(View.VISIBLE);
        getQuery_location_User.setText(mapStreet);
        userLocation = true;
    }
    private void captureBundle(){
        Bundle bundle = getIntent().getExtras();
        //TODO get Photo from bundle and Location.
        lat = Double.parseDouble(bundle.getString("lat", "0"));
        lon = Double.parseDouble(bundle.getString("lon", "0"));
        pictureUrl = bundle.getString("pictureUrl");
    }

    private void BTMethods(){
        query_cancel_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IncidenceQuery.this, MainActivity.class));
                finish();
            }
        });
        query_send_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(query_name_ET.getText())) {
                    Toast.makeText(getApplicationContext(), "Debe rellenar todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    //Theme_Black_NoTitleBar_Fullscreen
                    if (TextUtils.isEmpty(getQuery_location_User.getText()) && TextUtils.isEmpty(query_location_ET.getText())) {
                        Toast.makeText(getApplicationContext(), "Debe seleccionar la ubicación", Toast.LENGTH_SHORT).show();
                    } else {
                        //final Dialog dialog = new Dialog(IncidenceQuery.this, android.R.style.Theme_DeviceDefault_Light_DialogWhenLarge_NoActionBar);
                        final Dialog dialog = new Dialog(IncidenceQuery.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                        dialog.setContentView(R.layout.incidence_confirmation_dialog);
                        dialog.setTitle(query_name_ET.getText());
                        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                        Button BTsi = (Button) dialog.findViewById(R.id.buttonSi);
                        Button BTno = (Button) dialog.findViewById(R.id.buttonNo);
                        TextView tvNombre = (TextView) dialog.findViewById(R.id.incidenceNameTextView);
                        TextView tvLocation = (TextView) dialog.findViewById(R.id.incidenceLocationTextView);
                        TextView tvType = (TextView) dialog.findViewById(R.id.incidenceTypeTextView);
                        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
                        Utils.catchOutOfMemory(bmap, pictureUrl, 0, image);
                        tvNombre.setText(query_name_ET.getText());
                        String location = "";
                        if (mapStreet != null){
                            tvLocation.setText(mapStreet);
                        }else{
                            tvLocation.setText(addressLocation);
                        }
                        tvType.setText(incidenceTypeSpinner.getSelectedItem().toString());
                        BTsi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                final String name = String.valueOf(query_name_ET.getText());
                                final ProgressDialog loading = ProgressDialog.show(IncidenceQuery.this, "Registrando incidencia", "Por favor espera unos segundos...", false, false);
                                final JSONObject json = new JSONObject();
                                final int category = incidenceTypeSpinner.getSelectedItemPosition();
                                loading.show();
                                new UploadImageRequest(IncidenceQuery.this) {
                                    @Override
                                    public void onSuccess() {
                                        try {
                                            json.put("mac", Utils.getMac(IncidenceQuery.this));
                                            json.put("userId", Utils.getID());
                                            if (mapStreet != null) {
                                                json.put("street", mapStreet);
                                                json.put("streetbyUser", "1");
                                            } else {
                                                if (addressLocation != null) {
                                                    json.put("street", addressLocation);
                                                    json.put("streetbyUser", "0");
                                                }
                                            }
                                            if (latMap != 0) {
                                                json.put("lat", latMap);
                                            } else {
                                                if (lat != 0) {
                                                    json.put("lat", lat);
                                                }
                                            }
                                            if (lngMap != 0) {
                                                json.put("lng", lngMap);
                                            } else {
                                                if (lon != 0) {
                                                    json.put("lng", lon);
                                                }
                                            }
                                            json.put("category", category);
                                            String[] parts = pictureUrl.split("/");
                                            final String fileName = parts[parts.length - 1];
                                            json.put("image", fileName);
                                            json.put("title", name);
                                            json.put("email", Utils.getUser().getEmail());

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        new NewIncidenceRequest() {
                                            @Override
                                            public void onSuccess(String incidenceId) {
                                                String aux = incidenceId;
                                                loading.dismiss();
                                                startActivity(new Intent(IncidenceQuery.this, FinalScreen.class));
                                                finish();
                                            }

                                            @Override
                                            public void onFailed(String error) {
                                                new AlertDialog.Builder(IncidenceQuery.this)
                                                        .setTitle("Error " + error)
                                                        .setMessage("Hay un fallo de conexión con el servidor, inténtelo de nuevo más tarde.")
                                                        .setCancelable(true)
                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        })
                                                        .setIcon(android.R.drawable.ic_dialog_alert).create().show();
                                            }
                                        }.execute(json.toString());

                                    }
                                }.execute(pictureUrl);
                            }
                        });
                        BTno.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //dialog.dismiss();
                                final Dialog dialogFullScreen = new Dialog(IncidenceQuery.this, android.R.style.Theme_DeviceDefault_Light_DialogWhenLarge_NoActionBar);
                                dialogFullScreen.setContentView(R.layout.activity_image);
                                ImageView imageFull = (ImageView) dialogFullScreen.findViewById(R.id.imageFullScreen);
                                Utils.catchOutOfMemory(bmap, pictureUrl, 0, imageFull);

                                imageFull.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogFullScreen.dismiss();
                                        //dialog.show();
                                    }
                                });
                                dialogFullScreen.show();
                            }
                        });
                        dialog.show();
                    }
                }
            }
        });

        BT_mapsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userLocation){
                    startActivity(new Intent(IncidenceQuery.this, MapsActivity.class).putExtra("lat", lat).putExtra("lng", lon));
                }else {
                    startActivity(new Intent(IncidenceQuery.this, MapsActivity.class).putExtra("lat", latMap).putExtra("lng", lngMap));
                }
            }
        });
    }
    private void IBMethods(){
        query_clean_IB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query_name_ET.setText("");
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(IncidenceQuery.this, MainActivity.class));
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Utils.gps.resumeGPS();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (bmap != null){
            bmap.recycle();
            bmap = null;
        }
        if (bmap2 != null){
            bmap2.recycle();
            bmap2 = null;
        }
        //Utils.gps.closeGPS();
    }

}
