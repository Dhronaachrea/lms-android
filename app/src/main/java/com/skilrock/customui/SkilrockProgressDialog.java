package com.skilrock.customui;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.skilrock.lms.ui.R;

public class SkilrockProgressDialog {
	private Context context;
	private CharSequence title;
	private CharSequence message;
	private boolean indeterminate;
	private boolean cancelable;

	private CustomTextView loadingTextView;
	private ImageView progressBar;
	private Dialog commentDialog;

	public SkilrockProgressDialog(Context context, CharSequence title,
			CharSequence message, boolean indeterminate, boolean cancelable) {
		this.context = context;
		this.title = title;
		this.message = message;
		this.indeterminate = indeterminate;
		this.cancelable = cancelable;
	}

	public void show() {
		commentDialog = new Dialog(context);
		commentDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		commentDialog.setContentView(R.layout.widget_progress_dialog);
        // Aplying animation on "Play Now" Button

        Animation animation = AnimationUtils.loadAnimation(
                context, R.anim.roation);


		loadingTextView = (CustomTextView) commentDialog
				.findViewById(R.id.customProgressDialogText);
		progressBar = (ImageView) commentDialog
				.findViewById(R.id.progressBar1);
        progressBar.startAnimation(animation);
		commentDialog.setCancelable(cancelable);
//		progressBar.setIndeterminate(indeterminate);
		loadingTextView.setText(message);
        commentDialog.getWindow().setBackgroundDrawableResource(
                R.drawable.abc_popup_background_mtrl_mult);
		commentDialog.show();
	}

	public void dismiss() {
		commentDialog.dismiss();
	}

}
