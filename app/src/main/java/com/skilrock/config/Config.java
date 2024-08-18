package com.skilrock.config;

import android.content.Context;
import android.graphics.Typeface;

import com.skilrock.lms.ui.BuildConfig;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.Utils;

public class Config {

    static Config config;

    public static boolean isWithoutCrashApp = false;
    public static boolean isHTTPS = true;
    public static boolean isDebug = BuildConfig.IS_DEBUG;
    public static boolean isStatic = false;// for static app use
    public final static String dateRelease = "05/08/2016";
    // common for all, please don't change
    public static String[] myAccntOptions = new String[]{"Deposit", "Withdrawal", "My Tickets", "My Transactions", "My Withdrawals", "My Bank Deposit"};


    // for wearer only
    public static boolean isWearer = false;
    public static String baseURLWearer = "http://192.168.132.32";
    public final static String device_type_wearer = "MOBILE";
    public static final String domain_name = "192.168.132.32";
    public static final String appType = "ANDROID_APP_CASH";
    public static final String appTypeVersion = "Cash";
    public static final String loginToken = "khelplayrummy";
    public static final String os = "Android";


    //--------------------------------------------*Zimbabwe Configuration*---------------------------------------------------------//
    //    public static String baseURL = "http://192.168.124.166:8080/PMS"; //QA Server for Local
    //    public static String baseURL = "http://220.225.254.244:8080/PMS"; // QA Server
//        public static String baseURL = "http://41.190.38.202:8081/PMS"; // UAT Server
    //    public static String baseURL = "http://220.225.254.249:8081/PMS"; // Integration Server  username: sumit pwd: 87654321
    //    public static String baseURL = "http://192.168.124.254:8081/PMS"; // Integration
    //    public static String baseURL = "http://41.190.38.202:8080/PMS"; // Production Server
//    public static String baseURL = "http://115.111.246.156:8080/PMS"; // Test server for Demo date :- 18-01-2016
//    public static String baseURL = "http://162.13.131.240/PMS"; // Test server for Demo date :- 25-01-2016

//    public static final String fiveGameName = "KenoTwo", tenByNinety = "KenoSeven", fiveGameNameLagos = "KenoFive", fastGameName = "Zerotonine", oneToTwelve = "OneToTwelve", bonusGameName = "ZimLottoBonusTwo", bonusFree = "ZimLottoBonusTwoFree", twelveGameName = "TwelveByTwentyFour", thaiGameName = "PaperLottoGameScreen";
//    public static String COUNTRY = "Zim";//ZIM = "Zim", GHANA = "Ghana", LAGOS = "Lagos";
//    public static final String CURRENCY_SYMBOL = "$";

    //--------------------------------------------*Ghana Configuration*---------------------------------------------------------//

    //    public static String baseURL = "http://192.168.124.141:8081/PMSGhana";  // integration server
    //    public static String baseURL = "http://192.168.124.99/PMS";  // QA server
    //    public static String baseURL = "http://41.77.66.22/PMS"; // UAT server
//    public static String baseURL = "http://182.156.76.139:8085/PMS"; // UAT server new

//    public static final String fiveGameName = "Keno", tenByNinety = "KenoSeven", fiveGameNameLagos = "KenoFive", fastGameName = "Zerotonine", oneToTwelve = "OneToTwelve", bonusGameName = "ZimLottoBonusTwo", bonusFree = "ZimLottoBonusTwoFree", twelveGameName = "TwelveByTwentyFour", thaiGameName = "PaperLottoGameScreen";
    //    public static final String COUNTRY = "Ghana";//ZIM = "Zim", GHANA = "Ghana", LAGOS = "Lagos"
//    public static final String CURRENCY_SYMBOL = "GH\u20B5";

    //--------------------------------------------*lagos Configuration*---------------------------------------------------------//

    //    public static String baseURL = "http://192.168.124.201:8080/PMS/";
    //    Link for LMS:-   192.168.124.99:80/LMSLagos
    //    Link for PMS:-   192.168.124.99:80/PMSLagos
//    http://192.168.124.201:8180/LMSLagos/
    //    Public Ip      :-    220.225.254.250


    //new

//    public static String baseURL = "http://192.168.124.201:8180/LMSLagos";
//    public static String baseURL = "http://192.168.124.61:8180/PMSLagos";
//    http://192.168.124.201:8180/LMSLagos/

    //    public static String baseURL = "http://192.168.124.141:8081/PMS"; //integration
//    public static String baseURL = "http://192.168.124.61:8180/PMSLagos";//qa server
//    public static String baseURL = "http://220.225.254.250:80/PMSLagos";//new QA gmail
    //    public static String faqURL = "http://192.168.124.201:8080/PMS/com/skilrock/pms/mobile/playerInfo/Action/getFAQData.action";
    //    testplayer  12345678
    //    username:- nimish
    //    password:- 12345678

//    public static final String fiveGameName = "KenoFour", tenByNinety = "KenoSeven", fiveGameNameLagos = "KenoFive", oneToTwelve = "OneToTwelve", fastGameName = "Zerotonine", bonusGameName = "ZimLottoBonusTwo", bonusFree = "ZimLottoBonusTwoFree", twelveGameName = "TwelveByTwentyFour", thaiGameName = "PaperLottoGameScreen";
//    public static final String COUNTRY = "Lagos";//ZIM = "Zim", GHANA = "Ghana", LAGOS = "Lagos"
//    public static final String CURRENCY_SYMBOL = "NGN";

