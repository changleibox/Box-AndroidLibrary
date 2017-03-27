/**
 * Copyright  All right reserved by IZHUO.NET.
 */
package net.izhuo.app.library.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * @author Changlei
 *
 *         2014年6月18日
 */
public class IViewDrawable {

	private static Resources resources;

	public static Drawable getDrawable(Context context, int id) {
		if (resources == null) {
			resources = context.getResources();
		}
		Drawable imgOff = resources.getDrawable(id);
		imgOff.setBounds(0, 0, imgOff.getMinimumWidth(),
				imgOff.getMinimumHeight());
		return imgOff;
	}
}
