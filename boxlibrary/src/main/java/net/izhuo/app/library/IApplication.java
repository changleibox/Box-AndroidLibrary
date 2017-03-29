/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library;

import android.support.multidex.MultiDexApplication;

import net.izhuo.app.library.helper.IAppHelper;

/**
 * Created by Box on 16/12/3.
 * <p>
 * Application
 */
public class IApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        IAppHelper.setContext(getApplicationContext());
    }
}
