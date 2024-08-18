package com.skilrock.customui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;

import com.skilrock.lms.ui.R;
import com.skilrock.utils.GlobalVariables;

public class SimpleDialogBox {

    private Context context;
    private String messages;
    private String headerText;
    private OnClickListener doneClickListener;
    private Dialog commentDialog;

    public SimpleDialogBox(Context context, String messages, String headerText,
                           OnClickListener doneClickListener) {
        this.context = context;
        this.messages = messages;
        this.headerText = headerText;
        this.doneClickListener = doneClickListener;
    }

    // comment Dialog Box Open method

    public void showAlertDialog() {

        commentDialog = new Dialog(context);
        commentDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        commentDialog.setContentView(R.layout.widget_dialog_box);
        CustomTextView mDialogText = (CustomTextView) commentDialog
                .findViewById(R.id.dialogTextContent);

        CustomTextView mDialogHeader = (CustomTextView) commentDialog
                .findViewById(R.id.dialogHeaderText);

        mDialogText.setText(messages);

        mDialogHeader.setText(headerText);

        CustomTextView done = (CustomTextView) commentDialog
                .findViewById(R.id.dialogDone);
        done.setOnClickListener(doneClickListener);
        CustomTextView cancle = (CustomTextView) commentDialog.findViewById(R.id.dialogcancel);
        cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                commentDialog.dismiss();
            }
        });
//		commentDialog.getWindow().setBackgroundDrawableResource(
//				R.drawable.widget_progress_dialog_bg);
//		commentDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
//				LayoutParams.WRAP_CONTENT);

//        commentDialog.getWindow().setBackgroundDrawableResource(
//                R.drawable.abc_popup_background_mtrl_mult);
        commentDialog.getWindow().setLayout(
                context.getResources().getDisplayMetrics().widthPixels
                        - GlobalVariables.getPx(50, context),
                LayoutParams.WRAP_CONTENT);

        commentDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        commentDialog.show();
    }

    public void dismiss() {
        commentDialog.dismiss();
    }
}
