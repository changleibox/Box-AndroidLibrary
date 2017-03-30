/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

/**
 * 获取圆形图片
 * @author Changlei
 *
 * 2014年7月28日
 */
public class ILocusUtils {

	/**
	 * 获取圆形图片
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getBitmap(Bitmap bitmap){
		Bitmap output=Bitmap.createBitmap(bitmap.getHeight(),bitmap.getWidth(),Config.ARGB_8888);
		Canvas canvas=new Canvas(output);
		Paint paint=new Paint();
		Rect rect=new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
		RectF rectF=new RectF(rect);
		paint.setAntiAlias(true);  
		canvas.drawOval(rectF, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect,rectF, paint);
		return output;
	}

	/**
	 * 根据路径获取一个Bitmap
	 * @param path
	 * @return
	 */
	public final static Bitmap lessenUriImage(String path){
		Bitmap bitmap = BitmapFactory.decodeFile(path); //此时返回 bm 为空
		return bitmap;
	}
}
