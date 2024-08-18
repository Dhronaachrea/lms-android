package com.skilrock.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vishalp on 2/17/2015.
 */
public class VariableStorage {

    public static class UserPref {
        public static String USER_PREF = "global_pref";
        public static SharedPreferences.Editor variablesPreferencesEditor;
        private static SharedPreferences variablesPreferences;
        public static String USER_NAME = "user_name";
        public static String USER_PIC_URL = "user_pic_url";
        public static String USER_BAL = "user_bal";
        public static String BONUS_BAL = "bonus_bal";
        public static String DEPOSIT_BAL = "deposit_bal";
        public static String WITHDRAWAL_BAL = "withdrawal_bal";
        public static String WINNING_BAL = "winning_bal";
        public static String SESSION_ID = "session_id";
        public static String PLAYER_ID = "player_id";
        public static String GCM_ID = "gcm_id";

        public static void setStringPreferences(Context context, String key, String value) {
            variablesPreferencesEditor = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE).edit();
            variablesPreferencesEditor.putString(key, value);
            variablesPreferencesEditor.commit();
        }

        public static String getStringData(Context context, String key) {
            variablesPreferences = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
            return variablesPreferences.getString(key, "");
        }

        public static String getStringDataAmt(Context context, String key) {
            variablesPreferences = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
            return variablesPreferences.getString(key, "0.00");
        }


        public static void clearPref(Context context) {
            variablesPreferences = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
            variablesPreferences.edit().clear().commit();
        }
    }

    public static class GlobalPref {
        public static String EXIT_APP = "exit_app";
        public static String APP_VERSION = "app_version";
        public static String GLOBAL_PREF = "global_pref";
        private static SharedPreferences.Editor variablesPreferencesEditor;
        private static SharedPreferences variablesPreferences;
        public static String COMPANY_NAME = "company_name";
        public static String COPYRIGHT_TEXT = "copyright_text";
        public static String COUNTRY = "country";
        public static String CURRENCY_CODE = "currency_code";
        public static String CURRENCY_SYMBOL = "currency_symbol";
        public static String DATE_FORMAT = "date_format";
        public static String MOBILE_NO_LENGTH = "mobile_no_length";
        public static String USERNAME_MIN_LENGTH = "username_min_length";
        public static String PMS_MER_KEY = "pms_mer_key";
        public static String PMS_SECURE_CODE = "pms_secure_code";
        public static String LOGIN_DATA = "login_data";
        //for lotto League
        public static String PLAYER_LEAGUE_ACTIVE = "isPlayerLeagueActive";


        public static String SC_SER_NAME = "sc_ser_name";

        public static String DGE_SERVICE_DETAILS = "dge_service_details";
        public static String DGE_BANNERS_DETAILS = "dge_banners_details";

        public static String DGE_SEQ_CODE = "dge_seq_code";
        public static String DGE_ROOT_URL = "dge_root_url";
        public static String DGE_MER_KEY = "dge_mer_key";
        public static String DGE_MER_CODE = "dge_mer_code";
        public static String DGE_SER_NAME = "dge_ser_name";


        public static String IGE_SEC_CODE = "ige_sec_code";
        public static String IGE_ROOT_URL = "ige_root_url";
        public static String IGE_MER_KEY = "ige_mer_key";
        public static String IGE_MER_CODE = "ige_mer_code";
        public static String IGE_DOM_NAME = "ige_dom_name";
        public static String IGE_SER_CODE = "ige_ser_code";
        public static String IGE_SER_NAME = "ige_ser_name";
        public static String IGE_LANG = "ige_lang";


        public static String SLE_MER_CODE = "sle_mer_code";
        public static String SLE_MER_KEY = "sle_mer_key";
        public static String SLE_SEQ_CODE = "sle_seq_code";
        public static String SLE_ROOT_URL = "sle_root_url";
        public static String SLE_NOTIFICATION = "sle_notification";
        public static String SLE_SER_NAME = "sle_ser_name";
        public static String SLE_IS_ENABLED = "sle_is_enabled";


        public static void setStringPreferences(Context context, String key, String value) {
            variablesPreferencesEditor = context.getSharedPreferences(GLOBAL_PREF, Context.MODE_PRIVATE).edit();
            variablesPreferencesEditor.putString(key, value);
            variablesPreferencesEditor.commit();
        }

        public static String getStringData(Context context, String key) {
            variablesPreferences = context.getSharedPreferences(GLOBAL_PREF, Context.MODE_PRIVATE);
            return variablesPreferences.getString(key, "");
        }

        public static void setBooleanPreferences(Context context, String key, boolean value) {
            variablesPreferencesEditor = context.getSharedPreferences(GLOBAL_PREF, Context.MODE_PRIVATE).edit();
            variablesPreferencesEditor.putBoolean(key, value);
            variablesPreferencesEditor.commit();
        }

        public static boolean getBooleanData(Context context, String key) {
            variablesPreferences = context.getSharedPreferences(GLOBAL_PREF, Context.MODE_PRIVATE);
            return variablesPreferences.getBoolean(key, false);
        }

        public static void clearPref(Context context) {
            variablesPreferences = context.getSharedPreferences(GLOBAL_PREF, Context.MODE_PRIVATE);
            variablesPreferences.edit().clear().commit();
        }
    }

}
