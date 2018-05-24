/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author Changlei
 *
 * 2014年10月20日
 */
public class IMGridView extends GridView {

	public IMGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public IMGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public IMGridView(Context context) {
		super(context);
	}
	
	/* (non-Javadoc)
	 * @see android.widget.GridView#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, 
	            MeasureSpec.AT_MOST); 
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
