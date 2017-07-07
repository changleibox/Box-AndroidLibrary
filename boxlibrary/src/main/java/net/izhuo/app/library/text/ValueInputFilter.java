package net.izhuo.app.library.text;

import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;

import java.math.BigDecimal;
import java.util.Arrays;

@SuppressWarnings("deprecation")
public class ValueInputFilter extends DigitsKeyListener {

    private static final String EMPTY = "";
    private static final String NEGATIVE_SIGN = "-";

    private final BigDecimal mMinValue;
    private final BigDecimal mMaxValue;

    public ValueInputFilter(double minValue, double maxValue) {
        this(minValue, maxValue, true);
    }

    public ValueInputFilter(double minValue, double maxValue, boolean decimal) {
        super(minValue < 0, decimal);
        this.mMinValue = BigDecimal.valueOf(minValue);
        this.mMaxValue = BigDecimal.valueOf(maxValue);
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

        CharSequence tmpSource = removeInvalidZero(source, dest, dstart);
        if (tmpSource == null) {
            return EMPTY;
        }
        char[] chars = tmpSource.toString().toCharArray();
        for (int i = chars.length; i >= 0; i--) {
            char[] tmpChars = Arrays.copyOf(chars, i);
            if (!isExceed(new StringBuilder(dest).insert(dstart, tmpChars))) {
                return new String(tmpChars);
            }
        }
        return EMPTY;
    }

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
        return value.compareTo(mMaxValue) == 1 || value.compareTo(mMinValue) == -1;
    }

    private CharSequence removeInvalidZero(CharSequence source, Spanned dest, int dstart) {
        if (TextUtils.isEmpty(source)) {
            return null;
        }
        char sourceFirst = source.charAt(0);
        if (sourceFirst == '-' || TextUtils.isEmpty(dest)) {
            return source;
        }
        if (((dstart <= 1 && dest.charAt(0) == '0'
                || (dstart == 1 && dest.charAt(0) == '-' && dest.length() > 1 && source.length() == 1 && sourceFirst != '0' && dest.charAt(1) != '0'))
                || (dstart <= 2 && dest.charAt(0) == '-' && dest.length() > 1 && dest.charAt(1) == '0'))
                && sourceFirst != '.') {
            return null;
        }
        return source;
    }

}