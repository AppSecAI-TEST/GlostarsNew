package com.golstars.www.glostars.adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.golstars.www.glostars.MainFeed;
import com.golstars.www.glostars.ModelData.Comment;
import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.MyUser;
import com.golstars.www.glostars.R;
import com.golstars.www.glostars.ServerInfo;
import com.golstars.www.glostars.SingleItemDialogFragment;
import com.golstars.www.glostars.competition_page;
import com.golstars.www.glostars.hashtagResults;
import com.golstars.www.glostars.newFullscreen;
import com.golstars.www.glostars.user_profile;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static android.content.ContentValues.TAG;

/**
 * Created by Arif on 4/21/2017.
 */

/*public class PostData {
    
}*/
public class PostData extends RecyclerView.Adapter<PostData.MyViewHolder> {
    private ArrayList<Hashtag> data;
    MyUser mUser = MyUser.getmUser();
    Context context;
    public Integer screenWidth = 0;
    private boolean onBind;
    FragmentManager fm;

    public CommentData commentData;

    public PostData(ArrayList<Hashtag> data,Context context,Integer screenWidth,FragmentManager fm) {
        this.data=data;
        this.context=context;
        this.screenWidth=screenWidth;
        this.fm=fm;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_hash_tag,parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Integer pos = position;
        final Hashtag post = data.get(position);
        holder.username.setText(post.getPoster().getName());

        //holder.postRelative.setBackgroundColor(context.getResources().getColor(R.color.verylightviolet));
        if(position % 2 != 0){
            holder.postRelative.setBackgroundColor(context.getResources().getColor(R.color.verylightviolet));
        } else{
            holder.postRelative.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        //Use for hash tag
        HashTagHelper mTextHashTagHelper;
        mTextHashTagHelper = HashTagHelper.Creator.create(context.getResources().getColor(R.color.hashtag), new HashTagHelper.OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {
                context.startActivity(new Intent(context,hashtagResults.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("data",hashTag));
            }
        });

        // pass a TextView or any descendant of it (incliding EditText) here.
        // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
        holder.caption.setText(post.getDescription());
        mTextHashTagHelper.handle(holder.caption);

        holder.postTime.setText(post.getUploaded());
        holder.uselessTextView.setText("");
        if(!post.isIsCompeting()){
            holder.ratingBar.setNumberOfStars(1);
        }
        else holder.ratingBar.setNumberOfStars(5);

        System.out.println("My Rating "+post.getMyStarCount()+" position is "+position);
        holder.ratingBar.setRating((float)post.getMyStarCount());
        holder.totalStars.setText(String.valueOf(post.getStarsCount()));
        holder.totalComments.setText(post.getComments().size()+"");


        if(post.getPoster().getProfilePicURL().equals("/Content/Profile/Thumbs/male.jpg") || post.getPoster().getProfilePicURL().equals("/Content/Profile/Thumbs/Male.jpg")){
            holder.propic.setImageResource(R.drawable.nopicmalegrey);

        } else if(post.getPoster().getProfilePicURL().equals("/Content/Profile/Thumbs/female.jpg") || post.getPoster().getProfilePicURL().equals("/Content/Profile/Thumbs/Female.jpg")){
            holder.propic.setImageResource(R.drawable.nopicfemalegrey);
        }else{

            Picasso.with(context)
                    .load(post.getPicUrl())
                    .placeholder(R.drawable.loading)
                    //.resize(screenWidth,1000)
                    .resize(screenWidth, screenWidth)
                    .centerInside()
                    .into(holder.postImg);
            Picasso.with(context).load(post.getPoster().getProfilePicURL()).into(holder.propic);

        }




        if(post.isIsfeatured()){
            holder.featuredFlag.setVisibility(View.VISIBLE);
            holder.seeAllcomp.setVisibility(View.VISIBLE);
        } else{
            holder.featuredFlag.setVisibility(View.GONE);
            holder.seeAllcomp.setVisibility(View.GONE);
        }


        if(post.isIsCompeting()){
            holder.privacyIcon.setImageResource(R.drawable.privacy_competition_photo);
        } else {
            if(post.getPrivacy().equals("public")) {
                holder.privacyIcon.setImageResource(R.drawable.privacy_public_photo);
            } else if (post.getPrivacy().equals("followers") | post.getPrivacy().equals("friends")) {
                holder.privacyIcon.setImageResource(R.drawable.privacy_mutual_follower_photo);
            }
        }




        onBind = false;
        holder.seeAllcomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, competition_page.class);
                context.startActivity(intent);
            }
        });
        holder.propic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, user_profile.class);
                intent.putExtra("USER_ID",post.getPoster().getUserId());

                context.startActivity(intent);
            }
        });
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, user_profile.class);
                intent.putExtra("USER_ID",post.getPoster().getUserId());

                context.startActivity(intent);
            }
        });

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Delete Call "+position);
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
                params.add("PhotoId",data.get(position).getId()+"");
                client.post(context, url,params,new JsonHttpResponseHandler(){


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println("1. "+response.toString());
                            try {
                                data.get(position).setStarsCount(response.getJSONObject("resultPayload").getInt("totalRating"));
                                data.get(position).setMyStarCount(0);
                                holder.ratingBar.setRating(0);
                                notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            notifyDataSetChanged();
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
        //holder.ratingBar.

        /**************** Rating bar Change ***********************************/

        /*changed here */


        holder.ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = holder.ratingBar.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int)starsf + 1;
                    holder.ratingBar.setRating(stars);
                    System.out.println("Call rating touching...");
                    //needChange=true;
                    v.setPressed(false);
                    changeRating(holder.ratingBar,position);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }
                return true;
            }});
        /******************************************************************/

        /******************Action Listenner for all button************************/
        holder.postImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(MainFeed.isConnected(context)){
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("post", post);
                    bundle.putString("token", mUser.getToken());
                    bundle.putString("usrID", mUser.getUserId());
                    bundle.putParcelable("poster", post.getPoster());



                    Intent intent = new Intent(context, newFullscreen.class);
                    intent.putExtras(bundle);
                    //intent.putExtra("post", (Serializable) post);
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context, "couldn't reach servers", Toast.LENGTH_LONG).show();
                }

                /*
                FragmentTransaction ft = fm.beginTransaction();
                SingleItemDialogFragment newFragment = SingleItemDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                notifyDataSetChanged();

                newFragment.show(ft, "slideshow");
                */


            }
        });
        /*************************************************************************/
        //

       /* final List<Comment> listAllComment=data.get(position).getComments();
        final CommentData commentData=new CommentData(context,listAllComment);
        commentData.notifyDataSetChanged();*/

        holder.commentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainFeed.isConnected(context)){
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("post", post);
                    bundle.putString("token", mUser.getToken());
                    bundle.putString("usrID", mUser.getUserId());
                    bundle.putParcelable("poster", post.getPoster());



                    Intent intent = new Intent(context, newFullscreen.class);
                    intent.putExtras(bundle);
                    //intent.putExtra("post", (Serializable) post);
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context, "couldn't reach servers", Toast.LENGTH_LONG).show();
                }




