package com.skilrock.customui;

import java.util.Locale;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.skilrock.config.Config;
import com.skilrock.lms.ui.R;

public class CustomTextView extends TextView {
    private Context context;

    public enum TextStyles {
        THIN, LIGHT, MEDIUM, BOLD
    };

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(attrs);

    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public CustomTextView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    @Override
    public boolean isInEditMode() {
        // TODO Auto-generated method stub
        return true;
    }

    private void init() {
        setTypeface(Config.globalTextFont);
    }

    private void init(AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.CustomTextView);
        String value = array.getString(R.styleable.CustomTextView_text_style);
        if (value == null) {
            setTypeface(Config.globalTextFont);
        } else {
            switch (array.getString(R.styleable.CustomTextView_text_style)) {
                case "0":
                    setTextStyle(TextStyles.THIN);
                    break;
                case "1":
                    setTextStyle(TextStyles.LIGHT);
                    break;
                case "2":
                    setTextStyle(TextStyles.MEDIUM);
                    break;
                case "3":
                    setTextStyle(TextStyles.BOLD);
                    break;
                default:
                    setTypeface(Config.globalTextFont);
                    break;
            }
            array.recycle();
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if ((getId() == R.id.header_text) || getId() == R.id.header_sub_text) {
            super.setText(text.toString().toUpperCase(Locale.ENGLISH),
                    type);
        } else {
            super.setText(text, type);
        }
    }

    // @Override
    // protected void onDraw(Canvas canvas) {
    // int yOffset = getHeight() - getBaseline();
    // canvas.translate(0, -yOffset + 2);
    // canvas.translate(0, yOffset);
    // super.onDraw(canvas);
    // }

    public void setTextStyle(TextStyles textStyles) {
        switch (textStyles) {
            case THIN:
                setTypeface(Typeface.createFromAsset(context.getAssets(),
                        "ROBOTO-THIN.TTF"));
                break;

            case LIGHT:
                setTypeface(Typeface.createFromAsset(context.getAssets(),
                        "ROBOTO-LIGHT.TTF"));
                break;

            case MEDIUM:
                setTypeface(Typeface.createFromAsset(context.getAssets(),
                        "ROBOTO-MEDIUM.TTF"));
                break;

            case BOLD:
                setTypeface(Typeface.createFromAsset(context.getAssets(),
                        "ROBOTO-BOLD.TTF"));
                break;

        }
    }
}