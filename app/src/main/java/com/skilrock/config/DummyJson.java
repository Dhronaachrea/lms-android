package com.skilrock.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by sagar on 12/11/2015.
 */
public class DummyJson {
    private Context context;
    public static final String DUMMY_PREF = "duumy_pref";
    private static DummyJson dummyJson;
    private static HashMap<String, String> dummyConfigMap;
    private SharedPreferences.Editor variablesPreferencesEditor;
    private SharedPreferences variablesPreferences;
    private Gson gson = new Gson();
    public static final String dummyMap = "dummyMap";
    public static String dummyStateString = "{\"isSuccess\":true,\"stateData\":[{\"stateCode\":\"BUL\",\"stateName\":\"BULAWAYO\"},{\"stateCode\":\"HAR\",\"stateName\":\"HARARE\"},{\"stateCode\":\"MAN\",\"stateName\":\"MANICALAND\"},{\"stateCode\":\"MAC\",\"stateName\":\"MASHONALAND CENTRAL\"},{\"stateCode\":\"MAE\",\"stateName\":\"MASHONALAND EAST\"},{\"stateCode\":\"MAW\",\"stateName\":\"MASHONALAND WEST\"},{\"stateCode\":\"MAS\",\"stateName\":\"MASVINGO PROVINCE\"},{\"stateCode\":\"MTN\",\"stateName\":\"MATABELELAND NORTH\"},{\"stateCode\":\"MTS\",\"stateName\":\"MATABELELAND SOUTH\"},{\"stateCode\":\"MID\",\"stateName\":\"MIDLANDS PROVINCE\"}]}";
    public static String dummyCityList = "{\"cityData\":[{\"cityCode\":\"BEMB\",\"cityName\":\"Bembezi\"},{\"cityCode\":\"BING\",\"cityName\":\"Binga\"},{\"cityCode\":\"DAGA\",\"cityName\":\"Dagamela\"},{\"cityCode\":\"DEKA\",\"cityName\":\"Deka Drum\"},{\"cityCode\":\"DETE\",\"cityName\":\"Dete\"},{\"cityCode\":\"EAST\",\"cityName\":\"Eastnor\"},{\"cityCode\":\"HWAN\",\"cityName\":\"Hwange\"},{\"cityCode\":\"INYA\",\"cityName\":\"Inyati\"},{\"cityCode\":\"KAMA\",\"cityName\":\"Kamativi\"},{\"cityCode\":\"KARIY\",\"cityName\":\"Kariyangwe\"},{\"cityCode\":\"KAZUN\",\"cityName\":\"Kazungula\"},{\"cityCode\":\"KENM\",\"cityName\":\"Kenmaur\"},{\"cityCode\":\"LONE\",\"cityName\":\"Lonely Mine\"},{\"cityCode\":\"LUPA\",\"cityName\":\"Lupane\"},{\"cityCode\":\"LUSU\",\"cityName\":\"Lusulu\"},{\"cityCode\":\"MATE\",\"cityName\":\"Matetsi\"},{\"cityCode\":\"MLI\",\"cityName\":\"Mlibizi\"},{\"cityCode\":\"MSU\",\"cityName\":\"Msuna\"},{\"cityCode\":\"NKA\",\"cityName\":\"Nkayi\"},{\"cityCode\":\"NTA\",\"cityName\":\"Ntabazinduna\"},{\"cityCode\":\"NYAMA\",\"cityName\":\"Nyamandhlovu\"},{\"cityCode\":\"PAND\",\"cityName\":\"Pandamatenga\"},{\"cityCode\":\"QUEE\",\"cityName\":\"Queen's Mine\"},{\"cityCode\":\"SHAN\",\"cityName\":\"Shangani\"},{\"cityCode\":\"SIAB\",\"cityName\":\"Siabuwa\"},{\"cityCode\":\"TSHO\",\"cityName\":\"Tsholotsho\"},{\"cityCode\":\"TSHOT\",\"cityName\":\"Tshotsholo\"},{\"cityCode\":\"TURK\",\"cityName\":\"Turk Mine\"},{\"cityCode\":\"VICF\",\"cityName\":\"Victoria Falls\"}],\"isSuccess\":true}";

    private DummyJson() {
    }

    public static DummyJson getInstance(Context context) {

        if (dummyJson == null) {
            dummyJson = new DummyJson();
            dummyJson.setContext(context);
        }
        return dummyJson;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    public void setDummyMap(HashMap<String, String> mapToSave) {
        String hashMapString = gson.toJson(mapToSave);
        variablesPreferencesEditor = context.getSharedPreferences(DUMMY_PREF, Context.MODE_PRIVATE).edit();
        variablesPreferencesEditor.putString(dummyMap, hashMapString);
        variablesPreferencesEditor.apply();
    }

    public HashMap<String, String> getDummyMap() {

        if (dummyConfigMap == null) {

            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            variablesPreferences = context.getSharedPreferences(DUMMY_PREF, Context.MODE_PRIVATE);
            if (variablesPreferences != null) {
                variablesPreferences = context.getSharedPreferences(DUMMY_PREF, Context.MODE_PRIVATE);
                String dummyString = variablesPreferences.getString(dummyMap, "");
                if (!dummyString.equals("")) {
                    dummyConfigMap = gson.fromJson(dummyString, type);

                } else {
                    dummyConfigMap = new HashMap<String, String>();
                }
            }
        }

        return dummyConfigMap;
    }

}
