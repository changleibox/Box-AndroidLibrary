/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import net.izhuo.app.library.R;

/**
 * Created by Box on 2016/10/14.
 * <p>
 * 自动补全搜索框
 */
public class IAutoCompleteClearEditText extends AppCompatAutoCompleteTextView implements View.OnFocusChangeListener, TextWatcher {

    private Drawable mClearDrawable;
    private boolean hasFoucs;

    private OnSearchListener mSearchListener;

    public IAutoCompleteClearEditText(Context context) {
        super(context);
        initAddressEditTextView();
    }

    public IAutoCompleteClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAddressEditTextView();
    }

    public IAutoCompleteClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAddressEditTextView();
    }

    private void initAddressEditTextView() {
        this.mClearDrawable = this.getCompoundDrawables()[2];
        if (this.mClearDrawable == null) {
            //noinspection deprecation
            this.mClearDrawable = getResources().getDrawable(R.drawable.box_btn_clean);
        }

        assert this.mClearDrawable != null;
        int intrinsicWidth = this.mClearDrawable.getIntrinsicWidth();
        int intrinsicHeight = this.mClearDrawable.getIntrinsicHeight();
        this.mClearDrawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        this.setClearIconVisible(false);
        this.setOnFocusChangeListener(this);
        this.addTextChangedListener(this);
    }

    @Override
    public void setText(CharSequence text, boolean filter) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            super.setText(text, filter);
        } else {
            super.setText(text);
            dismissDropDown();
        }
    }

    @Override
    protected void replaceText(CharSequence text) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);
        if (this.hasFoucs) {
            this.setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mSearchListener != null) {
            if (TextUtils.isEmpty(s)) {
                dismissDropDown();
            } else {
                mSearchListener.onSearch(s);
            }
        }
    }

    public void setOnSearchListener(OnSearchListener listener) {
        this.mSearchListener = listener;
    }

    public interface OnSearchListener {
        void onSearch(CharSequence poi);
    }

    @SuppressWarnings("unused")
    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }

    public static Animation shakeAnimation(int counts) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0F, 10.0F, 0.0F, 0.0F);
        translateAnimation.setInterpolator(new CycleInterpolator((float) counts));
        translateAnimation.setDuration(1000L);
        return translateAnimation;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 1 && this.getCompoundDrawables()[2] != null) {
            boolean touchable = event.getX() > (float) (this.getWidth() - this.getTotalPaddingRight())
                    && event.getX() < (float) (this.getWidth() - this.getPaddingRight());
            if (touchable) {
                getText().clear();
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            this.setClearIconVisible(this.getText().length() > 0);
        } else {
            this.setClearIconVisible(false);
        }

    }

    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? this.mClearDrawable : null;
        this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], right, this.getCompoundDrawables()[3]);
    }
}
