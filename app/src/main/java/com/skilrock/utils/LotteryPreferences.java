package com.skilrock.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abhishekd on 4/29/2015.
 */
public class LotteryPreferences {
    private SharedPreferences sharedPreferences;
    private String PREF_ID = "lottery_pref";
    private String PERM_1 = "prem1";
    private String PERM_2 = "prem2";
    private String PERM_3 = "prem3";
    private String PERM_4 = "prem4";
    private String PERM_5 = "prem5";
    private String PERM_6 = "prem6";
    private String DIRECT_1 = "direct1";
    private String DIRECT_2 = "direct2";
    private String DIRECT_3 = "direct3";
    private String DIRECT_4 = "direct4";
    private String DIRECT_5 = "direct5";
    private String DIRECT_6 = "direct6";
    private String DIRECT_10 = "direct10";
    private String BANKER = "banker";
    private String BANKER_1_AGAINST_ALL = "banker1AgainstAll";
    private String ZERO_TO_NINE = "zeroToNine";
    private String ONE_TO_TWELVE = "oneToTwelve";

    private String TWELVE_BY_TWENTYFOUR = "twelveByTwentyFour";
    private String WIDTH = "width";
    private String HEIGHT = "height";

    private String DIRECT_12 = "Direct12";
    private String FIREST_12 = "First12";
    private String LAST_12 = "Last12";
    private String ALLODD = "AllOdd";
    private String ALLEVEN = "AllEven";
    private String ODDEVEN = "OddEven";
    private String EVENODD = "EvenOdd";
    private String JUMPEVENODD = "JumpEvenOdd";
    private String JUMPODDEVEN = "JumpOddEven";
    private String PERM_12 = "Perm12";
    private String MATCH_10 = "Match10";
    private String ANIM = "anim";
    //new bet DC-Direct3 DC_Perm3
    private String DC_DIRECT3 = "DC_Direct3";
    private String DC_Perm3 = "D_Perm3";


    //for machine
    private String MN_PERM2 = "MN_Perm2";
    private String MN_DIRECT2 = "MN_DIRECT2";
    private String MN_DIRECT3 = "MN_DIRECT3";
    private String MN_DIRECT4 = "MN_DIRECT4";
    private String MN_DIRECT5 = "MN_DIRECT5";
    private String MN_PERM3 = "MN_PERM3";
    private String MN_BANKER = "MN_BANKER";
    private String MN_BANKER1AGAINSTALL = "MN_BANKER1AGAINSTALL";
    private String MN_DIRECT1 = "MN_DIRECT1";

    //for lagos
    private String DC_Perm2 = "DcPerm2";
    private String DC_Direct2 = "DcDirect2";
    private String ONE_BY_TWELVE = "oneByTwelve";

    //for lagos five Game
    private String PERM_1_LAGOS = "prem1lagos";
    private String PERM_2_LAGOS = "prem2lagos";
    private String PERM_3_LAGOS = "prem3lagos";
    private String DIRECT_2_LAGOS = "direct2lagos";
    private String DIRECT_1_LAGOS = "direct1lagos";
    private String DIRECT_3_LAGOS = "direct3lagos";
    private String DIRECT_4_LAGOS = "direct4lagos";
    private String DIRECT_5_LAGOS = "direct5lagos";
    private String BANKER_LAGOS = "bankerlagos";
    private String BANKER_1_AGAINST_ALL_LAGOS = "banker1AgainstAlllagos";
    private String DC_Perm2_ALL_LAGOS = "DcPerm2lagos";
    private String DC_Direct2_ALL_LAGOS = "DcDirect2lagos";
    //new bet DC-Direct3 DC-Perm3
    private String DC_Direct3_LAGOS = "DcDirect3lagos";
    private String DC_Perm3_LAGOS = "DcPerm3lagos";

