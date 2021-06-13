package com.soft.homegardening;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class SlidingImage_Adapter extends PagerAdapter {
    //declare variables
    private ArrayList<ModelGif> gifArrayList;
    private LayoutInflater inflater;
    private Context context;

    //constructor
    public SlidingImage_Adapter(ArrayList<ModelGif> gifArrayList, Context context) {
        this.gifArrayList = gifArrayList;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    //getcount of images

    @Override
    public int getCount() {
        return gifArrayList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        //inflate layout file
        View imageLayout = inflater.inflate(R.layout.custom_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        final TextView tvShow = imageLayout.findViewById(R.id.tv_show);
        imageView.setImageResource(gifArrayList.get(position).getGifId());
        tvShow.setText(gifArrayList.get(position).getGifText());
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
