/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.adapter;

import android.annotation.SuppressLint;
import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import net.box.app.library.helper.IAppHelper;
import net.box.app.library.listener.IOnItemClickListener;
import net.box.app.library.listener.IOnItemLongClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Box on 17/3/15.
 * <p>
 * 自定义地RecyclerView.Adapter
 */
@SuppressWarnings({"unused", "WeakerAccess", "unchecked"})
public abstract class IArrayAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements Filterable {

    private final Object mLock = new Object();

    private boolean mNotifyOnChange = true;

    private List<T> mObjects;
    private ArrayList<T> mOriginalValues;

    private ArrayFilter mFilter;

    private IOnItemClickListener mClickListener;
    private IOnItemLongClickListener mLongClickListener;

    public IArrayAdapter() {
        this(new ArrayList<T>());
    }

    public IArrayAdapter(@NonNull T[] objects) {
        this(Arrays.asList(objects));
    }

    public IArrayAdapter(@NonNull Collection<T> objects) {
        mObjects = new ArrayList<>(objects);
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mNotifyOnChange = true;
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                mNotifyOnChange = true;
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                mNotifyOnChange = true;
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mNotifyOnChange = true;
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                mNotifyOnChange = true;
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                mNotifyOnChange = true;
            }
        });
    }

    public void set(Collection<? extends T> objects) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.clear();
                mOriginalValues.addAll(objects);
            } else {
                mObjects.clear();
                this.mObjects.addAll(objects);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
        onContentChanged();
    }

    public <E extends T> void set(E... items) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.clear();
                Collections.addAll(mOriginalValues, items);
            } else {
                mObjects.clear();
                Collections.addAll(mObjects, items);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
        onContentChanged();
    }

    public <E extends T> void add(E object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(object);
            } else {
                this.mObjects.add(object);
            }
        }
        if (mNotifyOnChange) notifyItemInserted(mObjects.indexOf(object));
        onContentChanged();
    }

    public void addAll(Collection<? extends T> objects) {
        int itemCount = getItemCount();
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.addAll(objects);
            } else {
                this.mObjects.addAll(objects);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
        onContentChanged();
    }

    public <E extends T> void addAll(E... items) {
        int itemCount = getItemCount();
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.addAll(mOriginalValues, items);
            } else {
                Collections.addAll(mObjects, items);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
        onContentChanged();
    }

    public <E extends T> void insert(@NonNull E object, int index) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(index, object);
            } else {
                mObjects.add(index, object);
            }
        }
        if (mNotifyOnChange) notifyItemInserted(index);
        onContentChanged();
    }

    public <E extends T> void remove(E object) {
        int index = mObjects.indexOf(object);
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.remove(object);
            } else {
                this.mObjects.remove(object);
            }
        }
        if (mNotifyOnChange) notifyItemRemoved(index);
        onContentChanged();
    }

    public void clear() {
        int itemCount = getItemCount();
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.clear();
            } else {
                this.mObjects.clear();
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
        onContentChanged();
    }

    public void sort(@NonNull Comparator<? super T> comparator) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.sort(mOriginalValues, comparator);
            } else {
                Collections.sort(mObjects, comparator);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
        onContentChanged();
    }

    public T getItem(int position) {
        if (position < 0 || position >= mObjects.size()) {
            return null;
        }
        return mObjects.get(position);
    }

    public <E extends T> int getPosition(@NonNull E item) {
        return mObjects.indexOf(item);
    }

    public List<T> getItems() {
        return Collections.unmodifiableList(mObjects);
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    public void setOnItemClickListener(IOnItemClickListener listener) {
        this.mClickListener = listener;
        notifyDataSetChanged();
    }

    public void setOnItemLongClickListener(IOnItemLongClickListener listener) {
        this.mLongClickListener = listener;
        notifyDataSetChanged();
    }

    @SuppressLint("RecyclerView")
    @CallSuper
    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {
        synchronized (mLock) {
            if (mClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mClickListener.onItemClick(IArrayAdapter.this, view, position, getItemId(position));
                    }
                });
            } else {
                holder.itemView.setOnClickListener(null);
                holder.itemView.setClickable(false);
            }
            if (mLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return mLongClickListener.onItemLongClick(IArrayAdapter.this, view, position, getItemId(position));
                    }
                });
            } else {
                holder.itemView.setOnLongClickListener(null);
                holder.itemView.setLongClickable(false);
            }
        }
    }

    public void onContentChanged() {
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    protected LayoutInflater getInflater() {
        return LayoutInflater.from(IAppHelper.getContext());
    }

    protected View inflate(@LayoutRes int id, ViewGroup parent) {
        return getInflater().inflate(id, parent, false);
    }

    /**
     * <p>An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     */
    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            final FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                final ArrayList<T> list;
                synchronized (mLock) {
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                final String prefixString = prefix.toString().toLowerCase();

                final ArrayList<T> values;
                synchronized (mLock) {
                    values = new ArrayList<>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<T> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final T value = values.get(i);
                    final String valueText = value.toString().toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");
                        for (String word : words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<T>) results.values;
            notifyDataSetChanged();
        }
    }

}
