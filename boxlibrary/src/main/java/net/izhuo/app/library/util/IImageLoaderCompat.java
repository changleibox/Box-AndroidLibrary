/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import net.izhuo.app.library.R;

/**
 * @author Box
 *         <p>
 *         2016年7月18日
 */
public class IImageLoaderCompat {

    private static ImageLoader mImageLoader = ImageLoader.getInstance();

    public static ImageLoader getImageLoader(Context context) {
        if (!mImageLoader.isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPoolSize(3)
                    .memoryCache(new WeakMemoryCache()).build();
            mImageLoader.init(config);
        }
        return mImageLoader;
    }

    public static DisplayImageOptions getOptions(int radius, int loadingImage, int emptyUriImage, int failImage) {
        if (loadingImage == 0) {
            loadingImage = R.drawable.box_img_user_def_avatar;
        }
        if (emptyUriImage == 0) {
            emptyUriImage = R.drawable.box_img_user_def_avatar;
        }
        if (failImage == 0) {
            failImage = R.drawable.box_img_user_def_avatar;
        }
        return new DisplayImageOptions.Builder()
                .showStubImage(loadingImage)
                .showImageForEmptyUri(emptyUriImage).showImageOnFail(failImage)
                .displayer(new RoundedBitmapDisplayer(radius)).bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
                .cacheOnDisc(true).build();
    }

    public static DisplayImageOptions getOptions(int radius, int defImage) {
        return getOptions(radius, defImage, defImage, defImage);
    }

    public static DisplayImageOptions getOptions(int radius) {
        return getOptions(radius, R.drawable.box_default_avatar_rect);
    }

}
