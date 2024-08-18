package com.skilrock.network;

import android.app.Dialog;
import android.os.AsyncTask;

import com.skilrock.escratch.customui.LoadingDialog;
import com.skilrock.utils.Utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by stpl on 12/28/2015.
 */
public abstract class Network extends AsyncTask<Void, Integer, Void> {

    protected Request request;
    private HttpURLConnection httpURLConnection;
    private Dialog dialog;

    protected Network(Request request) {
        this.request = request;
    }

    @Override
    protected void onPreExecute() {
        if (request instanceof DialogRequest) {
            dialog = ((DialogRequest) request).getDialog();
            dialog.show();
        }
    }

    @Override
    protected Void doInBackground(Void[] params) {
        if (request == null || request.getUrl() == null) return null;
        try {
            Utils.logPrint("Request:-" + request.getUrl() + request.getStringParams());
            if (!request.getUrl().contains("https"))
                httpURLConnection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
            else
                httpURLConnection = (HttpsURLConnection) new URL(request.getUrl()).openConnection();
            httpURLConnection.setReadTimeout(40000);
            httpURLConnection.setConnectTimeout(10000);
            Map<String, String> headers = request.getHeaders();
            if (headers != null)
                for (String header : headers.keySet())
                    httpURLConnection.setRequestProperty(header, headers.get(header));
            setRequestMethod(httpURLConnection, request);
            doInBackground(httpURLConnection.getInputStream(), dialog);
        } catch (IOException e) {
            e.printStackTrace();
            doInBackground(null, dialog);
        }
        return null;
    }

    public abstract void doInBackground(InputStream in, Dialog dialog);

    private void setRequestMethod(HttpURLConnection httpURLConnection, Request request) throws IOException {
        switch (request.getMethod()) {
            case GET:
                httpURLConnection.setRequestMethod("GET");
                break;
            case POST:
                httpURLConnection.setRequestMethod("POST");
                addBodyIfExists(httpURLConnection, request);
        }
    }

    private void addBodyIfExists(HttpURLConnection connection, Request request)
            throws IOException {
        byte[] body = addBody(request.getParams());
        if (body != null) {
            connection.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(body);
            out.close();
        }
    }

    private byte[] addBody(Map<String, String> params) {
        if (params == null || params.size() < 1) return null;
        StringBuilder body = new StringBuilder();
        try {
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    body.append("&");
                if (entry.getKey().equals("JSON_DATA")) {
                    body.append(entry.getValue());
                    continue;
                }
                body.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                body.append("=");
                body.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding UTF-8");
        }
        return body.toString().getBytes();
    }

    public interface Response {
        void onComplete(Object in, Dialog dialog, String method);
    }
}
