/**
 * Copyright  All right reserved by IZHUO.NET.
 */
package net.izhuo.app.library.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import net.izhuo.app.library.view.IProgress;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Box
 *         <p>
 *         2016年7月18日
 */
public class IProgressCompat {

    private static final Map<Context, IProgress> PROGRESS_MAP = Collections.synchronizedMap(new HashMap<Context, IProgress>());

    private static IProgress.Theme mTheme = IProgress.Theme.Black;

    public static IProgress showLoad(@NonNull Context context, CharSequence message) {
        IProgress progress = null;
        try {
            progress = PROGRESS_MAP.get(context);
            if (progress == null) {
                progress = new IProgress(context);
                progress.setCancelable(true);
                progress.setCanceledOnTouchOutside(false);
            }
            progress.setTheme(mTheme);
            progress.setMessage(message);
            PROGRESS_MAP.put(context, progress);
        } catch (Exception ignored) {
        }
        return progress;
    }

    public static IProgress showLoad(Context context, int messageResId) {
        return showLoad(context, messageResId == -1 ? null : context.getString(messageResId));
    }

    public static IProgress showLoad(Context context) {
        return showLoad(context, -1);
    }

    public static IProgress showLoad(Context context, IProgress.Theme theme, CharSequence message) {
        IProgress progress = showLoad(context, message);
        if (progress != null) {
            progress.setTheme(theme);
        }
        return progress;
    }

    public static IProgress showLoad(Context context, IProgress.Theme theme, int messageResId) {
        return showLoad(context, theme, messageResId == -1 ? null : context.getString(messageResId));
    }

    public static IProgress showLoad(Context context, IProgress.Theme theme) {
        return showLoad(context, theme, -1);
    }

    public static void loadDismiss(@NonNull Context context) {
        try {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                if (activity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())) {
                    return;
                }
            }
            IProgress progress = PROGRESS_MAP.get(context);
            if (progress != null) {
                progress.dismiss();
            }
        } catch (Exception ignored) {
        }
    }

    public static void onContextDestroy(@NonNull Context context) {
        loadDismiss(context);
        PROGRESS_MAP.remove(context);
    }

    /**
     * 请在第一次显示之前调用，否则无效
     *
     * @param theme {@link net.izhuo.app.library.view.IProgress.Theme} 默认为黑色主题
     */
    public static void setTheme(IProgress.Theme theme) {
        IProgressCompat.mTheme = theme;
    }
}
