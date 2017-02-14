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

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
/**
 * Created by admin on 1/31/2017.
 */
//will be implenting the calender here.

public class SignUp extends Fragment{

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;

    CheckBox termscheck;

    TextView terms;

    Button signUp;

    Spinner gender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sign_up_activity, container, false);

        firstName = (EditText) rootView.findViewById(R.id.firstnameSignUp);
        lastName = (EditText) rootView.findViewById(R.id.lastnameSignUp);
        email = (EditText) rootView.findViewById(R.id.emailSignUp);
        password = (EditText) rootView.findViewById(R.id.passwordSignUp);

        termscheck = (CheckBox) rootView.findViewById(R.id.termscheckBox);

        terms = (TextView) rootView.findViewById(R.id.terms);

        signUp = (Button) rootView.findViewById(R.id.createAccountButton);


        gender =(Spinner) rootView.findViewById(R.id.gender_spinner);


        return rootView;
    }





    }

