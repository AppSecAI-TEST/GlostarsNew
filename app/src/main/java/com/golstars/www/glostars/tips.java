package com.golstars.www.glostars;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class tips extends Fragment {

    TextView a;
    TextView b;
    TextView c;
    TextView d;
    TextView e;
    TextView f;
    TextView g;
    TextView h;
    TextView i;
    TextView j;
    TextView k;
    TextView ll;
    TextView m;
    TextView n;
    TextView o;
    TextView p;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_tips, container, false);


        a = (TextView)rootView.findViewById(R.id.a);
        b = (TextView)rootView.findViewById(R.id.b);
        c = (TextView)rootView.findViewById(R.id.c);
        d = (TextView)rootView.findViewById(R.id.d);
        e = (TextView)rootView.findViewById(R.id.e);
        f = (TextView)rootView.findViewById(R.id.f);
        g = (TextView)rootView.findViewById(R.id.g);
        h = (TextView)rootView.findViewById(R.id.h);
        i = (TextView)rootView.findViewById(R.id.i);
        j = (TextView)rootView.findViewById(R.id.j);
        k = (TextView)rootView.findViewById(R.id.k);
        ll = (TextView)rootView.findViewById(R.id.ll);
        m = (TextView)rootView.findViewById(R.id.m);
        n = (TextView)rootView.findViewById(R.id.n);
        o = (TextView)rootView.findViewById(R.id.o);
        p = (TextView)rootView.findViewById(R.id.p);


        final Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Ubuntu-Light.ttf");
        final Typeface bold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Ubuntu-Bold.ttf");

        a.setTypeface(bold);
        b.setTypeface(type);
        c.setTypeface(bold);
        d.setTypeface(type);
        e.setTypeface(bold);
        f.setTypeface(type);
        g.setTypeface(bold);
        h.setTypeface(type);
        i.setTypeface(bold);
        j.setTypeface(type);
        k.setTypeface(bold);
        ll.setTypeface(type);
        m.setTypeface(bold);
        n.setTypeface(type);
        o.setTypeface(bold);
        p.setTypeface(type);


        return rootView;
    }
}
