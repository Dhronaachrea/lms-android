package com.skilrock.lms.tracker;

/**
 * Created by stpl on 9/6/2015.
 */
public interface Fields {


    String MY_ACCCOUNT = "MY Account";
    String MY_TICKET = "My Tickets";
    String MY_TXN = "Transaction Report";
    String MY_WITHDRAWAL = "WithDrawal Report";

    String DEPOSIT = "Deposit";
    String WITHDRAWAL = "Withdrawal";

    String VPAYMENT = "Vpayment";
    String VISA_MASTERCARD = "Visa/MasterCard";
    String ECO_CASH = "EcoCash";
    String TELECASH = "TeleCash";
    String ONE_WALLET = "OneWallet";
    String AFRICA_OUTLET = "Africa Outlet";


    public static interface Category {
        String UX = "UX";
        String BONUS_BET_TYPE = "Bonus Bet Type";
        String BONUS_PURCHASE = "Bounus Purchase";
        String BONUS_GAME = Screen.BONUS_GAME;
        String FIVE_GAME = Screen.FIVE_GAME;
        String FAST_GAME = Screen.FAST_GAME;
        String TWELVE_GAME = Screen.TWELVE_GAME;
        String TEN_BY_THIRTY_GAME=Screen.TEN_THIRTY_GAME_SCREEN;

        String BONUS_KENO_GAME=Screen.BONUS_KENO_GAME;



        String FAST_PURCHASE = "Fast Purchase";

        String FIVE_PURCHASE = "Five Purchase";

        String TWELVE_PURCHASE = "Twelve Purchase";
        String TEN_BY_THIRTY_PURCHASE = "Ten by thirty Purchase";

        String BONUS_KENO_GAME_PURCHASE = "bonus keno game purchase";

        String FIVE_BET_TYPE = "Five Bet Type";
        String TWELVE_BET_TYPE = "Twelve Bet Type";
        String INITIAL_DEPOSIT = "Initial Deposit";
        String PAYMENT_GATEWAY = Screen.PAYMENT_GATEWAY;
        String DEPOSIT = "Deposit";
        String LOGIN = Screen.LOGIN;
        String DRAW_GAME = Screen.DRAW_GAME;
        String SCRATCH_CARD = "Scratch Card";
        String INSTANT_GAME = "Instant Game";
        String SPORTS_LOTTERY = "Sports Lottery";
        String REGISTER = Screen.REGISTER;
        String SL_MATCH_LIST = "SL Soccer Match List";
        String SL_CHECK_RESULT = "SL Soccer Check Result";
        String SL_SOCCER = "SL Soccer";
        String SL_PURCHASE = "SL Soccer Purchase";
        String SL_TICKET = "SL_Soccer_Ticket";
        String PROFILE_EDIT = "Profile Edit";
        String IGE_NEW_GAME = "IGE Game List";
        String IGE_UNFINISHED_GAME = "IGE Unfinish List";
        String IGE_DIALOG = "IGE Dialog";
        String IGE_PLAY_CASH = "IGE Play Cash";
        String IGE_TRY_FREE = "IGE Try Free";
        String IGE_UNFINISHED = "IGE Unfinished";
        String ACCOUNT = "account";
        String CHANGE_PASS = "change pass";
        String APP_TOUR = "app tour";
        String LOCATE_RETAILER = "locate retailer";
        String LOGOUT = "logout";
        String LOCATE_RETAILER_VIEW = "locate retailer view";
        String BUTTON_DEPOSIT = "deposit click";
        String BUTTON_TICKET = "ticket click";
        String BUTTON_TRANSACTION = "transaction click";
        String BUTTON_PROFILE = "profile click";
        String SPLASH = "splash activity";
        String TRACK_TICKET = "track ticket";
        String IGE_GAME_PLAY = "IGE Game Play";
        String IGE_GAME_PLAY_MANUAL = "IGE Manual Play";
        String IGE_GAME_PLAY_AUTOMATIC = "IGE Auto Play";
        String WITHDRAWAL = "Withdrawal";
        String MY_TICKET = "My Ticket Screen";
        String SL_FRAGMENT = "Sports Lottery Fragment";
        String GCMDIALOG = "Push Notification Ok button";
    }

    public static interface Action {
        String CLICK = "Click";
        String OPEN = "Open";
        String GET = "Get";
        String CLOSE = "Close";

        String IMG_LOAD = "Image Loading";
        String RESULT = "Result";

