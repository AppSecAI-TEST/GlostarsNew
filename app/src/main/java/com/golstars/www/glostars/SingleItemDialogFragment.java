package com.golstars.www.glostars;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.adapters.ImagePagerAdapter;
import com.golstars.www.glostars.adapters.PostData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;


/**
 * Created by edson on 20/03/17.
 */

public class SingleItemDialogFragment extends DialogFragment {

    //private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<Hashtag> postData;
    private TextView lblCount, lblTitle, lblDate,captionfullscreen,ratingcountfullscreen,commentcountfullscreen;
    private RatingBar ratingfullscreen;
    private ImageView commentfullscreen,sharefullscreen,clearRating;
    private int selectedPosition = 0;
    private boolean onBind = false;
    private String token;
    private String usrID;
    MyUser mUser;
    //PostData postDataAdapter;
    RecyclerView.Adapter postDataAdapter;

    LinearLayout captionContainer;

    Context context;
    ViewPager pager;
    ImagePagerAdapter pagerAdapter;


    private ImageView imageViewPreview;

    public static SingleItemDialogFragment newInstance(){
        SingleItemDialogFragment f = new SingleItemDialogFragment();
        return f;
    }


    boolean isShowCaption=false;
    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        captionContainer= (LinearLayout) v.findViewById(R.id.captionContainer);

        captionContainer.setVisibility(View.GONE);

        //lblCount = (TextView) v.findViewById(R.id.lbl_count);
        lblTitle = (TextView) v.findViewById(R.id.title);
        lblDate = (TextView) v.findViewById(R.id.date);
        captionfullscreen = (TextView)v.findViewById(R.id.captionFullscreen);

        commentcountfullscreen = (TextView)v.findViewById(R.id.commentcountfullscreen);
        sharefullscreen = (ImageView) v.findViewById(R.id.sharefullscreen);

        clearRating = (ImageView)v.findViewById(R.id.clearRatingfullscreen);
        ratingcountfullscreen = (TextView)v.findViewById(R.id.ratingstarcountfullscreen);
        captionfullscreen = (TextView)v.findViewById(R.id.captionFullscreen);
        ratingfullscreen = (RatingBar)v.findViewById(R.id.ratingBarfullscreen);
        commentfullscreen = (ImageView)v.findViewById(R.id.commenticonfullscreen);



        //images = (ArrayList<Hashtag>) getArguments().getSerializable("images");
        postData=((AdapterInfomation)context).getAllData();
        postDataAdapter=((AdapterInfomation)context).getAdapter();

        System.out.println("Get total data "+postData.size());
        selectedPosition = getArguments().getInt("position");
        token = getArguments().getString("token");
        usrID = getArguments().getString("usrID");

        mUser = MyUser.getmUser(getContext());

        return v;
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        postData=((AdapterInfomation)context).getAllData();
        postDataAdapter=((AdapterInfomation)context).getAdapter();

        System.out.println("Get total data "+postData.size());
        selectedPosition = getArguments().getInt("position");
        token = getArguments().getString("token");
        usrID = getArguments().getString("usrID");

        mUser = MyUser.getmUser();


        final RelativeLayout root = new RelativeLayout(getActivity());
        //View v = LayoutInflater.from(context).inflate(R.layout.fragment_image_slider,null);

        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        //root.addView(v);
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setContentView(R.layout.fragment_image_slider);



        pagerAdapter=new ImagePagerAdapter(context,postData);


        pager= (ViewPager) dialog.findViewById(R.id.viewpager);

        pager.setAdapter(pagerAdapter);

        pager.setCurrentItem(selectedPosition);






        captionContainer= (LinearLayout) dialog.findViewById(R.id.captionContainer);

        //captionContainer.setVisibility(View.GONE);
       /* captionContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captionContainer.setVisibility(View.VISIBLE);
            }
        });*/
        /*viewpager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(captionContainer.getVisibility()== View.VISIBLE){
                    captionContainer.setVisibility(View.GONE);
                }else{
                    captionContainer.setVisibility(View.VISIBLE);
                }
            }
        });*/
        // viewpager.setSi


        //lblCount = (TextView) v.findViewById(R.id.lbl_count);
        lblTitle = (TextView) dialog.findViewById(R.id.title);
        lblTitle.setText(postData.get(selectedPosition).getPoster().getName());
        lblDate = (TextView) dialog.findViewById(R.id.date);
        lblDate.setText(postData.get(selectedPosition).getUploaded());
        captionfullscreen = (TextView)dialog.findViewById(R.id.captionFullscreen);
        captionfullscreen.setText(postData.get(selectedPosition).getDescription());

