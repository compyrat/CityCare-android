package com.albertribas_ericcaballero_albertmarlet.proyecto_final;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.albertribas_ericcaballero_albertmarlet.proyecto_final.model.MessagesRecyclerV;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.model.PushMessage;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class MessagesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        fillCards();
    }

    private void fillCards(){
        mRecyclerView = (RecyclerView) findViewById(R.id.MessagesRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MessagesRecyclerV(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerViewOnClick();
    }

    private ArrayList<PushMessage> getDataSet(){
        ArrayList<PushMessage> results = new ArrayList<>();
        for (int index = 0; index < MainActivity.pushMessagesList.size(); index++) {
            results.add(index, MainActivity.pushMessagesList.get(index));
        }
        return results;
    }

    private void RecyclerViewOnClick() {
        ((MessagesRecyclerV) mAdapter).setOnItemClickListener(new MessagesRecyclerV.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                String date = "25/05/2016";

//                Calendar dateaux  = toCalendar(date);
            }
        });
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

}
