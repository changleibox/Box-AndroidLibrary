package com.daimajia.slider.library.Tricks;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class IFixedSpeedScroller extends Scroller {

    private int mDuration = 1000;

    public IFixedSpeedScroller(Context context) {
        super(context);
    }

    public IFixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public IFixedSpeedScroller(Context context, Interpolator interpolator, int period){
        this(context,interpolator);
        mDuration = period;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}
