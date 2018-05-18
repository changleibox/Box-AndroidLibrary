/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowManager;

import net.izhuo.app.library.IAppCompatActivity;
import net.izhuo.app.library.R;

/**
 * @author Box
 * @since Jdk1.6
 * <p>
 * 2016年1月6日 下午3:24:38
 * <p>
 * 模仿键盘
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class IKeyBoradDialog extends AppCompatDialog {

    private OnDismissListener mOnDismissListener;

    private int mAmountY, mScrollY;
    private View mRootView;

    private IAppCompatActivity mBaseActivity;

    @SuppressLint("InflateParams")
    public IKeyBoradDialog(IAppCompatActivity context) {
        super(context, R.style.Box_KeyBorad_Dialog);
        mBaseActivity = context;

        super.setOnDismissListener(mDismissListener);
    }

    /**
     * @param context
     * @param cancelable
     * @param cancelListener
     */
    private IKeyBoradDialog(IAppCompatActivity context, boolean cancelable, OnCancelListener cancelListener) {
        this(context);
        super.setCancelable(cancelable);
        super.setOnCancelListener(cancelListener);
    }

    private IKeyBoradDialog(Context context) {
        super(context);
    }

    private IKeyBoradDialog(Context context, int theme) {
        super(context, theme);
    }

    private IKeyBoradDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void show(View currentView) {
        mRootView = currentView.getRootView().findViewById(android.R.id.content);
        mScrollY = mRootView.getScrollY();
        int[] location = new int[2];
        currentView.getLocationOnScreen(location);
        mAmountY = location[1] + currentView.getMeasuredHeight();
        show();
    }

    @Override
    public void show() {
        final Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        super.show();

        final View decorView = window.getDecorView();
        if (decorView == null) {
            return;
        }

        ViewTreeObserver observer = decorView.getViewTreeObserver();
        observer.addOnPreDrawListener(new OnPreDrawListener() {

            private boolean isMeasure = false;

            @Override
            public boolean onPreDraw() {
                if (!isMeasure) {
                    isMeasure = true;
                    onDialogShow(IKeyBoradDialog.this, decorView.getMeasuredHeight());
                }
                return isMeasure;
            }
        });
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        this.mOnDismissListener = listener;
    }

    private void onDialogShow(Dialog dialog, int height) {
        if (mRootView == null) {
            return;
        }
        int dialogY = mBaseActivity.getHeight() - height;
        if (dialogY < mAmountY) {
            int offset = mAmountY - dialogY;
            mRootView.scrollBy(0, offset);
            mRootView.invalidate();
        }
    }

    private OnDismissListener mDismissListener = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface dialog) {
            if (mRootView != null) {
                mRootView.scrollTo(0, mScrollY);
                mRootView.invalidate();
            }
            if (mOnDismissListener != null) {
                mOnDismissListener.onDismiss(dialog);
            }
        }
    };

}
