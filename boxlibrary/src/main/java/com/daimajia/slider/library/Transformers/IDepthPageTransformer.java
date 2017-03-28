/*
 * Copyright © All right reserved by CHANGLEI.
 */

package com.daimajia.slider.library.Transformers;

import android.view.View;

public class IDepthPageTransformer extends IBaseTransformer {

	private static final float MIN_SCALE = 0.75f;

	@Override
	protected void onTransform(View view, float position) {
		if (position <= 0f) {
            IViewHelper.setTranslationX(view,0f);
            IViewHelper.setScaleX(view,1f);
            IViewHelper.setScaleY(view,1f);
		} else if (position <= 1f) {
			final float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            IViewHelper.setAlpha(view,1-position);
            IViewHelper.setPivotY(view,0.5f * view.getHeight());
            IViewHelper.setTranslationX(view,view.getWidth() * - position);
            IViewHelper.setScaleX(view,scaleFactor);
            IViewHelper.setScaleY(view,scaleFactor);
		}
	}

	@Override
	protected boolean isPagingEnabled() {
		return true;
	}

}
