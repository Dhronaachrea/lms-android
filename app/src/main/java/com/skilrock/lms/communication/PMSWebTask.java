package com.skilrock.lms.communication;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.gson.Gson;
import com.skilrock.config.Config;
import com.skilrock.config.DummyJson;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.MyAnimDrawable;
import com.skilrock.escratch.customui.LoadingDialog;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.SSLException;

public class PMSWebTask extends AsyncTask<Void, Void, Object> {
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

    public PMSWebTask(Context context, String message) {
        this.context = context;
        this.message = message;
    }

    public PMSWebTask(Activity activity, String path, String jsonTag, JSONObject urlData,
                      String methodType, Class<?> output, String message) {
        this.activity = activity;
        context = activity;
        this.path = path;
        this.urlData = urlData;
        this.methodType = methodType;
        this.output = output;
        this.message = message;
        this.jsonTag = jsonTag;
        if ((path.contains("playerLogout.action?") && methodType.equalsIgnoreCase("LOGOUT")) || path.contains("myAccountInfo.action?") || (path.contains("fetchNearByRetailerInfo.action?") && !(methodType.equals("RETAILER_HACK")))) {
            this.drawerBaseListener = (DrawerBaseListener) activity;
        } else {
            this.listener = (WebServicesListener) activity;
        }
    }

    public PMSWebTask(Fragment fragment, String path, String jsonTag, JSONObject urlData,
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
        boolean isSSL = false;
        Gson gson = new Gson();
        if (Config.isStatic && GlobalVariables.loadDummyData) {
            return (output == null) ? DummyJson.getInstance(context).getDummyMap().get("PMSWebTask" + methodType) : gson.fromJson(DummyJson.getInstance(context).getDummyMap().get("PMSWebTask" + methodType), output);
        } else {
            InputStream instream = null;
            // Create a new HttpClient and Post Header
            /* new SSL Implementation by mehul */
            while (true) {
                HttpClient httpclient;
                if (isSSL)
                    httpclient = Communication.getNewHttpClient();
                else
                    httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.getInstance().getBaseURL() + path);
                Utils.consolePrint(Config.getInstance().getBaseURL() + path);
                httppost.setHeader("reqChannel", Config.reqChannel);
                httppost.setHeader("sessionId", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.SESSION_ID));
                httppost.setHeader("appVersion", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.APP_VERSION));
                httppost.setHeader("userName", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.PMS_MER_KEY));
                httppost.setHeader("password", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.PMS_SECURE_CODE));
                Utils.logPrint(Arrays.toString(httppost.getAllHeaders()));
                Writer writer = new StringWriter();
                BufferedReader reader = null;
                try {
                    // Add your data
                    if (urlData != null) {
                        httppost.setHeader("Content-Type", "application/json");
                        if (jsonTag.equals("N/A")) {
                            httppost.setEntity(new StringEntity(urlData.toString(), "UTF-8"));
                        } else if (jsonTag.equals("GET")) {
                        } else {
                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair(jsonTag, urlData
                                    .toString()));
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        }
                    }

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        instream = entity.getContent();
                        reader = new BufferedReader(new InputStreamReader(instream,
                                HTTP.UTF_8));
                        int count;
                        char[] buf = new char[1024];
                        while ((count = reader.read(buf)) != -1) {
                            writer.write(buf, 0, count);
                        }
                        String string = writer.toString();
                        Utils.consolePrint(string);
                        if (Config.isStatic) {
                            HashMap<String, String> dummyMap = DummyJson.getInstance(context).getDummyMap();
                            dummyMap.put("PMSWebTask" + methodType, string);
                            DummyJson.getInstance(context).setDummyMap(dummyMap);
                        }
                        if (output != null) {
                            result = gson.fromJson(string, output);
                        } else
                            return writer.toString();
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
                    switch (methodType) {
                        case "GET_STATS":
                            result = "[{\"gameCode\":\"KenoTwo\",\"subDatas\":[{\"frequency\":6,\"lastSeenDisplay\":\"1 Hr 2 Min 3 Sec Ago\",\"lastSeenSec\":\"3000\",\"ball\":13},{\"frequency\":9,\"lastSeenDisplay\":\"1 Hr 0 Min 3 Sec Ago\",\"lastSeenSec\":\"2500\",\"ball\":78},{\"frequency\":5,\"lastSeenDisplay\":\"2 Min 3 Sec Ago\",\"lastSeenSec\":\"2000\",\"ball\":6}]},{\"gameCode\":\"LottoBonus\",\"subDatas\":[{\"frequency\":12,\"lastSeenDisplay\":\"5 Min Ago\",\"lastSeenSec\":\"3500\",\"ball\":1},{\"frequency\":8,\"lastSeenDisplay\":\"3 Hr Ago\",\"lastSeenSec\":\"4000\",\"ball\":22},{\"frequency\":7,\"lastSeenDisplay\":\"25 Sec Ago\",\"lastSeenSec\":\"3000\",\"ball\":89}]},{\"gameCode\":\"Zerotonine\",\"subDatas\":[{\"frequency\":8,\"lastSeenDisplay\":\"30 Sec Ago\",\"lastSeenSec\":\"3000\",\"ball\":5},{\"frequency\":9,\"lastSeenDisplay\":\"1 Min Ago\",\"lastSeenSec\":\"3500\",\"ball\":3},{\"frequency\":3,\"lastSeenDisplay\":\"5 Min Ago\",\"lastSeenSec\":\"4000\",\"ball\":9}]}]";
                    }
                    break;
                }
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

        if (activity == null || activity.isFinishing())
            return;

        localSet();
        if ((path.contains("playerLogout.action?") && methodType.equalsIgnoreCase("LOGOUT")) || path.contains("myAccountInfo.action?") || (path.contains("fetchNearByRetailerInfo.action?") && !(methodType.equals("RETAILER_HACK")))) {
            drawerBaseListener.onListen(methodType, result, dialog);
        } else {
            listener.onResult(methodType, result, dialog);
        }
    }

    public void localSet() {
        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getResources().updateConfiguration(config, null);
    }
}
