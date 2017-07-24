/*
 * Copyright (c) All right reserved by Box
 */

package net.izhuo.app.library.widget;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerViewCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.izhuo.app.library.R;

/**
 * Created by Box on 17/3/16.
 * <p/>
 * 具有上滑加载更多功能
 */
@SuppressWarnings("unused")
public class ISwipeRecyclerView extends RecyclerViewCompat {

    private static final int FOOTER_HEIGHT = 48;
    private static final int DEFAULT_PAGE_SIZE = 10;
    public static final int NO_TOTAL = -1;
    public static final int FAILURE_RESULT = -1;

    private int mCurrentPage;
    private int mPageSize = DEFAULT_PAGE_SIZE;
    private int mResultSize;
    private int mTotal = NO_TOTAL;
    private int mFooterHeight;

    private boolean isEnabled = true;
    private boolean isComplete = true;

    @Nullable
    private OnLoadMoreListener mLoadMoreListener;

    private TextView mFooterView;

    public ISwipeRecyclerView(Context context) {
        this(context, null);
    }

    public ISwipeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ISwipeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mFooterHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, FOOTER_HEIGHT, getResources().getDisplayMetrics());
        addFooterView(getFooterView(context));
    }

    public void setLoadMoreEnabled(boolean enable) {
        this.isEnabled = enable;
    }

    @Deprecated
    public void setLoadMoreComplete(boolean complete) {
        setComplete(complete);
    }

    public void setTotal(@IntRange(from = NO_TOTAL) int total) {
        this.mTotal = total;
    }

    public void setCurrentPage(int page) {
        this.mCurrentPage = page;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    @Override
    public void setEnabled(boolean enable) {
        super.setEnabled(enable);
        setLoadMoreEnabled(enable);
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
        if (complete && (isLoadedAll() || mResultSize == FAILURE_RESULT)) {
            mFooterView.setText(R.string.box_label_loaded_all);
        }
    }

    public int getPageSize() {
        return mPageSize;
    }

    public void setPageSize(@IntRange(from = 1) int pageSize) {
        this.mPageSize = pageSize;
    }

    /**
     * 设置返回的数量
     *
     * @param resultSize -1代表请求失败；
     */
    public void setResultSize(@IntRange(from = FAILURE_RESULT) int resultSize) {
        this.mResultSize = resultSize;
        setComplete(true);
    }

    public void setFooterVisibility(boolean visibility) {
        mFooterView.setVisibility(visibility ? VISIBLE : GONE);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mLoadMoreListener = listener;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        if (isLoadedAll()) {
            mFooterView.setText(R.string.box_label_loaded_all);
            return;
        }

        performLoadMore();
    }

    public void performLoadMore() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        int itemCount = layoutManager.getItemCount();

        if (isEnabled && isComplete && getLastCompletelyVisiblePosition(layoutManager) >= itemCount - 1) {
            isComplete = false;
            mCurrentPage++;
            mFooterView.setText(R.string.box_label_loading);
            if (mLoadMoreListener != null) {
                mLoadMoreListener.onLoadMore(mCurrentPage);
            }
        }
    }

    protected boolean isLoadedAll() {
        LayoutManager layoutManager = getLayoutManager();
        return layoutManager != null
                && ((mTotal != NO_TOTAL && layoutManager.getItemCount() >= mTotal)
                || (mResultSize != FAILURE_RESULT && mResultSize < mPageSize));
    }

    protected View getFooterView(Context context) {
        FrameLayout layout;
        if (mFooterView == null) {
            FrameLayout.LayoutParams footerParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, mFooterHeight);
            footerParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
            mFooterView = new TextView(context);
            mFooterView.setGravity(Gravity.CENTER);
            mFooterView.setLayoutParams(footerParams);
            mFooterView.setPadding(0, getPaddingBottom(), 0, 0);
            //noinspection deprecation
            mFooterView.setTextAppearance(context, R.style.TextAppearance_AppCompat);
            mFooterView.setTextColor(ContextCompat.getColor(getContext(), R.color.box_color_text_gray));

            layout = new FrameLayout(context);
            layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            layout.addView(mFooterView);
        } else {
            layout = (FrameLayout) mFooterView.getParent();
        }
        return layout;
    }

    /**
     * 获取最后一条展示的位置
     *
     * @return lastVisiblePosition
     */
    private int getLastCompletelyVisiblePosition(LayoutManager manager) {
        int position;
        if (manager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
            int[] lastPositions = layoutManager.findLastCompletelyVisibleItemPositions(null);
            position = getMaxPosition(lastPositions);
        } else {
            position = manager.getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获得最大的位置
     *
     * @param positions 完全可见的一组position
     * @return position值最大的
     */
    private int getMaxPosition(int[] positions) {
        int maxPosition = Integer.MIN_VALUE;
        for (int position : positions) {
            maxPosition = Math.max(maxPosition, position);
        }
        return maxPosition;
    }

    private void refreshFooterViewSpace() {
        if (mFooterView == null) {
            return;
        }
        FrameLayout parent = (FrameLayout) mFooterView.getParent();
        LayoutManager layoutManager = getLayoutManager();
        int itemCount = layoutManager.getItemCount();
        if (itemCount > layoutManager.getChildCount()) {
            parent.setPadding(0, 0, 0, 0);
            return;
        }
        int position = getLastCompletelyVisiblePosition(layoutManager);
        View lastView = getChildAt(position);
        if (lastView == null) {
            return;
        }
        int bottom = lastView.getBottom();
        if (bottom <= mFooterHeight) {
            parent.setPadding(0, 0, 0, 0);
        } else {
            parent.setPadding(0, layoutManager.getHeight() - bottom - getPaddingBottom(), 0, 0);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public interface OnLoadMoreListener {
        void onLoadMore(int currentPage);
    }
}
