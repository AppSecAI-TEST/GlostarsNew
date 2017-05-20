package com.golstars.www.glostars.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.golstars.www.glostars.ModelData.Comment;
import com.golstars.www.glostars.R;
import com.golstars.www.glostars.Timestamp;
import com.golstars.www.glostars.user_profile;
import com.squareup.picasso.Picasso;

import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * Created by Arif on 4/28/2017.
 */

public class CommentData extends ArrayAdapter<Comment> {

    public CommentData(Context context, List<Comment> objects) {
        super(context, R.layout.comment_model, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.comment_model, null);
        }

        final Comment comm = getItem(position);

        Typeface type = Typeface.createFromAsset(getContext().getAssets(),"fonts/Ubuntu-Light.ttf");


        if (comm != null) {
            TextView namecomment = (TextView) v.findViewById(R.id.namecomment);
            ImageView commentPic = (ImageView) v.findViewById(R.id.commentpic);
            EmojiconTextView comment = (EmojiconTextView) v.findViewById(R.id.comment);
            TextView hours = (TextView) v.findViewById(R.id.hourcomment);

            namecomment.setTypeface(type);
            comment.setTypeface(type);
            hours.setTypeface(type);


            hours.setText(Timestamp.getInterval(Timestamp.getOwnZoneDateTime(comm.getCommentTime())));

            Picasso.with(getContext()).load(comm.getProfilePicUrl()).into(commentPic);
            comment.setText(comm.getCommentMessage());
            //hours.setText("2");
            namecomment.setText(comm.getFirstName() + " " + comm.getLastName());

            commentPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("USER_ID", comm.getCommenterId());
                    intent.setClass(getContext(), user_profile.class);
                    getContext().startActivity(intent);

                }
            });

            namecomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("USER_ID", comm.getCommenterId());
                    intent.setClass(getContext(), user_profile.class);
                    getContext().startActivity(intent);
                }
            });


        }

        return v;
    }
}
