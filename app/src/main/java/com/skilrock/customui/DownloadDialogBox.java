package com.skilrock.customui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;

import com.skilrock.drawgame.DialogKeyListener;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.GlobalVariables;

public class DownloadDialogBox {

    private Context context;
    private String messages;
    private String headerText;
    private boolean isCancel;
    private boolean isOk;
    private OnClickListener okClickListener;
    private OnClickListener cancelClickListener;
    private Dialog commentDialog;
    private String okText;
    private String cancelText;
    private boolean isEdittextVisible;

    public DownloadDialogBox(Context context, String messages,
                             String headerText, boolean isCancel, boolean isOk,
                             OnClickListener okClickListener, OnClickListener cancelClickListener) {
        this.context = context;
        this.okClickListener = okClickListener;
        this.cancelClickListener = cancelClickListener;
        this.messages = messages;
        this.headerText = headerText;
        this.isCancel = isCancel;
        this.isOk = isOk;
        this.okText = "";
        this.cancelText = "";
        isEdittextVisible = false;
    }


    public DownloadDialogBox(Context context, String messages,
                             String headerText, boolean isCancel, boolean isOk,
                             OnClickListener okClickListener,
                             OnClickListener cancelClickListener, String okText,
                             String cancelText) {
        this.context = context;
        this.okClickListener = okClickListener;
        this.cancelClickListener = cancelClickListener;
        this.messages = messages;
        this.headerText = headerText;
        this.isCancel = isCancel;
        this.isOk = isOk;
        this.okText = okText;
        this.cancelText = cancelText;
        isEdittextVisible = false;
    }

    // comment Dialog Box Open method
    public void show() {
        commentDialog = new Dialog(context);
        commentDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        commentDialog.setContentView(R.layout.widget_dialog_box);
        CustomTextView mDialogText = (CustomTextView) commentDialog
                .findViewById(R.id.dialogTextContent);
        View view = commentDialog.findViewById(R.id.under_line);
        CustomTextView mDialogHeader = (CustomTextView) commentDialog
                .findViewById(R.id.dialogHeaderText);

        mDialogText.setText(messages);

        mDialogHeader.setText(headerText);

        if (headerText.equals("")) {
            view.setVisibility(View.GONE);
            mDialogHeader.setVisibility(View.GONE);
        } else {
            mDialogHeader.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        }

        final CustomTextView done = (CustomTextView) commentDialog
                .findViewById(R.id.dialogDone);
        final CustomTextView cancel = (CustomTextView) commentDialog
                .findViewById(R.id.dialogcancel);
        final CustomTextView lineHoriz = (CustomTextView) commentDialog.findViewById(R.id.lineHoriz);
        final EditText urlConnection = (EditText) commentDialog.findViewById(R.id.dialogUrlContent);
        if (isEdittextVisible)
            urlConnection.setVisibility(View.VISIBLE);
        if (okText.equals("") && cancelText.equals("")) {
            done.setText(context.getResources().getString(R.string.ok));
            cancel.setText(context.getResources().getString(R.string.cancel));
        } else {
            done.setText(okText);
            cancel.setText(cancelText);
        }
        if (isCancel) {
            lineHoriz.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
        } else {
            lineHoriz.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        }
        if (isOk) {
            done.setVisibility(View.VISIBLE);
        } else {
            done.setVisibility(View.GONE);
        }
        if (okClickListener != null) {
            done.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEdittextVisible) {
                        GlobalPref.getInstance(context).setBaseUrl(urlConnection.getText().toString().trim());
                    }
                    commentDialog.dismiss();
                    okClickListener.onClick(v);
                }
            });
        } else {
            done.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (isEdittextVisible) {
                        GlobalPref.getInstance(context).setBaseUrl(urlConnection.getText().toString().trim());
                    }
                    commentDialog.dismiss();
                }
            });
        }
        if (cancelClickListener != null) {
            cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentDialog.dismiss();
                    cancelClickListener.onClick(v);
                }
            });
        } else {
            cancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    commentDialog.dismiss();

                }
            });
        }

//		commentDialog.getWindow().setBackgroundDrawableResource(
//				R.drawable.widget_progress_dialog_bg);
//        commentDialog.getWindow().setBackgroundDrawableResource(
//                R.drawable.abc_popup_background_mtrl_mult);
        commentDialog.getWindow().setLayout(
                context.getResources().getDisplayMetrics().widthPixels
                        - GlobalVariables.getPx(50, context),
                LayoutParams.WRAP_CONTENT);
//        commentDialog.setOnKeyListener(new DialogKeyListener(commentDialog));
        commentDialog.setCancelable(false);
        commentDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        commentDialog.show();
    }

    public void dismiss() {
        if (commentDialog != null && commentDialog.isShowing())
            commentDialog.dismiss();
    }

    public void setIsEdittextVisible(boolean isEdittextVisible) {
        this.isEdittextVisible = isEdittextVisible;
    }
}
