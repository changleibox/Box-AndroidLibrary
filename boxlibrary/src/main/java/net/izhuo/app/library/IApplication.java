/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library;

import android.support.multidex.MultiDexApplication;

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
    }

    public static IApplication getInstance() {
        return instance;
    }
}
