package com.skilrock.escratch;

import android.content.Context;
import android.util.DisplayMetrics;

import com.skilrock.config.VariableStorage;
import com.skilrock.escratch.bean.GameListDataBean;
import com.skilrock.escratch.bean.IGEUnfinishGameData;
import com.skilrock.lms.ui.R;

/**
 * Created by stpl on 8/8/2015.
 */
public class IGEService {


    public static String prepareGameListURL(Context context) {
        String path = "getGameList.action?";
        String serviceURL = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_ROOT_URL);
        String domainName = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_DOM_NAME);
        String merchantKey = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_MER_KEY);
        String secureKey = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_SEC_CODE);
        String preparedData = "domainName=" + domainName + "&merchantKey=" + merchantKey + "&secureKey=" + secureKey + "&isImageGeneration=Y&isHtml=N&isFlash=N&isThemeWise=N";
        return serviceURL + path + preparedData;
    }

    public static String prepareUnfinishGameListURL(Context context) {
        String path = "unFinishedGameList.action?";
        String serviceURL = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_ROOT_URL);
        String merchantKey = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_MER_KEY);
        String domainName = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_DOM_NAME);
        String playerId = VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.PLAYER_ID);
        String preparedData = "domainName=" + domainName + "&playerId=" + playerId + "&isImageGeneration=Y&merchantKey=" + merchantKey;
        return serviceURL + path + preparedData;
    }


    public static String prepareGameAssetsURL(Context context, IGEUnfinishGameData.UnfinishedGameList unfinishedGameList, GameListDataBean.Games games) {
        String path = "loadGameAssets.action?";
        String gameNo;
        if (unfinishedGameList == null)
            gameNo = games.getGameNumber();
        else
            gameNo = unfinishedGameList.getGameMaster().getGameNum() + "";
        String serviceURL = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_ROOT_URL);
        String domainName = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_DOM_NAME);
        String merchantKey = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_MER_KEY);
        String secureKey = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_SEC_CODE);
        String merchantSessionId = VariableStorage.GlobalPref.getStringData(context, VariableStorage.UserPref.SESSION_ID);
        String preparedData = "domainName=" + domainName + "&gameNumber=" + gameNo + "&merchantKey=" + merchantKey + "&secureKey=" + secureKey + "&screenSize=" + getDeviceResolution(context) + "&clientType=image_generation&merchantSessionId=" + merchantSessionId;
        return serviceURL + path + preparedData;
    }

    public static String prepareTryBuyURL(Context context, String gameMode, GameListDataBean.Games games) {
        String path = "loadPortalGame.action?";
        String serviceURL = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_ROOT_URL);
        String domainName = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_DOM_NAME);
        String merchantKey = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_MER_KEY);
        String secureKey = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_SEC_CODE);
        String currencyCode = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_SYMBOL).trim();
        String lang = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_LANG);
        String merchantSessionId = VariableStorage.GlobalPref.getStringData(context, VariableStorage.UserPref.SESSION_ID);
        String playerId, preparedData;

        if (gameMode.equalsIgnoreCase("buy")) {
            merchantSessionId = VariableStorage.GlobalPref.getStringData(context, VariableStorage.UserPref.SESSION_ID);
            playerId = VariableStorage.GlobalPref.getStringData(context, VariableStorage.UserPref.PLAYER_ID);
            preparedData = "gameNumber=" + games.getGameNumber() + "&gameMode=" + gameMode + "&domainName=" + domainName + "&merchantKey=" + merchantKey + "&secureKey=" + secureKey + "&screenSize=" + getDeviceResolution(context) + "&currencyCode=" + currencyCode + "&lang=" + lang + "&isImageGeneration=true&playerId=" + playerId + "&clientType=image_generation&merchantSessionId=" + merchantSessionId;
        } else
            preparedData = "gameNumber=" + games.getGameNumber() + "&gameMode=" + gameMode + "&domainName=" + domainName + "&merchantKey=" + merchantKey + "&secureKey=" + secureKey + "&screenSize=" + getDeviceResolution(context) + "&currencyCode=" + currencyCode + "&lang=" + lang + "&isImageGeneration=true&clientType=image_generation&merchantSessionId=" + merchantSessionId;

        return serviceURL + path + preparedData;
    }

    public static String prepareUnfinish(Context context, IGEUnfinishGameData.UnfinishedGameList unfinishedGameList) {
        String path = "loadUnfinishGame.action?";
        String serviceURL = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_ROOT_URL);
        String domainName = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_DOM_NAME);
        String merchantKey = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_MER_KEY);
        String playerId = VariableStorage.GlobalPref.getStringData(context, VariableStorage.UserPref.PLAYER_ID);
        String currencyCode = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_SYMBOL).trim();
        String preparedData = "gameNumber=" + unfinishedGameList.getGameMaster().getGameNum() + "&domainName=" + domainName + "&gameId=" + unfinishedGameList.getGameMaster().getGameId() + "&ticketNbr=" + unfinishedGameList.getTicketNbr() + "&date=" + unfinishedGameList.getDate() + "&playerId=" + playerId + "&clientType=image_generation&screenSize=" + getDeviceResolution(context) + "&currencyCode=" + currencyCode + "&merchantKey=" + merchantKey;
        return serviceURL + path + preparedData;
    }


    public static String gameFinishURL(Context context, String gameMode, IGEUnfinishGameData.UnfinishedGameList unfinishedGameList, GameListDataBean.Games games, String ticketNo) {
        String path = "/gamePlay/api/gameFinishCall.action?";
        String serviceURL = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_ROOT_URL);
        String domainName = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_DOM_NAME);
        String merchantKey = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_MER_KEY);
        String secureKey = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_SEC_CODE);
        String currencyCode = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_SYMBOL).trim();
        String lang = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_LANG);
        String playerId = VariableStorage.GlobalPref.getStringData(context, VariableStorage.UserPref.PLAYER_ID);
        String merchantSessionId = VariableStorage.GlobalPref.getStringData(context, VariableStorage.UserPref.SESSION_ID);
        String preparedData;
        if (gameMode.equalsIgnoreCase("buy")) {
            playerId = VariableStorage.GlobalPref.getStringData(context, VariableStorage.UserPref.PLAYER_ID);
            preparedData = "gameNumber=" + games.getGameNumber() + "&gameMode=" + gameMode + "&domainName=" + domainName + "&merchantKey=" + merchantKey + "&secureKey=" + secureKey + "&screenSize=" + getDeviceResolution(context) + "&currencyCode=" + currencyCode + "&lang=" + lang + "&isImageGeneration=true&playerId=" + playerId + "&clientType=image_generation&merchantSessionId=" + merchantSessionId + "&ticketNbr=" + ticketNo;
        } else
            preparedData = "gameNumber=" + unfinishedGameList.getGameMaster().getGameNum() + "&gameMode=" + gameMode + "&domainName=" + domainName + "&merchantKey=" + merchantKey + "&secureKey=" + secureKey + "&screenSize=" + getDeviceResolution(context) + "&currencyCode=" + currencyCode + "&lang=" + lang + "&playerId=" + playerId + "&isImageGeneration=true&clientType=image_generation&ticketNbr=" + unfinishedGameList.getTicketNbr() + "&merchantSessionId=" + merchantSessionId;
        return serviceURL + path + preparedData;
    }

    public static String getDeviceResolution(Context context) {
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        int result = 0, navigationMenuHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            if (!ViewConfiguration.get(context).hasPermanentMenuKey()) {
//                int resId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
//                if (resId > 0) {
//                    navigationMenuHeight = context.getResources().getDimensionPixelSize(resId);
//                }
//            }
//        }
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels - result - (int) context.getResources().getDimension(R.dimen.main_header_height) - navigationMenuHeight;
        return width + "x" + height;

    }

}
