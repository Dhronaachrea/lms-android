package com.skilrock.customui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skilrock.config.VariableStorage;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.GlobalVariables;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by stpl on 3/4/2016.
 */
public class InputDialogBox {
    private Context context;
    private String hintText;
    private String headerText;
    private boolean isCancel;
    private boolean isOk;
    private View.OnClickListener okClickListener;
    private View.OnClickListener cancelClickListener;
    private View.OnClickListener submitClickListener;
    private Dialog commentDialog;
    private String okText;
    private String cancelText;

    private ImageView doneClick, closeClick;
    private CustomTextView headerName;
    public EditText inputText;
    private CustomTextView buttonClick, buttonLogout;
    public String pseudeText;


    public InputDialogBox(Context context, String hintText,
                          String headerText, boolean isCancel, boolean isOk, View.OnClickListener submitClickListener,
                          View.OnClickListener okClickListener, View.OnClickListener cancelClickListener) {
        this.context = context;
        this.okClickListener = okClickListener;
        this.cancelClickListener = cancelClickListener;
        this.hintText = hintText;
        this.headerText = headerText;
        this.isCancel = isCancel;
        this.isOk = isOk;
        this.okText = "";
        this.submitClickListener = submitClickListener;
        this.cancelText = "";
    }


    public InputDialogBox(Context context, String hintText,
                          String headerText, boolean isCancel, boolean isOk,
                          View.OnClickListener okClickListener, View.OnClickListener submitClickListener,
                          View.OnClickListener cancelClickListener, String okText,
                          String cancelText) {
        this.context = context;
        this.okClickListener = okClickListener;
        this.cancelClickListener = cancelClickListener;
        this.hintText = hintText;
        this.headerText = headerText;
        this.isCancel = isCancel;
        this.isOk = isOk;
        this.okText = okText;
        this.submitClickListener = submitClickListener;
        this.cancelText = cancelText;
    }

    // comment Dialog Box Open method
    public void show() {
        //header_dialog
        //ed_mobile_number
        commentDialog = new Dialog(context);
        commentDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        commentDialog.setContentView(R.layout.input_dialog_box);
        bindsId(commentDialog);
        //customize values
        valuePut();


        if (okClickListener != null) {
            doneClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentDialog.dismiss();
                    okClickListener.onClick(v);
                }
            });
        } else {
            doneClick.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    commentDialog.dismiss();
                    okClickListener.onClick(v);
                }
            });
        }

        if (cancelClickListener != null) {
            closeClick.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    commentDialog.dismiss();
                    cancelClickListener.onClick(v);
                }
            });
        } else {
            closeClick.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    commentDialog.dismiss();
                    ((Activity) context).overridePendingTransition(GlobalVariables.startAmin,
                            GlobalVariables.endAmin);
                    ((Activity) context).finish();
                }
            });
        }

//        buttonClick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pmsCall(headerText);
//            }
//        });

        buttonClick.setOnClickListener(commonListener);
        buttonLogout.setOnClickListener(commonListener);


        commentDialog.getWindow().setBackgroundDrawableResource(
                R.drawable.abc_popup_background_mtrl_mult);
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
        commentDialog.show();

    }

    View.OnClickListener commonListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.txt_submit:
                    pmsCall(headerText);
                    break;
                case R.id.dialog_logout:
                    logoutDone();
                    break;
            }
        }
    };

    private void logoutDone() {
        if (GlobalVariables.connectivityExists(context)) {
            String path = "/com/skilrock/pms/mobile/loginMgmt/Action/playerLogout.action?";
            String params = "userName=" + VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME);
            new PMSWebTask((Activity) context, path + params, "", null, "LOGOUT_FROM_LOGIN", null, "").execute();
        } else {
//            DrawerBaseActivity.selectedItemId = -1;
            GlobalVariables.showDataAlert(context);
        }
    }

    private void pmsCall(String headerText) {
        pseudeText = inputText.getText().toString() != null ? inputText.getText().toString() : "";
        if (!pseudeText.equalsIgnoreCase("")) {
            try {
                String path = "/com/skilrock/pms/mobile/loginMgmt/Action/registerPlayerPseudoName.action?pseudoNameRegData=";
                JSONObject psudObj = new JSONObject();
                psudObj.put("playerId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.PLAYER_ID));
                psudObj.put("pseudoName", pseudeText);
                String compPath = path + URLEncoder.encode(psudObj.toString());
                if (!pseudeText.equalsIgnoreCase("")) {
                    new PMSWebTask((Activity) context, compPath, "", null, "PseudoName", null, "Loading").execute();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.pseudoName_validation), Toast.LENGTH_SHORT).show();
        }
    }

    private void valuePut() {
        //put dynamic values in header and hint
        if (headerText.equals("") || headerText == null) {
            headerName.setVisibility(View.GONE);
        } else {
            headerName.setVisibility(View.VISIBLE);
            headerName.setText(headerText);
        }

        if (hintText.equals("") || hintText == null) {
            inputText.setHint("");
        } else {
            inputText.setHint(hintText);
        }

        /*visiblity of ok and cancel*/
        if (isOk) {
            doneClick.setVisibility(View.VISIBLE);
        } else {
            doneClick.setVisibility(View.GONE);
        }
        if (isCancel) {
            closeClick.setVisibility(View.VISIBLE);
        } else {
            closeClick.setVisibility(View.GONE);
        }
    }

    private void bindsId(Dialog commentDialog) {
        doneClick = (ImageView) commentDialog.findViewById(R.id.done);
        closeClick = (ImageView) commentDialog.findViewById(R.id.close);
        headerName = (CustomTextView) commentDialog.findViewById(R.id.header_name);
        inputText = (EditText) commentDialog.findViewById(R.id.input_box);
        buttonClick = (CustomTextView) commentDialog.findViewById(R.id.txt_submit);
        buttonLogout = (CustomTextView) commentDialog.findViewById(R.id.dialog_logout);
    }

    public void dismiss() {
        if (commentDialog != null && commentDialog.isShowing())
            commentDialog.dismiss();
    }

}
