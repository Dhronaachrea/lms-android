package com.skilrock.banner;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.skilrock.config.Config;
import com.skilrock.lms.ui.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class BannerAdapter extends PagerAdapter {
    Context activity;
    List<HashMap<String, String>> SliderImagesData;
    private OnBannerClickListener bannerClickListener;
    private int[] imgResources;

    public BannerAdapter(Context activity,
                         List<HashMap<String, String>> SliderImageDatas,
                         OnBannerClickListener bannerClickListener) {
        this.activity = activity;
        this.bannerClickListener = bannerClickListener;
        this.SliderImagesData = SliderImageDatas;
    }

    public BannerAdapter(Context activity, int[] imgResources,
                         OnBannerClickListener bannerClickListener) {
        this.activity = activity;
        this.bannerClickListener = bannerClickListener;
        this.imgResources = imgResources;
    }

    @Override
    public int getCount() {
        return SliderImagesData.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.banner_image, container, false);

        ImageView mImageView = (ImageView) view
                .findViewById(R.id.image_display);
//        PicassoTrustAll.getInstance(activity).load(Config.getInstance().getBaseURL() + SliderImagesData.get(position).get("imageurl")).placeholder(R.drawable.no_banner).into(mImageView);
        Picasso.with(activity).load(Config.getInstance().getBaseURL() + SliderImagesData.get(position).get("imageurl")).placeholder(R.drawable.no_banner).into(mImageView);
        mImageView.setTag(SliderImagesData.get(position));
        // imageLoader.displayImage(
        // SliderImagesData.get(position).get("imageurl"), mImageView,
        // options, imageListener);
        mImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.setId(position);
                bannerClickListener.onBannerClickListener((HashMap<String, String>) v.getTag());
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}