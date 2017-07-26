/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.helper;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.izhuo.app.library.IApplication;
import net.izhuo.app.library.util.IAppUtils;
import net.izhuo.app.library.util.IDeviceUuidFactory;

import java.util.Locale;

/**
 * Created by Box on 16/12/3.
 * <p>
 * 应用辅助类
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class IAppHelper {

    private static final IAppHelper instance = new IAppHelper();
    private static final Handler handler = new Handler(Looper.getMainLooper());

    private Locale mLocale = Locale.getDefault();

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

    public static Context getContext() {
        IApplication instance = IApplication.getInstance();
        if (instance == null) {
            throw new NullPointerException("请在Application.onCreate()调用IAppHelper.setContext(Context)，或者继承IApplication.");
        }
        return instance.getApplicationContext();
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

    public static <T> T getSystemService(String name) {
        //noinspection unchecked
        return (T) getContext().getSystemService(name);
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

    public static float applyDimension(int unit, float value) {
        return TypedValue.applyDimension(unit, value, getResources().getDisplayMetrics());
    }

    public static float applyDimensionDp(float value) {
        return applyDimension(TypedValue.COMPLEX_UNIT_DIP, value);
    }

    public static float applyDimensionSp(float value) {
        return applyDimension(TypedValue.COMPLEX_UNIT_SP, value);
    }

    public static LayoutInflater getInflater() {
        return LayoutInflater.from(getContext());
    }

    public static <T extends View> T inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot) {
        //noinspection unchecked
        return (T) getInflater().inflate(resource, root, attachToRoot);
    }

    public static <T extends View> T inflate(@LayoutRes int resource, @Nullable ViewGroup root) {
        return inflate(resource, root, root != null);
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static String[] getStringArray(@ArrayRes int resId) {
        return getResources().getStringArray(resId);
    }

    public static int getDimensionPixelSize(@DimenRes int id) {
        return getResources().getDimensionPixelSize(id);
    }

    public static int getColor(@ColorRes int id) {
        //noinspection deprecation
        return getResources().getColor(id);
    }

}
