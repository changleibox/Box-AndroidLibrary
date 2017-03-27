package com.daimajia.slider.library.SliderTypes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import net.izhuo.app.library.R;

/**
 * a simple slider view, which just show an image. If you want to make your own
 * slider view,
 *
 * just extend IBaseSliderView, and implement getView() method.
 */
public class IDefaultSliderView extends IBaseSliderView {

	public IDefaultSliderView(Context context) {
		super(context);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView() {
		View v = LayoutInflater.from(getContext()).inflate(
				R.layout.box_view_render_type_default, null);
		ImageView target = (ImageView) v
				.findViewById(R.id.box_daimajia_slider_image);
		bindEventAndShow(v, target);
		return v;
	}
}
