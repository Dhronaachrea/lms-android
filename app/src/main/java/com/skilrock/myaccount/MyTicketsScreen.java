package com.skilrock.myaccount;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.skilrock.adapters.TicketAdapter;
import com.skilrock.bean.PurchaseTicketBean;
import com.skilrock.bean.SportsTicketBean;
import com.skilrock.config.Config;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.DebouncedOnItemClickListener;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.RobotoTextView;
import com.skilrock.escratch.IGEScratchGameActivity;
import com.skilrock.escratch.IGETicketActivity;
import com.skilrock.escratch.bean.IGEUnfinishGameData;
import com.skilrock.utils.Utils;
import com.skilrock.lms.communication.IGETask;
import com.skilrock.lms.communication.SLETask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.sportslottery.SportsLotteryTicketActivity;
import com.skilrock.utils.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.skilrock.config.VariableStorage.GlobalPref.DGE_MER_CODE;
import static com.skilrock.config.VariableStorage.GlobalPref.DGE_ROOT_URL;
import static com.skilrock.config.VariableStorage.GlobalPref.IGE_MER_CODE;
import static com.skilrock.config.VariableStorage.GlobalPref.IGE_ROOT_URL;
import static com.skilrock.config.VariableStorage.GlobalPref.SLE_MER_CODE;
import static com.skilrock.config.VariableStorage.GlobalPref.SLE_ROOT_URL;
import static com.skilrock.config.VariableStorage.UserPref.PLAYER_ID;
import static com.skilrock.config.VariableStorage.UserPref.SESSION_ID;
import static com.skilrock.config.VariableStorage.UserPref.USER_NAME;

