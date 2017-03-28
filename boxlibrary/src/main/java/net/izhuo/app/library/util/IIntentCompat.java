/*
 * Copyright © All right reserved by CHANGLEI.
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
@SuppressWarnings({"deprecation", "unused"})
public class IIntentCompat {

    public static final int DEF_INTENT_TYPE = -1;

    @Deprecated
    public static IContext intentForResult(IContext iContext, Class<?> cls, String data, int type, int requestCode) {
        Bundle bundle = new Bundle();
        if (data != null) {
            bundle.putString(IConstants.INTENT_DATA, data);
        }
        bundle.putInt(IConstants.INTENT_TYPE, type);
        return intentForResult(iContext, cls, bundle, requestCode);
    }

    @Deprecated
    public static IContext intentDataForResult(IContext iContext, Class<?> cls, String data, int requestCode) {
        return intentForResult(iContext, cls, data, DEF_INTENT_TYPE, requestCode);
    }

    @Deprecated
    public static IContext intentTypeForResult(IContext iContext, Class<?> cls, int type, int requestCode) {
        return intentForResult(iContext, cls, null, type, requestCode);
    }

    @Deprecated
    public static IContext intent(IContext iContext, Class<?> cls, String data, int type) {
        Bundle bundle = new Bundle();
        if (data != null) {
            bundle.putString(IConstants.INTENT_DATA, data);
        }
        bundle.putInt(IConstants.INTENT_TYPE, type);
        return intent(iContext, cls, bundle);
    }

    @Deprecated
    public static IContext intentData(IContext iContext, Class<?> cls, String data) {
        return intent(iContext, cls, data, DEF_INTENT_TYPE);
    }

    @Deprecated
    public static IContext intentType(IContext iContext, Class<?> cls, int type) {
        return intent(iContext, cls, null, type);
    }

    @Deprecated
    public static String getIntentData(IContext iContext) {
        return getBundle(iContext).getString(IConstants.INTENT_DATA);
    }

    @Deprecated
    public static int getIntentType(IContext iContext) {
        return getBundle(iContext).getInt(IConstants.INTENT_TYPE, DEF_INTENT_TYPE);
    }

    public static Fragment intentForResult(Fragment fragment, Intent intent, int requestCode) {
        return intentForResult(fragment, intent, null, requestCode);
    }

    public static Fragment intentForResult(Fragment fragment, Class<?> cls, Bundle bundle, int requestCode) {
        return intentForResult(fragment, new Intent(fragment.getContext(), cls), bundle, requestCode);
    }

    public static Fragment intentForResult(Fragment fragment, Class<?> cls, int requestCode) {
        return intentForResult(fragment, cls, null, requestCode);
    }

    public static Activity intentForResult(Activity activity, Intent intent, int requestCode) {
        return intentForResult(activity, intent, null, requestCode);
    }

    public static Activity intentForResult(Activity activity, Class<?> cls, Bundle bundle, int requestCode) {
        return intentForResult(activity, new Intent(activity.getApplicationContext(), cls), bundle, requestCode);
    }

    public static Activity intentForResult(Activity activity, Class<?> cls, int requestCode) {
        return intentForResult(activity, cls, null, requestCode);
    }

    public static IContext intentForResult(IContext iContext, Intent intent, int requestCode) {
        return intentForResult(iContext, intent, null, requestCode);
    }

    public static IContext intentForResult(IContext iContext, Class<?> cls, Bundle bundle, int requestCode) {
        return intentForResult(iContext, new Intent(iContext.getApplicationContext(), cls), bundle, requestCode);
    }

    public static IContext intentForResult(IContext iContext, Class<?> cls, Bundle bundle, int requestCode, Bundle options) {
        return intentForResult(iContext, new Intent(iContext.getApplicationContext(), cls), bundle, requestCode, options);
    }

    public static IContext intentForResult(IContext iContext, Class<?> cls, int requestCode) {
        return intentForResult(iContext, cls, null, requestCode);
    }

    public static IContext intent(IContext iContext, Intent intent) {
        return intent(iContext, intent, null);
    }

    public static IContext intent(IContext iContext, Class<?> cls, Bundle bundle) {
        return intent(iContext, new Intent(iContext.getApplicationContext(), cls), bundle);
    }

    public static IContext intent(IContext iContext, Class<?> cls) {
        return intent(iContext, cls, null);
    }

    public static Context intent(Context context, Intent intent) {
        return intent(context, intent, null);
    }

    public static Context intent(Context context, Class<?> cls, Bundle bundle) {
        return intent(context, new Intent(context.getApplicationContext(), cls), bundle);
    }

    public static Context intent(Context context, Class<?> cls) {
        return intent(context, cls, null);
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

    public static IContext intentForResult(IContext iContext, Intent intent, Bundle bundle, int requestCode, Bundle options) {
        if (iContext instanceof Fragment) {
            intentForResult((Fragment) iContext, intent, bundle, requestCode, options);
        } else {
            Activity activity = iContext.getActivity();
            if (activity == null) {
                return iContext;
            }
            intentForResult(activity, intent, bundle, requestCode, options);
        }
        return iContext;
    }

    public static IContext intentForResult(IContext iContext, Intent intent, Bundle bundle, int requestCode) {
        return intentForResult(iContext, intent, bundle, requestCode, null);
    }

    public static Fragment intentForResult(Fragment fragment, Intent intent, Bundle bundle, int requestCode) {
        return intentForResult(fragment, intent, bundle, requestCode, null);
    }

    public static Fragment intentForResult(Fragment fragment, Intent intent, Bundle bundle, int requestCode, Bundle options) {
        intent.putExtras(getBundle(bundle));
        fragment.startActivityForResult(intent, requestCode, options);
        return fragment;
    }

    public static Activity intentForResult(Activity activity, Intent intent, Bundle bundle, int requestCode) {
        return intentForResult(activity, intent, bundle, requestCode, null);
    }

    public static Activity intentForResult(Activity activity, Intent intent, Bundle bundle, int requestCode, Bundle options) {
        intent.putExtras(getBundle(bundle));
        ActivityCompat.startActivityForResult(activity, intent, requestCode, options);
        return activity;
    }

    public static IContext intent(IContext iContext, Intent intent, Bundle bundle) {
        intent(iContext.getContext(), intent, bundle);
        return iContext;
    }

    public static Context intent(Context context, Intent intent, Bundle bundle) {
        intent.putExtras(getBundle(bundle));
        context.startActivity(intent);
        return context;
    }

    public static Context intent(Activity activity, Class<?> cls, Bundle options) {
        return intent(activity, new Intent(activity, cls), null, options);
    }

    public static Context intent(Activity activity, Class<?> cls, Bundle bundle, Bundle options) {
        return intent(activity, new Intent(activity, cls), bundle, options);
    }

    public static Context intent(Activity activity, Intent intent, Bundle bundle, Bundle options) {
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
