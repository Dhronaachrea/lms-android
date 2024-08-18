package com.skilrock.lms.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skilrock.banner.CirclePageIndicator;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.RobotoButton;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.preference.GlobalPref;


public class AppTourActivity extends FragmentActivity {

    private Analytics analytics;

    CustomPagerAdapter mSectionsPagerAdapter;
    CirclePageIndicator circlePageIndicator;
    ViewPager mViewPager;
    private LinearLayout indicatorView;
    private RobotoButton doneRB;
    String[] title;
    String[] desc;
    int[] backImg;
    int[] center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_tour);
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.APP_TOUR);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);

        init();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        doneRB = (RobotoButton) findViewById(R.id.done);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mSectionsPagerAdapter = new CustomPagerAdapter(getApplicationContext());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        circlePageIndicator.setViewPager(mViewPager);
        circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == center.length - 1) {
                    doneRB.setVisibility(View.VISIBLE);
                } else {
                    doneRB.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        doneRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init() {
        if (GlobalPref.getInstance(this).getCountry().equalsIgnoreCase("ZIM")) {
            title = new String[]{"Easy Transactions", "Single Click Purchase", "Premier Leagues on Mobile", "Amazing Game Themes", "Reach to Nearest Retailer"};
            desc = new String[]{"Add money to your account easily now. We have tied up with more providers.", "Buy draw games tickets in single click. No hassles now.", "We have brought sports betting.Now play Sports lottery from our app.", "Don't like to wait for results!! Now play and win instantly.", "Locating retailers has become easier. Use map to relocate the service providers."};
            backImg = new int[]{R.drawable.app_tour_bg_two, R.drawable.app_tour_bg_three, R.drawable.app_tour_bg_four, R.drawable.app_tour_bg_five, R.drawable.app_tour_bg_one};
            center = new int[]{R.drawable.screen_two, R.drawable.screen_three, R.drawable.screen_four, R.drawable.screen_five, R.drawable.screen_one};
        } else if (GlobalPref.getInstance(this).getCountry().equalsIgnoreCase("lagos")) {
            title = new String[]{"Fast Login and Register", "Change Password", "App Side drawer", "Game Introduction", "Single Click Purchase",
                    "Detailed Statistics", "Detailed Results", "My Accounts", "Deposit", "Select Duration", "My Transactions"};
            desc = new String[]{"Just a few steps and it's done. Now you can register with us in a few seconds.", "Open the change password window from app side drawer and update your password.",
                    "Quick links to important features are now just a swipe away.", "Quick links to buy draw games ticket, see results and see statistics on a single screen.",
                    "Buy draw games tickets in single click. No hassles now.", "Detailed statistics of draw games are now available for you.",
                    "Detailed results of draw games are now available for you.",
                    "See and edit your profile and balance details on a single screen.", "Add money to your account easily now. We have tied up with more partners.",
                    "Filter your transactions for specific duration.", "See your transactions in a better way now."};
            backImg = new int[]{R.drawable.app_tour_bg_three};
            center = new int[]{R.drawable.screen_two, R.drawable.screen_one, R.drawable.screen_fourteen, R.drawable.screen_three, R.drawable.screen_four,
                    R.drawable.screen_five, R.drawable.screen_six,
                    R.drawable.screen_ten, R.drawable.screen_eleven, R.drawable.screen_thirteen, R.drawable.screen_twelve};
        } else {
            title = new String[]{"My Accounts", "My Transactions", "Deposit", "Single Click Purchase", "Single Click Purchase"};
            desc = new String[]{"See and edit your profile and balance details on a single screen.", "See your transactions in a better way now.",
                    "Add money to your account easily now. We have tied up with more partners.", "Buy draw games tickets in single click. No hassles now.",
                    "Buy Soccer game tickets in single click. No hassles now."};
            backImg = new int[]{R.drawable.app_tour_bg_three};
            center = new int[]{R.drawable.screen_one, R.drawable.screen_two, R.drawable.screen_three, R.drawable.screen_four, R.drawable.screen_five};
        }
    }


    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;

        public CustomPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.fragment_main, collection, false);
            TextView titleTV, descTV;
            RelativeLayout backIV;
            ImageView centerIV;
            backIV = (RelativeLayout) view.findViewById(R.id.back_image);
            centerIV = (ImageView) view.findViewById(R.id.center_image);
            titleTV = (TextView) view.findViewById(R.id.title_text);
            descTV = (TextView) view.findViewById(R.id.desc_text);
            indicatorView = (LinearLayout) view.findViewById(R.id.indicator);
            if (backImg.length == 1)
                backIV.setBackgroundResource(backImg[0]);
            else
                backIV.setBackgroundResource(backImg[position % backImg.length]);
            centerIV.setImageResource(center[position]);
            titleTV.setText(title[position]);
            descTV.setText(desc[position]);
            collection.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return center.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }

}
