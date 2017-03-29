/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.daimajia.slider.library.Transformers;

import android.view.View;

public class IRotateUpTransformer extends IBaseTransformer {

	private static final float ROT_MOD = -15f;

	@Override
	protected void onTransform(View view, float position) {
		final float width = view.getWidth();
		final float rotation = ROT_MOD * position;

		IViewHelper.setPivotX(view,width * 0.5f);
        IViewHelper.setPivotY(view,0f);
        IViewHelper.setTranslationX(view,0f);
        IViewHelper.setRotation(view,rotation);
	}
	
	@Override
	protected boolean isPagingEnabled() {
		return true;
	}

}
