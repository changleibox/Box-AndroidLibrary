/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.daimajia.slider.library.Transformers;

/**
 * Created by daimajia on 14-5-29.
 */
import android.view.View;

public class IAccordionTransformer extends IBaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        IViewHelper.setPivotX(view,position < 0 ? 0 : view.getWidth());
        IViewHelper.setScaleX(view,position < 0 ? 1f + position : 1f - position);
    }

}