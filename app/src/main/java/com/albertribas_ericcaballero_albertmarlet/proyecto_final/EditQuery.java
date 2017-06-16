package com.albertribas_ericcaballero_albertmarlet.proyecto_final;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditQuery extends AppCompatActivity {

    private EditText edit_name_ET;
    private EditText edit_location_ET;
    private EditText edit_date_ET;
    private ImageButton edit_photo_IB;
    private Button edit_end_BT;
    private Spinner edit_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_query);
        references();
        events();
    }
    private void references(){
        edit_name_ET = (EditText) findViewById(R.id.edit_query_name_ET);
        edit_location_ET = (EditText) findViewById(R.id.edit_query_location_ET);
        edit_date_ET = (EditText) findViewById(R.id.edit_query_date_ET);
        edit_photo_IB = (ImageButton) findViewById(R.id.edit_query_photo_IB);
        edit_end_BT = (Button) findViewById(R.id.edit_query_end_BT);
        edit_spinner = (Spinner) findViewById(R.id.edit_spinner);
    }

    private void spinnerListener() {
        List list = new ArrayList();
        list.add("Deterioro de la Vía pública");
        list.add("Mobiliario urbano");
        list.add("Farolas");
        list.add("Alcantarillado");
        list.add("Limpieza");
        list.add("Semáforos y señales de tráfico");
        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_spinner.setAdapter(dataAdapter);
        edit_spinner.setPrompt("Selecciona un tipo de incidencia");
    }

    private void events(){
        IBMethods();
        BTMethods();
        spinnerListener();
        fillBoxes();
    }

    private void IBMethods(){
        edit_photo_IB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ir a la galería
            }
        });
    }
    private void BTMethods(){
        edit_end_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(edit_name_ET.getText());
                int category = edit_spinner.getSelectedItemPosition();
                String date = String.valueOf(edit_date_ET.getText());
                Bitmap photo = ((BitmapDrawable)edit_photo_IB.getDrawable()).getBitmap();
                MainActivity.incidenceList.get(IncidenceListActivity.card).setTitle(name);
                MainActivity.incidenceList.get(IncidenceListActivity.card).setCategory(category);
               // MainActivity.incidenceList.get(IncidenceListActivity.card).setDate(date);
                //MainActivity.incidenceList.get(IncidenceListActivity.card).setPicture(photo);
                Toast.makeText(EditQuery.this, "Cambios realizados correctamente", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditQuery.this, IncidenceListActivity.class));
                finish();
            }
        });
    }

    private void fillBoxes() {
        edit_name_ET.setText(MainActivity.incidenceList.get(IncidenceListActivity.card).getTitle());
        edit_location_ET.setText(MainActivity.incidenceList.get(IncidenceListActivity.card).getLocation());
       // edit_date_ET.setText(MainActivity.incidenceList.get(IncidenceListActivity.card).getDate());
        edit_spinner.setSelection(MainActivity.incidenceList.get(IncidenceListActivity.card).getCategory(), true);
        //edit_photo_IB.setImageBitmap(MainActivity.incidenceList.get(IncidenceListActivity.card).getPicture());
    }
}
