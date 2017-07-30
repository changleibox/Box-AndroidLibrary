/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import net.izhuo.app.library.IContext;
import net.izhuo.app.library.common.IConstants;

/**
 * Created by Box on 16/8/11.
 * <p>
 * 意图
 */
@SuppressWarnings({"deprecation", "unused", "WeakerAccess"})
public class IIntentCompat {

    public static final int DEF_INTENT_TYPE = -1;

    @Deprecated
    public static IContext startActivityForResult(IContext iContext, Class<?> cls, String data, int type, int requestCode) {
        Bundle bundle = new Bundle();
        if (data != null) {
            bundle.putString(IConstants.INTENT_DATA, data);
        }
        bundle.putInt(IConstants.INTENT_TYPE, type);
        return startActivityForResult(iContext, cls, bundle, requestCode);
    }

    @Deprecated
    public static IContext startActivityDataForResult(IContext iContext, Class<?> cls, String data, int requestCode) {
        return startActivityForResult(iContext, cls, data, DEF_INTENT_TYPE, requestCode);
    }

    @Deprecated
    public static IContext startActivityTypeForResult(IContext iContext, Class<?> cls, int type, int requestCode) {
        return startActivityForResult(iContext, cls, null, type, requestCode);
    }

    @Deprecated
    public static IContext startActivity(IContext iContext, Class<?> cls, String data, int type) {
        Bundle bundle = new Bundle();
        if (data != null) {
            bundle.putString(IConstants.INTENT_DATA, data);
        }
        bundle.putInt(IConstants.INTENT_TYPE, type);
        return startActivity(iContext, cls, bundle);
    }

    @Deprecated
    public static IContext startActivityData(IContext iContext, Class<?> cls, String data) {
        return startActivity(iContext, cls, data, DEF_INTENT_TYPE);
    }

    @Deprecated
    public static IContext startActivityType(IContext iContext, Class<?> cls, int type) {
        return startActivity(iContext, cls, null, type);
    }

    @Deprecated
    public static String getIntentData(IContext iContext) {
        return getBundle(iContext).getString(IConstants.INTENT_DATA);
    }

    @Deprecated
    public static int getIntentType(IContext iContext) {
        return getBundle(iContext).getInt(IConstants.INTENT_TYPE, DEF_INTENT_TYPE);
    }

    public static Fragment startActivityForResult(Fragment fragment, Intent intent, int requestCode) {
        return startActivityForResult(fragment, intent, null, requestCode);
    }

    public static Fragment startActivityForResult(Fragment fragment, Class<?> cls, Bundle bundle, int requestCode) {
        return startActivityForResult(fragment, new Intent(fragment.getContext(), cls), bundle, requestCode);
    }

    public static Fragment startActivityForResult(Fragment fragment, Class<?> cls, int requestCode) {
        return startActivityForResult(fragment, cls, null, requestCode);
    }

    public static Activity startActivityForResult(Activity activity, Intent intent, int requestCode) {
        return startActivityForResult(activity, intent, null, requestCode);
    }

    public static Activity startActivityForResult(Activity activity, Class<?> cls, Bundle bundle, int requestCode) {
        return startActivityForResult(activity, new Intent(activity.getApplicationContext(), cls), bundle, requestCode);
    }

    public static Activity startActivityForResult(Activity activity, Class<?> cls, int requestCode) {
        return startActivityForResult(activity, cls, null, requestCode);
    }

    public static IContext startActivityForResult(IContext iContext, Intent intent, int requestCode) {
        return startActivityForResult(iContext, intent, null, requestCode);
    }

    public static IContext startActivityForResult(IContext iContext, Class<?> cls, Bundle bundle, int requestCode) {
        return startActivityForResult(iContext, new Intent(iContext.getApplicationContext(), cls), bundle, requestCode);
    }

