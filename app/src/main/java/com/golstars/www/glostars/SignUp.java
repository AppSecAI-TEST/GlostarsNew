package com.golstars.www.glostars;

import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
/**
 * Created by admin on 1/31/2017.
 */
//will be implenting the calender here.

public class SignUp extends Fragment{

    Button btn ;
    int yearX , monthX, dayX;
    static final int DIALOG_ID = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sign_up_activity, container, false);
        btn = (Button) rootView.findViewById(R.id.date);

        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                    }
                }
        );


        return rootView;
    }



    }

