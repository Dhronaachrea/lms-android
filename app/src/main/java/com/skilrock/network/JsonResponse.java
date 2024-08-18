package com.skilrock.network;

import android.app.Dialog;

import com.google.gson.Gson;
import com.skilrock.config.Config;
import com.skilrock.utils.Utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by stpl on 1/5/2016.
 */
public class JsonResponse extends Network {

    private Object object;
    private Gson gson;
    private Class aClass;
    private Response response;
    private Dialog dialog;

    public JsonResponse(Request request, Response response) {
        this(request, null, response);
    }

    public JsonResponse(Request request, Class aClass, Response response) {
        super(request);
        this.gson = new Gson();
        this.aClass = aClass;
        this.response = response;
    }

    @Override
    public void doInBackground(InputStream in, Dialog dialog) {
        this.dialog = dialog;
        try {
            String data = getString(in);
            if (data == null) return;
            Utils.logPrint("Response:-" + data);
            if (aClass != null)
                object = gson.fromJson(data, aClass);
            else
                object = data;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (response != null)
            response.onComplete(object, dialog, request.getTag());
    }

    private String getString(InputStream inputStream) throws IOException {
        if (inputStream == null) return null;
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
