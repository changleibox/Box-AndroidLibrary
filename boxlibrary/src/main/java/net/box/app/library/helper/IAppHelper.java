/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.StringDef;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.box.app.library.IApplication;
import net.box.app.library.util.IAppUtils;
import net.box.app.library.util.IDeviceUuidFactory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

/**
 * Created by Box on 16/12/3.
 * <p>
 * 应用辅助类
 */
@SuppressWarnings({"unused", "WeakerAccess", "unchecked"})
public final class IAppHelper {

    @SuppressLint("InlinedApi")
    @StringDef({
            Context.POWER_SERVICE,
            Context.WINDOW_SERVICE,
            Context.LAYOUT_INFLATER_SERVICE,
            Context.ACCOUNT_SERVICE,
            Context.ACTIVITY_SERVICE,
            Context.ALARM_SERVICE,
            Context.NOTIFICATION_SERVICE,
            Context.ACCESSIBILITY_SERVICE,
            Context.CAPTIONING_SERVICE,
            Context.KEYGUARD_SERVICE,
            Context.LOCATION_SERVICE,
            //@hide: COUNTRY_DETECTOR,
            Context.SEARCH_SERVICE,
            Context.SENSOR_SERVICE,
            Context.STORAGE_SERVICE,
            Context.STORAGE_STATS_SERVICE,
            Context.WALLPAPER_SERVICE,
            Context.VIBRATOR_SERVICE,
            //@hide: STATUS_BAR_SERVICE,
            Context.CONNECTIVITY_SERVICE,
            //@hide: UPDATE_LOCK_SERVICE,
            //@hide: NETWORKMANAGEMENT_SERVICE,
            Context.NETWORK_STATS_SERVICE,
            //@hide: NETWORK_POLICY_SERVICE,
            Context.WIFI_SERVICE,
            Context.WIFI_AWARE_SERVICE,
            Context.WIFI_P2P_SERVICE,
            //@hide: LOWPAN_SERVICE,
            //@hide: WIFI_RTT_SERVICE,
            //@hide: ETHERNET_SERVICE,
            Context.NSD_SERVICE,
            Context.AUDIO_SERVICE,
            Context.FINGERPRINT_SERVICE,
            Context.MEDIA_ROUTER_SERVICE,
            Context.TELEPHONY_SERVICE,
            Context.TELEPHONY_SUBSCRIPTION_SERVICE,
            Context.CARRIER_CONFIG_SERVICE,
            Context.TELECOM_SERVICE,
            Context.CLIPBOARD_SERVICE,
            Context.INPUT_METHOD_SERVICE,
            Context.TEXT_SERVICES_MANAGER_SERVICE,
            Context.TEXT_CLASSIFICATION_SERVICE,
            Context.APPWIDGET_SERVICE,
            //@hide: VOICE_INTERACTION_MANAGER_SERVICE,
            //@hide: BACKUP_SERVICE,
            Context.DROPBOX_SERVICE,
            //@hide: DEVICE_IDLE_CONTROLLER,
            Context.DEVICE_POLICY_SERVICE,
            Context.UI_MODE_SERVICE,
            Context.DOWNLOAD_SERVICE,
            Context.NFC_SERVICE,
            Context.BLUETOOTH_SERVICE,
            //@hide: SIP_SERVICE,
            Context.USB_SERVICE,
            Context.LAUNCHER_APPS_SERVICE,
            //@hide: SERIAL_SERVICE,
            //@hide: HDMI_CONTROL_SERVICE,
            Context.INPUT_SERVICE,
            Context.DISPLAY_SERVICE,
            Context.USER_SERVICE,
            Context.RESTRICTIONS_SERVICE,
            Context.APP_OPS_SERVICE,
            Context.CAMERA_SERVICE,
            Context.PRINT_SERVICE,
            Context.CONSUMER_IR_SERVICE,
            //@hide: TRUST_SERVICE,
            Context.TV_INPUT_SERVICE,
            //@hide: NETWORK_SCORE_SERVICE,
            Context.USAGE_STATS_SERVICE,
            Context.MEDIA_SESSION_SERVICE,
            Context.BATTERY_SERVICE,
            Context.JOB_SCHEDULER_SERVICE,
            //@hide: PERSISTENT_DATA_BLOCK_SERVICE,
            //@hide: OEM_LOCK_SERVICE,
            Context.MEDIA_PROJECTION_SERVICE,
            Context.MIDI_SERVICE,
            Context.HARDWARE_PROPERTIES_SERVICE,
            //@hide: SOUND_TRIGGER_SERVICE,
            Context.SHORTCUT_SERVICE,
            //@hide: CONTEXTHUB_SERVICE,
            Context.SYSTEM_HEALTH_SERVICE,
            //@hide: INCIDENT_SERVICE,
            Context.COMPANION_DEVICE_SERVICE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ServiceName {
    }

    private static final IAppHelper instance = new IAppHelper();
    private static final Handler handler = new Handler(Looper.getMainLooper());

    private Locale mLocale = Locale.getDefault();

    private IAppHelper() {
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

    @SuppressLint("MissingPermission")
    @RequiresPermission(allOf = {Manifest.permission.READ_PHONE_STATE})
    public static String getDeviceId(Context context) {
        return new IDeviceUuidFactory(context).getDeviceUuid().toString();
    }

    public static <T> T getSystemService(@ServiceName @NonNull String name) {
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
        return ActivityCompat.getColor(getContext(), id);
    }

}
