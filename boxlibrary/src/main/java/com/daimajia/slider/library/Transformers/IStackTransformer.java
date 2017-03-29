/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.daimajia.slider.library.Transformers;

import android.view.View;

public class IStackTransformer extends IBaseTransformer {

	@Override
	protected void onTransform(View view, float position) {
		IViewHelper.setTranslationX(view,position < 0 ? 0f : -view.getWidth() * position);
	}

}
