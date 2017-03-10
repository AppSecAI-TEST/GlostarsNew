package com.golstars.www.glostars;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

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

    JSONObject data = null;



    TextView birth;
    CheckBox termscheck;

    TextView terms;
    TextView signupbanner;

    Button signUp;

    Spinner gender;
    String pWd = "";

    private static final MediaType JSONType = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType txtType = MediaType.parse("text/plain; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();


    private static final String MyPREFERENCES = "glostarsPrefs";
    Auth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sign_up_activity, container, false);

        auth = new Auth(getActivity().getApplicationContext());


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

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), termsOfUse.class));
            }
        });



        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usrname = email.getText().toString();
                String email = usrname;
                String name = firstName.getText().toString();
                String bdayY = YYYY.getText().toString() ;
                String bdayM = MM.getText().toString();
                String bdayD = DD.getText().toString();
                String genderSelected = gender.getSelectedItem().toString();
                String lastname = lastName.getText().toString();
                String pwd = password.getText().toString();
                pWd = pwd;

                if(termscheck.isChecked()){
                    try {
                        createAccount(usrname, email, name, bdayY, bdayM, bdayD, genderSelected, lastname, pwd);
                        JSONObject c = null;
                        while (c == null){
                            c = getData();
                        }
                        if(c.getInt("responseCode") == 1){
                            Toast.makeText(getContext(), c.getString("message"), Toast.LENGTH_LONG).show();
                            login("password", password.getText().toString(), c.getJSONObject("resultPayload").getString("email"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "You have to accept the terms and conditions to create an account", Toast.LENGTH_LONG).show();
                }


            }
        });

        return rootView;


    }



    public void createAccount(String username, String email, String name, String bdayY, String bdayM, String bdayD, String gender, String lastname, final String password) throws Exception {
        URL url = new URL("http://www.glostars.com/api/account/register");
        JSONObject msg = new JSONObject();
        msg.put("UserName", username);
        msg.put("Email", email);
        msg.put("Name", name);
        msg.put("BirthdayYear", bdayY);
        msg.put("BirthdayMonth", bdayM);
        msg.put("BirthdayDay", bdayD);
        msg.put("Gender", gender);
        msg.put("LastName", lastname);
        msg.put("Password", password);

        System.out.println(msg);


        RequestBody body = RequestBody.create(JSONType, msg.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                JSONObject newUser = new JSONObject();
                try {
                    newUser.put("responseCode", 0);
                    setData(newUser);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String msg = response.body().string();
                System.out.println(msg);

                try {
                    JSONObject newUser = new JSONObject(msg);
                    //Toast.makeText(getContext(), newUser.getString("message"), Toast.LENGTH_LONG).show();
                    setData(newUser);
                    //login("password", pWd ,newUser.getJSONObject("resultPayload").getString("email"));

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


    }

    public void login(String grantType, String password, String username) throws Exception{

        URL url = new URL("http://www.glostars.com/Token");
        /*
        String postMessage = "{'grant_type':" + "password," +
                             "'password':" + "91113603," +
                             "'username':" + "netosilvan@hotmail.com" + "}";
        */
        String postMessage = "username=" + username +
                "&password="+ password +
                "&grant_type=" + grantType;

        RequestBody body =  RequestBody.create(txtType, postMessage);

        System.out.println(body);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                //TODO: CREATE A DIALOG FOR FAILED LOGIN


                /*Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }*/
                String authData = response.body().string();
                System.out.println(authData);


                try {
                    JSONObject authObject = new JSONObject(authData);
                    auth.setUsername(authObject.getString("userName"));
                    auth.setAcessToken(authObject.getString("access_token"));
                    auth.setExpires(authObject.getString(".expires"));
                    auth.setIssued(authObject.getString(".issued"));
                    //auth.isTokenValid();

                    startActivity(new Intent(getActivity(), MainFeed.class));
                    getActivity().finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    public void setData(JSONObject data){
        this.data = data;
    }

    public JSONObject getData(){
        return data;
    }





    }

