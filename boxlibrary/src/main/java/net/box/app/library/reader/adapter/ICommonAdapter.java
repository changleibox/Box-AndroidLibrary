/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.reader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import net.box.app.library.reader.picture.IViewHolder;

import java.util.List;

/**
 * @author Box
 * @version v1.0
 * <p>
 * date 2015年8月26日 下午12:03:37
 * @since Jdk1.6 或 Jdk1.7
 */
@SuppressWarnings("WeakerAccess")
public abstract class ICommonAdapter<T> extends BaseAdapter {

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas;
    protected final int mItemLayoutId;

    public ICommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final IViewHolder viewHolder = getViewHolder(position, convertView,
                parent);
        convert(position, viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }

    public abstract void convert(int position, IViewHolder helper, T item);

    private IViewHolder getViewHolder(int position, View convertView,
                                      ViewGroup parent) {
        return IViewHolder.get(mContext, convertView, parent, mItemLayoutId,
                position);
    }

    /**
     * @return the mDatas
     */
    public List<T> getDatas() {
        return mDatas;
    }

}
