package com.golstars.www.glostars;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by admin on 1/31/2017.
 */

public class LoginActivity extends Fragment {



    private EditText email;
    private EditText password;
    private Button login;
    private TextView signup;
    private TextView forgotpass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_activity, container, false);

        email = (EditText) rootView.findViewById(R.id.emailEditText);
        password = (EditText) rootView.findViewById(R.id.passwordEditText);
        login = (Button) rootView.findViewById(R.id.logInButton);
        signup = (TextView) rootView.findViewById(R.id.signUp);
        forgotpass = (TextView) rootView.findViewById(R.id.forgotPass);




        return rootView;
    }

    //You can work with them now using the objects like :
    // email.getText().... blah blah blah




}