        LinearLayout commentContainer= (LinearLayout) dialog.findViewById(R.id.commentContainer);
        commentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Comment section click", Toast.LENGTH_SHORT).show();
            }
        });



        commentcountfullscreen = (TextView)dialog.findViewById(R.id.commentcountfullscreen);
        commentcountfullscreen.setText(postData.get(selectedPosition).getComments().size()+"");
        sharefullscreen = (ImageView) dialog.findViewById(R.id.sharefullscreen);

        clearRating = (ImageView)dialog.findViewById(R.id.clearRatingfullscreen);
        ratingcountfullscreen = (TextView)dialog.findViewById(R.id.ratingstarcountfullscreen);
        ratingcountfullscreen.setText(postData.get(selectedPosition).getStarsCount()+"");
        ratingfullscreen = (RatingBar)dialog.findViewById(R.id.ratingBarfullscreen);
        commentfullscreen = (ImageView)dialog.findViewById(R.id.commenticonfullscreen);






        if(!postData.get(selectedPosition).isIsCompeting()){
            ratingfullscreen.setNumStars(1);
        }
        else ratingfullscreen.setNumStars(5);

        ratingfullscreen.setRating((float)(postData.get(selectedPosition).getMyStarCount()));


        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                //which item currently view
                lblTitle.setText(postData.get(position).getPoster().getName());
                lblDate.setText(postData.get(position).getUploaded());
                captionfullscreen.setText(postData.get(position).getDescription());
                commentcountfullscreen.setText(postData.get(position).getComments().size()+"");
                ratingcountfullscreen.setText(postData.get(position).getStarsCount()+"");
                if(!postData.get(position).isIsCompeting()){
                    ratingfullscreen.setNumStars(1);
                }
                else ratingfullscreen.setNumStars(5);

                ratingfullscreen.setRating((float)(postData.get(position).getMyStarCount()));
                selectedPosition=position;
            }
        });


        clearRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Delete Call "+selectedPosition);
                String url = ServerInfo.BASE_URL_API+"images/rating";

                AsyncHttpClient client=new AsyncHttpClient();
                client.addHeader("Authorization", "Bearer " + mUser.getToken());
                System.out.println("Token "+mUser.getToken());
                try {
                    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    trustStore.load(null, null);
                    MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                    sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    client.setSSLSocketFactory(sf);
                }
                catch (Exception e) {}

                RequestParams params=new RequestParams();
                params.add("NumOfStars","0");
                params.add("PhotoId",postData.get(selectedPosition).getId()+"");

                final HashMap<Integer,Integer> hashMap=new HashMap<Integer, Integer>(); //use for hold selected position
                hashMap.put(postData.get(selectedPosition).getId(),selectedPosition);


                client.post(context, url,params,new JsonHttpResponseHandler(){


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println("1. "+response.toString());
                            try {
                                int picId=response.getJSONObject("resultPayload").getInt("picId");
                                postData.get(hashMap.get(picId)).setStarsCount(response.getJSONObject("resultPayload").getInt("totalRating"));
                                postData.get(hashMap.get(picId)).setMyStarCount(0);

                                if (hashMap.get(picId)==selectedPosition) {
                                    ratingcountfullscreen.setText(response.getJSONObject("resultPayload").getInt("totalRating")+"");
                                    ratingcountfullscreen.setText(postData.get(hashMap.get(picId)).getStarsCount()+"");
                                    ratingfullscreen.setRating((float)(postData.get(hashMap.get(picId)).getMyStarCount()));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            postDataAdapter.notifyDataSetChanged();
                            System.out.println("Loading complete");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        System.out.println("2 "+responseString);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                        System.out.println("3 "+errorResponse.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        System.out.println("4 "+errorResponse.toString());
                    }

                });
            }
        });





        return dialog;
    }
    public void changeRating(final RatingBar ratingBar, final int position){
        System.out.println("Change Call "+position);

        String url = ServerInfo.BASE_URL_API+"images/rating";

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

        RequestParams params=new RequestParams();
        params.add("NumOfStars",(int)ratingBar.getRating()+"");
        params.add("PhotoId",postData.get(position).getId()+"");
        client.post(context, url,params,new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    System.out.println("1. "+response.toString());
                    try {
                        postData.get(position).setStarsCount(response.getJSONObject("resultPayload").getInt("totalRating"));
                        postData.get(position).setMyStarCount((int)ratingBar.getRating());
                        ratingcountfullscreen.setText(response.getJSONObject("resultPayload").getInt("totalRating")+"");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    postDataAdapter.notifyDataSetChanged();
                    System.out.println("Loading complete");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("2 "+responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                System.out.println("3 "+errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                System.out.println("4 "+errorResponse.toString());
            }
        });
    }

    public void setContent(){
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}