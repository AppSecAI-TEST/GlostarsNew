package com.golstars.www.glostars;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionMenu;

public class compGallery extends Fragment {


    com.github.clans.fab.FloatingActionButton cameraFAB;
    com.github.clans.fab.FloatingActionButton competitionFAB;
    com.github.clans.fab.FloatingActionButton profileFAB;
    com.github.clans.fab.FloatingActionButton notificationFAB;
    com.github.clans.fab.FloatingActionButton homeFAB;

    FloatingActionMenu menuDown;
    RecyclerView gallery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_comp_gallery, container, false);



        gallery = (RecyclerView)rootView.findViewById(R.id.gallerygrid);



        cameraFAB =(com.github.clans.fab.FloatingActionButton)rootView.findViewById(R.id.cameraFAB);
        competitionFAB = (com.github.clans.fab.FloatingActionButton)rootView.findViewById(R.id.competitionFAB);
        profileFAB = (com.github.clans.fab.FloatingActionButton)rootView.findViewById(R.id.profileFAB);
        notificationFAB = (com.github.clans.fab.FloatingActionButton)rootView.findViewById(R.id.notificationFAB);
        homeFAB = (com.github.clans.fab.FloatingActionButton)rootView.findViewById(R.id.homeFAB);

        menuDown = (FloatingActionMenu)rootView.findViewById(R.id.menu_down);
        menuDown.setClosedOnTouchOutside(true);



        return rootView;



    }
}
