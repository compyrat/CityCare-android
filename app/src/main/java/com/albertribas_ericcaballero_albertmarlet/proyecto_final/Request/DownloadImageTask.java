package com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Utils;

import java.io.InputStream;

/**
 * Created by albertmarnun on 27/04/2016.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Bitmap mIcon11;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            mIcon11 = BitmapFactory.decodeStream(in, new Rect(), options);
            mIcon11 = Utils.scaleImage(mIcon11, 0);
        } catch (Exception e) {
            Log.e("Error DownloadImage", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        bmImage.setImageBitmap(result);

    }
}
