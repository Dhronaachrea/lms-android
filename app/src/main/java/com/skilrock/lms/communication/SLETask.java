package com.skilrock.lms.communication;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.View;

import com.skilrock.config.Config;
import com.skilrock.config.DummyJson;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.MyAnimDrawable;
import com.skilrock.escratch.customui.LoadingDialog;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLException;

public class SLETask extends AsyncTask<Void, Void, Object> {
    private final String jsonData;
    //private ProgressDialog dialog;
    private Activity activity;
    private String methodType;
    private Class<?> output;
    private Object result;
    private String message = "";
    private JSONObject urlData;
    private WebServicesListener listener;
    private String strURL;
    private Fragment fragment;
    private Context context;
    private View loadView;
    private MyAnimDrawable flyAnimationDrawable1;
    private LoadingDialog dialog;
    private String tag = null;
    private String gameCode;
    private boolean txnId = false;

    public SLETask(Activity activity, String methodType, String strURL, String jsonData) {
        this(activity, methodType, null, strURL, jsonData);
    }

    public SLETask(Activity activity, String methodType, String tag, String strURL, String jsonData) {
        context = this.activity = activity;
        this.strURL = strURL;
        this.jsonData = jsonData;
        this.methodType = methodType;
        this.tag = tag;
        this.listener = (WebServicesListener) activity;
    }

    public SLETask(Fragment fragment, String methodType, String tag, String strURL, String jsonData) {
        txnId = false;
        this.fragment = fragment;
        this.strURL = strURL;
        this.tag = tag;
        this.jsonData = jsonData;
        this.methodType = methodType;
        this.listener = (WebServicesListener) fragment;
        context = activity = fragment.getActivity();

        if (Config.isStatic && jsonData != null) {
            if ("SLE_TRACK".equalsIgnoreCase(methodType) && jsonData.contains("txnId")) {
                txnId = true;
                this.methodType = methodType + jsonData;
            }
        }
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
        boolean isSSL = false;
        if (Config.isStatic && GlobalVariables.loadDummyData) {
            result = JSONParser.parse(DummyJson.getInstance(context).getDummyMap().get("SLETask" + methodType));
        } else {
            HttpClient httpClient = null;
            HttpPost httpPost = null;
            HttpEntity httpEntity = null;
            while (true) {
                try {
                    Utils.consolePrint(strURL);
                /* new SSL Implementation by mehul */
                    if (isSSL)
                        httpClient = Communication.getNewHttpClient();
                    else
                        httpClient = new DefaultHttpClient();
                    httpPost = new HttpPost(strURL);
                    httpPost.setHeader("Content-Type", "application/json");
                    httpPost.setHeader("sessionId", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.SESSION_ID));
                    httpPost.setHeader("userName", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.SLE_MER_KEY));
                    httpPost.setHeader("password", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.SLE_SEQ_CODE));
                    httpPost.setHeader("reqChannel", Config.reqChannel);
                    httpPost.setHeader("appVersion", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.APP_VERSION));
                    if (tag == null)
                        httpPost.setEntity(new StringEntity(jsonData.toString(), "UTF-8"));
                    else if (tag.equals("")) {
                    } else {
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                        nameValuePairs.add(new BasicNameValuePair(tag, jsonData
                                .toString()));
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    }

                    httpEntity = httpClient.execute(httpPost).getEntity();
                    if (httpEntity != null) {
                        InputStream inputStream = httpEntity.getContent();
                        result = JSONParser.parse(inputStream);
                        Utils.consolePrint(result.toString());
                        if (Config.isStatic) {
                            HashMap<String, String> dummyMap = DummyJson.getInstance(context).getDummyMap();
                            dummyMap.put("SLETask" + methodType, result.toString());
                            DummyJson.getInstance(context).setDummyMap(dummyMap);
                        }
                    }
                    break;
                } catch (SSLException e) {
                    e.printStackTrace();
                    if (Config.isDebug)
                        isSSL = true;
                    else
                        break;
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(final Object result) {
        super.onPostExecute(result);
//        if (dialog.isShowing()) {
//            dialog.dismiss();
//            //loadView.setVisibility(View.GONE);
//        }
        if (Config.isStatic && txnId) {
            methodType = "SLE_TRACK";
        }
        if (activity == null || activity.isFinishing()) return;
        listener.onResult(methodType, result, dialog);
    }
}
