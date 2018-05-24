/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import net.box.app.library.common.IConstants;

/**
 * @author Box
 *
 * date 2016年7月18日
 */
public class IPreferences {

	private static SharedPreferences mPreferences;

	public static SharedPreferences getPreferences(Context context) {
		if (mPreferences == null) {
			mPreferences = context.getSharedPreferences(IConstants.DATA, Context.MODE_PRIVATE);
		}
		return mPreferences;
	}

}
