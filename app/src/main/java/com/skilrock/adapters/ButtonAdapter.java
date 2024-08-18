package com.skilrock.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.skilrock.bean.BetTypeBean;
import com.skilrock.bean.GamePlay;
import com.skilrock.customui.CustomTextView;
import com.skilrock.drawgame.TenByTwenty.TenByTwentyGameScreen;
import com.skilrock.drawgame.bonusLotto_keno_SixThirtySix.BonusKenoGameScreen;
import com.skilrock.drawgame.bonuslotto.BonusGameScreen;
import com.skilrock.drawgame.five.FiveGameScreen;
import com.skilrock.drawgame.fiveLagos.FiveGameScreenLagos;
import com.skilrock.drawgame.kenoSeven.TenByNinetyGameScreen;
import com.skilrock.drawgame.tenByEighty.TenByEightyGameScreen;
import com.skilrock.drawgame.tenByThirty.TenByThirtyGameScreen;
import com.skilrock.drawgame.twelve.TwelveGameScreen;
import com.skilrock.utils.Utils;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.DataSource;

public class ButtonAdapter extends BaseAdapter {
    private Activity activity;
    // private GamePlay gamePlay;
    private Button ok;
    private String density;
    private static LayoutInflater inflater = null;
    private int height;
    private int width;
    private AbsListView.LayoutParams params;
    private int textSize;
    private BetTypeBean bean;
    private int type;
    private boolean check;
    private Fragment fragment;

