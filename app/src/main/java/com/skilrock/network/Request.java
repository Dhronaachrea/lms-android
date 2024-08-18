package com.skilrock.network;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stpl on 12/28/2015.
 */
public class Request {
    private Map<String, String> headers;
    private Map<String, String> params;
    private String stringParams;
    private Method method;
    private String tag;
    private String url;

    public Request(String url) {
        this.url = url;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getStringParams() {
        return stringParams;
    }

    public void setStringParams(String stringParams) {
        this.stringParams = stringParams;
    }

    public Map<String, String> getParams() {
        if (stringParams == null)
            return params;
        else if (params == null) {
            params = new HashMap<>();
            params.put("JSON_DATA", stringParams);
        } else {
            params.put("JSON_DATA", stringParams);
        }
        return params;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public enum Method {
        GET, POST
    }
}
