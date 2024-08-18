package com.skilrock.scratch;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.skilrock.adapters.ScratchGameAdapter;
import com.skilrock.bean.RetailerListModal;
import com.skilrock.bean.ScratchGameBean;
import com.skilrock.config.Config;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.escratch.customui.LoadingDialog;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.locateretailer.LocateRetailer;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class ScratchCards extends DrawerBaseActivity {
    private ListView scratchList;
    private ArrayList<ScratchGameBean.ScratchGameData> gameDatas;
    private Target target;
    private LoadingDialog dialog;
    private Context context;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scratch);
        context = this;
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.SCRATCH_CARDS);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);

        dialog = new LoadingDialog(context);
        dialog.setMessage("");
        gameDatas = (ArrayList<ScratchGameBean.ScratchGameData>) getIntent().getSerializableExtra("data");
        sHeader();
        setDrawerItems();
        bindViewIds();
        locateRetailerScratch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        LocateRetailer.class);
                RetailerListModal listModal = new RetailerListModal();
                RetailerListModal.RetailerList modal = listModal.new RetailerList();
                modal.setAddr_1("Dlf Cyber City");
                modal.setPhoneNbr("+91 124 2235144");
                modal.setLastName("Technologies");
                modal.setMobileNbr("8802537403");
                modal.setAddr_2("Gurgaon");
                modal.setFirstName("Skilrock");
                modal.setEmail_id("aa@bb.com");
                modal.setType("Retailer");
                modal.setLatitude("28.497321");
                modal.setLongitude("77.093371");
                modal.setService(null);//new String[]{"Scratch Sale","Sports Lottery","High Winning,Cash in"});
                intent.putExtra("isScratchRet", true);
                listModal.setErrorMsg("");
                listModal.setSuccess(true);
                ArrayList<RetailerListModal.RetailerList> lists = new ArrayList<RetailerListModal.RetailerList>();
                lists.add(modal);
                listModal.setRetailerList(lists);
                listModal.setLat("28.497321");
                listModal.setLng("77.093371");
                intent.putExtra("modal", listModal);
                startActivity(intent);
                overridePendingTransition(GlobalVariables.startAmin,
                        GlobalVariables.endAmin);
            }
        });
        scratchList.setAdapter(new ScratchGameAdapter(getApplicationContext(),
                R.layout.scratch_row, gameDatas));
        scratchList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                dialog.show();
                target = new Target() {


                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                        dialog.dismiss();
                        analytics.sendAll(Fields.Category.SCRATCH_CARD, Fields.Action.GET, Fields.Label.SCRATCH_CARD_GAMES + ": " + gameDatas.get(position).getGameName());

                        ScratchDialog dialog = new ScratchDialog(ScratchCards.this,
                                gameDatas.get(position).getGameName(), gameDatas.get(
                                position).getFullGameImagePath(), bitmap);
                        dialog.show();
                    }

                    @Override
                    public void onBitmapFailed(Drawable drawable) {
                        if (Config.isStatic) {
                            Utils.Toast(context, "Data not available in offline mode");
                        }

                    }

                    @Override
                    public void onPrepareLoad(Drawable drawable) {

                    }
                };
                Picasso.with(ScratchCards.this).load(Config.getInstance().getBaseURL() + gameDatas.get(
                        position).getFullGameImagePath()).into(target);
            }
        });
        headerSubText.setText("RESULT");
        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        headerNavigation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerText.setText("Scratch Cards");
    }


    @Override
    protected void onResume() {
        super.onResume();
        manageHeader();
    }

    OnClickListener commonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
        }
    };

    private void bindViewIds() {
        scratchList = (ListView) findViewById(R.id.scratch_list);
    }

    private void manageHeader() {
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.GONE);
        headerSpinner.setVisibility(View.INVISIBLE);
        headerText.setVisibility(View.VISIBLE);
        headerSubText.setVisibility(View.GONE);
        locateRetailerScratch.setVisibility(View.GONE);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }

    @Override
    public void onDestroy() {  // could be in onPause or onStop
        Picasso.with(this).cancelRequest(target);
        super.onDestroy();
    }
}
