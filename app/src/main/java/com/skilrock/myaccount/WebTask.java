package com.skilrock.myaccount;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.skilrock.config.Config;
import com.skilrock.config.DummyJson;
import com.skilrock.config.VariableStorage;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.utils.GlobalVariables;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WebTask extends AsyncTask<Void, Void, String> {

    private WebServicesListener listener;
    private TxnStatusBean urlData;
    private String path;
    private String methodType;
    private Activity activity;
    private String methodTypeAppend = "IGE";

    public WebTask(WebServicesListener listener, TxnStatusBean urlData, String methodType) {
        this.urlData = urlData;
        this.listener = listener;
        this.activity = ((Fragment) listener).getActivity();
        this.methodType = methodType;
        this.path = urlData.getUrl();
        if (urlData != null && urlData.getUrl().toUpperCase().contains("INSTANTGAMEENGINE")) {
            methodTypeAppend = "IGE_TXN";
            this.methodType += methodTypeAppend;
        } else if (urlData != null && (urlData.getUrl().contains("DGE_Scheduler") || urlData.getUrl().toUpperCase().contains("DGE"))) {
            methodTypeAppend = "DGE_TXN";
            this.methodType += methodTypeAppend;
        }
    }


    @Override
    protected String doInBackground(Void... params) {
        if (GlobalVariables.loadDummyData) {
            return DummyJson.getInstance(activity).getDummyMap().get("WebTask" + methodType);
        } else {
            InputStream instream = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(path);
            Writer writer = new StringWriter();
            BufferedReader reader = null;

            try {
                httpPost.setHeader("Content-Type", "application/json");
                if (path.toUpperCase().contains("DGE")) {
                    httpPost.setHeader("userName", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_MER_KEY));
                    httpPost.setHeader("password", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_SEQ_CODE));
                    String data = new Gson().toJson(urlData);
                    httpPost.setEntity(new StringEntity(data, "UTF-8"));
                } else if (path.toUpperCase().contains("SPORTSLOTTERY")) {
                    httpPost.setHeader("userName", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.SLE_MER_KEY));
                    httpPost.setHeader("password", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.SLE_SEQ_CODE));
                    String data = new Gson().toJson(urlData);
                    httpPost.setEntity(new StringEntity(data, "UTF-8"));
                } else if (path.contains("/gamePlay/api/txnStatus.action?txnStatusData=")) {
                    String data = new Gson().toJson(urlData);
                    httpPost = new HttpPost(path + URLEncoder.encode(data));
                }
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();

                    int ch = -1;
                    StringBuffer sb = new StringBuffer();
                    while ((ch = instream.read()) != -1) {
                        sb.append((char) ch);
                    }
                    String string = sb.toString();
                    if (Config.isStatic) {
                        HashMap<String, String> dummyMap = DummyJson.getInstance(activity).getDummyMap();
                        dummyMap.put("WebTask" + methodType, string);
                        DummyJson.getInstance(activity).setDummyMap(dummyMap);
                    }
                    return string;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (urlData != null && (urlData.getUrl().toUpperCase().contains("INSTANTGAMEENGINE") || urlData.getUrl().toUpperCase().contains("DGE") || urlData.getUrl().toUpperCase().contains("DGE_Scheduler"))) {
            methodType = "BACKGROUND";
        }
        listener.onResult(methodType, result, null);
    }
}
