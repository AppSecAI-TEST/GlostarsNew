package com.golstars.www.glostars.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.golstars.www.glostars.ModelData.Hashtag;
import com.golstars.www.glostars.R;
import com.golstars.www.glostars.SingleItemDialogFragment;

import java.util.ArrayList;

public class ImagePagerAdapter extends PagerAdapter {
    Context context;
    ArrayList<Hashtag> data;
    SingleItemDialogFragment singleItemDialogFragment;
    public ImagePagerAdapter(Context context,ArrayList<Hashtag> data,SingleItemDialogFragment singleItemDialogFragment){
        this.context=context;
        this.data=data;
        this.singleItemDialogFragment=singleItemDialogFragment;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, collection, false);
        ImageView image_content= (ImageView) layout.findViewById(R.id.image_content);
        Glide.with(context).load(data.get(position).getPicUrl())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image_content);
        collection.addView(layout);
        image_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleItemDialogFragment.SwitchCaption();
            }
        });
        return layout;
    }


    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
