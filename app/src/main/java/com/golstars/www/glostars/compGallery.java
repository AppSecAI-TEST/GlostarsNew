package com.golstars.www.glostars;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.github.clans.fab.FloatingActionMenu;
import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.adapters.CompetitionData;
import com.golstars.www.glostars.adapters.PostAdapter;
import com.golstars.www.glostars.network.NotificationService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class compGallery extends Fragment {


    // --------------- recycler view settings ---------
    private ArrayList<Hashtag> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostAdapter mAdapter;
    //-------------------------------------------------
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    GridLayoutManager layoutManager;
    MyUser mUser;
    int pg = 1;
    private Intent homeIntent;


    private ArrayList<Hashtag> gridImages;

    private CompetitionData compAdapt;
    private ArrayList<Hashtag> compPicsUrls;

    Integer unseenNotifs = 0;


    PullRefreshLayout layout;


    RecyclerView gallery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_comp_gallery, container, false);


        gallery = (RecyclerView)rootView.findViewById(R.id.gallerygrid);


            layout = (PullRefreshLayout)rootView.findViewById(R.id.pullRefreshLayout);
            layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    try {
                        Intent intent = getActivity().getIntent();
                        getActivity().finish();
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });




            //---------------NETWORK AND RECYCLER VIEW --------------------------------
            //recyclerView = (RecyclerView) findViewById(R.id.mainfeedrecycler);

            compPicsUrls = new ArrayList<>();

            gridImages = new ArrayList<>();



            compAdapt = new CompetitionData(getActivity(), compPicsUrls,getFragmentManager());
            //compAdapt = new RecyclerGridAdapter(this, gridImages, this);

            int numOfColumns = 3;
            layoutManager = new GridLayoutManager(getActivity(), numOfColumns);
            gallery.setLayoutManager(layoutManager);
            gallery.setAdapter(compAdapt);

            //new getUserData().execute("");
        /*mUser.setContext(this);
        homeIntent = new Intent();
        homeIntent.putExtra("USER_ID",mUser.getUserId());
        homeIntent.setClass(getApplicationContext(),user_profile.class);*/



            gallery.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    //super.onScrolled(recyclerView, dx, dy);
                    System.out.println("Scrolling "+dx+" "+dy);
                    if(dy > 0){ //check for scroll down
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                        System.out.println("Total Item "+totalItemCount+" Loading "+loading);
                        if(!loading){
                            if((visibleItemCount + pastVisiblesItems) >= totalItemCount-2){
                                loading = true;
                                //pg++;
                                try {
                                    //callAsyncPopulate(pg);
                                    if(mUser == null){
                                        new getUserData().execute("");
                                    } else{
                                        load(false);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //populateFeed(mUser.getUserId(), pg, mUser.getToken());
                            }
                        }
                    }
                }


            });
            if(mUser == null){
                new getUserData().execute("");
            } else{
                load(false);
            }

//        if(!isConnected()){
//            startActivity(new Intent(this, noInternet.class));
//        }

        return rootView;


        }
    /*
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    } */

    public boolean isConnected(){
        boolean hasConnection;
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        hasConnection = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return hasConnection;

    }


    public void load(boolean fromRecursive){
        if(fromRecursive && !(pg<5))
            return;
        loading=true;
        String url = ServerInfo.BASE_URL_API+"/images/competition/" + pg;

        System.out.println(url);

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


        client.get(getActivity(), url,new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    System.out.println("1. "+response.toString());
                    Gson gson=new Gson();
                    ArrayList<Hashtag> getAllPost=gson.fromJson(response.getJSONArray("resultPayload").toString(), new TypeToken<ArrayList<Hashtag>>(){}.getType());




                    compPicsUrls.addAll(getAllPost);
                    System.out.println("Total Post "+postList.size());
                    compAdapt.notifyDataSetChanged();
                    loading=false;
                    pg++;
                    System.out.println("Loading complete");

                    if(pg<=5){
                        load(true);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }





    private class getUserData extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            mUser = MyUser.getmUser();
            mUser.setContext(getActivity().getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject object) {


            homeIntent = new Intent();
            homeIntent.putExtra("USER_ID",mUser.getUserId());
            homeIntent.setClass(getActivity().getApplicationContext(),user_profile.class);
            //getUnseen();
            load(false);


        }
    }

   /* public void callAsyncPopulate(Integer pg, String token) throws Exception{
        JSONObject data = new JSONObject();
        data.put("pg", pg);
        data.put("token", token);
        new populatePageAsync().execute(data);

    }*/


    /*private class populatePageAsync extends AsyncTask<JSONObject, Integer, JSONArray>{

        @Override
        protected JSONArray doInBackground(JSONObject... jsonObjects) {
            PictureService pictureService = new PictureService();
            JSONArray data = null;
            try {
                pictureService.getCompetitionPictures(jsonObjects[0].getInt("pg"), jsonObjects[0].getString("token"));
                while (data == null){
                    data = pictureService.getData();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(JSONArray data) {

            for(int i = 0; i < data.length(); i++){


                try{

                    JSONObject poster = data.getJSONObject(i).getJSONObject("poster");
                    JSONObject pic = data.getJSONObject(i);
                    String name = poster.getString("name");
                    String usrId = poster.getString("userId");
                    String profilePicUrl = poster.getString("profilePicURL");
                    String id = pic.getString("id");
                    String description = pic.getString("description");
                    String picURL = pic.getString("picUrl");

                    Boolean isFeatured = Boolean.valueOf(pic.getString("isfeatured"));
                    Boolean isCompeting = Boolean.valueOf(pic.getString("isCompeting"));
                    Integer starsCount = Integer.parseInt(pic.getString("starsCount"));
                    System.out.println("POSTER: " + name + " " + usrId + " " + id + " " + description + " " + picURL + " " + isFeatured + " " + isCompeting + " " + starsCount);

                    JSONArray ratings = pic.getJSONArray("ratings");
                    JSONArray comments = pic.getJSONArray("comments");

                    String uploaded = pic.getString("uploaded");
                    *//*String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
                    LocalDateTime localDateTime = LocalDateTime.parse(uploaded, DateTimeFormat.forPattern(pattern));*//*
                    String interval = Timestamp.getInterval(Timestamp.getOwnZoneDateTime(uploaded));

                    setmAdapter(name, usrId, id, description, picURL, profilePicUrl , isFeatured, isCompeting, starsCount, comments.length(), ratings, comments, interval);

                } catch (Exception e){
                    e.printStackTrace();
                }



            }

            if(!loading){
                loading = true;
            }

            if(pg == 1){
                pg++;
                try {
                    callAsyncPopulate(pg, mUser.getToken());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }*/


   /* private void setmAdapter(String author, String usr, String photoID, String description, String picURL, String profilePicURL, Boolean isFeatured, Boolean isCompeting, Integer starsCount,
                             Integer commentCount, JSONArray ratings, JSONArray comments, String uploaded){
        if(description == "null"){
            description = "";
        }
        Hashtag hashtag = new Hashtag(author, usr, photoID, description, picURL, profilePicURL, isFeatured, isCompeting, starsCount, commentCount);
        post.setComments(comments);
        post.setRatings(ratings);
        post.setUploaded(uploaded);
        gridImages.add(post);
        compPicsUrls.add(post.getPicURL());
        compAdapt.notifyDataSetChanged();
    }*/



    public ArrayList<Hashtag> getAllData() {
        return compPicsUrls;
    }


    public RecyclerView.Adapter getAdapter() {
        return compAdapt;



    }

}
