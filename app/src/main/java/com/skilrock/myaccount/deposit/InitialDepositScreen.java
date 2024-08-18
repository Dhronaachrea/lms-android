package com.skilrock.myaccount.deposit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skilrock.bean.DepositLimitBean;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.R;
import com.skilrock.myaccount.MyAccountActivity;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.DecimalDigitsInputFilter;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class InitialDepositScreen extends Fragment {
    private Analytics analytics;
    private LinearLayout depositParent;
    private TextView[] radioGroup;
    private TextView depositTV;
    private Context context;
    private String amount;
    private DepositLimitBean depositLimitBean;
    private double[] depositAmnt;
    private boolean[] checked;
    private float amtWidth;
    private Activity activity;
    private double maxValue;

    private boolean isManualAmount;
    private GlobalPref globalPref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalPref = GlobalPref.getInstance(activity);
        analytics = new Analytics();
        analytics.startAnalytics(getActivity());
        analytics.setScreenName(Fields.Screen.INITIAL_DEPOSITE_SCREEN);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkSelected();
        if (isManualAmount) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }, 700);
        } else {
            for (int i = 0; i < radioGroup.length; i++)
                if (checked[i] == true)
                    makeSelectedRadio(i);
        }
        /*else {
            for (int i = 0; i < radioGroup.length; i++) {
                if (checked[i])
                    makeSelectedRadio(i);
                else
                    makeSelectedRadio(2);
            }
        }*/
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyAccountActivity) getActivity()).getSpinner()
                .setSelection(0);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        context = getActivity();
        depositLimitBean = (DepositLimitBean) getArguments().getSerializable("depositLimit");
        depositAmnt = new double[100];
        amtWidth = GlobalVariables.getPx(100, context);
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        View view = inflater.inflate(R.layout.initial_deposit_screen, null);
        bindViewIds(view);
        makeSelectedRadio(2);
        setTextClickListener();
        fillAmountInArray(depositLimitBean.getPgRanges());
        ((CustomTextView) ((LinearLayout) radioGroup[4].getParent()).getChildAt(0)).setText(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE));
        ((EditText) ((LinearLayout) radioGroup[4].getParent()).getChildAt(1)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    makeSelectedRadio(4);
                }
            }
        });
        ((EditText) ((LinearLayout) radioGroup[4].getParent()).getChildAt(1)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER || keyEvent.getKeyCode() == EditorInfo.IME_ACTION_DONE)) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(radioGroup[i].getWindowToken(), 0);
                }
                return false;
            }
        });
        depositTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelected()) {
                    if (checkValidAmount(amount)) {
                        //country lagos chk for jeneth bank
                        if ((Double.parseDouble(amount) >= Double.parseDouble(radioGroup[0].getText().toString().replace(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE), "").trim())
                                && Double.parseDouble(amount) <= maxValue) || globalPref.getCountry().equalsIgnoreCase("lagos")) {
                            analytics.sendAll(Fields.Category.INITIAL_DEPOSIT, Fields.Action.CLICK, Fields.Label.AMOUNT + " : " + amount);
                            Intent intent = new Intent(context, DepositScreen.class);
                            intent.putExtra("amount", AmountFormat.getAmountFormatForMobileDecimal(Double.parseDouble(amount)) + "");
                            intent.putExtra("bean", depositLimitBean);
                            startActivity(intent);
                            ((EditText) ((LinearLayout) radioGroup[4].getParent()).getChildAt(1)).setText("");
                        } else {
                            analytics.sendAll(Fields.Category.INITIAL_DEPOSIT, Fields.Action.CLICK, Fields.Label.INVALID_AMOUNT + " : " + amount);
                            if (Double.parseDouble(amount) < Double.parseDouble(radioGroup[0].getText().toString().replace(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE), "").trim()))
                                Utils.Toast(getActivity(), "Amount must be greater than " + radioGroup[0].getText().toString());
                            else if (Double.parseDouble(amount) > maxValue)
                                Utils.Toast(getActivity(), "Amount must be less than " + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE)
                                        + maxValue);
                        }
                    } else {
                        analytics.sendAll(Fields.Category.INITIAL_DEPOSIT, Fields.Action.CLICK, Fields.Label.INVALID_AMOUNT + " : " + amount);
                        Utils.Toast(context, "Invalid Amount");
                    }
                } else {
                    analytics.sendAll(Fields.Category.INITIAL_DEPOSIT, Fields.Action.CLICK, Fields.Label.INVALID_AMOUNT + " Amount not Selected");
                    Utils.Toast(context, "Please select amount");
                }
            }
        });
        return view;
    }

