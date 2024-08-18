package com.roomorama.caldroid;

import android.view.View;

import java.util.Date;

/**
 * Created by stpl on 7/15/2015.
 */
public abstract class OnCalenderButtonListener {
    public abstract void onClick(Date v, int date, String day, String month, int year);
}
