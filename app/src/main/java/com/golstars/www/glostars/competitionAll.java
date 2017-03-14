package com.golstars.www.glostars;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class competitionAll extends AppCompatActivity implements OnSinglePicClick {



    //===========================FABS=========================================

    Animation fab_hide;
    Animation fab_show;
    Animation rotate_clockwise;
    Animation rotate_anticlockwise;

    boolean isOpen = false;


    FloatingActionButton mainFAB ;
    FloatingActionButton cameraFAB;
    FloatingActionButton competitionFAB;
    ImageView profileFAB;
    FloatingActionButton notificationFAB;
    FloatingActionButton homeFAB;

    TextView homebadge;
    TextView notificationbadge;
    TextView profilebadge;
    TextView camerabadge;
    TextView mainbadge;
    TextView competitionbadge;
    //===================================================================


    Button gallerybut;
    Button tipsbut;
    Button termsbut;
    Button recogbut;



    RecyclerView gallery;
    LinearLayout tips;
    ScrollView terms;
    ScrollView recog;

    TextView please;
    TextView termstex;
    TextView a;
    TextView b;
    TextView c;
    TextView d;
    TextView e;
    TextView f;
    TextView g;
    TextView h;
    TextView i;
    TextView j;
    TextView k;
    TextView ll;
    TextView m;
    TextView n;
    TextView o;
    TextView p;

    ImageView slogo;
    EditText search;
    ImageView gl;
    boolean showingFirst = true;

    // --------------- recycler view settings ---------
    private List<Post> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostAdapter mAdapter;
    //-------------------------------------------------
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager layoutManager;
    MyUser mUser;
    int pg = 1;

    GridView competitiongrid;
    private RecyclerGridAdapter compAdapt;
    private ArrayList<String> compPicsUrls;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_all);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gallery = (RecyclerView) findViewById(R.id.gallerygrid);
        tips = (LinearLayout)findViewById(R.id.tipslin);
        terms = (ScrollView)findViewById(R.id.termslin);
        recog = (ScrollView)findViewById(R.id.recoglin);

        gallerybut = (Button)findViewById(R.id.gallerybut);
        tipsbut = (Button)findViewById(R.id.tipsbut);
        termsbut = (Button)findViewById(R.id.termsbut);
        recogbut = (Button)findViewById(R.id.recogbut);

        gl = (ImageView)findViewById(R.id.glostarslogo);
        slogo = (ImageView)findViewById(R.id.searchlogo);
        search = (EditText)findViewById(R.id.searchedit);

        please = (TextView)findViewById(R.id.please);
        termstex = (TextView)findViewById(R.id.termstext);
        a = (TextView)findViewById(R.id.a);
        b = (TextView)findViewById(R.id.b);
        c = (TextView)findViewById(R.id.c);
        d = (TextView)findViewById(R.id.d);
        e = (TextView)findViewById(R.id.e);
        f = (TextView)findViewById(R.id.f);
        g = (TextView)findViewById(R.id.g);
       h = (TextView)findViewById(R.id.h);
        i = (TextView)findViewById(R.id.i);
        j = (TextView)findViewById(R.id.j);
        k = (TextView)findViewById(R.id.k);
        ll = (TextView)findViewById(R.id.ll);
        m = (TextView)findViewById(R.id.m);
       n = (TextView)findViewById(R.id.n);
        o = (TextView)findViewById(R.id.o);
        p = (TextView)findViewById(R.id.p);


        mainFAB = (FloatingActionButton)findViewById(R.id.mainFAB);
        cameraFAB =(FloatingActionButton)findViewById(R.id.cameraFAB);
        competitionFAB = (FloatingActionButton)findViewById(R.id.competitionFAB);
        profileFAB = (ImageView) findViewById(R.id.profileFAB);
        notificationFAB = (FloatingActionButton)findViewById(R.id.notificationFAB);
        homeFAB = (FloatingActionButton)findViewById(R.id.homeFAB);

        //=============Notification Badges===============================================
        homebadge = (TextView)findViewById(R.id.homebadge);
        notificationbadge = (TextView)findViewById(R.id.notificationbadge);
        profilebadge = (TextView)findViewById(R.id.profilebadge);
        camerabadge = (TextView)findViewById(R.id.uploadbadge);
        mainbadge = (TextView)findViewById(R.id.mainbadge);
        competitionbadge = (TextView)findViewById(R.id.competitionbadge);




        fab_show = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_show);
        fab_hide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_hide);
        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);



        mainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen) {

                    cameraFAB.startAnimation(fab_hide);
                    competitionFAB.startAnimation(fab_hide);
                    profileFAB.startAnimation(fab_hide);
                    notificationFAB.startAnimation(fab_hide);
                    homeFAB.startAnimation(fab_hide);
                    mainFAB.startAnimation(rotate_anticlockwise);


                    cameraFAB.setClickable(false);
                    competitionFAB.setClickable(false);
                    profileFAB.setClickable(false);
                    notificationFAB.setClickable(false);
                    isOpen=false;

                }

                else{
                    cameraFAB.startAnimation(fab_show);
                    competitionFAB.startAnimation(fab_show);
                    profileFAB.startAnimation(fab_show);
                    notificationFAB.startAnimation(fab_show);
                    mainFAB.startAnimation(rotate_clockwise);
                    homeFAB.startAnimation(fab_show);

                    cameraFAB.setVisibility(View.VISIBLE);
                    competitionFAB.setVisibility(View.VISIBLE);
                    profileFAB.setVisibility(View.VISIBLE);
                    notificationFAB.setVisibility(View.VISIBLE);
                    homeFAB.setVisibility(View.VISIBLE);

                    cameraFAB.setClickable(true);
                    competitionFAB.setClickable(true);
                    profileFAB.setClickable(true);
                    notificationFAB.setClickable(true);
                    homeFAB.setClickable(true);
                    isOpen=true;


                }
            }
        });





        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        gallerybut.setTypeface(type);
        tipsbut.setTypeface(type);
        termsbut.setTypeface(type);
        recogbut.setTypeface(type);
        a.setTypeface(type);
        b.setTypeface(type);
        c.setTypeface(type);
        d.setTypeface(type);
        e.setTypeface(type);
        f.setTypeface(type);
        g.setTypeface(type);
        h.setTypeface(type);
        i.setTypeface(type);
        j.setTypeface(type);
        k.setTypeface(type);
        ll.setTypeface(type);
        m.setTypeface(type);
        n.setTypeface(type);
        o.setTypeface(type);
        p.setTypeface(type);
        please.setTypeface(type);
        termstex.setTypeface(type);
        mainbadge.setTypeface(type);
        notificationbadge.setTypeface(type);
        homebadge.setTypeface(type);
        profilebadge.setTypeface(type);
        competitionbadge.setTypeface(type);
        camerabadge.setTypeface(type);





        slogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(competitionAll.this, searchResults.class));

            }
        });




        //==========================================================================

        gallerybut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gallerybut.setTextColor(getResources().getColor(R.color.colorPrimary));
                tipsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                termsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                recogbut.setTextColor(getResources().getColor(R.color.darkGrey));


                gallery.setVisibility(View.VISIBLE);
                tips.setVisibility(View.GONE);
                terms.setVisibility(View.GONE);
                recog.setVisibility(View.GONE);

            }
        });


        termsbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                termsbut.setTextColor(getResources().getColor(R.color.colorPrimary));
                tipsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                gallerybut.setTextColor(getResources().getColor(R.color.darkGrey));
                recogbut.setTextColor(getResources().getColor(R.color.darkGrey));


                terms.setVisibility(View.VISIBLE);
                tips.setVisibility(View.GONE);
                gallery.setVisibility(View.GONE);
                recog.setVisibility(View.GONE);
            }
        });



        tipsbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipsbut.setTextColor(getResources().getColor(R.color.colorPrimary));
                gallerybut.setTextColor(getResources().getColor(R.color.darkGrey));
                termsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                recogbut.setTextColor(getResources().getColor(R.color.darkGrey));


                tips.setVisibility(View.VISIBLE);
                gallery.setVisibility(View.GONE);
                terms.setVisibility(View.GONE);
                recog.setVisibility(View.GONE);
            }
        });


        recogbut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                recogbut.setTextColor(getResources().getColor(R.color.colorPrimary));
                tipsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                termsbut.setTextColor(getResources().getColor(R.color.darkGrey));
                gallerybut.setTextColor(getResources().getColor(R.color.darkGrey));


                recog.setVisibility(View.VISIBLE);
                tips.setVisibility(View.GONE);
                terms.setVisibility(View.GONE);
                gallery.setVisibility(View.GONE);
            }
        });

        //==========================================================================================

        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(competitionAll.this, MainFeed.class));
            }
        });


        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(competitionAll.this, notification.class));
            }
        });

        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(competitionAll.this, user_profile.class));
            }
        });


        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(competitionAll.this, upload.class));
            }
        });


        //---------------NETWORK AND RECYCLER VIEW --------------------------------
        //recyclerView = (RecyclerView) findViewById(R.id.mainfeedrecycler);

        mUser = MyUser.getmUser();
        mUser.setContext(this);
        compPicsUrls = new ArrayList<>();

        Picasso.with(this).load(mUser.getProfilePicURL()).into(profileFAB);



        //competitiongrid = (GridView)findViewById(R.id.gallerygrid);
        compAdapt = new RecyclerGridAdapter(this, compPicsUrls, this);
        int numOfColumns = 3;
        gallery.setLayoutManager(new GridLayoutManager(this, numOfColumns));
        gallery.setAdapter(compAdapt);

        loadPictures(pg, mUser.getToken());


    }

    private void loadPictures(int pg, String token) {
        JSONArray data = null;
        PictureService pictureService = new PictureService();
        try {
            pictureService.getCompetitionPictures(pg, token);
            while(data == null){
                data = pictureService.getData();

            }
            System.out.println("data to competition is : " + data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 0; i < data.length(); i++){

            try {
                JSONObject obj = data.getJSONObject(i);
                //setCompAdapter(obj.getString("picUrl"));
                compPicsUrls.add(obj.getString("picUrl"));
                compAdapt.notifyDataSetChanged();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }


        }


    }

    private void setCompAdapter(String picUrl) {
        compPicsUrls.add(picUrl);
        compAdapt.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(String url, Integer pos) {

    }
}