public class MyTicketsScreen extends Fragment implements WebServicesListener {
    //    private Context context;
    private List<TxnStatusBean> txnStatusBeans;
    private ListView itemList;
    private Button filter;
    private TicketAdapter adapter;
    private JSONObject jsonObject;
    private ArrayList<PurchaseTicketBean> ticketBeans;
    private Activity mActivity;
    private boolean isRefreshRequired = false;
    private RobotoTextView txtInfo;
    private int igePOsition;
    private Analytics analytics;
    public long timer;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        context = getActivity();
        ((MyAccountActivity) getActivity()).getSpinner()
                .setSelection(2);
        View view = inflater.inflate(R.layout.my_tickets, null);
        analytics = new Analytics();
        analytics.startAnalytics(getActivity());
        analytics.setScreenName(Fields.Screen.MY_TICKET);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
        bindViewIds(view);
        parseJson(getArguments().getString("TICKET"));
        filter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });

        if (ticketBeans.size() > 0) {
            txtInfo.setVisibility(View.GONE);
            itemList.setVisibility(View.VISIBLE);
            adapter = new TicketAdapter(getActivity(), R.layout.my_ticket_row, ticketBeans);
            itemList.setAdapter(adapter);
            itemList.setOnItemClickListener(new DebouncedOnItemClickListener(1000) {
                @Override
                public void onDebouncedItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (!Config.isStatic && !GlobalVariables.connectivityExists(getActivity())) {
                        GlobalVariables.showDataAlert(getActivity());
                        return;
                    }

                    if (ticketBeans.get(position).getServiceCode().equalsIgnoreCase("SLE")) {
                        analytics.sendAll(Fields.Category.MY_TICKET, Fields.Action.CLICK, Fields.Label.SLE);

                        JSONObject urlData = new JSONObject();
                        try {
                            urlData.put("merchantCode", VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.SLE_MER_CODE));
                            urlData.put("playerName", VariableStorage.UserPref.getStringData(getActivity(), VariableStorage.UserPref.USER_NAME));
                            urlData.put("sessionId", VariableStorage.UserPref.getStringData(getActivity(), VariableStorage.UserPref.SESSION_ID));
                            urlData.put("txnId", ticketBeans.get(position).getTxnId() + "");
                            urlData.put("ticketNumber", ticketBeans.get(position).getTicketNumber() + "");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String url = VariableStorage.GlobalPref.getStringData(mActivity, VariableStorage.GlobalPref.SLE_ROOT_URL);
                        SLETask webTask = new SLETask(MyTicketsScreen.this, "SLE_TRACK", "", url + "/com/skilrock/sle/mobile/reportsMgmt/Action/fetchPurchaseTicketData.action?requestData=" + URLEncoder.encode(urlData.toString()), "txnId:" + ticketBeans.get(position).getTxnId());
                        webTask.execute();
                    }
                    if (ticketBeans.get(position).getServiceCode().equalsIgnoreCase("DGE")) {
                        analytics.sendAll(Fields.Category.MY_TICKET, Fields.Action.CLICK, Fields.Label.DGE);
                        getTrackTicket("DGE", ticketBeans.get(position).getTxnId(), ticketBeans.get(position).getTicketNumber(), mActivity);
                    }
                    if (ticketBeans.get(position).getServiceCode().equalsIgnoreCase("IGE")) {
                        analytics.sendAll(Fields.Category.MY_TICKET, Fields.Action.CLICK, Fields.Label.IGE);

                        if (ticketBeans.get(position).getTxnPwt().equalsIgnoreCase("RA") || ticketBeans.get(position).getTxnPwt().equalsIgnoreCase("")) {
                            if (Config.isStatic && GlobalVariables.loadDummyData) {
                                Utils.Toast(getActivity(), "Data Unavailable!!");
                            } else {
                                String url = VariableStorage.GlobalPref.getStringData(mActivity, VariableStorage.GlobalPref.IGE_ROOT_URL);
                                url += "/gamePlay/api/trackTicketData.action?";
                                url = url + "txnId=" + ticketBeans.get(position).getTxnId();

                                IGETask igeTask = new IGETask(MyTicketsScreen.this, "IGE_UNFINISH", url, null, "");
                                igeTask.execute();
                            }

                        } else {
                            String date = ticketBeans.get(position).getTxnTime().split(" ")[0];
                            String url = VariableStorage.GlobalPref.getStringData(mActivity, VariableStorage.GlobalPref.IGE_ROOT_URL);
                            url += "trackTicket.action?";
                            SimpleDateFormat formatter = new SimpleDateFormat(VariableStorage.GlobalPref.getStringData(mActivity, VariableStorage.GlobalPref.DATE_FORMAT), Locale.US);
                            Date dt;
                            String formattedDate = "";
                            try {
                                dt = (Date) ((DateFormat) formatter).parse(date);
                                formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                formattedDate = formatter.format(dt);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            igePOsition = position;
                            url = url + "txnId=" + ticketBeans.get(position).getTxnId() + "&date=" + formattedDate;
                            IGETask igeTask = new IGETask(MyTicketsScreen.this, "IGE", url, "txnId:" + ticketBeans.get(position).getTxnId(), "");
                            igeTask.execute();
                        }


                    }

                }
            });
        } else {
            txtInfo.setVisibility(View.VISIBLE);
            itemList.setVisibility(View.GONE);
            //new DownloadDialogBox(getActivity(), "No Ticket Available !!", "", false, true, null, null).show();
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        //if (isRefreshRequired)
//            fetchTicketStatus();
    }

    @Override
    public void onStop() {
        super.onStop();
        analytics.endAnalytics(getActivity());
    }


    private void bindViewIds(View view) {
        itemList = (ListView) view.findViewById(R.id.tickets_list);
        filter = (Button) view.findViewById(R.id.filter);
        txtInfo = (RobotoTextView) view.findViewById(R.id.txt_info);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //fetchTicketStatus();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchTicketStatus();
    }

    private void fetchTicketStatus() {
        Map<String, TxnStatusBean> map = new HashMap<>();
        String DGEPath = "/services/tpDataMgmt/fetchTransactionStatus";
        String SLEPath = "/rest/dataMgmt/fetchTxnStatus";
        String IGEPath = "/gamePlay/api/txnStatus.action?txnStatusData=";

        for (PurchaseTicketBean bean : ticketBeans) {
            if (!map.containsKey(bean.getServiceCode())) {
                if (bean.getTxnPwt().equals("") || bean.getTxnPwt().equalsIgnoreCase("RA")) {
                    if (Config.isStatic && GlobalVariables.loadDummyData) {
                        Utils.Toast(getActivity(), "Data Unavailable!!");
                    } else {
                        TxnStatusBean txnStatusBean = new TxnStatusBean();
                        List<String> merchantTxnIds = new ArrayList<>();
                        merchantTxnIds.add(bean.getTxnId());
                        txnStatusBean.setMerchantTxnIdList(merchantTxnIds);
                        txnStatusBean.setSessionId(VariableStorage.UserPref.getStringData(getActivity(), SESSION_ID));
                        if (bean.getServiceCode().equalsIgnoreCase("DGE")) {
                            txnStatusBean.setMerchantCode(VariableStorage.GlobalPref.getStringData(getActivity(), DGE_MER_CODE));
                            txnStatusBean.setUrl(VariableStorage.GlobalPref.getStringData(getActivity(), DGE_ROOT_URL) + DGEPath);
                            txnStatusBean.setPlayerId(VariableStorage.UserPref.getStringData(getActivity(), PLAYER_ID));
                        } else if (bean.getServiceCode().equalsIgnoreCase("SLE")) {
                            txnStatusBean.setMerchantCode(VariableStorage.GlobalPref.getStringData(getActivity(), SLE_MER_CODE));
                            txnStatusBean.setUrl(VariableStorage.GlobalPref.getStringData(getActivity(), SLE_ROOT_URL) + SLEPath);
                            txnStatusBean.setPlayerName(VariableStorage.UserPref.getStringData(getActivity(), USER_NAME));
                        } else if (bean.getServiceCode().equalsIgnoreCase("IGE")) {
                            txnStatusBean.setMerchantCode(VariableStorage.GlobalPref.getStringData(getActivity(), IGE_MER_CODE));
                            txnStatusBean.setUrl(VariableStorage.GlobalPref.getStringData(getActivity(), IGE_ROOT_URL) + IGEPath);
                            txnStatusBean.setPlayerName(VariableStorage.UserPref.getStringData(getActivity(), USER_NAME));
                            txnStatusBean.setPlayerId(VariableStorage.UserPref.getStringData(getActivity(), PLAYER_ID));
                        }
                        map.put(bean.getServiceCode(), txnStatusBean);
                    }

                }
            } else {
                if (bean.getTxnPwt().equals("") || bean.getTxnPwt().equalsIgnoreCase("RA")) {
                    TxnStatusBean txnStatusBean = map.get(bean.getServiceCode());
                    List<String> merchantTxnIds = txnStatusBean.getMerchantTxnIdList();
                    merchantTxnIds.add(bean.getTxnId());
                }
            }
        }
        loadingTicketStatus(map);
    }

    private void loadingTicketStatus(Map<String, TxnStatusBean> map) {

        txnStatusBeans = new ArrayList<>();
        for (Map.Entry<String, TxnStatusBean> entry : map.entrySet()) {
            txnStatusBeans.add(entry.getValue());
        }
        if (txnStatusBeans.size() > 0) {
            timer = System.currentTimeMillis();
            new WebTask(this, txnStatusBeans.get(0), "BACKGROUND").execute();

        }

    }

    private void parseJson(String json) {
        ticketBeans = new ArrayList<PurchaseTicketBean>();
        try {
            jsonObject = new JSONObject(json);
            if (jsonObject.getInt("responseCode") == 0) {
                JSONArray ticketsArray = jsonObject.getJSONArray("txnData");
                for (int i = 0; i < ticketsArray.length(); i++) {
                    JSONObject singleData = ticketsArray.getJSONObject(i);
//(String gameDevName, String gameDispName, String serviceCode, String txnTime, double winAmount, double txnAmount, String txnId) {

                    String ticketNumber = "";
                    if (singleData.has("ticketNumber")) {
                        ticketNumber = singleData.getString("ticketNumber");
                    }
                    PurchaseTicketBean bean = new PurchaseTicketBean(
                            singleData.getString("gameDevName"),
                            singleData.getString("gameDispName"),
                            singleData.getString("serviceCode"),
                            singleData.getString("txnTime"),
                            singleData.getDouble("winAmount"),
                            singleData.getDouble("txnAmount"),
                            singleData.getString("txnId"),
                            singleData.getString("txnPwt"), ticketNumber);
                    ticketBeans.add(bean);
                }


            } else if (jsonObject.getInt("responseCode") == 118) {
                OnClickListener okClickListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfo.setLogout(getActivity());
                        Intent intent = new Intent(getActivity(),
                                MainScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().overridePendingTransition(
                                GlobalVariables.startAmin,
                                GlobalVariables.endAmin);
                    }
                };
                new DownloadDialogBox(getActivity(), /*txnBean.getErrorMsg()*/"Time Out. Login Again !", "", false, true, okClickListener, null).show();


            } else {
                new DownloadDialogBox(getActivity(), jsonObject.getString("responseMsg"), "", false, true, null, null).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void getTrackTicket(String serviceType, String txnId, String ticketNumber, Context context) {
        String url = "";
        if (serviceType.equalsIgnoreCase("DGE")) {
            url = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/reportsMgmt/trackTicket";
        } else {

            url = "";
        }
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.put("merchantCode", VariableStorage.UserPref.getStringData(context, VariableStorage.GlobalPref.DGE_MER_CODE));
            jsonObject.put("sessionId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.SESSION_ID));
            jsonObject.put("playerId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.PLAYER_ID));
            jsonObject.put("merchantTxnId", txnId);
            if (serviceType.equalsIgnoreCase("DGE"))
                jsonObject.put("ticketNumber", ticketNumber);

        } catch (JSONException e) {
            e.printStackTrace();
            jsonObject = null;
        }

        if (jsonObject != null)
            new com.skilrock.lms.communication.DGETask(MyTicketsScreen.this, "N/A", url, jsonObject.toString()).execute();
        else
            Utils.Toast(context, "Error on Fatching");


    }


    @Override
    public synchronized void onResult(String methodType, Object resultData, Dialog dialog) {
        try {
            isRefreshRequired = false;
            if (resultData != null) {
                switch (methodType) {
                    case "BACKGROUND":
                        timer = System.currentTimeMillis() - timer;
//                        Utils.ToastFargi(mActivity, timer / 1000 + "");
//                        resultData = "{\"re
// sponseCode\":0,\"responseMsg\":\"Success\",\"txnStatus\":[{\"txnId\":\"51102\",\"winStatus\":\"sattlement pending\",\"winAmount\":0},{\"txnId\":\"51535\",\"winStatus\":\"NON_WIN\",\"winAmount\":0},{\"txnId\":\"51087\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51105\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"50995\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51523\",\"winStatus\":\"NON_WIN\",\"winAmount\":0},{\"txnId\":\"51000\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51813\",\"winStatus\":\"NON_WIN\",\"winAmount\":0},{\"txnId\":\"51531\",\"winStatus\":\"NON_WIN\",\"winAmount\":0},{\"txnId\":\"52072\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51101\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51543\",\"winStatus\":\"NON_WIN\",\"winAmount\":0},{\"txnId\":\"51048\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51107\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51005\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51267\",\"winStatus\":\"WIN\",\"winAmount\":200000},{\"txnId\":\"51540\",\"winStatus\":\"NON_WIN\",\"winAmount\":0},{\"txnId\":\"51526\",\"winStatus\":\"NON_WIN\",\"winAmount\":0},{\"txnId\":\"50994\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51099\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"50996\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51539\",\"winStatus\":\"NON_WIN\",\"winAmount\":0},{\"txnId\":\"51481\",\"winStatus\":\"NON_WIN\",\"winAmount\":0},{\"txnId\":\"51104\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51536\",\"winStatus\":\"NON_WIN\",\"winAmount\":0},{\"txnId\":\"50993\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51106\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"50998\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"52073\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51542\",\"winStatus\":\"NON_WIN\",\"winAmount\":0},{\"txnId\":\"51544\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51098\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"50997\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51829\",\"winStatus\":\"NON_WIN\",\"winAmount\":0},{\"txnId\":\"51006\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51100\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51004\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"52071\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51538\",\"winStatus\":\"NON_WIN\",\"winAmount\":0},{\"txnId\":\"51097\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51009\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51088\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51109\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51103\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51795\",\"winStatus\":\"NON_WIN\",\"winAmount\":0},{\"txnId\":\"51096\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51108\",\"winStatus\":\"RA\",\"winAmount\":0},{\"txnId\":\"51796\",\"winStatus\":\"RA\",\"winAmount\":0}]}";
                        String json = (String) resultData;
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String resp = "";
                            int respCode = 10;
                            if (jsonObject.has("responseMsg")) {
                                resp = jsonObject.getString("responseMsg");
                            }
                            if (jsonObject.has("errorCode")) {
                                respCode = jsonObject.getInt("errorCode");
                            }
                            if (resp.equalsIgnoreCase("Success") || respCode == 0) {
                                JSONArray jsonArray = jsonObject.getJSONArray("txnStatus");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    for (int j = 0; j < ticketBeans.size(); j++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        if (object.getString("txnId").equalsIgnoreCase(ticketBeans.get(j).getTxnId())) {
                                            if (object.has("winStatus"))
                                                ticketBeans.get(j).setTxnPwt(object.getString("winStatus"));
                                            if (object.has("winAmount"))
                                                ticketBeans.get(j).setWinAmount(object.getDouble("winAmount"));
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                adapter.notifyDataSetInvalidated();
                                int index = itemList.getFirstVisiblePosition();
                                View v = itemList.getChildAt(0);
                                int top = (v == null) ? 0 : v.getTop();
                                itemList.setSelectionFromTop(index, top);
                                txnStatusBeans.remove(0);
                                if (txnStatusBeans.size() != 0) {
                                    timer = System.currentTimeMillis();
                                    new WebTask(this, txnStatusBeans.get(0), "BACKGROUND").execute();
                                }
                            } else if ((respCode == 501) || (respCode == 2001) || (respCode == 20001)) {
                                dialog.dismiss();
                                Utils.Toast(mActivity, getString(R.string.sql_exception));
                            } else {
                                Utils.Toast(mActivity, jsonObject.getString("responseMsg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Utils.Toast(mActivity, "Some Internal Error");
//                        GlobalVariables.showServerErr(mActivity);
                        } catch (Exception e) {
                            analytics.sendAll(Fields.Category.MY_TICKET, Fields.Action.GET, Fields.Label.FAILURE);

                            e.printStackTrace();
                            Utils.Toast(mActivity, "Some Internal error !");

                        }
                        break;
                    case "SLE_TRACK":
                        analytics.sendAll(Fields.Category.MY_TICKET, Fields.Action.OPEN, Fields.Label.SLE);

                        Gson gson = new Gson();
                        SportsTicketBean sportsTicketBean;
                        try {
                            sportsTicketBean = gson.fromJson(resultData.toString(),
                                    SportsTicketBean.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            Utils.Toast(mActivity, getString(R.string.some_internal_error));
                            return;
                        }
                        if (sportsTicketBean.getResponseMsg().equalsIgnoreCase("success")) {
                            Intent intent = new Intent(mActivity,
                                    SportsLotteryTicketActivity.class);
                            intent.putExtra("trackTicket", true);
                            intent.putExtra("sportsTicketBean", sportsTicketBean);
                            startActivity(intent);
                        } else if ((sportsTicketBean.getResponseCode() == 118) || (sportsTicketBean.getResponseCode() == 3009)) {
                            OnClickListener okClickListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UserInfo.setLogout(mActivity.getApplicationContext());
                                    Intent intent = new Intent(mActivity.getApplicationContext(),
                                            MainScreen.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mActivity.startActivity(intent);
                                    mActivity.overridePendingTransition(
                                            GlobalVariables.startAmin,
                                            GlobalVariables.endAmin);
                                }
                            };
                            dialog.dismiss();
                            new DownloadDialogBox(mActivity, sportsTicketBean.getResponseMsg(), "", false, true, okClickListener, null).show();
                        } else if ((sportsTicketBean.getResponseCode() == 501) || (sportsTicketBean.getResponseCode() == 2001) || (sportsTicketBean.getResponseCode() == 20001)) {
                            dialog.dismiss();
                            Utils.Toast(mActivity, getString(R.string.sql_exception));
                        } else {
                            dialog.dismiss();
                            Utils.Toast(mActivity, sportsTicketBean.getResponseMsg());
                        }
                        dialog.dismiss();
                        break;

                    case "IGE":
                        analytics.sendAll(Fields.Category.INSTANT_GAME, Fields.Action.OPEN, Fields.Label.IGE);

                        try {
                            JSONObject object = new JSONObject((String) resultData.toString());
                            if (object.has("errorCode") && object.getInt("errorCode") == 0) {
                                if (!object.getString("status").equalsIgnoreCase("RA")) {
                                    Intent intent = new Intent(mActivity, IGETicketActivity.class);
                                    intent.putExtra(IGETicketActivity.IGE_IMAGE_URL, object.getString("bckImgePth"));
                                    if (object.has("winAmnt"))
                                        intent.putExtra("winAmnt", object.getString("winAmnt"));
                                    if (object.has("tktNum"))
                                        intent.putExtra("tktNum", object.getString("tktNum"));
                                    if (object.has("gameName"))
                                        intent.putExtra("gameName", object.getString("gameName"));
                                    intent.putExtra("tcktAmt", ticketBeans.get(igePOsition).getTxnAmount() + "");
                                    intent.putExtra("purchaseTime", ticketBeans.get(igePOsition).getTxnTime());
                                    startActivity(intent);
                                    dialog.dismiss();
                                } else {
                                    dialog.dismiss();
                                    Utils.Toast(mActivity, "Unfinish ticket can not be tracked !");
                                }
                            } else if ((object.getInt("errorCode") == 118) || (object.getInt("errorCode") == 3009)) {
                                OnClickListener okClickListener = new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UserInfo.setLogout(mActivity.getApplicationContext());
                                        Intent intent = new Intent(mActivity.getApplicationContext(),
                                                MainScreen.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        mActivity.startActivity(intent);
                                        mActivity.overridePendingTransition(
                                                GlobalVariables.startAmin,
                                                GlobalVariables.endAmin);
                                    }
                                };
                                dialog.dismiss();
                                new DownloadDialogBox(mActivity, object.getString("errorMsg"), "", false, true, okClickListener, null).show();
                            } else if ((object.has("errorCode") && object.getInt("errorCode") == 501) || (object.has("errorCode") && object.getInt("errorCode") == 2001) || (object.has("errorCode") && object.getInt("errorCode") == 20001)) {
                                dialog.dismiss();
                                Utils.Toast(mActivity, getString(R.string.sql_exception));
                            } else {
                                dialog.dismiss();
                                Utils.Toast(mActivity, object.getString("errorMsg"));
                            }
                        } catch (JSONException e) {
                            analytics.sendAll(Fields.Category.MY_TICKET, Fields.Action.GET, Fields.Label.FAILURE);

                            dialog.dismiss();
                            e.printStackTrace();
                            Utils.Toast(mActivity, "Some Internal Error !");

//                        GlobalVariables.showServerErr(mActivity);
                        }
                        break;

                    case "IGE_UNFINISH":
                        analytics.sendAll(Fields.Category.IGE_UNFINISHED, Fields.Action.GET, Fields.Label.IGE);

                        try {
                            JSONObject object = new JSONObject((String) resultData.toString());
                            if (object.has("errorCode") && object.getInt("errorCode") == 0) {
                                IGEUnfinishGameData.GameMaster gameMaster = new IGEUnfinishGameData.GameMaster(object.getInt("gameNumber"), object.getInt("gameId"), object.getString("gameName"));
                                IGEUnfinishGameData.UnfinishedGameList gameList = new IGEUnfinishGameData.UnfinishedGameList(object.getLong("ticketNbr"), object.getString("date"), gameMaster);
                                Intent intent = new Intent(mActivity.getApplicationContext(), IGEScratchGameActivity.class);
                                intent.putExtra("mode", "UNFINISH");
                                intent.putExtra("games", gameList);
                                intent.putExtra("isMyTickets", true);
                                isRefreshRequired = true;
                                mActivity.startActivity(intent);
                                mActivity.overridePendingTransition(GlobalVariables.startAmin, GlobalVariables.endAmin);
                                dialog.dismiss();
                            } else if ((object.getInt("errorCode") == 118) || (object.getInt("errorCode") == 3009)) {
                                OnClickListener okClickListener = new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UserInfo.setLogout(mActivity.getApplicationContext());
                                        Intent intent = new Intent(mActivity.getApplicationContext(),
                                                MainScreen.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        mActivity.startActivity(intent);
                                        mActivity.overridePendingTransition(
                                                GlobalVariables.startAmin,
                                                GlobalVariables.endAmin);
                                    }
                                };
                                dialog.dismiss();
                                new DownloadDialogBox(mActivity, object.getString("errorMsg"), "", false, true, okClickListener, null).show();
                            } else if ((object.has("errorCode") && object.getInt("errorCode") == 501) || (object.has("errorCode") && object.getInt("errorCode") == 2001) || (object.has("errorCode") && object.getInt("errorCode") == 20001)) {
                                dialog.dismiss();
                                Utils.Toast(mActivity, getString(R.string.sql_exception));
                            } else {
                                dialog.dismiss();
                                Utils.Toast(mActivity, object.getString("errorMsg"));
                            }
                        } catch (JSONException e) {
                            analytics.sendAll(Fields.Category.MY_TICKET, Fields.Action.GET, Fields.Label.FAILURE);

                            dialog.dismiss();
                            e.printStackTrace();
                            Utils.Toast(mActivity, "Some Internal Error !");

                        }
                        break;


                    default:
                        try {
                            JSONObject response = (JSONObject) resultData;
//                            try {
//                                String fakeJson = "{\"mainData\":{\"commonSaleData\":{\"drawData\":[{\"drawId\":\"3048\",\"drawTime\":\"21:00:00\",\"drawName\":\"WINLOT FORMULAR\",\"drawResult\":\"N.A \",\"winningAmt\":555555555,\"drawStatus\":\"VERIFICATION PENDING\",\"drawDate\":\"2016-01-12\",\"ticketWinStatus\":\"UNCLAIMED\"}],\"purchaseAmt\":15,\"gameType\":\"normal\",\"purchaseDate\":\"12-01-2016\",\"ticketNumber\":\"250001110120969\",\"gameName\":\"5\\/90 Indoor\",\"gameDevName\":\"KenoFour\",\"purchaseTime\":\"16:34:37\"},\"betTypeData\":[{\"betAmtMul\":1,\"numberPicked\":\"2\",\"pickedNumbers\":\"39,69,88\",\"betName\":\"Perm2\",\"unitPrice\":5,\"panelPrice\":15,\"noOfLines\":3,\"isQp\":true}]},\"errorMsg\":\"\",\"isPromo\":false,\"isSuccess\":true}";
//                                resultData = fakeJson.toString();
//                                response = (JSONObject) resultData;
//                            } catch (Exception ex) {
//                            }
                            if (response.getBoolean("isSuccess")) {
                                analytics.sendAction(Fields.Category.MY_TICKET, Fields.Action.GET);

                                Intent intent = new Intent(mActivity,
                                        TrackTicketActivity.class);
                                String gameDevName = ((JSONObject) ((JSONObject) response.getJSONObject("mainData")).getJSONObject("commonSaleData")).getString("gameDevName");

                                if (gameDevName.equals(Config.bonusGameName) || gameDevName.equals(Config.bonusFree))
                                    analytics.sendAll(Fields.Category.BONUS_GAME, Fields.Action.OPEN, Fields.Label.BONUS_GAME);

                                intent.putExtra("betCode", 1);
                                intent.putExtra("data", resultData.toString());
                                intent.putExtra("isPurchase", true);
                                startActivity(intent);
                                mActivity.overridePendingTransition(
                                        GlobalVariables.startAmin, GlobalVariables.endAmin);
                                dialog.dismiss();
                            } else {
                                if ((response.getInt("errorCode") == 118) || (response.getInt("errorCode") == 3009)) {
                                    OnClickListener okClickListener = new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            UserInfo.setLogout(mActivity.getApplicationContext());
                                            Intent intent = new Intent(mActivity.getApplicationContext(),
                                                    MainScreen.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                    | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            mActivity.startActivity(intent);
                                            mActivity.overridePendingTransition(
                                                    GlobalVariables.startAmin,
                                                    GlobalVariables.endAmin);
                                        }
                                    };
                                    dialog.dismiss();
                                    new DownloadDialogBox(mActivity, response.getString("errorMsg"), "", false, true, okClickListener, null).show();
                                } else if ((response.getInt("errorCode") == 501) || (response.getInt("errorCode") == 2001) || (response.getInt("errorCode") == 20001)) {
                                    dialog.dismiss();
                                    Utils.Toast(mActivity, getString(R.string.sql_exception));
                                } else {
                                    dialog.dismiss();
                                    Utils.Toast(mActivity, response.getString("errorMsg"));
                                }
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            Utils.Toast(mActivity, "Some Internal Error !");
                            e.printStackTrace();
//                        GlobalVariables.showServerErr(mActivity);
                        }
                        break;
                }

            } else if (Config.isStatic && GlobalVariables.loadDummyData) {
                if (dialog != null)
                    dialog.dismiss();
                Utils.Toast(mActivity, "Data not available in offline mode");
            } else {
                if (dialog != null)
                    dialog.dismiss();
                Utils.consolePrint("Some Internal ERROR in My TicketScreen for null result data");
                Utils.Toast(mActivity, "Some Internal Error !");

            }
        } catch (IllegalStateException e) {
            if (dialog != null)
                dialog.dismiss();

            return;
        }


    }


}
