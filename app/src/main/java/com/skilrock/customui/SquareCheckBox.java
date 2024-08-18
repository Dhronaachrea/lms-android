package com.skilrock.customui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.skilrock.lms.ui.R;
import com.skilrock.utils.GlobalVariables;

/**
 * Created by stpl on 6/16/2015.
 */
public class SquareCheckBox extends CheckBox {

    private float textSize = 14;

    public SquareCheckBox(Context context) {
        super(context);
    }

    public SquareCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SquareCheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int square = Math.max(getMeasuredHeight(), getMeasuredWidth());
        textSize = square * 0.40f;
        textSize = GlobalVariables.pxToDp(textSize, getContext());
        setMeasuredDimension(square, square);
        init();
    }

    protected void init(AttributeSet attrs) {
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SquareCheckBox);
            String fontNumber = a.getString(R.styleable.SquareCheckBox_typeface);
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

    private void init(){
        setTextSize(textSize);
    }
}
