package com.golstars.www.glostars;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by admin on 2/13/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context CTX;
    private Integer image_id[] = {
            R.drawable.post1,
            R.drawable.post2,
            R.drawable.post3,
            R.drawable.post4,
            R.drawable.post5,
            R.drawable.post6,

    };


    public ImageAdapter(Context CTX){
        this.CTX = CTX;
    }

    @Override
    public int getCount(){

        return image_id.length;
    }

    @Override
    public Object getItem(int arg0){
        return null;
    }

    public long getItemId(int arg0){
        return 0;
    }

    public View getView(int arg0, View arg1, ViewGroup arg2){

        ImageView img;

        if (arg1 == null){
            img = new ImageView(CTX);

            img.setLayoutParams(new GridView.LayoutParams(160,160));
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setPadding(2,2,2,2);
        }else{
            img = (ImageView) arg1;
        }

        img.setImageResource(image_id[arg0]);
        return img;
    }
}
