/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.listener;

import android.view.View;

import net.box.app.library.adapter.IArrayAdapter;

/**
 * Created by Box on 16/8/27.
 * <p>
 * RecyclerView Item长按事件
 */
public interface IOnItemLongClickListener {

    boolean onItemLongClick(IArrayAdapter<?, ?> parent, View view, int position, long id);

}
