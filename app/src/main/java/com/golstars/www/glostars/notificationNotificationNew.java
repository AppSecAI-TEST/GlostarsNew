package com.golstars.www.glostars;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.golstars.www.glostars.Database.OfflineInfo;
import com.golstars.www.glostars.DatabaseModel.NotificationRealm;
import com.golstars.www.glostars.DatabaseModel.SingalPictureInfo;
import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.ModelData.Notification;
import com.golstars.www.glostars.adapters.NotificationAdapter;
import com.golstars.www.glostars.adapters.PostData;
import com.golstars.www.glostars.interfaces.OnItemClickListener;
import com.golstars.www.glostars.models.NotificationObj;
import com.golstars.www.glostars.models.Post;
import com.golstars.www.glostars.network.NotificationService;
import com.golstars.www.glostars.network.PictureService;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import io.realm.Realm;
import io.realm.RealmResults;

public class notificationNotificationNew extends Fragment implements OnItemClickListener,AdapterInfomation {

    RecyclerView notificationNew;
    MyUser mUser;
    String tempToken;
    List<NotificationObj> notifs;
    NotificationAdapter mAdapter;
    Realm realm=Realm.getDefaultInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_notification_notification_new, container, false);

        notificationNew = (RecyclerView)rootView.findViewById(R.id.notificationRecyclerNew);
        OfflineInfo offlineInfo=new OfflineInfo(getContext());



        notifs = new ArrayList<>();
        mAdapter = new NotificationAdapter(notifs, getContext(), this, new OnItemClickListener() {
            @Override
            public void onItemClickPost(Post item) {

            }

            @Override
            public void onItemClickNotif(NotificationObj notif) {
                Intent intent = new Intent();
                intent.putExtra("USER_ID", notif.getOriginID());
                intent.setClass(getContext(),user_profile.class);
                startActivity(intent);
            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        notificationNew.setLayoutManager(layoutManager);
        notificationNew.setItemAnimator(new DefaultItemAnimator());
        notificationNew.setAdapter(mAdapter);



        RealmResults<NotificationRealm> list= realm.where(NotificationRealm.class).findAll();
        if(!searchResults.isConnected(getContext())){
            loadFromOffline();
        }else{
            if(mUser == null){
                new getUserData().execute("");
            }
        }

        return rootView;



    }

    public void loadFromOffline(){

        RealmResults<NotificationRealm> list= realm.where(NotificationRealm.class).findAll();
        for (int i = 0; i < list.size(); i++) {
            try {
                NotificationRealm notificationRealm=list.get(i);

                JSONObject singleNotif = new JSONObject(notificationRealm.getNotificationData());

                String description = singleNotif.getString("description");
                String profilePicURL = singleNotif.getString("profilePicURL");
                String name = singleNotif.getString("name");
                //String id = singleNotif.getString("id");
                String usrId = singleNotif.getString("userId");
                String originatedById = singleNotif.getString("originatedById");
                String pictureId = singleNotif.getString("pictureId");
                Boolean seen = Boolean.valueOf(singleNotif.getString("seen"));
                Boolean checked = Boolean.valueOf(singleNotif.getString("checked"));
                String Seen = singleNotif.getString("seen");
                String picURL = singleNotif.getString("picUrl");

                String date = singleNotif.getString("date");
                            /*String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
                            LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormat.forPattern(pattern));
                            String interval = Timestamp.getInterval(localDateTime);*/

                String interval = Timestamp.getInterval(Timestamp.getOwnZoneDateTime(date));
//
//                        if (Seen.equals("false")){
//                            unseenNotifs ++;
//                        }

                setNotifsAdapter(description, profilePicURL, name, picURL, usrId, originatedById, pictureId, seen, interval, checked);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        mAdapter.notifyDataSetChanged();


    }


    public void addNotification(NotificationObj notification){
        notifs.add(0,notification);
        mAdapter.notifyDataSetChanged();
    }



    ArrayList<Hashtag> hashtags=new ArrayList<Hashtag>();
    DisplayMetrics displayMetrics = new DisplayMetrics();
    int Width = displayMetrics.widthPixels;
    private PostData postDataAdapter=new PostData(hashtags, getContext(),Width,getFragmentManager());
    @Override
    public ArrayList<Hashtag> getAllData() {
        return this.hashtags;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return this.postDataAdapter;
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
//            Picasso.with(getApplicationContext()).load(mUser.getProfilePicURL()).into(profileFAB);
            tempToken = mUser.getToken();
            try {
                populateNotificationsList(mUser.getUserId(), mUser.getToken());
                setActivityNotifSeen();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }

    private void setActivityNotifSeen(){

        StringEntity entity = null;
        try {
            entity = new StringEntity("{}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        NotificationService.activityNotifSeen(getContext(), mUser.getToken(),entity, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
            }
        } );
    }


    private void setUserNotifSeen(){
        StringEntity entity = null;
        try {
            entity = new StringEntity("{}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        NotificationService.userNotifsSeen(getContext(), mUser.getToken(), entity, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
            }
        });

    }


    private void populateNotificationsList(String userId, String token) throws JSONException {

        NotificationService.getNotifications(getContext() ,userId, token, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                System.out.println("Populate notification..");
                try {
                    JSONObject data = response.getJSONObject("resultPayload");
                    System.out.println(response);
                    JSONArray activityNotifications = data.getJSONArray("activityNotifications");
                    JSONArray followerNotifications = data.getJSONArray("followerNotifications");
                    System.out.println(activityNotifications);
                    System.out.println(followerNotifications);



                    System.out.println(activityNotifications.toString());
                    for(int i = 0; i < activityNotifications.length(); ++i){
                        JSONObject singleNotif = activityNotifications.getJSONObject(i);

                        realm.beginTransaction();
                        NotificationRealm notificationRealm=new NotificationRealm(singleNotif.getInt("id"),singleNotif.toString());
                        realm.insertOrUpdate(notificationRealm);
                        realm.commitTransaction();

                        String description = singleNotif.getString("description");
                        String profilePicURL = singleNotif.getString("profilePicURL");
                        String name = singleNotif.getString("name");
                        //String id = singleNotif.getString("id");
                        String usrId = singleNotif.getString("userId");
                        String originatedById = singleNotif.getString("originatedById");
                        String pictureId = singleNotif.getString("pictureId");
                        Boolean seen = Boolean.valueOf(singleNotif.getString("seen"));
                        Boolean checked = Boolean.valueOf(singleNotif.getString("checked"));
                        String Seen = singleNotif.getString("seen");
                        String picURL = singleNotif.getString("picUrl");

                        String date = singleNotif.getString("date");
                            /*String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
                            LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormat.forPattern(pattern));
                            String interval = Timestamp.getInterval(localDateTime);*/

                        String interval = Timestamp.getInterval(Timestamp.getOwnZoneDateTime(date));
//
//                        if (Seen.equals("false")){
//                            unseenNotifs ++;
//                        }

                        setNotifsAdapter(description, profilePicURL, name, picURL, usrId, originatedById, pictureId, seen, interval, checked);
                    }
                    mAdapter.notifyDataSetChanged();
                    /*1
                    if(unseenNotifs > 0){
                        menuDown.setMenuButtonColorNormal(ContextCompat.getColor(notification.this,R.color.colorPrimary));
                        notificationFAB.setColorNormal(ContextCompat.getColor(notification.this,R.color.colorPrimary));
                        menuDown.getMenuIconView().setImageResource(R.drawable.notimenu);
                        notificationFAB.setImageResource(R.drawable.notinoti);
                    } */

                } catch (JSONException e){
                    e.printStackTrace();
                }

            }




        });

        /*NotificationService notif = new NotificationService();
        JSONObject data = null;
        try {
            notif.getNotifications(userId, token);
            while (data == null){
                data = notif.getDataObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/









    }


//    private void setActivityNotifsAdapter(String description, String profilePicURL, String name, String id, String usrId, String originatedById, String pictureId, Boolean seen, String date, String picURL, Boolean checked) {
//        NotificationObj notif = new NotificationObj(originatedById, pictureId, description, name, profilePicURL, picURL, seen, checked);
//        notif.setDate(date);
//        notifs.add(notif);
//        mAdapter.notifyDataSetChanged();
//    }

    private void setNotifsAdapter(String description, String profilePicURL, String name, String picURL, String usrId, String originatedById, String pictureId, Boolean seen, String date, Boolean checked) {
        NotificationObj notif = new NotificationObj(originatedById, pictureId, description, name, profilePicURL, picURL, seen, checked);
        notif.setDate(date);
        notifs.add(notif);

    }

    @Override
    public void onItemClickPost(Post item) {

    }

    public void LoadSingalPhoto(JSONObject response){


        System.out.println("Sinagl page res "+response.toString());
        try {

            realm.beginTransaction();
            SingalPictureInfo singalPictureInfo=new SingalPictureInfo(response.getJSONObject("resultPayload").getInt("id"),response.toString());
            realm.insertOrUpdate(singalPictureInfo);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("SINGLE PICTURE SERVICE");
        System.out.println(response);
        ArrayList<Post> gridImages = new ArrayList();

        JSONObject pic = null;
        Hashtag hashtag = null;
        try {
            pic = response.getJSONObject("resultPayload");

            Gson gson=new Gson();
            hashtag=gson.fromJson(pic.toString(),Hashtag.class);


            JSONObject poster = pic.getJSONObject("poster");
            String name = poster.getString("name");
            String usrId = poster.getString("userId");
            String profilePicUrl = poster.getString("profilePicURL");
            String id = pic.getString("id");
            String description = pic.getString("description");
            String privacy = pic.getString("privacy");
            String picURL = pic.getString("picUrl");

            Boolean isFeatured = Boolean.valueOf(pic.getString("isfeatured"));
            Boolean isCompeting = Boolean.valueOf(pic.getString("isCompeting"));
            Integer starsCount = Integer.parseInt(pic.getString("starsCount"));

            JSONArray ratings = pic.getJSONArray("ratings");
            JSONArray comments = pic.getJSONArray("comments");

            String uploaded = pic.getString("uploaded");
            //String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
            //LocalDateTime localDateTime = LocalDateTime.parse(uploaded, DateTimeFormat.forPattern(pattern));
            //String interval = Timestamp.getInterval(localDateTime);

            Post post = new Post(name,usrId,id, description,picURL,profilePicUrl, isFeatured, isCompeting, starsCount,comments.length());
            post.setUploaded(uploaded);
            post.setRatings(ratings);
            post.setComments(comments);
            post.setPrivacy(privacy);


            gridImages.add(0, post);




        } catch (JSONException e) {
            e.printStackTrace();
        }



                    /*
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",0);
                    bundle.putString("token", mUser.getToken());
                    bundle.putString("usrID", mUser.getUserId());


                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    SingleItemDialogFragment newFragment = SingleItemDialogFragment.newInstance();
                    newFragment.setArguments(bundle); */

        /******************* New Fullscreen ***************************/
        OfflineInfo offlineInfo=new OfflineInfo(getContext());

        Bundle bundle = new Bundle();
        bundle.putParcelable("post", hashtag);
        bundle.putString("token", offlineInfo.getToken());
        bundle.putString("usrID", offlineInfo.getUserId());
        bundle.putParcelable("poster", hashtag.getPoster());



        Intent intent = new Intent(getActivity(), newFullscreen.class);
        intent.putExtras(bundle);
        //intent.putExtra("post", (Serializable) post);
        getActivity().startActivity(intent);
        /**************************************************************/

        hashtags.clear();

        hashtags.add(hashtag);




        postDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClickNotif(NotificationObj notif) {
        if(!searchResults.isConnected(getContext())){

            SingalPictureInfo info=realm.where(SingalPictureInfo.class).equalTo("id",Integer.parseInt(notif.getPicID())).findFirst();
            if(info==null){
                Toast.makeText(getContext(), "No internet available.", Toast.LENGTH_SHORT).show();
            }else {
                try {
                    LoadSingalPhoto(new JSONObject(info.data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            if(!(notif.getDescription() == "started following you")){

                System.out.println("PIC NOTIFICATION TO OPEN: " + notif.getPicID());
                System.out.println("USER TOKEN: " + tempToken); // im using this tempToken string because there was some error with tokens within this class
                PictureService.getSinglePic(tempToken, notif.getPicID(), new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        LoadSingalPhoto(response);
                    /*System.out.println("SINGLE PICTURE SERVICE");
                    System.out.println(response);
                    ArrayList<Post> gridImages = new ArrayList();

                    JSONObject pic = null;
                    Hashtag hashtag = null;
                    try {
                        pic = response.getJSONObject("resultPayload");

                        Gson gson=new Gson();
                        hashtag=gson.fromJson(pic.toString(),Hashtag.class);


                        JSONObject poster = pic.getJSONObject("poster");
                        String name = poster.getString("name");
                        String usrId = poster.getString("userId");
                        String profilePicUrl = poster.getString("profilePicURL");
                        String id = pic.getString("id");
                        String description = pic.getString("description");
                        String privacy = pic.getString("privacy");
                        String picURL = pic.getString("picUrl");

                        Boolean isFeatured = Boolean.valueOf(pic.getString("isfeatured"));
                        Boolean isCompeting = Boolean.valueOf(pic.getString("isCompeting"));
                        Integer starsCount = Integer.parseInt(pic.getString("starsCount"));

                        JSONArray ratings = pic.getJSONArray("ratings");
                        JSONArray comments = pic.getJSONArray("comments");

                        String uploaded = pic.getString("uploaded");
                        //String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
                        //LocalDateTime localDateTime = LocalDateTime.parse(uploaded, DateTimeFormat.forPattern(pattern));
                        //String interval = Timestamp.getInterval(localDateTime);

                        Post post = new Post(name,usrId,id, description,picURL,profilePicUrl, isFeatured, isCompeting, starsCount,comments.length());
                        post.setUploaded(uploaded);
                        post.setRatings(ratings);
                        post.setComments(comments);
                        post.setPrivacy(privacy);


                        gridImages.add(0, post);




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    *//*
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",0);
                    bundle.putString("token", mUser.getToken());
                    bundle.putString("usrID", mUser.getUserId());


                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    SingleItemDialogFragment newFragment = SingleItemDialogFragment.newInstance();
                    newFragment.setArguments(bundle); *//*

                    *//******************* New Fullscreen ***************************//*
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("post", hashtag);
                    bundle.putString("token", mUser.getToken());
                    bundle.putString("usrID", mUser.getUserId());
                    bundle.putParcelable("poster", hashtag.getPoster());



                    Intent intent = new Intent(getActivity(), newFullscreen.class);
                    intent.putExtras(bundle);
                    //intent.putExtra("post", (Serializable) post);
                    getActivity().startActivity(intent);
                    *//**************************************************************//*

                    hashtags.clear();

                    hashtags.add(hashtag);




                    postDataAdapter.notifyDataSetChanged();

                    //newFragment.show(ft, "slideshow");*/

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        System.out.println("FAILURE AT SINGLE PIC SERVICE");
                        System.out.println(errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        System.out.println("FAILURE AT SINGLE PIC SERVICE");
                        System.out.println(responseString);
                    }
                });

            }
        }

    }
}