//                final RelativeLayout root = new RelativeLayout(context);
//                root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                final Dialog dialog = new Dialog(context);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(root);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                dialog.getWindow().setContentView(R.layout.commentdialog);
//                final ListView commentrecycler = (ListView) dialog.findViewById(R.id.commentrecycler);
//                final EmojiconEditText commentbox = (EmojiconEditText) dialog.findViewById(R.id.commentBox);
//                final TextView sendcomment = (TextView) dialog.findViewById(R.id.sendcomment);
//
//
//                final List<Comment> listAllComment=data.get(position).getComments();
//                /*final CommentData */commentData=new CommentData(context,listAllComment);
//                commentrecycler.setAdapter(commentData);
//                commentData.notifyDataSetChanged();
//
//
//
//
//
//
//                commentrecycler.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                    @Override
//                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int p, long id) {
//
//                        if(!mUser.getUserId().equalsIgnoreCase(data.get(pos).getPoster().getUserId())){
//                            return false;
//                        }
//
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//                        builder.setTitle("Delete Comment");
//                        builder.setMessage("Are you sure want to delete this comment?");
//
//                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//
//                            public void onClick(DialogInterface dialogInterface, int which) {
//                                dialogInterface.dismiss();
//
//
//
//                                final ProgressDialog progressDialog = ProgressDialog.show(context, "",
//                                        "Comment deleting. Please wait...", true);
//                                progressDialog.setCanceledOnTouchOutside(true);
//                                progressDialog.show();
//
//                                String url = ServerInfo.BASE_URL + "api/images/DeleteComment?commentId="+listAllComment.get(p).getCommentId();
//                                AsyncHttpClient client=new AsyncHttpClient();
//                                try {
//                                    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//                                    trustStore.load(null, null);
//                                    MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
//                                    sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//                                    client.setSSLSocketFactory(sf);
//                                }
//                                catch (Exception e) {}
//                                RequestParams msg=new RequestParams();
//                                client.addHeader("Authorization", "Bearer " + mUser.getToken());
//                                final Integer integer=new Integer(p);
//
//
//                                client.get(context, url,new JsonHttpResponseHandler(){
//                                    @Override
//                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                                        try {
//                                            if(response.getJSONObject("resultPayload").getBoolean("res")){
//                                                Toast.makeText(context, "Successfully delete comment", Toast.LENGTH_SHORT).show();
//
//                                                listAllComment.remove(integer.intValue());
//
//
//
//                                                data.get(position).setComments(listAllComment);
//                                                commentData.notifyDataSetChanged();
//                                                notifyDataSetChanged();
//
//
//                                            }
//                                            progressDialog.dismiss();
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                                        super.onFailure(statusCode, headers, responseString, throwable);
//                                        progressDialog.dismiss();
//                                    }
//
//                                    @Override
//                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                                        super.onFailure(statusCode, headers, throwable, errorResponse);
//                                        progressDialog.dismiss();
//                                    }
//
//                                    @Override
//                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                                        super.onFailure(statusCode, headers, throwable, errorResponse);
//                                        progressDialog.dismiss();
//                                    }
//
//                                });
//                            }
//
//                        });
//
//                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // I do not need any action here you might
//                                dialog.dismiss();
//                            }
//                        });
//
//                        AlertDialog alert = builder.create();
//                        alert.show();
//
//
//
//                        return true;
//                    }
//                });
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//                ImageView emoji_btn= (ImageView) dialog.findViewById(R.id.emoji_btn);
//
//                ImageView dialogClose= (ImageView) dialog.findViewById(R.id.imageView4);
//
//                dialogClose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//
//                EmojIconActions emojIcon = new EmojIconActions(context, root, commentbox, emoji_btn);
//
//                emojIcon.setUseSystemEmoji(true);
//                commentbox.setUseSystemDefault(true);
//
//                emojIcon.ShowEmojIcon();
////
//
//                emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
//                    @Override
//                    public void onKeyboardOpen() {
//                        Log.e(TAG, "Keyboard opened!");
//                    }
//
//                    @Override
//                    public void onKeyboardClose() {
//                        Log.e(TAG, "Keyboard closed");
//                    }
//                });
//
//
//
//                sendcomment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        final String comment = String.valueOf(commentbox.getText());
//
//                        try {
//                            String url = ServerInfo.BASE_URL + "api/images/comment";
//                            AsyncHttpClient client=new AsyncHttpClient();
//                            try {
//                                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//                                trustStore.load(null, null);
//                                MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
//                                sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//                                client.setSSLSocketFactory(sf);
//                            }
//                            catch (Exception e) {}
//                            RequestParams msg=new RequestParams();
//                            client.addHeader("Authorization", "Bearer " + mUser.getToken());
//                            msg.add("CommentText", comment);
//                            msg.add("PhotoId", data.get(position).getId()+"");
//
//                            client.post(context, url,msg,new JsonHttpResponseHandler(){
//                                @Override
//                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                                    try {
//                                        JSONObject comment=response.getJSONObject("resultPayload");
//                                        Comment c=new Comment(comment.getInt("commentId"),comment.getString("commentMessage"),comment.getString("commenterUserName"),comment.getString("commentUserNameId"),comment.getString("commentTime"),comment.getString("profilePicUrl"),comment.getString("firstName"),comment.getString("lastName"));
//                                        listAllComment.add(c);
//                                        commentData.notifyDataSetChanged();
//                                        data.get(position).setComments(listAllComment);
//                                        notifyDataSetChanged();
//                                        commentrecycler.setSelection(listAllComment.size()-1);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        commentbox.setText("");
//                    }
//                });
//
//               /* mBuilder.setView(mView);
//                AlertDialog dialog = mBuilder.create();
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/
//                dialog.show();
            }
        });


    }



    public ArrayList<Hashtag> getListData(){
        return this.data;
    }




    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView username, caption, postTime, totalStars, totalComments, uselessTextView,comptext,seeall;
        public ImageView postImg;
        public ImageView propic;
        public SimpleRatingBar ratingBar;
        public ImageView commentsBtn, privacyIcon;
        public RelativeLayout featuredFlag,seeAllcomp;
        public ImageView deleteIcon;
        public RelativeLayout postRelative;
        public MyViewHolder(View view) {
            super(view);
            Typeface type = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Ubuntu-Light.ttf");
            Typeface bold = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Ubuntu-Bold.ttf");

            username=(TextView)view.findViewById(R.id.userNAME);
            caption=(TextView)view.findViewById(R.id.userCAPTION);
            postTime=(TextView)view.findViewById(R.id.uploadTIME);
            postImg = (ImageView)view.findViewById(R.id.userPOST);
            propic = (ImageView)view.findViewById(R.id.userPIC);
            totalStars=(TextView)view.findViewById(R.id.ratingstarcount);
            totalComments=(TextView)view.findViewById(R.id.commentcount);
            ratingBar = (SimpleRatingBar) view.findViewById(R.id.ratingBar);
            commentsBtn = (ImageView)view.findViewById(R.id.commenticon);
            uselessTextView = (TextView)view.findViewById(R.id.timedigit);
            comptext = (TextView)view.findViewById(R.id.comptext);
            featuredFlag = (RelativeLayout)view.findViewById(R.id.compflag);
            deleteIcon = (ImageView)view.findViewById(R.id.clearRating);
            seeAllcomp = (RelativeLayout)view.findViewById(R.id.seeAllcomppost);
            privacyIcon = (ImageView)view.findViewById(R.id.privacyCOMP);
            seeall = (TextView)view.findViewById(R.id.seeallhash);
            postRelative = (RelativeLayout)view.findViewById(R.id.postHashtag);



            username.setTypeface(bold);
            caption.setTypeface(type);
            postTime.setTypeface(type);
            totalStars.setTypeface(type);
            totalComments.setTypeface(type);
            comptext.setTypeface(bold);
            seeall.setTypeface(bold);
        }
    }

    public void changeRating(final SimpleRatingBar ratingBar, final int position){
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
        params.add("PhotoId",data.get(position).getId()+"");
        client.post(context, url,params,new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    System.out.println("1. "+response.toString());
                    try {
                        data.get(position).setStarsCount(response.getJSONObject("resultPayload").getInt("totalRating"));
                        data.get(position).setMyStarCount((int)ratingBar.getRating());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    notifyDataSetChanged();
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
}