        String DROPDOWN = "Dropdown";
        String STATS = "Stats";
        String VERSION = "version check";
    }

    public static interface Screen {
        String SPLASH = "Splash";
        String APP_TOUR = "App Tour";
        String FIVE_GAME = "Five Game";
        String FAST_GAME = "Fast Game";
        String BONUS_GAME = "Bonus Game";
        String MAIN_SCREEN = "Home Screen";
        String EDIT_PROFILE = "Profile Edit";
        String SPORTS_LOTTERY = "Sports Lottery";
        String SPORTS_LOTTERY_SOCCER = "Sports Lottery Soccer";
        String SPORTS_LOTTERY_SOCCER_MATCH_LIST = SPORTS_LOTTERY_SOCCER + " Match List";
        String SPORTS_LOTTERY_SOCCER_CHECK_RESULT = SPORTS_LOTTERY_SOCCER + " Check Result";
        String SPORTS_LOTTERY_SOCCER_PURCHASE = SPORTS_LOTTERY_SOCCER + " Purchase";
        String SPORTS_LOTTERY_SOCCER_TICKET = SPORTS_LOTTERY_SOCCER + " Ticket";
        String SPORTS_LOTTERY_SOCCER_TRACK_TICKET = SPORTS_LOTTERY_SOCCER + " Track Ticket";
        String MY_ACCOUNT = "My Account";
        String MY_TICKET = "My Ticket";
        String MY_TRANSACTION = "My Transactions";
        String DRAW_GAME = "Draw Game";
        String INSTANT_GAME = "Instant Game";
        String INSTANT_GAME_LIST = Screen.INSTANT_GAME + " List";
        String LOCATE_RETAILER = "Locate Retailer";
        String LOGIN = "Login";
        String REGISTER = "Register";
        String IGE_DIALOG = Screen.INSTANT_GAME + " Dialog";
        String INSTANT_GAME_SCRATCH = Screen.INSTANT_GAME + " Scratch";
        String SCRATCH_CARDS = "Scratch Cards";


        String DGE_STAT = "Dge Stat";
        String DGE_RESULT = "Dge Result";

        String FIVE_GAME_SCREEN = "Five Game Screen";
        String FAST_GAME_SCREEN = "Fast Game Screen";
        String BONUS_GAME_SCREEN = "Bonus Game Screen";
        String TWELVE_GAME_SCREEN = "Twelve Game Screen";
        String TEN_THIRTY_GAME_SCREEN = "Ten by thirty Game Screen";

        String BONUS_KENO_GAME = "bonus keno game screen";

        String TICKET_DESC_ACTIVITY_SCREEN = "Ticket Disc Actiivity";

        String WITHDRAWAL_REPORT_SCREEN = "Withdrawal Resport Screen";

        String WITHDRAWAL_SCREEN = "Withdrawal Screen";

        String DEPOSITE_SCREEN = "Deposite Screen";

        String INITIAL_DEPOSITE_SCREEN = "Initial Deposite Screen";
        String DRAW_GAME_STATS = DRAW_GAME + " Stats";
        String DRAW_GAME_TICKET = DRAW_GAME + " Ticket";
        String TWELVE_GAME = "Twelve Game";
        String TEN_BY_THIRTY_GAME = "Ten by thirty Game";

        String DRAW_GAME_RESULT = DRAW_GAME + " Result";
        String PAYMENT_GATEWAY = "Payment Gateway";
        String MOBILE_MONEY = "Mobile Money";

        String INSTANT_GAME_PLAY = "IGE Game Play Screen";
        String DRAWER_BASE_ACTIVITY = "drawer_base_activity";

        String PROFILE_FRAGMENT = "profile fragment";
        String TRACK_TICKET = "track ticket";
        String GCMDIALOG = "Push Notification";
        String AFRICA_OUTLET_FRAGMENT = "Africa Outlets";
    }

    public static interface Label {
        String SUCCESS = "Success";
        String FAILURE = "Failure";
        String BONUS_DIRECT_6 = "Bonus Direct 6";
        String BONUS_PERM_6 = "Bonus Perm 6";

        String SAVE = "Save";
        String MAP = "Map View";
        String LIST = "List View";
        String SLE = Screen.SPORTS_LOTTERY;
        String DGE = Screen.DRAW_GAME;
        String IGE = Screen.INSTANT_GAME;
        String BUY = "Buy";
        String TRY = "Try";
        String V_PAYMENT = "V Payment";
        String VISA_MASTER_CARD = "Visa/Master Card";
        String ECO_CASH = "Eco Cash";
        String TELE_CASH = "Tele Cash";
        String ONE_WALLET = "One Wallet";
        String AFRICA_LOTTO_OUTLETS = "Africa Lotto Outlets";

