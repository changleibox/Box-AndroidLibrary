/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.box.app.library.IContext;

/**
 * Created by Box on 16/8/25.
 * <p>
 * RecycleView.Adapter基类
 * <p>
 * 此类已过时，请使用{@link IArrayAdapter}
 */
@Deprecated
public abstract class IBaseAdapter<T, VH extends RecyclerView.ViewHolder> extends IArrayAdapter<T, VH> {

    private IContext mContext;

    @Deprecated
    public IBaseAdapter(IContext iContext) {
        this.mContext = iContext;
    }

    @Deprecated
    public IContext getIContext() {
        return mContext;
    }

    @Deprecated
    public Context getContext() {
        return getIContext().getContext();
    }

    @Deprecated
    public Activity getActivity() {
        return getIContext().getActivity();
    }

    @Deprecated
    public void notifyDataSetChangedBase() {
        super.notifyDataSetChanged();
        setNotifyOnChange(true);
    }

    @Deprecated
    public void notifyItemChangedBase(int position) {
        super.notifyItemChanged(position);
        setNotifyOnChange(true);
    }

    @Deprecated
    public void notifyItemChangedBase(int position, Object payload) {
        super.notifyItemChanged(position, payload);
        setNotifyOnChange(true);
    }

    @Deprecated
    public void notifyItemRangeChangedBase(int positionStart, int itemCount) {
        super.notifyItemRangeChanged(positionStart, itemCount);
        setNotifyOnChange(true);
    }

    @Deprecated
    public void notifyItemRangeChangedBase(int positionStart, int itemCount, Object payload) {
        super.notifyItemRangeChanged(positionStart, itemCount, payload);
        setNotifyOnChange(true);
    }

    @Deprecated
    public void notifyItemInsertedBase(int position) {
        super.notifyItemInserted(position);
        setNotifyOnChange(true);
    }

    @Deprecated
    public void notifyItemMovedBase(int fromPosition, int toPosition) {
        super.notifyItemMoved(fromPosition, toPosition);
        setNotifyOnChange(true);
    }

    @Deprecated
    public void notifyItemRangeInsertedBase(int positionStart, int itemCount) {
        super.notifyItemRangeInserted(positionStart, itemCount);
        setNotifyOnChange(true);
    }

    @Deprecated
    public void notifyItemRemovedBase(int position) {
        super.notifyItemRemoved(position);
        setNotifyOnChange(true);
    }

    @Deprecated
    public void notifyItemRangeRemovedBase(int positionStart, int itemCount) {
        super.notifyItemRangeRemoved(positionStart, itemCount);
        setNotifyOnChange(true);
    }

    @Deprecated
    public abstract void onBindViewHolder(@NonNull VH holder, int position, int viewType);

    @Deprecated
    @Override
    public final void onBindViewHolder(@NonNull final VH holder, final int position) {
        super.onBindViewHolder(holder, position);
        onBindViewHolder(holder, position, getItemViewType(position));
    }

}
