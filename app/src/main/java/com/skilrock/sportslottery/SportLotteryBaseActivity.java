package com.skilrock.sportslottery;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skilrock.lms.ui.R;

public class SportLotteryBaseActivity extends Activity implements View.OnClickListener {

    private ImageView imgBack;
    private ImageView imgGameLogo;
    private TextView txtGameName;
    private RelativeLayout header;

    protected void bindIds(){
        header = (RelativeLayout) findViewById(R.id.header);
        imgBack = (ImageView) header.findViewById(R.id.img_back);
        imgGameLogo = (ImageView) header.findViewById(R.id.img_game_icon);
        txtGameName = (TextView) header.findViewById(R.id.txt_game_name);
    }

    protected void addListeners(){
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.img_back){

            return;
        }
    }

    public void setGameLogo(int resourceId){
        imgGameLogo.setImageResource(resourceId);
    }

    public void setGameName(String gameName){
        txtGameName.setText(gameName);
    }
}
