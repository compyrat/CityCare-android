package com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by albertmarnun on 12/05/2016.
 */
public abstract class UploadImageRequest extends AsyncTask<String, Void, String> {
    private String aux = "";
    private String encodedString = "";
    private Bitmap bitmap;
    private Context context;

    public UploadImageRequest(Context iContext){
        context = iContext;
    }

    protected String doInBackground(String... urls) {

        int serverResponseCode = 0;
        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String selectedFilePath = urls[0];

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;


        bitmap = Utils.catchOutOfMemory(selectedFilePath, 0);
        bitmap = Utils.scaleImage(bitmap, 0);



        File selectedFile = new File(context.getCacheDir(), "UploadImage");
        try {
            selectedFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        //bitmap = Bitmap.createScaledBitmap(this.bitmap, w, h, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outStream);

        //File selectedFile = new File(selectedFilePath);

        byte[] bitmapdata = outStream.toByteArray();

        try {
            FileOutputStream fos = new FileOutputStream(selectedFile);

            fos.write(bitmapdata);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length - 1];

        System.out.print("-80");
        try {
            System.out.print("-90");
            FileInputStream fileInputStream = new FileInputStream(selectedFile);
            URL url = new URL(Utils.URL + "/incidencias/image/upload");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);//Allow Inputs
            connection.setDoOutput(true);//Allow Outputs
            connection.setUseCaches(false);//Don't use a cached Copy
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            connection.setRequestProperty("uploaded_file", selectedFilePath);

            System.out.print("-100");
            //creating new dataoutputstream
            dataOutputStream = new DataOutputStream(connection.getOutputStream());

            //writing bytes to data outputstream
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                    + selectedFilePath + "\"" + lineEnd);

            dataOutputStream.writeBytes(lineEnd);

            //returns no. of bytes present in fileInputStream
            bytesAvailable = fileInputStream.available();
            //selecting the buffer size as minimum of available bytes or 1 MB
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            //setting the buffer as byte array of size of bufferSize
            buffer = new byte[bufferSize];

            //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            //loop repeats till bytesRead = -1, i.e., no bytes are left to read
            while (bytesRead > 0) {
                //write the bytes read from inputstream
                dataOutputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();

            //closing the input and output streams
            fileInputStream.close();
            dataOutputStream.flush();
            dataOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        return "";
    }


    protected void onPostExecute(String urls) {
        // TODO: check this.exception
        // TODO: do something with the feed
        if (!aux.equals("")) {
            try {
                JSONObject json = new JSONObject(aux);
                String id = json.getString("user");
                onSuccess();
                //read(intAux);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        onSuccess();
    }

    public abstract void onSuccess();


}
