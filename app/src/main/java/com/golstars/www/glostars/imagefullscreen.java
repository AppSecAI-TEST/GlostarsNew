package com.golstars.www.glostars;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class imagefullscreen extends AppCompatActivity {

    ImageView image;
    TextView author;
    TextView caption;
    Button followfullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagefullscreen);

        image = (ImageView)findViewById(R.id.fullscreenimage);
        author = (TextView)findViewById(R.id.author);
        caption = (TextView)findViewById(R.id.caption);
        followfullscreen = (Button)findViewById(R.id.followfullscreen);

        followfullscreen.setTransformationMethod(null);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        followfullscreen.setTypeface(type);

        String imgSrc = this.getIntent().getStringExtra("IMAGE_SAUCE");
        String aut = this.getIntent().getStringExtra("IMAGE_AUTHOR");
        String description = this.getIntent().getStringExtra("IMAGE_CAPTION");

        if(imgSrc == null){
            finish();
        } else {
            Picasso.with(this).load(imgSrc).into(image);
        }

        if(author == null){
            finish();
        } else {
            author.setText(aut);
        }

        if(description == null){
            description = "";
            caption.setText(description);

        } else {
            caption.setText(description);
        }

    }

}
