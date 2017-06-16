package com.albertribas_ericcaballero_albertmarlet.proyecto_final;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request.BlackListRequest;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request.ServerStatusRequest;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DELAY = 500;
    private Timer timer = null;
    private Boolean stop = false;
    private TimerTask task = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);

        new ServerStatusRequest() {
            @Override
            public void onSuccess() {

                new BlackListRequest() {
                    @Override
                    public void onSuccess() {
                        timer = new Timer();
                        timer.schedule(task, SPLASH_SCREEN_DELAY);
                    }

                    @Override
                    public void onFailed() {
                        createDialog("Usuario bloqueado, por favor póngase en contacto con nuestro equipo técnico");
                    }
                }.execute(Utils.getMac(SplashScreen.this));

            }

            @Override
            public void onFailed() {
                createDialog("Servidor no disponible, vuelva a intentarlo en unos minutos");
            }
        }.execute();


        task = new TimerTask() {
            @Override
            public void run() {
                if (!stop) {
                    Intent mainIntent = new Intent().setClass(
                            SplashScreen.this, MainActivity.class);
                    startActivity(mainIntent);
                }
                finish();
            }
        };

    }

    private void createDialog(String message){
        new AlertDialog.Builder(SplashScreen.this)
                .setTitle("Error")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Recargar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(SplashScreen.this, SplashScreen.class));
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onBackPressed();
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert).create().show();
    }
    @Override
    public void onBackPressed() {
        stop = true;
        if (timer != null){
            timer.cancel();
            timer = null;
            super.finish();
        }
    }
}
