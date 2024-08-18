package com.skilrock.lms.communication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.gson.Gson;
import com.skilrock.config.Config;
import com.skilrock.config.DummyJson;
import com.skilrock.customui.MyAnimDrawable;
import com.skilrock.escratch.customui.LoadingDialog;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class WeaverWebTask extends AsyncTask<Void, Void, Object> {
    // private ProgressDialog dialog;
    private Activity activity;
    private String methodType;
    private Class<?> output;
    private Object result;
    private String message = "";
    private JSONObject urlData;
    private WebServicesListener listener;
    private String path;
    private Context context;
    private DrawerBaseListener drawerBaseListener;
    private View loadView;
    private MyAnimDrawable flyAnimationDrawable1;
    protected LoadingDialog dialog;
    private String jsonTag = "";

    public WeaverWebTask(Context context, String message) {
        this.context = context;
        this.message = message;
    }

    public WeaverWebTask(Activity activity, String path, String jsonTag, JSONObject urlData,
                         String methodType, Class<?> output, String message) {
        this.activity = activity;
        context = activity;
        this.path = path;
        this.urlData = urlData;
        this.methodType = methodType;
        this.output = output;
        this.message = message;
        this.jsonTag = jsonTag;
        if (path.contains("playerLogout.action?") || path.contains("myAccountInfo.action?") || (path.contains("fetchNearByRetailerInfo.action?") && !(methodType.equals("RETAILER_HACK"))) || path.contains("playerLogout")) {
            this.drawerBaseListener = (DrawerBaseListener) activity;
        } else {
            this.listener = (WebServicesListener) activity;
        }
    }

    public WeaverWebTask(Fragment fragment, String path, String jsonTag, JSONObject urlData,
                         String methodType, Class<?> output, String message) {
        this.activity = fragment.getActivity();
        this.path = path;
        this.urlData = urlData;
        this.methodType = methodType;
        this.output = output;
        this.message = message;
        this.jsonTag = jsonTag;
        this.listener = (WebServicesListener) fragment;
        context = fragment.getActivity();
    }

    @Override
    protected void onPreExecute() {
        dialog = new LoadingDialog(context);
        dialog.setMessage(message);
        dialog.show();
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Void... params) {
        Gson gson = new Gson();
        if (Config.isStatic && GlobalVariables.loadDummyData) {
            return (output == null) ? DummyJson.getInstance(context).getDummyMap().get("WeaverWebTask" + methodType) : gson.fromJson(DummyJson.getInstance(context).getDummyMap().get("WeaverWebTask" + methodType), output);
        } else {
            try {
                URL url = new URL(Config.baseURLWearer + path);
                Utils.consolePrint(Config.baseURLWearer + path);
                if (urlData != null)
                    Utils.consolePrint(urlData.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestMethod("POST");
                con.setReadTimeout(15 * 1000);
                con.setConnectTimeout(15 * 1000);
                OutputStream os = con.getOutputStream();
                try {
                    // Add your data
                    if (urlData != null) {
                        if (jsonTag.equals("N/A")) {
                            os.write(urlData.toString().getBytes("UTF-8"));
//                        httppost.setEntity(new StringEntity(urlData.toString(), "UTF-8"));
                        } else if (jsonTag.equals("GET")) {
                        } else {
                            os.write(urlData.toString().getBytes());
                        }
                    }
                    // Execute HTTP Post Request
                    StringBuilder sb = new StringBuilder();
                    int HttpResult = con.getResponseCode();
                    if (HttpResult == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                    }

                    Utils.consolePrint("" + sb.toString());
                    if (Config.isStatic) {
                        HashMap<String, String> dummyMap = DummyJson.getInstance(context).getDummyMap();
                        dummyMap.put("WeaverWebTask" + methodType, sb.toString());
                        DummyJson.getInstance(context).setDummyMap(dummyMap);
                    }

                    gson = new Gson();
                    if (output != null)
                        result = gson.fromJson(sb.toString(), output);
                    else
                        return sb.toString();

                } catch (Exception e) {
                    switch (methodType) {
                        case "GET_STATS":
                            result = "[{\"gameCode\":\"KenoTwo\",\"subDatas\":[{\"frequency\":6,\"lastSeenDisplay\":\"1 Hr 2 Min 3 Sec Ago\",\"lastSeenSec\":\"3000\",\"ball\":13},{\"frequency\":9,\"lastSeenDisplay\":\"1 Hr 0 Min 3 Sec Ago\",\"lastSeenSec\":\"2500\",\"ball\":78},{\"frequency\":5,\"lastSeenDisplay\":\"2 Min 3 Sec Ago\",\"lastSeenSec\":\"2000\",\"ball\":6}]},{\"gameCode\":\"LottoBonus\",\"subDatas\":[{\"frequency\":12,\"lastSeenDisplay\":\"5 Min Ago\",\"lastSeenSec\":\"3500\",\"ball\":1},{\"frequency\":8,\"lastSeenDisplay\":\"3 Hr Ago\",\"lastSeenSec\":\"4000\",\"ball\":22},{\"frequency\":7,\"lastSeenDisplay\":\"25 Sec Ago\",\"lastSeenSec\":\"3000\",\"ball\":89}]},{\"gameCode\":\"Zerotonine\",\"subDatas\":[{\"frequency\":8,\"lastSeenDisplay\":\"30 Sec Ago\",\"lastSeenSec\":\"3000\",\"ball\":5},{\"frequency\":9,\"lastSeenDisplay\":\"1 Min Ago\",\"lastSeenSec\":\"3500\",\"ball\":3},{\"frequency\":3,\"lastSeenDisplay\":\"5 Min Ago\",\"lastSeenSec\":\"4000\",\"ball\":9}]}]";
                    }
                } finally {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
//        if (dialog.isShowing()) {
//            dialog.dismiss();
//            //loadView.setVisibility(View.GONE);
//        }
        if (path.contains("playerLogout.action?") || path.contains("myAccountInfo.action?") || (path.contains("fetchNearByRetailerInfo.action?") && !(methodType.equals("RETAILER_HACK"))) || path.contains("playerLogout")) {
            drawerBaseListener.onListen(methodType, result, dialog);
        } else {
            listener.onResult(methodType, result, dialog);
        }
    }
}
