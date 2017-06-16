package com.albertribas_ericcaballero_albertmarlet.proyecto_final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request.RegisterRequest;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Digest;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.SaveSharedPreference;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    private Button registerButton;
    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText password;
    private String json_name;
    private String json_email;
    private String json_surname;
    private String json_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        references();
        events();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }
    private void references(){
        registerButton = (Button)findViewById(R.id.registerButton2);
        name = (EditText)findViewById(R.id.registerName);
        surname = (EditText)findViewById(R.id.registerSurname);
        email = (EditText)findViewById(R.id.registerEmail);
        password = (EditText)findViewById(R.id.registerPassword);
    }
    private void events(){
    registerButtonEvent();
    }

    private void registerButtonEvent(){
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStrings();
                registerRequest();
            }
        });
    }
    private void getStrings() {
        json_name = name.getText().toString();
        json_email = email.getText().toString();
        json_password = Digest.StringToSha1(password.getText().toString());
        json_surname = surname.getText().toString();
    }

    private void registerRequest() {
        if (!Utils.isValidEmail(json_email)) {
            Toast.makeText(getApplicationContext(), "El correo electronico no es valido.", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject json = new JSONObject();
            try {
                json.put("name", json_name);
                json.put("lastname", json_surname);
                json.put("email", json_email);
                json.put("password", json_password);
                json.put("mac", Utils.getMac(Register.this));
                json.put("accountType", "user");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new RegisterRequest() {
                @Override
                public void onSuccess(String id) {
                    Utils.createUser(id, json_name, json_email, "", "", "", json_surname, json_password);
                    SaveSharedPreference.setUserID(Register.this, id, false);
                    changeActivity(MainActivity.class);
                }

                @Override
                public void onFailed(String error) {
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                }
            }.execute(json.toString());
        }
    }

    private void changeActivity(Class activity){
        Intent mainIntent = new Intent().setClass(Register.this, activity);
        startActivity(mainIntent);
        this.finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Register.this, MainActivity.class));
        finish();
    }
}
