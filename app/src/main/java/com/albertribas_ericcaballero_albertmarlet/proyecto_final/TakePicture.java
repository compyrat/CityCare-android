package com.albertribas_ericcaballero_albertmarlet.proyecto_final;

import android.*;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.QuotesReaderDbHelper;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Tutorial;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Utils;

import java.io.File;
import java.io.IOException;

public class TakePicture extends AppCompatActivity {

    private ImageView previewImageView;
    private boolean pictureShooted = false;
    static final int takePictureRequest = 1;
    static final int takePictureFromGallery = 2;
    private String pictureUrl;
    private String lat;
    private String lon;
    private Button incidenceActivityButton;
    private Button savePictureButton;
    private Bitmap bmap = null;
    private Bitmap bmap2 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        references();
        if (!pictureShooted) {
            takePictureIntent();
        }
        events();
    }

    private void references(){
        previewImageView = (ImageView)findViewById(R.id.previewImageView);
        incidenceActivityButton = (Button)findViewById(R.id.incidenceActivityContinueButton);
        savePictureButton = (Button)findViewById(R.id.savePictureButton);
    }
    private void setLatLonValues(){
        try{
            lat = Utils.gps.getLatitude();
            lon = Utils.gps.getLongitude();
        }catch(NullPointerException npe){
            lat = "0";
            lon = "0";
            Log.e("setLatLonValues", "Exception thrown NullPointerException getting latlng");
        }
    }


    private void takePictureIntent(){
        if (Build.VERSION.SDK_INT >= 23 && TakePicture.this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                pictureUrl = photoFile.getAbsolutePath();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, takePictureRequest);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= 23 && TakePicture.this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (requestCode == takePictureRequest && resultCode == RESULT_OK) {
            pictureShooted = true;
            Utils.catchOutOfMemory(bmap, pictureUrl, 0, previewImageView);

            savePicture();
        }
        else if (resultCode == RESULT_CANCELED) {
            changeActivity(MainActivity.class);
        }
    }

    private File createImageFile() throws IOException {

        // Create an image file name
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                Utils.getIncidenceId(),  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        pictureUrl = "file:" + image.getAbsolutePath();
        return image;
    }

    private void events(){
        setLatLonValues();
        continueToIncidenceActivity();
        savePictureButton();
        clickImage();
    }

    private void clickImage(){
        previewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogFullScreen = new Dialog(TakePicture.this, android.R.style.Theme_DeviceDefault_Light_DialogWhenLarge_NoActionBar);
                dialogFullScreen.setContentView(R.layout.activity_image);
                ImageView imageFull = (ImageView) dialogFullScreen.findViewById(R.id.imageFullScreen);

                Utils.catchOutOfMemory(bmap2, pictureUrl, 0, imageFull);

                imageFull.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bmap2.recycle();
                        dialogFullScreen.dismiss();
                        //dialog.show();
                    }
                });
                dialogFullScreen.show();
            }
        });

    }
    private void continueToIncidenceActivity(){
        incidenceActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent().setClass(TakePicture.this, IncidenceQuery.class);
                mainIntent.putExtra("pictureUrl", pictureUrl);
                mainIntent.putExtra("lat", lat);
                mainIntent.putExtra("lon", lon);
                if (bmap != null){
                    bmap.recycle();
                    bmap = null;
                }
                startActivity(mainIntent);
                finish();
            }
        });
    }
    private void savePictureButton(){
        savePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeTakePicture();
            }
        });
    }

    private void savePicture(){
        QuotesReaderDbHelper usdbh = new QuotesReaderDbHelper(getApplicationContext(), "incidencias", null, 1);
        SQLiteDatabase db = usdbh.getWritableDatabase();
        if (lat == null || lon == null){
            lat = "0";
            lon = "0";
        }
        db.execSQL("insert into incidencias (lat, lon, url) values ('" + lat + "','" + lon + "','" + pictureUrl + "')");
        tutorial();
    }
    private void closeTakePicture() {
        if (bmap != null) {
            bmap.recycle();
            bmap = null;
        }
        changeActivity(MainActivity.class);
    }

    private void tutorial(){
        if (Utils.getTakePicTutorial()) {
            Utils.setTakePicTutorial(TakePicture.this, false);
            View[] arrV = new View[]{incidenceActivityButton, savePictureButton};
            String[] texts = {"Pulse 'Continuar' para seguir con la incidencia", "Pulse 'Guardar' para que la imagen se almacene en la galería de la aplicación"};
            int[] gravitys = {2, 2};
            boolean[] CircOrRect = {false, false};
            Tutorial.createTutorials(TakePicture.this, gravitys, CircOrRect, texts, arrV);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_take_picture, menu);
        return true;
    }
    private void changeActivity(Class activity){
        Intent mainIntent = new Intent().setClass(TakePicture.this, activity);
        startActivity(mainIntent);
        this.finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeTakePicture();
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
