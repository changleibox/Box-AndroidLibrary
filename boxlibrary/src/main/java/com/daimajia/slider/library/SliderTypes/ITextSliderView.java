package com.daimajia.slider.library.SliderTypes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.izhuo.app.library.R;

/**
 * This is a slider with a description TextView.
 */
public class ITextSliderView extends IBaseSliderView {
	public ITextSliderView(Context context) {
		super(context);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView() {
		View v = LayoutInflater.from(getContext()).inflate(
				R.layout.box_view_render_type_text, null);
		ImageView target = (ImageView) v
				.findViewById(R.id.box_daimajia_slider_image);
		TextView description = (TextView) v.findViewById(R.id.box_description);
		description.setText(getDescription());
		bindEventAndShow(v, target);
		return v;
	}
}
