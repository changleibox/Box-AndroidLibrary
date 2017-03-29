/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.daimajia.slider.library.Transformers;

import android.view.View;

public class IBackgroundToForegroundTransformer extends IBaseTransformer {

	@Override
	protected void onTransform(View view, float position) {
		final float height = view.getHeight();
		final float width = view.getWidth();
		final float scale = min(position < 0 ? 1f : Math.abs(1f - position), 0.5f);

        IViewHelper.setScaleX(view,scale);
        IViewHelper.setScaleY(view,scale);
        IViewHelper.setPivotX(view,width*0.5f);
        IViewHelper.setPivotY(view,height*0.5f);
        IViewHelper.setTranslationX(view,position < 0 ? width * position : -width * position * 0.25f);
	}

	private static final float min(float val, float min) {
		return val < min ? min : val;
	}

}
