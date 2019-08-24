/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import net.box.app.library.IContext;

/**
 * Created by Box on 16/8/11.
 * <p>
 * 意图
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class IIntentCompat {

    public static final int DEF_INTENT_TYPE = -1;

    public static Fragment startActivityForResult(Fragment fragment, Intent intent, int requestCode) {
        return startActivityForResult(fragment, intent, null, requestCode);
    }

    public static <T extends Activity> Fragment startActivityForResult(Fragment fragment, Class<T> cls, Bundle bundle, int requestCode) {
        return startActivityForResult(fragment, new Intent(fragment.getContext(), cls), bundle, requestCode);
    }

    public static <T extends Activity> Fragment startActivityForResult(Fragment fragment, Class<T> cls, int requestCode) {
        return startActivityForResult(fragment, cls, null, requestCode);
    }

    public static Activity startActivityForResult(Activity activity, Intent intent, int requestCode) {
        return startActivityForResult(activity, intent, null, requestCode);
    }

    public static <T extends Activity> Activity startActivityForResult(Activity activity, Class<T> cls, Bundle bundle, int requestCode) {
        return startActivityForResult(activity, new Intent(activity.getApplicationContext(), cls), bundle, requestCode);
    }

    public static <T extends Activity> Activity startActivityForResult(Activity activity, Class<T> cls, int requestCode) {
        return startActivityForResult(activity, cls, null, requestCode);
    }

    public static IContext startActivityForResult(IContext iContext, Intent intent, int requestCode) {
        return startActivityForResult(iContext, intent, null, requestCode);
    }

    public static <T extends Activity> IContext startActivityForResult(IContext iContext, Class<T> cls, Bundle bundle, int requestCode) {
        return startActivityForResult(iContext, new Intent(iContext.getApplicationContext(), cls), bundle, requestCode);
    }

    public static <T extends Activity> IContext startActivityForResult(IContext iContext, Class<T> cls, Bundle bundle, int requestCode, Bundle options) {
        return startActivityForResult(iContext, new Intent(iContext.getApplicationContext(), cls), bundle, requestCode, options);
    }

    public static <T extends Activity> IContext startActivityForResult(IContext iContext, Class<T> cls, int requestCode) {
        return startActivityForResult(iContext, cls, null, requestCode);
    }

    public static IContext startActivity(IContext iContext, Intent intent) {
        return startActivity(iContext, intent, null);
    }

    public static <T extends Activity> IContext startActivity(IContext iContext, Class<T> cls, Bundle bundle) {
        return startActivity(iContext, new Intent(iContext.getApplicationContext(), cls), bundle);
    }

    public static <T extends Activity> IContext startActivity(IContext iContext, Class<T> cls) {
        return startActivity(iContext, cls, null);
    }

    public static Context startActivity(Context context, Intent intent) {
        return startActivity(context, intent, null);
    }

    public static <T extends Activity> Context startActivity(Context context, Class<T> cls, Bundle bundle) {
        return startActivity(context, new Intent(context.getApplicationContext(), cls), bundle);
    }

    public static <T extends Activity> Context startActivity(Context context, Class<T> cls) {
        return startActivity(context, cls, null);
    }

    public static Fragment startActivity(Fragment fragment, Intent intent) {
        return startActivity(fragment, intent, null);
    }

    public static <T extends Activity> Fragment startActivity(Fragment fragment, Class<T> cls, Bundle bundle) {
        return startActivity(fragment, new Intent(fragment.getContext(), cls), bundle);
    }

    public static <T extends Activity> Fragment startActivity(Fragment fragment, Class<T> cls) {
        return startActivity(fragment, cls, null);
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
        fragment.startActivityForResult(intent, requestCode);
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
        if (iContext instanceof Fragment) {
            startActivity((Fragment) iContext, intent, bundle);
        } else {
            startActivity(iContext.getContext(), intent, bundle);
        }
        return iContext;
    }

    public static Context startActivity(Context context, Intent intent, Bundle bundle) {
        intent.putExtras(getBundle(bundle));
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        return context;
    }

    public static Fragment startActivity(Fragment fragment, Intent intent, Bundle bundle) {
        intent.putExtras(getBundle(bundle));
        fragment.startActivity(intent);
        return fragment;
    }

    public static <T extends Activity> Context startActivity(Context context, Class<T> cls, @Nullable Bundle bundle, Bundle options) {
        return startActivity(context, new Intent(context, cls), bundle, options);
    }

    public static Context startActivity(Context context, Intent intent, @Nullable Bundle bundle, Bundle options) {
        intent.putExtras(getBundle(bundle));
        ActivityCompat.startActivity(context, intent, options);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return context;
    }

    public static <T extends Activity> Fragment startActivity(Fragment fragment, Class<T> cls, @Nullable Bundle bundle, Bundle options) {
        return startActivity(fragment, new Intent(fragment.getContext(), cls), bundle, options);
    }

    public static Fragment startActivity(Fragment fragment, Intent intent, @Nullable Bundle bundle, Bundle options) {
        intent.putExtras(getBundle(bundle));
        fragment.startActivity(intent, options);
        return fragment;
    }

    private static Bundle getBundle(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        return bundle;
    }

}
