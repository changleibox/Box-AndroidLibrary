/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.helper;

import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;

/**
 * Created by box on 2017/4/16.
 * <p>
 * 倒计时
 */

public class ICountDownHelper extends CountDownTimer {

    private static final long INTERVAL = 1000L;

    @Nullable
    private Callback mCallback;

    /**
     * @param millisInFuture The number of millis in the future from the call
     *                       to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                       is called.
     */
    public ICountDownHelper(long millisInFuture, @Nullable Callback callback) {
        super(millisInFuture, INTERVAL);
        this.mCallback = callback;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (mCallback != null) {
            mCallback.onTick(DateFormat.format("mm:ss", millisUntilFinished), millisUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        if (mCallback != null) {
            mCallback.onFinish();
        }
    }

    public interface Callback {
        void onTick(CharSequence time, long millisUntilFinished);

        void onFinish();
    }
}
