/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewCompat;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

/**
 * Created by box on 2017/5/16.
 * <p>
 * headerView adapter
 */

@SuppressWarnings("WeakerAccess")
public class IHeaderViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private final RecyclerView.Adapter<? super RecyclerView.ViewHolder> mAdapter;

    ArrayList<RecyclerViewCompat.FixedViewInfo> mHeaderViewInfos;
    ArrayList<RecyclerViewCompat.FixedViewInfo> mFooterViewInfos;

    static final ArrayList<RecyclerViewCompat.FixedViewInfo> EMPTY_INFO_LIST = new ArrayList<>();

    private final boolean isFilterable;
    private boolean isStaggered;

    public IHeaderViewAdapter(ArrayList<RecyclerViewCompat.FixedViewInfo> headerViewInfos,
                              ArrayList<RecyclerViewCompat.FixedViewInfo> footerViewInfos,
                              RecyclerView.Adapter<? super RecyclerView.ViewHolder> adapter) {
        mAdapter = adapter;
        isFilterable = adapter instanceof Filterable;

        if (headerViewInfos == null) {
            mHeaderViewInfos = EMPTY_INFO_LIST;
        } else {
            mHeaderViewInfos = headerViewInfos;
        }

        if (footerViewInfos == null) {
            mFooterViewInfos = EMPTY_INFO_LIST;
        } else {
            mFooterViewInfos = footerViewInfos;
        }
    }

    public int getHeadersCount() {
        return mHeaderViewInfos.size();
    }

    public int getFootersCount() {
        return mFooterViewInfos.size();
    }

    @Override
    public int getItemCount() {
        if (mAdapter != null) {
            return getFootersCount() + getHeadersCount() + mAdapter.getItemCount();
        } else {
            return getFootersCount() + getHeadersCount();
        }
    }

    public boolean isEmpty() {
        return mAdapter == null || mAdapter.getItemCount() == 0;
    }

    public boolean removeHeader(View v) {
        for (int i = 0; i < mHeaderViewInfos.size(); i++) {
            RecyclerViewCompat.FixedViewInfo info = mHeaderViewInfos.get(i);
            if (info.view == v) {
                mHeaderViewInfos.remove(i);
                return true;
            }
        }

        return false;
    }

    public boolean removeFooter(View v) {
        for (int i = 0; i < mFooterViewInfos.size(); i++) {
            RecyclerViewCompat.FixedViewInfo info = mFooterViewInfos.get(i);
            if (info.view == v) {
                mFooterViewInfos.remove(i);
                return true;
            }
        }

        return false;
    }

    @Override
    public long getItemId(int position) {
        int numHeaders = getHeadersCount();
        if (mAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    @Override
    public int getItemViewType(int position) {
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return mHeaderViewInfos.get(position).viewType;
        }
        int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }

        return mFooterViewInfos.get(position - adapterCount - getHeadersCount()).viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType <= RecyclerViewCompat.ITEM_VIEW_TYPE_HEADER + getHeadersCount()) {
            return getViewHolder(mHeaderViewInfos.get(viewType - RecyclerViewCompat.ITEM_VIEW_TYPE_HEADER).view);
        }
        if (viewType >= RecyclerViewCompat.ITEM_VIEW_TYPE_FOOTER - getFootersCount()) {
            return getViewHolder(mFooterViewInfos.get(RecyclerViewCompat.ITEM_VIEW_TYPE_FOOTER - viewType).view);
        }
        if (mAdapter != null) {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return;
        }
        int adjPosition = position - numHeaders;
        if (mAdapter != null) {
            if (adjPosition < mAdapter.getItemCount()) {
                mAdapter.onBindViewHolder(holder, adjPosition);
            }
        }
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (mAdapter != null) {
            mAdapter.registerAdapterDataObserver(observer);
        }
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(observer);
        }
    }

    @Override
    public Filter getFilter() {
        if (isFilterable) {
            return ((Filterable) mAdapter).getFilter();
        }
        return null;
    }

    public void adjustSpanSize(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int numHeaders = getHeadersCount();
                    int adjPosition = position - numHeaders;
                    if (position < numHeaders || adjPosition >= mAdapter.getItemCount())
                        return manager.getSpanCount();
                    return 1;
                }
            });
        }

        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            isStaggered = true;
        }
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mAdapter.onAttachedToRecyclerView(recyclerView);
    }

    private RecyclerView.ViewHolder getViewHolder(View itemView) {
        if (isStaggered) {
            StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setFullSpan(true);
            itemView.setLayoutParams(params);
        }
        return new HeaderFooterViewHolder(itemView);
    }

    private class HeaderFooterViewHolder extends RecyclerView.ViewHolder {

        public HeaderFooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
