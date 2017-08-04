package com.golstars.www.glostars;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.golstars.www.glostars.ModelData.Comment;
import com.golstars.www.glostars.ModelData.FollowInfo;
import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.ModelData.Rating;
import com.golstars.www.glostars.ModelData.UserDetails;
import com.golstars.www.glostars.models.Follower;
import com.golstars.www.glostars.network.FollowerService;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;
import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class newFollowing extends Fragment {

    ListView newfollowing;

    //ArrayList<Follower> followers;
    ArrayList<Follower> following;

    FollowersAdapter followingAdaper;
    //FollowersAdapter followersAdapter;

    //ListView followersList;
    //ListView followingList;

    ImageView followpropic;
    TextView surname;
    TextView lastname;

    MyUser mUser;

    String guestUserID;
    String mUserID;
    String token;
    private Handler handler=new Handler();

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_new_following, container, false);

        newfollowing = (ListView) rootView.findViewById(R.id.newfollowingList);
        //followers = new ArrayList<>();
        following = new ArrayList<>();

        guestUserID  = getArguments().getString("guestUserId");
        mUserID = getArguments().getString("myUserId");
        token =  getArguments().getString("token");

        //followersAdapter = new FollowersAdapter(getContext(), R.layout.activity_followers_model, followers);
        followingAdaper = new FollowersAdapter(getContext(), R.layout.activity_followers_model, following);

        //followersList.setAdapter(followersAdapter);

        //newfollowing.setAdapter(followingAdaper); wtf is happening here
        new getUserData().execute("");

        LoadServer();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
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








        hub.on("EditProfile",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("EditProfile "+o);
                handler.post(new Runnable() {
                    public void run() {

                        Gson gson=new Gson();
                        UserDetails userDetails=gson.fromJson(o,UserDetails.class);

                        for (Follower follower:following
                                ) {
                            if(userDetails.id.equalsIgnoreCase(follower.getUserId())) {


                                follower.setUserName(userDetails.name + " " + userDetails.lastName);
                                follower.setProfilePicture(userDetails.profilePicURL);
                                followingAdaper.notifyDataSetChanged();
                                break;
                            }
                        }



                    }
                });

            }
        },String.class);





        hub.on("FollowUpdate",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("FollowUpdate "+o);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson=new Gson();
                        FollowInfo followInfo=gson.fromJson(o,FollowInfo.class);

                        //Here data recieved originate user based



                        if(followInfo.originatedById.equalsIgnoreCase(me.getUserId()) || followInfo.destinationById.equalsIgnoreCase(me.getUserId())){

                            boolean b=false;
                            for (Follower follower:following
                                    ) {
                                if (followInfo.originatedById.equalsIgnoreCase(follower.getUserId())) {
                                    b=true;
                                    System.out.println("In 1");
                                    Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/Ubuntu-Light.ttf");
                                    if (followInfo.isMutual) {
                                        follower.setRigStatus("mutual");
                                    } else if (followInfo.destinationFollowOriginate) {
                                        follower.setRigStatus("follower");
                                    } else if (followInfo.originateFollowDestination) {
                                        follower.setRigStatus("following");

                                    } else {
                                        follower.setRigStatus("follow");
                                    }
                                    followingAdaper.notifyDataSetChanged();
                                    break;

                                } else if (followInfo.destinationById.equalsIgnoreCase(follower.getUserId())) {
                                    System.out.println("In 2");
                                    b=true;
                                    Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/Ubuntu-Light.ttf");
                                    if (followInfo.isMutual) {
                                        follower.setRigStatus("mutual");
                                    } else if (followInfo.originateFollowDestination) {
                                        follower.setRigStatus("follower");
                                    } else if (followInfo.destinationFollowOriginate) {
                                        follower.setRigStatus("following");

                                    } else {
                                        follower.setRigStatus("follow");
                                    }
                                    followingAdaper.notifyDataSetChanged();
                                    break;
                                }
                            }
                            if(!b){
                               /* if(followInfo.originatedById.equalsIgnoreCase(me.getUserId())){
                                    Follower follower=new Follower(followInfo.destinationProfilePhoto,followInfo.destinationById,followInfo.destinationUserName);
                                    followers.add();
                                }else{
                                    followers.add(new Follower(followInfo.originateProfilePhoto,followInfo.originatedById,followInfo.originateUserName));
                                }*/

                            }

                        }

                    }


                });

            }
        },String.class);



    }

    private class getUserData extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            mUser = MyUser.getmUser();
            mUser.setContext(getContext());
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            LoadFollowers(guestUserID, token, mUserID);
//
//            Picasso.with(getApplicationContext()).load(mUser.getProfilePicURL()).into(profileFAB);


        }
    }

    public void LoadFollowers(String usr, String token, final String myID){
       /* FollowerService.LoadFollowers(this, myID, token, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject resultPayload = response.getJSONObject("resultPayload");
                    myFollowers = resultPayload.getJSONArray("followerList");
                    myFollowing = resultPayload.getJSONArray("followingList");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/

        FollowerService.LoadFollowers(getContext(), usr, token, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    System.out.println(response);
                    JSONArray followerList = null;
                    JSONArray followingList = null;

                    JSONObject resultPayload = response.getJSONObject("resultPayload");
                    followerList = resultPayload.getJSONArray("followerList");
                    followingList = resultPayload.getJSONArray("followingList");

                   /*if((myFollowers != null)  && (myFollowing != null)){

                   }*/

                    Set<String> followerListSet=new HashSet<String>();
                    Set<String> followingListSet=new HashSet<String>();



//                    for(int i = 0; i < followerList.length(); i++){
//                        Follower follower = new Follower();
//                        follower.setUserId(followerList.getJSONObject(i).getString("id"));
//
//                        followerListSet.add(followerList.getJSONObject(i).getString("id"));
//
//                        String name = followerList.getJSONObject(i).getString("name");
//                        String surname = followerList.getJSONObject(i).getString("lastName");
//
//                        follower.setUserName(name + " " + surname);
//                        follower.setProfilePicture(followerList.getJSONObject(i).getString("profilemediumPath"));
//
//                        if(follower.getUserId().equals(mUserID)){
//                            follower.setRigStatus("");
//                        }
//                        boolean isMutual=followerList.getJSONObject(i).getBoolean("is_mutual");
//                        if(isMutual){
//                            follower.setRigStatus("mutual");
//                        }else{
//                            follower.setRigStatus("follower");
//                        }
//                        followers.add(follower);
//                    }


                    for(int i = 0; i < followingList.length(); i++){
                        Follower follower = new Follower();
                        follower.setUserId(followingList.getJSONObject(i).getString("id"));

                        followingListSet.add(followingList.getJSONObject(i).getString("id"));

                        String name = followingList.getJSONObject(i).getString("name");
                        String surname = followingList.getJSONObject(i).getString("lastName");

                        follower.setUserName(name + " " + surname);
                        follower.setProfilePicture(followingList.getJSONObject(i).getString("profilemediumPath"));

                        if(follower.getUserId().equals(mUserID)){
                            follower.setRigStatus("");
                        }
                        boolean isMutual=followingList.getJSONObject(i).getBoolean("is_mutual");
                        if(isMutual){
                            follower.setRigStatus("mutual");
                        }else{
                            follower.setRigStatus("following");
                        }

                        following.add(follower);


                    }



                    /*for ( Follower f  :followers
                         ) {

                        if(followerListSet.contains(f.getUserId()) && followingListSet.contains(f.getUserId())){
                            f.setRigStatus("mutual");
                        }else if(followerListSet.contains(f.getUserId())){
                            f.setRigStatus("follower");
                        }else if(followingListSet.contains(f.getUserId())){
                            f.setRigStatus("following");
                        }else{
                            f.setRigStatus("follow");
                        }
                    }

                    for ( Follower f  :following
                         ) {

                        if(followerListSet.contains(f.getUserId()) && followingListSet.contains(f.getUserId())){
                            f.setRigStatus("mutual");
                        }else if(followerListSet.contains(f.getUserId())){
                            f.setRigStatus("follower");
                        }else if(followingListSet.contains(f.getUserId())){
                            f.setRigStatus("following");
                        }else{
                            f.setRigStatus("follow");
                        }
                    }*/
                    //followersAdapter.notifyDataSetChanged();
                    followingAdaper.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }




    private class FollowersAdapter extends ArrayAdapter<Follower> {

        ArrayList<Follower> followers;
        Context context;


        public FollowersAdapter(Context context, int resource, ArrayList<Follower> followers) {
            super(context, resource, followers);

            this.context = context;
            this.followers = followers;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.activity_followers_model, null);
            }

            final Follower p = getItem(position);

            if (p != null) {

                ImageView usrPic= (ImageView) v.findViewById(R.id.propicfollow);

                TextView name = (TextView) v.findViewById(R.id.namefollow);

                final Button follow = (Button) v.findViewById(R.id.followBUT);

                LinearLayout informationContainer= (LinearLayout) v.findViewById(R.id.informationContainer);
                informationContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context,user_profile.class);
                        intent.putExtra("USER_ID",followers.get(position).getUserId());
                        startActivity(intent);
                    }
                });

                if (name != null) {
                    name.setText(p.getUserName());
                }

                if (surname != null) {
                    surname.setText("");
                }


                String fStatus = p.getStatus();
                follow.setText(fStatus);

                if(fStatus.equals("")){
                    follow.setVisibility(View.INVISIBLE);
                } else if(fStatus.equals("follower")){
                    follow.setText("Follower");
                    follow.setTextColor(ContextCompat.getColor(context,R.color.white));
                    follow.setBackground(ContextCompat.getDrawable(context,R.drawable.followbackbutton));
                    follow.setTransformationMethod(null);
                    Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/Ubuntu-Light.ttf");
                    follow.setTypeface(type);
                } else if(fStatus.equals("following")){
                    follow.setText("Following");
                    follow.setTextColor(ContextCompat.getColor(context,R.color.white));
                    follow.setBackground(ContextCompat.getDrawable(context,R.drawable.followingbutton));
                    follow.setTransformationMethod(null);
                    Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/Ubuntu-Light.ttf");
                    follow.setTypeface(type);
                } else if(fStatus.equals("mutual")){
                    follow.setText("Mutual Followers");
                    follow.setTextColor(ContextCompat.getColor(context,R.color.white));
                    follow.setBackground(ContextCompat.getDrawable(context,R.drawable.mutualfollowerbutton));
                    follow.setTransformationMethod(null);
                    Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/Ubuntu-Light.ttf");
                    follow.setTypeface(type);
                }else if(fStatus.equals("follow")){
                    follow.setText("Follow");
                    follow.setTextColor(ContextCompat.getColor(context,R.color.white));
                    follow.setBackgroundResource(R.drawable.followbutton);
                    follow.setTransformationMethod(null);
                    Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/Ubuntu-Light.ttf");
                    follow.setTypeface(type);
                }

                follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(follow.getText().toString().equalsIgnoreCase("follower") || follow.getText().toString().equalsIgnoreCase("follow")){
                            String url = ServerInfo.BASE_URL_FOLLOWER_API+"Following/"+p.getUserId();
                            AsyncHttpClient client=new AsyncHttpClient();
                            try {
                                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                                trustStore.load(null, null);
                                MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                                sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                                client.setSSLSocketFactory(sf);
                            }
                            catch (Exception e) {}
                            MyUser myUser=MyUser.getmUser();
                            client.addHeader("Authorization", "Bearer " + myUser.getToken());
                            RequestParams requestParams=new RequestParams();

                            client.post(getActivity(), url,requestParams,new JsonHttpResponseHandler(){


                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    try {
                                        System.out.println("1. "+response.toString());
                                        if(response.getJSONObject("resultPayload").getBoolean("result")){
                                            if(response.getJSONObject("resultPayload").getBoolean("is_mutual")){
                                                follow.setText("mutual");
                                                follow.setBackground(getResources().getDrawable(R.drawable.mutualfollowerbutton));
                                                p.setRigStatus("mutual");
                                            }
                                            else if(response.getJSONObject("resultPayload").getBoolean("me_follow")){
                                                follow.setText("following");
                                                follow.setBackground(getResources().getDrawable(R.drawable.followingbutton));
                                                p.setRigStatus("following");
                                            }else if(response.getJSONObject("resultPayload").getBoolean("he_follow")){
                                                follow.setText("follower");
                                                follow.setBackground(getResources().getDrawable(R.drawable.followbackbutton));
                                                p.setRigStatus("follower");
                                            }else{
                                                follow.setText("follow");
                                                follow.setBackgroundResource(R.drawable.followbutton);
                                                p.setRigStatus("follow");
                                            }
                                            notifyDataSetChanged();
                                        }else{
                                            Toast.makeText(context, response.getJSONObject("resultPayload").getString("msg"), Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else{
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(DialogInterface.BUTTON_POSITIVE==which){
                                        String url = ServerInfo.BASE_URL_FOLLOWER_API+"Unfollowing/"+p.getUserId();
                                        AsyncHttpClient client=new AsyncHttpClient();
                                        try {
                                            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                                            trustStore.load(null, null);
                                            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                                            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                                            client.setSSLSocketFactory(sf);
                                        }
                                        catch (Exception e) {}
                                        MyUser myUser=MyUser.getmUser();
                                        client.addHeader("Authorization", "Bearer " + myUser.getToken());
                                        RequestParams requestParams=new RequestParams();

                                        client.post(getActivity(), url,requestParams,new JsonHttpResponseHandler(){


                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                try {
                                                    System.out.println("1. "+response.toString());
                                                    if(response.getJSONObject("resultPayload").getBoolean("result")){
                                                        if(response.getJSONObject("resultPayload").getBoolean("is_mutual")){
                                                            follow.setText("mutual");
                                                            follow.setBackground(getResources().getDrawable(R.drawable.mutualfollowerbutton));
                                                            p.setRigStatus("mutual");
                                                        }
                                                        else if(response.getJSONObject("resultPayload").getBoolean("me_follow")){
                                                            follow.setText("following");
                                                            follow.setBackground(getResources().getDrawable(R.drawable.followingbutton));
                                                            p.setRigStatus("following");
                                                        }else if(response.getJSONObject("resultPayload").getBoolean("he_follow")){
                                                            follow.setText("follower");
                                                            follow.setBackground(getResources().getDrawable(R.drawable.followbackbutton));
                                                            p.setRigStatus("follower");
                                                        }else{
                                                            follow.setText("follow");
                                                            follow.setBackgroundResource(R.drawable.followbutton);
                                                            p.setRigStatus("follow");
                                                        }
                                                        notifyDataSetChanged();
                                                    }else{
                                                        Toast.makeText(context, response.getJSONObject("resultPayload").getString("msg"), Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }else{
                                        dialog.dismiss();
                                    }

                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Are you sure?").setPositiveButton("Unfollow", dialogClickListener)
                                    .setNegativeButton("Cancel", dialogClickListener).show();



                        }

                    }
                });


                if(p.getProfilePicture().equals("/Content/Profile/Thumbs/male.jpg") || p.getProfilePicture().equals("/Content/Profile/Thumbs/Male.jpg")){
                    usrPic.setImageResource(R.drawable.nopicmalegrey);

                } else if(p.getProfilePicture().equals("/Content/Profile/Thumbs/female.jpg") || p.getProfilePicture().equals("/Content/Profile/Thumbs/Female.jpg")){
                    usrPic.setImageResource(R.drawable.nopicfemalegrey);
                }else{
                    Picasso.with(getActivity()).load(p.getProfilePicture()).into(usrPic);
                    //
                }


            }

            return v;
        }

    }
}
