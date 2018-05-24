/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.box.app.library.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>
 * This dialog box for the imitation of IOS flat design dialog box. This dialog
 * box only to realize a simple dialog box functions, if you want to realize
 * more complex functions, please use the {@link AlertDialog}.
 * </p>
 * <p>
 * A subclass of Dialog that can display one or two buttons. If you only want to
 * display a String in this dialog box, use the
 * {@link IOSDialog#setMessage(int)} or
 * {@link IOSDialog#setMessage(CharSequence)} method. If you want to display a
 * more complex view, use the {@link IOSDialog#setContentView(View)} or
 * {@link IOSDialog#setContentView(int)}add your view to it. <br>
 * If you want to use a button, you can call
 * <p>
 * <pre>
 * {@link IOSDialog#setNegativeButton(int, OnClickListener)} <br>
 * {@link IOSDialog#setNegativeButton(CharSequence, OnClickListener)}
 * </pre>
 * <p>
 * or
 * <p>
 * <pre>
 * {@link IOSDialog#setPositiveButton(CharSequence, OnClickListener)} <br>
 * {@link IOSDialog#setPositiveButton(int, OnClickListener)}
 * </pre>
 * <p>
 * method.
 * </p>
 * <p>
 * The specific implementation of logic is not discussed here, similar to the
 * {@link AlertDialog}.
 * </p>
 *
 * @author Changlei
 *         <p>
 *         2015年1月26日
 * @version [v1.0]
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class IOSDialog extends AppCompatDialog {

    public enum ClickDismissType {
        ClickBefore, ClickAfter, NotDismiss
    }

    public static final int DEF_CLICK_BUTTON_INDEX = 0;

    public TextView mTvTitle;
    public Button mBtnPositive, mBtnNegative;

    private Context mContext;
    private View mContentView, mLLButtonsContainer;
    private TextView mTvMessage;

    private int mClickButtonIndex = DEF_CLICK_BUTTON_INDEX;

    private FrameLayout mFlCustomView;
    private View mCustomView;

    private ClickDismissType mDismissType = ClickDismissType.ClickAfter;

    /**
     * Copyright All right reserved by IZHUO.NET.
     */
    public IOSDialog(Context context) {
        super(context, R.style.Box_Translucent_Dialog);
        init(context, Gravity.CENTER);
    }

    /**
     * @param context
     * @param cancelable
     * @param cancelListener
     */
    private IOSDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context, Gravity.CENTER);
    }

    /**
     * @param context
     * @param theme
     */
    private IOSDialog(Context context, int theme, int gravity) {
        super(context, theme);
        init(context, gravity);
    }

    /**
     * @param context
     * @param theme
     */
    private IOSDialog(Context context, int theme) {
        this(context, theme, Gravity.CENTER);
    }

    /**
     * 初始化对话框
     */
    private void init(Context context, int gravity) {
        this.mContext = context;
        setContentView(getDefView());
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = gravity;
        window.setAttributes(lp);
    }

    /**
     * 初始化布局及控件
     */
    private View getDefView() {
        mContentView = View.inflate(mContext, R.layout.box_dialog_default, null);
        return mContentView;
    }

    /**
     * Set the screen content to an explicit view. This view is placed directly
     * into the screen's view hierarchy. It can itself be a complex view
     * hierarhcy.
     *
     * @param view The desired content to display.
     */
    @Override
    public void setContentView(View view) {
        mContentView = view;
        super.setContentView(view);

        if (mContentView != null) {
            mTvTitle = (TextView) mContentView.findViewById(R.id.box_tv_title);
            mTvMessage = (TextView) mContentView.findViewById(R.id.box_tv_message);
            mBtnPositive = (Button) mContentView.findViewById(R.id.box_btn_sure);
            mBtnNegative = (Button) mContentView.findViewById(R.id.box_btn_cancel);

            mLLButtonsContainer = mContentView.findViewById(R.id.box_ll_dialog);
            mFlCustomView = (FrameLayout) mContentView.findViewById(R.id.box_fl_custom_view);

            mLLButtonsContainer.setVisibility(View.GONE);

            mBtnPositive.setVisibility(View.GONE);
            mBtnNegative.setVisibility(View.GONE);
        }
    }

    /**
     * Set the screen content from a layout resource. The resource will be
     * inflated, adding all top-level views to the screen.
     *
     * @param layoutResID Resource ID to be inflated.
     */
    @Override
    public void setContentView(int layoutResID) {
        setContentView(LayoutInflater.from(getContext()).inflate(layoutResID, null));
    }

    public View getContentView() {
        return mContentView;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }
    }

    public IOSDialog setTitleVisible(boolean isVisible) {
        if (mTvTitle != null) {
            if (isVisible) {
                mTvTitle.setVisibility(View.VISIBLE);
            } else {
                mTvTitle.setVisibility(View.GONE);
            }
        }
        return this;
    }

    @Override
    public void setTitle(int resId) {
        setTitle(mContext.getString(resId));
    }

    public IOSDialog setMessage(CharSequence message) {
        if (mTvMessage != null) {
            mTvMessage.setText(message);
        }
        return this;
    }

    public IOSDialog setMessage(int resId) {
        setMessage(mContext.getString(resId));
        return this;
    }

    public IOSDialog setClickDismissType(ClickDismissType dismissType) {
        this.mDismissType = dismissType;
        return this;
    }

    public IOSDialog setPositiveButtonTextColor(int color) {
        mBtnPositive.setTextColor(color);
        return this;
    }

    public IOSDialog setPositiveButtonTextColor(ColorStateList colors) {
        mBtnPositive.setTextColor(colors);
        return this;
    }

    public IOSDialog setPositiveButtonText(CharSequence sure) {
        mBtnPositive.setText(sure);
        return this;
    }

    public void removePositiveButton() {
        mBtnPositive.setVisibility(View.GONE);
        int visibility = mBtnNegative.getVisibility();
        if (visibility == View.VISIBLE) {
            mLLButtonsContainer.setVisibility(View.VISIBLE);
        } else {
            mLLButtonsContainer.setVisibility(View.GONE);
        }
        mBtnPositive.setBackgroundResource(R.drawable.box_tv_click_down);
        mBtnNegative.setBackgroundResource(R.drawable.box_tv_click_down);
    }

    public IOSDialog setPositiveButton(int sureResId, final OnClickListener onClickListener) {
        return setPositiveButton(getString(sureResId), onClickListener);
    }

    public IOSDialog setPositiveButton(CharSequence sure, final OnClickListener clickListener) {
        createPositiveButton(clickListener);
        setPositiveButtonText(sure);
        return this;
    }

    protected IOSDialog createPositiveButton(final OnClickListener clickListener) {
        mLLButtonsContainer.setVisibility(View.VISIBLE);
        int visibility = mBtnNegative.getVisibility();
        if (visibility == View.VISIBLE) {
            mBtnPositive.setBackgroundResource(R.drawable.box_tv_click_left_down);
            mBtnNegative.setBackgroundResource(R.drawable.box_tv_click_right_down);
        } else {
            mBtnPositive.setBackgroundResource(R.drawable.box_tv_click_down);
        }
        mBtnPositive.setVisibility(View.VISIBLE);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDismissType == ClickDismissType.ClickBefore) {
                    dismiss();
                }
                mClickButtonIndex = BUTTON_POSITIVE;
                if (clickListener != null) {
                    clickListener.onClick(IOSDialog.this, BUTTON_POSITIVE);
                }
                if (mDismissType == ClickDismissType.NotDismiss) {
                    return;
                }
                if (mDismissType == ClickDismissType.ClickAfter) {
                    dismiss();
                }
            }
        };
        mBtnPositive.setOnClickListener(listener);
        return this;
    }

    public IOSDialog setNegativeButtonTextColor(int color) {
        mBtnNegative.setTextColor(color);
        return this;
    }

    public IOSDialog setNegativeButtonTextColor(ColorStateList colors) {
        mBtnNegative.setTextColor(colors);
        return this;
    }

    public IOSDialog setNegativeButtonText(CharSequence cancel) {
        mBtnNegative.setText(cancel);
        return this;
    }

    public void removeNegativeButton() {
        mBtnNegative.setVisibility(View.GONE);
        int visibility = mBtnPositive.getVisibility();
        if (visibility == View.VISIBLE) {
            mLLButtonsContainer.setVisibility(View.VISIBLE);
        } else {
            mLLButtonsContainer.setVisibility(View.GONE);
        }
        mBtnPositive.setBackgroundResource(R.drawable.box_tv_click_down);
        mBtnNegative.setBackgroundResource(R.drawable.box_tv_click_down);
    }

    public IOSDialog setNegativeButton(int cancelResId, final OnClickListener onClickListener) {
        setNegativeButton(getString(cancelResId), onClickListener);
        return this;
    }

    public IOSDialog setNegativeButton(CharSequence cancel, final OnClickListener clickListener) {
        createNegativeButton(clickListener);
        setNegativeButtonText(cancel);
        return this;
    }

    protected IOSDialog createNegativeButton(final OnClickListener clickListener) {
        mLLButtonsContainer.setVisibility(View.VISIBLE);
        int visibility = mBtnPositive.getVisibility();
        if (visibility == View.VISIBLE) {
            mBtnPositive.setBackgroundResource(R.drawable.box_tv_click_left_down);
            mBtnNegative.setBackgroundResource(R.drawable.box_tv_click_right_down);
        } else {
            mBtnNegative.setBackgroundResource(R.drawable.box_tv_click_down);
        }
        mBtnNegative.setVisibility(View.VISIBLE);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDismissType == ClickDismissType.ClickBefore) {
                    dismiss();
                }
                mClickButtonIndex = BUTTON_NEGATIVE;
                if (clickListener != null) {
                    clickListener.onClick(IOSDialog.this, BUTTON_NEGATIVE);
                }
                if (mDismissType == ClickDismissType.NotDismiss) {
                    return;
                }
                if (mDismissType == ClickDismissType.ClickAfter) {
                    dismiss();
                }
            }
        };
        mBtnNegative.setOnClickListener(listener);
        return this;
    }

    /**
     * 获取被点击按钮的索引
     *
     * @return 被点击按钮的索引
     * <p>
     * {@link DialogInterface#BUTTON_NEGATIVE} or
     * {@link DialogInterface#BUTTON_POSITIVE} or
     * {@link IOSDialog#DEF_CLICK_BUTTON_INDEX}
     * <p>
     * 如果返回的是{@link IOSDialog#DEF_CLICK_BUTTON_INDEX} 则说明没有点击任何按钮
     */
    public int getClickButtonIndex() {
        return mClickButtonIndex;
    }

    @Override
    public void show() {
        mClickButtonIndex = DEF_CLICK_BUTTON_INDEX;
        super.show();
    }

    public void setCustomView(View view) {
        if (view == null || mFlCustomView == null) {
            return;
        }
        mFlCustomView.removeAllViews();
        mFlCustomView.addView(view);
        mCustomView = view;
        mTvMessage.setVisibility(View.GONE);
    }

    public View getCustomView() {
        return mCustomView;
    }

    private CharSequence getString(int resId) {
        return mContext.getString(resId);
    }

    /**
     * Created by Box on 16/11/29.
     * <p>
     * IOSDialog按钮点击事件
     */
    public interface OnClickListener extends DialogInterface.OnClickListener {

        @IntDef({BUTTON_POSITIVE, BUTTON_NEGATIVE})
        @Retention(RetentionPolicy.SOURCE)
        @interface Whitch {
        }

        @Override
        void onClick(DialogInterface dialog, @Whitch int which);
    }

}
