package net.box.app.library.compat;

import androidx.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by box on 2017/7/26.
 * <p>
 * 处理常用数据类型
 */

public class INumberCompat {

    public static double valueOf(CharSequence value, @NonNull Double defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        double d = defaultValue;
        try {
            d = Double.valueOf(value.toString());
        } catch (Exception ignored) {
        }
        return d;
    }

    public static float valueOf(CharSequence value, @NonNull Float defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        float d = defaultValue;
        try {
            d = Float.valueOf(value.toString());
        } catch (Exception ignored) {
        }
        return d;
    }

    public static long valueOf(CharSequence value, @NonNull Long defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        long d = defaultValue;
        try {
            d = Long.valueOf(value.toString());
        } catch (Exception ignored) {
        }
        return d;
    }

    public static int valueOf(CharSequence value, @NonNull Integer defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        int d = defaultValue;
        try {
            d = Integer.valueOf(value.toString());
        } catch (Exception ignored) {
        }
        return d;
    }
}
