package com.albertribas_ericcaballero_albertmarlet.proyecto_final.model;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.albertribas_ericcaballero_albertmarlet.proyecto_final.R;

import java.util.ArrayList;

/**
 * Created by albertribgar on 25/05/2016.
 */
public class MessagesRecyclerV extends RecyclerView.Adapter<MessagesRecyclerV.MessagesHolder> {
    private static String LOG_TAG = "messagesRecyclerVAdapter";
    private ArrayList<PushMessage> mDataset = new ArrayList<>();
    private static MyClickListener myClickListener;

    public static class MessagesHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView subject_TV;
        TextView calendar_TV;

        public MessagesHolder(View itemView) {
            super(itemView);
            subject_TV = (TextView) itemView.findViewById(R.id.message_title);
            calendar_TV = (TextView) itemView.findViewById(R.id.message_date);

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

    public MessagesRecyclerV(ArrayList<PushMessage> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public MessagesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_messages, parent, false);

        MessagesHolder dataObjectHolder = new MessagesHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(MessagesHolder holder, int position) {
        holder.subject_TV.setText(mDataset.get(position).getSubject());
        //Calendar cal = mDataset.get(position).getCalendar();
        //holder.calendar_TV.setText(cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR));
        holder.calendar_TV.setText(mDataset.get(position).getCalendar());
    }

    public void addItem(PushMessage dataObj, int index) {
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
