package com.albertribas_ericcaballero_albertmarlet.proyecto_final;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request.DeleteIncidenceRequest;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request.DownloadImageTask;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request.IncidenceRequest;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Utils;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.model.Incidence;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.model.IncidenceCategory;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.model.IncidenceStatus;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.model.RecyclerV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class IncidenceListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;


    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button more = null;
    private Button mine = null;
    private TextView pag = null;

    public static int card;

    private int skip = 0;
    private int lastCount = 0;
    private  Boolean isFirstTime = true;
    public static String numIncidence = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidence_list);
        references();
        events();
        if (isFirstTime){
            getIncidenceListfromRequest();
            mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
            mRecyclerView.setHasFixedSize(true);
        }

    }
    private void references(){
        pag = (TextView) findViewById(R.id.pag);
        more = (Button) findViewById(R.id.more);
        mine = (Button) findViewById(R.id.mine);
        pag.setText("Pag " + String.valueOf(1));

    }
    private void events(){
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((skip) < (Integer.parseInt(numIncidence))) {
                    getIncidenceListfromRequest();
                }
                pag.setText("Pag " + String.valueOf((skip / 5) + 1));
                Log.v("SKIP", skip + "");


            }
        });

        mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((skip / 5) > 0) {
                    getIncidenceListfromRequest();
                }
                skip -= lastCount + 5;
                if (skip < 0) {
                    skip = 0;
                    pag.setText("Pag " + String.valueOf(1));
                }else{
                    pag.setText("Pag " + String.valueOf((skip/5)+1));
                }
                Log.v("SKIP", skip + "");


            }
        });
    }
    private void fillCards(){
        isFirstTime = false;
        if (mLayoutManager != null) mLayoutManager = null;
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (mAdapter != null) mAdapter = null;
        mAdapter = new RecyclerV(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerViewOnClick();

    }

    private void getIncidenceListfromRequest(){
        MainActivity.incidenceList.clear();
        lastCount = 0;
        new IncidenceRequest() {
            @Override
            public void onSuccess(JSONObject json) {
                try {
                    JSONArray array = json.getJSONArray("incidencias");
                    lastCount = array.length();
                    if (array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {

                            JSONObject jsonIncidence = array.getJSONObject(i);
                            String incidenceId;
                            String title;
                            String date;
                            String pictureURL;
                            String location = null;
                            int category;
                            int status;
                            incidenceId = jsonIncidence.optString("_id");
                            title = jsonIncidence.optString("title");

                            date = jsonIncidence.optString("date");

                            Calendar dateaux = toCalendar(date);

                            if (!jsonIncidence.optString("street").equals("null")) {
                                location = jsonIncidence.optString("street");
                            } else {
                                location = "Calle Falsa 123";
                            }
                            category = jsonIncidence.optInt("category");
                            status = jsonIncidence.optInt("status");
                            pictureURL = jsonIncidence.optString("imageName");

                            Incidence inc = new Incidence(incidenceId, title, category, dateaux, pictureURL, location, status);
                            MainActivity.incidenceList.add(inc);

                            skip += 1;
                        }
                        fillCards();

                    }
                }catch(JSONException e){
                    e.printStackTrace();
                    new AlertDialog.Builder(IncidenceListActivity.this)
                            .setTitle("Lista de Consultas Vacía")
                            .setMessage("No hay ninguna consulta")
                            .setCancelable(false)
                            .setPositiveButton("Volver al menú principal", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(IncidenceListActivity.this, MainActivity.class));
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert).create().show();
                }
            }

            public Calendar toCalendar(final String iso8601string) {
                Calendar calendar = GregorianCalendar.getInstance();
                String s = iso8601string.replace("Z", "+00:00");
                 s = s.substring(0, 10);  // to get rid of the ":"
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(s);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendar.setTime(date);
                return calendar;
            }

            @Override
            public void onFailed(int error) {
                new AlertDialog.Builder(IncidenceListActivity.this)
                        .setTitle("Error " + error)
                        .setMessage("Hay un fallo de conexión con el servidor, inténtelo de nuevo más tarde.")
                        .setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert).create().show();
            }
        }.execute(String.valueOf(skip), String.valueOf(Utils.incidenceActionType));
    }

    private void RecyclerViewOnClick() {
        ((RecyclerV) mAdapter).setOnItemClickListener(new RecyclerV.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                card = position;
                if (Utils.incidenceActionType == 1) {
                    startActivity(new Intent(IncidenceListActivity.this, EditQuery.class));
                } else if (Utils.incidenceActionType == 2) {
                    new AlertDialog.Builder(IncidenceListActivity.this)
                            .setTitle("Borrar entrada")
                            .setMessage("Desea borrar la incidencia: " + MainActivity.incidenceList.get(card).getTitle() + "?")
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    JSONObject json = new JSONObject();
                                    final ProgressDialog loading = ProgressDialog.show(IncidenceListActivity.this, "Borrando incidencia", "Por favor espera unos segundos...", false, false);
                                    try {
                                        json.put("userId", Utils.getID());
                                        json.put("incidenceId", MainActivity.incidenceList.get(card).getIncidenceId());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    new DeleteIncidenceRequest() {
                                        @Override
                                        public void onSuccess() {
                                            ((RecyclerV) mAdapter).deleteItem(card);
                                            loading.dismiss();
                                            MainActivity.incidenceList.remove(card);

                                        }

                                        @Override
                                        public void onFailed(String error) {

                                        }
                                    }.execute(json.toString());

                                    //clearCardViews();
                                    //LoadCardViews();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert).create().show();
                } else if (Utils.incidenceActionType == 3) {
                    //Seguimiento

                    final Dialog dialog = new Dialog(IncidenceListActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                    dialog.setContentView(R.layout.incidence_tracing_dialog);
                    dialog.setTitle(MainActivity.incidenceList.get(card).getTitle());
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                    Button dialogButton = (Button) dialog.findViewById(R.id.buttonOk);
                    TextView tvLocation = (TextView) dialog.findViewById(R.id.incidenceLocationTextView);
                    TextView tvType = (TextView) dialog.findViewById(R.id.incidenceTypeTextView);
                    ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
                    TextView tvStatus = (TextView) dialog.findViewById(R.id.status_incidence_TV);
                    TextView tvDate = (TextView) dialog.findViewById(R.id.date_incidence_TV);

                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialogFullScreen = new Dialog(IncidenceListActivity.this, android.R.style.Theme_DeviceDefault_Light_DialogWhenLarge_NoActionBar);
                            dialogFullScreen.setContentView(R.layout.activity_image);
                            ImageView imageFull = (ImageView) dialogFullScreen.findViewById(R.id.imageFullScreen);
                            new DownloadImageTask(imageFull).execute("http://104.155.1.24:3131/incidencias/image/" + MainActivity.incidenceList.get(card).getPicture());
                            imageFull.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

                            imageFull.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogFullScreen.dismiss();
                                    //dialog.show();
                                }
                            });
                            dialogFullScreen.show();
                        }
                    });

                    String process = null;
                    switch (MainActivity.incidenceList.get(card).getStatus()) {
                        case 0:
                            process = IncidenceStatus.TRAMITE.toString();
                            tvStatus.setTextColor(Color.rgb(255, 145, 79));
                            break;
                        case 1:
                            process = IncidenceStatus.LEIDA.toString();
                            tvStatus.setTextColor(Color.rgb(79, 183, 255));
                            break;
                        case 2:
                            process = IncidenceStatus.PROCESO.toString();
                            tvStatus.setTextColor(Color.rgb(177, 79, 255));
                            break;
                        case 3:
                            process = IncidenceStatus.SOLUCIONADA.toString();
                            tvStatus.setTextColor(Color.rgb(0, 214, 42));
                            break;
                        case 4:
                            process = IncidenceStatus.DENEGADA.toString();
                            tvStatus.setTextColor(Color.RED);
                            break;
                        default:
                            process = IncidenceStatus.INDETERMINADA.toString();
                            tvStatus.setTextColor(Color.BLACK);
                            break;
                    }
                    new DownloadImageTask(image).execute("http://104.155.1.24:3131/incidencias/image/" + MainActivity.incidenceList.get(card).getPicture());
                    tvLocation.setText(MainActivity.incidenceList.get(card).getLocation());
                    Calendar cal = MainActivity.incidenceList.get(card).getDate();
                    tvDate.setText(cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR));
                    String categ = null;
                    switch (MainActivity.incidenceList.get(card).getCategory()) {
                        case 0:
                            categ = IncidenceCategory.DETERIORO.toString();
                            break;
                        case 1:
                            categ = IncidenceCategory.MOBILIARIO.toString();
                            break;
                        case 2:
                            categ = IncidenceCategory.ALUMBRADO.toString();
                            break;
                        case 3:
                            categ = IncidenceCategory.ALCANTARILLADO.toString();
                            break;
                        case 4:
                            categ = IncidenceCategory.LIMPIEZA.toString();
                            break;
                        case 5:
                            categ = IncidenceCategory.SEMAFOROS.toString();
                            break;
                        default:
                            categ = IncidenceStatus.INDETERMINADA.toString();
                            break;
                    }
                    tvType.setText(categ);
                    tvStatus.setText(process);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            }
        });
        //Indica la acción que tomará el OnClick de las CardViews
        //Posiciona un marker para cada parada de la línea
    }

    private ArrayList<Incidence> getDataSet() {
        ArrayList results = new ArrayList<>();
        if (MainActivity.incidenceList.size()==0){
            new AlertDialog.Builder(IncidenceListActivity.this)
                    .setTitle("Lista de Consultas Vacía")
                    .setMessage("No hay ninguna consulta")
                    .setCancelable(false)
                    .setPositiveButton("Volver al menú principal", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(IncidenceListActivity.this, MainActivity.class));
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert).create().show();
        }else {
            for (int index = 0; index < MainActivity.incidenceList.size(); index++) {
                results.add(index, MainActivity.incidenceList.get(index));
            }
        }
        return results;
        //consigue los datos de Linea
    }

    private void clearCardViews() {
        mAdapter = null;
        mRecyclerView.setAdapter(mAdapter);
    }
//Limpiar CardViews

    private void LoadCardViews() {
        mAdapter = new RecyclerV(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(IncidenceListActivity.this, MainActivity.class));
        finish();
    }
}
