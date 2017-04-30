package com.golstars.www.glostars.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.golstars.www.glostars.models.NotificationObj;
import com.golstars.www.glostars.interfaces.OnItemClickListener;
import com.golstars.www.glostars.R;

import java.util.List;

/**
 * Created by edson on 22/02/17.
 * Notifications adapter class
 */

//TODO:FINISH NOTIFICATIONS ADAPTER

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    public List<NotificationObj> notfications;
    public Context context;
    private final OnItemClickListener listener;
    private final OnItemClickListener listener2;

    public NotificationAdapter(List<NotificationObj> nots, Context context, OnItemClickListener listener, OnItemClickListener listener2 ){
        this.notfications = nots;
        this.context = context;
        this.listener = listener;
        this.listener2 = listener2;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView propic, postpreview;
        public TextView name, surname, notiBanner, min, hour, timenumber;

        public MyViewHolder(View itemView, final OnItemClickListener listener, final OnItemClickListener listener2) {
            super(itemView);
            propic = (ImageView)itemView.findViewById(R.id.propicNOTI);
            postpreview = (ImageView)itemView.findViewById(R.id.postpreviewNOTI);
            name = (TextView)itemView.findViewById(R.id.nameNOTI);
            surname = (TextView)itemView.findViewById(R.id.surnameNOTI);
            notiBanner = (TextView)itemView.findViewById(R.id.notiBanner);
            min = (TextView)itemView.findViewById(R.id.minbannerNOTI);
            hour = (TextView)itemView.findViewById(R.id.hourbannerNOTI);
            timenumber = (TextView)itemView.findViewById(R.id.timeNOTI);

            Typeface type = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Ubuntu-Light.ttf");
            name.setTypeface(type);
            notiBanner.setTypeface(type);
            min.setTypeface(type);
            hour.setTypeface(type);
            timenumber.setTypeface(type);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClickNotif(notfications.get(getLayoutPosition()));
                }
            });

            propic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener2.onItemClickNotif(notfications.get(getLayoutPosition()));
                }
            });

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener2.onItemClickNotif(notfications.get(getLayoutPosition()));
                }
            });

            surname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener2.onItemClickNotif(notfications.get(getLayoutPosition()));
                }
            });

        }


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notificationmodel, parent, false);

        return new MyViewHolder(view, listener, listener2);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NotificationObj notifcation = notfications.get(position);
        Glide.with(context).load(notifcation.getProfilePicURL()).into(holder.propic);
        Glide.with(context).load(notifcation.getPicURL()).into(holder.postpreview);
        holder.name.setText(notifcation.getName());
        holder.notiBanner.setText(notifcation.getDescription());
        holder.surname.setText("");
        holder.timenumber.setText(notifcation.getDate());
        holder.min.setText("");
        holder.hour.setText("");
        if (notifcation.getSeen().equals(false)){
            holder.itemView.setBackgroundColor(Color.parseColor("#D0D0D0"));
        }


    }

    @Override
    public int getItemCount() {
        return notfications.size();
    }


}
