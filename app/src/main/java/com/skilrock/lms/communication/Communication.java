package com.skilrock.lms.communication;

import android.content.Context;

import com.skilrock.bean.FortuneBean;
import com.skilrock.bean.KenoBean;
import com.skilrock.config.Config;
import com.skilrock.config.DummyJson;
import com.skilrock.config.VariableStorage;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.DataSource;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLException;

public class Communication {
    public static Context context;
    public static String GCM_PROJECT_ID = "30501279555";
    public static String GCM_PREF = "gcm_pre";
    public static String GCM_PREF_KEY = "gcm_pre_key";

    public static List<String> loginBannerData = new ArrayList<String>();
    public static List<String> nonloginBannerData = new ArrayList<String>();
    public static String OTPRequest = "No";

    // public static String appName = "Android.apk";
    // public static double availableVersion = 0.0;

    // public static String Config.baseURL = "http://192.168.124.77:8080/PMS/";
    // private static String Config.updateURL =
    // "http://192.168.124.77:8080/DownloadApp/android/phone/";
    // public static String faqURL =
    // "http://192.168.124.77:8080/PMS/com/skilrock/pms/mobile/playerInfo/Action/getFAQData.action";

    public static String appName = "Android.apk";

    public static String getbaseURL() {
        return Config.baseURL;
    }

    public static String getUpdateURL() {
        return Config.updateURL;
    }

