/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.box.app.library.util;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings({"unused", "HardwareIds", "MissingPermission"})
public class IDeviceUuidFactory {
    private static final String PREFS_FILE = "device_id.xml";
    private static final String PREFS_DEVICE_ID = "device_id";
    private static UUID uuid;

    @RequiresPermission(allOf = {Manifest.permission.READ_PHONE_STATE})
    public IDeviceUuidFactory(Context var1) {
        if (uuid == null) {
            Class var2 = IDeviceUuidFactory.class;
            synchronized (IDeviceUuidFactory.class) {
                if (uuid == null) {
                    SharedPreferences var3 = var1.getSharedPreferences(PREFS_FILE, 0);
                    String var4 = var3.getString(PREFS_DEVICE_ID, null);
                    if (var4 != null) {
                        uuid = UUID.fromString(var4);
                    } else {
                        String var5 = Secure.getString(var1.getContentResolver(), "android_id");

                        try {
                            if (!"9774d56d682e549c".equals(var5)) {
                                uuid = UUID.nameUUIDFromBytes(var5.getBytes("utf8"));
                            } else {
                                String var6 = getTelephonyManager(var1).getDeviceId();
                                uuid = var6 != null ? UUID.nameUUIDFromBytes(var6.getBytes("utf8")) : this.generateDeviceUuid(var1);
                            }
                        } catch (UnsupportedEncodingException var8) {
                            throw new RuntimeException(var8);
                        }

                        var3.edit().putString(PREFS_DEVICE_ID, uuid.toString()).apply();
                    }
                }
            }
        }

    }

    public UUID getDeviceUuid() {
        return uuid;
    }

    private UUID generateDeviceUuid(Context var1) {
        String var2 = Build.BOARD + Build.BRAND + Build.CPU_ABI + Build.DEVICE + Build.DISPLAY
                + Build.FINGERPRINT + Build.HOST + Build.ID + Build.MANUFACTURER + Build.MODEL
                + Build.PRODUCT + Build.TAGS + Build.TYPE + Build.USER;
        TelephonyManager var3 = getTelephonyManager(var1);
        String var4 = var3 != null ? var3.getDeviceId() : null;
        String var5 = Secure.getString(var1.getContentResolver(), "android_id");
        WifiManager var6 = (WifiManager) var1.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String var7 = var6 != null ? var6.getConnectionInfo().getMacAddress() : null;
        if (isEmpty(var4) && isEmpty(var5) && isEmpty(var7)) {
            return UUID.randomUUID();
        } else {
            String var8 = var2 + var4 + var5 + var7;
            return UUID.nameUUIDFromBytes(var8.getBytes());
        }
    }

    private static boolean isEmpty(Object var0) {
        return var0 == null || (var0 instanceof String && ((String) var0).trim().length() == 0 || (var0 instanceof Map && ((Map) var0).isEmpty()));
    }

    private TelephonyManager getTelephonyManager(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
    }
}
