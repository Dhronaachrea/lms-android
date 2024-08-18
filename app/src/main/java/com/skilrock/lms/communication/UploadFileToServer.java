package com.skilrock.lms.communication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;

import com.skilrock.bean.ProfileDetail;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;

public class UploadFileToServer extends AsyncTask<Void, Integer, String> {
    private long totalSize;
    private String filePath;
    private Activity activity;
    private String boundary;
    private BufferedReader br;
    private StringBuilder sb;
    private JSONObject jsonObject;
    private ProgressDialog progressDialog;

    public UploadFileToServer(Activity activity, String filePath, JSONObject jsonObject) {
        this.filePath = filePath;
        this.activity = activity;
        this.jsonObject = jsonObject;
    }

    @Override
    protected void onPreExecute() {
        // setting progress bar to zero
        // progressBar.setProgress(0);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading Image...");
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        // Making progress bar visible
        // progressBar.setVisibility(View.VISIBLE);
        // updating progress bar value
        //progressBar.setProgress(progress[0]);

        // updating percentage value
        // txtPercentage.setText(String.valueOf(progress[0]) + "%");
    }

    @Override
    protected String doInBackground(Void... params) {
        return uploadFile(filePath);
    }

    public String uploadFile(String sourceFileUri) {
        String result = "";
        int count = 0;
        String fileName = sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        boundary = "*****";
        boolean isSSL = false;
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        if (!sourceFile.isFile()) {
            return "";
        } else {
            while (true) {
                count++;
                try {
                    FileInputStream fileInputStream = new FileInputStream(
                            sourceFile);
                    Utils.consolePrint(Config.getInstance().getBaseURL() + "/rest/playerMgmt/upload");
                    URL url = new URL(Config.getInstance().getBaseURL() + "/rest/playerMgmt/upload");
                    // Open a HTTP connection to the URL
                    if (Config.getInstance().getBaseURL().contains("https")) {
                        conn = (HttpsURLConnection) url.openConnection();
                        if (isSSL) {
                            ((HttpsURLConnection) conn).setSSLSocketFactory(new SSLFactory());
                            ((HttpsURLConnection) conn).setHostnameVerifier(TrustAllSSLSocketFactory.hostnameVerifier);
                        }
                    } else
                        conn = (HttpURLConnection) url.openConnection();

                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    //conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data; boundary="
                                    + boundary);
                    conn.setRequestProperty("img", fileName);
                    conn.setRequestProperty("reqChannel", Config.reqChannel);
                    conn.setRequestProperty("appVersion", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.APP_VERSION));
                    conn.setRequestProperty("userName", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.PMS_MER_KEY));
                    conn.setRequestProperty("password", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.PMS_SECURE_CODE));
                    conn.setRequestProperty("playerId", VariableStorage.UserPref.getStringData(activity.getApplicationContext(), VariableStorage.UserPref.PLAYER_ID));
                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    String arr[] = fileName.split("/");
                    dos.writeBytes("Content-Disposition: form-data; name=\"img\";filename=\""
                            + arr[arr.length - 1] + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }
                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    // Responses from the server (code and message)
                    br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    sb = new StringBuilder();
                    String output;
                    while ((output = br.readLine()) != null) {
                        sb.append(output);
                    }
                    fileInputStream.close();
                    dos.flush();
                    dos.close();
                    if (sb != null) {
                        result = sb.toString();
                    } else {
                        result = "";
                    }
                    break;
                } catch (SSLException e) {
                    e.printStackTrace();
                    if (Config.isDebug && count < 3)
                        isSSL = true;
                    else
                        break;
                } catch (Exception e) {
                    e.printStackTrace();
                    result = "";
                    break;
                }
            }
            Utils.consolePrint(result);
            return result;

        } // End else block

    }

    @Override
    protected void onPostExecute(String result) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            //loadView.setVisibility(View.GONE);
        }
        try {
            JSONObject resultJson = new JSONObject(result.toString());
            if (resultJson.getInt("responseCode") == 0) {
                jsonObject.put("profilePhoto", resultJson.getString("imgPath"));
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Config.IMAGE_DIRECTORY_NAME);
                deleteRecursive(mediaStorageDir);
            } else {
                jsonObject.put("profilePhoto", "");
                Utils.Toast(activity, resultJson.getString("responseMsg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // save profile call
        PMSWebTask PMSWebTask = new PMSWebTask(activity, "/rest/playerMgmt/saveUserProfile", "N/A", jsonObject, "SAVE_PROFILE", ProfileDetail.class, "Saving...");
        PMSWebTask.execute();
        super.onPostExecute(result);
    }

    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);
        fileOrDirectory.delete();
    }
}