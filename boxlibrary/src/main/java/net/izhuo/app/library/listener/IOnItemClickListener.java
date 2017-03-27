package net.izhuo.app.library.listener;

import android.view.View;

import net.izhuo.app.library.adapter.IBaseAdapter;

/**
 * Created by Box on 16/8/27.
 * <p>
 * RecyclerView Item点击事件
 */
public interface IOnItemClickListener {

    void onItemClick(IBaseAdapter parent, View view, int position, long id);

}
