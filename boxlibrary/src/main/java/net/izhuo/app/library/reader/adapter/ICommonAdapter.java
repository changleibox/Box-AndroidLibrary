package net.izhuo.app.library.reader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import net.izhuo.app.library.reader.picture.IViewHolder;

import java.util.List;

/**
 * @author Box
 * 
 * @since Jdk1.6 或 Jdk1.7
 * 
 * @version v1.0
 *
 * @date 2015年8月26日 下午12:03:37
 * 
 * @开发公司或单位 成都爱卓信息技术有限公司
 * 
 * @版权 本文件版权归属成都爱卓信息技术有限公司所有
 * 
 * @description 一个选择照片的基本adapter
 * 
 * @最后修改日期 2015年8月26日 下午12:03:37
 * 
 * @修改人 Box
 * 
 * @复审人 Box
 */
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
