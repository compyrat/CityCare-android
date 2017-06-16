package com.albertribas_ericcaballero_albertmarlet.proyecto_final;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request.DownloadImageTask;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request.UpdateUserRequest;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request.UploadAvatarRequest;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Digest;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Tutorial;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Utils;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {

    private EditText ETName = null;
    private EditText ETemail = null;
    private EditText ETLastName = null;
    private EditText ETPassword = null;
    private TextView TVUpPoints = null;
    private TextView TVDownPoints = null;
    private TextView TVName = null;

    private ImageButton BTpass;
    private Button BTEdit;
    private Button BTCancel = null;

    private ImageView arrowLeft = null;
    private ImageView arrowRight = null;

    private LinearLayout LLPassword;
    private LinearLayout LLLastName;

    private ImageView iV = null;
    private User user = null;

    private AppBarLayout appBarLayout;
    private String imagePath= "";
    private LinearLayout LLTutorial5;

    private final static int SELECT_PHOTO = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coordinator_profile);
        references();
        rellenar();
        BTMethods();
    }

    private void references(){
        ETName = (EditText) findViewById(R.id.etName);
        ETemail = (EditText) findViewById(R.id.etEmail);
        ETLastName = (EditText) findViewById(R.id.etLastName);
        ETPassword = (EditText) findViewById(R.id.etPassword);
        TVUpPoints = (TextView) findViewById(R.id.tvUpPoints);
        TVDownPoints = (TextView) findViewById(R.id.tvDownPoints);
        TVName = (TextView) findViewById(R.id.tv_name);
        arrowLeft = (ImageView) findViewById(R.id.flechaLeft);
        arrowRight = (ImageView) findViewById(R.id.flechaRight);

        BTpass = (ImageButton) findViewById(R.id.bt_password);
        BTEdit = (Button) findViewById(R.id.BT_edit);
        BTCancel = (Button) findViewById(R.id.BT_cancel);

        LLLastName = (LinearLayout) findViewById(R.id.LLLastName);
        LLPassword = (LinearLayout) findViewById(R.id.LLPass);
        LLTutorial5 = (LinearLayout) findViewById(R.id.Tutorial5);

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.setExpanded(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -appBarLayout.getTotalScrollRange()) {
                    showButtons();
                } else {
                    hideButtons();
                }
            }
        });

        iV = (ImageView) findViewById(R.id.imageView5);
        user = Utils.getUser();
    }

    private void showButtons(){
        if (!Utils.getFacebookLogin()) {
        arrowLeft.setImageResource(0);
        arrowRight.setImageResource(android.R.drawable.ic_menu_edit);
        arrowRight.setScaleType(ImageView.ScaleType.CENTER);
        ETName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        ETLastName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        }
    }

    private void hideButtons(){
//        if (arrowLeft.getVisibility()!=View.GONE) {
            arrowLeft.setImageResource(R.drawable.up);
            arrowRight.setImageResource(R.drawable.up);
            arrowLeft.setScaleType(ImageView.ScaleType.FIT_XY);
            arrowRight.setScaleType(ImageView.ScaleType.FIT_XY);
            ETName.setInputType(InputType.TYPE_NULL);
            ETLastName.setInputType(InputType.TYPE_NULL);
//        }
    }

    private void rellenar(){
        if (Utils.getFacebookLogin()){
            LLPassword.setVisibility(View.GONE);
            LLLastName.setVisibility(View.GONE);
            BTEdit.setEnabled(false);
        }else{
            LLPassword.setVisibility(View.VISIBLE);
            LLLastName.setVisibility(View.VISIBLE);
        }
        TVName.setText(user.getName());
        ETName.setText(user.getName());
        ETLastName.setText(user.getLastName());
        ETemail.setText(user.getEmail());
        TVUpPoints.setText(String.valueOf(user.getPossitivePoints()));
        TVUpPoints.setTextColor(Color.GREEN);
        TVDownPoints.setText(String.valueOf(user.getNegativePoints()));
        TVDownPoints.setTextColor(Color.RED);
        if (!user.getPhotoUrl().equals("")) {
            if (!user.getPhotoUrl().contains("http")) {
                new DownloadImageTask(iV).execute(Utils.URL + "/users/image/" + user.getPhotoUrl());
            }else {
                new DownloadImageTask(iV).execute(user.getPhotoUrl());
            }
        }else{
            iV.setImageResource(R.drawable.not_user);
        }
        tutorial();
    }

    private void BTMethods() {
        if (!Utils.getFacebookLogin()){
            iV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                }
            });
            BTpass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ETPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ETPassword.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(ETPassword, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        }

        BTCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizar();
            }
        });

        BTEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog loading = ProgressDialog.show(Profile.this, "Guardando datos", "Por favor espera unos segundos...", false, false);
                if (!Utils.getFacebookLogin()) {

                    String name = String.valueOf(ETName.getText());
                    String lastName = String.valueOf(ETLastName.getText());

                    String password = String.valueOf(ETPassword.getText());

                    final JSONObject json = new JSONObject();
                    try {
                        json.put("id", Utils.getID());
                        json.put("name", name);
                        json.put("lastname", lastName);
                        if (!password.equals("") && password != null) {
                            json.put("pass", Digest.StringToSha1(password));
                        }else{
                            json.put("pass", Utils.getUser().getPassword());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (!imagePath.equals("")) {

                        new UploadAvatarRequest(Profile.this) {
                            @Override
                            public void onFail() {

                            }

                            @Override
                            public void onSuccess(String path) {
                                try {
                                    json.put("avatar", path);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                new UpdateUserRequest() {
                                    @Override
                                    public void onSuccess() {
                                        loading.dismiss();
                                        Log.v("test", "test");
                                        finalizar();
                                    }
                                }.execute(json.toString());
                            }
                        }.execute(imagePath);
                    } else {
                        try {
                            json.put("avatar", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new UpdateUserRequest() {
                            @Override
                            public void onSuccess() {
                                loading.dismiss();
                                Log.v("test", "test");
                                finalizar();
                            }
                        }.execute(json.toString());
                    }
                }else{
                    finalizar();
                }
            }
        });
    }

    private void tutorial(){
        if (Utils.getProfileTutorial()) {
            Utils.setProfileTutorial(Profile.this, false);
            View[] arrV = new View[]{TVName, LLTutorial5};
            String[] texts = {"Arrastre esta barra para editar su usario.", "Una vez colapsado, edite a su gusto los campos disponibles."};
            int[] gravitys = {8, 2};
            boolean[] CircOrRect = {false, false};
            Tutorial.createTutorials(Profile.this, gravitys, CircOrRect, texts, arrV);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            iV.setImageBitmap(bitmap);
            //bitmap = null;


            cursor.close();
        }
    }

    @Override
    public void onBackPressed() {
        finalizar();
    }
    private void finalizar(){
        Intent i = new Intent(Profile.this, MainActivity.class);
        startActivity(i);
        this.finish();
    }
}
