package com.skilrock.customui;

import android.graphics.drawable.AnimationDrawable;

public class MyAnimDrawable extends AnimationDrawable {
    public interface IAnimationFinishListener {
        void onAnimationFinished();
    }

    private boolean finished = false;
    private IAnimationFinishListener animationFinishListener;

    public IAnimationFinishListener getAnimationFinishListener() {
        return animationFinishListener;
    }

    public void setAnimationFinishListener(IAnimationFinishListener animationFinishListener) {
        this.animationFinishListener = animationFinishListener;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public boolean selectDrawable(int idx) {
        boolean ret = super.selectDrawable(idx);

        if ((idx != 0) && (idx == getNumberOfFrames() - 1)) {
            if (!finished) {
                finished = true;
                if (animationFinishListener != null) animationFinishListener.onAnimationFinished();
            }
        }

        return ret;
    }
}
