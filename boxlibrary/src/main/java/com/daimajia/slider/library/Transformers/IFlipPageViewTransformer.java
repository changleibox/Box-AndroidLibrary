package com.daimajia.slider.library.Transformers;

import android.os.Build;
import android.view.View;

import com.daimajia.slider.library.Tricks.IViewPagerEx;

public class IFlipPageViewTransformer extends IBaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        float percentage = 1 - Math.abs(position);
        if(Build.VERSION.SDK_INT >= 13){
            view.setCameraDistance(12000);
        }
        setVisibility(view, position);
        setTranslation(view);
        setSize(view, position, percentage);
        setRotation(view, position, percentage);
    }

    private void setVisibility(View page, float position) {
        if (position < 0.5 && position > -0.5) {
            page.setVisibility(View.VISIBLE);
        } else {
            page.setVisibility(View.INVISIBLE);
        }
    }

    private void setTranslation(View view) {
        IViewPagerEx viewPager = (IViewPagerEx) view.getParent();
        int scroll = viewPager.getScrollX() - view.getLeft();
        IViewHelper.setTranslationX(view,scroll);
    }

    private void setSize(View view, float position, float percentage) {
        IViewHelper.setScaleX(view,(position != 0 && position != 1) ? percentage : 1);
        IViewHelper.setScaleY(view,(position != 0 && position != 1) ? percentage : 1);
    }

    private void setRotation(View view, float position, float percentage) {
        if (position > 0) {
            IViewHelper.setRotationY(view,-180 * (percentage + 1));
        } else {
            IViewHelper.setRotationY(view,180 * (percentage + 1));
        }
    }
}