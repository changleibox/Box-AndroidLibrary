/*
 * Copyright © All right reserved by CHANGLEI.
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.izhuo.app.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class IDeviceUuidFactory {
    protected static final String PREFS_FILE = "device_id.xml";
    protected static final String PREFS_DEVICE_ID = "device_id";
    protected static UUID uuid;

    public IDeviceUuidFactory(Context var1) {
        if (uuid == null) {
            Class var2 = IDeviceUuidFactory.class;
            synchronized (IDeviceUuidFactory.class) {
                if (uuid == null) {
                    SharedPreferences var3 = var1.getSharedPreferences("device_id.xml", 0);
                    String var4 = var3.getString("device_id", null);
                    if (var4 != null) {
                        uuid = UUID.fromString(var4);
                    } else {
                        String var5 = Secure.getString(var1.getContentResolver(), "android_id");

                        try {
                            if (!"9774d56d682e549c".equals(var5)) {
                                uuid = UUID.nameUUIDFromBytes(var5.getBytes("utf8"));
                            } else {
                                @SuppressWarnings("WrongConstant") String var6 = ((TelephonyManager) var1.getSystemService("phone")).getDeviceId();
                                uuid = var6 != null ? UUID.nameUUIDFromBytes(var6.getBytes("utf8")) : this.generateDeviceUuid(var1);
                            }
                        } catch (UnsupportedEncodingException var8) {
                            throw new RuntimeException(var8);
                        }

                        var3.edit().putString("device_id", uuid.toString()).commit();
                    }
                }
            }
        }

    }

    @SuppressWarnings("WrongConstant")
    private UUID generateDeviceUuid(Context var1) {
        @SuppressWarnings("deprecation") String var2 = Build.BOARD + Build.BRAND + Build.CPU_ABI + Build.DEVICE + Build.DISPLAY + Build.FINGERPRINT + Build.HOST + Build.ID + Build.MANUFACTURER + Build.MODEL + Build.PRODUCT + Build.TAGS + Build.TYPE + Build.USER;
        TelephonyManager var3 = (TelephonyManager) var1.getSystemService("phone");
        String var4 = var3.getDeviceId();
        String var5 = Secure.getString(var1.getContentResolver(), "android_id");
        WifiManager var6 = (WifiManager) var1.getSystemService("wifi");
        String var7 = var6.getConnectionInfo().getMacAddress();
        if (isEmpty(var4) && isEmpty(var5) && isEmpty(var7)) {
            return UUID.randomUUID();
        } else {
            String var8 = var2 + var4 + var5 + var7;
            return UUID.nameUUIDFromBytes(var8.getBytes());
        }
    }

    public UUID getDeviceUuid() {
        return uuid;
    }

    private static boolean isEmpty(Object var0) {
        return var0 == null || (var0 instanceof String && ((String) var0).trim().length() == 0 || (var0 instanceof Map && ((Map) var0).isEmpty()));
    }
}
