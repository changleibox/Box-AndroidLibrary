/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.helper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by Box on 16/8/27.
 * <p/>
 * recyclerView滚动工具
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class IScrollHelper extends RecyclerView.OnScrollListener {

    private RecyclerView mRecyclerView;
    @Nullable
    private AppBarLayout mAppBarLayout;

    private RecyclerView.LayoutManager mLayoutManager;

    private boolean move;
    private int mPosition;

    public IScrollHelper(@NonNull RecyclerView recyclerView) {
        this(recyclerView, null);
    }

    public IScrollHelper(@NonNull RecyclerView recyclerView, @Nullable AppBarLayout appBarLayout) {
        this.mRecyclerView = recyclerView;
        this.mAppBarLayout = appBarLayout;

        mRecyclerView.addOnScrollListener(this);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        //在这里进行第二次滚动（最后的100米！）
        if (move) {
            move = false;
            int firstVisiablePosition = 0;
            if (mLayoutManager instanceof LinearLayoutManager) {
                firstVisiablePosition = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                firstVisiablePosition = findMin(((StaggeredGridLayoutManager) mLayoutManager).findFirstVisibleItemPositions(null));
            }
            //获取要置顶的项在当前屏幕的位置，mPosition是记录的要置顶项在RecyclerView中的位置
            int position = mPosition - firstVisiablePosition;
            if (0 <= position && position < mRecyclerView.getChildCount()) {
                //获取要置顶的项顶部离RecyclerView顶部的距离
                int top = mRecyclerView.getChildAt(position).getTop();
                //最后的移动
                mRecyclerView.scrollBy(0, top);
            }
        }
    }

    public void moveToPosition(int position) {
        if (mLayoutManager == null) {
            mLayoutManager = mRecyclerView.getLayoutManager();
        }

        mPosition = position;

        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstPosition = 0;
        int lastPosition = 0;
        if (mLayoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) this.mLayoutManager;
            firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
            lastPosition = linearLayoutManager.findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) this.mLayoutManager;
            firstPosition = findMin(staggeredGridLayoutManager.findFirstVisibleItemPositions(null));
            lastPosition = findMax(staggeredGridLayoutManager.findLastVisibleItemPositions(null));
        }
        //然后区分情况
        if (position <= firstPosition) {
            if (mAppBarLayout != null) {
                mAppBarLayout.setExpanded(true);
            }
            //当要置顶的项在当前显示的第一个项的前面时
            mRecyclerView.scrollToPosition(position);
        } else if (position <= lastPosition) {
            if (mAppBarLayout != null) {
                mAppBarLayout.setExpanded(false);
            }
            //当要置顶的项已经在屏幕上显示时
            mRecyclerView.scrollBy(0, mRecyclerView.getChildAt(position - firstPosition).getTop());
        } else {
            if (mAppBarLayout != null) {
                mAppBarLayout.setExpanded(false);
            }
            //当要置顶的项在当前显示的最后一项的后面时
            mRecyclerView.scrollToPosition(position);
            //这里这个变量是用在RecyclerView滚动监听里面的
            move = true;
        }
    }

    private int findMin(int[] firstPositions) {
        int min = firstPositions[0];
        for (int value : firstPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

}
