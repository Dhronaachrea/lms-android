package com.skilrock.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.skilrock.bean.BetTypeBean;
import com.skilrock.bean.GamePlay;
import com.skilrock.customui.CustomTextView;
import com.skilrock.drawgame.OneToTwelve.OneToTwelveGameScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.Utils;

import android.os.Handler;

import java.text.DecimalFormat;


/**
 * Created by stpl on 11/19/2015.
 */
public class OneTwelveGameAdapter extends BaseAdapter {
    private Activity activity;
    // private GamePlay gamePlay;
    private Button ok;
    private String density;
    private LayoutInflater inflater = null;
    private int height;
    private int width;
    private AbsListView.LayoutParams params;
    private int[] textSize;
    private BetTypeBean bean;
    public static int[] numbersSelected;
    private int maxNumberToBeSelected;
    private int numFast;
    private int freq;
    DecimalFormat formatter;
    private Fragment fragment;

    //for new inc dec
    private Handler repeatUpdateHandler = new Handler();
    private boolean autoIncrement = false;
    private boolean autoDecrement = false;

    private static final long MIN_DELAY_MS = 500;
    private long mLastClickTime;
    private GlobalPref globalPref;

    private String colorSelect;
    private String colorDeSelect;

    public OneTwelveGameAdapter(Activity a, int width, int height, int[] textSize,
                                BetTypeBean bean) {
        maxNumberToBeSelected = bean.getMaxBetAmtMul();
        this.bean = bean;
        this.height = height;
        Log.i("height", height + " ");
        this.width = width;
        this.textSize = textSize;
        this.activity = a;
        density = "hdpi";
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        globalPref = GlobalPref.getInstance(fragment.getActivity());

        colorSelect = globalPref.getCountry().equalsIgnoreCase("LAGOS") ? "#00304b" : "#42A085";
        colorDeSelect = globalPref.getCountry().equalsIgnoreCase("LAGOS") ? "#88d5ff" : "#CAEEE4";
    }

    public OneTwelveGameAdapter(Fragment fragment, int width, int height, int[] textSize,
                                BetTypeBean bean) {
        maxNumberToBeSelected = bean.getMaxBetAmtMul();
        this.bean = bean;
        this.height = height;
        Log.i("height", height + " ");
        this.width = width;
        this.textSize = textSize;
        this.activity = fragment.getActivity();
        density = "hdpi";
        this.fragment = fragment;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        globalPref = GlobalPref.getInstance(fragment.getActivity());

        colorSelect = globalPref.getCountry().equalsIgnoreCase("LAGOS") ? "#00304b" : "#42A085";
        colorDeSelect = globalPref.getCountry().equalsIgnoreCase("LAGOS") ? "#88d5ff" : "#CAEEE4";
    }

    public OneTwelveGameAdapter(Activity a, GamePlay gamePlay, Button ok,
                                String density) {
        this.activity = a;
        // this.gamePlay = gamePlay;
        this.ok = ok;
        this.density = density;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return numbersSelected.length;
    }

    @Override
    public Object getItem(int position) {
        return numbersSelected[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        View vi = convertView;
        formatter = new DecimalFormat("00");
        if (convertView == null) {
            vi = inflater.inflate(R.layout.fast_game_row, null);
            params = new AbsListView.LayoutParams(width, height);
        } else
            vi = (View) convertView;
        LinearLayout mainLayout = (LinearLayout) vi.findViewById(R.id.main_lay);
        mainLayout.setLayoutParams(params);
        mainLayout.setGravity(Gravity.CENTER);

        final CustomTextView fastNumber = (CustomTextView) vi
                .findViewById(R.id.fast_number);
        final CustomTextView inc = (CustomTextView) vi.findViewById(R.id.inc);
        final CustomTextView dec = (CustomTextView) vi.findViewById(R.id.dec);
        final CustomTextView selectedNos = (CustomTextView) vi
                .findViewById(R.id.no_selected);
        fastNumber.setTextSize(textSize[0]);
        numFast = pos + 1;
        fastNumber.setText(formatter.format(numFast) + "");
        if (numbersSelected[pos] <= 0) {
            fastNumber.setTextColor(Color.parseColor(colorDeSelect));
        } else {
            fastNumber.setTextColor(Color.parseColor(colorSelect));
        }
        inc.setTextSize(textSize[1]);
        dec.setTextSize(textSize[1]);
        selectedNos.setTextSize(textSize[1]);
        freq = numbersSelected[pos];
        selectedNos.setText(formatter.format(numbersSelected[pos]) + "");
        inc.setOnClickListener(getOnClickInc(selectedNos, pos, fastNumber));
        dec.setOnClickListener(getOnClickDec(selectedNos, pos, fastNumber));


        //for long press
        // Auto increment for a long click
        View.OnLongClickListener clicklist = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                synchronized (this) {
                    switch (v.getId()) {
                        case R.id.inc:
                            if (!autoIncrement) {
                                autoIncrement = true;
                                repeatUpdateHandler.post(new RepetetiveUpdater(0, maxNumberToBeSelected, 1, R.id.inc, selectedNos, pos, fastNumber));
                            }
                            break;
                        case R.id.dec:
                            if (!autoDecrement) {
                                autoDecrement = true;
                                repeatUpdateHandler.post(new RepetetiveUpdater(0, maxNumberToBeSelected, 1, R.id.dec, selectedNos, pos, fastNumber));
                            }
                            break;

                        default:
                            return true;
                    }
                }
                return false;
            }
        };

