package com.skilrock.customui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.skilrock.adapters.BetAdapater;
import com.skilrock.bean.BetTypeBean;
import com.skilrock.lms.ui.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BetDialog extends AlertDialog {
    private Context context;
    public static int count;
    private ListView contactList;
    private StringBuffer buffer;
    private String numbers;
    private HashMap<String, String> data;
    private BetAdapater betAdapater;
    private ArrayList<BetTypeBean> betTypeBeans;
    private ImageView done, close;
    public static String selectedBet;
    public static int selectedPos;
    //for zim crash
    private TextView betName;

    public BetDialog(Context context, ArrayList<BetTypeBean> betTypeBeans, TextView betName) {
        super(context, R.style.DialogZoomAnim);
        this.context = context;
        this.betTypeBeans = betTypeBeans;
        this.betName = betName;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectedPos = 0;
    }

    public BetDialog(ArrayList<BetTypeBean> betTypeBeans, Context context) {
        super(context, R.style.DialogZoomAnim);
        this.context = context;
        this.betTypeBeans = betTypeBeans;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void show() {
        super.show();
        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        final View view = ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.bet_dialog, null);
        View headerView = view.findViewById(R.id.header_id);
        done = (ImageView) headerView.findViewById(R.id.done);
        close = (ImageView) headerView.findViewById(R.id.close);
        close.setVisibility(View.INVISIBLE);
        done.setImageResource(R.drawable.close);
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        contactList = (ListView) view.findViewById(R.id.contact_list);
        betAdapater = new BetAdapater(context, R.layout.bet_row, betTypeBeans,
                this);
        contactList.setAdapter(betAdapater);
        contactList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectedPos = position;
//                selectedBet = "";
//                selectedBet = betTypeBeans.get(position).getBetDisplayName();
                betName.setText(betTypeBeans.get(position).getBetDisplayName());
                cancel();
            }
        });
        this.setContentView(view);
        ((CustomTextView) view.findViewById(R.id.header_name))
                .setText(context.getResources().getString(R.string.sel_bet));
    }

}