    public static String getAvailableVersion(Context context) {
        return VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.APP_VERSION);
    }

    public static String getAppName() {
        return appName;
    }

    public static HashMap<String, String> login(String username,
                                                String password, String bindingId, String deviceName,
                                                String deviceType, double version, String gcmId,
                                                String deviceOSVer, String deviceRes, String deviceManufacturerName) {
        JSONObject json = null;
        HashMap<String, String> data = null;

        String path = "com/skilrock/pms/mobile/loginMgmt/Action/playerLogin.action?";
        String params = "userName=" + username + "&password=" + password
                + "&bindingId=" + bindingId + "&deviceName=" + deviceName
                + "&deviceType=" + deviceType + "&currentVersion=" + version
                + "&lang=" + DataSource.selectedLang + "&gcmId=" + gcmId
                + "&deviceRes=" + deviceRes + "&deviceOSVer=" + deviceOSVer
                + "&deviceModel=" + deviceManufacturerName;

        try {
            json = getJSONData(Config.baseURL + path + params);

            // json = new JSONObject(
            // "{\"currentTime\":\"2014-09-29 01:40:07\",\"currenctSymbol\":\"USD\",\"imagePath\":[\"/images/banner/post_login/banner1.png\",\"/images/banner/post_login/banner2.png\",\"/images/banner/post_login/banner3.png\",\"/images/banner/post_login/banner4.png\"],\"bindingId\":\"NOT_REQUIRED\",\"currentBalance\":\"1465.00\",\"dateFormat\":\"dd-mm-yyyy\",\"isSuccess\":true,\"companyName\":\"Africa Lotto\",\"sample\":\"N\"}");
            if (json != null) {
                data = new HashMap<String, String>();

                if (json.getBoolean("isSuccess")) {
                    data.put("isSuccess", "true");
                    data.put("currentTime", json.getString("currentTime"));
                    // data.put("bannerUrl", json.getString("bannerUrl"));
                    data.put("currenctSymbol", json.getString("currenctSymbol"));
                    data.put("currentBalance", json.getString("currentBalance"));
                    if (json.has("userName")) {
                        data.put("userName", json.getString("userName"));
                    } else {
                        data.put("userName", username);
                    }

                    if (json.has("merchantCode")) {
                        data.put("merchantCode", json.getString("merchantCode"));
                    } else {
                        data.put("merchantCode", "");
                    }

                    if (json.has("sessionId")) {
                        data.put("sessionId", json.getString("sessionId"));
                    } else {
                        data.put("merchantCode", "");
                    }
                    data.put("dateFormat", json.getString("dateFormat"));

                    data.put("companyName", json.getString("companyName"));
                    data.put("sample", json.getString("sample"));
                    // data.put("drawName", json.getString("drawName"));
                    data.put("bindingId", json.getString("bindingId"));

                    if (json.has("imagePath")) {

                        JSONArray imgePath = json.getJSONArray("imagePath");
                        try {

                            if (nonloginBannerData.size() > 0) {
                                nonloginBannerData.clear();
                            }
                        } catch (Exception e) {
                        }
                        for (int i = 0; i < imgePath.length(); i++) {
                            nonloginBannerData.add(Config.baseURL
                                    + imgePath.getString(i));

                        }

                    } else {
                        try {
                            if (nonloginBannerData.size() > 0) {
                                nonloginBannerData.clear();
                            }
                        } catch (Exception e) {
                        }
                        nonloginBannerData.add(Config.baseURL);
                    }

                } else {
                    data.put("isSuccess", "false");
                    data.put("errorCode", json.getString("errorCode"));
                    data.put("errorMsg", json.getString("errorMsg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return data;
    }

    public static JSONObject logout(String username) {
        JSONObject json = null;

        String path = "com/skilrock/pms/mobile/loginMgmt/Action/playerLogout.action?";
        String params = "userName=" + username;

        try {
            json = getJSONData(Config.baseURL + path + params);

            if (json != null) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject sentOTPVerificationCode(String verificationCode) {
        JSONObject data = null;
        String path = "com/skilrock/pms/mobile/loginMgmt/Action/checkOtpCode.action?";
        String params = "verificationCode=" + verificationCode;
        try {
            // String HARDCODE_RESPONSE =
            // "{\"message\":\"You have successfully registered\",\"isSuccess\":true}";
            // JSONObject json = new JSONObject(HARDCODE_RESPONSE);

            JSONObject json = getJSONData(Config.baseURL + path + params);
            if (json != null)
                data = json;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static JSONObject newdrawGameData(String username) {
        JSONObject data = null;
        String path = "com/skilrock/pms/mobile/drawGames/drawMgmt/Action/fetchLoginDrawData.action?";
        String params = "userName=" + username;

        String HARDCODE_RESPONSE = "{\"drawgames\":{\"title\":\"Draw Games\",\"game\":[{\"id\":\"dg001\",\"title\":\"5by90\",\"banner\":{\"imageurl\":\"http://img4.wikia.nocookie.net/__cb20120908005246/metalgear/images/9/9e/Banner-metal-gear-social-ops.jpg\",\"action\":\"http://google.com\"},\"winningnumber\":\"5,27,76,1,3\",\"draws\":{\"draw\":[{\"id\":\"dg001_d001\",\"title\":\"5by90_draw1\",\"drawtime\":\"21-12-2014 19:00:00\",\"freezetime\":\"150\"},{\"id\":\"dg001_d002\",\"title\":\"5by90_draw2\",\"drawtime\":\"31-12-2014 19:00:00\",\"freezetime\":\"300\"}]},\"bets\":{\"bet\":[{\"id\":\"dg001_b001\",\"title\":\"5by90_bet1\",\"minnumber\":\"2\",\"maxnumber\":\"15\",\"unitprice\":\"0.1\",\"prefunitprice\":\"0.4\",\"maxbetamount\":\"1\",\"lastplayednumber\":\"5,27,76,1,3\",\"favoritenumber\":\"5,27\"},{\"id\":\"dg001_b002\",\"title\":\"5by90_bet2\",\"minnumber\":\"5\",\"maxnumber\":\"15\",\"unitprice\":\"1\",\"prefunitprice\":\"2\",\"maxbetamount\":\"5\",\"lastplayednumber\":\"15,7,6,41,3\",\"favoritenumber\":\"5,27,7,6,41,3,22,12,1,10,11,34,35,36,37\"}]}},{\"id\":\"dg002\",\"title\":\"6by36\",\"banner\":{\"imageurl\":\"http://milweb1.com/cart/magento/media/catalog/product/cache/1/image/5e06319eda06f020e43594a9c230972d/f/i/file_1_3.jpg\",\"action\":\"590_Draw1_Gameplay\"},\"winningnumber\":\"6,33\",\"draws\":{\"draw\":[{\"id\":\"dg002_d001\",\"title\":\"6by36_draw1\",\"drawtime\":\"31-12-2014 19:00:00\",\"freezetime\":\"300\"},{\"id\":\"dg002_d002\",\"title\":\"6by36_draw2\",\"drawtime\":\"21-12-2014 19:00:00\",\"freezetime\":\"150\"}]},\"bets\":{\"bet\":[{\"id\":\"dg002_b001\",\"title\":\"6by36_bet1\",\"minnumber\":\"2\",\"maxnumber\":\"10\",\"unitprice\":\"0.1\",\"prefunitprice\":\"0.4\",\"maxbetamount\":\"0.5\",\"lastplayednumber\":\"51,2,76,1,3\",\"favoritenumber\":\"1,3,22,12,14\"},{\"id\":\"dg002_b002\",\"title\":\"6by36_bet2\",\"minnumber\":\"3\",\"maxnumber\":\"10\",\"unitprice\":\"2\",\"prefunitprice\":\"20\",\"maxbetamount\":\"20\",\"lastplayednumber\":\"15,27,44,34,76,1,3\",\"favoritenumber\":\"5,27,7,6,41,3,22,12,1,10\"}]}}]}}";

        try {

            JSONObject json = new JSONObject(HARDCODE_RESPONSE);
            // JSONObject json = getJSONData(Config.baseURL + path + params);

            if (json != null)
                data = json;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static JSONObject drawGameData(String username) {
        JSONObject data = null;
        String path = "com/skilrock/pms/mobile/drawGames/drawMgmt/Action/fetchLoginDrawData.action?";
        String params = "userName=" + username;

        // String HARDCODE_RESPONSE =
        // "{\"errorMessage\":\"\",\"gameData\":[{\"betTypeData\":[{\"maxBetAmtMul\":500,\"unitPrice\":0.1,\"betDisplayName\":\"Direct1\",\"betCode\":1},{\"maxBetAmtMul\":500,\"unitPrice\":0.1,\"betDisplayName\":\"Direct2\",\"betCode\":2},{\"maxBetAmtMul\":500,\"unitPrice\":0.1,\"betDisplayName\":\"Direct3\",\"betCode\":3},{\"maxBetAmtMul\":500,\"unitPrice\":0.1,\"betDisplayName\":\"Direct4\",\"betCode\":4},{\"maxBetAmtMul\":500,\"unitPrice\":0.1,\"betDisplayName\":\"Direct5\",\"betCode\":5},{\"maxBetAmtMul\":500,\"unitPrice\":0.1,\"betDisplayName\":\"Perm2\",\"betCode\":6},{\"maxBetAmtMul\":500,\"unitPrice\":0.1,\"betDisplayName\":\"Perm3\",\"betCode\":7},{\"maxBetAmtMul\":500,\"unitPrice\":0.1,\"betDisplayName\":\"Banker1AgainstAll\",\"betCode\":9}],\"advanceDraw\":[{\"drawId\":554,\"drawName\":null,\"drawDateTime\":\"2014-09-24 18:00:00\"},{\"drawId\":555,\"drawName\":null,\"drawDateTime\":\"2014-09-25 18:00:00\"},{\"drawId\":556,\"drawName\":null,\"drawDateTime\":\"2014-09-26 18:00:00\"},{\"drawId\":557,\"drawName\":null,\"drawDateTime\":\"2014-09-27 18:00:00\"},{\"drawId\":558,\"drawName\":null,\"drawDateTime\":\"2014-09-29 18:00:00\"}],\"gameId\":2,\"gameDispName\":\"5/90\",\"ticketExpiry\":15,\"gameDevName\":\"Keno\",\"currentDraw\":[{\"drawId\":553,\"drawName\":null,\"drawFreezeTime\":1800,\"drawDateTime\":\"2014-09-23 18:00:00\"}],\"jackpotLimit\":0}],\"errorCode\":0,\"isSuccess\":true}";

        try {

            // JSONObject json = new JSONObject(HARDCODE_RESPONSE);
            JSONObject json = getJSONData(Config.baseURL + path + params);

            if (json != null)
                data = json;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static JSONObject activeGameData(String username) {
        JSONObject data = null;

        String path = "com/skilrock/pms/mobile/home/reportsMgmt/Action/getActiveGames.action?";
        String params = "userName=" + username;

        try {
            JSONObject json = getJSONData(Config.baseURL + path + params);
            if (json != null)
                data = json;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static JSONObject kenoBuy(KenoBean kenoBean, String username,
                                     String gameDevName) {
        JSONObject json = null;
        JSONObject data = null;
        JSONObject commonSaleDta = null;
        InputStream is = null;
        String path = null;
        if (gameDevName.equals("Keno")) {
            path = "com/skilrock/pms/mobile/drawGames/playMgmt/Action/kenoGhanaBuyAction.action?";
        }
        // if (gameDevName.equals("ZimLottoBonusTwo")) {
        // path =
        // "com/skilrock/pms/mobile/drawGames/playMgmt/Action/zimLottoBonusTwoBuyAction.action?";
        // }
        // if (gameDevName.equals("Zerotonine")) {
        // path =
        // "com/skilrock/pms/mobile/drawGames/playMgmt/Action/zeroToNineBuyAction.action?";
        // }
        try {
            commonSaleDta = new JSONObject();
            json = new JSONObject();
            json.put("totalPurchaseAmt", AmountFormat
                    .getCurrentAmountFormatForMobile(kenoBean
                            .getTotalPurchaseAmt()));
            json.put("noOfPanel", kenoBean.getNoOfPanel());

            JSONArray panelData = new JSONArray();
            for (int i = 0; i < kenoBean.getPanelData().size(); i++) {
                JSONObject panelElement = new JSONObject();
                panelElement.put("isQp", kenoBean.getPanelData().get(i).isQp());
                panelElement.put("noPicked", kenoBean.getPanelData().get(i)
                        .getNoPicked());
                panelElement.put("betName", kenoBean.getPanelData().get(i)
                        .getPlayType());
                panelElement.put("betAmtMul", kenoBean.getPanelData().get(i)
                        .getBetAmtMul());
                panelElement.put("pickedNumbers", kenoBean.getPanelData()
                        .get(i).getPickedNumbers());
                panelData.put(panelElement);
            }
            json.put("userName", username);
            json.put("betTypeData", panelData);

            JSONArray array = new JSONArray();
            JSONObject object = new JSONObject();
            object.put("drawId", DataSource.Keno.drawId);
            array.put(0, object);

            commonSaleDta.put("noOfDraws", kenoBean.getNoOfDraws());
            commonSaleDta.put("isAdvancePlay", kenoBean.getAdvancePlay());
            commonSaleDta.put("drawData", array);

            json.put("commonSaleData", commonSaleDta);
            if (gameDevName.equals("Keno")) {
                is = postData(Config.baseURL + path, "json", json);
            }
            // if (gameDevName.equals("ZimLottoBonusTwo")) {
            // is = postData(Config.baseURL + path, "requestData", json);
            // }
            // if (gameDevName.equals("Zerotonine")) {
            // is = postData(Config.baseURL + path, "requestData", json);
            // }
            Utils.logPrint("req-" + json + "");
            if (is != null) {
                json = null;
                json = JSONParser.parse(is);
                Utils.logPrint("resp-" + json + "");
                data = new JSONObject();
                data = json;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public static JSONObject regenerateOTP(String mobileNumber) {
        JSONObject data = null;
        String path = "com/skilrock/pms/mobile/loginMgmt/Action/regenerateOtp.action?";
        String params = "mobileNumber=" + mobileNumber;
        try {
            // String HARDCODE_RESPONSE =
            // "{\"message\":\"OTP code send successfully\",\"isSuccess\":true}";
            // JSONObject json = new JSONObject(HARDCODE_RESPONSE);

            JSONObject json = getJSONData(Config.baseURL + path + params);
            if (json != null)
                data = json;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static JSONObject fortuneBuy(FortuneBean fortuneBean, String username) {
        JSONObject json = null;
        JSONObject data = null;
        InputStream is = null;
        String path = "com/skilrock/pms/mobile/drawGames/playMgmt/Action/fortuneBuyAction.action?";

        try {
            json = new JSONObject();
            // Create the JSON request here
            json.put("totalPurchaseAmt", fortuneBean.getTotalPurchaseAmt());
            json.put("noOfPanel", fortuneBean.getPanelData().size());
            json.put("isQp", fortuneBean.isQp());
            json.put("noPicked", fortuneBean.getPanelData().size());
            json.put("playType", "Fortune");

            JSONArray panelData = new JSONArray();
            for (int i = 0; i < fortuneBean.getPanelData().size(); i++) {
                JSONObject panelElement = new JSONObject();
                panelElement.put("betAmtMul", fortuneBean.getPanelData().get(i)
                        .getBetAmtMul());
                panelElement.put("pickedNumbers", fortuneBean.getPanelData()
                        .get(i).getPickedNumbers());
                panelData.put(panelElement);
            }
            json.put("panelData", panelData);

            json.put("totalBetAmtMul", fortuneBean.getTotalBetAmtMul());
            json.put("noOfDraws", "1");
            json.put("isAdvancePlay", fortuneBean.getAdvancePlay());
            json.put("userName", username);

            is = postData(Config.baseURL + path, "json", json);

            if (is != null) {
                json = null;
                json = JSONParser.parse(is);
                data = new JSONObject();
                data = json;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static JSONObject checkAvailability(String username) {
        JSONObject data = null;
        String path = "com/skilrock/pms/mobile/loginMgmt/Action/checkUserName.action?";
        String params = "userName=" + username;
        try {
            JSONObject json = getJSONData(Config.baseURL + path + params);

            if (json != null)
                data = json;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static JSONObject register(JSONObject registerdata) {
        JSONObject json = null;
        InputStream is = null;
        String path = "com/skilrock/pms/mobile/loginMgmt/Action/playerRegistrationAction.action?";

        is = postData(Config.baseURL + path, "registrationData", registerdata);

        try {
            if (is != null) {
                json = null;
                json = JSONParser.parse(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject changePassword(String oldPassword,
                                            String newPassword, String userName) {
        JSONObject json = null;
        InputStream is = null;
        JSONObject data = null;
        String path = "com/skilrock/pms/mobile/loginMgmt/Action/changePlayerPassword.action?";

        try {
            data = new JSONObject();
            data.put("oldPassword", oldPassword);
            data.put("newPassword", newPassword);
            data.put("userName", userName);

            is = postData(Config.baseURL + path, "changePasswordData", data);

            if (is != null) {
                json = null;
                json = JSONParser.parse(is);

                // if(json.getBoolean("isSuccess"))
                // {
                // data = new JSONObject();
                // data = json;
                // }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject oldTransactions(String username) {
        JSONObject data = null;
        String path = "com/skilrock/pms/mobile/home/reportsMgmt/Action/fetchLastTxnReport.action?";
        String params = "userName=" + username;
        try {
            JSONObject json = getJSONData(Config.baseURL + path + params);

            if (json != null)
                data = json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static JSONObject getWinningResult(String username, int gameId) {
        JSONObject data = null;
        String path = "com/skilrock/pms/mobile/home/reportsMgmt/Action/winningResultReport.action?";
        String params = "gameId=" + gameId + "&userName=" + username;
        try {
            JSONObject json = getJSONData(Config.baseURL + path + params);

            if (json != null)
                data = json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static JSONObject purchasedTickets(String username) {
        JSONObject data = null;
        String path = "com/skilrock/pms/mobile/home/reportsMgmt/Action/fetchPurchaseTicketReport.action?";
        String params = "userName=" + username;
        try {
            JSONObject json = getJSONData(Config.baseURL + path + params);

            if (json != null)
                data = json;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static JSONObject getTicketData(String username, String ticketNo) {
        JSONObject data = null;
        String path = "com/skilrock/pms/mobile/drawGames/reportsMgmt/Action/fetchTicketDataReport.action?";
        String params = "userName=" + username + "&ticketNum=" + ticketNo;

        try {
            JSONObject json = getJSONData(Config.baseURL + path + params);
            // JSONObject json = new
            // JSONObject("{\"ticketNo\":\"100000120811424\",\"drawData\":[\"2013-03-22 19:51:00*Gaurav\"],\"purchaseAmt\":40,\"isSuccess\":true,\"panelData\":[{\"qp\":false,\"noPicked\":\"2\",\"betAmtMul\":0,\"pickedNumbers\":\"25,44\",\"unitPrice\":0,\"playType\":\"Direct2\",\"noOfLines\":1},{\"qp\":false,\"noPicked\":\"2\",\"betAmtMul\":0,\"pickedNumbers\":\"13,35,45,53\",\"unitPrice\":0,\"playType\":\"Perm2\",\"noOfLines\":6}],\"gameName\":\"5/90\",\"purchaseTime\":\"22/03/2013 19:42:53\"}");

            if (json != null)
                data = json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static JSONObject getRegTicketData(String username) {
        JSONObject data = null;
        String path = "com/skilrock/pms/mobile/home/reportsMgmt/Action/fetchPurchaseTicketReport.action?";
        String params = "userName=" + username + "&reportType=SECOND_CHANCE";

        try {
            JSONObject json = getJSONData(Config.baseURL + path + params);
            // JSONObject json = new
            // JSONObject("{\"ticketNo\":\"100000120811424\",\"drawData\":[\"2013-03-22 19:51:00*Gaurav\"],\"purchaseAmt\":40,\"isSuccess\":true,\"panelData\":[{\"qp\":false,\"noPicked\":\"2\",\"betAmtMul\":0,\"pickedNumbers\":\"25,44\",\"unitPrice\":0,\"playType\":\"Direct2\",\"noOfLines\":1},{\"qp\":false,\"noPicked\":\"2\",\"betAmtMul\":0,\"pickedNumbers\":\"13,35,45,53\",\"unitPrice\":0,\"playType\":\"Perm2\",\"noOfLines\":6}],\"gameName\":\"5/90\",\"purchaseTime\":\"22/03/2013 19:42:53\"}");

            if (json != null)
                data = json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static JSONObject getVersionControl(double currentVersion,
                                               String deviceName, String deviceType) {
        JSONObject json = null;
        boolean isSSL = false;
        if (Config.isStatic && GlobalVariables.loadDummyData) {
            json = JSONParser.parse(DummyJson.getInstance(context).getDummyMap().get("Communication_getVersionControl"));
        } else {
            HttpClient httpClient = null;
            HttpGet httpGet = null;
            HttpEntity httpEntity = null;
            String path = "/com/skilrock/pms/mobile/playerInfo/Action/versionControl.action?";
            String params = "deviceName=" + deviceName + "&deviceType="
                    + deviceType + "&currentVersion=" + currentVersion;
            while (true) {
                try {
                    URL url = new URL(Config.getInstance().getBaseURL() + path + params);
                    Utils.consolePrint(url.toURI() + "");
                    if (isSSL)
                        httpClient = getNewHttpClient();
                    else
                        httpClient = new DefaultHttpClient();
                    httpGet = new HttpGet(url.toURI());
                    httpGet.setHeader("appVersion", currentVersion + "");
                    httpEntity = httpClient.execute(httpGet).getEntity();
                    if (httpEntity != null) {
                        InputStream inputStream = httpEntity.getContent();
                        json = JSONParser.parse(inputStream);
                        if (Config.isStatic) {
                            HashMap<String, String> dummyMap = DummyJson.getInstance(context).getDummyMap();
                            dummyMap.put("Communication_getVersionControl", json.toString());
                            DummyJson.getInstance(context).setDummyMap(dummyMap);
                        }
                    }
                    Utils.logPrint(json.toString());
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
        return json;
    }

    public static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", TrustAllSSLSocketFactory.getSocketFactory(), 443));
//            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            e.printStackTrace();
            return new DefaultHttpClient();
        }
    }

    public static JSONObject withdrawal(String amount, String withdrawChannel,
                                        String userName) {
        JSONObject json = null;
        JSONObject data = null;
        InputStream is = null;
        String path = "com/skilrock/pms/mobile/accMgmt/Action/playerWithdrawlAction.action?";
        try {
            data = new JSONObject();
            data.put("playerName", userName);
            data.put("withdrawlAmount", amount);
            data.put("withdrawlChannel", withdrawChannel);

            is = postData(Config.baseURL + path, "withdrawlData", data);

            if (is != null) {
                json = null;
                json = JSONParser.parse(is);

                // if(json.getBoolean("isSuccess"))
                // {
                // data = new JSONObject();
                // data = json;
                // }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static HashMap<String, String> myAccounts(String userName) {
        HashMap<String, String> hashMap = null;
        String path = "com/skilrock/pms/mobile/playerInfo/Action/myAccountInfo.action?";
        String params = "userName=" + userName;
        try {
            JSONObject json = getJSONData(Config.baseURL + path + params);
            if (json != null) {
                if (json.getBoolean("isSuccess")) {
                    hashMap = new HashMap<String, String>();
                    hashMap.put("isSuccess", "true");
                    JSONObject personalInfo = json
                            .getJSONObject("personalInfo");
                    hashMap.put("firstName",
                            personalInfo.getString("firstName"));
                    hashMap.put("lastName", personalInfo.getString("lastName"));
                    hashMap.put("emailId", personalInfo.getString("emailId"));
                    hashMap.put("mobileNum",
                            personalInfo.getString("mobileNum"));

                    JSONObject walletInfo = json.getJSONObject("walletInfo");
                    hashMap.put("bonusBal", walletInfo.getString("bonusBal"));
                    hashMap.put("depositBal",
                            walletInfo.getString("depositBal"));
                    hashMap.put("withdrawlBal",
                            walletInfo.getString("withdrawlBal"));
                    hashMap.put("winningBal",
                            walletInfo.getString("winningBal"));
                    hashMap.put("availableBal",
                            walletInfo.getString("availableBal"));
                } else {
                    hashMap = new HashMap<String, String>();
                    hashMap.put("isSuccess", "false");
                    hashMap.put("errorCode", json.getString("errorCode"));
                    hashMap.put("errorMsg", json.getString("errorMsg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return hashMap;
        }
        return hashMap;
    }

    public static String getTermsAndCondition() {
        // return
        // "http://192.168.124.119:8080/PlayerMgmt/com/skilrock/pms/mobile/playerInfo/Action/termsAndConditions.action";
        // String path =
        // "com/skilrock/pms/mobile/playerInfo/Action/termsAndConditions.action?userName="
        // + DataSource.Login.username;
        String path = "com/skilrock/pms/mobile/playerInfo/Action/termsAndConditions.action?deviceType=android";
        return Config.baseURL + path;
    }

    public static JSONObject verification(String userName, String deviceName,
                                          String deviceType) {
        JSONObject data = null;
        String path = "com/skilrock/pms/mobile/playerInfo/Action/deviceBindingVerificationCode.action?";
        String params = "userName=" + userName + "&deviceName=" + deviceName
                + "&deviceType=" + deviceType;

        try {
            JSONObject json = getJSONData(Config.baseURL + path + params);
            if (json != null)
                data = json;
            Utils.logPrint("data-" + data.toString());
            Utils.consolePrint(data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public static HashMap<String, String> codeCheck(String userName,
                                                    String deviceName, String deviceType, String verificationCode) {
        HashMap<String, String> data = null;
        String path = "com/skilrock/pms/mobile/playerInfo/Action/deviceBindingUpdate.action?";
        String params = "userName=" + userName + "&deviceName=" + deviceName
                + "&deviceType=" + deviceType + "&verificationCode="
                + verificationCode;
        try {
            JSONObject json = getJSONData(Config.baseURL + path + params);
            if (json != null) {
                if (json.getBoolean("isSuccess")) {
                    data = new HashMap<String, String>();
                    data.put("isSuccess", json.getString("isSuccess"));
                    data.put("bindingId", json.getString("bindingId"));
                } else {
                    // {"errorCode":132,"errorMsg":"Verification Code Expired","isSuccess":false}
                    data = new HashMap<String, String>();
                    data.put("isSuccess", json.getString("isSuccess"));
                    data.put("errorMsg", json.getString("errorMsg"));
                    data.put("errorCode", json.getString("errorCode"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    private static InputStream postData(String strURL, String name,
                                        JSONObject json) {
        InputStream instream = null;
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(strURL);
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair(name, json.toString()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                instream = entity.getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instream;
    }

    public static JSONObject getJSONData(String strURL) {
        JSONObject data = null;
        HttpClient httpClient = null;
        HttpGet httpGet = null;
        HttpEntity httpEntity = null;
        boolean isSSL = false;
        while (true) {
            try {
                Utils.consolePrint(strURL);
                if (isSSL)
                    httpClient = getNewHttpClient();
                else
                    httpClient = new DefaultHttpClient();
                httpGet = new HttpGet(strURL);
                httpEntity = httpClient.execute(httpGet).getEntity();
                if (httpEntity != null) {
                    InputStream inputStream = httpEntity.getContent();
                    data = JSONParser.parse(inputStream);
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
        return data;
    }


    public static JSONObject bankWithdrawal(String amount, String userName,
                                            String bankId, String accntNo) {
        JSONObject json = null;
        JSONObject data = null;
        InputStream is = null;
        String path = "com/skilrock/pms/mobile/accMgmt/Action/bankDeposit.action?";
        try {
            data = new JSONObject();
            data.put("playerName", userName);
            data.put("amount", amount);
            data.put("bankId", bankId);
            data.put("accountNo", accntNo);
            is = postData(Config.baseURL + path, "requestData", data);
            if (is != null) {
                json = null;
                json = JSONParser.parse(is);

                // if(json.getBoolean("isSuccess"))
                // {
                // data = new JSONObject();
                // data = json;
                // }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    public static JSONObject forgotPassword(String phonenumber) {
        JSONObject json = null;
        String path = "com/skilrock/pms/mobile/loginMgmt/Action/forgotPlayerPassword.action?";
        String params = "mobileNo=" + phonenumber;
        try {
            json = getJSONData(Config.baseURL + path + params);
            Utils.consolePrint(json.toString());
            if (json != null) {

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONObject getCityList(String stateCode) {
        JSONObject data = null;
        String path = "/com/skilrock/pms/mobile/playerInfo/Action/getCityList.action?";
        String params = "stateCode=" + stateCode;
        try {
            // JSONObject json = getJSONData(Config.baseURL + path);
            JSONObject json = getJSONData(Config.getInstance().getBaseURL() + path + params);
            Utils.logPrint("regData-" + Config.getInstance().getBaseURL() + path + params);

            if (json != null)
                data = json;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    public static JSONObject getStateList() {
        JSONObject data = null;
        String path = "/com/skilrock/pms/mobile/playerInfo/Action/getStateList.action";
        try {
            // JSONObject json = getJSONData(Config.baseURL + path);
            JSONObject json = getJSONData(Config.getInstance().getBaseURL() + path);
            if (json != null)
                data = json;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    public static JSONObject sportsLotteyBuy(JSONObject object) {

        JSONObject data = null;
        String path = "/com/skilrock/sle/mobile/playMgmt/Action/sportsLotteryPurchaseTicket.action?";
        try {
            data = object;
            // data = postDataSLE(baseURLSLE + path, "requestData", data);

//            data = new JSONObject(
//                    "{\"errorMsg\":\"\",\"tktData\":{\"brCd\":\"98113130000001020\",\"balance\":\"1773.50\",\"gameTypeId\":1,\"ticketNo\":9811313000000102,\"purchaseDate\":\"2014-11-09\",\"gameId\":1,\"gameTypeName\":\"Soccer 8\",\"boardData\":[{\"drawId\":7,\"drawTime\":\"20:00:00\",\"drawName\":\"Tuesday Lucky\",\"eventType\":\"[-1, D, +1]\",\"unitPrice\":1,\"noOfLines\":4,\"eventData\":[{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHome\":\"Mumbai Indians\",\"eventDate\":\"2014-02-09 09:00:00.0\",\"eventDisplayAway\":\"Kolkata Knight Riders\",\"selection\":\"H\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHome\":\"Kochi Tuskers Kerala\",\"eventDate\":\"2014-02-09 09:00:00.0\",\"eventDisplayAway\":\"Kings XI Punjab\",\"selection\":\"H\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHome\":\"Delhi Daredevils\",\"eventDate\":\"2014-02-09 14:00:00.0\",\"eventDisplayAway\":\"Deccan Chargers\",\"selection\":\"H\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHome\":\"Chennai SUPER Kings\",\"eventDate\":\"2014-02-09 14:00:00.0\",\"eventDisplayAway\":\"Royal Challengers Bangalore\",\"selection\":\"H\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHome\":\"Athlectic Bilbao\",\"eventDate\":\"2014-02-09 05:00:00.0\",\"eventDisplayAway\":\"Genk\",\"selection\":\"H\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHome\":\"Celta Vigo\",\"eventDate\":\"2014-02-09 06:00:00.0\",\"eventDisplayAway\":\"Evian Thonon Gaillard\",\"selection\":\"H\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHome\":\"Betis\",\"eventDate\":\"2014-02-09 09:00:00.0\",\"eventDisplayAway\":\"Evian Thonon Gaillard\",\"selection\":\"H,A\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHome\":\"Evian Thonon Gaillard\",\"eventDate\":\"2014-02-09 09:00:00.0\",\"eventDisplayAway\":\"Westerlo\",\"selection\":\"H,A\"}],\"boardPrice\":4,\"drawDate\":\"2014-11-14\"}],\"trxId\":\"32\",\"ticketAmt\":\"4.00\",\"gameName\":\"Soccer\",\"purchaseTime\":\"20:28:59\"},\"isSuccess\":true}");

//            data = new JSONObject("{\"errorMsg\":\"\",\"tktData\":{\"brCd\":\"98113130000001020\",\"balance\":\"1773.50\",\"gameTypeId\":1,\"ticketNo\":9811313000000102,\"purchaseDate\":\"2014-11-09\",\"gameId\":1,\"gameTypeName\":\"Soccer 8\",\"boardData\":[{\"drawId\":7,\"drawTime\":\"20:00:00\",\"drawName\":\"Tuesday Lucky\",\"eventType\":\"[-1, D, +1]\",\"unitPrice\":1,\"noOfLines\":4,\"eventData\":[{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHomeShort\":\"MI\",\"eventDisplayHome\":\"Mumbai Indians\",\"eventDate\":\"2014-02-09 09:00:00.0\",\"eventDisplayAwayShort\":\"KKR\",\"eventDisplayAway\":\"Kolkata Knight Riders\",\"selection\":\"-1\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHomeShort\":\"KTK\",\"eventDisplayHome\":\"Kochi Tuskers Kerala\",\"eventDate\":\"2014-02-09 09:00:00.0\",\"eventDisplayAwayShort\":\"KXIP\",\"eventDisplayAway\":\"Kings XI Punjab\",\"selection\":\"+2\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHomeShort\":\"DD\",\"eventDisplayHome\":\"Delhi Daredevils\",\"eventDate\":\"2014-02-09 14:00:00.0\",\"eventDisplayAwayShort\":\"DC\",\"eventDisplayAway\":\"Deccan Chargers\",\"selection\":\"-2\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHomeShort\":\"CSK\",\"eventDisplayHome\":\"Chennai SUPER Kings\",\"eventDate\":\"2014-02-09 14:00:00.0\",\"eventDisplayAwayShort\":\"RCB\",\"eventDisplayAway\":\"Royal Challengers Bangalore\",\"selection\":\"-1\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHomeShort\":\"AB\",\"eventDisplayHome\":\"Athlectic Bilbao\",\"eventDate\":\"2014-02-09 05:00:00.0\",\"eventDisplayAwayShort\":\"GK\",\"eventDisplayAway\":\"Genk\",\"selection\":\"-1\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHomeShort\":\"Celta Vigo\",\"eventDisplayHome\":\"CV\",\"eventDate\":\"2014-02-09 06:00:00.0\",\"eventDisplayAwayShort\":\"ETG\",\"eventDisplayAway\":\"Evian Thonon Gaillard\",\"selection\":\"-2\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHomeShort\":\"BTS\",\"eventDisplayHome\":\"Betis\",\"eventDate\":\"2014-02-09 09:00:00.0\",\"eventDisplayAwayShort\":\"ETG\",\"eventDisplayAway\":\"Evian Thonon Gaillard\",\"selection\":\"-2,D\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHomeShort\":\"ETG\",\"eventDisplayHome\":\"Evian Thonon Gaillard\",\"eventDate\":\"2014-02-09 09:00:00.0\",\"eventDisplayAwayShort\":\"W\",\"eventDisplayAway\":\"Westerlo\",\"selection\":\"D,+1\"}],\"boardPrice\":4,\"drawDate\":\"2014-11-14\"}],\"trxId\":\"32\",\"ticketAmt\":\"4.00\",\"gameName\":\"Soccer\",\"purchaseTime\":\"20:28:59\"},\"isSuccess\":true}");

            data = new JSONObject("{\"errorMsg\":\"\",\"tktData\":{\"brCd\":\"98113130000001020\",\"balance\":\"1773.50\",\"gameTypeId\":1,\"ticketNo\":9811313000000102,\"purchaseDate\":\"2014-11-09\",\"gameId\":1,\"gameTypeName\":\"Soccer 8\",\"boardData\":[{\"drawId\":7,\"drawTime\":\"20:00:00\",\"drawName\":\"Tuesday Lucky\",\"eventType\":\"[-1, D, +1]\",\"unitPrice\":1,\"noOfLines\":4,\"eventData\":[{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHomeShort\":\"MI\",\"eventDisplayHome\":\"Mumbai Indians\",\"eventDate\":\"2014-02-09 09:00:00.0\",\"eventDisplayAwayShort\":\"KKR\",\"eventDisplayAway\":\"Kolkata Knight Riders\",\"selection\":\"H\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHomeShort\":\"KTK\",\"eventDisplayHome\":\"Kochi Tuskers Kerala\",\"eventDate\":\"2014-02-09 09:00:00.0\",\"eventDisplayAwayShort\":\"KXIP\",\"eventDisplayAway\":\"Kings XI Punjab\",\"selection\":\"D\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHomeShort\":\"DD\",\"eventDisplayHome\":\"Delhi Daredevils\",\"eventDate\":\"2014-02-09 14:00:00.0\",\"eventDisplayAwayShort\":\"DC\",\"eventDisplayAway\":\"Deccan Chargers\",\"selection\":\"A\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHomeShort\":\"CSK\",\"eventDisplayHome\":\"Chennai SUPER Kings\",\"eventDate\":\"2014-02-09 14:00:00.0\",\"eventDisplayAwayShort\":\"RCB\",\"eventDisplayAway\":\"Royal Challengers Bangalore\",\"selection\":\"D\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHomeShort\":\"AB\",\"eventDisplayHome\":\"Athlectic Bilbao\",\"eventDate\":\"2014-02-09 05:00:00.0\",\"eventDisplayAwayShort\":\"GK\",\"eventDisplayAway\":\"Genk\",\"selection\":\"H\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHomeShort\":\"Celta Vigo\",\"eventDisplayHome\":\"CV\",\"eventDate\":\"2014-02-09 06:00:00.0\",\"eventDisplayAwayShort\":\"ETG\",\"eventDisplayAway\":\"Evian Thonon Gaillard\",\"selection\":\"D\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHomeShort\":\"BTS\",\"eventDisplayHome\":\"Betis\",\"eventDate\":\"2014-02-09 09:00:00.0\",\"eventDisplayAwayShort\":\"ETG\",\"eventDisplayAway\":\"Evian Thonon Gaillard\",\"selection\":\"A\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHomeShort\":\"ETG\",\"eventDisplayHome\":\"Evian Thonon Gaillard\",\"eventDate\":\"2014-02-09 09:00:00.0\",\"eventDisplayAwayShort\":\"W\",\"eventDisplayAway\":\"Westerlo\",\"selection\":\"H,D,A\"}],\"boardPrice\":4,\"drawDate\":\"2014-11-14\"}],\"trxId\":\"32\",\"ticketAmt\":\"4.00\",\"gameName\":\"Soccer\",\"purchaseTime\":\"20:28:59\"},\"isSuccess\":true}");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    public static String getProfile() {
        return "{\"personalInfo\":{\"lastName\":\"potter\",\"emailId\":\"nalini.singh@skilrock.com\",\"mobileNum\":\"8377030975\",\"firstName\":\"harry\",\"address\":\"harry\",\"state\":\"Greater Accra\",\"city\":\"Accra\",\"dob\":\"1997-01-17\",\"gender\":\"male\",\"profilePhoto\":\"\\r\\nR0lGODlhEAAPAPQAAIyq7zlx3lqK5zFpznOe7/729fvh3/3y8e1lXt1jXO5tZe9zbLxeWfB6c6lbV/GDffKIgvKNh/OYkvSblvSinfWrp3dTUfawq/e1sf3r6v/8/P/9/f///////wAAAAAAACH5BAEAAB0ALAAAAAAQAA8AAAWK4GWJpDWN6KU8nNK+bsIxs3FdVUVRUhQ9wMUCgbhkjshbbkkpKnWSqC84rHA4kmsWu9lICgWHlQO5lsldSMEgrkAaknccQBAE4mKtfkPQaAIZFw4TZmZdAhoHAxkYg25wchABAQMDeIRYHF5gEkcSBo2YEGlgEEcQoI4SDRWrrayrFxCDDrW2t7ghADs=\\r\\n\"},\"walletInfo\":{\"bonusBal\":\"0.00\",\"depositBal\":\"998450.00\",\"withdrawlBal\":\"998560.00\",\"winningBal\":\"110.00\",\"availableBal\":\"1210.00\"},\"isSuccess\":true}";
    }


    public static JSONObject drawGameDataSLE(String merchantCode,
                                             String playerName, String sessionId) {
        JSONObject json = null;
        JSONObject data = null;
        InputStream is = null;
        String path = "com/skilrock/sle/mobile/gameDrawData/playMgmt/Action/fetchSLEDrawData.action?";
        try {
            data = new JSONObject();
            data.put("playerName", playerName);
            data.put("merchantCode", merchantCode);
            data.put("sessionId", sessionId);
//			is = postData(Config.baseURL + path, "requestData", data);
            // if (is != null) {
            // json = null;
            // json = JSONParser.parse(is);

//            json = new JSONObject(
//                    "{\"errorMsg\":\"\",\"isSuccess\":true,\"sportsLotteryData\":{\"gameData\":[{\"maxEventCount\":1,\"gameDisplayName\":\"Soccer\",\"tktMaxAmt\":1000,\"gameId\":1,\"minEventCount\":1,\"gameDevname\":\"SOCCER\",\"tktThresholdAmt\":250,\"gameTypeData\":[{\"gameTypeDevName\":\"soccer8\",\"gameTypeId\":1,\"drawData\":{\"drawId\":5,\"currentData\":[{\"eventData\":[{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHome\":\"Nice\",\"eventDate\":\"20-01-2015 15:45:00\",\"eventId\":59,\"eventDisplayAway\":\"Nigeria\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHome\":\"KV Oostende\",\"eventDate\":\"20-01-2015 15:50:00\",\"eventId\":60,\"eventDisplayAway\":\"Real Betis\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHome\":\"Evian Thonon Gaillard\",\"eventDate\":\"20-01-2015 15:40:00\",\"eventId\":61,\"eventDisplayAway\":\"Levante\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHome\":\"Nice\",\"eventDate\":\"20-01-2015 15:40:00\",\"eventId\":62,\"eventDisplayAway\":\"Levante\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHome\":\"Nigeria\",\"eventDate\":\"20-01-2015 15:45:00\",\"eventId\":63,\"eventDisplayAway\":\"Olympique Marseille\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHome\":\"Nice\",\"eventDate\":\"20-01-2015 15:55:00\",\"eventId\":64,\"eventDisplayAway\":\"Olympique Lyonnais\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHome\":\"Nantes\",\"eventDate\":\"20-01-2015 15:50:00\",\"eventId\":65,\"eventDisplayAway\":\"Espanyol\"},{\"eventLeague\":\"League one\",\"eventVenue\":\"Gurgaon\",\"eventDisplayHome\":\"Nantes\",\"eventDate\":\"20-01-2015 15:55:00\",\"eventId\":66,\"eventDisplayAway\":\"Blackburn Rovers\"}]}],\"drawDisplayString\":\"Tuesday Special\",\"drawDateTime\":\"Tue 20 Jan 2015 04:00 PM\",\"drawNumber\":1,\"ftg\":\"2015-01-20 15:34:08\"},\"eventType\":\"[H, D, A]\",\"gameTypeDisplayName\":\"Soccer 12\",\"gameTypeMaxBetAmtMultiple\":100,\"gameTypeUnitPrice\":1}]}]}}");

            json = new JSONObject("{\"responseCode\":0,\"sleData\":{\"gameData\":[{\"maxEventCount\":1,\"gameDisplayName\":\"Soccer\",\"tktMaxAmt\":999999,\"gameId\":1,\"minEventCount\":1,\"gameDevname\":\"SOCCER\",\"tktThresholdAmt\":100,\"gameTypeData\":[{\"gameTypeDevName\":\"soccer13\",\"gameTypeId\":1,\"drawData\":[{\"drawId\":1,\"drawDisplayString\":\"WEEKEND\",\"drawDateTime\":\"Thu 16 Jul 2015 10:15 AM\",\"drawNumber\":1,\"eventData\":[{\"eventLeague\":\"CAF Champions League\",\"eventVenue\":\"Bayer Leverkusen\",\"eventCodeAway\":\"*BUT\",\"eventDisplayHome\":\"Bayer Leverkusen(women)\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":11,\"eventDisplayAway\":\"Bechem United(women)\",\"eventCodeHome\":\"*BAL\"},{\"eventLeague\":\"Bundesliga\",\"eventVenue\":\"Barcelona\",\"eventCodeAway\":\"AUG\",\"eventDisplayHome\":\"Bayer Leverkusen(women)\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":5,\"eventDisplayAway\":\"Augsburg\",\"eventCodeHome\":\"*BAL\"},{\"eventLeague\":\"CAF Confederations Cup\",\"eventVenue\":\"Barcelona\",\"eventCodeAway\":\"ARG\",\"eventDisplayHome\":\"Barcelona(women)\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":9,\"eventDisplayAway\":\"Argentina\",\"eventCodeHome\":\"*BAR\"},{\"eventLeague\":\"CAF U-20 Championship\",\"eventVenue\":\"Augsburg\",\"eventCodeAway\":\"ATM\",\"eventDisplayHome\":\"Barcelona(women)\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":1,\"eventDisplayAway\":\"Atletico Madrid\",\"eventCodeHome\":\"*BAR\"},{\"eventLeague\":\"CAF U-20 Championship\",\"eventVenue\":\"Atalanta\",\"eventCodeAway\":\"*KOT\",\"eventDisplayHome\":\"Atalanta\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":4,\"eventDisplayAway\":\"Asante Kotoko(women)\",\"eventCodeHome\":\"ATA\"},{\"eventLeague\":\"CAF Confederations Cup\",\"eventVenue\":\"Barcelona\",\"eventCodeAway\":\"BIL\",\"eventDisplayHome\":\"Atalanta\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":7,\"eventDisplayAway\":\"Athletic Bilbao\",\"eventCodeHome\":\"ATA\"},{\"eventLeague\":\"Bundesliga\",\"eventVenue\":\"Barcelona\",\"eventCodeAway\":\"ASG\",\"eventDisplayHome\":\"Atletico Madrid\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":6,\"eventDisplayAway\":\"Ashanti-Gold\",\"eventCodeHome\":\"ATM\"},{\"eventLeague\":\"CAF U-20 Championship\",\"eventVenue\":\"Athletic Bilbao\",\"eventCodeAway\":\"ATA\",\"eventDisplayHome\":\"Augsburg\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":3,\"eventDisplayAway\":\"Atalanta\",\"eventCodeHome\":\"AUG\"},{\"eventLeague\":\"CAF Confederations Cup\",\"eventVenue\":\"Augsburg\",\"eventCodeAway\":\"MUN\",\"eventDisplayHome\":\"Augsburg\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":10,\"eventDisplayAway\":\"Bayern Munich\",\"eventCodeHome\":\"AUG\"},{\"eventLeague\":\"CAF Confederations Cup\",\"eventVenue\":\"Atletico Madrid\",\"eventCodeAway\":\"ATA\",\"eventDisplayHome\":\"Athletic Bilbao\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":8,\"eventDisplayAway\":\"Atalanta\",\"eventCodeHome\":\"BIL\"},{\"eventLeague\":\"English Premiere League\",\"eventVenue\":\"Brazil\",\"eventCodeAway\":\"ASG\",\"eventDisplayHome\":\"Brazil\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":13,\"eventDisplayAway\":\"Ashanti-Gold\",\"eventCodeHome\":\"BRA\"},{\"eventLeague\":\"CAF U-20 Championship\",\"eventVenue\":\"Bayer Leverkusen\",\"eventCodeAway\":\"ATA\",\"eventDisplayHome\":\"Bayern Munich\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":2,\"eventDisplayAway\":\"Atalanta\",\"eventCodeHome\":\"MUN\"},{\"eventLeague\":\"CAF Confederations Cup\",\"eventVenue\":\"Bayern Munich\",\"eventCodeAway\":\"BEN\",\"eventDisplayHome\":\"Bayern Munich\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":12,\"eventDisplayAway\":\"Benefica\",\"eventCodeHome\":\"MUN\"}],\"ftg\":\"2015-07-16 10:05:00\"}],\"eventType\":\"[D, A, H]\",\"gameTypeDisplayName\":\"Soccer Cash 13\",\"gameTypeMaxBetAmtMultiple\":600,\"gameTypeUnitPrice\":1},{\"gameTypeDevName\":\"soccer10\",\"gameTypeId\":2,\"drawData\":[{\"drawId\":9,\"drawDisplayString\":\"WEEKEND\",\"drawDateTime\":\"Thu 16 Jul 2015 10:15 AM\",\"drawNumber\":9,\"eventData\":[{\"eventLeague\":\"Bundesliga\",\"eventVenue\":\"Barcelona\",\"eventCodeAway\":\"AUG\",\"eventDisplayHome\":\"Bayer Leverkusen(women)\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":5,\"eventDisplayAway\":\"Augsburg\",\"eventCodeHome\":\"*BAL\"},{\"eventLeague\":\"CAF Confederations Cup\",\"eventVenue\":\"Barcelona\",\"eventCodeAway\":\"ARG\",\"eventDisplayHome\":\"Barcelona(women)\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":9,\"eventDisplayAway\":\"Argentina\",\"eventCodeHome\":\"*BAR\"},{\"eventLeague\":\"CAF U-20 Championship\",\"eventVenue\":\"Augsburg\",\"eventCodeAway\":\"ATM\",\"eventDisplayHome\":\"Barcelona(women)\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":1,\"eventDisplayAway\":\"Atletico Madrid\",\"eventCodeHome\":\"*BAR\"},{\"eventLeague\":\"CAF U-20 Championship\",\"eventVenue\":\"Atalanta\",\"eventCodeAway\":\"*KOT\",\"eventDisplayHome\":\"Atalanta\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":4,\"eventDisplayAway\":\"Asante Kotoko(women)\",\"eventCodeHome\":\"ATA\"},{\"eventLeague\":\"CAF Confederations Cup\",\"eventVenue\":\"Barcelona\",\"eventCodeAway\":\"BIL\",\"eventDisplayHome\":\"Atalanta\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":7,\"eventDisplayAway\":\"Athletic Bilbao\",\"eventCodeHome\":\"ATA\"},{\"eventLeague\":\"Bundesliga\",\"eventVenue\":\"Barcelona\",\"eventCodeAway\":\"ASG\",\"eventDisplayHome\":\"Atletico Madrid\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":6,\"eventDisplayAway\":\"Ashanti-Gold\",\"eventCodeHome\":\"ATM\"},{\"eventLeague\":\"CAF U-20 Championship\",\"eventVenue\":\"Athletic Bilbao\",\"eventCodeAway\":\"ATA\",\"eventDisplayHome\":\"Augsburg\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":3,\"eventDisplayAway\":\"Atalanta\",\"eventCodeHome\":\"AUG\"},{\"eventLeague\":\"CAF Confederations Cup\",\"eventVenue\":\"Augsburg\",\"eventCodeAway\":\"MUN\",\"eventDisplayHome\":\"Augsburg\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":10,\"eventDisplayAway\":\"Bayern Munich\",\"eventCodeHome\":\"AUG\"},{\"eventLeague\":\"CAF Confederations Cup\",\"eventVenue\":\"Atletico Madrid\",\"eventCodeAway\":\"ATA\",\"eventDisplayHome\":\"Athletic Bilbao\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":8,\"eventDisplayAway\":\"Atalanta\",\"eventCodeHome\":\"BIL\"},{\"eventLeague\":\"CAF U-20 Championship\",\"eventVenue\":\"Bayer Leverkusen\",\"eventCodeAway\":\"ATA\",\"eventDisplayHome\":\"Bayern Munich\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":2,\"eventDisplayAway\":\"Atalanta\",\"eventCodeHome\":\"MUN\"}],\"ftg\":\"2015-07-16 10:05:00\"}],\"eventType\":\"[D, A, H]\",\"gameTypeDisplayName\":\"Soccer Cash 10\",\"gameTypeMaxBetAmtMultiple\":600,\"gameTypeUnitPrice\":1},{\"gameTypeDevName\":\"soccer6\",\"gameTypeId\":3,\"drawData\":[{\"drawId\":17,\"drawDisplayString\":\"WEEKEND\",\"drawDateTime\":\"Thu 16 Jul 2015 10:15 AM\",\"drawNumber\":17,\"eventData\":[{\"eventLeague\":\"Bundesliga\",\"eventVenue\":\"Barcelona\",\"eventCodeAway\":\"AUG\",\"eventDisplayHome\":\"Bayer Leverkusen(women)\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":5,\"eventDisplayAway\":\"Augsburg\",\"eventCodeHome\":\"*BAL\"},{\"eventLeague\":\"CAF U-20 Championship\",\"eventVenue\":\"Augsburg\",\"eventCodeAway\":\"ATM\",\"eventDisplayHome\":\"Barcelona(women)\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":1,\"eventDisplayAway\":\"Atletico Madrid\",\"eventCodeHome\":\"*BAR\"},{\"eventLeague\":\"CAF U-20 Championship\",\"eventVenue\":\"Atalanta\",\"eventCodeAway\":\"*KOT\",\"eventDisplayHome\":\"Atalanta\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":4,\"eventDisplayAway\":\"Asante Kotoko(women)\",\"eventCodeHome\":\"ATA\"},{\"eventLeague\":\"Bundesliga\",\"eventVenue\":\"Barcelona\",\"eventCodeAway\":\"ASG\",\"eventDisplayHome\":\"Atletico Madrid\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":6,\"eventDisplayAway\":\"Ashanti-Gold\",\"eventCodeHome\":\"ATM\"},{\"eventLeague\":\"CAF U-20 Championship\",\"eventVenue\":\"Athletic Bilbao\",\"eventCodeAway\":\"ATA\",\"eventDisplayHome\":\"Augsburg\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":3,\"eventDisplayAway\":\"Atalanta\",\"eventCodeHome\":\"AUG\"},{\"eventLeague\":\"CAF U-20 Championship\",\"eventVenue\":\"Bayer Leverkusen\",\"eventCodeAway\":\"ATA\",\"eventDisplayHome\":\"Bayern Munich\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":2,\"eventDisplayAway\":\"Atalanta\",\"eventCodeHome\":\"MUN\"}],\"ftg\":\"2015-07-16 10:05:00\"}],\"eventType\":\"[D, -2, -1, +1, +2]\",\"gameTypeDisplayName\":\"Soccer Cash 6\",\"gameTypeMaxBetAmtMultiple\":600,\"gameTypeUnitPrice\":0.5},{\"gameTypeDevName\":\"soccer4\",\"gameTypeId\":4,\"drawData\":[{\"drawId\":25,\"drawDisplayString\":\"WEEKEND\",\"drawDateTime\":\"Thu 16 Jul 2015 10:15 AM\",\"drawNumber\":25,\"eventData\":[{\"eventLeague\":\"CAF U-20 Championship\",\"eventVenue\":\"Augsburg\",\"eventCodeAway\":\"ATM\",\"eventDisplayHome\":\"Barcelona(women)\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":1,\"eventDisplayAway\":\"Atletico Madrid\",\"eventCodeHome\":\"*BAR\"},{\"eventLeague\":\"CAF U-20 Championship\",\"eventVenue\":\"Atalanta\",\"eventCodeAway\":\"*KOT\",\"eventDisplayHome\":\"Atalanta\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":4,\"eventDisplayAway\":\"Asante Kotoko(women)\",\"eventCodeHome\":\"ATA\"},{\"eventLeague\":\"CAF U-20 Championship\",\"eventVenue\":\"Athletic Bilbao\",\"eventCodeAway\":\"ATA\",\"eventDisplayHome\":\"Augsburg\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":3,\"eventDisplayAway\":\"Atalanta\",\"eventCodeHome\":\"AUG\"},{\"eventLeague\":\"CAF U-20 Championship\",\"eventVenue\":\"Bayer Leverkusen\",\"eventCodeAway\":\"ATA\",\"eventDisplayHome\":\"Bayern Munich\",\"eventDate\":\"16-07-2015 10:10:00\",\"eventId\":2,\"eventDisplayAway\":\"Atalanta\",\"eventCodeHome\":\"MUN\"}],\"ftg\":\"2015-07-16 10:05:00\"}],\"eventType\":\"[D, -2, -1, +1, +2]\",\"gameTypeDisplayName\":\"Soccer Cash 4\",\"gameTypeMaxBetAmtMultiple\":600,\"gameTypeUnitPrice\":0.5}]}]},\"responseMsg\":\"success\"}");

            // Log.i("resp", json.toString());
            // if (json.getBoolean("isSuccess")) {
            // data = new JSONObject();
            data = json;
            // } else {
            // data = null;
            // }
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
