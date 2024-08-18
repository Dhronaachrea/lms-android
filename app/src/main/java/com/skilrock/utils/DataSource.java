package com.skilrock.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.json.JSONObject;

import com.skilrock.bean.ActiveGameList;
import com.skilrock.bean.BetTypeBean;
import com.skilrock.bean.KenoBean;
import com.skilrock.bean.PanelData;
import com.skilrock.bean.WinningResultList;

public class DataSource {
    public static boolean isBankOkay;
    public static ArrayList<BetTypeBean> betTypeBean;
    public static int[] numbers = new int[90];
    public static int[] numbersForSix = new int[36];
    public static String deviceName = "Android";
    public static String deviceType = "Phone";
    public static JSONObject gamesJson[];
    public static ArrayList<ActiveGameList> activeGameLists;
    public static ArrayList<WinningResultList> winnersLists;
    public static String gameName;
    public static String selectedLang;
    public static String gameDevName;
    public static TreeMap<String, String> bankListBeans;
    public static String sportsListJson = "{\"errorMsg\":\"\",\"isSuccess\":true,\"sportsLotteryData\":{\"gameData\":[{\"maxEventCount\":1,\"gameDisplayName\":\"Soccer\",\"tktMaxAmt\":1000,\"gameId\":1,\"minEventCount\":1,\"gameDevname\":\"SOCCER\",\"tktThresholdAmt\":250,\"gameTypeData\":[{\"gameTypeDevName\":\"soccer12\",\"gameTypeId\":2,\"drawData\":{\"drawId\":13,\"currentData\":[{\"eventData\":[{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":9},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":10},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":11},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":12},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":13},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":14},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":15},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":16},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":17},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":69},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":71},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":72}]}],\"drawDisplayString\":\"Thu,Jan-8\",\"drawDateTime\":\"Thu 08 Jan 2015 07:30 PM\",\"drawNumber\":13,\"ftg\":\"2015-01-06 13:00:00\"},\"eventType\":\"[H, D, A]\",\"gameTypeDisplayName\":\"Soccer 12\",\"gameTypeMaxBetAmtMultiple\":100,\"gameTypeUnitPrice\":1},{\"gameTypeDevName\":\"soccer8\",\"gameTypeId\":1,\"drawData\":{\"drawId\":7,\"currentData\":[{\"eventData\":[{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":9},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":10},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":11},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":12},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":13},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":14},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":15},{\"eventDisplayHome\":\"West Broomwich Albion\",\"eventDisplayAway\":\"Manchester United\",\"eventLeague\":\"Africal Premier League\",\"eventVenue\":\"Madagascar\",\"eventDate\":\"14:30, 10 Sep 2014\",\"eventId\":16}]}],\"drawDisplayString\":\"Fri,Nov-14\",\"drawDateTime\":\"Fri 14 Nov 2014 08:00 PM\",\"drawNumber\":7,\"ftg\":\"2014-11-13 20:00:00\"},\"eventType\":\"[H, D, A]\",\"gameTypeDisplayName\":\"Soccer 8\",\"gameTypeMaxBetAmtMultiple\":100,\"gameTypeUnitPrice\":1}]}]}}";
    // public static String sportsListJson =
    // "{\"errorMsg\":\"\",\"isSuccess\":true,\"sportsLotteryData\":{\"gameData\":[{\"maxEventCount\":3,\"gameDisplayName\":\"Soccer\",\"tktMaxAmt\":10000,\"gameId\":1,\"minEventCount\":1,\"gameDevname\":\"SOCCER\",\"tktThresholdAmt\":100,\"gameTypeData\":[{\"gameTypeDevName\":\"soccer8\",\"gameTypeId\":1,\"drawData\":{\"drawId\":19,\"currentData\":[{\"eventData\":[{\"eventDisplayString\":\"Caracas VS Zulia\",\"eventId\":123},{\"eventDisplayString\":\"Alianza Petrolera VS Santa Fe\",\"eventId\":124},{\"eventDisplayString\":\"AC Milan VS Atl Madrid\",\"eventId\":125},{\"eventDisplayString\":\"Audax VS Bragantino\",\"eventId\":126},{\"eventDisplayString\":\"Bangu VS Vasco da Gama\",\"eventId\":127},{\"eventDisplayString\":\"Nottingham VS Leicester\",\"eventId\":128},{\"eventDisplayString\":\"Gillingham VS Sheffield Utd\",\"eventId\":129},{\"eventDisplayString\":\"Arsenal VS Bayern Munich\",\"eventId\":130}]}],\"drawDisplayString\":\"National Weekly\",\"drawDateTime\":\"Thu 27 Feb 2014 11:00 PM\",\"drawNumber\":19,\"ftg\":\"2014-02-27 23:00:00\"},\"eventType\":\"[H, D, A]\",\"gameTypeDisplayName\":\"Soccer 8\",\"gameTypeMaxBetAmtMultiple\":100,\"gameTypeUnitPrice\":1.1}]}]}}";
    public static int maxBetAmtMul;
    public static double betUnitPriceDouble;

    public static class BankData {
        public static String accountNo;
        public static String accountName;
        public static String bankName;
        public static String bankID;
        public static String amount;
    }

    public static class Login {
        public static String username;
        public static String currenctSymbol = "$";
        public static int currentBalance;
        public static String companyName;
        public static String sample;
        public static boolean isSessionValid;
        public static boolean isPurSuccess;
        public static String bannerUrl;
        public static String merchantCode;
        public static String sessionID;

        // public static String password;
        // public static String drawName;
    }

    public static class SportsLottery {
        public static String gameTypeDisplayName;
        public static String drawDisplayString;
        public static String drawDateTime;
        public static int gameId;
        public static int gameTypeId;
        public static int drawId;

    }

    public static class Ghana {
        public static final int DIRECT1 = 1;
        public static final int DIRECT2 = 2;
        public static final int DIRECT3 = 3;
        public static final int DIRECT4 = 4;
        public static final int DIRECT5 = 5;
        public static final int PERM2 = 6;
        public static final int PERM3 = 7;
        public static final int BANKER = 8;
        public static final int BANKER_1_AGAINST_ALL = 9;
        public static final int DC_Direct2 = 9;
        public static final int DC_Perm2 = 10;
        public static final int DC_Direct3 = 11;
        public static final int DC_Perm3 = 12;
        public static final int PERM1 = 13;
        public static final int PERM6 = 14;
        public static final int DIRECT6 = 15;
        public static String[] listIndoor;
        public static int[] panelCount;
    }

    public static class Keno {
        public static String[] listIndoor = {"Perm2", "Perm3", "Direct1",
                "Direct2", "Banker1AgainstAll", "Banker", "Direct3", "Direct4",
                "Direct5", "DC-Direct2", "DC-Perm2", "DC-Direct3", "DC-Perm3"};
        public static String[] list = {"Perm2", "Perm3", "Direct1", "Direct2",
                "Banker1AgainstAll", "Banker", "Direct3", "Direct4", "Direct5",
                "DC-Direct2", "DC-Perm2", "DC-Direct3", "DC-Perm3"};

        public static final int DIRECT1 = 2;
        public static final int DIRECT2 = 3;
        public static final int DIRECT3 = 6;
        public static final int DIRECT4 = 7;
        public static final int DIRECT5 = 8;
        public static final int PERM2 = 0;
        public static final int PERM3 = 1;
        public static final int BANKER = 5;
        public static final int BANKER_1_AGAINST_ALL = 4;
        public static final int DC_Direct2 = 9;
        public static final int DC_Perm2 = 10;
        public static final int DC_Direct3 = 11;
        public static final int DC_Perm3 = 12;

        public static double[] unitPrice = new double[list.length];

        public static double[] unitPriceIndoor = new double[listIndoor.length];

        public static int gameId;
        public static int freezeTime;
        public static int ticketExpiry;
        public static String gameDispName;
        public static String gameDevName;
        public static long jackpotLimit;

        public static int[] panelCount = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0};

        public static int[] panelCountIndoor = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0};

        public static KenoBean kenoBean;
        public static ArrayList<PanelData> panelData;
        public static double totalPurchaseAmt = 0;
        public static int numbersSelected = 0;

        public static boolean belowLine;
        public static int drawId;
    }

    public static class Fortune {
        // public static final int[] gridValues = { R.drawable.a, R.drawable.b,
        // R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f,
        // R.drawable.g, R.drawable.h, R.drawable.i, R.drawable.j };

        public static final int CAPRICORN = 0;
        public static final int AQUARIUS = 1;
        public static final int PISCES = 2;
        public static final int ARIES = 3;
        public static final int TAURUS = 4;
        public static final int GEMINI = 5;
        public static final int CANCER = 6;
        public static final int LEO = 7;
        public static final int VIRGO = 8;
        public static final int LIBRA = 9;
        public static final int SCORPIO = 10;
        public static final int SAGITTARIUS = 11;

        public static int[] unitPrice;

        public static int gameId;
        public static int freezeTime;
        public static int ticketExpiry;
        public static String gameDispName;
        public static String gameDevName;
        public static long jackpotLimit;

        public static int[] fortunePanels = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static class Reports {
        public static final int OLD_WITHDRAWAL = 1;
        public static final int OLD_TRANSACTION = 2;
        public static final int PURCHASED_TICKETS = 3;
    }

    public static void resetVariables() {
        Keno.kenoBean = null;
        if (Keno.panelData != null)
            Keno.panelData.clear();

        Keno.panelData = null;
        Keno.totalPurchaseAmt = 0.0;
        Keno.numbersSelected = 0;

        for (int i = 0; i < DataSource.numbers.length; i++)
            DataSource.numbers[i] = 0;
        for (int i = 0; i < Keno.panelCount.length; i++)
            Keno.panelCount[i] = 0;
        for (int i = 0; i < Keno.panelCountIndoor.length; i++)
            Keno.panelCountIndoor[i] = 0;
        Keno.belowLine = false;
    }

}