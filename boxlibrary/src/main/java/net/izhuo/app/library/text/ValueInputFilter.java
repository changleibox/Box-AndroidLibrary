package net.izhuo.app.library.text;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;

import java.text.NumberFormat;

public class ValueInputFilter implements InputFilter {

    private static final String TAG = ValueInputFilter.class.getSimpleName();

    private double mMinValue = Double.MIN_VALUE;
    private double mMaxValue = Double.MAX_VALUE;

    @SuppressWarnings("deprecation")
    public ValueInputFilter(double minValue, double maxValue) {
        this.mMinValue = minValue;
        this.mMaxValue = maxValue;

        NumberFormat format = NumberFormat.getInstance();
        Log.i(TAG, "minValue = " + format.format(mMinValue));
        Log.i(TAG, "maxValue = " + format.format(mMaxValue));
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder builder = new StringBuilder(dest);
        if (TextUtils.isEmpty(source)) {
            builder.delete(dstart, dend);
        } else {
            builder.insert(dstart, source.toString().toCharArray());
        }

        double value;
        try {
            value = Double.valueOf(builder.toString());
        } catch (Exception e) {
            return "";
        }
        if (value > mMaxValue || value < mMinValue) {
            return "";
        }
        return null;
    }
}