/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.internal.$Gson$Types;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by box on 2017/5/5.
 * <p>
 * 处理Uri
 */

@SuppressWarnings({"WeakerAccess", "unused", "unchecked"})
public class IURIUtils {

    public static <T> T getQueryParameter(@NonNull Activity activity, @NonNull String name, @NonNull T defaultValue) {
        return getQueryParameter(activity, name, null, defaultValue);
    }

    public static <T> T getQueryParameter(@Nullable Uri data, @NonNull String name, @NonNull T defaultValue) {
        return getQueryParameter(data, name, null, defaultValue);
    }

    public static <T> T getQueryParameter(@NonNull Activity activity, @NonNull String name, @Nullable Class<T> cls, @Nullable T defaultValue) {
        return getQueryParameter(activity.getIntent().getData(), name, cls, defaultValue);
    }

    public static <T> T getQueryParameter(@Nullable Uri data, @NonNull String name, @Nullable Class<T> cls, @Nullable T defaultValue) {
        if (cls == null && defaultValue == null) {
            throw new NullPointerException("cls和defaultValue不能同时为null!");
        }
        if (cls == null) {
            cls = (Class<T>) defaultValue.getClass();
        }
        String parameter = data == null ? null : data.getQueryParameter(name);
        return TextUtils.isEmpty(parameter) ? defaultValue : getResponseType(cls, parameter);
    }

    private static <T> T getResponseType(@NonNull Class<T> type, @NonNull String value) {
        T tmpType;
        if ($Gson$Types.equals(type, Byte.class)) {
            tmpType = (T) Byte.valueOf(value);
        } else if ($Gson$Types.equals(type, Double.class)) {
            tmpType = (T) Double.valueOf(value);
        } else if ($Gson$Types.equals(type, Float.class)) {
            tmpType = (T) Float.valueOf(value);
        } else if ($Gson$Types.equals(type, Integer.class)) {
            tmpType = (T) Integer.valueOf(value);
        } else if ($Gson$Types.equals(type, Long.class)) {
            tmpType = (T) Long.valueOf(value);
        } else if ($Gson$Types.equals(type, Short.class)) {
            tmpType = (T) Short.valueOf(value);
        } else if ($Gson$Types.equals(type, BigDecimal.class)) {
            tmpType = (T) BigDecimal.valueOf(Double.valueOf(value));
        } else if ($Gson$Types.equals(type, BigInteger.class)) {
            tmpType = (T) BigInteger.valueOf(Long.valueOf(value));
        } else if ($Gson$Types.equals(type, Boolean.class)) {
            tmpType = (T) Boolean.valueOf(value);
        } else if (Enum.class.isAssignableFrom(type)) {
            tmpType = (T) Enum.valueOf((Class<Enum>) type, value);
        } else {
            tmpType = (T) String.valueOf(value);
        }
        return tmpType;
    }

    public static Intent createUriIntent(String uriString) {
        return createUriIntent(Intent.ACTION_VIEW, uriString);
    }

    public static Intent createUriIntent(String action, String uriString) {
        return new Intent(action, Uri.parse(uriString));
    }

    public static class UrlCreator {

        private final Map<String, Object> mObjectMap = new HashMap<>();
        private final String mUrl;

        public UrlCreator(@NonNull String url) {
            this(url, null);
        }

        public UrlCreator(@NonNull String url, @Nullable String path) {
            if (!TextUtils.isEmpty(path)) {
                if (url.charAt(url.length() - 1) != '/') {
                    url += "/";
                }
                url += path;
            }
            mUrl = url;
        }

        public void put(@NonNull String key, @Nullable Object value) {
            mObjectMap.put(key, value);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(mUrl);
            int lastIndex = builder.length() - 1;
            char c = builder.charAt(lastIndex);
            if (c == '/') {
                builder.deleteCharAt(lastIndex).append("?");
            } else if (c != '?') {
                builder.append("?");
            }
            for (String key : mObjectMap.keySet()) {
                Object value = mObjectMap.get(key);
                if (value == null) {
                    continue;
                }
                builder.append(key).append("=").append(String.valueOf(value)).append("&");
            }
            int length = builder.length();
            if (length > 0) {
                builder.deleteCharAt(length - 1);
            }
            return builder.toString();
        }
    }
}
