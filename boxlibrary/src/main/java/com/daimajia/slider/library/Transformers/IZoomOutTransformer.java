/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.daimajia.slider.library.Transformers;

import android.view.View;

public class IZoomOutTransformer extends IBaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        final float scale = 1f + Math.abs(position);
        IViewHelper.setScaleX(view,scale);
        IViewHelper.setScaleY(view,scale);
        IViewHelper.setPivotX(view,view.getWidth() * 0.5f);
        IViewHelper.setPivotY(view,view.getWidth() * 0.5f);
        IViewHelper.setAlpha(view,position < -1f || position > 1f ? 0f : 1f - (scale - 1f));
        if(position < -0.9){
            //-0.9 to prevent a small bug
            IViewHelper.setTranslationX(view,view.getWidth() * position);
        }
    }

}