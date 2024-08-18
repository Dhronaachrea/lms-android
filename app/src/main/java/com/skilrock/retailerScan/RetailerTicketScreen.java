package com.skilrock.retailerScan;

/**
 * Created by stpl on 10/3/2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.skilrock.adapters.RetailerTicketAdapter;
import com.skilrock.bean.MyRetailerBean;
import com.skilrock.bean.SportsTicketBean;
import com.skilrock.config.Config;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.myaccount.TrackTicketActivity;
import com.skilrock.sportslottery.SportsLotteryTicketActivity;
import com.skilrock.utils.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by stpl on 9/8/2016.
 */
public class RetailerTicketScreen extends Activity implements WebServicesListener {
    ImageView round_image;
    private ListView itemList;
    private RetailerTicketAdapter retailerAdapter;
    private JSONObject jsonObject;
    private ArrayList<MyRetailerBean.TicketData> ticketBeans;
    MyRetailerBean.TicketData ticketData = null;
    private Activity mActivity;
    private boolean isRefreshRequired = false;
    //    private RobotoTextView txtInfo;
    private int igePOsition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retailer);
        bindIds();

        round_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RetailerTicketScreen.this, RetailerActivity.class);
                startActivityForResult(intent, 0012);
            }
        });
        listData();


    }

    protected void listData() {
        if (GlobalVariables.connectivityExists(getApplicationContext())) {
            String rt_path = "/rest/playerMgmt/fetchSaleTransactions";
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject();
                jsonObject.put("sessionId", VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.SESSION_ID));
                jsonObject.put("playerId", VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.PLAYER_ID));
            } catch (JSONException e) {
                e.printStackTrace();
                jsonObject = null;
            }
            if (jsonObject != null)
                new PMSWebTask(RetailerTicketScreen.this, rt_path, "N/A", jsonObject, "RETAILER_TICKETS", null, "Loading...").execute();

            else
                Toast.makeText(RetailerTicketScreen.this, "Error on Fetching", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0012 && data != null && data.hasExtra("refreshList")) {
            listData();
        }
    }


    private void bindIds() {
        itemList = (ListView) findViewById(R.id.list);
//        txtInfo = (RobotoTextView) findViewById(R.id.txt_info);
        round_image = (ImageView) findViewById(R.id.round_image);
    }


    private void parseJson(String json) {
        ticketBeans = new ArrayList<>();
        try {
            final MyRetailerBean retailerBean = new Gson().fromJson(json, MyRetailerBean.class);
            ticketBeans = retailerBean.getTicketData();
            if (retailerBean.getResponseCode() == 0) {
                if (ticketBeans.size() > 0) {
//                    txtInfo.setVisibility(View.GONE);
                    itemList.setVisibility(View.VISIBLE);
                    retailerAdapter = new RetailerTicketAdapter(RetailerTicketScreen.this, R.layout.retailer_ticket, ticketBeans);
                    itemList.setAdapter(retailerAdapter);
                    itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (GlobalVariables.connectivityExists((getApplicationContext()))) {
                                if (ticketBeans.get(position).getServiceCode().equalsIgnoreCase("DGE")) {
                                    Toast.makeText(RetailerTicketScreen.this, ticketBeans.get(position).getTicketNumber() + "", Toast.LENGTH_SHORT).show();
                                    String path = "DGE_Scheduler/services/reportsMgmt/ticketWinStatus";
                                    JSONObject json = new JSONObject();

                                    try {
                                        if (ticketBeans.get(position).getTicketNumber().length() == 18) {
                                            json.put("ticketNo", ticketBeans.get(position).getTicketNumber() + "Nxt" + "-1");
                                        } else {
                                            String ticketNo = ticketBeans.get(position).getTicketNumber();
                                            String respTicketNo = ticketNo.substring(0, ticketNo.length() - 2) + "Nxt" + ticketNo.substring(ticketNo.length() - 2, ticketNo.length());
//                                            int value = 0;
//                                            String result = null;
//                                            int a = Integer.parseInt(String.valueOf(ticketBeans.get(position).getTicketNumber().charAt(ticketBeans.get(position).getTicketNumber().length() - 2)));
//                                            int b = Integer.parseInt(String.valueOf(ticketBeans.get(position).getTicketNumber().charAt(ticketBeans.get(position).getTicketNumber().length() - 1)));
//                                            value = value + ((a * 10) + b);
//                                            for (int i = 0; i < 18; i++) {
//                                                result = result + ticketBeans.get(position).getTicketNumber().charAt(i);
//                                            }
                                            json.put("ticketNo", respTicketNo);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (json != null) {
                                        new PMSWebTask(RetailerTicketScreen.this, path, "N/A", json, "DGERESULT", null, "Loading...").execute();
                                    } else
                                        Toast.makeText(RetailerTicketScreen.this, "Error on Fetching", Toast.LENGTH_SHORT).show();
                                } else if (ticketBeans.get(position).getServiceCode().equalsIgnoreCase("SLE")) {
                                    String path = "SportsLottery/rest/drawMgmt/TpTrackSLETicket";
                                    JSONObject json = new JSONObject();

                                    try {
                                        if (ticketBeans.get(position).getTicketNumber().length() == 18) {
                                            json.put("ticketNo", ticketBeans.get(position).getTicketNumber() + "Nxt" + "-1");
                                        } else {
//                                            int value = 0;
//                                            String result = null;
//                                            int a = Integer.parseInt(String.valueOf(ticketBeans.get(position).getTicketNumber().charAt(ticketBeans.get(position).getTicketNumber().length() - 2)));
//                                            int b = Integer.parseInt(String.valueOf(ticketBeans.get(position).getTicketNumber().charAt(ticketBeans.get(position).getTicketNumber().length() - 1)));
//                                            value = value + ((a * 10) + b);
//                                            for (int i = 0; i < 18; i++) {
//                                                result = result + ticketBeans.get(position).getTicketNumber().charAt(i);
//                                            }
                                            String ticketNo = ticketBeans.get(position).getTicketNumber();
                                            String respTicketNo = ticketNo.substring(0, ticketNo.length() - 2) + "Nxt" + ticketNo.substring(ticketNo.length() - 2, ticketNo.length());
//
                                            json.put("ticketNo", respTicketNo);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (json != null) {
                                        new PMSWebTask(RetailerTicketScreen.this, path, "N/A", json, "SLERESULT", null, "Loading...").execute();

                                    } else
                                        Toast.makeText(RetailerTicketScreen.this, "Error on Fetching", Toast.LENGTH_SHORT).show();


                                }

                            }
                        }

                    });


                }
            } else if (retailerBean.getResponseCode() == 118) {
                View.OnClickListener okClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfo.setLogout(RetailerTicketScreen.this);
                        Intent intent = new Intent(RetailerTicketScreen.this,
                                MainScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        RetailerTicketScreen.this.overridePendingTransition(
                                GlobalVariables.startAmin,
                                GlobalVariables.endAmin);
                    }
                };
                new DownloadDialogBox(RetailerTicketScreen.this, /*txnBean.getErrorMsg()*/"Time Out. Login Again !", "", false, true, okClickListener, null).show();


            } else {
                new DownloadDialogBox(RetailerTicketScreen.this, jsonObject.getString("responseMsg"), "", false, true, null, null).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

//        if (isRefreshRequired)
//            fetchTicketStatus();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public void onResult(String methodType, Object resultData, Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
        if (resultData != null) {
            try {
                isRefreshRequired = false;
                if (resultData != null) {
                    switch (methodType) {
                        case "DGERESULT":
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    RetailerTicketScreen.this);
                            builder.setTitle("Win Status");
                            builder.setIcon(R.drawable.img_winning_balance);
                            JSONObject jsonObject1 = new JSONObject(resultData.toString());
                            JSONArray array = jsonObject1.getJSONArray("drawWinList");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject a = array.getJSONObject(i);
                                String x = a.optString("winResult");
                                String y = (String) a.opt("winningAmt");
                                builder.setMessage(x + " \n " + y);
                            }

                            builder.show();
                            DrawerBaseActivity.selectedItemId = -1;

                            break;

                        case "SLERESULT":
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(RetailerTicketScreen.this);
                            builder1.setTitle("Win Status");
                            builder1.setIcon(R.drawable.img_winning_balance);
                            JSONObject obj = new JSONObject(resultData.toString());
                            String x = obj.optString("winStatus");
                            String y = obj.optString("totalWinAmt");
                            builder1.setMessage(x + "\n" + y);
                            builder1.show();
                            DrawerBaseActivity.selectedItemId = -1;
                            break;

                        case "RETAILER_TICKETS":
                            try {
                                JSONObject json = new JSONObject((String) resultData);
                                String retailer_json = (String) resultData;
                                if ((json).getString("responseMsg").equalsIgnoreCase("SUCCESS")) {
                                    parseJson(retailer_json);
                                } else {
                                    String errorMsg = json.optString("responseMsg");
                                    if (errorMsg.trim().length() > 0) {
                                        Toast.makeText(RetailerTicketScreen.this, errorMsg, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            DrawerBaseActivity.selectedItemId = -1;
                            break;

//                    case "BACKGROUND":
//                        String json = (String) resultData;
//                        try {
//                            JSONObject jsonObject = new JSONObject(json);
//                            String resp = "";
//                            int respCode = 10;
//                            if (jsonObject.has("responseMsg")) {
//                                resp = jsonObject.getString("responseMsg");
//                            }
//                            if (jsonObject.has("errorCode")) {
//                                respCode = jsonObject.getInt("errorCode");
//                            }
//                            if (resp.equalsIgnoreCase("Success") || respCode == 0) {
//                                JSONArray jsonArray = jsonObject.getJSONArray("txnStatus");
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    for (int j = 0; j < ticketBeans.size(); j++) {
//                                        JSONObject object = jsonArray.getJSONObject(i);
//                                        if (object.getString("txnId").equalsIgnoreCase(ticketBeans.get(j).getTxnId())) {
//                                            if (object.has("winStatus"))
//                                                ticketBeans.get(j).setTxnPwt(object.getString("winStatus"));
//                                            if (object.has("winAmount"))
//                                                ticketBeans.get(j).setWinAmount(object.getDouble("winAmount"));
//                                        }
//                                    }
//                                }
//                                adapter.notifyDataSetChanged();
//                                adapter.notifyDataSetInvalidated();
//                                int index = itemList.getFirstVisiblePosition();
//                                View v = itemList.getChildAt(0);
//                                int top = (v == null) ? 0 : v.getTop();
//                                itemList.setSelectionFromTop(index, top);
//                                txnStatusBeans.remove(0);
//                                if (txnStatusBeans.size() != 0) {
//                                    new WebTask(this, txnStatusBeans.get(0), "BACKGROUND").execute();
//                                }
//                            } else if ((respCode == 501) || (respCode == 2001) || (respCode == 20001)) {
//                                dialog.dismiss();
//                                Toast.makeText(mActivity, getString(R.string.sql_exception), Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(mActivity, jsonObject.getString("responseMsg"), Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(mActivity, "Some Internal Error", Toast.LENGTH_SHORT).show();
////                        GlobalVariables.showServerErr(mActivity);
//                        } catch (Exception e) {
//
//                            e.printStackTrace();
//                            Toast.makeText(mActivity, "Some Internal error !", Toast.LENGTH_SHORT).show();
//
//                        }
//                        break;
                        case "SLE_TRACK":
                            Gson gson = new Gson();
                            SportsTicketBean sportsTicketBean = gson.fromJson(resultData.toString(),
                                    SportsTicketBean.class);

                            if (sportsTicketBean.getResponseMsg().equalsIgnoreCase("success")) {
                                Intent intent = new Intent(mActivity,
                                        SportsLotteryTicketActivity.class);
                                intent.putExtra("trackTicket", true);
                                intent.putExtra("sportsTicketBean", sportsTicketBean);
                                startActivity(intent);
                            }
                            DrawerBaseActivity.selectedItemId = -1;
                            break;

                        default:
                            try {
                                JSONObject response = (JSONObject) resultData;
                                if (response.getBoolean("isSuccess")) {

                                    Intent intent = new Intent(mActivity, TrackTicketActivity.class);
                                    String gameDevName = ((JSONObject) ((JSONObject) response.getJSONObject("mainData")).getJSONObject("commonSaleData")).getString("gameDevName");


                                    intent.putExtra("betCode", 1);
                                    intent.putExtra("data", resultData.toString());
                                    intent.putExtra("isPurchase", true);
                                    startActivity(intent);
                                    mActivity.overridePendingTransition(
                                            GlobalVariables.startAmin, GlobalVariables.endAmin);
                                } else {
                                    if ((response.getInt("errorCode") == 118) || (response.getInt("errorCode") == 3009)) {
                                        View.OnClickListener okClickListener = new View.OnClickListener() {
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
                                        new DownloadDialogBox(mActivity, response.getString("errorMsg"), "", false, true, okClickListener, null).show();
                                    } else if ((response.getInt("errorCode") == 501) || (response.getInt("errorCode") == 2001) || (response.getInt("errorCode") == 20001)) {
                                        GlobalVariables.showServerErr(getApplicationContext());
                                    } else {
                                        Toast.makeText(mActivity, response.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                GlobalVariables.showServerErr(getApplicationContext());
                                e.printStackTrace();
//                        GlobalVariables.showServerErr(mActivity);
                            }
                            break;
                    }

                } else {
                    Toast.makeText(mActivity, "Some Internal Error !", Toast.LENGTH_SHORT).show();

                }
            } catch (IllegalStateException e) {
                GlobalVariables.showServerErr(getApplicationContext());
            } catch (JSONException e) {
                GlobalVariables.showServerErr(getApplicationContext());
                e.printStackTrace();
            }
        } else {
            GlobalVariables.showServerErr(getApplicationContext());
        }
        DrawerBaseActivity.selectedItemId = -1;
    }

    public JSONObject fakeJsonResponseWin() {
        JSONObject jsonObject = null;
        String fakeJson = "{\"isSuccess\":true,\"responseData\":{\"agentId\":0,\"agentName\":0,\"drawWinList\":[{\"drawDateTime\":\"2016-09-12 13:00:00\",\"drawId\":1434,\"winStatus eventId\":0,\"isAppReq\":false,\"isHighLevel\":false,\"isValid\":false,\"panel WinList\":[{\"betAmtMultiple\":1,\"isAppReq\":false,\"isHighLevel\":false,\"isValid\":false,\"panelId\":1,\"lineId\":1,\"partyId\":0,\"pickedData\":\"03,50,78\",\"status \":\"N.A.\",\"winningAmt\":0.0,\"playType\":\"Direct3\",\"rankId\":0,\"promoWin Amt\":0.0}],\"status\":\"R.A\",\"tableName\":\"1434\",\"winningAmt\":\"0.0\",\" winResult\":\"N.A \",\"drawname\":\"Monday Special Mid\",\"drawStatusForticket\":\"VERIFICATION_PENDING\",\"tktStatusForDraw\":\"UNBLOCKED\",\"rankId\":0}],\"gameName\":\"Original 5/90 \",\"gameNo\":2,\"noOfDraws\":1,\"partyId\":{\"16128\":\"16128\"},\"retailerId\":16128,\"saleAmt\":0.1,\"saleDate\":\"201 6-09-11 10:10:49\",\"status\":\"SUCCESS\",\"statusCheck\":\"\",\"isRaffle\":false,\"gam eId\":2}}";
        try {
            jsonObject = new JSONObject(fakeJson);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }
}

