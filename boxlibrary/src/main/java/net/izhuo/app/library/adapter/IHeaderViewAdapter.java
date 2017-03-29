/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.izhuo.app.library.IContext;
import net.izhuo.app.library.R;
import net.izhuo.app.library.listener.IOnItemClickListener;
import net.izhuo.app.library.listener.IOnItemLongClickListener;
import net.izhuo.app.library.viewholder.IHeaderViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Box on 16/8/25.
 * <p>
 * 有HeaderView的Adapter
 */
@SuppressWarnings("unused")
public abstract class IHeaderViewAdapter<T, VH extends IHeaderViewHolder> extends IBaseAdapter<T, VH> {

    private List<View> mHeaderViews = new ArrayList<>();
    private LayoutInflater mInflater;

    private IOnItemClickListener mClickListener;

    private IOnItemLongClickListener mLongClickListener;

    public IHeaderViewAdapter(IContext iContext) {
        super(iContext);
        mInflater = LayoutInflater.from(getContext());
    }

    public void addHeaderView(View headerView) {
        mHeaderViews.add(headerView);
        notifyHeaderViews();
    }

    public final void notifyHeaderViews() {
        notifyItemChanged(0);
    }

    @Override
    public final void notifyDataSetChangedBase() {
        super.notifyItemRangeChangedBase(0, size());
    }

    @Override
    public final void notifyItemChangedBase(int position) {
        super.notifyItemChangedBase(position + 1);
    }

    @Override
    public final void notifyItemChangedBase(int position, Object payload) {
        super.notifyItemChangedBase(position + 1, payload);
    }

    @Override
    public final void notifyItemRangeChangedBase(int positionStart, int itemCount) {
        super.notifyItemRangeChangedBase(positionStart + 1, itemCount);
    }

    @Override
    public final void notifyItemRangeChangedBase(int positionStart, int itemCount, Object payload) {
        super.notifyItemRangeChangedBase(positionStart + 1, itemCount, payload);
    }

    @Override
    public final void notifyItemInsertedBase(int position) {
        super.notifyItemInsertedBase(position + 1);
    }

    @Override
    public final void notifyItemMovedBase(int fromPosition, int toPosition) {
        super.notifyItemMovedBase(fromPosition + 1, toPosition + 1);
    }

    @Override
    public final void notifyItemRangeInsertedBase(int positionStart, int itemCount) {
        super.notifyItemRangeInsertedBase(positionStart + 1, itemCount);
    }

    @Override
    public final void notifyItemRemovedBase(int position) {
        super.notifyItemRemoved(position + 1);
    }

    @Override
    public final void notifyItemRangeRemovedBase(int positionStart, int itemCount) {
        super.notifyItemRangeRemoved(positionStart + 1, itemCount);
    }

    @Override
    public int getItemCount() {
        return super.size() + 1;
    }

    @Override
    public final int getItemViewType(int position) {
        if (position == 0) {
            return IHeaderViewHolder.TYPE_HEADER_VIEW;
        }
        return getViewType(position - 1);
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == IHeaderViewHolder.TYPE_HEADER_VIEW) {
            View view = View.inflate(getContext(), R.layout.box_view_header_view_container, null);
            //noinspection unchecked
            return (VH) new IHeaderViewHolder(view, viewType);
        }
        return onCreateViewHolder(parent, mInflater, viewType);
    }

    @Override
    public final void onBindViewHolder(final VH holder, int position, int viewType) {
        if (viewType == IHeaderViewHolder.TYPE_HEADER_VIEW) {
            int headerViewSize = mHeaderViews.size();
            holder.headerContainer.setVisibility(headerViewSize > 0 ? View.VISIBLE : View.GONE);

            int childCount = holder.headerContainer.getChildCount();
            if (headerViewSize != childCount) {
                for (View headerView : mHeaderViews) {
                    ViewGroup parent = (ViewGroup) headerView.getParent();
                    if (holder.headerContainer.equals(parent)) {
                        continue;
                    }
                    if (parent != null) {
                        parent.removeView(headerView);
                    }
                    holder.headerContainer.addView(headerView);
                }
            }
        } else {
            if (mClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = holder.getBaseAdapterPosition();
                        mClickListener.onItemClick(IHeaderViewAdapter.this, v, adapterPosition, getItemId(adapterPosition));
                    }
                });
            } else {
                holder.itemView.setOnClickListener(null);
            }
            if (mLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int adapterPosition = holder.getBaseAdapterPosition();
                        return mLongClickListener.onItemLongClick(IHeaderViewAdapter.this, v, adapterPosition, getItemId(adapterPosition));
                    }
                });
            } else {
                holder.itemView.setOnLongClickListener(null);
            }
            onHeaderBindViewHolder(holder, position - 1, viewType);
        }
    }

    @Override
    public void setOnItemClickListener(IOnItemClickListener listener) {
        super.setOnItemClickListener(null);
        this.mClickListener = listener;
        notifyDataSetChanged();
    }

    @Override
    public void setOnItemLongClickListener(IOnItemLongClickListener listener) {
        super.setOnItemLongClickListener(null);
        this.mLongClickListener = listener;
        notifyDataSetChanged();
    }

    public int getViewType(@SuppressWarnings("UnusedParameters") int position) {
        return 0;
    }

    public abstract VH onCreateViewHolder(ViewGroup parent, LayoutInflater inflater, int viewType);

    public abstract void onHeaderBindViewHolder(VH holder, int position, int viewType);
}
