package com.skilrock.utils;

import com.skilrock.config.VariableStorage;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class AmountFormat {
    public static String getAmountFormatForMobile(double amount) {
//        DecimalFormat df = new DecimalFormat("###0.00");
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
        df.applyPattern("###0.00");
        // System.out.println(df.format(amount));
        return df.format(amount);
    }

    public static String getAmountFormatForMobileDecimal(double amount) {
        return String.format("%.2f", amount);
    }

    public static String getAmountFormatTxn(double amount) {
//        DecimalFormat df = DecimalFormat("#,##0.00");
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
        df.applyPattern("#,##0.00");
        // System.out.println(df.format(amount));
        return df.format(amount);
    }

    public static String getAmountFormatForTwoDecimal(String amount) {
//        DecimalFormat df = new DecimalFormat("###0.00");
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
        df.applyPattern("###0.00");
        // System.out.println(df.format(amount));
        try {
            return df.format(Double.parseDouble(amount));
        } catch (Exception e) {
            e.printStackTrace();
            return amount;
        }

    }

    public static String getCorrectAmountFormat(String amount) {
//        DecimalFormat df = new DecimalFormat("###0.00");
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
        df.applyPattern("###0.00");
        // System.out.println(df.format(amount));
        String amountText = amount;
        try {
            if (amountText.contains(",")) {
                amountText = amountText.replace(",", "").trim();
            }
            String res = df.format(Double.parseDouble(amountText));
            if (res.contains(",")) {
                res = res.replace(",", "");
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return amountText;
        }

    }


    public static String getAmountFormatForSingleDecimal(double amount) {
//        DecimalFormat df = new DecimalFormat("###0.0");
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
        df.applyPattern("###0.0");
        // System.out.println(df.format(amount));
        return df.format(amount);
    }

    public static double fmtToTwoDecimal(double number) {
        return Math.round((number * 100)) / 100.0;

    }

    public static String getCurrentAmountFormatForMobile(double amount) {
//        DecimalFormat df = new DecimalFormat("###0.00");
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
        df.applyPattern("###0.00");
        // System.out.println(df.format(amount));
        return df.format(amount);
    }

    public static double roundDrawTktAmt(double mrp) {
        if (mrp <= 10.0) {
            return (double) (long) (mrp * 2 + 0.5) / 2;
        } else {
            return (double) (long) mrp;
        }
    }

    public static int getBetAmountMultiple(double betAmount, double unitPrice) {

        return (int) ((betAmount * 10000) / (unitPrice * 10000));

    }

    public static void main(String[] args) {
        // double ticketPrice = 1.75;
        // double newPrice = roundDrawTktAmt(ticketPrice);
        //
        // double ticketPrice1 = 0.1;
        // System.out.println(roundDrawTktAmt(Double.parseDouble(".")));
        // System.out.println(new BigDecimal("1.0").remainder(new
        // BigDecimal("0.1000000")));
        // System.out.println(new BigDecimal("0.3").divide(new
        // BigDecimal("0.1")));

        Pattern pattern = Pattern
                .compile("^[+a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,4}[.a-zA-z]{0,4}$");
        // Pattern pattern= Patterns.EMAIL_ADDRESS;

        Utils.logPrint(pattern.matcher("abc@xyz.co.").matches() + "");
        /*
         * double amount=0.4; double amount1=232342.04;
		 * 
		 * System.out.println(getAmountFormatForMobile(amount));
		 * System.out.println(getAmountFormatForMobile(amount1));
		 * System.out.println(getAmountFormatForMobile(amount));
		 * System.out.println(amount1);
		 */

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static int getDecimalCount(double value) {
        String values = value + "";
        int length = 0;
        if (values.contains(".0"))
            return 0;

        length = values.substring(values.indexOf('.') + 1).length();
        return length == 0 ? 1 : length;
    }


}
