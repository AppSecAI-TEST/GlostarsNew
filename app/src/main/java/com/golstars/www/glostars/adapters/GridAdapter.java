package com.golstars.www.glostars.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by edson on 22/02/17.
 */

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> ImgUrls;

    public GridAdapter(Context c, ArrayList<String> imgs) {
        mContext = c;
        this.ImgUrls = imgs;
    }

    public int getCount() {
        return ImgUrls.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);

        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageDrawable(null);
        /*
        DownloadImageTask downloadImageTask = new DownloadImageTask();
        Bitmap bm = null;
        try {
            downloadImageTask.getImage(ImgUrls.get(position));
            while(bm == null){
                bm = downloadImageTask.getData();
            }
            System.out.println(bm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bm);*/
        Picasso.with(mContext).load(ImgUrls.get(position)).into(imageView);
        return imageView;
    }


}
