/*
 * Copyright © All right reserved by CHANGLEI.
 */

package net.izhuo.app.library.view;

import android.os.Build.VERSION;
import android.view.View;

public class ICompat {
	
	private static final int SIXTY_FPS_INTERVAL = 1000 / 60;
	
	public static void postOnAnimation(View view, Runnable runnable) {
		if (VERSION.SDK_INT >= 16) {
			ISDK16.postOnAnimation(view, runnable);
		} else {
			view.postDelayed(runnable, SIXTY_FPS_INTERVAL);
		}
	}

}