        String BONUS_GAME_SCREEN = Category.BONUS_GAME + " Screen";
        String BONUS_GAME_STATS = Category.BONUS_GAME + " Stats";
        String BONUS_GAME_RESULT = Category.BONUS_GAME + " Result";
        String FIVE_GAME_SCREEN = Category.FIVE_GAME + " Screen";
        String FIVE_GAME_STATS = Category.FIVE_GAME + " Stats";
        String FIVE_GAME_RESULT = Category.FIVE_GAME + " Result";
        String FAST_GAME_SCREEN = Category.FAST_GAME + " Screen";
        String FAST_GAME_STATS = Category.FAST_GAME + " Stats";
        String FAST_GAME_RESULT = Category.FAST_GAME + " Result";
        String TWELVE_GAME_SCREEN = Category.TWELVE_GAME + " Screen";
        String TWELVE_GAME_STATS = Category.TWELVE_GAME + " Stats";
        String TWELVE_GAME_RESULT = Category.TWELVE_GAME + " Result";
        String TICKET = "Ticket";
        String PICK_NEW = "Pick New";
        String QUICK_PICK = "Quick Pick";
        String LAST_PICK = "Last Pick";
        String FAV_NO = "Fav Nos";


        String FIVE_PERM_1 = "Five Perm 1";
        String FIVE_PERM_2 = "Five Perm 2";
        String FIVE_PERM_3 = "Five Perm 3";

        String TWELVE_DIRECT_12 = "Twelve Direct 12";
        String TWELVE_FIRST_12 = "Twelve First 12";
        String TWELVE_LAST_12 = "Twelve Last 12";
        String TWELVE_ALL_ODD = "Twelve All Odd";
        String TWELVE_ALL_EVEN = "Twelve All Even";
        String TWELVE_ODD_EVEN = "Twelve Odd Even";
        String TWELVE_EVEN_ODD = "Twelve Even Odd";
        String TWELVE_JUMP_EVEN_ODD = "Twelve Jump Even Odd";
        String TWELVE_JUMP_ODD_EVEN = "Twelve Jump Odd Even";
        String TWELVE_PERM_12 = "Twelve Perm 12";
        String TWELVE_MATCH_10 = "Twelve Match 10";

        String TEN_BY_THIRTY_DIRECT_12 = "ten by thirty Direct 12";
        String TEN_BY_THIRTY_FIRST_12 = "ten by thirty First 12";
        String TEN_BY_THIRTY_LAST_12 = "ten by thirty Last 12";
        String TEN_BY_THIRTY_ALL_ODD = "ten by thirty All Odd";
        String TEN_BY_THIRTY_ALL_EVEN = "ten by thirty All Even";
        String TEN_BY_THIRTY_ODD_EVEN = "ten by thirty Odd Even";
        String TEN_BY_THIRTY_EVEN_ODD = "ten by thirty Even Odd";
        String TEN_BY_THIRTY_JUMP_EVEN_ODD = "ten by thirty Jump Even Odd";
        String TEN_BY_THIRTY_JUMP_ODD_EVEN = "ten by thirty Jump Odd Even";
        String TEN_BY_THIRTY_PERM_12 = "ten by thirty Perm 12";
        String TEN_BY_THIRTY_MATCH_10 = "ten by thirty Match 10";

        String AMOUNT = "Amount";
        String INVALID_AMOUNT = "Invalid Amount";
        String LOGIN = "login";
        String MY_ACCOUNT_ACTIVITY = "my account activity";
        String CHANGE_PASSWORD_ACTIVITY = "change password activity";
        String APP_TOUR_ACTIVITY = "app tour activity";
        String LOCATE_RETAILER_ACTIVITY = "locate retailer activity";
        String LOGOUT = "logout";
        String SCRATCH_CARD_GAMES = "scratch card games";
        String DEPOSIT = "deposit";
        String WITHDRAWAL = "withdrawal";
        String MY_TICKETS = "my tickets";
        String MY_TRANSACTION = "my transaction";
        String WITHDRAWAL_REPORT = "withdrawal report";
        String BONUS_GAME = "bonus game";
        String VERSION = "Version Check";
    }
}
