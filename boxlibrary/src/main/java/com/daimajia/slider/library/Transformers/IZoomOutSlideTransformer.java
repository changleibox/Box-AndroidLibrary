/*
 * Copyright © All right reserved by CHANGLEI.
 */

package com.daimajia.slider.library.Transformers;

import android.view.View;

public class IZoomOutSlideTransformer extends IBaseTransformer {

	private static final float MIN_SCALE = 0.85f;
	private static final float MIN_ALPHA = 0.5f;

	@Override
	protected void onTransform(View view, float position) {
		if (position >= -1 || position <= 1) {
			// Modify the default slide transition to shrink the page as well
			final float height = view.getHeight();
			final float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
			final float vertMargin = height * (1 - scaleFactor) / 2;
			final float horzMargin = view.getWidth() * (1 - scaleFactor) / 2;

            // Center vertically
            IViewHelper.setPivotY(view,0.5f * height);


			if (position < 0) {
                IViewHelper.setTranslationX(view,horzMargin - vertMargin / 2);
			} else {
                IViewHelper.setTranslationX(view,-horzMargin + vertMargin / 2);
			}

			// Scale the page down (between MIN_SCALE and 1)
			IViewHelper.setScaleX(view,scaleFactor);
            IViewHelper.setScaleY(view,scaleFactor);

			// Fade the page relative to its size.
            IViewHelper.setAlpha(view,MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
		}
	}

}
