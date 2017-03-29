/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.common;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import net.izhuo.app.library.entity.IHotAddress;
import net.izhuo.app.library.util.IProgressCompat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Changlei
 *         <p>
 *         2014年7月29日
 */
@SuppressWarnings("unused")
public final class IConstants {

    public static boolean DEBUG = true;

    public static void setDebugMode(boolean debugMode) {
        DEBUG = debugMode;
    }

    public static final String CHARSET_NAME = "utf-8";
    public static final String INTENT_TYPE = "intent_type";
    public static final String INTENT_DATA = "intent_data";
    public static final String INTENT_DATA_ADDITION = "intent_data_addition";
    public static final String INTENT_TYPE_ADDITION = "intent_type_addition";

    public static final String DATA = "data";

    public static Uri getTelUri(String tel) {
        return Uri.parse("tel:" + tel);
    }

    public static final class ICaches {
        private static final Map<String, Activity> ACTIVITY_MAP = Collections.synchronizedMap(new HashMap<String, Activity>());
        public static final List<IHotAddress> HOT_ADDRESSES = Collections.synchronizedList(new ArrayList<IHotAddress>());
        // 存放含有索引字母的位置
        public static final Map<String, Integer> SELECTORS = Collections.synchronizedMap(new HashMap<String, Integer>());
    }

    public static final class IActivityCaches {
        public static Collection<Activity> getActivities() {
            return ICaches.ACTIVITY_MAP.values();
        }

        public static Activity getActivity(@NonNull String activityName) {
            return ICaches.ACTIVITY_MAP.get(activityName);
        }

        public static <T extends Activity> Activity getActivity(@NonNull Class<T> cls) {
            return getActivity(cls.getSimpleName());
        }

        public static <T extends Activity> void putActivity(@NonNull T t) {
            ICaches.ACTIVITY_MAP.put(t.getClass().getSimpleName(), t);
        }

        public static void removeActivity(@NonNull String activityName) {
            ICaches.ACTIVITY_MAP.remove(activityName);
        }

        public static <T extends Activity> void removeActivity(@NonNull T t) {
            removeActivity(t.getClass().getSimpleName());
            IProgressCompat.onContextDestroy(t);
        }
    }

    public interface IKey {

        String PAGE = "page";
        String PAGE_SIZE = "pageSize";

        String RESULT = "result";
        String CODE = "code";
        String CONTENT = "content";

        String LONGITUDE = "longitude";
        String LATITUDE = "latitude";
        String ADDRESS = "address";
        String PICTURE = "picture";

        String HTTP_HEAD = "http://";
        String HTTPS_HEAD = "https://";
        String ERROR = "error";

        String URL = "url";
    }

    public static final class IHttpError {

        public static final String NO_RESPONSE = "no response";
        public static final String JSON_ERROR = "json error";

        public static final int CODE_SERVER_ERROR = 0x000500;

        public static final SparseArray<String> ERROR_MAP = new SparseArray<>();

        static {
            ERROR_MAP.put(CODE_SERVER_ERROR, "未知异常！");
        }

        public static void put(int key, String value) {
            ERROR_MAP.put(key, value);
        }

        public static String get(int key) {
            return ERROR_MAP.get(key);
        }
    }

    public interface IRequestCode {
        int REQUSET_PICTURE = 0x000500;
        int REQUSET_LOOK = 0x000501;
        int REQUSET_CAMERA = 0x000502;
        int REQUEST_CHOOSE_FILE = 0x000503;
        int REQUEST_PERMISSION_SDCARD = 0x000504;
        int REQUEST_PERMISSION_CAMERA = 0x000505;
    }

}
