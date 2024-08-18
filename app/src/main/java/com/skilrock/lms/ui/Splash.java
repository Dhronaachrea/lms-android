package com.skilrock.lms.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.customui.MyAnimDrawable;
import com.skilrock.lms.communication.Communication;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.DataSource;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Splash extends Activity {
    //private ProgressBar progressBar;
    private RelativeLayout splashMain;
    private CustomTextView copyrightText;
    private TextView orgName;
    public static final int progress_bar_type = 0;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private boolean downloadStarted = false;
    private Handler handler;
    private ProgressDialog pDialog;
    private boolean downloadComplete;
    private Resources resources;
    private Context context;

    private JSONObject jsonData;
    private double availableVersion;
    private String OTPRequest;
    private ArrayList<HashMap<String, String>> banners;
    private ArrayList<HashMap<String, String>> serviceInfos;
    private boolean isForceDownload;
    private Gson serviceGson, bannerGson;
    private ImageView animImage;
    private MyAnimDrawable flyAnimationDrawable1;
    private ImageView logoImage;
    private Analytics analytics;
    private GlobalPref globalPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalPref = GlobalPref.getInstance(this);
        context = Splash.this;
        Communication.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /**
         * Set Configuration
         */
        Config.getInstance().initialize(getApplicationContext());

        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.SPLASH);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);

        resources = getResources();
        setContentView(R.layout.activity_splash);
        bindIds();
        updateUIThemes();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) logoImage.getLayoutParams();
        layoutParams.setMargins(0, metrics.widthPixels / 4, 0, 0);
        logoImage.setLayoutParams(layoutParams);

        /***************Flurry and Google Analytics***********/

        // Builder parameters can overwrite the screen name set on the tracker.
        // tracker.send(new HitBuilders.EventBuilder().setCategory("UX").setAction("Check Version").setLabel("Version Check").set(Fields.SPLASH, "Success").build());

        /********************Analytics End********************/


        handler = new Handler();
        //progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                analytics.sendAll(Fields.Category.SPLASH, Fields.Action.OPEN, Fields.Label.VERSION);

                checkUpdate.start();
            }
        }, 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //*********Please do not pass application context in param*********//
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (VariableStorage.GlobalPref.getStringData(getApplicationContext(),
                VariableStorage.GlobalPref.EXIT_APP).equals("true")) {
            VariableStorage.GlobalPref.setStringPreferences(getApplicationContext(),
                    VariableStorage.GlobalPref.EXIT_APP, "false");
            //finish();
        }
        copyrightText.setText(resources.getString(R.string.ver) + getVersion()
                + "");
        orgName.setText(Config.orgName);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Thread t = Thread.currentThread();
        // t.stop();
        t = null;
        System.exit(0);
    }

    private Thread checkUpdate = new Thread() {
        public void run() {
            try {
                if (GlobalVariables.connectivityExists(context)) {
//                    if (!Config.isHTTPS)
                    jsonData = Communication.getVersionControl(getVersion(),
                            DataSource.deviceName, DataSource.deviceType);
//                    else
//                        jsonData = Communication.getVersionControlHTTPS(getVersion(),
//                                DataSource.deviceName, DataSource.deviceType);
                    VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.APP_VERSION, getVersion() + "");
                    if (jsonData != null) {
                        if (jsonData.getBoolean("isSuccess")) {
                            analytics.sendAll(Fields.Category.SPLASH, Fields.Action.VERSION, Fields.Label.SUCCESS);

                            if (jsonData.getBoolean("isNewVersion")) {
                                availableVersion = Double.parseDouble(jsonData
                                        .getString("updatedVersion"));
                                if (jsonData.getString("isMandatory").equals("YES") && !GlobalVariables.loadDummyData)
                                    isForceDownload = true;
                                else
                                    isForceDownload = false;
                            }

                            if (jsonData.has("OTPReq")) {
                                OTPRequest = jsonData.getString("OTPReq");
                            } else {
                                OTPRequest = "No";
                            }

//                            isPlayerLeagueActive
                            VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.PLAYER_LEAGUE_ACTIVE, jsonData.optString("isPlayerLeagueActive"));


                            VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.COMPANY_NAME, jsonData.getString("companyName"));
//                            GlobalPref.getInstance(context).setCountry(jsonData.getString("country"));

                            String abc = jsonData.getString("currencyCode");
                            if (globalPref.getCountry().equalsIgnoreCase("Lagos")) {
                                VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.CURRENCY_CODE, Html.fromHtml(jsonData.getString("currencyCode")) + " ");
                            } else {
                                VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.CURRENCY_CODE, jsonData.getString("currencyCode") + " ");
                            }
                            VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.CURRENCY_SYMBOL, jsonData.getString("currencySymbol") + " ");
                            VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.DATE_FORMAT, jsonData.getString("dateFormat"));
                            VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.MOBILE_NO_LENGTH, jsonData.getString("mobileNoLength"));
                            VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.USERNAME_MIN_LENGTH, jsonData.getString("usernameMinLength"));

                            VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.PMS_MER_KEY, jsonData.getString("merchantKey"));
                            VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.PMS_SECURE_CODE, jsonData.getString("secureCode"));


                            JSONArray jsonArray = jsonData.getJSONArray("servicesData");
                            serviceInfos = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("serviceCode", jsonObject.getString("serviceCode"));
                                map.put("serviceName", jsonObject.getString("serviceName"));
                                serviceInfos.add(map);
                                switch (jsonObject.getString("serviceCode")) {
                                    case Config.IGE:
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.IGE_MER_KEY, jsonObject.getString("merchantKey"));
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.IGE_DOM_NAME, jsonObject.getString("domainName"));
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.IGE_LANG, jsonObject.getString("lang"));
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.IGE_MER_CODE, jsonObject.getString("merchantCode"));
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.IGE_SER_NAME, jsonObject.getString("serviceName"));
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.IGE_SER_CODE, jsonObject.getString("serviceCode"));
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.IGE_ROOT_URL, jsonObject.getString("serviceRootUrl"));
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.IGE_SEC_CODE, jsonObject.getString("secureCode"));
                                        break;
                                    case Config.DG:
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.DGE_SER_NAME, jsonObject.getString("serviceName"));
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.DGE_SEQ_CODE, jsonObject.getString("secureCode"));
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.DGE_ROOT_URL, jsonObject.getString("serviceRootUrl"));
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.DGE_MER_KEY, jsonObject.getString("merchantKey"));
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.DGE_MER_CODE, jsonObject.getString("merchantCode"));
                                        break;
                                    case Config.SL:
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.SLE_SER_NAME, jsonObject.getString("serviceName"));
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.SLE_ROOT_URL, jsonObject.getString("serviceRootUrl"));
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.SLE_MER_CODE, jsonObject.getString("merchantCode"));
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.SLE_MER_KEY, jsonObject.getString("merchantKey"));
                                        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.SLE_SEQ_CODE, jsonObject.getString("secureCode"));
                                        VariableStorage.GlobalPref.setBooleanPreferences(context, VariableStorage.GlobalPref.SLE_IS_ENABLED, /*true*/ jsonObject.optBoolean("isSLSaleEnabled", false));
                                        break;
                                }
                            }
                            if (jsonData.has("banners")) {
                                JSONArray imagePath = jsonData.getJSONArray("banners");
                                banners = new ArrayList<>();
                                for (int i = 0; i < imagePath.length(); i++) {
                                    if (imagePath.length() > 0) {
                                        JSONObject object = imagePath.getJSONObject(i);
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put("imageurl", object.getString("imageUrl"));
                                        map.put("actiontype", object.getString("actionType"));
                                        map.put("actionvalue", object.getString("actionValue"));
                                        banners.add(map);
                                    }
                                }
                            }
                            if (availableVersion > getVersion() && !GlobalVariables.loadDummyData) {
                                handler.post(showUpdate);

                            } else {
                                startCommonLogin();
                            }


                        } else {
                            analytics.sendAll(Fields.Category.SPLASH, Fields.Action.VERSION, Fields.Label.FAILURE);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Utils.Toast(context, jsonData.getString("errorMsg"));
                                        finish();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } else if (Config.isStatic && GlobalVariables.loadDummyData) {
                        Utils.Toast(context, "Data not available in offline mode");
                        finish();
                    } else {
                        analytics.sendAll(Fields.Category.SPLASH, Fields.Action.VERSION, Fields.Label.FAILURE);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.Toast(context, "Server error!");
                                finish();
                            }
                        });
                    }
                } else {
                    analytics.sendAll(Fields.Category.SPLASH, Fields.Action.VERSION, Fields.Label.FAILURE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GlobalVariables.showDataAlert(context);
                        }
                    });

                }
            } catch (Exception e) {
                analytics.sendAll(Fields.Category.SPLASH, Fields.Action.VERSION, Fields.Label.FAILURE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.Toast(context, "Server error!");
                        finish();
                    }
                });
            }
        }
    };

    private Runnable showUpdate = new Runnable() {
        public void run() {
            //progressBar.setVisibility(View.GONE);
            OnClickListener okClickListener = new OnClickListener() {

                @Override
                public void onClick(View v) {
                    downloadStarted = true;
                    downloadComplete = false;
                    try {
                        new DownloadUpdate().execute(jsonData.getString("downloadUrl"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            OnClickListener cancelClickListener = new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (isForceDownload) {
                        finish();
                    } else {
                        startCommonLogin();
                    }
                }
            };
            try {
                new DownloadDialogBox(Splash.this,
                        resources.getString(R.string.download_text)
                                + " " + jsonData.getString("updatedVersion") + " " + resources.getString(R.string.download_text_two),
                        resources.getString(R.string.update_text) + getVersion(), true, true,
                        okClickListener, cancelClickListener).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    public double getVersion() {
        double currentVersion = 1.0;
        try {
            String version = getPackageManager().getPackageInfo(
                    getApplicationInfo().packageName, 0).versionName;
            currentVersion = Double.parseDouble(version);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentVersion;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage(resources.getString(R.string.downloading));
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    class DownloadUpdate extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(0);
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                // System.out.println(url.toURI());
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setConnectTimeout(15000);
                c.setDoOutput(true);
                c.connect();

                int contentLength = c.getContentLength();

                // download the file
                // InputStream input = new BufferedInputStream(url.openStream(),
                // 8192);

                // Output stream
                // OutputStream output = new
                // FileOutputStream("/sdcard/downloadedfile.jpg");

                String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "";
                File file = new File(PATH);
                file.mkdirs();

                File outputFile = new File(file, BuildConfig.APP_NAME);
                if (outputFile.exists())
                    outputFile.delete();
                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = is.read(data)) != -1) {
                    total += count;
                    // publishing the progress onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / contentLength));

                    // writing data to file
                    fos.write(data, 0, count);
                }
                if (total == contentLength)
                    downloadComplete = true;

                fos.flush();
                fos.close();
                is.close();

            } catch (Exception e) {
                downloadComplete = false;
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            dismissDialog(0);

            if (downloadComplete) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory()
                                + Config.apkPath
                                + BuildConfig.APP_NAME)),
                        "application/vnd.android.package-archive");
                startActivity(intent);
            } else {
                OnClickListener okClickListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadStarted = false;
                        finish();
                    }
                };
                new DownloadDialogBox(Splash.this,
                        resources.getString(R.string.dwn_fld_msg),
                        resources.getString(R.string.dwn_fld), false, true,
                        okClickListener, null).show();
            }
        }
    }

    private void startCommonLogin() {
        Intent intent = new Intent(Splash.this, MainScreen.class);
        intent.putExtra("isSLE", getIntent().getBooleanExtra("isSLE", false));
        intent.putExtra("gameTypeId", getIntent().getStringExtra("gameTypeId"));
        serviceGson = new Gson();
        bannerGson = new Gson();
        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.DGE_SERVICE_DETAILS, serviceGson.toJson(serviceInfos));
        if (banners != null) {
            VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.DGE_BANNERS_DETAILS, bannerGson.toJson(banners));
        } else {
            VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.DGE_BANNERS_DETAILS, bannerGson.toJson(new ArrayList<>()));
        }
        startActivity(intent);
        this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        Splash.this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }

    private void updateUIThemes() {
        //progressBar.getIndeterminateDrawable().setColorFilter(
        //resources.getColor(R.color.splash_progress_color),
        // android.graphics.PorterDuff.Mode.SRC_IN);
    }

    private void bindIds() {
        logoImage = (ImageView) findViewById(R.id.logo_image);
        animImage = (ImageView) findViewById(R.id.anim_image);
        int[] animRes = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.i, R.drawable.j, R.drawable.k, R.drawable.l, R.drawable.m};
        flyAnimationDrawable1 = new MyAnimDrawable();
        for (int i = 0; i < animRes.length; i++) {
            flyAnimationDrawable1.addFrame(context.getResources().getDrawable(animRes[i]), 40);
        }
        flyAnimationDrawable1.setOneShot(false);
        animImage.setImageDrawable(flyAnimationDrawable1);
        ((Animatable) animImage.getDrawable()).start();
        splashMain = (RelativeLayout) findViewById(R.id.splash_main);
        //progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        copyrightText = (CustomTextView) findViewById(R.id.copyright);
        orgName = (TextView) findViewById(R.id.org_name);
    }

    @Override
    public void finish() {
        super.finish();
        DrawerBaseActivity.selectedItemId = -1;
        overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }



	/* ########### Banner Class Start ############# */


}
