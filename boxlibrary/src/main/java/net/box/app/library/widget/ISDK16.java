/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */


package net.box.app.library.widget;

import android.annotation.TargetApi;
import androidx.core.view.ViewCompat;
import android.view.View;

@TargetApi(16)
public class ISDK16 {

	public static void postOnAnimation(View view, Runnable r) {
		ViewCompat.postOnAnimation(view, r);
	}
	
}
