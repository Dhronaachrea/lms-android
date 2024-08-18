package com.skilrock.escratch;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.escratch.util.ImageLoader;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by stpl on 8/7/2015.
 */
public class IGETicketActivity extends DrawerBaseActivity {

    private ImageView image;
    private LinearLayout progressBar;
    private String url;
    private CustomTextView subHeaderText, header, ticketNumber;
    public static String IGE_IMAGE_URL = "ige_image_url";
    private String ticketStatus = "";
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        imageLoader= new ImageLoader(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ige_ticket_activity);
        sHeader();
        setDrawerItems();
        manageHeader();
        image = (ImageView) findViewById(R.id.img_ige_ticket);
        progressBar = (LinearLayout) findViewById(R.id.prog_ige_loading);
        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        header = (CustomTextView) findViewById(R.id.header_text);
        subHeaderText = (CustomTextView) findViewById(R.id.header_sub_text);
        ticketNumber = (CustomTextView) findViewById(R.id.ticketNumber);
        subHeaderText.setVisibility(View.GONE);
        header.setText(getString(R.string.track_tckts));
        header.setTypeface(null, Typeface.BOLD);
        if (getIntent().hasExtra("gameName")) {
            subHeaderText.setText(getIntent().getStringExtra("gameName"));
            subHeaderText.setVisibility(View.VISIBLE);
        } else
            subHeaderText.setVisibility(View.GONE);

        if (getIntent().hasExtra("tktNum"))
            ticketStatus = ticketStatus.concat("Ticket No. : " + getIntent().getStringExtra("tktNum"));
        ticketStatus = ticketStatus.concat("\nPurchase Time : " + getIntent().getStringExtra("purchaseTime"));
        ticketStatus = ticketStatus.concat("\nPurchase Amount : " + VariableStorage.GlobalPref.getStringData(IGETicketActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + getIntent().getStringExtra("tcktAmt"));
        if (getIntent().hasExtra("winAmnt")) {
            ticketStatus = ticketStatus.concat("\nWinning Amount : " + VariableStorage.GlobalPref.getStringData(IGETicketActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + getIntent().getStringExtra("winAmnt"));
        }
        if (ticketStatus != "") {
            ticketNumber.setText(ticketStatus);
            ticketNumber.setVisibility(View.VISIBLE);
        }

        headerNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        url = getIntent().getStringExtra(IGE_IMAGE_URL);
        if (url != null && !url.equals("")) {
            if(Config.isStatic){
                new FetchImageFromURLTask().execute(url);
            }else{
                Picasso.with(this).load(url).into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        Utils.Toast(IGETicketActivity.this, "Some Internal Error !!");
                        finish();
                    }
                });
            }
        } else {
            Utils.Toast(IGETicketActivity.this, "Some Internal Error !!");
            finish();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        manageHeader();
    }

    private void manageHeader() {
        locateRetailerScratch.setVisibility(View.GONE);
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.GONE);
        headerSpinner.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
//        headerSubText.setVisibility(View.GONE);
    }

    private class FetchImageFromURLTask extends AsyncTask<String, Void, Bitmap> {

        private String imgDesc = "";

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //  this.imgDesc = params[1];
            String path = params[0];
            return imageLoader.getImageInBitmap(path);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            progressBar.setVisibility(View.GONE);
            image.setImageBitmap(bitmap);
        }
    }
}
