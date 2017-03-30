/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.listener;

import android.view.View;

import net.izhuo.app.library.adapter.IBaseAdapter;

/**
 * Created by Box on 16/8/27.
 * <p/>
 * RecyclerView Item长按事件
 */
public interface IOnItemLongClickListener {

    boolean onItemLongClick(IBaseAdapter parent, View view, int position, long id);

}