//    private void defaultLocale() {
//        Locale locale = new Locale("en_US");
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        context.getResources().updateConfiguration(config, null);
//    }

    private void bindViewIds(View view) {

        view.findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.requestFocus();
                v.setFocusable(true);
                ((EditText) v).setCursorVisible(true);
                makeSelectedRadio(4);
            }
        });
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        depositParent = (LinearLayout) view.findViewById(R.id.deposit_parent);
        depositTV = (TextView) view.findViewById(R.id.deposit);
        radioGroup = new TextView[5];
        radioGroup[0] = (TextView) depositParent.getChildAt(0);
        radioGroup[0].setTag(0);
        radioGroup[1] = (TextView) depositParent.getChildAt(2);
        radioGroup[1].setTag(1);
        radioGroup[2] = (TextView) depositParent.getChildAt(4);
        radioGroup[2].setTag(2);
        radioGroup[3] = (TextView) depositParent.getChildAt(6);
        radioGroup[3].setTag(3);
        radioGroup[4] = (TextView) ((LinearLayout) depositParent.getChildAt(8)).getChildAt(0);
        radioGroup[4].setTag(4);

        checked = new boolean[radioGroup.length];
        Arrays.fill(checked, false);
        //depositParent.setPadding(displaymetrics.widthPixels / 2, 0, 0, 0);
//        for (int i = 0; i < radioGroup.length; i++) {
//            radioGroup[i].measure(0,0);
//            radioGroup[i].setPadding((displaymetrics.widthPixels-radioGroup[i].getMeasuredWidth())/2, 0, 0, 0);
//        }
    }


    private boolean checkSelected() {
        boolean result = false;
        for (int i = 0; i < radioGroup.length; i++) {
            if (!checked[i]) {
                result = false;
            } else {
                result = true;
                if (i == 4) {
                    amount = ((EditText) ((LinearLayout) radioGroup[4].getParent()).getChildAt(1)).getText().toString();
                    analytics.sendAll(Fields.Category.INITIAL_DEPOSIT, Fields.Action.CLICK, Fields.Label.AMOUNT + " Enter : " + amount);
                } else {
                    amount = radioGroup[i].getText().toString();
                    analytics.sendAll(Fields.Category.INITIAL_DEPOSIT, Fields.Action.CLICK, Fields.Label.AMOUNT + " Selected : " + amount);
                }
                break;
            }

        }
        return result;
    }

    private void setTextClickListener() {
        for (int i = 0; i < radioGroup.length; i++) {
            radioGroup[i].setOnClickListener(getClickListener());

        }
    }

    private View.OnClickListener getClickListener() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSelectedRadio(Integer.parseInt(v.getTag().toString()));
            }
        };
        return onClickListener;
    }

    private void makeSelectedRadio(int pos) {
        for (int i = 0; i < radioGroup.length; i++) {
            if (i == pos) {
                checked[i] = true;
                ((TextView) radioGroup[i]).setCompoundDrawablesWithIntrinsicBounds(R.drawable.radio_on, 0, 0, 0);
                if (pos != 4) {
                    isManualAmount = false;
                    amount = ((TextView) radioGroup[i]).getText().toString();
                    ((EditText) ((LinearLayout) radioGroup[4].getParent()).getChildAt(1)).setCursorVisible(false);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(radioGroup[i].getWindowToken(), 0);
                } else {
                    isManualAmount = true;
//                    ((EditText) ((LinearLayout) radioGroup[4].getParent()).getChildAt(1)).setFocusable(true);
                    ((EditText) ((LinearLayout) radioGroup[4].getParent()).getChildAt(1)).setCursorVisible(true);
                    amount = ((EditText) ((LinearLayout) radioGroup[i].getParent()).getChildAt(1)).getText().toString();
                    ((EditText) ((LinearLayout) radioGroup[i].getParent()).getChildAt(1))
                            .setFilters(new InputFilter[]{new DecimalDigitsInputFilter(9, 2)});
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.showSoftInput(((EditText) ((LinearLayout) radioGroup[i].getParent()).getChildAt(1)), InputMethodManager.SHOW_FORCED);
                    ((EditText) ((LinearLayout) radioGroup[i].getParent()).getChildAt(1)).requestFocus();
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    imm.showSoftInput(((EditText) ((LinearLayout) radioGroup[i].getParent()).getChildAt(1)), InputMethodManager.SHOW_IMPLICIT);
                }
            } else {
                checked[i] = false;
                ((TextView) radioGroup[i]).setCompoundDrawablesWithIntrinsicBounds(R.drawable.radio_off, 0, 0, 0);
            }
        }
    }

    private boolean checkValidAmount(String deposit) {
        if (deposit != null) {
            try {
                if (deposit.contains(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE))) {
                    deposit = deposit.replace(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE), "").trim();
                    this.amount = deposit;
                }
                double amnt = Double.parseDouble(deposit);
                if (amnt > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {

                Utils.Toast(getActivity(), "Invalid Amount");
                return false;
            }
        } else {
            return false;
        }
    }

