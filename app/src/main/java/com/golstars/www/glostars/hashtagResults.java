package com.golstars.www.glostars;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.ModelData.Poster;
import com.golstars.www.glostars.adapters.PostData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class hashtagResults extends AppCompatActivity {

    RecyclerView hashtags;

    ArrayList<Hashtag> posts=new ArrayList<Hashtag>();
    PostData postDataAdapter;
    int pg=1;
    MyUser mUser = MyUser.getmUser();
    private boolean loading=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Hashtags"); //Changing the activity label here.
        setContentView(R.layout.activity_hashtag_results);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int Height = displayMetrics.heightPixels;
        int Width = displayMetrics.widthPixels;


        hashtags = (RecyclerView)findViewById(R.id.hashtagrecycler);
        postDataAdapter=new PostData(posts,hashtagResults.this,Width);
        getPostData(getIntent().getStringExtra("data"));


        LinearLayoutManager horizontalLayoutManagaer= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        hashtags.setLayoutManager(horizontalLayoutManagaer);
        hashtags.setAdapter(postDataAdapter);



    }

    private void getPostData(String data) {
        //posts.clear();
        loading=true;
        String url = ServerInfo.BASE_URL_API+"images/HashTagPhoto?searchTag="+data+"&count="+pg;

        AsyncHttpClient client=new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + mUser.getToken());
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);
        }
        catch (Exception e) {}

        client.get(getApplicationContext(), url,new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    System.out.println("1. "+response.toString());
                    Gson gson=new Gson();
                    ArrayList<Hashtag> getAllPost=gson.fromJson(response.getJSONArray("resultPayload").toString(), new TypeToken<ArrayList<Hashtag>>(){}.getType());
                    posts.addAll(getAllPost);
                    System.out.println("Total Post "+posts.size());
                    postDataAdapter.notifyDataSetChanged();
                    loading=false;
                    pg++;
                    System.out.println("Loading complete");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });


    }
}
