/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.util;

import android.content.Context;
import android.support.annotation.IntDef;
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

    public static Object showText(Context context, CharSequence text, @Duration int duration) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        Object o;
        if (mCreateToastListener != null && (o = mCreateToastListener.onCreateToast(context, text)) != null) {
            return o;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), text, duration);
        } else {
            mToast.setText(text);
        }
        mToast.show();
        return mToast;
    }

    public static Object showText(Context context, CharSequence text) {
        return showText(context, text, DEFAULT_DURATION);
    }

    public static Object showText(Context context, int res) {
        return showText(context, context.getString(res));
    }

    public static Object showText(Context context, int res, Object... formatArgs) {
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

    public interface OnCreateToastListener {
        Object onCreateToast(Context context, CharSequence text);
    }

}
