/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Parcelable;

/**
 * @author Changlei
 *
 *         2015年1月8日
 */
public class IShortcutUtil {

	private static final String DATA = "data";
	private static final String VERSION = "version_shortcut";
	private static final int DEF_VERSION_CODE = 1;
	private static Intent mShortcutintent;

	public static void createShortCut(Activity act, int iconResId,
			int appnameResId) {
		SharedPreferences preferences = act.getSharedPreferences(DATA,
				Context.MODE_APPEND);

		int versionCode = preferences.getInt(VERSION, DEF_VERSION_CODE);
		if (!preferences.contains(VERSION) || versionCode < getVersion(act)) {
			mShortcutintent = new Intent(
					"com.android.launcher.action.INSTALL_SHORTCUT");
			// 不允许重复创建
			mShortcutintent.putExtra("duplicate", false);
			// 需要现实的名称
			mShortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
					act.getString(appnameResId));
			// 快捷图片
			Parcelable icon = Intent.ShortcutIconResource.fromContext(
					act.getApplicationContext(), iconResId);
			mShortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
			// 点击快捷图片，运行的程序主入口
			mShortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
					getIntentShortcut(act));
			// 发送广播
			act.sendBroadcast(mShortcutintent);
			Editor edit = preferences.edit();
			edit.putInt(VERSION, getVersion(act));
			edit.commit();
		}
	}

	/**
	 * 快捷方式信息
	 * 
	 * @param activity
	 * @return
	 */
	private static Intent getIntentShortcut(Activity activity) {
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.setComponent(activity.getComponentName());
		i.addCategory(Intent.CATEGORY_LAUNCHER);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		return i;
	}

	public static int getVersion(Activity context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			int version = info.versionCode;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return DEF_VERSION_CODE;
		}
	}

}
