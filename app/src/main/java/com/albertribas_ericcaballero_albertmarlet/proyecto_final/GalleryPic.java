package com.albertribas_ericcaballero_albertmarlet.proyecto_final;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.QuotesReaderDbHelper;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("deprecation")
public class GalleryPic extends Activity {

    private Button continueButton;
    private ImageView imageViewFinalPicture;

    private QuotesReaderDbHelper usdbh;
    private SQLiteDatabase db;

    private List<String> url = new ArrayList();

    private String finalUrl;
    private String lat;
    private String lon;

    private TextView dateText;

    Gallery gallery = null;

    Bitmap bmap = null;

    private Date today = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_pic);
        references();
        events();
    }

    private void events() {
        if (checkIfGalleryHasImages()) {
            getImagesFromGallery();
            createGallery();
            continueToIncidence();
        }
    }

    private boolean checkIfGalleryHasImages() {
        Cursor crs = db.rawQuery("SELECT * FROM incidencias", null);
        if(crs != null && crs.getCount() > 0){
           return true;
        }else {
            new AlertDialog.Builder(GalleryPic.this).setTitle("Galería vacia").setMessage("No hay imágenes en la galería").setCancelable(false)
                    .setPositiveButton("Volver al menú principal", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(GalleryPic.this, MainActivity.class));
                            finish();
                        }
                    }).create().show();
            return false;
        }
    }
    private void continueToIncidence() {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor crs = null;
                crs = db.rawQuery("SELECT lat, lon, url FROM incidencias where url = '" + finalUrl + "'", null);
                while (crs.moveToNext()) {
                    if(crs.getString(crs.getColumnIndex("lat")) != null) {
                        lat = crs.getString(crs.getColumnIndex("lat"));
                        lon = crs.getString(crs.getColumnIndex("lon"));
                    }else{
                        lat = "0";
                        lon = "0";
                    }
                }
                Intent mainIntent = new Intent().setClass(GalleryPic.this, IncidenceQuery.class);
                mainIntent.putExtra("pictureUrl", finalUrl);
                mainIntent.putExtra("lat", lat);
                mainIntent.putExtra("lon", lon);
                if (bmap != null) {
                    bmap.recycle();
                    bmap = null;
                }
                startActivity(mainIntent);
                finish();
            }
        });
    }

    private void setImageOnImageView(ImageView imageView, String urlPosition) {
        Utils.catchOutOfMemory(bmap, urlPosition, 0, imageView);
    }

    private void createGallery() {
        setImageOnImageView(imageViewFinalPicture, url.get(0));
        finalUrl = url.get(0);
        if(Utils.getDate(finalUrl).getTime() == 0){
            continueButton.setEnabled(false);
        }
        try {
            dateText.setText(Utils.getDateFromFile(url.get(0)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        gallery.setAdapter(new ImageAdapter(this));
        gallery.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                setImageOnImageView(imageViewFinalPicture, url.get(position));
                finalUrl = url.get(position);
                if(Utils.getDate(finalUrl).getTime() == 0){
                    continueButton.setEnabled(false);
                }
                try {
                    dateText.setText(Utils.getDateFromFile(finalUrl));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void references() {
        continueButton = (Button) findViewById(R.id.select_picture_from_gallery_button);
        imageViewFinalPicture = (ImageView) findViewById(R.id.image1);

        dateText = (TextView) findViewById(R.id.dateTextOnGallery);

        usdbh = new QuotesReaderDbHelper(this, "incidencias", null, 1);
        db = usdbh.getWritableDatabase();

        gallery = (Gallery) findViewById(R.id.gallery1);
        today.getTime();
    }

    private void getImagesFromGallery() {
        Cursor crs = db.rawQuery("SELECT * FROM incidencias", null);
        while (crs.moveToNext()) {
            String link = crs.getString(crs.getColumnIndex("url"));
            url.add(link);
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private int itemBackground;

        public ImageAdapter(Context c) {
            context = c;
            // sets a grey background; wraps around the images
            TypedArray a = obtainStyledAttributes(R.styleable.MyGallery);
            itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
            a.recycle();
        }

        // returns the number of images
        public int getCount() {
            return url.size();
        }

        // returns the ID of an item
        public Object getItem(int position) {
            return position;
        }

        // returns the ID of an item
        public long getItemId(int position) {
            return position;
        }

        // returns an ImageView view
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            setImageOnImageView(imageView, url.get(position));
            imageView.setLayoutParams(new Gallery.LayoutParams(200, 200));
            imageView.setBackgroundResource(itemBackground);
            return imageView;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (bmap != null) {
            bmap.recycle();
            bmap = null;
        }
        startActivity(new Intent(GalleryPic.this, MainActivity.class));
        finish();
    }
    @Override
    public void onStop(){
        super.onStop();
        if (bmap != null){
            bmap.recycle();
            bmap = null;
        }
    }
}