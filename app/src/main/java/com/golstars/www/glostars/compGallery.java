package com.golstars.www.glostars;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.golstars.www.glostars.DatabaseModel.CompetionRealm;
import com.golstars.www.glostars.DatabaseModel.RecentPicRealm;
import com.golstars.www.glostars.ModelData.Comment;
import com.golstars.www.glostars.ModelData.FollowInfo;
import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.ModelData.Rating;
import com.golstars.www.glostars.ModelData.UserDetails;
import com.golstars.www.glostars.adapters.CompetitionData;
import com.golstars.www.glostars.adapters.PostAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class compGallery extends Fragment implements AdapterInfomation {


    // --------------- recycler view settings ---------
    private ArrayList<Hashtag> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostAdapter mAdapter;
    //-------------------------------------------------
    private boolean loading;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    GridLayoutManager layoutManager;
    MyUser mUser;
    int pg ;
    private Intent homeIntent;


    private ArrayList<Hashtag> gridImages;

    private CompetitionData compAdapt;
    private ArrayList<Hashtag> compPicsUrls;

    Integer unseenNotifs = 0;


    PullRefreshLayout layout;


    RecyclerView gallery;
    Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_comp_gallery, container, false);

        pg = 1;
        loading = true;

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

        if(isConnected(getContext())){
            LoadServer();
            if(mUser == null){
                new getUserData().execute("");
            } else{
                load(false);
            }
        }else{
            LoadOffline();
        }







        return rootView;


    }

    private void LoadOffline() {
        Realm realm=Realm.getDefaultInstance();
        Gson gson=new Gson();
        RealmResults<CompetionRealm> all=realm.where(CompetionRealm.class).findAllSorted("id", Sort.DESCENDING);
        for (CompetionRealm competionRealm:all
                ) {
            compPicsUrls.add(gson.fromJson(competionRealm.data,Hashtag.class));
        }
        compAdapt.notifyDataSetChanged();
    }

    public static boolean isConnected(Context context){
        boolean hasConnection;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        hasConnection = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return hasConnection;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pg", pg);
        outState.putBoolean("loading", loading);
        System.out.println("PAGE IS: " + pg);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //pg = savedInstanceState.getInt("pg");
        //loading = savedInstanceState.getBoolean("loading");
    }


    public void LoadServer(){
        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        HubConnection connection = new HubConnection(ServerInfo.BASE_URL);
        HubProxy hub = connection.createHubProxy("GlostarsHub");

        final MyUser me=MyUser.getmUser();
        System.out.println("server Token "+me.getToken());

        Credentials credentials=new Credentials() {
            @Override
            public void prepareRequest(Request request) {
                request.addHeader("Authorization", "Bearer " + me.getToken());

            }
        };
        connection.setCredentials(credentials);
        SignalRFuture<Void> awaitConnection = connection.start();
        try {
            awaitConnection.get();
        } catch (InterruptedException e) {
            // Handle ...
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
       /* try {
            hub.invoke("hello");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        //<editor-fold desc="Follow Update">
        hub.on("FollowUpdate",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("FollowUpdate "+o);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson=new Gson();
                        FollowInfo followInfo=gson.fromJson(o,FollowInfo.class);

                        if(followInfo.originatedById.equalsIgnoreCase(me.getUserId()) || followInfo.destinationById.equalsIgnoreCase(me.getUserId())){

                            for (Hashtag h:compPicsUrls
                                    ) {

                                System.out.println("Check with user id "+h.getPoster().getUserId());
                                if(h.getPoster().getUserId().equalsIgnoreCase(me.getUserId())){
                                    continue;
                                }else if(followInfo.isMutual){
                                    h.setIs_mutual(true);
                                    h.setHe_follow(true);
                                    h.setMe_follow(true);
                                }else if(h.getPoster().getUserId().equalsIgnoreCase(followInfo.originatedById)){
                                    if(followInfo.originateFollowDestination){
                                        h.setIs_mutual(false);
                                        h.setHe_follow(false);
                                        h.setMe_follow(true);
                                    }else if(followInfo.destinationFollowOriginate){
                                        h.setIs_mutual(false);
                                        h.setHe_follow(true);
                                        h.setMe_follow(false);
                                    }else{
                                        h.setIs_mutual(false);
                                        h.setHe_follow(false);
                                        h.setMe_follow(false);
                                    }
                                }else if(h.getPoster().getUserId().equalsIgnoreCase(followInfo.destinationById)){

                                    if(followInfo.originateFollowDestination){
                                        h.setIs_mutual(false);
                                        h.setHe_follow(true);
                                        h.setMe_follow(false);
                                    }else if(followInfo.destinationFollowOriginate){
                                        h.setIs_mutual(false);
                                        h.setHe_follow(false);
                                        h.setMe_follow(true);
                                    }else{
                                        h.setIs_mutual(false);
                                        h.setHe_follow(false);
                                        h.setMe_follow(false);
                                    }
                                }
                            }
                            compAdapt.notifyDataSetChanged();
                        }

                    }
                });

            }
        },String.class);
        //</editor-fold>

        hub.on("updatePicture",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("Update Picture "+o);



                handler.post(new Runnable() {
                    public void run() {

                        Gson gson=new Gson();
                        Hashtag hashtag=gson.fromJson(o.toString(),Hashtag.class);
                        System.out.println("hash tag "+hashtag.toString());

                        for (int i = 0; i < compPicsUrls.size(); i++) {
                            System.out.println(compPicsUrls.get(i).getId()+"--"+hashtag.getId());
                            if(compPicsUrls.get(i).getId()==hashtag.getId()){
                                System.out.println("Found...");
                                for (Rating rating:hashtag.getRatings()
                                        ) {
                                    if(rating.getRaterId().equalsIgnoreCase(compPicsUrls.get(i).getPoster().getUserId())){
                                        hashtag.setMyStarCount(rating.getStarsCount());
                                        break;
                                    }
                                }

                                List<Comment> comments=compPicsUrls.get(i).getComments();
                                comments.clear();
                                for (com.golstars.www.glostars.ModelData.Comment comment:hashtag.getComments()
                                        ) {
                                    comments.add(comment);
                                }
                                hashtag.setComments(comments);

                                compPicsUrls.set(i,hashtag);
                                compAdapt.notifyDataSetChanged();
                                break;
                            }
                        }


                    }
                });

            }
        },String.class);

        hub.on("AddPicture",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("Add Picture "+o);
                handler.post(new Runnable() {
                    public void run() {

                        Gson gson=new Gson();
                        Hashtag hashtag=gson.fromJson(o.toString(),Hashtag.class);
                        System.out.println("hash tag "+hashtag.toString());

                        if(hashtag.isIsCompeting()){
                            compPicsUrls.add(0,hashtag);
                            compAdapt.notifyDataSetChanged();
                        }
                    }
                });

            }
        },String.class);


        hub.on("RemovePicture",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("Remove Picture Id "+o);



                handler.post(new Runnable() {
                    public void run() {

                        int postId=Integer.parseInt(o);

                        for (Hashtag hashtag:compPicsUrls){
                            if(hashtag.getId()==postId){
                                compPicsUrls.remove(hashtag);
                                compAdapt.notifyDataSetChanged();
                                return;
                            }
                        }
                    }
                });

            }
        },String.class);


        hub.on("EditProfile",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("EditProfile "+o);
                handler.post(new Runnable() {
                    public void run() {

                        Gson gson=new Gson();
                        UserDetails userDetails=gson.fromJson(o,UserDetails.class);

                        for (Hashtag hashtag:compPicsUrls){
                            if(hashtag.getPoster().getUserId().equalsIgnoreCase(userDetails.id)){
                                hashtag.getPoster().setName(userDetails.name+" "+userDetails.lastName);
                                hashtag.getPoster().setProfilePicURL(userDetails.profilePicURL);
                                Picasso.with(getContext()).invalidate(userDetails.profilePicURL);
                            }
                        }
                        compAdapt.notifyDataSetChanged();
                    }
                });

            }
        },String.class);







    }

    public void addPic(Hashtag hashtag){
        for (int i = 0; i < compPicsUrls.size(); i++) {
            System.out.println(compPicsUrls.get(i).getId()+"--"+hashtag.getId());
            if(compPicsUrls.get(i).getId()==hashtag.getId()){
                System.out.println("Found...");
                compPicsUrls.set(i,hashtag);
                compAdapt.notifyDataSetChanged();
                break;
            }
        }
    }





    public boolean isConnected(){
        boolean hasConnection;
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        hasConnection = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return hasConnection;

    }


    public void load(boolean fromRecursive){
        System.out.println("I am in checked...");
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


                    Realm r= Realm.getDefaultInstance();
                    for (Hashtag hashtag:getAllPost
                            ) {
                        CompetionRealm competionRealm=new CompetionRealm(hashtag.getId(),gson.toJson(hashtag).toString());
                        r.beginTransaction();
                        r.insertOrUpdate(competionRealm);
                        r.commitTransaction();

                    }
                    if(pg==1){
                        compPicsUrls.clear();
                        compAdapt.notifyDataSetChanged();
                    }
                    compPicsUrls.addAll(getAllPost);
                    System.out.println("Total Post "+compPicsUrls.size());
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
