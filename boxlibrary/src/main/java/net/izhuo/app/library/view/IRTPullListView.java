/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.izhuo.app.library.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 下拉刷新和上划加载更多的ListView
 *
 * @author Changlei
 *         <p>
 *         2014年7月29日
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class IRTPullListView extends ListView implements OnScrollListener, OnClickListener {

    private final static int RELEASE_To_REFRESH = 0;
    private final static int PULL_To_REFRESH = 1;
    private final static int REFRESHING = 2;
    private final static int DONE = 3;
    private final static int LOADING = 4;
    private final static int LOAD_NEW_INFO = 5;
    private final static int LOAD_MORE_SUCCESS = 3;
    private final static int RATIO = 3;
    private final static int GET_MORE_DONE = 6;
    private final static int GET_MORE = 7;

    private LinearLayout headView;
    private RelativeLayout footerView;
    private ProgressBar moreProgressBar;
    private TextView mTextView;
    private TextView tipsTextview;
    private TextView lastUpdatedTextView;
    private ImageView arrowImageView;
    private ProgressBar progressBar;
    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;
    private boolean isRecored;
    private int headContentHeight;
    private int startY;
    private int firstItemIndex;
    private int refresh_state;
    private int get_state;
    private boolean isBack;
    private OnRefreshListener refreshListener;
    private OnGetMoreListener getMoreListener;
    private boolean isRefreshable;
    private boolean isPush;
    private int visibleLastIndex;
    private int visibleItemCount;
    private ListAdapter mAdapter;
    private Context mContext;
    private int pageSize = 1;
    private boolean isLoadFull;
    private boolean isSetRefreshable;

    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;

    public IRTPullListView(Context context) {
        super(context);
        this.mContext = context;
        init(context);
    }

    public IRTPullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context);
    }

    /**
     * 初始化界面及数据
     *
     * @param context
     */
    @SuppressLint("InflateParams")
    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        headView = (LinearLayout) inflater.inflate(R.layout.box_view_pulllist_head, null);
        arrowImageView = (ImageView) headView.findViewById(R.id.box_head_arrowImageView);
        progressBar = (ProgressBar) headView.findViewById(R.id.box_head_progressBar);
        tipsTextview = (TextView) headView.findViewById(R.id.box_head_tipsTextView);
        lastUpdatedTextView = (TextView) headView.findViewById(R.id.box_head_lastUpdatedTextView);
        footerView = (RelativeLayout) inflater.inflate(R.layout.box_view_list_footview, null);
        moreProgressBar = (ProgressBar) footerView.findViewById(R.id.box_footer_progress);
        mTextView = (TextView) footerView.findViewById(R.id.box_text_view);

        measureView(headView);
        headContentHeight = headView.getMeasuredHeight();

        headView.setPadding(0, -1 * headContentHeight, 0, 0);
        headView.invalidate();

        addHeaderView(headView, null, false);
        setOnScrollListener(this);

        // addFooterView(footerView);
        footerView.setOnClickListener(this);

        animation = new RotateAnimation(0, 180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(250);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(180, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(200);
        reverseAnimation.setFillAfter(true);

        refresh_state = DONE;
        get_state = GET_MORE;
        isRefreshable = false;
        isPush = true;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (mAdapter != null) {
            throw new IllegalStateException(
                    "Cannot add header view to list -- setAdapter has already been called.");
        }
        this.pageSize = pageSize;
    }

    public View getFooterView() {
        if (footerView != null) {
            return footerView;
        }
        return null;
    }

    public ListAdapter getListAdapter() {
        return mAdapter;
    }

    @Override
    public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2, int arg3) {
        firstItemIndex = firstVisiableItem;
        visibleLastIndex = firstVisiableItem + arg2 - 1;
        visibleItemCount = arg2;
        if (firstItemIndex == 1 && !isPush) {
            setSelection(0);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {
        try {
            if (arg1 == OnScrollListener.SCROLL_STATE_IDLE && !isLoadFull
                    && arg0.getLastVisiblePosition() == arg0.getPositionForView(getFooterView())) {
                onGetMore();
            }
        } catch (Exception e) {
            // nothing
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isRefreshable) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (firstItemIndex == 0 && !isRecored) {
                        isRecored = true;
                        isPush = true;
                        startY = (int) event.getY();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (refresh_state != REFRESHING && refresh_state != LOADING) {
                        //noinspection StatementWithEmptyBody
                        if (refresh_state == DONE) {
                        }
                        if (refresh_state == PULL_To_REFRESH) {
                            refresh_state = DONE;
                            changeHeaderViewByState();
                        }
                        if (refresh_state == RELEASE_To_REFRESH) {
                            refresh_state = REFRESHING;
                            changeHeaderViewByState();
                            onRefresh();
                        }
                    }
                    isRecored = false;
                    isBack = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int tempY = (int) event.getY();
                    if (!isRecored && firstItemIndex == 0) {
                        isRecored = true;
                        startY = tempY;
                    }
                    if (refresh_state != REFRESHING && isRecored && refresh_state != LOADING) {
                        if (refresh_state == RELEASE_To_REFRESH) {
                            setSelection(0);
                            if (((tempY - startY) / RATIO < headContentHeight) && (tempY - startY) > 0) {
                                refresh_state = PULL_To_REFRESH;
                                changeHeaderViewByState();
                            } else if (tempY - startY <= 0) {
                                refresh_state = DONE;
                                changeHeaderViewByState();
                            }
                        }
                        if (refresh_state == PULL_To_REFRESH) {
                            setSelection(0);
                            if ((tempY - startY) / RATIO >= headContentHeight) {
                                refresh_state = RELEASE_To_REFRESH;
                                isBack = true;
                                changeHeaderViewByState();
                            } else if (tempY - startY <= 0) {
                                refresh_state = DONE;
                                changeHeaderViewByState();
                                isPush = false;
                            }
                        }
                        if (refresh_state == DONE) {
                            if (tempY - startY > 0) {
                                refresh_state = PULL_To_REFRESH;
                                changeHeaderViewByState();
                            }
                        }
                        if (refresh_state == PULL_To_REFRESH) {
                            headView.setPadding(0, -1 * headContentHeight + (tempY - startY) / RATIO, 0, 0);
                        }
                        if (refresh_state == RELEASE_To_REFRESH) {
                            headView.setPadding(0, (tempY - startY) / RATIO - headContentHeight, 0, 0);
                        }
                    }
                    break;
            }
        }
        try {
            return super.onTouchEvent(event);
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 根据此时headView的状态，改变headView
     */
    private void changeHeaderViewByState() {
        switch (refresh_state) {
            case RELEASE_To_REFRESH:
                arrowImageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.startAnimation(animation);
                tipsTextview.setText(getResources().getString(
                        R.string.box_lable_release_to_refresh));
                break;
            case PULL_To_REFRESH:
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.VISIBLE);
                if (isBack) {
                    isBack = false;
                    arrowImageView.clearAnimation();
                    arrowImageView.startAnimation(reverseAnimation);
                    tipsTextview.setText(getResources().getString(
                            R.string.box_lable_pull_to_refresh));
                } else {
                    tipsTextview.setText(getResources().getString(
                            R.string.box_lable_pull_to_refresh));
                }
                break;
            case REFRESHING:
                headView.setPadding(0, 0, 0, 0);
                progressBar.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.GONE);
                tipsTextview.setText(getResources().getString(
                        R.string.box_lable_refreshing));
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                break;
            case DONE:
                headView.setPadding(0, -1 * headContentHeight, 0, 0);
                progressBar.setVisibility(View.GONE);
                arrowImageView.clearAnimation();
                arrowImageView.setImageResource(R.drawable.box_img_pulltorefresh);
                tipsTextview.setText(getResources().getString(
                        R.string.box_lable_pull_to_refresh));
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 设置ListView选中最后一个
     */
    private void setSelectionfoot() {
        this.setSelection(visibleLastIndex - visibleItemCount + 1);
    }

    /**
     * 设置下拉刷新事件回调
     *
     * @param refreshListener 回调接口
     */
    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
        if (!isSetRefreshable) {
            isRefreshable = true;
        }
    }

    public boolean isRefreshable() {
        return isRefreshable;
    }

    public void setRefreshable(boolean isRefreshable) {
        this.isRefreshable = isRefreshable;
        this.isSetRefreshable = true;
    }

    /**
     * 设置上划加载事件回调
     *
     * @param getMoreListener 回调接口
     */
    public void setOnGetMoreListener(OnGetMoreListener getMoreListener) {
        this.getMoreListener = getMoreListener;
    }

    /**
     * 下拉刷新事件回调接口
     *
     * @author Changlei
     *         <p>
     *         2014年8月1日
     */
    public interface OnRefreshListener {
        void onRefresh();
    }

    /**
     * 上划加载更多事件回调接口
     *
     * @author Changlei
     *         <p>
     *         2014年8月1日
     */
    public interface OnGetMoreListener {
        void onGetMore();
    }

    /**
     * 刷新完成以后调用此方法
     */
    private void onRefreshComplete(int resultSize) {
        refresh_state = DONE;
        lastUpdatedTextView.setText(getResources().getString(
                R.string.box_lable_updating, getCurrentTime()));
        changeHeaderViewByState();
        invalidateViews();
        setSelection(0);
        setResultSize(resultSize);
    }

    /**
     * 加载完成以后调用此方法
     */
    private void onGetMoreComplete(int resultSize) {
        get_state = GET_MORE;
        moreProgressBar.setVisibility(View.GONE);
        mTextView.setText(mContext.getString(R.string.box_btn_get_more));
        setSelectionfoot();
        setResultSize(resultSize);
    }

    /**
     * 这个方法是根据结果的大小来决定footer显示的。
     * <p>
     * 这里假定每次请求的条数为10。如果请求到了10条。则认为还有数据。如过结果不足10条，则认为数据已经全部加载，这时footer显示已经全部加载
     * </p>
     *
     * @param resultSize
     */
    private void setResultSize(int resultSize) {
        if (resultSize == 0) {
            mTextView.setText(mContext.getString(R.string.box_lable_load_all));
            isLoadFull = true;
        } else if (resultSize > 0 && resultSize < getPageSize()) {
            mTextView.setText(mContext.getString(R.string.box_lable_load_all));
            isLoadFull = true;
        } else {
            mTextView.setText(mContext.getString(R.string.box_btn_get_more));
            isLoadFull = false;
        }
        if (getFooterViewsCount() > 0) {
            removeFooterView(footerView);
        }
        Log.d("RTPullListView", "pageSize====>" + getPageSize() + "\ncount"
                + getCount());
        if (getCount() - getHeaderViewsCount() - getFooterViewsCount() >= getPageSize()) {
            addFooterView(footerView);
        }
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    private String getCurrentTime() {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("GMT+8"))
                .format(date);
    }

    /**
     * 刷新方法
     */
    private void onRefresh() {
        if (refreshListener != null) {
            new Thread() {
                public void run() {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        public void run() {
                            removeSingleFooterView();
                            refreshListener.onRefresh();
                        }
                    });
                }
            }.start();
        }
    }

    /**
     * 获取更多方法
     */
    private void onGetMore() {
        if (get_state == GET_MORE) {
            get_state = GET_MORE_DONE;
            if (getMoreListener != null) {
                moreProgressBar.setVisibility(View.VISIBLE);
                mTextView.setText(mContext.getString(R.string.box_lable_get_load));
                new Thread() {
                    public void run() {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            public void run() {
                                getMoreListener.onGetMore();
                            }
                        });
                    }
                }.start();
            }
        }
    }

    private void setRefresh(int resultSize) {
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = LOAD_NEW_INFO;
            msg.obj = resultSize;
            mHandler.sendMessage(msg);
        }
    }

    private void setGetMore(int resultSize) {
        if (mHandler != null) {
            Message msg = new Message();
            msg.what = LOAD_MORE_SUCCESS;
            msg.obj = resultSize;
            mHandler.sendMessage(msg);
        }
    }

    public void sendHandle(int resultSize) {
        setRefresh(resultSize);
        setGetMore(resultSize);
    }

    /**
     * 点击刷新
     */
    public void clickToRefresh() {
        refresh_state = REFRESHING;
        changeHeaderViewByState();
        onRefresh();
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(ListAdapter adapter) {
        lastUpdatedTextView.setText(getResources().getString(
                R.string.box_lable_updating, getCurrentTime()));
        this.mAdapter = adapter;
        super.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (!isLoadFull) {
            onGetMore();
        }
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int resultSize;
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_MORE_SUCCESS:
                    if (msg.obj != null) {
                        resultSize = (Integer) msg.obj;
                    } else {
                        resultSize = 0;
                    }
                    onGetMoreComplete(resultSize);
                    break;
                case LOAD_NEW_INFO:
                    if (msg.obj != null) {
                        resultSize = (Integer) msg.obj;
                    } else {
                        resultSize = 0;
                    }
                    onRefreshComplete(resultSize);
                    break;
                default:
                    break;
            }
            ((BaseAdapter) mAdapter).notifyDataSetChanged();
        }
    };

    public void removeSingleFooterView() {
        if (footerView != null && getFooterViewsCount() > 0) {
            removeFooterView(footerView);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (xDistance > yDistance) {
                    return false; // 表示向下传递事件
                }
        }

        return super.onInterceptTouchEvent(ev);
    }

}