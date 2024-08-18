package com.skilrock.customui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;

import com.skilrock.lms.ui.R;
import com.skilrock.utils.SuperscriptSpanAdjuster;

public class AmountTextView extends RobotoTextView {

    private float TEXT_SIZE;
    private float CURRENCY_SIZE = 1f;
    private float PENNY_SIZE = 1f;
    private boolean isTransaction = false;
    private String TEXT;
    private String CURRENCY_SYMBOL = "";
    private String NEG = "";
    private int CURRENCY_SYMBOL_COLOR;
    private SpannableString spannableString;

    public AmountTextView(Context context) {
        super(context);
    }

    public AmountTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AmountTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AmountTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        super.init(attrs);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AmountTextView);
            CURRENCY_SYMBOL = a.getString(R.styleable.AmountTextView_currency_symbol);
            CURRENCY_SIZE = a.getDimension(R.styleable.AmountTextView_currency_text_size, 1f);
            PENNY_SIZE = a.getDimension(R.styleable.AmountTextView_penny_text_size, 1f);
            isTransaction = a.getBoolean(R.styleable.AmountTextView_isTransaction, true);
            CURRENCY_SYMBOL_COLOR = a.getColor(R.styleable.AmountTextView_currency_text_color, getCurrentTextColor());

            if (CURRENCY_SYMBOL == null)
                CURRENCY_SYMBOL = "";

            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        TEXT_SIZE = getTextSize();
        TEXT = new SpannableString(getText()).toString();
        setMeasuredDimension((int) (getMeasuredWidth() + (TEXT_SIZE / 2) * CURRENCY_SYMBOL.length()), getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getText().toString().contains(",")) {
            TEXT = getText().toString().replace(",", "").trim();
        }
        if (getText().toString().contains(CURRENCY_SYMBOL)) TEXT.replace(CURRENCY_SYMBOL, "");
        spannableString = new SpannableString(TEXT);
        int start = 0;
        if (TEXT.contains("-")) {
            NEG = "-";
            TEXT = TEXT.replace("-", "");
            start = 1;
        } else {
            NEG = "";
        }
        if (checkDouble(TEXT)) {
            if (!CURRENCY_SYMBOL.equals("")) {
                if (CURRENCY_SIZE == 1f)
                    CURRENCY_SIZE = TEXT_SIZE;
                TEXT = NEG + CURRENCY_SYMBOL + TEXT;
                spannableString = new SpannableString(TEXT);
                if (!(NEG == "")) {
                    spannableString.setSpan(new ForegroundColorSpan(CURRENCY_SYMBOL_COLOR), start, CURRENCY_SYMBOL.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new SuperscriptSpanAdjuster(0.14), start, CURRENCY_SYMBOL.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new RelativeSizeSpan(0.8f), start, CURRENCY_SYMBOL.length() + 1, 0);
                } else {
                    spannableString.setSpan(new ForegroundColorSpan(CURRENCY_SYMBOL_COLOR), start, CURRENCY_SYMBOL.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new SuperscriptSpanAdjuster(0.14), start, CURRENCY_SYMBOL.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new RelativeSizeSpan(0.8f), start, CURRENCY_SYMBOL.length(), 0);

                }
            }
            String[] amount = TEXT.split("\\.");
            if (PENNY_SIZE == 1f)
                PENNY_SIZE = TEXT_SIZE;
            if (!isTransaction) {
                spannableString.setSpan(new RelativeSizeSpan(textProportion(PENNY_SIZE)), amount[0].length(), TEXT.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {

//                spannableString.setSpan(new SuperscriptSpanAdjuster(0.14), 2, 3, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//                spannableString.setSpan(new RelativeSizeSpan(0.8f), 2, 3, 0);


                spannableString.setSpan(new RelativeSizeSpan(0.8f), amount[0].length() + 1, TEXT.length(), 0);
                spannableString.setSpan(new SuperscriptSpanAdjuster(0.14), amount[0].length() + 1, TEXT.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            setText(spannableString);
        }
    }

    private boolean checkDouble(String text) {

        if (!text.contains("."))
            return false;
        try {
            Double.parseDouble(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setCurrencySymbol(String symbol) {
        CURRENCY_SYMBOL = symbol;
        requestLayout();
    }

    public void setCurrencySize(float size) {
        CURRENCY_SIZE = size;
        requestLayout();
    }

    public void setPennySize(float size) {
        PENNY_SIZE = size;
        requestLayout();
    }

    public void setCurrencySymbolColor(int color) {
        CURRENCY_SYMBOL_COLOR = color;
        requestLayout();
    }

    public void setText(String TEXT) {
        super.setText(TEXT);
        this.TEXT = TEXT;
        requestLayout();
    }

    private float textProportion(float size) {
        return size / TEXT_SIZE;
    }
}
