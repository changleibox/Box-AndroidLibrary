/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import net.box.app.library.IContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Box on 16/9/2.
 * <p>
 * 处理6.0权限问题
 */
@SuppressWarnings("unused")
public class IPermissionCompat {

    public static boolean checkSelfPermission(@NonNull IContext iContext, int requestCode, @NonNull String... permissions) {
        if (iContext instanceof Fragment) {
            return checkSelfPermission((Fragment) iContext, requestCode, permissions);
        } else if (iContext instanceof Activity) {
            return checkSelfPermission((Activity) iContext, requestCode, permissions);
        }
        return false;
    }

    public static boolean checkSelfPermission(@NonNull Fragment fragment, int requestCode, @NonNull String... permissions) {
        List<String> notPermissions = new ArrayList<>();
        if (!checkSelfPermissions(fragment.getActivity(), notPermissions, permissions)) {
            fragment.requestPermissions(notPermissions.toArray(new String[notPermissions.size()]), requestCode);
            return false;
        }
        return true;
    }

    public static boolean checkSelfPermission(@NonNull Activity activity, int requestCode, @NonNull String... permissions) {
        List<String> notPermissions = new ArrayList<>();
        if (!checkSelfPermissions(activity.getApplication(), notPermissions, permissions)) {
            ActivityCompat.requestPermissions(activity, notPermissions.toArray(new String[notPermissions.size()]), requestCode);
            return false;
        }
        return true;
    }

    public static boolean checkSelfPermissions(@NonNull Context context, @NonNull String... permissions) {
        return checkSelfPermissions(context, null, permissions);
    }

    public static boolean checkSelfPermissions(@NonNull Context context, List<String> notPermissions, @NonNull String... permissions) {
        if (notPermissions == null) {
            notPermissions = new ArrayList<>();
        }
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                notPermissions.add(permission);
            }
        }
        return notPermissions.size() == 0;
    }

    public static boolean hasSelfPermission(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
