/*
 * Copyright © All right reserved by CHANGLEI.
 */

package com.daimajia.slider.library.Transformers;

import android.view.View;

public class IRotateDownTransformer extends IBaseTransformer {

	private static final float ROT_MOD = -15f;

	@Override
	protected void onTransform(View view, float position) {
		final float width = view.getWidth();
		final float height = view.getHeight();
		final float rotation = ROT_MOD * position * -1.25f;

		IViewHelper.setPivotX(view,width * 0.5f);
        IViewHelper.setPivotY(view,height);
        IViewHelper.setRotation(view,rotation);
	}
	
	@Override
	protected boolean isPagingEnabled() {
		return true;
	}

}
