package com.skilrock.lms.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.HeaderBaseActivity;
import com.skilrock.lms.communication.Communication;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends HeaderBaseActivity implements
        OnClickListener, OnFocusChangeListener, WebServicesListener {
    private CustomTextView showStatus;
    private TableRow statusRow;
    private EditText userName;
    private EditText phoneNumber;
    private EditText dateOfBirth;
    private Button register;
    private TextView txtTc;
    private CheckBox checkTerms;
    private LinkedHashMap<String, String> stateMap = new LinkedHashMap<String, String>();
    private LinkedHashMap<String, String> cityMap = new LinkedHashMap<String, String>();
    private String regDoneMsg;
    private TextView resendtv;


    private JSONObject registerData;
    final private static int DIALOG_DOB = 0;
    final private static int REGISTER_SUCCESS = 1;
    private String preUsername;
    private boolean usernameValid;
    private JSONObject checkavailData;
    private final int usernameAvailable = 0;
    private final int doRegister = 1;
    private int state = -1;
    private int cityCodePos = 0;
    private Resources resources;


    //new variables
    private String isFull = "N";
    LinearLayout lay_continue;
    LinearLayout lay_otp_resend;
    private Button registerOklast;
    private int minuserlen;
    private int mobilelen;
    private Context context;
    private JSONObject loginData;
    private Analytics analytics;
    private EditText referPromoCode, email;
    private GlobalPref globalPref;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalPref = GlobalPref.getInstance(this);
        resources = getResources();
        context = this;
        setContentView(R.layout.activity_register);
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.REGISTER);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
        ((CustomTextView) findViewById(R.id.header_name))
                .setText(R.string.reg_header);
        txtTc = (TextView) findViewById(R.id.txt_tc);
        txtTc.setOnClickListener(this);
        (findViewById(R.id.close)).setVisibility(View.INVISIBLE);
        ((ImageView) (findViewById(R.id.done))).setImageResource(R.drawable.close);
        (findViewById(R.id.done))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });
       /*if (otpreq == true) {
            findViewById(R.id.registerOklast).setVisibility(View.GONE);
            findViewById(R.id.registerOk).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.registerOklast).setVisibility(View.VISIBLE);
            findViewById(R.id.registerOk).setVisibility(View.GONE);
        }*/
        setHeaderText(R.string.reg_header);
        minuserlen = Integer.parseInt(VariableStorage.GlobalPref.getStringData(RegisterActivity.this, VariableStorage.GlobalPref.USERNAME_MIN_LENGTH));
        mobilelen = Integer.parseInt(VariableStorage.GlobalPref.getStringData(RegisterActivity.this, VariableStorage.GlobalPref.MOBILE_NO_LENGTH));

        userName = (EditText) findViewById(R.id.username);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        if (globalPref.getCountry().equalsIgnoreCase("ghana")) {
            findViewById(R.id.mobile_code).setVisibility(View.VISIBLE);
            phoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mobilelen)});
        }
        dateOfBirth = (EditText) findViewById(R.id.date_of_birth);

        //after merge
        email = (EditText) findViewById(R.id.email);
        //new implementation for promocode
        referPromoCode = (EditText) findViewById(R.id.refer_promo_code);

        userName.setOnFocusChangeListener(this);
        dateOfBirth.setOnFocusChangeListener(this);
        dateOfBirth.setOnClickListener(this);
        statusRow = (TableRow) findViewById(R.id.statusrow);
        showStatus = (CustomTextView) findViewById(R.id.showavailibility);
        register = (Button) findViewById(R.id.lets_play);
        register.setOnClickListener(this);
        phoneNumber.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER || keyEvent.getKeyCode() == EditorInfo.IME_ACTION_DONE || keyEvent.getKeyCode() == EditorInfo.IME_ACTION_NEXT || keyEvent.getKeyCode() == EditorInfo.IME_ACTION_GO || keyEvent.getKeyCode() == EditorInfo.IME_FLAG_NAVIGATE_NEXT)) {
                    letsPlayCall();
                    InputMethodManager imm = (InputMethodManager) RegisterActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(phoneNumber.getWindowToken(), 0);

                }
                return false;
            }
        });
        phoneNumber.setOnKeyListener(new View.OnKeyListener() {


            @Override
            public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
                if (keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER || keyEvent.getKeyCode() == EditorInfo.IME_ACTION_DONE || keyEvent.getKeyCode() == EditorInfo.IME_ACTION_NEXT || keyEvent.getKeyCode() == EditorInfo.IME_ACTION_GO || keyEvent.getKeyCode() == EditorInfo.IME_FLAG_NAVIGATE_NEXT)) {
                    letsPlayCall();
                    InputMethodManager imm = (InputMethodManager) RegisterActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(phoneNumber.getWindowToken(), 0);
                }
                return false;
            }


        });

        //registerOklast = (Button) findViewById(R.id.registerOklast);

        //  register.setOnClickListener(this);
        //  registerOklast.setOnClickListener(this);
        /*progressDialog = new SkilrockProgressDialog(RegisterActivity.this, "",
                resources.getString(R.string.please_wait), false, false);
		progressDialog.show();*/

       /* tvusername = (TextView) findViewById(R.id.tvusername);
        tvdate_of_birth = (TextView) findViewById(R.id.tvdate_of_birth);
        tvmobile = (TextView) findViewById(R.id.tvmobile);
        otp = (EditText) findViewById(R.id.otp);
        resend = (Button) findViewById(R.id.resend);
        registerlast = (Button) findViewById(R.id.registerlast);*/
        //registerlast.setOnClickListener(this);
        //lay_otp_resend = (LinearLayout) findViewById(R.id.lay_otp_resend);
        //lay_continue = (LinearLayout) findViewById(R.id.lay_continue);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!globalPref.getCountry().equalsIgnoreCase("Zim")) {
            referPromoCode.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
        }
        // For Terms and Conditions
        String url = Communication.getTermsAndCondition();
        //TextView terms = (TextView) findViewById(R.id.terms);
        SpannableString span = new SpannableString(getResources().getString(
                R.string.agreement));
        span.setSpan(new UnderlineSpan(), 63, span.length(),
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(R.color.five_color_four), 63, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       /* terms.setText(span);
        terms.setMovementMethod(LinkMovementMethod.getInstance());
        terms.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Terms.class);
                startActivity(intent);
            }
        });*/


       /* TextView otptext = (TextView) findViewById(R.id.otptext);
        String a = (String) otptext.getText();
        Spannable wordtoSpan = new SpannableString(a);

        wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), 7, 26, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        otptext.setText(wordtoSpan);*/

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_of_birth:
                showDialog(DIALOG_DOB);

                break;


            case R.id.lets_play:
                letsPlayCall();
                break;

            case R.id.txt_tc:
                startActivity(new Intent(this, ActivityTC.class));
                break;

        }
    }

    private void letsPlayCall() {
        if (userName.getText().toString().trim().length() == 0
                || phoneNumber.getText().toString().trim().length() == 0
                || dateOfBirth.getText().toString().trim().length() == 0
                || (email.getText().toString().trim().length() == 0 && globalPref.getCountry().equalsIgnoreCase("Zim")))
            Utils.Toast(RegisterActivity.this,
                    resources.getString(R.string.all_man)
            );
        else if (userName.getText().toString().trim().length() < minuserlen)
            Utils.Toast(RegisterActivity.this,
                    getString(R.string.user_name_validate) + minuserlen + getString(R.string.character)
            );

               /* else if (!checkUserName(userName.getText().toString().trim())
                        || !usernameValid)
                    Utils.Toast(RegisterActivity.this,
                            resources.getString(R.string.invalid_u_n),
                            Toast.LENGTH_LONG);*/
        else if (!dateOfBirthvalidation(dateOfBirth.getText().toString()
                .trim()))
            Utils.Toast(RegisterActivity.this,
                    resources.getString(R.string.age_validation)
            );
        else if (!checkPhoneNumber(phoneNumber.getText().toString().trim()))
            Utils.Toast(RegisterActivity.this,
                    getResources().getString(R.string.mobile_no_validate) + mobilelen
            );
        else if (!isEmailValid(email.getText().toString().trim()) && globalPref.getCountry().equalsIgnoreCase("ZIM"))
            Utils.Toast(RegisterActivity.this,
                    resources.getString(R.string.emailvalidation));
        else {
            try {
                registerData = new JSONObject();
                registerData.put("userName", userName.getText().toString()
                        .trim());
                registerData.put("mobileNum", phoneNumber.getText()
                        .toString().trim());
                String format = VariableStorage.GlobalPref.getStringData(RegisterActivity.this, VariableStorage.GlobalPref.DATE_FORMAT);
                SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
                //String date = formatter.format(Date.parse(dateOfBirth.getText().toString()));
                try {
                    Date d = formatter.parse(dateOfBirth.getText().toString().trim());
                    String de = formatter.format(d);
                    registerData.put("dateOfBirth", de);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                registerData.put("isFull", isFull);
                if (GlobalVariables.connectivityExists(context)) {
                    if (globalPref.getCountry().equalsIgnoreCase("ZIM")) {
                        //new implementation for promocode
                        registerData.put("promoCode", referPromoCode.getText().toString().trim());
                        //new implementaion emial Id
                        registerData.put("emailId", email.getText().toString().trim());
                    }
                    analytics.sendAction(Fields.Category.REGISTER, Fields.Action.CLICK);
                    String path = "/com/skilrock/pms/mobile/loginMgmt/Action/playerRegistrationAction.action?";
                    String params = "registrationData=" + URLEncoder.encode(registerData.toString());
                    new PMSWebTask(RegisterActivity.this, path + params, "", null, "REG", null, "").execute();
                } else {
                    GlobalVariables.showDataAlert(context);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog dialogDetails = null;
        LayoutInflater inflater = null;
        View dialogView = null;
        DatePickerDialog dateDlg = null;
        AlertDialog.Builder dialogbuilder = null;
        switch (id) {
            case DIALOG_DOB:
                dateDlg = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Time chosenDate = new Time();
                                chosenDate.set(dayOfMonth, monthOfYear, year);
                                long dtDob = chosenDate.toMillis(true);
                                Utils.logPrint(dtDob + "");
                                CharSequence strDate = DateFormat.format(
                                        "dd-MM-yyyy", dtDob);
                                Utils.Toast(
                                        RegisterActivity.this,
                                        resources.getString(R.string.date_picked)
                                                + strDate);
                                dateOfBirth.setText(strDate);
                            }
                        }, 2011, 0, 1);
                dateDlg.setMessage(resources.getString(R.string.bday));
                break;
            case REGISTER_SUCCESS:
                try {
                    if (Communication.OTPRequest.equals("No")
                            || Communication.OTPRequest.equalsIgnoreCase("No")) {
                        regDoneMsg = resources.getString(R.string.cred_sent);
                    } else {
                        regDoneMsg = resources.getString(R.string.otp_sent);
                    }
                } catch (Exception e) {
                    RegisterActivity.this.finish();

                }
                OnClickListener okClickListener = new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {

                            if (Communication.OTPRequest.equalsIgnoreCase("No")) {
                                RegisterActivity.this.finish();

                            } else {
                                RegisterActivity.this.finish();
                                startActivity(new Intent(RegisterActivity.this,
                                        OTPActivity.class));
                            }
                        } catch (Exception e) {
                            RegisterActivity.this.finish();

                        }
                    }
                };
                new DownloadDialogBox(RegisterActivity.this, regDoneMsg, "", false,
                        true, okClickListener, null).show();
                break;
        }
        if (id == DIALOG_DOB)
            return dateDlg;
        else
            return dialogDetails;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case DIALOG_DOB:
                DatePickerDialog dateDlg = (DatePickerDialog) dialog;
                int iDay,
                        iMonth,
                        iYear;
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, -18);
                iDay = cal.get(Calendar.DAY_OF_MONTH);
                iMonth = cal.get(Calendar.MONTH);
                iYear = cal.get(Calendar.YEAR);
                dateDlg.updateDate(iYear, iMonth, iDay);
                break;
            case REGISTER_SUCCESS:
                // CustomTextView serverUser;
                // CustomTextView serverPass;
                // Button successOk;
                // final AlertDialog alertDialog = (AlertDialog) dialog;
                // serverUser = (CustomTextView) alertDialog
                // .findViewById(R.id.serverusername);
                // serverPass = (CustomTextView)
                // alertDialog.findViewById(R.id.serverpass);
                // successOk = (Button) alertDialog
                // .findViewById(R.id.registrationsuccess_ok);
                // successOk.setOnClickListener(new View.OnClickListener() {
                // @Override
                // public void onClick(View v) {
                // RegisterActivity.this.finish();
                // }
                // });
                // try {
                // serverUser.setText(registerResponse.getString("userName"));
                // serverPass.setText(registerResponse.getString("password"));
                // } catch (JSONException e) {
                // e.printStackTrace();
                // }
                break;
        }
    }

    private boolean isValid(String email) {
        Pattern pattern = Pattern
                .compile("^[+a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,4}[.a-zA-z]{0,4}$");
        // Pattern pattern= Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean checkUserName(String userName) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]{1}[a-zA-Z0-9_]{4,15}$");
        return pattern.matcher(userName).matches();
    }

    private boolean checkFirstName(String firstName) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        return pattern.matcher(firstName).matches();
    }

    private boolean dateOfBirthvalidation(String dob) {
        int day, month, year;
        Calendar currentCal = Calendar.getInstance();
        int break1 = dob.indexOf('-');
        int break2 = dob.indexOf("-", break1 + 1);
        day = Integer.parseInt(dob.substring(0, break1));
        month = Integer.parseInt(dob.substring(break1 + 1, break2)) - 1;
        year = Integer.parseInt(dob.substring(break2 + 1));
        Calendar selectedCal = Calendar.getInstance();
        selectedCal.set(Calendar.DAY_OF_MONTH, day);
        selectedCal.set(Calendar.MONTH, month);
        selectedCal.set(Calendar.YEAR, year);
        // System.out.println(date1 + "\n" + date2 + "\n" + (date1-date2));


        if (getDiffYears(selectedCal, currentCal) >= 18.0)
            return true;
        else
            return false;
    }

    public static int getDiffYears(Calendar first, Calendar last) {
        int diff = last.get(Calendar.YEAR) - first.get(Calendar.YEAR);
        if (first.get(Calendar.MONTH) > last.get(Calendar.MONTH)
                || (first.get(Calendar.MONTH) == last.get(Calendar.MONTH) && first
                .get(Calendar.DATE) > last.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    private boolean checkPhoneNumber(String number) {
       /* if (number.length() < 9 || number.length() > 16)
            return false;
        else if (Long.parseLong(number) == 0)
            return false;
        else
            return true;*/
        if (number.length() == mobilelen) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {

            case R.id.date_of_birth:
                if (hasFocus)
                    showDialog(DIALOG_DOB);
                break;


        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        switch (methodType) {
            case "REG":
                if (resultData != null) {
                    try {
                        loginData = new JSONObject(resultData.toString());
                        if (loginData.getBoolean("isSuccess")) {
                            dialog.dismiss();
                            showDialog(REGISTER_SUCCESS);
                            analytics.sendAll(Fields.Category.REGISTER, Fields.Action.GET, Fields.Label.SUCCESS);
                        } else {
                            dialog.dismiss();
                            Utils.Toast(RegisterActivity.this,
                                    loginData.getString("errorMsg")
                            );
                            analytics.sendAll(Fields.Category.REGISTER, Fields.Action.GET, Fields.Label.FAILURE);
                        }
                    } catch (Exception e) {
                        dialog.dismiss();
                        GlobalVariables.showServerErr(context);
                        analytics.sendAll(Fields.Category.REGISTER, Fields.Action.GET, Fields.Label.FAILURE);
                    }
                } else {
                    dialog.dismiss();
                    GlobalVariables.showServerErr(context);
                    analytics.sendAll(Fields.Category.REGISTER, Fields.Action.GET, Fields.Label.FAILURE);
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }
}
