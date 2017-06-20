package com.golstars.www.glostars;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Looper;
import android.os.Message;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.golstars.www.glostars.network.Signupnet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
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

    public Spinner gender;
    String pWd = "";

    private static final MediaType JSONType = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType txtType = MediaType.parse("text/plain; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();
    private android.os.Handler hander;



    private static final String MyPREFERENCES = "glostarsPrefs";
    Auth auth;

    private String[] genderEntries;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sign_up_activity, container, false);

        this.genderEntries = new String[] {
                "Male","Female"
        };

        ArrayAdapter<String> genderItems = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,genderEntries);
        //gender.setAdapter(genderItems);

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


        hander = new android.os.Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(getContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
            }
        };

        signUp.setTransformationMethod(null);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), termsOfUse.class));
            }
        });


        terms.setPaintFlags(terms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String log_email = email.getText().toString();
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

                int year_obirth = 0;
                int month_obirth = 0;
                int day_obirth = 0;


                if(bdayD.isEmpty() || bdayM.isEmpty() || bdayD.isEmpty()) {
                    Toast.makeText(getContext(), "Enter a valid birth date", Toast.LENGTH_LONG).show();
                } else{
                    year_obirth = Integer.parseInt(bdayY);
                    month_obirth = Integer.parseInt(bdayM);
                    day_obirth = Integer.parseInt(bdayD);

                }

                int this_year = Calendar.getInstance().get(Calendar.YEAR);

                if(log_email.indexOf('@') < 0){
                    Toast.makeText(getContext(), "Enter a valid email", Toast.LENGTH_LONG).show();

                } else if(log_email.length() <= 1){
                    Toast.makeText(getContext(), "Enter a valid email", Toast.LENGTH_LONG).show();
                } else if(log_email.indexOf('.') < 0) {
                    Toast.makeText(getContext(), "Enter a valid email", Toast.LENGTH_LONG).show();
                } else if(log_email.charAt(log_email.indexOf('@') + 1) == '.'){
                    Toast.makeText(getContext(), "Enter a valid email", Toast.LENGTH_LONG).show();
                } else if(log_email.indexOf('@') == 0){
                    Toast.makeText(getContext(), "Enter a valid email", Toast.LENGTH_LONG).show();
                } else if(log_email.indexOf('.') == log_email.length() -1){
                    Toast.makeText(getContext(), "Enter a valid email", Toast.LENGTH_LONG).show();
                }
                else{

                    /************ checking birthday date *******************/

                    if((year_obirth < 1900) && (year_obirth >= this_year)){
                        Toast.makeText(getContext(), "Enter a valid birth date", Toast.LENGTH_LONG).show();
                    } else if((month_obirth < 1) && (month_obirth > 12)){
                        Toast.makeText(getContext(), "Enter a valid birth date", Toast.LENGTH_LONG).show();
                    } else if(day_obirth < 1){
                        Toast.makeText(getContext(), "Enter a valid birth date", Toast.LENGTH_LONG).show();
                    } else if((month_obirth == 2) && (day_obirth > 28)){
                        Toast.makeText(getContext(), "Enter a valid birth date", Toast.LENGTH_LONG).show();
                    } else if(((month_obirth == 4) || (month_obirth == 6) || (month_obirth == 7) || (month_obirth == 11)) && (day_obirth > 30)){
                        Toast.makeText(getContext(), "Enter a valid birth date", Toast.LENGTH_LONG).show();
                    } else if((day_obirth > 31)){
                        Toast.makeText(getContext(), "Enter a valid birth date", Toast.LENGTH_LONG).show();
                    } else {
                        startActivity(new Intent(getActivity(),inputCode.class));
                    }









                }







//
//                if(termscheck.isChecked()){
//                    if(name.isEmpty()){
//
//                        Toast.makeText(getContext(), "'First Name' field is obligatory", Toast.LENGTH_LONG).show();
//                    }else if(email.isEmpty()){
//
//                        Toast.makeText(getContext(), "'Email' field is obligatory", Toast.LENGTH_LONG).show();
//                    }else if(pwd.isEmpty()){
//
//                        Toast.makeText(getContext(), "'Password' field is obligatory", Toast.LENGTH_LONG).show();
//                    }else{
//
//                        try {
//                            createAccount(usrname, email, name, bdayY, bdayM, bdayD, genderSelected, lastname, pwd);
//                            JSONObject c = null;
//                            while (c == null){
//                                c = getData();
//                            }
//                            if(c.getInt("responseCode") == 1){
//                                Toast.makeText(getContext(), c.getString("message"), Toast.LENGTH_LONG).show();
//                                //login("password", password.getText().toString(), c.getJSONObject("resultPayload").getString("email"));
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//
//
//                } else {
//                    Toast.makeText(getActivity(), "You have to accept the terms and conditions to create an account", Toast.LENGTH_LONG).show();
//                }


            }
        });

        return rootView;


    }



    public void createAccount(final String username, String email, String name, String bdayY, String bdayM, String bdayD, String gender, String lastname, final String password) throws Exception {
        URL url = new URL("https://www.glostars.com/api/account/register");
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

        /*
        StringEntity entity = null;
        entity = new StringEntity(msg.toString());

        Signupnet.signupService(getContext(), entity, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("SUCCESS!");
                Toast.makeText(getContext(), "SUCCESS!", Toast.LENGTH_LONG).show();
                try {
                    login("password", password, username);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("SIGNUP FAIL");
            }
        }); */

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
                Message msg = hander.obtainMessage(1,"Servers currently unavailable");
                msg.sendToTarget();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    Message msg = hander.obtainMessage(1,"this user already exists");
                    msg.sendToTarget();

                    JSONObject newUser = new JSONObject();
                    try {
                        newUser.put("responseCode", 0);
                        setData(newUser);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    System.out.println("ERROR AT CREATE ACCOUNT");
                    throw new IOException("Unexpected code " + response);
                }

                Message msge = hander.obtainMessage(1,"user created");
                msge.sendToTarget();

                String msg = response.body().string();
                System.out.println(msg);

                try {
                    JSONObject newUser = new JSONObject(msg);
                    //Toast.makeText(getContext(), newUser.getString("message"), Toast.LENGTH_LONG).show();
                    setData(newUser);
                    login("password", pWd , newUser.getJSONObject("resultPayload").getString("email"));



                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


    }

    public void login(String grantType, String password, String username) throws Exception{

        URL url = new URL("https://www.glostars.com/Token");
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

        client.newCall(request).enqueue( new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Message msg = hander.obtainMessage(1,"Servers currently unavailable");
                msg.sendToTarget();

                //Toast.makeText(getContext(), "Servers currently unavailable", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    Message msg = hander.obtainMessage(1,"Login or password incorrect");
                    msg.sendToTarget();

                    //Toast.makeText(getContext(), "Login or password incorrect", Toast.LENGTH_LONG).show();
                    throw new IOException("Unexpected code " + response);

                }
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

//                    login.setBackgroundResource(R.drawable.roundedbutton1);
//                    login.setTextColor(getResources().getColor(R.color.colorPrimary));

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

