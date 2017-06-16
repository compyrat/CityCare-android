package com.albertribas_ericcaballero_albertmarlet.proyecto_final;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Push.QuickstartPreferences;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Push.RegistrationIntentService;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request.DownloadImageTask;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request.IncidenceRequest;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request.UserSummaryRequest;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Gps;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.QuotesReaderDbHelper;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.SaveSharedPreference;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Tutorial;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Utils;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.model.Incidence;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.model.PushMessage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private static final String TAG = "MainActivity";

    private FloatingActionButton fab = null;
    private DrawerLayout drawer = null;
    private ActionBarDrawerToggle toggle = null;
    private Toolbar toolbar = null;
    private NavigationView navigationView = null;

    private View headerView;
    private ImageView iV ;
    private TextView MenuUsername;
    private TextView MenuUsermail;
    private TextView inc_send;
    private TextView solved_inc;
    private TextView pospoints;
    private TextView negpoints;

    private Menu menu;
    private MenuItem MenuLoginItem;
    View[] arrV;

    private int index=0;

    public static ArrayList<Incidence> incidenceList;
    public static ArrayList<PushMessage> pushMessagesList = new ArrayList<>();

    private LinearLayout LLTutorial1;
    private LinearLayout LLTutorial2;
    private LinearLayout LLTutorial3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        references();
        events();
        userInfo();
    }

    private void references(){
        checkIfUserIsLogged();
        Utils.getAllTutorials(MainActivity.this);
        if (Utils.gps == null) {
            Utils.gps = new Gps(MainActivity.this);
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.inflateHeaderView(R.layout.nav_header_menu);
        iV = (ImageView) headerView.findViewById(R.id.imageView);
        MenuUsername = (TextView) headerView.findViewById(R.id.MenuUsername);
        MenuUsermail = (TextView) headerView.findViewById(R.id.MenuEmail);
        menu = navigationView.getMenu();
        MenuLoginItem = menu.findItem(R.id.nav_signin);
        incidenceList = new ArrayList<>();
        inc_send = (TextView)findViewById(R.id.sendInc_txt_number);
        solved_inc = (TextView)findViewById(R.id.solvedInc_txt_number);
        pospoints = (TextView)findViewById(R.id.positivePoints_txt_number);
        negpoints = (TextView)findViewById(R.id.negativePoints_txt_number);

        LLTutorial1 = (LinearLayout)findViewById(R.id.LLTutorial1);
        LLTutorial2 = (LinearLayout)findViewById(R.id.LLTutorial2);
        LLTutorial3 = (LinearLayout)findViewById(R.id.LLTutorial3);
    }

    private void pictureIconEvent(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //changeActivity(TakePicture.class);
                setIncidenceActivity();
            }
        });
    }

    private void loginButtonEvent() {
        iV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Utils.getIsRegister()) {
                    new UserSummaryRequest() {
                        @Override
                        public void onSuccess(JSONObject json) {
                            try {
                                JSONObject jUser = json.getJSONObject("user");
                                String name = jUser.optString("name");
                                String id = jUser.optString("_id");
                                String email = jUser.optString("email");
                                String birthday = jUser.optString("birthday");
                                String gender = jUser.optString("gender");
                                String avatar = jUser.optString("avatar");
                                String lastName = jUser.optString("lastname", "");
                                String pass = jUser.optString("password", "");
                                int positivePoints = jUser.optInt("positivePoints", 0);
                                int negativePoints = jUser.optInt("negativePoints", 0);
                                Utils.updateUser(id, name, email, avatar, gender, birthday, lastName, pass, positivePoints, negativePoints);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            changeActivity(Profile.class);
                        }
                    }.execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Debes iniciar sesión.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void changeStatistics(String incidenceCount, String incidenceSolved, int positivePoints, int negativePoints){
        IncidenceListActivity.numIncidence = incidenceCount;
        inc_send.setText(incidenceCount);
        solved_inc.setText(incidenceSolved);
        pospoints.setText(String.valueOf(positivePoints));
        negpoints.setText(String.valueOf(negativePoints));
        pospoints.setTextColor(Color.GREEN);
        negpoints.setTextColor(Color.RED);

        if (Utils.getMainTutorial()) {
            Utils.setMainTutorial(MainActivity.this, false);
            View[] arrV = new View[]{LLTutorial1, LLTutorial2, LLTutorial3, fab};
            String[] texts = {"Aquí se muestra un resumen de las incidencias del usuario", "Puntos positivos por incidencias tramitadas correctamente", "Puntos negativos por realizar trámites incorrectos", "Pulse aquí para empezar a realizar una incidencia"};
            int[] gravitys = {8, 2, 2, 1};
            boolean[] CircOrRect = {false, false, false, true};
            Tutorial.createTutorials(MainActivity.this, gravitys, CircOrRect, texts, arrV);
        }

    }
    private void events(){
        openSQLLiteConnection();

        pictureIconEvent();
        loginButtonEvent();
        broadcast();
        registerReceiver();
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
        checkDeletedPictures();
    }

    private void checkDeletedPictures(){
        Cursor crs = null;
        crs = Utils.db.rawQuery("SELECT * FROM incidencias", null);
        while (crs.moveToNext()) {
            if (Utils.getDate(crs.getString(crs.getColumnIndex("url"))).getTime() == 0) {
                Utils.db.execSQL("delete from incidencias where url = '"+crs.getString(crs.getColumnIndex("url"))+"'");
            }
        }
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void broadcast() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    if (Utils.notification){
                        Utils.notification = false;
                        gestor(Utils.type);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "NO", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void gestor(String type){
        switch(type){
            case "1": changeActivity(MainActivity.class);
                break;
            case "2":
                Utils.incidenceActionType = 3;
                changeActivity(IncidenceListActivity.class);
                break;
            case "3":
                Utils.incidenceActionType = 3;
                changeActivity(IncidenceListActivity.class);
                break;
            default:changeActivity(MainActivity.class);
                break;
        }
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private void checkIfUserIsLogged(){
        if(SaveSharedPreference.getUserID(MainActivity.this).length() == 0)
        {
            changeActivity(LogIn.class);
        }
      else
        {
            Utils.setID(SaveSharedPreference.getUserID(MainActivity.this));
            Utils.setFacebookLogin(SaveSharedPreference.getIsFacebook(MainActivity.this));
        }
    }


    private void openSQLLiteConnection(){
        Utils.usdbh = new QuotesReaderDbHelper(this, "incidencias", null, 1);
        Utils.db = Utils.usdbh.getWritableDatabase();
    }
    private void userInfo() {
        if (Utils.getIsRegister()) {
            new UserSummaryRequest() {
                @Override
                public void onSuccess(JSONObject json) {
                    try {
                        JSONObject jUser = json.getJSONObject("user");
                        String name = jUser.optString("name");
                        String id = jUser.optString("_id");
                        String email = jUser.optString("email");
                        String birthday = jUser.optString("birthday");
                        String gender = jUser.optString("gender");
                        String avatar = jUser.optString("avatar", "");

                        MenuUsername.setText(name);
                        MenuUsermail.setText(email);
                        MenuLoginItem.setTitle("Cerrar Sesion");
                        String lastName = jUser.optString("lastname", "");
                        String pass = jUser.optString("password", "");
                        String incidenceCount = json.optString("numberIncidences");
                        String incidenceSolved = json.optString("numberIncidencesSolved");
                        int positivePoints = jUser.optInt("positivePoints", 0);
                        int negativePoints = jUser.optInt("negativePoints", 0);
                        Utils.updateUser(id, name, email, avatar, gender, birthday, lastName, pass, positivePoints, negativePoints);
                        changeStatistics(incidenceCount, incidenceSolved, positivePoints, negativePoints);
                        if (!avatar.equals("")) {
                            if (!avatar.contains("http")) {
                                new DownloadImageTask(iV).execute(Utils.URL + "/users/image/" + avatar);
                            }else {
                                new DownloadImageTask(iV).execute(avatar);
                            }
                        }else{
                            iV.setImageResource(R.drawable.not_user);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute();

        }else{
            MenuUsername.setText("RibasCaballeroMarlet");
            MenuUsermail.setText("RibCabMar@gmail.com");
            MenuLoginItem.setTitle("Iniciar Sesion");
            iV.setImageResource(R.drawable.not_user);
        }
    }

    private void setIncidenceActivity() {

        String[] options = new String[]{"\tNueva foto", "\tAbrir galería"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona una opción");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int option) {
                switch (option) {
                    case 0:
                        changeActivity(TakePicture.class);
                        finish();
                        break;
                    case 1:
                        changeActivity(GalleryPic.class);
                        finish();
                        break;
                }
            }
        });
        builder.setIcon(R.drawable.ic_menu_gallery);
        builder.show();
    }
        @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void checkGpsActivity() {
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        /*if (!gps_enabled) {
            new AlertDialog.Builder(MainActivity.this).setTitle("Información").setMessage("Esta aplicación hace uso de la localización GPS. \nPor favor, encienda la localización GPS.")
                    .setPositiveButton("Ir a ajustes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
                        }
                    }).create().show();
        }*/
    }
    @Override
    public void onResume() {
        super.onResume();
        checkGpsActivity();
        Utils.gps.resumeGPS();
        if (Utils.gps == null) {
            Utils.gps = new Gps(MainActivity.this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Utils.gps.closeGPS();
    }
    private void changeActivity(Class activity){
        Intent mainIntent = new Intent(MainActivity.this, activity);
        startActivity(mainIntent);
        this.finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            new IncidenceRequest() {
                @Override
                public void onSuccess(JSONObject iJson) {
                    JSONObject json = iJson;
                }

                @Override
                public void onFailed(int error) {

                }
            }.execute();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new) {
            setIncidenceActivity();
        } else if (id == R.id.nav_remove) {
            Utils.incidenceActionType=2;
            changeActivity(IncidenceListActivity.class);
        } else if (id == R.id.nav_tracing) {
            Utils.incidenceActionType=3;
            changeActivity(IncidenceListActivity.class);
        } else if (id == R.id.nav_signin) {
            if (Utils.getIsRegister()){
                if (Utils.getFacebookLogin()) {
                    LogIn.logOut(MainActivity.this);
                }
                //DESCONECTAR LOGIN NORMAL
                Utils.setIsRegister(false);
                Utils.deleteUser();
                changeActivity(LogIn.class);
                SaveSharedPreference.setUserID(MainActivity.this, "", false);
                userInfo();
                Toast.makeText(getApplicationContext(), "Usuario desconectado", Toast.LENGTH_SHORT).show();
            }else{
                changeActivity(LogIn.class);
            }
        } /*else if (id == R.id.nav_settings) {
            changeActivity(MessagesActivity.class);
        }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
