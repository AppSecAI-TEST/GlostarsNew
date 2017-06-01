package com.golstars.www.glostars;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class newFollowing extends Fragment {

    ListView newfollowing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_new_following, container, false);

        newfollowing = (ListView)rootView.findViewById(R.id.newfollowingList);

        return rootView;
    }
}
