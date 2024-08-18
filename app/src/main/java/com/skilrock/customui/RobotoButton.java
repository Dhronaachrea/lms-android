package com.skilrock.customui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import com.skilrock.lms.ui.R;

/**
 * Created by stpl on 7/3/2015.
 */
public class RobotoButton extends Button {
    public RobotoButton(Context context) {
        super(context);
    }

    public RobotoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RobotoButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RobotoButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RobotoButton);
            String fontNumber = a.getString(R.styleable.RobotoButton_typeface);
            String fontName = null;
            if (fontNumber!=null) {
                switch (fontNumber){
                    case "0": fontName = "ROBOTO-BOLD.TTF";break;
                    case "1": fontName = "ROBOTO-MEDIUM.TTF";break;
                    case "2": fontName = "ROBOTO-REGULAR.TTF";break;
                    case "3": fontName = "ROBOTO-LIGHT.TTF";break;
                    case "4": fontName = "ROBOTO-THIN.TTF";break;
                }
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }
}
