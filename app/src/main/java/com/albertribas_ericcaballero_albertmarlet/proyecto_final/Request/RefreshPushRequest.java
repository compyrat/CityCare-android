package com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request;

import android.os.AsyncTask;

import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by albertmarnun on 24/05/2016.
 */
public abstract class RefreshPushRequest extends AsyncTask<String, Void, String> {
    public String aux = "";

    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(Utils.URL + "/users/edit/token"); //the URL we will send the request to

            JSONObject JSON = null;
            try {
                JSON = new JSONObject(urls[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");

            //Las siguientes propiedades solo son para POST en GET se han de quitar
            conn.setRequestProperty("Content-Type","application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //Hasta aquí

            // Starts the query
            OutputStream os = conn.getOutputStream();
            byte[] outputBytes = JSON.toString().getBytes("UTF-8");
            os.write(outputBytes);
            os.close();
            os.close();

            conn.connect();
            int response = conn.getResponseCode();
            InputStream is = conn.getInputStream();


            // Convert the InputStream into a string
            //String contentAsString = readIt(is, len);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    protected void onPostExecute(String urls) {
        onSuccess();
    }
    public abstract void onSuccess();

}