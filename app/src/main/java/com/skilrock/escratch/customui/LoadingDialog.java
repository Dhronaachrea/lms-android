package com.skilrock.escratch.customui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skilrock.customui.MyAnimDrawable;
import com.skilrock.lms.ui.R;

/**
 * Created by stpl on 8/8/2015. Updated by Vishnu on 16/02/2016
 */
public class LoadingDialog extends Dialog {

    private MyAnimDrawable flyAnimationDrawable1;
    private ImageView animView;
    private TextView loadingText;

    public LoadingDialog(Context context) {
        super(context, android.R.style.Theme_Black_NoTitleBar);
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.custom_progress_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        animView = (ImageView) findViewById(R.id.anim);
        int[] animRes = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.i, R.drawable.j, R.drawable.k, R.drawable.l, R.drawable.m};
        try {
            flyAnimationDrawable1 = new MyAnimDrawable();
            for (int i = 0; i < animRes.length; i++) {
                Drawable drawable = getContext().getResources().getDrawable(animRes[i]);
                flyAnimationDrawable1.addFrame(drawable, 40);
            }
            flyAnimationDrawable1.setOneShot(false);
            animView.setImageDrawable(flyAnimationDrawable1);
            ((Animatable) animView.getDrawable()).start();

        } catch (OutOfMemoryError e) {
            animView.setVisibility(View.GONE);
        } catch (Exception e1) {
            animView.setVisibility(View.GONE);
        }

    }

    public void setMessage(String message) {
        if (loadingText == null)
            loadingText = (TextView) findViewById(R.id.loading_text);
        loadingText.setText(message != null && !message.equals("") ? message : "Loading...");
    }

}