    public ButtonAdapter(Fragment fragment, int width, int height, int textSize,
                         BetTypeBean bean, int type) {
        this.bean = bean;
        this.fragment = fragment;
        this.height = height;
        Utils.logPrint("height:-" + height + " ");
        this.width = width;
        this.textSize = textSize;
        this.activity = fragment.getActivity();
        this.type = type;
        density = "hdpi";
        check = true;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ButtonAdapter(Activity activity, int width, int height, int textSize,
                         BetTypeBean bean, int type) {
        this.bean = bean;
        this.height = height;
        Utils.logPrint("height:-" + height + " ");
        this.width = width;
        this.textSize = textSize;
        this.activity = activity;
        this.type = type;
        density = "hdpi";
        check = true;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ButtonAdapter(Activity a, GamePlay gamePlay, Button ok,
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
        return DataSource.numbers.length;
    }

    @Override
    public Object getItem(int position) {
        return DataSource.numbers[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
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

        if (DataSource.numbers[pos] == 1) {
            button.setTextColor(activity.getResources().getColor(R.color.white));
            button.setBackgroundDrawable(activity.getResources().getDrawable(
                    R.drawable.selected_nos_bg));
        } else if (DataSource.numbers[pos] == -1) {// Pre Selected
            button.setTextColor(activity.getResources().getColor(
                    R.color.white));
            button.setBackgroundDrawable(activity.getResources().getDrawable(
                    R.drawable.disable_nos_bg));
        } else {
            button.setTextColor(activity.getResources().getColor(
                    R.color.buy_option_d_color));
            button.setBackgroundDrawable(activity.getResources().getDrawable(
                    R.drawable.select_no_bg_d));
        }

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hide_keyboard(activity);
                if (check) {
                    if (DataSource.Keno.numbersSelected < bean.getMaxNo()) {
                        if (DataSource.numbers[pos] == 0) {
                            DataSource.numbers[pos] = 1;
                            DataSource.Keno.numbersSelected += 1;
                            button.setTextColor(activity.getResources().getColor(
                                    R.color.white));
                            button.setBackgroundDrawable(activity.getResources()
                                    .getDrawable(R.drawable.selected_nos_bg));
                        } else if (DataSource.numbers[pos] == 1) {
                            DataSource.numbers[pos] = 0;
                            DataSource.Keno.numbersSelected -= 1;
                            button.setTextColor(activity.getResources().getColor(
                                    R.color.buy_option_d_color));
                            button.setBackgroundDrawable(activity.getResources()
                                    .getDrawable(R.drawable.select_no_bg_d));
                        }

                    } else {
                        if (DataSource.numbers[pos] == 1) {
                            DataSource.numbers[pos] = 0;
                            DataSource.Keno.numbersSelected -= 1;
                            button.setTextColor(activity.getResources().getColor(
                                    R.color.buy_option_d_color));
                            button.setBackgroundDrawable(activity.getResources()
                                    .getDrawable(R.drawable.select_no_bg_d));
                        } else {
//                        if (Config.COUNTRY.equalsIgnoreCase("ghana"))
//                            Toast.makeText(
//                                    activity,
//                                    "You can only select " + bean.getMaxNo()
//                                            + " numbers only", Toast.LENGTH_SHORT).show();
//                        else if (Config.COUNTRY.equalsIgnoreCase("Zim"))
                            Utils.Toast(activity,
                                    "You can select max " + bean.getMaxNo()
                                            + " number(s) only");
                        }
                    }
                    switch (type) {
                        case 0:
                            if (DataSource.Keno.numbersSelected >= bean
                                    .getMinNo()) {
                                bean.setCurrentNos(((BonusGameScreen) (fragment)).getPickedNo());
                            } else {
                                bean.setCurrentNos(new String[]{});
                            }
                            ((BonusGameScreen) (fragment)).numberSelected = bean.getCurrentNos().length == 0 ? 0 : bean.getCurrentNos().length;
                            ((BonusGameScreen) (fragment)).gameAmtCalculation(bean);
                            break;
                        case 1:
                            if (DataSource.Keno.numbersSelected >= bean
                                    .getMinNo()) {
                                bean.setCurrentNos(((FiveGameScreen) fragment).getPickedNo());
                            } else {
                                bean.setCurrentNos(new String[]{});
                            }
                            ((FiveGameScreen) fragment).numberSelected = bean.getCurrentNos().length == 0 ? 0 : bean.getCurrentNos().length;
                            ((FiveGameScreen) fragment).gameAmtCalcuation(bean);
                            break;
                        case 2:
                            if (DataSource.Keno.numbersSelected >= bean
                                    .getMinNo()) {
                                bean.setCurrentNos(((TwelveGameScreen) fragment).getPickedNo());
                            } else {
                                bean.setCurrentNos(new String[]{});
                            }
                            ((TwelveGameScreen) fragment).numberSelected = bean.getCurrentNos().length == 0 ? 0 : bean.getCurrentNos().length;
                            ((TwelveGameScreen) fragment).gameAmtCalculation(bean);
                            break;
                        case 3:
                            if (DataSource.Keno.numbersSelected >= bean
                                    .getMinNo()) {
                                bean.setCurrentNos(((FiveGameScreenLagos) fragment).getPickedNo());
                            } else {
                                bean.setCurrentNos(new String[]{});
                            }
                            ((FiveGameScreenLagos) fragment).numberSelected = bean.getCurrentNos().length == 0 ? 0 : bean.getCurrentNos().length;
                            ((FiveGameScreenLagos) fragment).gameAmtCalculation(bean);
                            break;
                        case 4:
                            if (DataSource.Keno.numbersSelected >= bean
                                    .getMinNo()) {
                                bean.setCurrentNos(((TenByNinetyGameScreen) fragment).getPickedNo());
                            } else {
                                bean.setCurrentNos(new String[]{});
                            }
                            ((TenByNinetyGameScreen) fragment).numberSelected = bean.getCurrentNos().length == 0 ? 0 : bean.getCurrentNos().length;
                            ((TenByNinetyGameScreen) fragment).gameAmtCalculation(bean);
                            break;
                        case 5:
                            if (DataSource.Keno.numbersSelected >= bean
                                    .getMinNo()) {
                                bean.setCurrentNos(((TenByThirtyGameScreen) fragment).getPickedNo());
                            } else {
                                bean.setCurrentNos(new String[]{});
                            }
                            ((TenByThirtyGameScreen) fragment).numberSelected = bean.getCurrentNos().length == 0 ? 0 : bean.getCurrentNos().length;
                            ((TenByThirtyGameScreen) fragment).gameAmtCalculation(bean);
                            break;
                        case 6:
                            if (DataSource.Keno.numbersSelected >= bean
                                    .getMinNo()) {
                                bean.setCurrentNos(((BonusKenoGameScreen) fragment).getPickedNo());
                            } else {
                                bean.setCurrentNos(new String[]{});
                            }
                            ((BonusKenoGameScreen) fragment).numberSelected = bean.getCurrentNos().length == 0 ? 0 : bean.getCurrentNos().length;
                            ((BonusKenoGameScreen) fragment).gameAmtCalculation(bean);
                            break;
                        case 7:
                            if (DataSource.Keno.numbersSelected >= bean
                                    .getMinNo()) {
                                bean.setCurrentNos(((TenByTwentyGameScreen) fragment).getPickedNo());
                            } else {
                                bean.setCurrentNos(new String[]{});
                            }
                            ((TenByTwentyGameScreen) fragment).numberSelected = bean.getCurrentNos().length == 0 ? 0 : bean.getCurrentNos().length;
                            ((TenByTwentyGameScreen) fragment).gameAmtCalculation(bean);
                            break;
                        case 8:
                            if (DataSource.Keno.numbersSelected >= bean
                                    .getMinNo()) {
                                bean.setCurrentNos(((TenByEightyGameScreen) fragment).getPickedNo());
                            } else {
                                bean.setCurrentNos(new String[]{});
                            }
                            ((TenByEightyGameScreen) fragment).numberSelected = bean.getCurrentNos().length == 0 ? 0 : bean.getCurrentNos().length;
                            ((TenByEightyGameScreen) fragment).gameAmtCalculation(bean);
                            break;
                    }
                }
            }
        });

        return vi;
    }

    private boolean onTablet() {
        int intScreenSize = activity.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        return (intScreenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) // LARGE
                || (intScreenSize == Configuration.SCREENLAYOUT_SIZE_LARGE + 1); // Configuration.SCREENLAYOUT_SIZE_XLARGE
    }

    public void updateBean(BetTypeBean betTypeBean) {
        this.bean = betTypeBean;
    }

    public void setButtonClickable(boolean check) {
        this.check = check;
        notifyDataSetChanged();
    }

    public static void hide_keyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
