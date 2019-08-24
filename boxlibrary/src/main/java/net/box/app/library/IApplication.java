/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library;

import android.app.Activity;
import android.os.Bundle;
import androidx.multidex.MultiDexApplication;

import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.EScenarioType;

import net.box.app.library.common.IConstants;
import net.box.app.library.util.IProgressCompat;

/**
 * Created by Box on 16/12/3.
 * <p>
 * Application
 */
public class IApplication extends MultiDexApplication {

    private static IApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        MobclickAgent.setScenarioType(this, EScenarioType.E_UM_NORMAL);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallback());
    }

    public static IApplication getInstance() {
        return instance;
    }

    private static class ActivityLifecycleCallback implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            IConstants.IActivityCaches.putActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            IConstants.IActivityCaches.removeActivity(activity);
            IProgressCompat.onContextDestroy(activity);
        }
    }
}
