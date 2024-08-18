package com.skilrock.customui;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.skilrock.drawgame.five.FiveGameScreen;
import com.skilrock.utils.Utils;

public class CustomEditText extends EditText {
    private Fragment fragment;
    private boolean isKeyBoardShow = false;

    /**
     * @param context
     */
    public CustomEditText(Context context) {
        super(context);
        setCustomSelection();
    }

    /**
     * @param context
     * @param attrs
     */
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomSelection();
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomSelection();
    }

    @Override
    public void setSelection(int start, int stop) {
        // TODO Auto-generated method stub
        Utils.logPrint("setSelection");
        super.setSelection(start, stop);
    }


    @Override
    public void setSelection(int index) {
        // TODO Auto-generated method stub
        Utils.logPrint("selectionChanged");
        super.setSelection(index);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub

        setSelection(getText().length());
        super.onDraw(canvas);
    }

    @Override
    public boolean isSuggestionsEnabled() {
        return false;
    }


    private void setCustomSelection() {
        setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (fragment instanceof FiveGameScreen && isKeyBoardShow) {
                ((FiveGameScreen) fragment).setUnitPrice();
                return true;
            }
            return dispatchKeyEvent(event);
        }
        return dispatchKeyEvent(event);
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setIsKeyBoardShow(boolean isKeyBoardShow) {
        this.isKeyBoardShow = isKeyBoardShow;
    }
}