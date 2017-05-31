package com.golstars.www.glostars;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class notificationNotificationNew extends Fragment {

    RecyclerView notificationNew;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_notification_notification_new, container, false);

        notificationNew = (RecyclerView)rootView.findViewById(R.id.notificationRecyclerNew);

        return rootView;
    }
}
