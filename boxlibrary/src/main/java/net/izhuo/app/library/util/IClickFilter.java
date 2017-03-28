/*
 * Copyright © All right reserved by CHANGLEI.
 */

package net.izhuo.app.library.util;

import android.annotation.SuppressLint;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * @author Changlei
 *
 *         2015年5月17日
 * 
 *         设置view的点击效果，只限于设置的是图片
 */
public class IClickFilter {

	private static final float TRANS = -25f;
	private static final float[] BT_SELECTED = new float[] { 1, 0, 0, 0, TRANS,
			0, 1, 0, 0, TRANS, 0, 0, 1, 0, TRANS, 0, 0, 0, 1, 0 };

	private static final float[] BT_NOT_SELECTED = new float[] { 1, 0, 0, 0, 0,
			0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 };

	private static final ColorMatrixColorFilter PRESS_FILTER = new ColorMatrixColorFilter(
			BT_SELECTED);
	private static final ColorMatrixColorFilter NORMAL_FILTER = new ColorMatrixColorFilter(
			BT_NOT_SELECTED);

	private IClickFilter() {
	}

	/**
	 * 设置{@link View} 及子类，的背景点击效果
	 */
	public static final void filterBackground(View v) {
		v.setOnTouchListener(BACKGROUND_TOUCH_LISTENER);
	}

	/**
	 * 设置{@link ImageView} 及子类，的前景点击效果
	 */
	public static final void filterForeground(ImageView v) {
		v.setOnTouchListener(FOREGROUND_TOUCH_LISTENER);
	}

	private static final OnTouchListener BACKGROUND_TOUCH_LISTENER = new OnTouchListener() {
		@SuppressLint({ "ClickableViewAccessibility", "NewApi" })
		@Override
		public boolean onTouch(final View v, MotionEvent event) {
			Drawable drawable = v.getBackground();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (v instanceof ImageView) {
					((ImageView) v).setColorFilter(PRESS_FILTER);
				} else if (drawable != null) {
					drawable.setColorFilter(PRESS_FILTER);
					v.setBackgroundDrawable(drawable);
				}
				break;
			case MotionEvent.ACTION_OUTSIDE:
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				if (v instanceof ImageView) {
					((ImageView) v).setColorFilter(NORMAL_FILTER);
				} else if (drawable != null) {
					drawable.setColorFilter(NORMAL_FILTER);
					v.setBackgroundDrawable(drawable);
				}
				break;
			}
			return false;
		}
	};

	private static final OnTouchListener FOREGROUND_TOUCH_LISTENER = new OnTouchListener() {
		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			ImageView imageView = (ImageView) v;
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				imageView.setColorFilter(PRESS_FILTER);
				break;
			case MotionEvent.ACTION_OUTSIDE:
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				imageView.setColorFilter(NORMAL_FILTER);
				break;
			}
			return false;
		}
	};

}
