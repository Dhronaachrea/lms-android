package com.skilrock.myaccount;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.roomorama.caldroid.OnCalenderButtonListener;
import com.skilrock.adapters.TxnAdapter;
import com.skilrock.bean.TicketBean;
import com.skilrock.bean.TxnBean;
import com.skilrock.config.Config;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.BetterSpinner;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DebouncedOnClickListener;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.lms.communication.*;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.CustomTypefaceSpan;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.TxnArrangement;
import com.skilrock.utils.Utils;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

public class MyTransactionFragment extends Fragment implements WebServicesListener {
    private TxnBean txnBean;
    private View mProgressView;
    private View mTxnView;
    //    private Context context;
    private ListView itemList;
    private ImageView fetchMyTxns;
    private TextView fromMonth, fromDate, fromYear, toDate, toMonth, toYear, clzBalText, clzBalance, selectDateText;
    private BetterSpinner betterSpinner;
    private Button filter;
    private TxnAdapter adapter;
    private LinkedHashMap<String, ArrayList<TxnBean.TransactionData>> hashMap;
    private static String[] titles;
    private JSONObject jsonObject;
    private ArrayList<TicketBean> ticketBeans;
    private LinearLayout llCalanderPre;
    private LinearLayout llCalanderNext;
    private CaldroidFragment dialogCaldroidFragment;
    private CustomTextView txtInfo;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
    private Date minDate = new Date();
    private Date fDate = new Date();
    private Date tDate = new Date();
    private Activity mActivity;
    private Analytics analytics;
    private GlobalPref globalPref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.context = mActivity;
        globalPref = GlobalPref.getInstance(mActivity);
        analytics = new Analytics();
        analytics.startAnalytics(mActivity);
        analytics.setScreenName(Fields.Screen.MY_TRANSACTION);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
        for (int i = 0; i < 12; i++) {
            monthArr.add("JAN");
            monthArr.add("FEB");
            monthArr.add("MAR");
            monthArr.add("APR");
            monthArr.add("MAY");
            monthArr.add("JUN");
            monthArr.add("JUL");
            monthArr.add("AUG");
            monthArr.add("SEP");
            monthArr.add("OCT");
            monthArr.add("NOV");
            monthArr.add("DEC");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyAccountActivity) mActivity).getSpinner()
                .setSelection(3);
        View view = inflater.inflate(R.layout.my_txns, null);
        bindViewIds(view);
        betterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (titles[position].equalsIgnoreCase("Ledger"))
                    adapter = new TxnAdapter(mActivity, R.layout.my_txn_row, hashMap.get(titles[position]), false);
                else
                    adapter = new TxnAdapter(mActivity, R.layout.my_txn_row, hashMap.get(titles[position]), true);

                itemList.setAdapter(adapter);
            }
        });
        showProgress(true, "Please wait...");
        filter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        fetchMyTxns.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalVariables.connectivityExists(mActivity)) {
                    if (!Config.isWearer) {
                        String params = "userName=" + VariableStorage.UserPref.getStringData(mActivity.getApplicationContext(), VariableStorage.UserPref.USER_NAME) +
                                "&startDate=" + getDate(true) + "&endDate=" + getDate(false);
                        new PMSWebTask(MyTransactionFragment.this, path + params, "", null, "", TxnBean.class, "Loading...").execute();
                    } else {
                        path = "/Weaver/service/rest/transactionDetails";
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("playerToken", VariableStorage.UserPref.getStringData(mActivity.getApplicationContext(), VariableStorage.UserPref.SESSION_ID));
                            jsonObject.put("playerId", VariableStorage.UserPref.getStringData(mActivity.getApplicationContext(), VariableStorage.UserPref.PLAYER_ID));
                            jsonObject.put("domainName", Config.domain_name);
                            jsonObject.put("txnType", "ALL");
                            jsonObject.put("fromDate", getDate(true).replace("-", "/"));
                            jsonObject.put("toDate", getDate(false).replace("-", "/"));
                            jsonObject.put("offset", "0");
                            jsonObject.put("limit", "50");
                            new WeaverWebTask(MyTransactionFragment.this, path, "", jsonObject, "", TxnBean.class, "Loading...").execute();
                        } catch
                                (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    GlobalVariables.showDataAlert(mActivity);
                }
//                Toast.makeText(mActivity, "API Not Available !", Toast.LENGTH_SHORT).show();
            }
        });


        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(new SimpleDateFormat("MMM", Locale.US).parse(fromMonth.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int fMonth = cal.get(Calendar.MONTH) + 1;
        try {
            cal.setTime(new SimpleDateFormat("MMM", Locale.US).parse(toMonth.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int tMonth = cal.get(Calendar.MONTH) + 1;

        setStyledTextSELECTDATE();
        setStyledClzBalText();
        Date date = new Date();
        String[] formatedDate = dateFormat.format(date).split(" ");
        fromMonth.setText(formatedDate[1].toUpperCase(Locale.ENGLISH));
        fromDate.setText(Integer.parseInt(formatedDate[0]) + "");
        fromYear.setText(formatedDate[2] + "");
        toMonth.setText(formatedDate[1].toUpperCase(Locale.ENGLISH));
        toDate.setText(Integer.parseInt(formatedDate[0]) + "");
        toYear.setText(formatedDate[2] + "");

        if (GlobalVariables.connectivityExists(mActivity)) {
            if (!Config.isWearer) {
                String params = "userName=" + VariableStorage.UserPref.getStringData(mActivity.getApplicationContext(), VariableStorage.UserPref.USER_NAME) +
                        "&startDate=" + getDate(true) + "&endDate=" + getDate(false);
                new PMSWebTask(MyTransactionFragment.this, path + params, "", null, "", TxnBean.class, "Loading...").execute();
            } else {
                path = "/Weaver/service/rest/transactionDetails";
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("playerToken", VariableStorage.UserPref.getStringData(mActivity.getApplicationContext(), VariableStorage.UserPref.SESSION_ID));
                    jsonObject.put("playerId", VariableStorage.UserPref.getStringData(mActivity.getApplicationContext(), VariableStorage.UserPref.PLAYER_ID));
                    jsonObject.put("domainName", Config.domain_name);
                    jsonObject.put("txnType", "ALL");
                    jsonObject.put("fromDate", getDate(true).replace("-", "/"));
                    jsonObject.put("toDate", getDate(false).replace("-", "/"));
                    jsonObject.put("offset", "0");
                    jsonObject.put("limit", "50");
                    new WeaverWebTask(MyTransactionFragment.this, path, "", jsonObject, "", TxnBean.class, "Loading...").execute();
                } catch
                        (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            GlobalVariables.showDataAlert(mActivity);
        }
        setCalListener(savedInstanceState);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        analytics.endAnalytics(mActivity);
    }


    private void setCalListener(final Bundle state) {

        DebouncedOnClickListener listener = new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                switch (v.getId()) {
                    case R.id.cal_view1:
                        if (dialogCaldroidFragment != null && dialogCaldroidFragment.isVisible())
                            return;
                        dialogCaldroidFragment = new CaldroidFragment();
                        dialogCaldroidFragment.setMaxDate(new Date());

                        dialogCaldroidFragment.setCaldroidListener(new CaldroidListener() {
                            @Override
                            public void onSelectDate(Date date, View view) {

                            }
                        });
                        // If activity is recovered from rotation
                        final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
                        if (state != null) {
                            dialogCaldroidFragment.restoreDialogStatesFromKey(
                                    getChildFragmentManager(), state,
                                    "DIALOG_CALDROID_SAVED_STATE", dialogTag);
                            Bundle args = dialogCaldroidFragment.getArguments();
                            if (args == null) {
                                args = new Bundle();
                                dialogCaldroidFragment.setArguments(args);
                            }
                        } else {
                            // Setup arguments
                            Bundle bundle = new Bundle();
                            bundle.putInt("day", Integer.parseInt(fromDate.getText().toString()));
                            bundle.putInt("month", getMonthInInteger(fromMonth.getText().toString()));
                            bundle.putInt("year", Integer.parseInt(fromYear.getText().toString()));
                            bundle.putString("dialogTitle", null);
                            dialogCaldroidFragment.setArguments(bundle);
                            // Setup dialogTitle
                            dialogCaldroidFragment.setArguments(bundle);
                        }
                        dialogCaldroidFragment.setOkClickListener("OK", new OnCalenderButtonListener() {
                            @Override
                            public void onClick(Date selDate, int date, String day, String month, int year) {
                                fDate = selDate;
                                fromMonth.setText(month.toUpperCase(Locale.ENGLISH));
                                fromDate.setText(date + "");
                                fromYear.setText(year + "");
                                try {
                                    minDate = getDate(date, month, year);
                                    if (fDate.compareTo(tDate) > 0) {
                                        Date tDate = new Date();
                                        String[] formatedDate = dateFormat.format(tDate).split(" ");

                                        toMonth.setText(formatedDate[1].toUpperCase(Locale.ENGLISH));
                                        toDate.setText(Integer.parseInt(formatedDate[0]) + "");
                                        toYear.setText(formatedDate[2] + "");
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                dialogCaldroidFragment.dismiss();
                            }
                        });
                        dialogCaldroidFragment.setCancleClickListener("CANCEL", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogCaldroidFragment.dismiss();
                            }
                        });
                        dialogCaldroidFragment.setNowClickListener("TODAY", new OnCalenderButtonListener() {
                            @Override
                            public void onClick(Date selDate, int date, String day, String month, int year) {
                                fDate = selDate;
                                fromMonth.setText(month.toUpperCase(Locale.ENGLISH));
                                fromDate.setText(date + "");
                                fromYear.setText(year + "");
                                try {
                                    minDate = getDate(date, month, year);
                                    if (fDate.compareTo(tDate) > 0) {
                                        Date tDate = new Date();
                                        String[] formatedDate = dateFormat.format(tDate).split(" ");

                                        toMonth.setText(formatedDate[1].toUpperCase(Locale.ENGLISH));
                                        toDate.setText(Integer.parseInt(formatedDate[0]) + "");
                                        toYear.setText(formatedDate[2] + "");
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                dialogCaldroidFragment.dismiss();
                            }
                        });
//                        if (!dialogCaldroidFragment.isVisible())
                        dialogCaldroidFragment.show(getChildFragmentManager(),
                                dialogTag);
                        break;

                    case R.id.cal_view2:
                        if (dialogCaldroidFragment != null && dialogCaldroidFragment.isVisible())
                            return;
                        dialogCaldroidFragment = new CaldroidFragment();
                        dialogCaldroidFragment.setMinDate(minDate);
                        dialogCaldroidFragment.setMaxDate(new Date());
                        dialogCaldroidFragment.setCaldroidListener(new CaldroidListener() {
                            @Override
                            public void onSelectDate(Date date, View view) {

                            }
                        });
                        // If activity is recovered from rotation
                        final String dialogTag2 = "CALDROID_DIALOG_FRAGMENT";
                        if (state != null) {
                            dialogCaldroidFragment.restoreDialogStatesFromKey(
                                    getChildFragmentManager(), state,
                                    "DIALOG_CALDROID_SAVED_STATE", dialogTag2);
                            Bundle args = dialogCaldroidFragment.getArguments();
                            if (args == null) {
                                args = new Bundle();
                                dialogCaldroidFragment.setArguments(args);
                            }
                        } else {
                            // Setup arguments
                            Bundle bundle = new Bundle();
                            bundle.putInt("day", Integer.parseInt(toDate.getText().toString()));
                            bundle.putInt("month", getMonthInInteger(toMonth.getText().toString()));
                            bundle.putInt("year", Integer.parseInt(toYear.getText().toString()));
                            bundle.putString("dialogTitle", null);
                            // Setup dialogTitle
                            dialogCaldroidFragment.setArguments(bundle);
                        }
                        dialogCaldroidFragment.setOkClickListener("OK", new OnCalenderButtonListener() {
                            @Override
                            public void onClick(Date selDate, int date, String day, String month, int year) {
                                tDate = selDate;
                                toMonth.setText(month.toUpperCase(Locale.ENGLISH));
                                toDate.setText(date + "");
                                toYear.setText(year + "");
                                dialogCaldroidFragment.dismiss();
                            }
                        });
                        dialogCaldroidFragment.setCancleClickListener("CANCEL", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogCaldroidFragment.dismiss();
                            }
                        });
                        dialogCaldroidFragment.setNowClickListener("TODAY", new OnCalenderButtonListener() {
                            @Override
                            public void onClick(Date selDate, int date, String day, String month, int year) {
                                tDate = selDate;
                                toMonth.setText(month.toUpperCase(Locale.ENGLISH));
                                toDate.setText(date + "");
                                toYear.setText(year + "");
                                dialogCaldroidFragment.dismiss();
                            }
                        });
                        dialogCaldroidFragment.show(getChildFragmentManager(),
                                dialogTag2);
                        break;
                }
            }
        };

        llCalanderPre.setOnClickListener(listener);

        llCalanderNext.setOnClickListener(listener);

    }

    private Date getDate(int date, String month, int year) throws ParseException {
        return dateFormat.parse(date + " " + month + " " + year);
    }

    private void bindViewIds(View view) {
        itemList = (ListView) view.findViewById(R.id.tickets_list);
        filter = (Button) view.findViewById(R.id.filter);
        betterSpinner = (BetterSpinner) view.findViewById(R.id.spinner);
        fetchMyTxns = (ImageView) view.findViewById(R.id.getTxns);
        fromMonth = (TextView) view.findViewById(R.id.fromMonth);
        fromDate = (TextView) view.findViewById(R.id.fromDate);
        fromYear = (TextView) view.findViewById(R.id.fromYear);
        toDate = (TextView) view.findViewById(R.id.toDate);
        toMonth = (TextView) view.findViewById(R.id.toMonth);
        toYear = (TextView) view.findViewById(R.id.toYear);
        txtInfo = (CustomTextView) view.findViewById(R.id.txt_info);
        clzBalance = (TextView) view.findViewById(R.id.clzBalance);
        mProgressView = view.findViewById(R.id.txn_progres);
        mTxnView = view.findViewById(R.id.parentView);
        selectDateText = (TextView) view.findViewById(R.id.selectDateText);
        betterSpinner.setVisibility(View.GONE);
        clzBalText = (TextView) view.findViewById(R.id.clzBalText);
        llCalanderPre = (LinearLayout) view.findViewById(R.id.cal_view1);
        llCalanderNext = (LinearLayout) view.findViewById(R.id.cal_view2);
    }

    public void showProgress(final boolean show, String msg) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mTxnView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public void setStyledTextSELECTDATE() {
        SpannableString styledString = new SpannableString("SELECT\nDATE");
        Typeface typefaceThin = Typeface.createFromAsset(mActivity.getAssets(), "ROBOTO-LIGHT.TTF");
        Typeface typefaceBold = Typeface.createFromAsset(mActivity.getAssets(), "ROBOTO-MEDIUM.TTF");
        styledString.setSpan(new CustomTypefaceSpan("", typefaceBold), 0, 6, 0);
        styledString.setSpan(new CustomTypefaceSpan("", typefaceThin), 7, 11, 0);
        styledString.setSpan(new RelativeSizeSpan(1.4f), 7, 11, 0);
        selectDateText.setText(styledString);
    }

    public void setStyledClzBalText() {
//        SpannableString styledString = new SpannableString("Closing Balance");
//        Typeface typefaceThin = Typeface.createFromAsset(mActivity.getAssets(), "ROBOTO-THIN.TTF");
//        styledString.setSpan(new CustomTypefaceSpan("", typefaceThin), 0, styledString.length(), 0);
        clzBalText.setText("Closing Balance");
    }

    public void setStyledClzBal(String balance) {
        SpannableString styledString = new SpannableString(balance);
        Typeface typefaceThin = Typeface.createFromAsset(mActivity.getAssets(), "ROBOTO-BOLD.TTF");
        styledString.setSpan(new CustomTypefaceSpan("", typefaceThin), 0, styledString.length(), 0);
        clzBalance.setText(styledString);
    }


    private void afterProcess(ArrayList<TxnBean.TransactionData> transactionDatas, Context mActivity) {
        if (transactionDatas.size() > 0) {
            txtInfo.setVisibility(View.GONE);
            itemList.setVisibility(View.VISIBLE);

            hashMap = new TxnArrangement(transactionDatas).getArrangdTxnData();
            hashMap.put("Ledger", transactionDatas);
            final ArrayList<String> title = new ArrayList<>(hashMap.keySet());
            titles = title.toArray(new String[title.size()]);
            ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(mActivity, R.layout.txn_spinner_row, R.id.spinner_text, titles);
            betterSpinner.setText("Ledger");
            betterSpinner.setAdapter(adapterSpinner);
            betterSpinner.clearFocus();
            betterSpinner.setVisibility(View.VISIBLE);
            adapter = new TxnAdapter(mActivity, R.layout.my_txn_row, hashMap.get("Ledger"), false);
            itemList.setAdapter(adapter);
        } else {
            betterSpinner.setVisibility(View.GONE);
            txtInfo.setVisibility(View.VISIBLE);
            itemList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        if (mActivity == null) {
            if (dialog != null)
                dialog.dismiss();
            return;
        }
        if (resultData != null) {
            showProgress(false, "");
            txnBean = (TxnBean) resultData;
            if (txnBean.isSuccess() || txnBean.getErrorCode() == 0) {
                if (Config.isWearer) {
                    ArrayList<TxnBean.TransactionData> data = new ArrayList<>();
                    for (int i = 0; i < txnBean.getTxnList().size(); i++) {
                        data.add(new TxnBean.TransactionData(txnBean.getTxnList().get(i).getBalance(),
                                txnBean.getTxnList().get(i).getTxnAmount() + "", txnBean.getTxnList().get(i).getTransactionDate(),
                                txnBean.getTxnList().get(i).getTxnType(), txnBean.getTxnList().get(i).getParticular(),
                                txnBean.getTxnList().get(i).getOpeningBalance()));
                    }
                    txnBean.setTransactionData(data);
                }
                if (!Config.isWearer) {
                    VariableStorage.UserPref.setStringPreferences(mActivity, VariableStorage.UserPref.USER_BAL, AmountFormat.getAmountFormatForMobile(txnBean.getClosingBal()));
                    if (globalPref.getCountry().equalsIgnoreCase("ghana") || globalPref.getCountry().equalsIgnoreCase("lagos")) {
                        VariableStorage.UserPref.setStringPreferences(mActivity, VariableStorage.UserPref.BONUS_BAL, AmountFormat.getAmountFormatForMobile(txnBean.getBonusBal()));
                        VariableStorage.UserPref.setStringPreferences(mActivity, VariableStorage.UserPref.DEPOSIT_BAL, AmountFormat.getAmountFormatForMobile(txnBean.getDepositBal()));
                        VariableStorage.UserPref.setStringPreferences(mActivity, VariableStorage.UserPref.WINNING_BAL, AmountFormat.getAmountFormatForMobile(txnBean.getWinningBal()));
                        VariableStorage.UserPref.setStringPreferences(mActivity, VariableStorage.UserPref.WITHDRAWAL_BAL, AmountFormat.getAmountFormatForMobile(txnBean.getWithdrawlBal()));
                    }
                    setStyledClzBal(VariableStorage.GlobalPref.getStringData(mActivity, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatTxn(txnBean.getClosingBal()));
                } else {
                    try {
                        setStyledClzBal(VariableStorage.GlobalPref.getStringData(mActivity, VariableStorage.GlobalPref.CURRENCY_CODE)
                                + AmountFormat.getAmountFormatTxn(Double.parseDouble(txnBean.getTxnList().get(0).getBalance())));
                    } catch (IndexOutOfBoundsException e) {
                        setStyledClzBal(VariableStorage.GlobalPref.getStringData(mActivity, VariableStorage.GlobalPref.CURRENCY_CODE)
                                + AmountFormat.getAmountFormatTxn(Double.parseDouble(VariableStorage.UserPref.getStringData(mActivity, VariableStorage.UserPref.USER_BAL))));
                    }
                }
                dialog.dismiss();
                afterProcess(txnBean.getTransactionData(), mActivity);
            } else {
                if (txnBean.getErrorCode() == 118 || txnBean.getErrorCode() == 203) {
                    OnClickListener okClickListener = new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserInfo.setLogout(mActivity.getApplicationContext());
                            Intent intent = new Intent(mActivity.getApplicationContext(),
                                    MainScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            mActivity.overridePendingTransition(
                                    GlobalVariables.startAmin,
                                    GlobalVariables.endAmin);
                        }
                    };
                    dialog.dismiss();
                    new DownloadDialogBox(mActivity, txnBean.getErrorMsg(), "", false, true, okClickListener, null).show();
                } else if (txnBean.getErrorCode() == 501) {
                    dialog.dismiss();
                    Utils.Toast(mActivity, getString(R.string.sql_exception));
                } else {
                    if (txnBean.getClosingBal() != 0) {
                        setStyledClzBal(VariableStorage.GlobalPref.getStringData(mActivity, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatTxn(txnBean.getClosingBal()));
                        VariableStorage.UserPref.setStringPreferences(mActivity, VariableStorage.UserPref.USER_BAL, txnBean.getClosingBal() + "");
                    }
                    dialog.dismiss();
                    Utils.Toast(mActivity, txnBean.getErrorMsg());
                }
            }
        } else {
            dialog.dismiss();
            txtInfo.setVisibility(View.VISIBLE);
            GlobalVariables.showServerErr(mActivity);
            showProgress(false, "");
        }
    }


    private String path = "/com/skilrock/pms/mobile/home/reportsMgmt/Action/fetchLastTxnReport.action?";

    private String getDate(boolean frmDate) {
        String dateTime = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        Date dt;
        String formattedDate = "";
        if (frmDate) {
            dateTime = fromDate.getText().toString() + "-" + fromMonth.getText().toString() + "-" + fromYear.getText().toString();
        } else {
            dateTime = toDate.getText().toString() + "-" + toMonth.getText().toString() + "-" + toYear.getText().toString();
        }
        try {
            dt = (Date) ((DateFormat) formatter).parse(dateTime);
            formatter = new SimpleDateFormat(VariableStorage.GlobalPref.getStringData(mActivity, VariableStorage.GlobalPref.DATE_FORMAT), Locale.US);
            formattedDate = formatter.format(dt);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return "";

    }

    private int getMonthInInteger(String in) {
        return monthArr.indexOf(in) + 1;
    }

    private ArrayList<String> monthArr = new ArrayList<>();


}