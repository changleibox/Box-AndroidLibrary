/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 解决ScrollView嵌套ViewPager出现的滑动冲突问题
 */
public class IMyScrollView extends ScrollView {

	private float xDistance, yDistance, xLast, yLast;

	public IMyScrollView(Context context) {
		super(context);
	}

	public IMyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public IMyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();

			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			xLast = curX;
			yLast = curY;

			if (xDistance > yDistance) {
				return false;
			}
		}

		return super.onInterceptTouchEvent(ev);
	}
} 
