package com.skilrock.customui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.skilrock.adapters.BetAdapater;
import com.skilrock.adapters.BonusButtonAdapter;
import com.skilrock.bean.BetTypeBean;
import com.skilrock.config.Config;
import com.skilrock.config.DirectDissmissListener;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.DataSource;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import java.util.HashMap;

public class BonusNumberDialog extends AlertDialog {
    private Context context;
    private ExpandableGridView gridView;
    private StringBuffer buffer;
    private String numbers;
    private HashMap<String, String> data;
    private BetAdapater betAdapater;
    private String[][] selectedNos;
    private ImageView done, close;
    public static String selectedBet;

    private Activity activity;
    private double widthForGridChild;
    private double heightForGridChild;
    private int textSize;
    private BetTypeBean typeBean;
    private RelativeLayout footerRL;
    private int height;
    private int width;
    private Button backB, saveB;
    private int pos;
    private DirectDissmissListener directDissmissListener;
    private String gameCode;

    public BonusNumberDialog(Context context, double widthForGridChild, double heightForGridChild, int textSize, String[][] selectedNos, int pos, Fragment fragment) {
        super(context, R.style.DialogZoomAnim);
        this.context = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.widthForGridChild = widthForGridChild;
        this.heightForGridChild = heightForGridChild;
        this.textSize = textSize;
        this.selectedNos = selectedNos;
        this.pos = pos;
        directDissmissListener = (DirectDissmissListener) fragment;
    }


    @Override
    public void show() {
        super.show();
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = (int) (displaymetrics.widthPixels * 0.90);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width;
        getWindow().setAttributes(params);

        heightForGridChild = displaymetrics.heightPixels;
        widthForGridChild = width;
        float density = context.getResources().getDisplayMetrics().density;
        if (density <= 0.75) {
            heightForGridChild = displaymetrics.heightPixels - (int) (displaymetrics.density * 18);
            widthForGridChild = displaymetrics.widthPixels - (int) (displaymetrics.density * 18);
        }
        heightForGridChild = heightForGridChild - GlobalVariables.getPx(10, context);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //   widthForGridChild = getWindow().getDecorView().getWidth();
            }
        }, 100);
        //widthForGridChild = widthForGridChild - GlobalVariables.getPx(10, context);
//        Window window = this.getWindow();
//        window.setLayout((int) widthForGridChild, LinearLayout.LayoutParams.WRAP_CONTENT);
//        getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        final View view = ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.bonus_no_selection_dialog, null);
        View headerView = view.findViewById(R.id.header_id);
        backB = (Button) view.findViewById(R.id.back);
        saveB = (Button) view.findViewById(R.id.save);
        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataSource.Keno.numbersSelected < 6) {
                    Utils.Toast(context, context.getResources().getString(R.string.plz_sel_six_no));
                } else {
                    String[] strings = new String[6];
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0; i < DataSource.numbersForSix.length; i++) {
                        if (DataSource.numbersForSix[i] == 1) {
                            stringBuffer.append(i + 1 + ",");
                        }
                    }
                    strings = stringBuffer.toString().split(",");
                    selectedNos[pos] = strings;
                    directDissmissListener.onDismissCustomDiloag(selectedNos);
                    dismiss();
                }
            }
        });
        done = (ImageView) headerView.findViewById(R.id.done);
        close = (ImageView) headerView.findViewById(R.id.close);
        done.setVisibility(View.INVISIBLE);
        close.setVisibility(View.INVISIBLE);
        gridView = (ExpandableGridView) view.findViewById(R.id.no_grid);
        footerRL = (RelativeLayout) view.findViewById(R.id.footer);
        heightForGridChild = heightForGridChild - context.getResources().getDimension(R.dimen.dialog_header_height) - context.getResources().getDimension(R.dimen.game_footer_height);

        if (gameCode.equalsIgnoreCase(Config.bonusGameName))
            DataSource.numbersForSix = new int[42];
        else
            DataSource.numbersForSix = new int[36];

        for (int i = 0; i < selectedNos[pos].length; i++) {
            if (!selectedNos[pos][i].equals(""))
                DataSource.numbersForSix[Integer.parseInt(selectedNos[pos][i]) - 1] = 1;
        }
        gridView.setAdapter(new BonusButtonAdapter((Activity) context,
                (int) widthForGridChild / 6, (int) widthForGridChild / 6,
                textSize, selectedNos[pos], saveB));
        gridView.setExpanded(true);
        this.setContentView(view);
        ((CustomTextView) view.findViewById(R.id.header_name))
                .setText(context.getResources().getString(R.string.sel_numb));
    }

    public void gameCode(String gameCode) {
        this.gameCode = gameCode;
    }
}