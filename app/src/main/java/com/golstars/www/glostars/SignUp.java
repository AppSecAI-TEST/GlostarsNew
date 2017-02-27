package com.golstars.www.glostars;

import android.app.DatePickerDialog;
import android.graphics.Typeface;
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
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 1/31/2017.
 */
//will be implenting the calender here.

public class SignUp extends Fragment{

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText YYYY;
    EditText MM;
    EditText DD;



    TextView birth;
    CheckBox termscheck;

    TextView terms;
    TextView signupbanner;

    Button signUp;

    Spinner gender;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sign_up_activity, container, false);



        firstName = (EditText) rootView.findViewById(R.id.firstnameSignUp);
        lastName = (EditText) rootView.findViewById(R.id.lastnameSignUp);
        email = (EditText) rootView.findViewById(R.id.emailSignUp);
        password = (EditText) rootView.findViewById(R.id.passwordSignUp);

        signupbanner =(TextView)rootView.findViewById(R.id.signupbanner);

        termscheck = (CheckBox) rootView.findViewById(R.id.termscheckBox);

        terms = (TextView) rootView.findViewById(R.id.terms);

        signUp = (Button) rootView.findViewById(R.id.createAccountButton);

        YYYY = (EditText)rootView.findViewById(R.id.YYYY);
        MM= (EditText)rootView.findViewById(R.id.MM);
        DD = (EditText)rootView.findViewById(R.id.DD);

        birth = (TextView)rootView.findViewById(R.id.birthbanner);


        gender =(Spinner) rootView.findViewById(R.id.gender_spinner);


        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Ubuntu-Light.ttf");
        firstName.setTypeface(type);
        lastName.setTypeface(type);
        email.setTypeface(type);
        termscheck.setTypeface(type);
        terms.setTypeface(type);
        signUp.setTypeface(type);
        YYYY.setTypeface(type);
        MM.setTypeface(type);
        DD.setTypeface(type);
        birth.setTypeface(type);
        signupbanner.setTypeface(type);


        signUp.setTransformationMethod(null);



        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usrname = email.getText().toString();
                String email = usrname;
                String name = firstName.getText().toString();
                Integer bdayY = 0;
                Integer bdayM = 0;
                Integer bdayD = 0;
                String genderSelected = gender.getSelectedItem().toString();
                String lastname = lastName.getText().toString();
                String pwd = password.getText().toString();

                if(termscheck.isChecked()){
                    try {
                        createAccount(usrname, email, name, bdayY, bdayM, bdayD, genderSelected, lastname, pwd);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "You have to accept the terms and conditions to create an account", Toast.LENGTH_LONG).show();
                }


            }
        });

        return rootView;


    }



    public void createAccount(String username, String email, String name, Integer bdayY, Integer bdayM, Integer bdayD, String gender, String lastname, String password) throws IOException {
        URL url = new URL("http://www.glostars.com/api/account/register");
        String postMessage =    "{'UserName':" + username +
                ",'Email':" + email +
                ",'Name':" + name +
                ",'BirthdayYear':" + bdayY +
                ",'BirthdayMonth':" + bdayM +
                ",'BirthdayDay':" + bdayD +
                ",'Gender':" + gender +
                ",'LastName':" + lastname +
                ",'Password':" + password + "}";
        System.out.println(postMessage);


        RequestBody body = RequestBody.create(JSON, postMessage);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                System.out.println(response.body().string());

            }
        });


    }





    }

