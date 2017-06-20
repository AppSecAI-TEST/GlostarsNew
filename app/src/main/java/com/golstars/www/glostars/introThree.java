package com.golstars.www.glostars;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class introThree extends Fragment {


    Button gettingStarted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_intro_three, container, false);

        gettingStarted = (Button)rootView.findViewById(R.id.getStarted);


        gettingStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),searchResults.class));
            }
        });

        return rootView;
    }

}
