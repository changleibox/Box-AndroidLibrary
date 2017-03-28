/*
 * Copyright © All right reserved by CHANGLEI.
 */

package net.izhuo.app.library.helper;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.util.TypedValue;

import net.izhuo.app.library.util.IAppUtils;
import net.izhuo.app.library.util.IDeviceUuidFactory;

import java.util.Locale;

/**
 * Created by Box on 16/12/3.
 * <p>
 * 应用辅助类
 */
public class IAppHelper {

    private static final IAppHelper instance = new IAppHelper();
    private static final Handler handler = new Handler(Looper.getMainLooper());

    private Locale mLocale = Locale.getDefault();
    private Context mContext;

    private IAppHelper() {
    }

    public static IAppHelper getInstance() {
        return instance;
    }

    public static void setLocale(Locale locale) {
        IAppUtils.setLocale(getContext(), locale);
        instance.mLocale = locale;
        Locale.setDefault(locale);
    }

    public static Locale getLocale() {
        return instance.mLocale;
    }

    public static void setContext(Context context) {
        instance.mContext = context;
    }

    public static Context getContext() {
        if (instance.mContext == null) {
            throw new NullPointerException("请在Application.onCreate()调用IAppHelper.setContext(Context)，或者继承IApplication.");
        }
        return instance.mContext;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static void runOnUiThread(Runnable runnable) {
        getHandler().post(runnable);
    }

    public static String getDeviceId(Context context) {
        return new IDeviceUuidFactory(context).getDeviceUuid().toString();
    }

    public static Object getSystemService(String name) {
        return getContext().getSystemService(name);
    }

    public static String getString(@StringRes int resId) {
        return getContext().getString(resId);
    }

    public static String getString(@StringRes int resId, Object... formatArgs) {
        return getContext().getString(resId, formatArgs);
    }

    public static float applyDimension(int unit, int value) {
        return TypedValue.applyDimension(unit, value, getContext().getResources().getDisplayMetrics());
    }

}
