/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package androidx.recyclerview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

import androidx.annotation.Nullable;

import net.box.app.library.adapter.IHeaderViewAdapter;

import java.util.ArrayList;

/**
 * Created by box on 2017/5/16.
 * <p>
 * 实现可添加头尾的recyclerView
 */

@SuppressWarnings({"unused"})
public class RecyclerViewCompat extends RecyclerView {

    public static final int ITEM_VIEW_TYPE_HEADER = Integer.MIN_VALUE;
    public static final int ITEM_VIEW_TYPE_FOOTER = Integer.MAX_VALUE;

    public class FixedViewInfo {
        public View view;
        public Object data;
        public int viewType;
    }

    ArrayList<FixedViewInfo> mHeaderViewInfos = new ArrayList<>();
    ArrayList<FixedViewInfo> mFooterViewInfos = new ArrayList<>();

    private Adapter<?> mAdapter;
    private boolean isShouldSpan;

    private AdapterContextMenuInfo mContextMenuInfo;

    public RecyclerViewCompat(Context context) {
        super(context);
    }

    public RecyclerViewCompat(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewCompat(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addHeaderView(View v, Object data) {
        final FixedViewInfo info = new FixedViewInfo();
        info.view = v;
        info.data = data;
        info.viewType = ITEM_VIEW_TYPE_HEADER + mHeaderViewInfos.size();
        mHeaderViewInfos.add(info);

        // Wrap the adapter if it wasn't already wrapped.
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void addHeaderView(View v) {
        addHeaderView(v, null);
    }

    public int getHeaderViewsCount() {
        return mHeaderViewInfos.size();
    }

    public boolean removeHeaderView(View v) {
        if (mHeaderViewInfos.size() > 0) {
            boolean result = false;
            if (mAdapter != null && ((IHeaderViewAdapter) mAdapter).removeHeader(v)) {
                mAdapter.notifyDataSetChanged();
                result = true;
            }
            removeFixedViewInfo(v, mHeaderViewInfos);
            return result;
        }
        return false;
    }

    private void removeFixedViewInfo(View v, ArrayList<FixedViewInfo> where) {
        int len = where.size();
        for (int i = 0; i < len; ++i) {
            FixedViewInfo info = where.get(i);
            if (info.view == v) {
                where.remove(i);
                break;
            }
        }
    }

    public void addFooterView(View v, Object data) {
        final FixedViewInfo info = new FixedViewInfo();
        info.view = v;
        info.data = data;
        info.viewType = ITEM_VIEW_TYPE_FOOTER - mFooterViewInfos.size();
        mFooterViewInfos.add(info);

        // Wrap the adapter if it wasn't already wrapped.
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void addFooterView(View v) {
        addFooterView(v, null);
    }

    public int getFooterViewsCount() {
        return mFooterViewInfos.size();
    }

    public boolean removeFooterView(View v) {
        if (mFooterViewInfos.size() > 0) {
            boolean result = false;
            if (mAdapter != null && ((IHeaderViewAdapter) mAdapter).removeFooter(v)) {
                mAdapter.notifyDataSetChanged();
                result = true;
            }
            removeFixedViewInfo(v, mFooterViewInfos);
            return result;
        }
        return false;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (!(adapter instanceof IHeaderViewAdapter)) {
            //noinspection unchecked
            mAdapter = new IHeaderViewAdapter(mHeaderViewInfos, mFooterViewInfos, adapter);
        } else {
            mAdapter = adapter;
        }
        super.setAdapter(mAdapter);
        if (isShouldSpan) {
            ((IHeaderViewAdapter) mAdapter).adjustSpanSize(this);
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (layout instanceof GridLayoutManager || layout instanceof StaggeredGridLayoutManager) {
            isShouldSpan = true;
        }
        super.setLayoutManager(layout);
    }

    @Override
    int getAdapterPositionFor(ViewHolder viewHolder) {
        if (viewHolder.hasAnyOfTheFlags(ViewHolder.FLAG_INVALID |
                ViewHolder.FLAG_REMOVED | ViewHolder.FLAG_ADAPTER_POSITION_UNKNOWN)
                || !viewHolder.isBound()) {
            return RecyclerView.NO_POSITION;
        }
        return mAdapterHelper.applyPendingUpdatesToPosition(viewHolder.mPosition) - getHeaderViewsCount();
    }

    public ViewHolder findViewHolderForAdapterPosition(int position) {
        if (mDataSetHasChangedAfterLayout) {
            return null;
        }
        final int childCount = mChildHelper.getUnfilteredChildCount();
        // hidden VHs are not preferred but if that is the only one we find, we rather return it
        ViewHolder hidden = null;
        for (int i = 0; i < childCount; i++) {
            final ViewHolder holder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && !holder.isRemoved() && super.getAdapterPositionFor(holder) == position) {
                if (mChildHelper.isHidden(holder.itemView)) {
                    hidden = holder;
                } else {
                    return holder;
                }
            }
        }
        return hidden;
    }

    @Override
    protected AdapterContextMenuInfo getContextMenuInfo() {
        return mContextMenuInfo;
    }

    @Override
    public boolean showContextMenuForChild(View originalView) {
        final int position = getChildAdapterPosition(originalView);
        final long itemId = getChildItemId(originalView);
        mContextMenuInfo = new AdapterContextMenuInfo(originalView, position, itemId);
        return super.showContextMenuForChild(originalView);
    }
}
