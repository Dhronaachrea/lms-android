package com.skilrock.customui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.skilrock.config.VariableStorage;
import com.skilrock.drawgame.DialogKeyListener;
import com.skilrock.lms.ui.R;
import com.skilrock.myaccount.deposit.PaymentGatwayFragment;
import com.skilrock.utils.GlobalVariables;

/**
 * Created by stpl on 11/10/2015.
 */
public class MobileConfirmDialog {
    private Context context;
    private String messages;
    private String headerText;
    private boolean isCancel;
    private boolean isOk;
    private View.OnClickListener okClickListener;
    private View.OnClickListener cancelClickListener;
    private Dialog commentDialog;
    private String okText;
    private String cancelText;
    private EditText mobNumb;

    public MobileConfirmDialog(Context context, String messages,
                               String headerText, boolean isCancel, boolean isOk,
                               View.OnClickListener okClickListener, View.OnClickListener cancelClickListener) {
        //new MobileConfirmDialog(this,null,"mobile confirmation",false,true,null,okClickListener)
        this.context = context;
        this.okClickListener = okClickListener;
        this.cancelClickListener = cancelClickListener;
        this.messages = messages;
        this.headerText = headerText;
        this.isCancel = isCancel;
        this.isOk = isOk;
        this.okText = "";
        this.cancelText = "";
    }

    public void show() {
        commentDialog = new Dialog(context);
        commentDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        commentDialog.setContentView(R.layout.mobile_confirm_dialog);
        CustomTextView mDialogText = (CustomTextView) commentDialog.findViewById(R.id.dialogTextContent);
        View view = commentDialog.findViewById(R.id.under_line);
        CustomTextView mDialogHeader = (CustomTextView) commentDialog
                .findViewById(R.id.dialogHeaderText);
        mobNumb = (EditText) commentDialog
                .findViewById(R.id.ed_mobile_number);
        mobNumb.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.MOBILE_NO_LENGTH).equals("")
                ? "0" : VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.MOBILE_NO_LENGTH)))});

        mDialogHeader.setText(headerText);

        if (headerText.equals("")) {
            view.setVisibility(View.GONE);
            mDialogHeader.setVisibility(View.GONE);
        } else {
            mDialogHeader.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        }

        final CustomTextView done = (CustomTextView) commentDialog
                .findViewById(R.id.txt_submit);
        final ImageView cancel = (ImageView) commentDialog.findViewById(R.id.close);
//        ImageView close = (ImageView) commentDialog.findViewById(R.id.close);

//        cancel.setVisibility(View.GONE);
        done.setVisibility(View.VISIBLE);
//        final CustomTextView lineHoriz = (CustomTextView) commentDialog.findViewById(R.id.lineHoriz);
        if (okText.equals("") && cancelText.equals("")) {
            done.setText(context.getResources().getString(R.string.ok));
        } else {
            done.setText(okText);
        }
        if (isOk) {
            done.setVisibility(View.VISIBLE);
        } else {
            done.setVisibility(View.GONE);
        }
        if (okClickListener != null) {
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PaymentGatwayFragment.mobNo = mobNumb.getText().toString();
                    okClickListener.onClick(v);
                }
            });
        } else {
            done.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    commentDialog.dismiss();
                }
            });
        }
        if (cancelClickListener != null) {
            cancel.setOnClickListener(cancelClickListener);
        } else {
            cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    commentDialog.dismiss();

                }
            });
        }
        commentDialog.getWindow().setLayout(
                context.getResources().getDisplayMetrics().widthPixels
                        - GlobalVariables.getPx(50, context),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        commentDialog.setCancelable(false);
        commentDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        commentDialog.setOnKeyListener(new DialogKeyListener(commentDialog));
        commentDialog.show();
    }

    public void dismiss() {
        if (commentDialog != null && commentDialog.isShowing())
//            PaymentGatwayFragment.mobNo = mobNumb.getText().toString();
            commentDialog.dismiss();
    }


}
