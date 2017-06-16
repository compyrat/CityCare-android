package com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.albertribas_ericcaballero_albertmarlet.proyecto_final.model.User;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by albertmarnun on 27/04/2016.
 */
public class Utils {
    public static final String URL = "http://104.155.1.24:3131";
    public static JSONObject JSON = null;

    private static User user;
    private static Boolean isRegister = false;
    private static Boolean facebookLogin = false;
    private static String userID;

    public static Boolean notification = false;
    public static String type = "";

    private static String mac;
    public static int incidenceActionType = 0;

    public static QuotesReaderDbHelper usdbh ;
    public static SQLiteDatabase db;

    public static Gps gps = null;

    public static boolean firstLog = true;

    private static boolean mainTutorial = true;
    private static boolean takePicTutorial = true;
    private static boolean incidenceTutorial = true;
    private static boolean profileTutorial = true;

    public static void getAllTutorials(Context context){
        mainTutorial = SaveSharedPreference.getMainTutorial(context);
        takePicTutorial = SaveSharedPreference.getTakePictureTutorial(context);
        incidenceTutorial = SaveSharedPreference.getIncienceTutorial(context);
        profileTutorial = SaveSharedPreference.getProfileTutorail(context);
    }
    public static void setMainTutorial(Context context, Boolean tutorial){
        SaveSharedPreference.setMaintutorial(context, tutorial);
        mainTutorial = tutorial;
    }
    public static Boolean getMainTutorial(){
        return mainTutorial;
    }

    public static void setTakePicTutorial(Context context, Boolean tutorial){
        SaveSharedPreference.setTakePictureTutoria(context, tutorial);
        takePicTutorial = tutorial;
    }
    public static Boolean getTakePicTutorial(){
        return takePicTutorial;
    }

    public static void setIncidenceTutorial(Context context, Boolean tutorial){
        SaveSharedPreference.setIncienceTutorial(context, tutorial);
        incidenceTutorial = tutorial;
    }
    public static Boolean getIncidenceTutorial(){
        return incidenceTutorial;
    }

    public static void setProfileTutorial(Context context, Boolean tutorial){
        SaveSharedPreference.setProfileTutorail(context, tutorial);
        profileTutorial = tutorial;
    }
    public static Boolean getProfileTutorial(){
        return profileTutorial;
    }

    public static final Pattern EMAIL_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public static void setNotification(String iType){
        type = iType;
        notification = true;
    }
    public static boolean hasHardwareAcceleration(Activity activity) {
        // Has HW acceleration been enabled manually in the current window?
        Window window = activity.getWindow();
        if (window != null) {
            if ((window.getAttributes().flags
                    & WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED) != 0) {
                return true;
            }
        }

        // Has HW acceleration been enabled in the manifest?
        try {
            ActivityInfo info = activity.getPackageManager().getActivityInfo(
                    activity.getComponentName(), 0);
            if ((info.flags & ActivityInfo.FLAG_HARDWARE_ACCELERATED) != 0) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Chrome", "getActivityInfo(self) should not fail");
        }

        return false;
    }
    public static boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    //
    // Implementation User
    public static void createUser(String id, String name, String email, String photoUrl, String gender, String birthday){
        user = new User(id, name, email, photoUrl, gender, birthday);
        setID(id);
        setIsRegister(true);
    }
    public static void createUser(String id, String name, String email, String photoUrl, String gender, String birthday, String LastName, String pass){
        user = new User(id, name, email, photoUrl, gender, birthday, LastName, pass);
        setID(id);
        setIsRegister(true);
    }

    public static void createUser(String id, String name, String email, String photoUrl, String gender, String birthday, String LastName, String pass, int possiteve, int negative){
        user = new User(id, name, email, photoUrl, gender, birthday, LastName, pass, possiteve, negative);
        setID(id);
        setIsRegister(true);
    }

