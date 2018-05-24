/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library;

import android.support.multidex.MultiDexApplication;

import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.EScenarioType;

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
    }

    public static IApplication getInstance() {
        return instance;
    }
}
