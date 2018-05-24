/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.util;

import android.util.Log;

import net.box.app.library.common.IConstants;

/**
 * @author Box
 */
public class ILogCompat {

    public static void i(String tag, Object msg) {
        if (IConstants.DEBUG) {
            if (msg != null) {
                Log.i(tag, msg.toString());
            }
        }
    }

    public static void d(String tag, Object msg) {
        if (IConstants.DEBUG) {
            if (msg != null) {
                Log.d(tag, msg.toString());
            }
        }
    }

    public static void e(String tag, Object msg) {
        if (IConstants.DEBUG) {
            if (msg != null) {
                Log.e(tag, msg.toString());
            }
        }
    }

    public static void v(String tag, Object msg) {
        if (IConstants.DEBUG) {
            if (msg != null) {
                Log.v(tag, msg.toString());
            }
        }
    }

    public static void w(String tag, Object msg) {
        if (IConstants.DEBUG) {
            if (msg != null) {
                Log.w(tag, msg.toString());
            }
        }
    }

}