//    private void fillAmountInArray(ArrayList<DepositLimitBean.PgRange> pgRanges) {
//        DecimalFormat df = new DecimalFormat("0.00");
//        df.setMaximumFractionDigits(2);
//        int mul = 1;
//        for (int i = 0; i < (pgRanges.size()-1); i++) {
//            depositAmnt[i * 2] = pgRanges.get(i).getMin();
//            depositAmnt[i * 2 + 1] = pgRanges.get(i).getMax();
//            radioGroup[i * 2].setText(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE) + df.format(depositAmnt[0] * mul));
//            mul = mul * 10;
//            if (i == pgRanges.size() - 2) {
//                radioGroup[i * 2].setText(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE) + df.format(depositAmnt[0] * mul));
//            } else {
//                radioGroup[i * 2 + 1].setText(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE) + df.format(depositAmnt[0] * mul));
//            }
//            mul = mul * 10;
//        }
//    }


    private void fillAmountInArray(ArrayList<DepositLimitBean.PgRange> pgRanges) {

        ArrayList<Double> integerSet = new ArrayList<>();
        ArrayList<Double> integerSetMax = new ArrayList<>();
        for (int i = 0; i < pgRanges.size(); i++) {
            integerSet.add(pgRanges.get(i).getMin());
            integerSetMax.add(pgRanges.get(i).getMax());
        }
        Collections.sort(integerSet);
        if (integerSet.get(0) == 0) {
            integerSet.add(0, 0.01);
        }

        // set Max Values
        Collections.sort(integerSetMax, Collections.reverseOrder());

        if (integerSetMax.get(0) == 0) {
            integerSetMax.add(0, 0.01);
        }


        maxValue = integerSetMax.get(0);

        Locale.getDefault().getDisplayLanguage();
//        DecimalFormat df = new DecimalFormat("0.00");
        int mul = 1;
        for (int i = 0; i < 3; i++) {
            if (pgRanges.size() > i) {
                depositAmnt[i * 2] = integerSet.get(0);
                depositAmnt[i * 2 + 1] = pgRanges.get(i).getMax();
            } else {
                depositAmnt[i * 2] = 0;
                depositAmnt[i * 2 + 1] = 0;
            }
            //old code
//            depositAmnt[i * 2] = pgRanges.get(i).getMin();
//            depositAmnt[i * 2 + 1] = pgRanges.get(i).getMax();
            radioGroup[i * 2].setText(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE) + String.format("%.2f", depositAmnt[0] * mul));
            mul = mul * 10;
            if (i == 2) {
                radioGroup[i * 2].setText(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE) + String.format("%.2f", depositAmnt[0] * mul));
            } else {
                radioGroup[i * 2 + 1].setText(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE) + String.format("%.2f", depositAmnt[0] * mul));
            }
            mul = mul * 10;
        }
    }

    public static void hide_keyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onStop() {
        super.onStop();
        analytics.endAnalytics(getActivity());
        hide_keyboard(getActivity());
    }
}

