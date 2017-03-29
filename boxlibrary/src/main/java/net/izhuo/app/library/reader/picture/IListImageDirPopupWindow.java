/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.reader.picture;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import net.izhuo.app.library.R;
import net.izhuo.app.library.reader.adapter.ICommonAdapter;
import net.izhuo.app.library.reader.entity.IImageFloder;

import java.util.List;

public class IListImageDirPopupWindow extends
		IBasePopupWindowForListView<IImageFloder> {
	private ListView mListDir;

	public IListImageDirPopupWindow(int width, int height,
									List<IImageFloder> datas, View convertView) {
		super(convertView, width, height, true, datas);
	}

	@Override
	public void initViews() {
		mListDir = (ListView) findViewById(R.id.box_id_list_dir);
		mListDir.setAdapter(new ICommonAdapter<IImageFloder>(context,
				mDatas, R.layout.box_item_list_dir) {
			@Override
			public void convert(int position, IViewHolder helper,
					IImageFloder item) {
				if (item != null) {
					helper.setText(R.id.box_id_dir_item_name, item.getName());
					helper.setImageByUrl(R.id.box_id_dir_item_image,
							item.getFirstImagePath());
					helper.setText(
							R.id.box_id_dir_item_count,
							getContentView().getContext().getString(
									R.string.box_lable_spread, item.getCount()));
				}
			}
		});
	}

	public interface OnImageDirSelected {
		void selected(IImageFloder floder);
	}

	private OnImageDirSelected mImageDirSelected;

	public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
		this.mImageDirSelected = mImageDirSelected;
	}

	@Override
	public void initEvents() {
		mListDir.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (mImageDirSelected != null) {
					mImageDirSelected.selected(mDatas.get(position));
				}
			}
		});
	}

	@Override
	public void init() {
	}

	@Override
	protected void beforeInitWeNeedSomeParams(Object... params) {
	}

}
