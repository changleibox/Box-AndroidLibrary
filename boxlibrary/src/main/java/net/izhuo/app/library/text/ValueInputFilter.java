package net.izhuo.app.library.text;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.Arrays;

public class ValueInputFilter implements InputFilter {

    private static final String EMPTY = "";

    private final double mMinValue;
    private final double mMaxValue;

    public ValueInputFilter(double minValue, double maxValue) {
        this.mMinValue = minValue;
        this.mMaxValue = maxValue;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (TextUtils.isEmpty(source)) {
            return null;
        }

        char[] chars = source.toString().toCharArray();
        for (int i = source.length(); i >= 0; i--) {
            char[] tmpChars = Arrays.copyOf(chars, i);
            if (!isExceed(new StringBuilder(dest).insert(dstart, tmpChars))) {
                return new String(tmpChars);
            }
        }
        return EMPTY;
    }

    private boolean isExceed(CharSequence valueStr) {
        double value;
        try {
            value = Double.valueOf(valueStr.toString());
        } catch (Exception e) {
            return true;
        }
        return value > mMaxValue || value < mMinValue;
    }

    // private static final String TAG = ValueInputFilter.class.getSimpleName();
    //
    // private double mMinValue = Double.MIN_VALUE;
    // private double mMaxValue = Double.MAX_VALUE;
    //
    // @SuppressWarnings("deprecation")
    // public ValueInputFilter(double minValue, double maxValue) {
    //     this.mMinValue = minValue;
    //     this.mMaxValue = maxValue;
    //
    //     NumberFormat format = NumberFormat.getInstance();
    //     Log.i(TAG, "minValue = " + format.format(mMinValue));
    //     Log.i(TAG, "maxValue = " + format.format(mMaxValue));
    // }
    //
    // @Override
    // public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
    //     StringBuilder builder = new StringBuilder(dest);
    //     if (TextUtils.isEmpty(source)) {
    //         builder.delete(dstart, dend);
    //     } else {
    //         builder.insert(dstart, source.toString().toCharArray());
    //     }
    //
    //     double value;
    //     try {
    //         value = Double.valueOf(builder.toString());
    //     } catch (Exception e) {
    //         return "";
    //     }
    //     if (value > mMaxValue || value < mMinValue) {
    //         return "";
    //     }
    //     return null;
    // }
}