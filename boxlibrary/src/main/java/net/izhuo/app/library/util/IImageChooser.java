/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.RequiresPermission;

import net.izhuo.app.library.IContext;
import net.izhuo.app.library.R;
import net.izhuo.app.library.common.IConstants;
import net.izhuo.app.library.reader.activity.IChoosePictureActivity;
import net.izhuo.app.library.reader.activity.IPreviewPictureActivity;
import net.izhuo.app.library.reader.picture.IOpenType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Box on 16/8/11.
 * <p>
 * 图片选择器
 */
public class IImageChooser {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    public static void startActivityForPicture(IContext iContext, List<String> datas, @IntRange(from = 1) int maxCount) {
        if (maxCount < 1) {
            throw new IllegalAccessError("maxCount min is 1!");
        }
        if (datas == null) {
            datas = new ArrayList<>();
        }
        Bundle bundle = new Bundle();
        bundle.putString(IConstants.INTENT_DATA, IJsonDecoder.objectToJson(datas));
        bundle.putInt(IConstants.INTENT_TYPE, maxCount);
        iContext.startActivityForResult(IChoosePictureActivity.class, bundle, IConstants.IRequestCode.REQUSET_PICTURE);
        Activity activity = iContext.getActivity();
        if (activity != null) {
            activity.overridePendingTransition(R.anim.box_anim_activity_show, R.anim.box_anim_menuhide);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    public static void startActivityForPicture(IContext iContext, IOpenType.Type type, List<String> totalImages, List<String> selectImages, int selectIndex, int maxSelectCount) {
        Bundle bundle = new Bundle();
        bundle.putInt(IOpenType.OPEN_TYPE, type.toInteger());
        bundle.putString(IConstants.INTENT_DATA, IJsonDecoder.objectToJson(totalImages));
        bundle.putInt(IConstants.INTENT_TYPE, selectIndex);
        bundle.putString(IConstants.INTENT_DATA_ADDITION, IJsonDecoder.objectToJson(selectImages));
        bundle.putInt(IConstants.INTENT_TYPE_ADDITION, maxSelectCount);
        iContext.startActivityForResult(IPreviewPictureActivity.class, bundle, IConstants.IRequestCode.REQUSET_LOOK);
        Activity activity = iContext.getActivity();
        if (activity != null) {
            activity.overridePendingTransition(R.anim.box_anim_activity_show, R.anim.box_anim_menuhide);
        }
    }

}
