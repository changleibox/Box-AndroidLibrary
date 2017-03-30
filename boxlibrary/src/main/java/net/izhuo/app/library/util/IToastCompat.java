/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Box 2016年7月18日
 */
@SuppressWarnings("unused")
public class IToastCompat {

    private static Toast mToast;

    private static OnCreateToastListener mCreateToastListener;

    public static Object showText(Context context, CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        Object o;
        if (mCreateToastListener != null && (o = mCreateToastListener.onCreateToast(context, text)) != null) {
            return o;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
        return mToast;
    }

    public static Object showText(Context context, int res) {
        return showText(context, context.getString(res));
    }

    public static Object showText(Context context, int res, Object... formatArgs) {
        return showText(context, context.getString(res, formatArgs));
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
