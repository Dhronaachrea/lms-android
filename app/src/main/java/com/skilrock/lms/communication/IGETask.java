package com.skilrock.lms.communication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.skilrock.config.Config;
import com.skilrock.config.DummyJson;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.MyAnimDrawable;
import com.skilrock.escratch.customui.LoadingDialog;
import com.skilrock.network.Network;
import com.skilrock.network.Request;
import com.skilrock.utils.GlobalVariables;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;

public class IGETask extends Network {
    private final String jsonData;
    //private ProgressDialog dialog;
    private Activity activity;
    private String methodType;
    private Object result;
    private String message = "";
    private WebServicesListener listener;
    private String strURL;
    private Fragment fragment;
    private Context context;
    private View loadView;
    private MyAnimDrawable flyAnimationDrawable1;
    private LoadingDialog dialog;
    //    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private String gameCode;
    private boolean txnId = false;
//    private Request request;

    public IGETask(Activity activity, String methodType, String strURL, String jsonData, String message) {
        super(null);
        context = this.activity = activity;
        this.strURL = strURL;
        this.jsonData = jsonData;
        this.message = message;
        this.methodType = methodType;
        this.listener = (WebServicesListener) activity;
        request = newRequestData();
//        request = newRequestData();
    }

    public IGETask(Fragment fragment, String methodType, String strURL, String jsonData, String message) {
        super(null);
        txnId = false;
        this.fragment = fragment;
        this.strURL = strURL;
        this.jsonData = jsonData;
        this.methodType = methodType;
        this.message = message;
        this.listener = (WebServicesListener) fragment;
        context = activity = fragment.getActivity();

        if (Config.isStatic && jsonData != null) {
            if ("IGE".equalsIgnoreCase(methodType) && jsonData.contains("txnId")) {
                txnId = true;
                this.methodType = methodType + jsonData;
            }
        }
        request = newRequestData();
//        request = newRequestData();
    }


    @Override
    protected void onPreExecute() {
        dialog = new LoadingDialog(context);
        dialog.setMessage(message);
        dialog.show();
//        super.onPreExecute();
    }

//    @Override
//    protected Object doInBackground(Void... params) {


//        if (Config.isStatic && GlobalVariables.loadDummyData) {
//            result = JSONParser.parse(DummyJson.getInstance(context).getDummyMap().get("IGETask" + methodType));
//        } else {
//            OkHttpClient httpClient = new OkHttpClient();
//            Request request = null;
//            Request.Builder requestBuilder = null;
//            try {
//                System.out.println(strURL + "&lang=eng");
//                requestBuilder = new Request.Builder().url(strURL + "&lang=eng").addHeader("Content-Type", "application/json")
//                        .addHeader("sessionId", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.SESSION_ID))
//                        .addHeader("userName", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.IGE_MER_KEY))
//                        .addHeader("password", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.IGE_SEC_CODE))
//                        .addHeader("reqChannel", Config.reqChannel)
//                        .addHeader("appVersion", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.APP_VERSION));
//                if (jsonData != null) {
//                    requestBuilder.post(RequestBody.create(MEDIA_TYPE_MARKDOWN, jsonData.toString()));
//                }
//                request = requestBuilder.build();
//
//                Response response = httpClient.newCall(request).execute();
//                String resultString = response.body().string();
//                result = JSONParser.parse(resultString);
//                if (Config.isStatic) {
//                    HashMap<String, String> dummyMap = DummyJson.getInstance(context).getDummyMap();
//                    dummyMap.put("IGETask" + methodType, resultString);
//                    DummyJson.getInstance(context).setDummyMap(dummyMap);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }

    @Override
    public void doInBackground(InputStream in, Dialog dialog) {
        if (Config.isStatic && GlobalVariables.loadDummyData) {
            result = JSONParser.parse(DummyJson.getInstance(context).getDummyMap().get("IGETask" + methodType));
        } else {
            try {
                String resultString = getString(in);
                result = JSONParser.parse(resultString);
                if (Config.isStatic) {
                    HashMap<String, String> dummyMap = DummyJson.getInstance(context).getDummyMap();
                    dummyMap.put("IGETask" + methodType, resultString);
                    DummyJson.getInstance(context).setDummyMap(dummyMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onPostExecute(Void v) {
//        super.onPostExecute(result);
//        if (dialog.isShowing()) {
//            dialog.dismiss();
//            //loadView.setVisibility(View.GONE);
//        }
        if (methodType.contains("GAME_ASSETS")) {
            methodType = "GAME_ASSETS";
        } else if (methodType.contains("BACK_TXN")) {
            methodType = "BACK_TXN";
        } else if (methodType.contains("GAME_FINISH")) {
            methodType = "GAME_FINISH";
        } else if (txnId) {
            methodType = "IGE";
        }
        if (activity == null || activity.isFinishing())
            return;
        listener.onResult(methodType, result, dialog);
    }

    private Request newRequestData() {
        // Request According to Network
        Request request = new Request(strURL);
        request.setMethod(Request.Method.POST);
        request.setTag(methodType);

        //set Headers;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("sessionId", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.SESSION_ID));
        headers.put("userName", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.IGE_MER_KEY));
        headers.put("password", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.IGE_SEC_CODE));
        headers.put("reqChannel", Config.reqChannel);
        headers.put("appVersion", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.APP_VERSION));
        request.setHeaders(headers);
        if (jsonData != null) {
            request.setStringParams(jsonData.toString());
        }

        return request;
    }

    private String getString(InputStream inputStream) throws IOException {
        StringBuffer textInfo = new StringBuffer();
        BufferedInputStream is = new BufferedInputStream(inputStream);
        int data;
        while ((data = is.read()) != -1)
            textInfo.append((char) data);
        is.close();
        inputStream.close();
        return textInfo.toString();
    }
}