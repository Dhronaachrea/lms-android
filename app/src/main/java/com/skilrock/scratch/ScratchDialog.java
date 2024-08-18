package com.skilrock.scratch;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.ui.R;

public class ScratchDialog extends AlertDialog {
    private final Bitmap bitmap;
    private Context context;
    private ProgressDialog progressDialog;
    // private Button close;
    private ImageView scratchImage;
    private String fullImg;
    private String gamename;
    private AnimationDrawable drawable;

    // private ImageLoader imageLoader;

    public ScratchDialog(Context context, String gameName, String fullImg, Bitmap bitmap) {
        super(context, R.style.DialogZoomAnim);
        this.context = context;
        this.bitmap = bitmap;
        this.fullImg = fullImg;
        this.gamename = gameName;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // imageLoader = new ImageLoader(context);
    }

    @Override
    public void show() {
        super.show();
        final View view = ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.scrath_dialog, null);
        scratchImage = (ImageView) view.findViewById(R.id.scratch_image);

        Display mDisplay = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        final int width = mDisplay.getWidth();
        final int height = mDisplay.getHeight();
        getWindow().setLayout(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        this.setContentView(view);
        ((CustomTextView) view.findViewById(R.id.header_name))
                .setText(gamename);
        ImageView imageView = ((ImageView) view.findViewById(R.id.scratch_image));
        //Picasso.with(context).load(Config.baseURL + fullImg).into(imageView);
        imageView.setImageBitmap(bitmap);
        ((ImageView) view.findViewById(R.id.close)).setVisibility(View.GONE);
        ((ImageView) view.findViewById(R.id.done)).setImageResource(R.drawable.close);
        ((ImageView) view.findViewById(R.id.done))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dismiss();

                    }
                });
    }
}
