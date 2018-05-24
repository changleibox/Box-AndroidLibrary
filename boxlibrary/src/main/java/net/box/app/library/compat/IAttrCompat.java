/*
 * Copyright (c) All right reserved by Box
 */

package net.box.app.library.compat;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * Created by Box on 17/3/16.
 * <p>
 * 获取属性值
 */
public class IAttrCompat {

    public static int getValue(Context context, int attr, int defValue) {
        int[] attrsArray = {attr};
        TypedArray typedArray = context.obtainStyledAttributes(attrsArray);
        int value = typedArray.getDimensionPixelSize(0, defValue);
        typedArray.recycle();
        return value;
    }

    public static int getColor(Context context, int attr, int defColor) {
        int[] attrsArray = {attr};
        TypedArray typedArray = context.obtainStyledAttributes(attrsArray);
        int value = typedArray.getColor(0, defColor);
        typedArray.recycle();
        return value;
    }

    public static int getResourceId(Context context, int attr, int defValue) {
        int[] attrsArray = {attr};
        TypedArray typedArray = context.obtainStyledAttributes(attrsArray);
        int value = typedArray.getResourceId(0, defValue);
        typedArray.recycle();
        return value;
    }

    public static boolean getBoolean(Context context, int attr, boolean defValue) {
        int[] attrsArray = {attr};
        TypedArray typedArray = context.obtainStyledAttributes(attrsArray);
        boolean value = typedArray.getBoolean(0, defValue);
        typedArray.recycle();
        return value;
    }
}