    //--------------------------------------------*End of Configuration*---------------------------------------------------------//

    public static final String IMAGE_DIRECTORY_NAME = "Profile";
    public static String subDir = "ProfileCom";
    public static String updateURL;
    public static String splashWaitTimeInMills;
    public static String orgName;
    public static String apkPath;
    public static int userNameMinLength;
    public static int drawerItemCodes[];
    public static final int LOGIN = 1, MY_ACCT = 2, INBOX = 3, USER_SETTING = 4, APP_SETTING = 5, INVITE = 6, LOCATE = 7, LOGOUT = 8;
    public static String drawerItems[];
    public static final String DG = "DGE", SC = "SC", IGE = "IGE", SL = "SLE", EX = "";
    public static final String ZIM = "ZIM", GHANA = "GHANA", LAGOS = "LAGOS";
    public static String COUNTRY;//= "Ghana";//ZIM = "Zim", GHANA = "Ghana", LAGOS = "Lagos"
    public static String CURRENCY_SYMBOL;

    public static Typeface timerFont;
    public static Typeface globalTextFont;
    public static String reqChannel = "Android";

    public static final String fiveGameName = BuildConfig.FIVE_GAME;
    public static final String bonusKeno = "KenoNine", tenByEighty = "tenByEightyGame" /*"KenoSix"*/, tenByThirty = "TenByThirty", tenByTwenty = "TenByTwenty", tenByNinety = "KenoSeven", fiveGameNameLagos = "KenoFive", fastGameName = "Zerotonine", oneToTwelve = "OneToTwelve", bonusGameName = "ZimLottoBonus", bonusGameNameTwo = "ZimLottoBonusTwo", bonusFree = "ZimLottoBonusTwoFree", twelveGameName = "TwelveByTwentyFour", thaiGameName = "PaperLottoGameScreen", fiveGameNameMachine = "KenoEight";
    public static String baseURL;

    public enum MyAcctOptions {
        DEPOSIT(myAccntOptions[0]), WITHDRAWAL(myAccntOptions[1]), MY_TICKETS(myAccntOptions[2]), MY_TRANSACTION(myAccntOptions[3]), WITHDRAWAL_REPORT(myAccntOptions[4]), MY_BANK_DEPOSIT(myAccntOptions[5]);
        private String options;

        MyAcctOptions(String options) {
            this.options = options;
        }

        @Override
        public String toString() {
            return options;
        }
    }

    public void initialize(Context context) {
        updateURL = "http://192.168.124.201:8080/DownloadApp/android/phone/";

        splashWaitTimeInMills = "3000";
        orgName = context.getResources().getString(R.string.org_name);
        apkPath = "/download/";
        userNameMinLength = 5;
        drawerItems = new String[]{"Login", "My Account", "Inbox",
                "User Settings", "App Settings", "Invite Friend",
                "Locate Retailer", "Logout"};

        drawerItemCodes = new int[]{LOGIN, MY_ACCT, INBOX, USER_SETTING,
                APP_SETTING, INVITE, LOCATE, LOGOUT};
        timerFont = Typeface.createFromAsset(context.getAssets(),
                "CRYSTA_0.TTF");
        globalTextFont = Typeface.createFromAsset(context.getAssets(),
                "ROBOTO-REGULAR.TTF");

        if (BuildConfig.FLAVOR.contains("ghana")) {
            GlobalPref.getInstance(context).setCountry("GHANA");
            COUNTRY = "Ghana";
            CURRENCY_SYMBOL = "GH\\u20B5";
        } else if (BuildConfig.FLAVOR.contains("lagos")) {
            COUNTRY = "Lagos";
            GlobalPref.getInstance(context).setCountry("LAGOS");
            CURRENCY_SYMBOL = "NGN";
        } else if (BuildConfig.FLAVOR.contains("zim")) {
            COUNTRY = "Zim";
            GlobalPref.getInstance(context).setCountry("ZIM");
            CURRENCY_SYMBOL = "$";
        }
        if (!Config.isDebug) {
            baseURL = BuildConfig.BASE_URL;
        } else {
            if (!GlobalPref.getInstance(context).getIsBaseUrlFirst()) {
                GlobalPref.getInstance(context).setBaseUrl(BuildConfig.BASE_URL);
                GlobalPref.getInstance(context).setIsBaseUrlFirst(true);
            }
            baseURL = GlobalPref.getInstance(context).getBaseUrl();
        }
    }

    public static Config getInstance() {
        if (config == null)
            config = new Config();
//            initialize(context);
        return config;
        //------------ Zimbabwe update URL -------------//
//        updateURL = "http://117.239.227.85:80/DownloadApp/android/phone/";

        //------------ Ghana update URL ----------------//
        // updateURL = "http://192.168.124.84/DownloadApp/android/phone/";

        //------------ Lagos update URL ----------------//

    }

    public String getBaseURL() {
        if (baseURL == null)
            return baseURL = BuildConfig.BASE_URL;
        Utils.consolePrint("base URL" + baseURL);
        return baseURL;
    }

}
