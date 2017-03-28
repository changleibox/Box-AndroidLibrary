/*
 * Copyright © All right reserved by CHANGLEI.
 */

package com.daimajia.slider.library.Transformers;

import android.view.View;

public class ICubeInTransformer extends IBaseTransformer {

	@Override
	protected void onTransform(View view, float position) {
		// Rotate the fragment on the left or right edge
        IViewHelper.setPivotX(view,position > 0 ? 0 : view.getWidth());
        IViewHelper.setPivotY(view,0);
        IViewHelper.setRotation(view,-90f * position);
	}

	@Override
	public boolean isPagingEnabled() {
		return true;
	}

}