    public static String getIncidenceId() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Incidencia_" + timeStamp + "_" + Utils.getID();
        return imageFileName;
    }
    public static User getUser(){
        return user;
    }
    public static void deleteUser(){
        user = null;
    }
    public static void updateUser(String id, String name, String email, String photoUrl, String gender, String birthday){
        if (user == null){
            createUser(id, name, email, photoUrl, gender, birthday);
        }else{
            user = null;
            createUser(id, name, email, photoUrl, gender, birthday);
        }
    }
    public static void updateUser(String id, String name, String email, String photoUrl, String gender, String birthday, String LastName, String pass, int possiteve, int negative){
        if (user != null) user = null;

        createUser(id, name, email, photoUrl, gender, birthday, LastName, pass, possiteve, negative);

    }

    public static void setMac(String mac) {
        Utils.mac = mac;
    }
    //
    // Implementation isRegister;
    public static void setIsRegister(Boolean active){
        isRegister = active;
        if (!active) setFacebookLogin(false);
    }
    public static Boolean getIsRegister(){
        return isRegister;
    }

    public static Boolean getFacebookLogin() {
        return facebookLogin;
    }
    public static void setFacebookLogin(Boolean facebookLogin) {
        Utils.facebookLogin = facebookLogin;
    }
    public static void setID(String id){
        userID = id;
        setIsRegister(true);
    }
    public static String getID(){
        return userID;
    }

    public static Cursor getIncidencias() {
        return db.rawQuery("SELECT * FROM incidencias", null);
    }

    public static String getDateFromFile(String url) throws ParseException {
        File file = new File(url);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy    HH:mm:ss", Locale.ENGLISH);
        return dateFormat.format(file.lastModified());
    }
    public static Date getDate(String url){
        File file = new File(url);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy    HH:mm:ss", Locale.ENGLISH);
        Date date = new Date(file.lastModified());
        return date;
    }

    public static String getMac(Context context){
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }

    public static void catchOutOfMemory(Bitmap bitmap, String pictureUrl, int quality, ImageView imageView){
        int qual = quality + 1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            options.inSampleSize = qual;
            bitmap = BitmapFactory.decodeFile(pictureUrl, options);
            //if(!Utils.hasHardwareAcceleration(activity)) {

            if (imageView != null) {
                imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                //}
                imageView.setImageBitmap(bitmap);
            }
        }catch (OutOfMemoryError ex){
            Log.d("OutOfMemoryLog","Saltado excepción a calidad: "+qual);
            catchOutOfMemory(bitmap, pictureUrl, qual, imageView);
        }
    }

    public static Bitmap catchOutOfMemory(String pictureUrl, int quality){
        int qual = quality + 1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = null;
        try {
            options.inSampleSize = qual;
           bitmap = BitmapFactory.decodeFile(pictureUrl, options);
            //if(!Utils.hasHardwareAcceleration(activity)) {
        }catch (OutOfMemoryError ex){
            Log.d("OutOfMemoryLog","Saltado excepción a calidad: "+qual);
            bitmap = catchOutOfMemory(pictureUrl, qual);
        }
        return bitmap;
    }

    public static Bitmap scaleImage(Bitmap bmap, int quality){
        int h = bmap.getHeight();
        int w = bmap.getWidth();
        Bitmap bmap2 = null;
        int[] res = new int[]{1280, 1024, 720, 640, 480, 240, 1};
        try {
            // resize if > 800
            if ((h > w) && (h > res[quality])) {
                double f = (double) res[quality] / (double) h;
                h = res[quality];
                w *= f;
            } else if (w > res[quality]) {
                double f = (double) res[quality] / (double) w;
                w = res[quality];
                h *= f;
            }
            bmap2 = Bitmap.createScaledBitmap(bmap, w, h, true);
        }catch(OutOfMemoryError e){
            Log.d("OutOfMemoryScale", "ScaleImage "+ quality);
            scaleImage(bmap, quality+1);
        }
        return bmap2;
    }
}
