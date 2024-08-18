package com.skilrock.escratch.customui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skilrock.config.Config;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.AutoResizeTextView;
import com.skilrock.customui.CustomTextView;
import com.skilrock.escratch.IGEGameListActivity;
import com.skilrock.escratch.IGEScratchGameActivity;
import com.skilrock.escratch.bean.GameListDataBean;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.LoginActivity;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.CustomTypefaceSpan;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.LotteryPreferences;
import com.skilrock.utils.Utils;
import com.squareup.picasso.Picasso;


public class IGEPurchaseDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private Activity activity;
    private Dialog dialog;
    private GameListDataBean.Games games;
    private TextView playForCash, prize;
    private CustomTextView title;
    private TextView description;
    private ImageView imageView;
    private LinearLayout playBtnForCash;
    private TextView txtTryFree;
    private TextView txtUnfinish;
    private Analytics analytics;

    public IGEPurchaseDialog(Context context, GameListDataBean.Games games) {
        super(context, R.style.DialogNoBorderBackGround);
        this.context = context;
        this.games = games;
        activity = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
        getWindow().setWindowAnimations(R.style.DialogAnimationZoom);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.ige_popup_dialog);
        analytics = new Analytics();
        analytics.startAnalytics(context);
        analytics.setScreenName(Fields.Screen.IGE_DIALOG);
        playForCash = (TextView) findViewById(R.id.playForCash);
        playBtnForCash = (LinearLayout) findViewById(R.id.btnPlayCash);
        prize = (TextView) findViewById(R.id.price);
        txtTryFree = (TextView) findViewById(R.id.txt_try_free);
        txtUnfinish = (TextView) findViewById(R.id.txt_unfinish);
        title = (CustomTextView) findViewById(R.id.title);
        imageView = (ImageView) findViewById(R.id.list_image);
        description = (TextView) findViewById(R.id.desc);
        ((AutoResizeTextView) description).setMinTextSize(GlobalVariables.dpToPx(11.25f, getContext()));
        prize.setTextColor(context.getResources().getColor(R.color.yellow));
        setButtonStyle(getContext());
        setPriceStyle(games.getGamePrice() + "", getContext());
        title.setText(games.getGameName());
        description.setText(games.getGameDescription());
        //description.setMaxLines(3);
//        description).setMinTextSize(9);
        description.setText(games.getGameDescription().replaceAll("\\s+", " ").trim());
        int width = (new LotteryPreferences(activity).getWIDTH()) - GlobalVariables.getPx((int) activity.getResources().getDimension(R.dimen._34sdp), activity);
        Picasso.with(getContext()).load(games.getGameImageLocations().getImagePath()).resize(width / 3, width / 3).placeholder(R.drawable.placeholder).into(imageView);
        playBtnForCash.setOnClickListener(this);
        txtTryFree.setOnClickListener(this);
        txtUnfinish.setOnClickListener(this);
        if (!UserInfo.isLogin(context))
            txtUnfinish.setVisibility(View.GONE);
        else
            txtUnfinish.setVisibility(View.VISIBLE);
    }

    public void setButtonStyle(Context context) {
        SpannableString styledString = new SpannableString("PLAY FOR CASH!!");
        Typeface typefaceThin = Typeface.createFromAsset(context.getAssets(), "ROBOTO-LIGHT.TTF");
        Typeface typefaceBold = Typeface.createFromAsset(context.getAssets(), "ROBOTO-REGULAR.TTF");
        styledString.setSpan(new CustomTypefaceSpan("", typefaceThin), 0, 8, 0);
        styledString.setSpan(new CustomTypefaceSpan("", typefaceBold), 9, 15, 0);
        playForCash.setText(styledString);
    }

    public void setPriceStyle(String price, Context context) {
        SpannableString styledString = new SpannableString(Config.CURRENCY_SYMBOL + "" + price);
        Typeface typefaceThin = Typeface.createFromAsset(context.getAssets(), "ROBOTO-LIGHT.TTF");
        Typeface typefaceBold = Typeface.createFromAsset(context.getAssets(), "ROBOTO-BOLD.TTF");
        styledString.setSpan(new CustomTypefaceSpan("", typefaceThin), 0, 1, 0);
        styledString.setSpan(new CustomTypefaceSpan("", typefaceBold), 1, styledString.length(), 0);
        prize.setText(styledString);
    }


    @Override
    public void onClick(View v) {
        if (!GlobalVariables.connectivityExists(activity)) {
            GlobalVariables.showDataAlert(activity);
            return;
        }
        switch (v.getId()) {
            case R.id.btnPlayCash:
                analytics.sendAction(Fields.Category.IGE_DIALOG, Fields.Category.IGE_PLAY_CASH);
                onClickBuy("Buy");
                break;

            case R.id.txt_try_free:
                analytics.sendAction(Fields.Category.IGE_DIALOG, Fields.Category.IGE_TRY_FREE);
                Intent intent = new Intent(context, IGEScratchGameActivity.class);
                intent.putExtra("games", games);
                intent.putExtra("mode", "Try");
                context.startActivity(intent);
                dismiss();
                break;

            case R.id.txt_unfinish:
                analytics.sendAction(Fields.Category.IGE_DIALOG, Fields.Category.IGE_UNFINISHED);
                onClickUnfinish();
                break;
        }
    }

    private void onClickBuy(String mode) {
        if (!UserInfo.isLogin(context)) {
            activity.startActivityForResult(new Intent(context,
                    LoginActivity.class), 100);
            activity.overridePendingTransition(GlobalVariables.startAmin,
                    GlobalVariables.endAmin);
        } else {
            double balance = Double.parseDouble(AmountFormat.getCorrectAmountFormat(VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_BAL)));
            double purchaseAmount = new Double(games.getGamePrice());
            if (balance < purchaseAmount) {
                Utils.Toast(context, "Available balace is low");
            } else {
                Intent intent = new Intent(context, IGEScratchGameActivity.class);
                intent.putExtra("games", games);
                intent.putExtra("mode", mode);
                context.startActivity(intent);
                dismiss();
            }
        }
    }

    private void onClickUnfinish() {
        if (!UserInfo.isLogin(context)) {
            activity.startActivityForResult(new Intent(context,
                    LoginActivity.class), 100);
            activity.overridePendingTransition(GlobalVariables.startAmin,
                    GlobalVariables.endAmin);
        } else {
            double balance = Double.parseDouble(AmountFormat.getCorrectAmountFormat(VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_BAL)));
            double purchaseAmount = new Double(games.getGamePrice());
//            if (balance < purchaseAmount) {
//                Toast.makeText(context, "Available balace is low", Toast.LENGTH_SHORT).show();
//            } else {
            Intent in = new Intent(context, IGEGameListActivity.class);
            in.putExtra("UNFINISH_GAME_LIST", true);
            context.startActivity(in);
            dismiss();
//            }
        }
    }

    public void login() {
        if (!UserInfo.isLogin(context))
            txtUnfinish.setVisibility(View.GONE);
        else
            txtUnfinish.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(context);
    }
}
