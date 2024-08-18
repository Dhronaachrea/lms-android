package com.skilrock.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.skilrock.bean.GamePlay;
import com.skilrock.customui.CustomTextView;
import com.skilrock.utils.Utils;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.DataSource;

public class BonusButtonAdapter extends BaseAdapter {
    private Activity activity;
    // private GamePlay gamePlay;
    private Button ok;
    private String density;
    private static LayoutInflater inflater = null;
    private int height;
    private int width;
    private AbsListView.LayoutParams params;
    private int textSize;
    private String[] selectedNos;
    private int count;
    private Button saveB;

    public BonusButtonAdapter(Activity a, int width, int height, int textSize,
                              String[] selectedNos, Button saveB) {
        this.selectedNos = selectedNos;
        this.height = height;
        Utils.logPrint("height:-" + height);
        this.width = width;
        this.textSize = textSize;
        this.saveB = saveB;
        this.activity = a;
        density = "hdpi";
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < selectedNos.length; i++) {
            if (!selectedNos[i].equals("")) {
                count++;
            }
        }
        DataSource.Keno.numbersSelected = count;
    }

    public BonusButtonAdapter(Activity a, GamePlay gamePlay, Button ok,
                              String density) {
        this.activity = a;
        // this.gamePlay = gamePlay;
        this.ok = ok;
        this.density = density;
        DataSource.Keno.numbersSelected = 0;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return DataSource.numbersForSix.length;
    }

    @Override
    public Object getItem(int position) {
        return DataSource.numbersForSix[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.game_buttons, null);
            params = new AbsListView.LayoutParams(width, height);
        } else
            vi = (View) convertView;
        LinearLayout mainLayout = (LinearLayout) vi.findViewById(R.id.main_lay);
        mainLayout.setLayoutParams(params);
        mainLayout.setGravity(Gravity.CENTER);

        final CustomTextView button = (CustomTextView) vi
                .findViewById(R.id.game_button);
        button.setText(String.valueOf(pos + 1));
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        if (DataSource.numbersForSix[pos] == 1) {
            button.setTextColor(activity.getResources().getColor(R.color.white));
            button.setBackgroundDrawable(activity.getResources().getDrawable(
                    R.drawable.selected_nos_bg));
        } else if (DataSource.numbersForSix[pos] == -1) {// Pre Selected
            button.setTextColor(activity.getResources().getColor(
                    R.color.grey_selected));
            button.setBackgroundDrawable(activity.getResources().getDrawable(
                    R.drawable.buttons_normal));
        } else {
            button.setTextColor(activity.getResources().getColor(
                    R.color.buy_option_d_color));
            button.setBackgroundDrawable(activity.getResources().getDrawable(
                    R.drawable.select_no_bg_d));
        }
        if (DataSource.Keno.numbersSelected < 6)
            enableSave(false);
        else
            enableSave(true);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataSource.Keno.numbersSelected < 6) {
                    if (DataSource.numbersForSix[pos] == 0) {
                        DataSource.numbersForSix[pos] = 1;
                        DataSource.Keno.numbersSelected += 1;
                        button.setTextColor(activity.getResources().getColor(
                                R.color.white));
                        button.setBackgroundDrawable(activity.getResources()
                                .getDrawable(R.drawable.selected_nos_bg));
                    } else if (DataSource.numbersForSix[pos] == 1) {
                        DataSource.numbersForSix[pos] = 0;
                        DataSource.Keno.numbersSelected -= 1;
                        button.setTextColor(activity.getResources().getColor(
                                R.color.buy_option_d_color));
                        button.setBackgroundDrawable(activity.getResources()
                                .getDrawable(R.drawable.select_no_bg_d));
                    }
                    if (DataSource.Keno.numbersSelected < 6)
                        enableSave(false);
                    else
                        enableSave(true);
                } else {
                    if (DataSource.numbersForSix[pos] == 1) {
                        enableSave(false);
                        DataSource.numbersForSix[pos] = 0;
                        DataSource.Keno.numbersSelected -= 1;
                        button.setTextColor(activity.getResources().getColor(
                                R.color.buy_option_d_color));
                        button.setBackgroundDrawable(activity.getResources()
                                .getDrawable(R.drawable.select_no_bg_d));
                    } else {
                        enableSave(true);
                    }
                }
//                if (type == 1) {
//                    bean.setCurrentNos(BonusGameScreen.getPickedNo());
//                    BonusGameScreen.numberSelected = bean.getCurrentNos()[0].equals("") ? 0 : bean.getCurrentNos().length;
//                    BonusGameScreen.gameAmtCalculation(bean);
//                }
            }
        });

        return vi;
    }

    private void enableSave(boolean b) {
        if (b) {
            saveB.setEnabled(true);
            saveB.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.dialog_button_bg));
            saveB.setTextColor(Color.WHITE);
        } else {
            saveB.setEnabled(false);
            saveB.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.dialog_button_bg_disable));
            saveB.setTextColor(Color.parseColor("#9678a6"));
        }
    }

    private boolean onTablet() {
        int intScreenSize = activity.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        return (intScreenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) // LARGE
                || (intScreenSize == Configuration.SCREENLAYOUT_SIZE_LARGE + 1); // Configuration.SCREENLAYOUT_SIZE_XLARGE
    }
}
