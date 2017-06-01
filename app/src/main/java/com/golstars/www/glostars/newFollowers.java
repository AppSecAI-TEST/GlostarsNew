package com.golstars.www.glostars;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class newFollowers extends Fragment {

    ListView newfollowers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_new_followers, container, false);

        newfollowers = (ListView)rootView.findViewById(R.id.newfollowerList);

        return rootView;
    }
}
