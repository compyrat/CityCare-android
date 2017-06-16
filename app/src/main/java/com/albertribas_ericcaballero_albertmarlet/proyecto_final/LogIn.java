package com.albertribas_ericcaballero_albertmarlet.proyecto_final;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request.LoginEmailRequest;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request.LoginFacebookRequest;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Digest;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.SaveSharedPreference;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Utils;
import com.facebook.FacebookSdk;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class LogIn extends AppCompatActivity{

    //FACEBOOK
    private LoginButton bFacebook = null;
    private CallbackManager callbackManager = null;
    private AccessToken accessToken = null;
    private Button registerButton;
    private Button loginButton;
    private EditText loginEmailText;
    private EditText loginPasswordText;
    private String loginEmail;
    private String loginPassword;
    private Boolean secondTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_log_in);
        references();
        events();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void references(){
        bFacebook = (LoginButton) findViewById(R.id.login_button);
        //bFacebook.setReadPermissions(permissions);
        bFacebook.setReadPermissions(Arrays.asList(
                "public_profile",
                "email",
                "user_birthday",
                "user_friends",
                "user_hometown"));
        callbackManager = CallbackManager.Factory.create();
        accessToken = AccessToken.getCurrentAccessToken();
        callbackManager = CallbackManager.Factory.create();
        registerButton = (Button)findViewById(R.id.registerButton);
        loginButton = (Button)findViewById(R.id.loginButton);
        loginEmailText = (EditText)findViewById(R.id.loginEmail);
        loginPasswordText = (EditText)findViewById(R.id.loginPassword);
    }

    private void events(){
        facebookEvent();
        registerEvent();
        loginEvent();
    }
    private void registerEvent(){
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(Register.class);
            }
        });
    }
    private void getStrings(){
        loginEmail = loginEmailText.getText().toString();
        loginPassword = loginPasswordText.getText().toString();
    }
    private void loginEvent(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStrings();
                JSONObject json = new JSONObject();
                try {
                    json.put("email", loginEmail);
                    json.put("password", Digest.StringToSha1(loginPassword));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new LoginEmailRequest() {
                    @Override
                    public void onSuccess(String id) {
                        Utils.setID(id);
                        Utils.createUser(id, "", loginEmail, "", "", "");
                        SaveSharedPreference.setUserID(LogIn.this, id, false);
                        Utils.setIsRegister(true);
                        changeActivity(MainActivity.class);
                    }

                    @Override
                    public void onFailed(String error) {
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                    }
                }.execute(json.toString());
            }
        });
    }
    public static void logOut(Context context){
        if (!FacebookSdk.isInitialized()){
            FacebookSdk.sdkInitialize(context);
        }
        LoginManager.getInstance().logOut();
    }
    private void facebookEvent(){
        // Callback registration
        bFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                URL url = null;
                                try {
                                    url = new URL(object.getJSONObject("picture").getJSONObject("data").getString("url"));
                                    final String urlString = String.valueOf(url);
                                    final String gender = object.optString("gender");
                                    final String name = object.optString("name");
                                    final String email = object.optString("email");
                                    final String id = object.optString("id");
                                    final String birthday = object.optString("birthday");

                                    JSONObject json = new JSONObject();
                                    try {
                                        json.put("avatar", urlString);
                                        json.put("name", name);
                                        json.put("id_prov", id);
                                        json.put("email", email);
                                        json.put("birthday", birthday);
                                        json.put("gender", gender);
                                        json.put("mac", Utils.getMac(LogIn.this));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    new LoginFacebookRequest() {
                                        @Override
                                        public void onSuccess(String id) {
                                            Utils.createUser(id, name, email, urlString, gender, birthday);
                                            SaveSharedPreference.setUserID(LogIn.this, id, true);
                                            Utils.setFacebookLogin(true);
                                            changeActivity(MainActivity.class);
                                        }
                                    }.execute(json.toString());
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large),hometown");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.v("LoginActivity", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.v("LoginActivity", error.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(LogIn.this, "Vuelve a pulsar para salir.", Toast.LENGTH_SHORT).show();
        if (secondTime) {
            this.finish();
        }
        secondTime = true;

    }
    private void changeActivity(Class activity){
        Intent mainIntent = new Intent(LogIn.this, activity);
        startActivity(mainIntent);
        this.finish();
    }
}
