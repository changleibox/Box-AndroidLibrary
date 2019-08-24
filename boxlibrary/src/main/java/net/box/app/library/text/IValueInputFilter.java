/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.text;

import androidx.annotation.IntDef;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Created by box on 2017/11/22.
 * <p>
 * 限制ediitext输入数字的最大值和最小值
 */

@SuppressWarnings({"deprecation", "WeakerAccess"})
public class IValueInputFilter extends DigitsKeyListener {

    public static final int FLAG_INCLUSIVE_EXCLUSIVE = 0x11;
    public static final int FLAG_INCLUSIVE_INCLUSIVE = 0x22;
    public static final int FLAG_EXCLUSIVE_EXCLUSIVE = 0x33;
    public static final int FLAG_EXCLUSIVE_INCLUSIVE = 0x44;

    private static final String EMPTY = "";
    private static final String NEGATIVE_SIGN = "-";

    @IntDef({FLAG_INCLUSIVE_EXCLUSIVE, FLAG_INCLUSIVE_INCLUSIVE, FLAG_EXCLUSIVE_EXCLUSIVE, FLAG_EXCLUSIVE_INCLUSIVE})
    private @interface Flag {
    }

    private final BigDecimal mMinValue;
    private final BigDecimal mMaxValue;

    private boolean isIncludeMin;
    private boolean isIncludeMax;

    public IValueInputFilter(double minValue, double maxValue) {
        this(minValue, maxValue, FLAG_INCLUSIVE_INCLUSIVE);
    }

    public IValueInputFilter(double minValue, double maxValue, @Flag int flag) {
        this(minValue, maxValue, true, flag);
    }

    public IValueInputFilter(double minValue, double maxValue, boolean decimal, @Flag int flag) {
        super(minValue < 0, decimal);
        this.mMinValue = BigDecimal.valueOf(minValue);
        this.mMaxValue = BigDecimal.valueOf(maxValue);
        if (flag == FLAG_EXCLUSIVE_EXCLUSIVE) {
            isIncludeMin = false;
            isIncludeMax = false;
        } else {
            isIncludeMin = flag == FLAG_INCLUSIVE_EXCLUSIVE || flag == FLAG_INCLUSIVE_INCLUSIVE;
            isIncludeMax = flag == FLAG_INCLUSIVE_INCLUSIVE || flag == FLAG_EXCLUSIVE_INCLUSIVE;
        }
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        CharSequence filter = super.filter(source, start, end, dest, dstart, dend);
        if (filter != null) {
            return filter;
        }
        if (TextUtils.isEmpty(source)) {
            if (isExceed(new StringBuilder(dest).delete(dstart, dend))) {
                return dest.subSequence(dstart, dend);
            }
            return null;
        }

        if ((dest.length() == 1 && TextUtils.equals(dest, "-") && TextUtils.equals(source, "."))
                || (TextUtils.isEmpty(dest) && ((TextUtils.equals(NEGATIVE_SIGN, source) || TextUtils.equals(".", source))))) {
            return null;
        }

        char[] chars = source.toString().toCharArray();
        for (int i = chars.length; i >= 0; i--) {
            char[] tmpChars = Arrays.copyOf(chars, i);
            if (!isExceed(new StringBuilder(dest).insert(dstart, tmpChars))) {
                return new String(tmpChars);
            }
        }
        return EMPTY;
    }

    /**
     * 判断是否超过边界值
     */
    private boolean isExceed(CharSequence valueStr) {
        if (TextUtils.isEmpty(valueStr) || (valueStr.length() == 1 && (valueStr.charAt(0) == '-' || valueStr.charAt(0) == '.'))) {
            return false;
        }
        BigDecimal value;
        try {
            value = new BigDecimal(valueStr.toString());
        } catch (Exception e) {
            return true;
        }

        return value.compareTo(mMaxValue) >= (isIncludeMax ? 1 : 0) || value.compareTo(mMinValue) <= (isIncludeMin ? -1 : 0);
    }

}