package com.skilrock.banner;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.skilrock.lms.ui.R;

public class BannerView {

    private static final long ANIM_VIEWPAGER_DELAY = 6000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 6000;

    // UI References
    private BannerViewPager mViewPager;
    private PageIndicator mIndicator;
    private List<HashMap<String, String>> imageDataList;
    private OnBannerClickListener bannerClickListener;
    boolean stopSliding = false;
    private Runnable animateViewPager;
    private Handler handler;

    public BannerView(Context mContext) {

    }

    public View onCreateViewPanel(Context context,
                                  View view, List<HashMap<String, String>> imageList,
                                  OnBannerClickListener bannerClickListener) {

        this.bannerClickListener = bannerClickListener;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.banner_fragment, null);

        mViewPager = (BannerViewPager) view.findViewById(R.id.view_pager);
        mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        imageDataList = imageList;
        if (imageDataList != null && imageDataList.size() != 0) {
            mViewPager.setAdapter(new BannerAdapter(context, imageDataList,
                    this.bannerClickListener));
            mIndicator.setViewPager(mViewPager);
            runnable(imageDataList.size());
            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
        }
        // for ghana only for one banner
        if (imageDataList.size() == 1) {
            view.findViewById(R.id.indicator).setVisibility(View.INVISIBLE);
        }
        //
        mViewPager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {

                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_UP:
                        // calls when touch release on ViewPager
                        // if (imageDataList != null && imageDataList.size() != 0) {
                        stopSliding = false;
                        runnable(imageDataList.size());
                        handler.postDelayed(animateViewPager,
                                ANIM_VIEWPAGER_DELAY_USER_VIEW);
                        // }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // calls when ViewPager touch
                        if (handler != null && stopSliding == false) {
                            stopSliding = true;
                            handler.removeCallbacks(animateViewPager);
                        }
                        break;
                }
                return false;
            }
        });

        return view;

    }

    public void onPauseBanner() {
        if (handler != null) {
            // Remove callback
            handler.removeCallbacks(animateViewPager);
        }
    }

    public void runnable(final int size) {
        handler = new Handler();
        animateViewPager = new Runnable() {
            public void run() {
                if (!stopSliding) {
                    if (mViewPager.getCurrentItem() == size - 1) {
                        mViewPager.setCurrentItem(0);
                    } else {
                        mViewPager.setCurrentItem(
                                mViewPager.getCurrentItem() + 1, true);
                    }
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
                }
            }
        };
    }

}
