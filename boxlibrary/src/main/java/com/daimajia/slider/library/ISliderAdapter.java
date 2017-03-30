/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.daimajia.slider.library;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderTypes.IBaseSliderView;

import java.util.ArrayList;

/**
 * A slider adapter
 */
public class ISliderAdapter extends PagerAdapter implements IBaseSliderView.ImageLoadListener{

    @SuppressWarnings("unused")
	private Context mContext;
    private ArrayList<IBaseSliderView> mImageContents;

    public ISliderAdapter(Context context){
        mContext = context;
        mImageContents = new ArrayList<IBaseSliderView>();
    }

    public <T extends IBaseSliderView> void addSlider(T slider){
        slider.setOnImageLoadListener(this);
        mImageContents.add(slider);
        notifyDataSetChanged();
    }

    public IBaseSliderView getSliderView(int position){
        if(position < 0 || position >= mImageContents.size()){
            return null;
        }else{
            return mImageContents.get(position);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public <T extends IBaseSliderView> void removeSlider(T slider){
        if(mImageContents.contains(slider)){
            mImageContents.remove(slider);
            notifyDataSetChanged();
        }
    }

    public void removeSliderAt(int position){
        if(mImageContents.size() > position){
            mImageContents.remove(position);
            notifyDataSetChanged();
        }
    }

    public void removeAllSliders(){
        mImageContents.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImageContents.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        IBaseSliderView b = mImageContents.get(position);
        View v = b.getView();
        container.addView(v);
        return v;
    }

    @Override
    public void onStart(IBaseSliderView target) {

    }

    /**
     * When image download error, then remove.
     * @param result
     * @param target
     */
    @Override
    public void onEnd(boolean result, IBaseSliderView target) {
        if(target.isErrorDisappear() == false || result == true){
            return;
        }
        for (IBaseSliderView slider: mImageContents){
            if(slider.equals(target)){
                removeSlider(target);
                break;
            }
        }
    }

}
