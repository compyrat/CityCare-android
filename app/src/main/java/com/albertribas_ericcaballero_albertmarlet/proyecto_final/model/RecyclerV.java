package com.albertribas_ericcaballero_albertmarlet.proyecto_final.model;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.albertribas_ericcaballero_albertmarlet.proyecto_final.R;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Request.DownloadImageTask;
import com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by albertribgar on 12/05/2016.
 */
public class RecyclerV extends RecyclerView.Adapter<RecyclerV.DataObjectHolder> {
    private static String LOG_TAG = "RecyclerVAdapter";
    private ArrayList<Incidence> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        ImageView photo_IV;
        TextView title_TV;
        TextView date_TV;
        TextView status_TV;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title_TV = (TextView) itemView.findViewById(R.id.cardview_title);
            date_TV = (TextView) itemView.findViewById(R.id.cardview_date);
            status_TV = (TextView) itemView.findViewById(R.id.cardview_status);
            photo_IV = (ImageView) itemView.findViewById(R.id.cardview_placeholder);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public RecyclerV(ArrayList<Incidence> myDataset) {
        mDataset = myDataset;
    }

    public void addIncidences(ArrayList<Incidence> myDataset){
        for (int index = 0; index < myDataset.size(); index++) {
            mDataset.add(myDataset.get(index));
        }

    }
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.title_TV.setText(mDataset.get(position).getTitle());
        Calendar cal = mDataset.get(position).getDate();
        holder.date_TV.setText(cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.YEAR));
        String process;
        switch (mDataset.get(position).getStatus()) {
            case 0:
                process = IncidenceStatus.TRAMITE.toString();
                holder.status_TV.setTextColor(Color.rgb(255, 145, 79));
                break;
            case 1:
                process = IncidenceStatus.LEIDA.toString();
                holder.status_TV.setTextColor(Color.rgb(79, 183, 255));
                break;
            case 2:
                process = IncidenceStatus.PROCESO.toString();
                holder.status_TV.setTextColor(Color.rgb(177, 79, 255));
                break;
            case 3:
                process = IncidenceStatus.SOLUCIONADA.toString();
                holder.status_TV.setTextColor(Color.rgb(0, 214, 42));
                break;
            case 4:
                process = IncidenceStatus.DENEGADA.toString();
                holder.status_TV.setTextColor(Color.RED);
                break;
            default:
                process = IncidenceStatus.INDETERMINADA.toString();
                holder.status_TV.setTextColor(Color.BLACK);
                break;
        }
        holder.status_TV.setText(process);
        new DownloadImageTask(holder.photo_IV).execute(Utils.URL+"/incidencias/image/"+mDataset.get(position).getPicture());
    }

    public void addItem(Incidence dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}
