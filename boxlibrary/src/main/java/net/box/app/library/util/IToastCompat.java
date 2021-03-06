/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.util;

import android.content.Context;
import androidx.annotation.IntDef;
import androidx.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Box 2016年7月18日
 */
@SuppressWarnings("unused")
public class IToastCompat {

    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    private @interface Duration {
    }

    private static Toast mToast;
    private static int DEFAULT_DURATION = Toast.LENGTH_SHORT;

    private static OnCreateToastListener mCreateToastListener;

    public static <T> T showText(Context context, CharSequence text, @Duration int duration) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        T o;
        if (mCreateToastListener != null && (o = mCreateToastListener.onCreateToast(context, text)) != null) {
            return o;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), text, duration);
        } else {
            mToast.setText(text);
        }
        mToast.show();
        //noinspection unchecked
        return (T) mToast;
    }

    public static <T> T showText(Context context, CharSequence text) {
        return showText(context, text, DEFAULT_DURATION);
    }

    public static <T> T showText(Context context, @StringRes int res) {
        return showText(context, context.getString(res));
    }

    public static <T> T showText(Context context, @StringRes int res, Object... formatArgs) {
        return showText(context, context.getString(res, formatArgs));
    }

    public static void setDefaultDuration(int defaultDuration) {
        DEFAULT_DURATION = defaultDuration;
    }

    /**
     * 注意：设置这个监听以后就要自己实现显示toast的方法，当回调方法的返回值为空的时候，显示Toast
     *
     * @param createToastListener 监听
     */
    public static void setOnCreateToastListener(OnCreateToastListener createToastListener) {
        IToastCompat.mCreateToastListener = createToastListener;
    }

    @SuppressWarnings("WeakerAccess")
    public interface OnCreateToastListener {
        <T> T onCreateToast(Context context, CharSequence text);
    }

}
