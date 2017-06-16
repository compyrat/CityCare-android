package com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ericcabmun on 11/05/2016.
 */
public class SaveSharedPreference
{
    private static final String ID= "user_id";
    private static final String ISFACEBOOK = "isFacebook";
    private static final String MAINTUTORIAL = "mainTutorial";
    private static final String TAKEPICTURETUTORIAL = "takePicTutorial";
    private static final String INCIDENCETUTORIAL = "incidenceTutorial";
    private static final String PROFILETUTORIAL = "profileTutorial";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserID(Context ctx, String userID, Boolean isFacebook)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(ID, userID);
        editor.putBoolean(ISFACEBOOK, isFacebook);
        editor.commit();
    }

    public static String getUserID(Context ctx)
    {
        return getSharedPreferences(ctx).getString(ID, "");
    }
    public static Boolean getIsFacebook(Context context){
        return getSharedPreferences(context).getBoolean(ISFACEBOOK, false);
    }

    // TUTORIALS
    public static Boolean getMainTutorial(Context context){
        return getSharedPreferences(context).getBoolean(MAINTUTORIAL, true);
    }
    public static void setMaintutorial(Context context, Boolean tutorial){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(MAINTUTORIAL, tutorial);
        editor.commit();
    }

    public static Boolean getTakePictureTutorial(Context context){
        return getSharedPreferences(context).getBoolean(TAKEPICTURETUTORIAL, true);
    }
    public static void setTakePictureTutoria(Context context, Boolean tutorial){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(TAKEPICTURETUTORIAL, tutorial);
        editor.commit();
    }

    public static Boolean getIncienceTutorial(Context context){
        return getSharedPreferences(context).getBoolean(INCIDENCETUTORIAL, true);
    }
    public static void setIncienceTutorial(Context context, Boolean tutorial){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(INCIDENCETUTORIAL, tutorial);
        editor.commit();
    }

    public static Boolean getProfileTutorail(Context context){
        return getSharedPreferences(context).getBoolean(PROFILETUTORIAL, true);
    }
    public static void setProfileTutorail(Context context, Boolean tutorial){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(PROFILETUTORIAL, tutorial);
        editor.commit();
    }
}