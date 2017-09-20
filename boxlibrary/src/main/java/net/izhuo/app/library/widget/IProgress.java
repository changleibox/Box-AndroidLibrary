/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import net.izhuo.app.library.R;

/**
 * @author Box
 *         <p>
 *         2015年7月22日
 */
@SuppressWarnings("JavaDoc")
public class IProgress extends Dialog {

    private Context mContext;
    private TextView mTvMessage;

    /**
     * @param context
     */
    public IProgress(Context context) {
        this(context, R.style.Box_Translucent_Dialog);
    }

    public IProgress(Context context, @StyleRes int theme) {
        super(context, theme);
        init(context, Theme.Black);
    }

    public IProgress(Context context, Theme theme) {
        super(context, R.style.Box_Translucent_Dialog);
        init(context, theme);
    }

    /**
     * 初始化对话框
     */
    private void init(Context context, Theme theme) {
        this.mContext = context;
        setContentView(getDefView());
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(lp);

        setTheme(theme);
    }

    /**
     * 初始化布局及控件
     */
    private View getDefView() {
        View contentView = View.inflate(mContext, R.layout.box_dialog_progress, null);
        mTvMessage = (TextView) contentView.findViewById(R.id.box_tv_message);
        return contentView;
    }

    public IProgress setTheme(Theme theme) {
        View view = findViewById(R.id.box_progress_content);
        if (theme == Theme.Black) {
            view.setBackgroundResource(R.drawable.box_progress_bg_black);
            mTvMessage.setTextColor(Color.WHITE);
        } else if (theme == Theme.White) {
            view.setBackgroundResource(R.drawable.box_progress_bg_white);
            mTvMessage.setTextColor(Color.BLACK);
        }
        return this;
    }

    public IProgress setMessage(CharSequence sequence) {
        if (mTvMessage != null) {
            if (TextUtils.isEmpty(sequence)) {
                mTvMessage.setVisibility(View.GONE);
            } else {
                mTvMessage.setVisibility(View.VISIBLE);
                mTvMessage.setText(sequence);
            }
        }
        return this;
    }

    public IProgress setMessage(@StringRes int resId) {
        return setMessage(mContext.getString(resId));
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum Theme {
        White, Black
    }
}