    public static IContext startActivityForResult(IContext iContext, Class<?> cls, Bundle bundle, int requestCode, Bundle options) {
        return startActivityForResult(iContext, new Intent(iContext.getApplicationContext(), cls), bundle, requestCode, options);
    }

    public static IContext startActivityForResult(IContext iContext, Class<?> cls, int requestCode) {
        return startActivityForResult(iContext, cls, null, requestCode);
    }

    public static IContext startActivity(IContext iContext, Intent intent) {
        return startActivity(iContext, intent, null);
    }

    public static IContext startActivity(IContext iContext, Class<?> cls, Bundle bundle) {
        return startActivity(iContext, new Intent(iContext.getApplicationContext(), cls), bundle);
    }

    public static IContext startActivity(IContext iContext, Class<?> cls) {
        return startActivity(iContext, cls, null);
    }

    public static Context startActivity(Context context, Intent intent) {
        return startActivity(context, intent, null);
    }

    public static Context startActivity(Context context, Class<?> cls, Bundle bundle) {
        return startActivity(context, new Intent(context.getApplicationContext(), cls), bundle);
    }

    public static Context startActivity(Context context, Class<?> cls) {
        return startActivity(context, cls, null);
    }

    public static Bundle getBundle(IContext iContext) {
        return getBundle(iContext.getActivity());
    }

    public static Bundle getBundle(Activity activity) {
        if (activity == null) {
            return new Bundle();
        }
        return getBundle(activity.getIntent().getExtras());
    }

    public static IContext startActivityForResult(IContext iContext, Intent intent, Bundle bundle, int requestCode, Bundle options) {
        if (iContext instanceof Fragment) {
            startActivityForResult((Fragment) iContext, intent, bundle, requestCode, options);
        } else {
            Activity activity = iContext.getActivity();
            if (activity == null) {
                return iContext;
            }
            startActivityForResult(activity, intent, bundle, requestCode, options);
        }
        return iContext;
    }

    public static IContext startActivityForResult(IContext iContext, Intent intent, Bundle bundle, int requestCode) {
        return startActivityForResult(iContext, intent, bundle, requestCode, null);
    }

    public static Fragment startActivityForResult(Fragment fragment, Intent intent, Bundle bundle, int requestCode) {
        return startActivityForResult(fragment, intent, bundle, requestCode, null);
    }

    public static Fragment startActivityForResult(Fragment fragment, Intent intent, Bundle bundle, int requestCode, Bundle options) {
        intent.putExtras(getBundle(bundle));
        fragment.startActivityForResult(intent, requestCode, options);
        return fragment;
    }

    public static Activity startActivityForResult(Activity activity, Intent intent, Bundle bundle, int requestCode) {
        return startActivityForResult(activity, intent, bundle, requestCode, null);
    }

    public static Activity startActivityForResult(Activity activity, Intent intent, Bundle bundle, int requestCode, Bundle options) {
        intent.putExtras(getBundle(bundle));
        ActivityCompat.startActivityForResult(activity, intent, requestCode, options);
        return activity;
    }

    public static IContext startActivity(IContext iContext, Intent intent, Bundle bundle) {
        startActivity(iContext.getContext(), intent, bundle);
        return iContext;
    }

    public static Context startActivity(Context context, Intent intent, Bundle bundle) {
        intent.putExtras(getBundle(bundle));
        context.startActivity(intent);
        return context;
    }

    public static Context startActivity(Activity activity, Class<?> cls, Bundle options) {
        return startActivity(activity, new Intent(activity, cls), null, options);
    }

    public static Context startActivity(Activity activity, Class<?> cls, Bundle bundle, Bundle options) {
        return startActivity(activity, new Intent(activity, cls), bundle, options);
    }

    public static Context startActivity(Activity activity, Intent intent, Bundle bundle, Bundle options) {
        intent.putExtras(getBundle(bundle));
        ActivityCompat.startActivity(activity, intent, options);
        return activity;
    }

    private static Bundle getBundle(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        return bundle;
    }

}
