/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.daimajia.slider.library.Transformers;

import android.view.View;

public class IFlipHorizontalTransformer extends IBaseTransformer {

	@Override
	protected void onTransform(View view, float position) {
		final float rotation = 180f * position;
        IViewHelper.setAlpha(view,rotation > 90f || rotation < -90f ? 0 : 1);
        IViewHelper.setPivotY(view,view.getHeight()*0.5f);
		IViewHelper.setPivotX(view,view.getWidth() * 0.5f);
		IViewHelper.setRotationY(view,rotation);
	}

}
