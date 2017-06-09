package com.golstars.www.glostars;

import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class newFullscreen extends AppCompatActivity {

    ImageView newFullscreenPost;
    RecyclerView picturepreview;
    TextView fullscreenUsername;
    TextView fullscreenUploadTime;
    ImageView fullscreenPrivacy;
    ImageView fullscreenProflePic;
    Button newFullscreenFollow;
    TextView newFullscreenCaption;
    RatingBar newRating;
    TextView newRatingCount;
    ImageView newCommentIcon;
    TextView newCommentCount;
    ListView newFullscreenComments;
    EmojiconEditText newCommentArea;
    TextView newSendComment;
    ImageView emojiButton;
    EmojIconActions emojIcon;
    View rootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_fullscreen);

        newFullscreenPost = (ImageView)findViewById(R.id.newfullscreenimage);
        picturepreview = (RecyclerView)findViewById(R.id.previewPictures);
        fullscreenUsername = (TextView)findViewById(R.id.newFullscreenUsername);
        fullscreenUploadTime = (TextView)findViewById(R.id.newFullscreenUploadTime);
        fullscreenPrivacy = (ImageView)findViewById(R.id.newFullscreenPrivacy);
        fullscreenProflePic = (ImageView)findViewById(R.id.newFullscreenProfilePic);
        newFullscreenFollow = (Button)findViewById(R.id.newFullscreenFollowButton);
        newFullscreenCaption = (TextView)findViewById(R.id.newFullscreenCaption);
        newRating = (RatingBar)findViewById(R.id.newFullscreenRating);
        newRatingCount = (TextView)findViewById(R.id.newFullscreenRatingCount);
        newCommentIcon = (ImageView)findViewById(R.id.newFullscreenCommentIcon);
        newCommentCount = (TextView)findViewById(R.id.newFullscreenCommentCount);
        newFullscreenComments = (ListView)findViewById(R.id.newFullscreenComments);
        newCommentArea = (EmojiconEditText) findViewById(R.id.newFullscreenCommentArea);
        newSendComment = (TextView)findViewById(R.id.newFullscreenSendComment);
        emojiButton = (ImageView) findViewById(R.id.emojiButton);
        rootView = (View) findViewById(R.id.rootViewFullscreen);



        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        fullscreenUsername.setTypeface(type);
        fullscreenUploadTime.setTypeface(type);
        newFullscreenCaption.setTypeface(type);
        newRatingCount.setTypeface(type);
        newCommentCount.setTypeface(type);
        newSendComment.setTypeface(type);
        newCommentArea.setTypeface(type);

        newFullscreenFollow.setTransformationMethod(null);



        emojIcon = new EmojIconActions(newFullscreen.this,rootView,newCommentArea,emojiButton);
        emojIcon.ShowEmojIcon();

        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });




    }
}
