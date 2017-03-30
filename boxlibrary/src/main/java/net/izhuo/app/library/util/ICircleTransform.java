/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

/**
 *
 */
package net.izhuo.app.library.util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.squareup.picasso.Transformation;

/**
 * Created by Box on 2015年11月1日.
 * <p>
 * 创建圆角图片
 */
public class ICircleTransform implements Transformation {

    private static final int CIRCLE = -1;

    private final int size;

    public ICircleTransform() {
        this(CIRCLE);
    }

    public ICircleTransform(int size) {
        this.size = size;
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        return this.size == 0 ? bitmap : (this.size == CIRCLE ? transformCircle(bitmap) : transformRadius(bitmap));
    }

    @Override
    public String key() {
        return this.size == CIRCLE ? "circle" : "radius" + size;
    }

    private Bitmap transformCircle(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();
        int size = Math.min(width, height);

        int x = (width - size) / 2;
        int y = (height - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0,
                Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG));
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    private Bitmap transformRadius(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Paint paintColor = new Paint();
        paintColor.setFlags(Paint.ANTI_ALIAS_FLAG);

        RectF rectF = new RectF(new Rect(0, 0, width, height));

        canvas.drawRoundRect(rectF, this.size, this.size, paintColor);

        Paint paintImage = new Paint();
        paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(source, 0, 0, paintImage);
        source.recycle();
        return output;
    }

}