    //for machine
    private String MN_PERM2_LAGOS = "MN_Perm2lagos";
    private String MN_DIRECT2_LAGOS = "MN_DIRECT2lagos";
    private String MN_DIRECT3_LAGOS = "MN_DIRECT3lagos";
    private String MN_DIRECT4_LAGOS = "MN_DIRECT4lagos";
    private String MN_DIRECT5_LAGOS = "MN_DIRECT5lagos";
    private String MN_PERM3_LAGOS = "MN_PERM3lagos";
    private String MN_BANKER_LAGOS = "MN_BANKERlagos";
    private String MN_BANKER1AGAINSTALL_LAGOS = "MN_BANKER1AGAINSTALLlagos";
    private String MN_DIRECT1_LAGOS = "MN_DIRECT1lagos";

    //tenbyninty
    private String DIRECT_10_TEN_THIRTY = "Direct10";
    private String FIRST_10_TEN_THIRTY = "First10";
    private String MIDDLE_10_TEN_THIRTY = "Middle10";
    private String LAST_10_TEN_THIRTY = "Last10";

    //bonusKeno
    private String DIRECT_1_BONUS_KENO = "Direct1";
    private String DIRECT_2_BONUS_KENO = "Direct2";
    private String DIRECT_3_BONUS_KENO = "Direct3";
    private String DIRECT_4_BONUS_KENO = "Direct4";
    private String DIRECT_5_BONUS_KENO = "Direct5";
    private String DIRECT_6_BONUS_KENO = "Direct6";

    private String PERM_2_BONUS_KENO = "Perm2";
    private String PERM_3_BONUS_KENO = "Perm3";
    private String PERM_4_BONUS_KENO = "Perm4";

    //tenbytwenty
    private String FIRST_10_TEN_BY_TWENTY = "first10";
    private String LAST_10_TEN_BY_TWENTY = "last10";
    private String ALLODD_TEN_BY_TWENTY = "allodd";
    private String ALLEVEN_TEN_BY_TWENTY = "alleven";

    private String ODDEVEN_TEN_BY_TWENTY = "oddeven";
    private String EVENODD_TEN_BY_TWENTY = "evenodd";
    private String JUMP_ODD_EVEN_TEN_BY_TWENTY = "jumpoddeven";
    private String JUMP_EVEN_ODD_TEN_BY_TWENTY = "jumpoddeven";
    private String DIRECT_10_TEN_BY_TWENTY = "direct10";

    //tenbyeighty
    private String DIRECT_1_TEN_BY_EIGHTY = "direct1";
    private String DIRECT_2_TEN_BY_EIGHTY = "direct2";
    private String DIRECT_3_TEN_BY_EIGHTY = "direct3";
    private String DIRECT_4_TEN_BY_EIGHTY = "direct4";
    private String DIRECT_5_TEN_BY_EIGHTY = "direct5";
    private String DIRECT_6_TEN_BY_EIGHTY = "direct6";
    private String DIRECT_7_TEN_BY_EIGHTY = "direct7";
    private String DIRECT_8_TEN_BY_EIGHTY = "direct8";
    private String DIRECT_9_TEN_BY_EIGHTY = "direct9";
    private String DIRECT_10_TEN_BY_EIGHTY = "direct10";
    //end of tenbyeighty


//    private String BANKER_1_AGAINST_ALL_LAGOS = "banker1AgainstAlllagos";
//    private String DC_Perm2_ALL_LAGOS = "DcPerm2lagos";
//    private String DC_Direct2_ALL_LAGOS = "DcDirect2lagos";

    //start tenBytwenty


    //tenbyeighty

    public String getDIRECT_1_TEN_BY_EIGHTY() {
        return sharedPreferences.getString(DIRECT_1_TEN_BY_EIGHTY, "");
    }

