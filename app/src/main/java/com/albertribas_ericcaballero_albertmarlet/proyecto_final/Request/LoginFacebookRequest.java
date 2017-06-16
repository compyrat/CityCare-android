package com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request;

import android.os.AsyncTask;

import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by albertmarnun on 21/04/2016.
 */
public abstract class LoginFacebookRequest extends AsyncTask<String, Void, String> {

    public String aux = "";

    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(Utils.URL + "/users/login/facebook"); //the URL we will send the request to

            int len = 500;
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

            aux = convertStreamToString(is);

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
        // TODO: check this.exception
        // TODO: do something with the feed
        if (!aux.equals("")){
            try {
                JSONObject json = new JSONObject(aux);
                String id = json.getString("user");
                onSuccess(id);
                //read(intAux);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public abstract void onSuccess(String id);

    private String convertStreamToString(InputStream is) {
/*
 * To convert the InputStream to String we use the BufferedReader.readLine()
 * method. We iterate until the BufferedReader return null which means
 * there's no more data to read. Each line will appended to a StringBuilder
 * and returned as String.
 */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}

