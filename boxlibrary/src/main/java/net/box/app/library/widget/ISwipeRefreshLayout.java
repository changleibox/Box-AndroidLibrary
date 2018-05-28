package net.box.app.library.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.yalantis.phoenix.PullToRefreshView;

/**
 * Created by box on 2018/5/28.
 * <p>
 * 下拉刷新
 */
public class ISwipeRefreshLayout extends PullToRefreshView {

    public ISwipeRefreshLayout(Context context) {
        super(context);
    }

    public ISwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        super.setOnRefreshListener(listener);
    }

    interface OnRefreshListener extends PullToRefreshView.OnRefreshListener {
    }
}
