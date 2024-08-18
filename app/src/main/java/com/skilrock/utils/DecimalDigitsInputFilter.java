package com.skilrock.utils;

import android.text.InputFilter;
import android.text.Spanned;

import com.skilrock.utils.Utils;

/**
 * Input filter that limits the number of decimal digits that are allowed to be
 * entered.
 */
public class DecimalDigitsInputFilter implements InputFilter {

    private final int decimalDigits;
    private int length;
    private final int lengthInit;
    private int counter = 0;

    /**
     * Constructor.
     *
     * @param decimalDigits maximum decimal digits
     */
    public DecimalDigitsInputFilter(int length, int decimalDigits) {
        this.decimalDigits = decimalDigits;
        this.length = length;
        lengthInit = length;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int dotPos = -1;
        int len = dest.length();
        Utils.consolePrint("length is " + len);
        Utils.consolePrint("Counter is " + counter++);

//        if (source.toString().length() > 1) {
//            if (source.toString().contains(".")) {
//                if (source.toString().split("\\.")[1].length() > decimalDigits)
//                    return "";
//            } else {
//                if (source.toString().split("\\.")[0].length() > lengthInit)
//                    return "";
//            }
//        }

        if (!(dest.toString()).contains(".")) {
            length = lengthInit;
        }

        if (source.toString().length() > 1 || (source.toString().length() == 1 && dest.toString().contains("."))) {
            Utils.consolePrint("here we come");
            if (source.toString().contains(".") || (source.toString().length() == 1 && dest.toString().contains(".")))
                length = lengthInit + decimalDigits + 1;
        }

        for (int i = 0; i < len; i++) {
            char c = dest.charAt(i);
            if (c == '.' || c == ',') {
                dotPos = i;
                break;
            }
        }

        if (len >= length || len <= lengthInit && source.toString().equals(".")) {
            if (source.toString().equals(".") && dotPos == -1) {
                length = lengthInit + decimalDigits + 1;
            } else {
//                if (!(dest.toString().indexOf(".") == 0))
                return "";


            }
        }

        if (dotPos >= 0) {

            // protects against many dots
            if (source.toString().equals(".") || source.toString().equals(",")) {
                return "";
            }
            // if the text is entered before the dot
            if (dend <= dotPos) {
                return null;
            }
            if (len - dotPos > decimalDigits) {
                return "";
            }
        }
        return null;
    }
}