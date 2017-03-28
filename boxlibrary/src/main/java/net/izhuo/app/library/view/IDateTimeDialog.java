/*
 * Copyright © All right reserved by CHANGLEI.
 */

package net.izhuo.app.library.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

import net.izhuo.app.library.R;
import net.izhuo.app.library.util.IAppUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Box
 * @since Jdk1.6
 * <p>
 * 2015年11月18日 下午1:25:52
 * <p>
 * 日期时间选择对话框
 */
@SuppressWarnings({"deprecation", "unused"})
public class IDateTimeDialog extends AppCompatDialog implements OnDateChangedListener,
        View.OnClickListener, OnValueChangeListener, OnScrollListener, Formatter {

    private static final SimpleDateFormat DATE_FORMAT_DATE_TIME_MM = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static final SimpleDateFormat DATE_FORMAT_DATE_TIME = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm", Locale.getDefault());
    private static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.getDefault());
    private static final SimpleDateFormat DATE_FORMAT_MONTH = new SimpleDateFormat(
            "yyyy-MM", Locale.getDefault());

    private static final int WEEK_COUNT = 5;

    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";
    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String IS_24_HOUR = "is24hour";

    private final String mTo;

    private DatePicker mDatePicker;
    private TimePicker mTimePicker;

    private NumberPicker mWeekPicker;
    private LinearLayout mLLDateTime;

    private TextView mTvTitle;

    private OnDateTimeSetListener mDateSetListener;

    private String mCurrentWeek;
    private List<String> mWeeks = new ArrayList<>();

    private Mode mode;

    public enum Mode {
        DATE_TIME, WEEK, DATE, MONTH
    }

    public interface OnDateTimeSetListener {

        /**
         * 注意：返回的dateStr格式为yyyy-MM-dd HH:mm:ss,
         * startDate为开始日期的00:00:00，结束时间为结束日期的23:59:59
         */
        void onDateSet(DatePicker view, Date startDate, Date endDate, String startDateStr, String endDateStr);
    }

    public IDateTimeDialog(Context context) {
        super(context, R.style.Box_Dialog_Bottom_Menu);

        mTo = " " + context.getString(R.string.box_lable_to) + " ";

        setContentView(getContentView(context));

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
    }

    private View getContentView(Context context) {
        View contentView = View.inflate(context, R.layout.box_dialog_timer, null);
        mLLDateTime = (LinearLayout) contentView.findViewById(R.id.box_ll_date_time);
        mDatePicker = (DatePicker) contentView.findViewById(R.id.box_datePicker);
        mTimePicker = (TimePicker) contentView.findViewById(R.id.box_timePicker);
        mWeekPicker = (NumberPicker) contentView.findViewById(R.id.box_np_week);
        mTvTitle = (TextView) contentView.findViewById(R.id.box_tv_title);

        Button btnSure = (Button) contentView.findViewById(R.id.box_btn_sure);
        Button btnCancel = (Button) contentView.findViewById(R.id.box_btn_cancel);

        btnSure.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        Calendar c = Calendar.getInstance(Locale.getDefault());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        mDatePicker.setSpinnersShown(true);
        mDatePicker.init(year, month, day, this);
        mDatePicker.setSpinnersShown(true);
        mDatePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
        mTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

        setMode(Mode.DATE_TIME);

        recalculateDatePicker(mDatePicker, true);
        recalculateTimePicker(mTimePicker);

        initWeekPicker();

        IAppUtils.setDatePickerDividerHeight(mDatePicker);
        IAppUtils.setTimePickerDividerHeight(mTimePicker);
        IAppUtils.setDividerHeight(mWeekPicker);
        return contentView;
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(YEAR, mDatePicker.getYear());
        state.putInt(MONTH, mDatePicker.getMonth());
        state.putInt(DAY, mDatePicker.getDayOfMonth());

        state.putInt(HOUR, mTimePicker.getCurrentHour());
        state.putInt(MINUTE, mTimePicker.getCurrentMinute());
        state.putBoolean(IS_24_HOUR, mTimePicker.is24HourView());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int year = savedInstanceState.getInt(YEAR);
        int month = savedInstanceState.getInt(MONTH);
        int day = savedInstanceState.getInt(DAY);
        mDatePicker.init(year, month, day, this);

        int hour = savedInstanceState.getInt(HOUR);
        int minute = savedInstanceState.getInt(MINUTE);
        mTimePicker.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR));
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        mDatePicker.init(year, month, day, this);
    }

    @Override
    public String format(int value) {
        return mWeeks.get(value).replaceAll(mTo, ",");
    }

    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        mCurrentWeek = format(newVal);
    }

    public void updateDate(int year, int monthOfYear, int dayOfMonth, int hour, int minute) {
        mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
    }

    public void updateDate(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        updateDate(year, month, day, hour, minute);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (v.getId() == R.id.box_btn_cancel) {
            return;
        }
        if (mDateSetListener == null) {
            return;
        }
        try {
            Date date = getDate();
            Date startDate = new Date();
            Date endDate = new Date();
            String startDateStr = DATE_FORMAT_DATE_TIME_MM.format(startDate);
            String endDateStr = DATE_FORMAT_DATE_TIME_MM.format(endDate);
            switch (mode) {
                case WEEK:
                    String[] dates = mCurrentWeek.split(",");
                    startDateStr = dates[0] + " 00:00:00";
                    startDate = DATE_FORMAT_DATE_TIME_MM.parse(startDateStr);
                    endDateStr = dates[1] + " 23:59:59";
                    endDate = DATE_FORMAT_DATE_TIME_MM.parse(endDateStr);
                    break;
                case DATE:
                    String dateStr = DATE_FORMAT_DATE.format(date);
                    startDateStr = dateStr + " 00:00:00";
                    startDate = DATE_FORMAT_DATE_TIME_MM.parse(startDateStr);
                    endDateStr = dateStr + " 23:59:59";
                    endDate = DATE_FORMAT_DATE_TIME_MM.parse(endDateStr);
                    break;
                case MONTH:
                    dateStr = DATE_FORMAT_MONTH.format(date);
                    startDateStr = dateStr + "-01 00:00:00";
                    startDate = DATE_FORMAT_DATE_TIME_MM.parse(startDateStr);
                    endDateStr = dateStr + "-" + getMaxValueByMonth(date) + " 23:59:59";
                    endDate = DATE_FORMAT_DATE_TIME_MM.parse(endDateStr);
                    break;
                case DATE_TIME:
                    dateStr = DATE_FORMAT_DATE_TIME.format(date);
                    startDateStr = dateStr + ":00";
                    startDate = DATE_FORMAT_DATE_TIME_MM.parse(startDateStr);
                    endDateStr = dateStr + ":59";
                    endDate = DATE_FORMAT_DATE_TIME_MM.parse(endDateStr);
                    break;
            }
            mDateSetListener.onDateSet(mDatePicker, startDate, endDate, startDateStr, endDateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTitle(CharSequence title) {
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }
    }

    public void setTitle(int resId) {
        setTitle(getContext().getString(resId));
    }

    public String getCurrentDateStr() {
        return DATE_FORMAT_DATE_TIME.format(getDate());
    }

    public Date getDate() {
        try {
            return DATE_FORMAT_DATE_TIME.parse(getDateStr());
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public void setOnDateTimeSetListener(OnDateTimeSetListener l) {
        this.mDateSetListener = l;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        switch (mode) {
            case DATE_TIME:
                mLLDateTime.setVisibility(View.VISIBLE);
                mDatePicker.setVisibility(View.VISIBLE);
                mTimePicker.setVisibility(View.VISIBLE);
                mWeekPicker.setVisibility(View.GONE);
                recalculateDatePicker(mDatePicker, true);
                break;
            case DATE:
                mLLDateTime.setVisibility(View.VISIBLE);
                mDatePicker.setVisibility(View.VISIBLE);
                mTimePicker.setVisibility(View.GONE);
                mWeekPicker.setVisibility(View.GONE);
                recalculateDatePicker(mDatePicker, true);
                break;
            case WEEK:
                mLLDateTime.setVisibility(View.GONE);
                mWeekPicker.setVisibility(View.VISIBLE);
                break;
            case MONTH:
                mLLDateTime.setVisibility(View.VISIBLE);
                mDatePicker.setVisibility(View.VISIBLE);
                mTimePicker.setVisibility(View.GONE);
                mWeekPicker.setVisibility(View.GONE);
                recalculateDatePicker(mDatePicker, false);
                break;
        }
    }

    @Deprecated
    public void setWeek() {
        setMode(Mode.WEEK);
    }

    @Deprecated
    public void setDateTime() {
        setMode(Mode.DATE_TIME);
    }

    @Deprecated
    public void setDate() {
        setMode(Mode.DATE);
    }

    @Deprecated
    public void setMonth() {
        setMode(Mode.MONTH);
    }

    private String getDateStr() {
        int year = mDatePicker.getYear();
        int month = mDatePicker.getMonth();
        int day = mDatePicker.getDayOfMonth();
        int hour = mTimePicker.getCurrentHour();
        int minute = mTimePicker.getCurrentMinute();
        return year + "-" + (month + 1) + "-" + day + " " + hour + ":" + minute;
    }

    private int getMaxValueByMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DATE);
    }

    private void initWeekPicker() {
        mWeeks.clear();
        int currentWeek = WEEK_COUNT / 2 + WEEK_COUNT % 2 - 1;
        for (int i = 0; i < WEEK_COUNT; i++) {
            Date startDate = formatStartDate(i - currentWeek, 0);
            Date endDate = formatStartDate(i - currentWeek, 6);
            mWeeks.add(DATE_FORMAT_DATE.format(startDate) + mTo + DATE_FORMAT_DATE.format(endDate));
        }

        mWeekPicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        mWeekPicker.setOnValueChangedListener(this);
        mWeekPicker.setOnScrollListener(this);
        mWeekPicker.setFormatter(this);
        mWeekPicker.setDisplayedValues(mWeeks.toArray(new String[mWeeks.size()]));
        mWeekPicker.setMinValue(0);
        mWeekPicker.setMaxValue(mWeeks.size() - 1);
        mWeekPicker.setWrapSelectorWheel(false);
        mWeekPicker.setDisplayedValues(mWeeks.toArray(new String[mWeeks.size()]));
        mWeekPicker.setValue(currentWeek);

        mCurrentWeek = format(currentWeek);
    }

    private Date formatStartDate(int x, int y) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.setTime(new Date());
        int week = c.get(Calendar.DAY_OF_WEEK);
        if (week == 1) {
            c.add(Calendar.DATE, -6 + (x * 7) + y);
        } else {
            c.add(Calendar.DATE, -(week - 2) + (x * 7) + y);
        }
        return c.getTime();
    }

    private void recalculateDatePicker(FrameLayout container, boolean hasDay) {
        if (!hasDay) {
            ((ViewGroup) ((ViewGroup) mDatePicker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            return;
        }
        LinearLayout containerTime = (LinearLayout) ((ViewGroup) container.getChildAt(0)).getChildAt(0);
        for (int i = 0; i < containerTime.getChildCount(); i++) {
            View view = containerTime.getChildAt(i);
            view.setPadding(0, 0, 0, 0);
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
            layoutParams.weight = 1;
            layoutParams.width = 0;
            view.setLayoutParams(layoutParams);
        }
    }

    private void recalculateTimePicker(FrameLayout container) {
        LinearLayout containerTime = (LinearLayout) container.getChildAt(0);
        for (int i = 0; i < containerTime.getChildCount(); i++) {
            View view = containerTime.getChildAt(i);
            view.setPadding(0, 0, 0, 0);
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
            layoutParams.weight = 1;
            layoutParams.width = 0;
            view.setLayoutParams(layoutParams);
        }
    }

}
