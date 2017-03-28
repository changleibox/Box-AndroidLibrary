/*
 * Copyright © All right reserved by CHANGLEI.
 */

package net.izhuo.app.library.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.izhuo.app.library.IContext;
import net.izhuo.app.library.listener.IOnItemClickListener;
import net.izhuo.app.library.listener.IOnItemLongClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Box on 16/8/25.
 * <p/>
 * RecycleView.Adapter基类
 */
@SuppressWarnings({"unused"})
public abstract class IBaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private final Object mLock = new Object();
    private boolean mNotifyOnChange = true;

    private List<T> mObjects = new ArrayList<>();
    private IContext mContext;

    private IOnItemClickListener mClickListener;

    private IOnItemLongClickListener mLongClickListener;

    public IBaseAdapter(IContext iContext) {
        this.mContext = iContext;
    }

    public IContext getIContext() {
        return mContext;
    }

    public Context getContext() {
        return getIContext().getContext();
    }

    public Activity getActivity() {
        return getIContext().getActivity();
    }

    public void notifyDataSetChangedBase() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    public void notifyItemChangedBase(int position) {
        super.notifyItemChanged(position);
        mNotifyOnChange = true;
    }

    public void notifyItemChangedBase(int position, Object payload) {
        super.notifyItemChanged(position, payload);
        mNotifyOnChange = true;
    }

    public void notifyItemRangeChangedBase(int positionStart, int itemCount) {
        super.notifyItemRangeChanged(positionStart, itemCount);
        mNotifyOnChange = true;
    }

    public void notifyItemRangeChangedBase(int positionStart, int itemCount, Object payload) {
        super.notifyItemRangeChanged(positionStart, itemCount, payload);
        mNotifyOnChange = true;
    }

    public void notifyItemInsertedBase(int position) {
        super.notifyItemInserted(position);
        mNotifyOnChange = true;
    }

    public void notifyItemMovedBase(int fromPosition, int toPosition) {
        super.notifyItemMoved(fromPosition, toPosition);
        mNotifyOnChange = true;
    }

    public void notifyItemRangeInsertedBase(int positionStart, int itemCount) {
        super.notifyItemRangeInserted(positionStart, itemCount);
        mNotifyOnChange = true;
    }

    public void notifyItemRemovedBase(int position) {
        super.notifyItemRemoved(position);
        mNotifyOnChange = true;
    }

    public void notifyItemRangeRemovedBase(int positionStart, int itemCount) {
        super.notifyItemRangeRemoved(positionStart, itemCount);
        mNotifyOnChange = true;
    }

    public void add(T object) {
        synchronized (mLock) {
            mObjects.add(object);
        }
        if (mNotifyOnChange) notifyItemChangedBase(mObjects.indexOf(object));
    }

    public void addAll(Collection<T> collection) {
        int positionStart = mObjects.size();
        synchronized (mLock) {
            mObjects.addAll(collection);
        }
        if (mNotifyOnChange) {
            if (positionStart == 0) {
                notifyDataSetChanged();
            } else {
                notifyItemRangeChangedBase(positionStart, collection.size());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void addAll(T... items) {
        int positionStart = mObjects.size();
        synchronized (mLock) {
            Collections.addAll(mObjects, items);
        }
        if (mNotifyOnChange) {
            if (positionStart == 0) {
                notifyDataSetChanged();
            } else {
                notifyItemRangeChangedBase(positionStart, items.length);
            }
        }
    }

    public void insert(T object, int index) {
        synchronized (mLock) {
            mObjects.add(index, object);
        }
        if (mNotifyOnChange) notifyItemInsertedBase(index);
    }

    public void remove(T object) {
        int index = mObjects.indexOf(object);
        synchronized (mLock) {
            mObjects.remove(object);
        }
        if (mNotifyOnChange) notifyItemRemovedBase(index);
    }

    public void clear() {
        int itemCount = mObjects.size();
        synchronized (mLock) {
            mObjects.clear();
        }
        if (mNotifyOnChange) notifyItemRangeRemovedBase(0, itemCount);
    }

    public void sort(Comparator<? super T> comparator) {
        synchronized (mLock) {
            Collections.sort(mObjects, comparator);
        }
        if (mNotifyOnChange) notifyDataSetChangedBase();
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    @Override
    public int getItemCount() {
        return size();
    }

    public T getItem(int position) {
        return mObjects.get(position);
    }

    public int getPosition(T item) {
        return mObjects.indexOf(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public final int size() {
        return mObjects.size();
    }

    public abstract void onBindViewHolder(VH holder, int position, int viewType);

    @Override
    public final void onBindViewHolder(final VH holder, final int position) {
        if (mClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    mClickListener.onItemClick(IBaseAdapter.this, v, adapterPosition, getItemId(adapterPosition));
                }
            });
        } else {
            holder.itemView.setOnClickListener(null);
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    return mLongClickListener.onItemLongClick(IBaseAdapter.this, v, adapterPosition, getItemId(adapterPosition));
                }
            });
        } else {
            holder.itemView.setOnLongClickListener(null);
        }
        onBindViewHolder(holder, position, getItemViewType(position));
    }

    public void setOnItemClickListener(IOnItemClickListener listener) {
        this.mClickListener = listener;
        notifyDataSetChanged();
    }

    public void setOnItemLongClickListener(IOnItemLongClickListener listener) {
        this.mLongClickListener = listener;
        notifyDataSetChanged();
    }
}