    public void setDIRECT_1_TEN_BY_EIGHTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_1_TEN_BY_EIGHTY, value);
        editor.commit();
    }

    public String getDIRECT_2_TEN_BY_EIGHTY() {
        return sharedPreferences.getString(DIRECT_1_TEN_BY_EIGHTY, "");
    }

    public void setDIRECT_2_TEN_BY_EIGHTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_2_TEN_BY_EIGHTY, value);
        editor.commit();
    }

    public String getDIRECT_3_TEN_BY_EIGHTY() {
        return sharedPreferences.getString(DIRECT_3_TEN_BY_EIGHTY, "");
    }

    public void setDIRECT_3_TEN_BY_EIGHTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_3_TEN_BY_EIGHTY, value);
        editor.commit();
    }

    public String getDIRECT_4_TEN_BY_EIGHTY() {
        return sharedPreferences.getString(DIRECT_4_TEN_BY_EIGHTY, "");
    }

    public void setDIRECT_4_TEN_BY_EIGHTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_4_TEN_BY_EIGHTY, value);
        editor.commit();
    }

    public String getDIRECT_5_TEN_BY_EIGHTY() {
        return sharedPreferences.getString(DIRECT_5_TEN_BY_EIGHTY, "");
    }

    public void setDIRECT_5_TEN_BY_EIGHTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_5_TEN_BY_EIGHTY, value);
        editor.commit();
    }

    public String getDIRECT_6_TEN_BY_EIGHTY() {
        return sharedPreferences.getString(DIRECT_6_TEN_BY_EIGHTY, "");
    }

    public void setDIRECT_6_TEN_BY_EIGHTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_6_TEN_BY_EIGHTY, value);
        editor.commit();
    }

    public String getDIRECT_7_TEN_BY_EIGHTY() {
        return sharedPreferences.getString(DIRECT_7_TEN_BY_EIGHTY, "");
    }

    public void setDIRECT_7_TEN_BY_EIGHTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_7_TEN_BY_EIGHTY, value);
        editor.commit();
    }

    public String getDIRECT_8_TEN_BY_EIGHTY() {
        return sharedPreferences.getString(DIRECT_8_TEN_BY_EIGHTY, "");
    }

    public void setDIRECT_8_TEN_BY_EIGHTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_8_TEN_BY_EIGHTY, value);
        editor.commit();
    }

    public String getDIRECT_9_TEN_BY_EIGHTY() {
        return sharedPreferences.getString(DIRECT_9_TEN_BY_EIGHTY, "");
    }

    public void setDIRECT_9_TEN_BY_EIGHTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_9_TEN_BY_EIGHTY, value);
        editor.commit();
    }

    public String getDIRECT_10_TEN_BY_EIGHTY() {
        return sharedPreferences.getString(DIRECT_10_TEN_BY_EIGHTY, "");
    }

    public void setDIRECT_10_TEN_BY_EIGHTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_10_TEN_BY_EIGHTY, value);
        editor.commit();
    }

    //end of tenbyeighty

    //setters
    public void setFIRST_10_TEN_BY_TWENTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIRST_10_TEN_BY_TWENTY, value);
        editor.commit();
    }

    public void setLAST_10_TEN_BY_TWENTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_10_TEN_BY_TWENTY, value);
        editor.commit();
    }

    public void setALLODD_TEN_BY_TWENTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ALLODD_TEN_BY_TWENTY, value);
        editor.commit();
    }

    public void setALLEVEN_TEN_BY_TWENTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ALLEVEN_TEN_BY_TWENTY, value);
        editor.commit();
    }

    public void setODDEVEN_TEN_BY_TWENTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ODDEVEN_TEN_BY_TWENTY, value);
        editor.commit();
    }

    public void setEVENODD_TEN_BY_TWENTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EVENODD_TEN_BY_TWENTY, value);
        editor.commit();
    }

    public void setJUMP_ODD_EVEN_TEN_BY_TWENTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(JUMP_ODD_EVEN_TEN_BY_TWENTY, value);
        editor.commit();
    }

    public void setJUMP_EVEN_ODD_TEN_BY_TWENTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(JUMP_EVEN_ODD_TEN_BY_TWENTY, value);
        editor.commit();
    }

    public void setDIRECT_10_TEN_BY_TWENTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_10_TEN_BY_TWENTY, value);
        editor.commit();
    }


    //getters

    public String getFIRST_10_TEN_BY_TWENTY() {
        return sharedPreferences.getString(FIRST_10_TEN_BY_TWENTY, "");
    }

    public String getLAST_10_TEN_BY_TWENTY() {
        return sharedPreferences.getString(LAST_10_TEN_BY_TWENTY, "");
    }

    public String getALLODD_TEN_BY_TWENTY() {
        return sharedPreferences.getString(ALLODD_TEN_BY_TWENTY, "");
    }

    public String getALLEVEN_TEN_BY_TWENTY() {
        return sharedPreferences.getString(ALLEVEN_TEN_BY_TWENTY, "");
    }

    public String getODDEVEN_TEN_BY_TWENTY() {
        return sharedPreferences.getString(ODDEVEN_TEN_BY_TWENTY, "");
    }

    public String getEVENODD_TEN_BY_TWENTY() {
        return sharedPreferences.getString(EVENODD_TEN_BY_TWENTY, "");
    }

    public String getJUMP_ODD_EVEN_TEN_BY_TWENTY() {
        return sharedPreferences.getString(JUMP_ODD_EVEN_TEN_BY_TWENTY, "");
    }

    public String getJUMP_EVEN_ODD_TEN_BY_TWENTY() {
        return sharedPreferences.getString(JUMP_EVEN_ODD_TEN_BY_TWENTY, "");
    }

    public String getDIRECT_10_TEN_BY_TWENTY() {
        return sharedPreferences.getString(DIRECT_10_TEN_BY_TWENTY, "");
    }


    //end changes


    public String getMATCH_10() {
        return sharedPreferences.getString(MATCH_10, "");
    }


    public void setMATCH_10(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MATCH_10, value);
        editor.commit();
    }

    public String getPERM_12() {
        return sharedPreferences.getString(PERM_12, "");
    }

    public void setPERM_12(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PERM_12, value);
        editor.commit();
    }

    public String getEVENODD() {
        return sharedPreferences.getString(EVENODD, "");
    }

    public void setEVENODD(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EVENODD, value);
        editor.commit();
    }

    public String getODDEVEN() {
        return sharedPreferences.getString(ODDEVEN, "");
    }

    public void setODDEVEN(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ODDEVEN, value);
        editor.commit();
    }

    public String getALLEVEN() {
        return sharedPreferences.getString(ALLEVEN, "");
    }

    public void setALLEVEN(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ALLEVEN, value);
        editor.commit();
    }

    public String getALLODD() {
        return sharedPreferences.getString(ALLODD, "");
    }

    public void setALLODD(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ALLODD, value);
        editor.commit();
    }

    public String getLAST_12() {
        return sharedPreferences.getString(LAST_12, "");
    }

    public void setLAST_12(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_12, value);
        editor.commit();
    }

    public String getFIREST_12() {
        return sharedPreferences.getString(FIREST_12, "");
    }

    public void setFIREST_12(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIREST_12, value);
        editor.commit();
    }

    public String getDIRECT_12() {
        return sharedPreferences.getString(DIRECT_12, "");
    }

    public void setDIRECT_12(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_12, value);
        editor.commit();
    }

    public String getJUMPEVENODD() {
        return sharedPreferences.getString(JUMPEVENODD, "");
    }

    public void setJUMPEVENODD(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(JUMPEVENODD, value);
        editor.commit();
    }

    public String getJUMPODDEVEN() {
        return sharedPreferences.getString(JUMPODDEVEN, "");
    }

    public void setJUMPODDEVEN(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(JUMPODDEVEN, value);
        editor.commit();
    }


    public LotteryPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
    }


    public void setPERM_1(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PERM_1, value);
        editor.commit();
    }

    public String getPERM_1() {
        return sharedPreferences.getString(PERM_1, "");
    }

    public void setPERM_2(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PERM_2, value);
        editor.commit();
    }

    public String getPERM_2() {
        return sharedPreferences.getString(PERM_2, "");
    }

    public void setPERM_3(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PERM_3, value);
        editor.commit();
    }

    public String getPERM_3() {
        return sharedPreferences.getString(PERM_3, "");
    }


    public void setWIDTH(int width) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(WIDTH, width);
        editor.commit();
    }

    public int getWIDTH() {
        return sharedPreferences.getInt(WIDTH, 480);
    }


    public void setHEIGHT(int height) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HEIGHT, height);
        editor.commit();
    }

    public int getHEIGHT() {
        return sharedPreferences.getInt(HEIGHT, 800);
    }


    public void setPERM_4(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PERM_4, value);
        editor.commit();
    }

    public String getPERM_4() {
        return sharedPreferences.getString(PERM_4, "");
    }

    public void setPERM_5(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PERM_5, value);
        editor.commit();
    }

    public String getPERM_5() {
        return sharedPreferences.getString(PERM_5, "");
    }

    public void setPERM_6(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PERM_6, value);
        editor.commit();
    }

    public String getPERM_6() {
        return sharedPreferences.getString(PERM_6, "");
    }

    public String getDIRECT_1() {
        return sharedPreferences.getString(DIRECT_1, "");
    }

    public String getDIRECT_2() {
        return sharedPreferences.getString(DIRECT_2, "");
    }

    public String getDIRECT_3() {
        return sharedPreferences.getString(DIRECT_3, "");
    }

    public String getDIRECT_4() {
        return sharedPreferences.getString(DIRECT_4, "");
    }

    public String getDIRECT_10() {
        return sharedPreferences.getString(DIRECT_10, "");
    }

    public String getDIRECT_5() {
        return sharedPreferences.getString(DIRECT_5, "");
    }

    public String getDIRECT_6() {
        return sharedPreferences.getString(DIRECT_6, "");
    }

    public String getBANKER() {
        return sharedPreferences.getString(BANKER, "");
    }

    public String getBANKER_1_AGAINST_ALL() {
        return sharedPreferences.getString(BANKER_1_AGAINST_ALL, "");
    }

    public String getZERO_TO_NINE() {
        return sharedPreferences.getString(ZERO_TO_NINE, "");
    }

    public void setONE_TO_TWELVE(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ONE_TO_TWELVE, value);
        editor.commit();
    }

    public String getONE_TO_TWELVE() {
        return sharedPreferences.getString(ONE_TO_TWELVE, "");
    }


    public void setDIRECT_1(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_1, value);
        editor.commit();
    }

    public void setDIRECT_2(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_2, value);
        editor.commit();
    }

    public void setDIRECT_3(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_3, value);
        editor.commit();
    }

    public void setDIRECT_4(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_4, value);
        editor.commit();
    }

    public void setDIRECT_10(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_10, value);
        editor.commit();
    }

    public void setDIRECT_5(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_5, value);
        editor.commit();
    }

    public void setDIRECT_6(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_6, value);
        editor.commit();
    }

    public void setBANKER(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BANKER, value);
        editor.commit();
    }

    public void setBANKER_1_AGAINST_ALL(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BANKER_1_AGAINST_ALL, value);
        editor.commit();
    }

    public void setZERO_TO_NINE(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ZERO_TO_NINE, value);
        editor.commit();
    }


    public void setONE_BY_TWELVE(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ONE_BY_TWELVE, value);
        editor.commit();
    }

    public void setTWELVE_BY_TWENTYFOUR(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TWELVE_BY_TWENTYFOUR, value);
        editor.commit();
    }

    //lagos Data

    public void setDC_Perm2(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DC_Perm2, value);
        editor.commit();
    }

    public String getDC_Perm2() {
        return sharedPreferences.getString(DC_Perm2, "");
    }

    public void setDC_Direct2(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DC_Direct2, value);
        editor.commit();
    }

    public String getDC_Direct2() {
        return sharedPreferences.getString(DC_Direct2, "");
    }

    public String getONE_BY_TWELVE() {
        return sharedPreferences.getString(ONE_BY_TWELVE, "");
    }

    public String getTWELVE_BY_TWENTYFOUR() {
        return sharedPreferences.getString(TWELVE_BY_TWENTYFOUR, "");
    }

    public String getPERM_1_LAGOS() {
        return sharedPreferences.getString(PERM_1_LAGOS, "");
    }

    public void setPERM_1_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PERM_1_LAGOS, value);
        editor.commit();
    }

    public String getPERM_2_LAGOS() {
        return sharedPreferences.getString(PERM_2_LAGOS, "");
    }

    public void setPERM_2_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PERM_2_LAGOS, value);
        editor.commit();
    }

    public String getPERM_3_LAGOS() {
        return sharedPreferences.getString(PERM_3_LAGOS, "");
    }

    public void setPERM_3_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PERM_3_LAGOS, value);
        editor.commit();
    }

    public String getDIRECT_2_LAGOS() {
        return sharedPreferences.getString(DIRECT_2_LAGOS, "");
    }

    public void setDIRECT_2_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_2_LAGOS, value);
        editor.commit();
    }


    public String getDIRECT_1_LAGOS() {
        return sharedPreferences.getString(DIRECT_1_LAGOS, "");
    }

    public void setDIRECT_1_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_1_LAGOS, value);
        editor.commit();
    }

    public String getDIRECT_3_LAGOS() {
        return sharedPreferences.getString(DIRECT_3_LAGOS, "");
    }

    public void setDIRECT_3_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_3_LAGOS, value);
        editor.commit();
    }

    public String getDIRECT_4_LAGOS() {
        return sharedPreferences.getString(DIRECT_4_LAGOS, "");
    }

    public void setDIRECT_4_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_4_LAGOS, value);
        editor.commit();
    }

    public String getDIRECT_5_LAGOS() {
        return sharedPreferences.getString(DIRECT_5_LAGOS, "");
    }

    public void setDIRECT_5_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_5_LAGOS, value);
        editor.commit();
    }


    public String getBANKER_LAGOS() {
        return sharedPreferences.getString(BANKER_LAGOS, "");
    }

    public void setBANKER_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BANKER_LAGOS, value);
        editor.commit();
    }

    public String getBANKER_1_AGAINST_ALL_LAGOS() {
        return sharedPreferences.getString(BANKER_1_AGAINST_ALL_LAGOS, "");
    }

    public void setBANKER_1_AGAINST_ALL_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BANKER_1_AGAINST_ALL_LAGOS, value);
        editor.commit();
    }

    public String getDC_Perm2_ALL_LAGOS() {
        return sharedPreferences.getString(DC_Perm2_ALL_LAGOS, "");
    }

    public void setDC_Perm2_ALL_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DC_Perm2_ALL_LAGOS, value);
        editor.commit();
    }


    public String getDC_Direct2_ALL_LAGOS() {
        return sharedPreferences.getString(DC_Direct2_ALL_LAGOS, "");
    }

    public void setDC_Direct2_ALL_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DC_Direct2_ALL_LAGOS, value);
        editor.commit();
    }


    //laagos machine
    public void setMN_PERM2_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_PERM2_LAGOS, value);
        editor.commit();
    }

    public String getMN_PERM2_LAGOS() {
        return sharedPreferences.getString(MN_PERM2_LAGOS, "");
    }

    public void setMN_DIRECT3_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_DIRECT3_LAGOS, value);
        editor.commit();
    }

    public String getMN_DIRECT3_LAGOS() {
        return sharedPreferences.getString(MN_DIRECT3_LAGOS, "");
    }

    public void setMN_DIRECT2_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_DIRECT2_LAGOS, value);
        editor.commit();
    }

    public String getMN_DIRECT2_LAGOS() {
        return sharedPreferences.getString(MN_DIRECT2_LAGOS, "");
    }

    public void setMN_DIRECT4_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_DIRECT4_LAGOS, value);
        editor.commit();
    }

    public String getMN_DIRECT4_LAGOS() {
        return sharedPreferences.getString(MN_DIRECT4_LAGOS, "");
    }

    public void setMN_DIRECT5_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_DIRECT5_LAGOS, value);
        editor.commit();
    }

    public String getMN_DIRECT5_LAGOS() {
        return sharedPreferences.getString(MN_DIRECT5_LAGOS, "");
    }

    public void setMN_PERM3_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_PERM3_LAGOS, value);
        editor.commit();
    }

    public String getMN_PERM3_LAGOS() {
        return sharedPreferences.getString(MN_PERM3_LAGOS, "");
    }

    public void setMN_BANKER_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_BANKER_LAGOS, value);
        editor.commit();
    }

    public String getMN_BANKER_LAGOS() {
        return sharedPreferences.getString(MN_BANKER_LAGOS, "");
    }

    public void setMN_BANKER1AGAINSTALL_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_BANKER1AGAINSTALL_LAGOS, value);
        editor.commit();
    }

    public String getMN_BANKER1AGAINSTALL_LAGOS() {
        return sharedPreferences.getString(MN_BANKER1AGAINSTALL_LAGOS, "");
    }

    public void setMN_DIRECT1_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_DIRECT1_LAGOS, value);
        editor.commit();
    }

    public String getMN_DIRECT1_LAGOS() {
        return sharedPreferences.getString(MN_DIRECT1_LAGOS, "");
    }


    //for another mc -lagos
    public void setMN_PERM2(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_PERM2, value);
        editor.commit();
    }

    public String getMN_PERM2() {
        return sharedPreferences.getString(MN_PERM2, "");
    }

    public void setMN_DIRECT3(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_DIRECT3, value);
        editor.commit();
    }

    public String getMN_DIRECT3() {
        return sharedPreferences.getString(MN_DIRECT3, "");
    }

    public void setMN_DIRECT2(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_DIRECT2, value);
        editor.commit();
    }

    public String getMN_DIRECT2() {
        return sharedPreferences.getString(MN_DIRECT2, "");
    }

    public void setMN_DIRECT4(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_DIRECT4, value);
        editor.commit();
    }

    public String getMN_DIRECT4() {
        return sharedPreferences.getString(MN_DIRECT4, "");
    }

    public void setMN_DIRECT5(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_DIRECT5, value);
        editor.commit();
    }

    public String getMN_DIRECT5() {
        return sharedPreferences.getString(MN_DIRECT5, "");
    }

    public void setMN_PERM3(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_PERM3, value);
        editor.commit();
    }

    public String getMN_PERM3() {
        return sharedPreferences.getString(MN_PERM3, "");
    }

    public void setMN_BANKER(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_BANKER, value);
        editor.commit();
    }

    public String getMN_BANKER() {
        return sharedPreferences.getString(MN_BANKER, "");
    }

    public void setMN_BANKER1AGAINSTALL(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_BANKER1AGAINSTALL, value);
        editor.commit();
    }

    public String getMN_BANKER1AGAINSTALL() {
        return sharedPreferences.getString(MN_BANKER1AGAINSTALL, "");
    }

    public void setMN_DIRECT1(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MN_DIRECT1, value);
        editor.commit();
    }

    public String getMN_DIRECT1() {
        return sharedPreferences.getString(MN_DIRECT1, "");
    }

    public void setDC_DIRECT3_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DC_Direct3_LAGOS, value);
        editor.commit();
    }

    public String getDC_DIRECT3_LAGOS() {
        return sharedPreferences.getString(DC_Direct3_LAGOS, "");
    }


    public void setDC_DIRECT3(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DC_DIRECT3, value);
        editor.commit();
    }

    public String getDC_DIRECT3() {
        return sharedPreferences.getString(DC_DIRECT3, "");
    }

    public void setDC_Perm3(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DC_Perm3, value);
        editor.commit();
    }

    public String getDC_Perm3() {
        return sharedPreferences.getString(DC_Perm3, "");
    }

    public void setDC_Perm3_LAGOS(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DC_Perm3_LAGOS, value);
        editor.commit();
    }

    public String getDC_Perm3_LAGOS() {
        return sharedPreferences.getString(DC_Perm3_LAGOS, "");
    }


    //tenby ninty
    public String getFIRST_10_TEN_THIRTY() {
        return sharedPreferences.getString(FIRST_10_TEN_THIRTY, "");
    }

    public void setFIRST_10_TEN_THIRTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_10_TEN_THIRTY, value);
        editor.commit();
    }

    public String getDIRECT_10_TEN_THIRTY() {
        return sharedPreferences.getString(DIRECT_10_TEN_THIRTY, "");
    }

    public void setDIRECT_10_TEN_THIRTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_10_TEN_THIRTY, value);
        editor.commit();
    }

    public String getMIDDLE_10_TEN_THIRTY() {
        return sharedPreferences.getString(MIDDLE_10_TEN_THIRTY, "");
    }

    public void setMIDDLE_10_TEN_THIRTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MIDDLE_10_TEN_THIRTY, value);
        editor.commit();
    }

    public String getLAST_10_TEN_THIRTY() {
        return sharedPreferences.getString(LAST_10_TEN_THIRTY, "");
    }

    public void setLAST_10_TEN_THIRTY(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_10_TEN_THIRTY, value);
        editor.commit();
    }

    //bonusKeno
    public String getDIRECT_1_BONUS_KENO() {
        return sharedPreferences.getString(DIRECT_1_BONUS_KENO, "");
    }

    public void setDIRECT_1_BONUS_KENO(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_1_BONUS_KENO, value);
        editor.commit();
    }


    public String getDIRECT_2_BONUS_KENO() {
        return sharedPreferences.getString(DIRECT_2_BONUS_KENO, "");
    }

    public void setDIRECT_2_BONUS_KENO(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_2_BONUS_KENO, value);
        editor.commit();
    }


    public String getDIRECT_3_BONUS_KENO() {
        return sharedPreferences.getString(DIRECT_3_BONUS_KENO, "");
    }

    public void setDIRECT_3_BONUS_KENO(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_3_BONUS_KENO, value);
        editor.commit();
    }

    public String getDIRECT_4_BONUS_KENO() {
        return sharedPreferences.getString(DIRECT_4_BONUS_KENO, "");
    }

    public void setDIRECT_4_BONUS_KENO(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_4_BONUS_KENO, value);
        editor.commit();
    }


    public String getDIRECT_5_BONUS_KENO() {
        return sharedPreferences.getString(DIRECT_5_BONUS_KENO, "");
    }

    public void setDIRECT_5_BONUS_KENO(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_5_BONUS_KENO, value);
        editor.commit();
    }

    public String getDIRECT_6_BONUS_KENO() {
        return sharedPreferences.getString(DIRECT_6_BONUS_KENO, "");
    }

    public void setDIRECT_6_BONUS_KENO(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DIRECT_6_BONUS_KENO, value);
        editor.commit();
    }


    public String getPERM_2_BONUS_KENO() {
        return sharedPreferences.getString(PERM_2_BONUS_KENO, "");
    }

    public void setPERM_2_BONUS_KENO(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PERM_2_BONUS_KENO, value);
        editor.commit();
    }

    public String getPERM_3_BONUS_KENO() {
        return sharedPreferences.getString(PERM_3_BONUS_KENO, "");
    }

    public void setPERM_3_BONUS_KENO(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PERM_3_BONUS_KENO, value);
        editor.commit();
    }

    public String getPERM_4_BONUS_KENO() {
        return sharedPreferences.getString(PERM_4_BONUS_KENO, "");
    }

    public void setPERM_4_BONUS_KENO(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PERM_4_BONUS_KENO, value);
        editor.commit();
    }


}
