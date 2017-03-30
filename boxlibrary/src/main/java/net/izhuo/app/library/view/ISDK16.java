/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */


package net.izhuo.app.library.view;

import android.annotation.TargetApi;
import android.support.v4.view.ViewCompat;
import android.view.View;

@TargetApi(16)
public class ISDK16 {

	public static void postOnAnimation(View view, Runnable r) {
		ViewCompat.postOnAnimation(view, r);
	}
	
}
