/**
 * Copyright  All right reserved by IZHUO.NET.
 */
package net.izhuo.app.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import net.izhuo.app.library.common.IConstants;

/**
 * @author Box
 *
 * @date 2016年7月18日
 */
public class IPreferences {

	public static SharedPreferences mPreferences;

	public static SharedPreferences getPreferences(Context context) {
		if (mPreferences == null) {
			mPreferences = context.getSharedPreferences(IConstants.DATA, Context.MODE_APPEND);
		}
		return mPreferences;
	}

}
