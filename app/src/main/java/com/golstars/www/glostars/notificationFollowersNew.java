package com.golstars.www.glostars;

import android.content.Intent;
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

import com.golstars.www.glostars.DatabaseModel.FollowerNotificationRealm;
import com.golstars.www.glostars.DatabaseModel.NotificationRealm;
import com.golstars.www.glostars.ModelData.Hashtag;
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

public class notificationFollowersNew extends Fragment implements OnItemClickListener,AdapterInfomation {


    RecyclerView followerNew;
    List<NotificationObj> follNotifs;
    NotificationAdapter follAdapter;
    MyUser mUser;
    String tempToken;
    Realm realm=Realm.getDefaultInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_notification_followers_new, container, false);


        followerNew = (RecyclerView)rootView.findViewById(R.id.followerRecyclerNew);

        follNotifs = new ArrayList<>();
        follAdapter = new NotificationAdapter(follNotifs,getContext(), this , new OnItemClickListener() {
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

        RecyclerView.LayoutManager layoutM = new LinearLayoutManager(getContext());
        followerNew.setLayoutManager(layoutM);
        followerNew.setItemAnimator(new DefaultItemAnimator());
        followerNew.setAdapter(follAdapter);

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
        RealmResults<FollowerNotificationRealm> list= realm.where(FollowerNotificationRealm.class).findAll();
        for (int i = 0; i < list.size(); i++) {
            try {
                FollowerNotificationRealm notificationRealm=list.get(i);

                JSONObject singleNotif = new JSONObject(notificationRealm.FollowerNotificationRealmData);

                String description = "started following you";
                String profilePicURL = singleNotif.getString("profilePicURL");
                String name = singleNotif.getString("name");
                String usrId = singleNotif.getString("userId");
                String originatedById = singleNotif.getString("originatedById");
                Boolean seen = Boolean.valueOf(singleNotif.getString("seen"));
                Boolean checked = Boolean.valueOf(singleNotif.getString("checked"));
                String Seen = singleNotif.getString("seen");
                String date = singleNotif.getString("date");
                String interval = Timestamp.getInterval(Timestamp.getOwnZoneDateTime(date));

                setActivityNotifsAdapter(description, profilePicURL, name, "", usrId, originatedById, null, seen, interval,null, checked);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        follAdapter.notifyDataSetChanged();
    }

    public void addNotification(NotificationObj notification){
        follNotifs.add(0,notification);
        follAdapter.notifyDataSetChanged();
    }



    private void populateNotificationsList(String userId, String token) throws JSONException {

        NotificationService.getNotifications(getContext(), userId, token, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject data = response.getJSONObject("resultPayload");
                    System.out.println(response);
                    JSONArray activityNotifications = data.getJSONArray("activityNotifications");
                    JSONArray followerNotifications = data.getJSONArray("followerNotifications");
                    System.out.println(activityNotifications);
                    System.out.println(followerNotifications);



                    for(int i = 0; i < followerNotifications.length(); ++i){
                        JSONObject singleNotif = followerNotifications.getJSONObject(i);

                        realm.beginTransaction();
                        FollowerNotificationRealm followerNotificationRealm=new FollowerNotificationRealm(singleNotif.getInt("followerNotificationId"),singleNotif.toString());
                        realm.insertOrUpdate(followerNotificationRealm);
                        realm.commitTransaction();

                        String description = "started following you";
                        String profilePicURL = singleNotif.getString("profilePicURL");
                        String name = singleNotif.getString("name");
                        String usrId = singleNotif.getString("userId");
                        String originatedById = singleNotif.getString("originatedById");
                        Boolean seen = Boolean.valueOf(singleNotif.getString("seen"));
                        Boolean checked = Boolean.valueOf(singleNotif.getString("checked"));
                        String Seen = singleNotif.getString("seen");
                        String date = singleNotif.getString("date");
                        String interval = Timestamp.getInterval(Timestamp.getOwnZoneDateTime(date));

                        setActivityNotifsAdapter(description, profilePicURL, name, "", usrId, originatedById, null, seen, interval,null, checked);
                    }
                    follAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        });

    }

//    private void setFollowerNotifsAdapter(String description, String profilePicURL, String name, String id, String usrId, String originatedById, String pictureId, Boolean seen, String date, Boolean checked) {
//        NotificationObj notif = new NotificationObj(originatedById, pictureId, description, name, profilePicURL, null, seen, checked);
//        notif.setDate(date);
//        follNotifs.add(notif);
//        follAdapter.notifyDataSetChanged();
//    }

        private void setActivityNotifsAdapter(String description, String profilePicURL, String name, String id, String usrId, String originatedById, String pictureId, Boolean seen, String date, String picURL, Boolean checked) {
            NotificationObj notif = new NotificationObj(originatedById, pictureId, description, name, profilePicURL, picURL, seen, checked);
            notif.setDate(date);
            follNotifs.add(notif);

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


    ArrayList<Hashtag> hashtags=new ArrayList<Hashtag>();
    DisplayMetrics displayMetrics = new DisplayMetrics();
    int Width = displayMetrics.widthPixels;
    private PostData postDataAdapter=new PostData(hashtags, getContext(),Width,getFragmentManager());

    public ArrayList<Hashtag> getAllData() {
        return this.hashtags;
    }


    public RecyclerView.Adapter getAdapter() {
        return this.postDataAdapter;
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

            tempToken = mUser.getToken();


            tempToken = mUser.getToken();
            try {
                populateNotificationsList(mUser.getUserId(), mUser.getToken());
                setUserNotifSeen();
            } catch (Exception e1) {
                e1.printStackTrace();
            }


        }
    }





    @Override
    public void onItemClickPost(Post item) {

    }

    @Override
    public void onItemClickNotif(NotificationObj notif) {
        if(!(notif.getDescription() == "started following you")){

            System.out.println("PIC NOTIFICATION TO OPEN: " + notif.getPicID());
            System.out.println("USER TOKEN: " + tempToken); // im using this tempToken string because there was some error with tokens within this class
            PictureService.getSinglePic(tempToken, notif.getPicID(), new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
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




                    Bundle bundle = new Bundle();
                    bundle.putInt("position",0);
                    bundle.putString("token", mUser.getToken());
                    bundle.putString("usrID", mUser.getUserId());


                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    SingleItemDialogFragment newFragment = SingleItemDialogFragment.newInstance();
                    newFragment.setArguments(bundle);

                    hashtags.clear();

                    hashtags.add(hashtag);
                    postDataAdapter.notifyDataSetChanged();

                    newFragment.show(ft, "slideshow");

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





            /*
            Intent intent = new Intent();
            intent.putExtra("IMAGE_SAUCE",notif.getPicURL());
            intent.putExtra("IMAGE_AUTHOR", mUser.getName());
            intent.putExtra("IMAGE_CAPTION","");
            intent.setClass(getApplicationContext(), imagefullscreen.class);
            startActivity(intent); */
        }
    }
}
