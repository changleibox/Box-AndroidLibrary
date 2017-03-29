/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.reader.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import net.izhuo.app.library.IBaseActivity;
import net.izhuo.app.library.common.IConstants.IKey;
import net.izhuo.app.library.util.IImageLoaderCompat;
import net.izhuo.app.library.view.IPhotoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Box
 * @version v1.0
 * @since Jdk1.6 或 Jdk1.7
 * <p>
 * 2015年8月26日 下午12:04:22
 * <p>
 * 放大查看照片时候，左右滑动，需要的adapter
 */
public class IVPAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mImages = new ArrayList<>();
    private DisplayImageOptions mContentOptions;

    public IVPAdapter(Context mContext) {
        this.mContext = mContext;
        mContentOptions = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc(true).build();
    }

    public void setData(List<String> images) {
        this.mImages = images;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final IBaseActivity activity = ((IBaseActivity) mContext);
        final String image = mImages.get(position);
        // System.out.println(image);
        IPhotoView imageView = new IPhotoView(mContext);
        imageView.setScaleType(ScaleType.FIT_CENTER);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);

        ImageLoadingListener loadingListener = new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                // activity.showLoad(activity);
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                activity.loadDismiss();
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                activity.loadDismiss();
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                activity.loadDismiss();
            }
        };

        try {
            if (image.contains(IKey.HTTP_HEAD)) {
                IImageLoaderCompat.getImageLoader(activity).displayImage(image, imageView, mContentOptions, loadingListener);
            } else {
                String uri = Uri.fromFile(new File(image)).toString();
                IImageLoaderCompat.getImageLoader(activity).displayImage(uri, imageView, mContentOptions, loadingListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        container.addView(imageView);
        return imageView;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

}