        View.OnTouchListener touchlist = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (v.getId()) {
                    case R.id.inc:
                        //previous if (event.getAction() == MotionEvent.ACTION_UP)
                        if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) && autoIncrement) {
                            autoIncrement = false;
                        }
                        break;
                    case R.id.dec:
                        if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) && autoDecrement) {
                            autoDecrement = false;
                        }
                        break;

                }
                return false;
            }
        };


        long lastClickTime = mLastClickTime;
        long now = System.currentTimeMillis();
        mLastClickTime = now;
        if (now - lastClickTime < MIN_DELAY_MS) {
            inc.setOnLongClickListener(clicklist);
            dec.setOnLongClickListener(clicklist);
            dec.setOnTouchListener(touchlist);
            inc.setOnTouchListener(touchlist);
        }

        return vi;
    }


    private boolean onTablet() {
        int intScreenSize = activity.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        return (intScreenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) // LARGE
                || (intScreenSize == Configuration.SCREENLAYOUT_SIZE_LARGE + 1); // Configuration.SCREENLAYOUT_SIZE_XLARGE
    }

    private void incDecHandler(int min, int max, int numberGap, int clickedId, CustomTextView CustomTextView, int pos,
                               final CustomTextView fastNumber) {
        switch (clickedId) {
            case R.id.inc:
                increment(max, numberGap, CustomTextView, pos, fastNumber);
                break;
            case R.id.dec:
                decrement(min, numberGap, CustomTextView, pos, fastNumber);
                break;

        }
    }

    public void increment(int max, int numberGap,
                          CustomTextView CustomTextView, int pos,
                          final CustomTextView fastNumber) {
        int value = Integer.parseInt(CustomTextView.getText().toString());
        if (isMaxSelected(numbersSelected)) {
            Utils.Toast(activity, "Max " + maxNumberToBeSelected + " Numbers Selected");
        } else {
            if (value < max) {
                value += numberGap;
                numbersSelected[pos] = Integer.parseInt(formatter.format(value));
                CustomTextView.setText(formatter.format(value) + "");
                fastNumber.setTextColor(Color.parseColor(colorSelect));
                gameCalcilation();
            }

        }
    }

    public void decrement(int min, int numberGap,
                          CustomTextView CustomTextView, int pos,
                          final CustomTextView fastNumber) {
        int value1 = Integer.parseInt(CustomTextView.getText().toString());
        if (value1 > min) {
            value1 -= numberGap;
            if (value1 == 0) {
                fastNumber.setTextColor(Color.parseColor(colorDeSelect));
            } else {
                fastNumber.setTextColor(Color.parseColor(colorSelect));
            }
            numbersSelected[pos] = Integer.parseInt(formatter.format(value1));
            CustomTextView.setText(formatter.format(value1) + "");
            gameCalcilation();
        } else {
            fastNumber.setTextColor(Color.parseColor(colorDeSelect));
        }
    }

    private void gameCalcilation() {
        ((OneToTwelveGameScreen) fragment).selectedBetBean.setCurrentNos(((OneToTwelveGameScreen) fragment).getPickedNo());
        ((OneToTwelveGameScreen) fragment).numberSelected = ((OneToTwelveGameScreen) fragment).getNoSelected(R.id.select_nos);
        ((OneToTwelveGameScreen) fragment).gameAmtCalculation(((OneToTwelveGameScreen) fragment).selectedBetBean);
    }

    private View.OnClickListener getOnClickInc(final CustomTextView CustomTextView,
                                               final int pos, final CustomTextView fastNumber) {
        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                incDecHandler(0, maxNumberToBeSelected, 1, R.id.inc, CustomTextView, pos,
                        fastNumber);
            }
        };
        return clickListener;
    }

    private View.OnClickListener getOnClickDec(final CustomTextView CustomTextView,
                                               final int pos, final CustomTextView fastNumber) {
        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                incDecHandler(0, maxNumberToBeSelected, 1, R.id.dec, CustomTextView, pos,
                        fastNumber);
            }
        };
        return clickListener;
    }

    private boolean isMaxSelected(int[] data) {
        int totalSum = 0;
        for (int i = 0; i < data.length; i++) {
            totalSum += data[i];
        }
        if (totalSum >= maxNumberToBeSelected) {
            return true;
        } else {
            return false;
        }
    }

    class RepetetiveUpdater implements Runnable {
        int min, max, clickedId, pos, numberGap;
        CustomTextView customTextView, fastNumber;

        public RepetetiveUpdater(int min, int max, int numberGap, int clickedId, CustomTextView customTextView, int pos,
                                 final CustomTextView fastNumber) {
            this.min = min;
            this.max = max;
            this.clickedId = clickedId;
            this.numberGap = numberGap;
            this.pos = pos;
            this.customTextView = customTextView;
            this.fastNumber = fastNumber;
        }

        public void run() {
            if (autoIncrement) {
                incDecHandler(0, maxNumberToBeSelected, 1, R.id.inc, customTextView, pos, fastNumber);
                repeatUpdateHandler.postDelayed(new RepetetiveUpdater(0, maxNumberToBeSelected, 1, R.id.inc, customTextView, pos, fastNumber), 100);
            } else if (autoDecrement) {
                incDecHandler(0, maxNumberToBeSelected, 1, R.id.dec, customTextView, pos, fastNumber);
                repeatUpdateHandler.postDelayed(new RepetetiveUpdater(0, maxNumberToBeSelected, 1, R.id.inc, customTextView, pos, fastNumber), 100);
            }
        }
    }

}

