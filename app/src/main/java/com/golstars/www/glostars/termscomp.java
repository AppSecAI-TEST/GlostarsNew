package com.golstars.www.glostars;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class termscomp extends Fragment {

    TextView header;
    TextView terms;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_termscomp, container, false);

        header = (TextView)rootView.findViewById(R.id.please);
        terms = (TextView)rootView.findViewById(R.id.termstext);


        final Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Ubuntu-Light.ttf");
        final Typeface bold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Ubuntu-Bold.ttf");

        header.setTypeface(bold);
        terms.setTypeface(type);


        return rootView;
    }
}
