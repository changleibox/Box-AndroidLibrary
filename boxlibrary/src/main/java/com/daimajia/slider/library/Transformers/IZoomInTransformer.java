/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.daimajia.slider.library.Transformers;

import android.view.View;

public class IZoomInTransformer extends IBaseTransformer {

	@Override
	protected void onTransform(View view, float position) {
		final float scale = position < 0 ? position + 1f : Math.abs(1f - position);
		IViewHelper.setScaleX(view,scale);
        IViewHelper.setScaleY(view,scale);
        IViewHelper.setPivotX(view,view.getWidth() * 0.5f);
        IViewHelper.setPivotY(view,view.getHeight() * 0.5f);
        IViewHelper.setAlpha(view,position < -1f || position > 1f ? 0f : 1f - (scale - 1f));
	}

}
