/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.common;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import net.box.app.library.entity.IHotAddress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Changlei
 * <p>
 * 2014年7月29日
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class IConstants {

    public static boolean DEBUG = true;
    public static final String EMPTY = "";

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
        private static final Map<String, List<Activity>> ACTIVITY_MAP = Collections.synchronizedMap(new HashMap<String, List<Activity>>());
        public static final List<IHotAddress> HOT_ADDRESSES = Collections.synchronizedList(new ArrayList<IHotAddress>());
        // 存放含有索引字母的位置
        public static final Map<String, Integer> SELECTORS = Collections.synchronizedMap(new HashMap<String, Integer>());
    }

    public static final class IActivityCaches {
        public static List<Activity> getActivities() {
            final List<Activity> activities = new CopyOnWriteArrayList<>();
            for (List<Activity> activityList : ICaches.ACTIVITY_MAP.values()) {
                if (activityList != null) {
                    activities.addAll(activityList);
                }
            }
            return Collections.unmodifiableList(activities);
        }

        public static List<Activity> getActivities(@NonNull String activityName) {
            final List<Activity> activities = ICaches.ACTIVITY_MAP.get(activityName);
            return Collections.unmodifiableList(activities == null ? new CopyOnWriteArrayList<Activity>() : activities);
        }

        public static <T extends Activity> List<Activity> getActivities(@NonNull Class<T> cls) {
            return getActivities(cls.getSimpleName());
        }

        public static <T extends Activity> void putActivity(@NonNull T t) {
            final String simpleName = t.getClass().getSimpleName();
            final List<Activity> activities = new CopyOnWriteArrayList<>(getActivities(simpleName));
            activities.add(t);
            ICaches.ACTIVITY_MAP.put(simpleName, activities);
        }

        public static void removeActivity(@NonNull String activityName) {
            ICaches.ACTIVITY_MAP.remove(activityName);
        }

        public static <T extends Activity> void removeActivity(@NonNull T t) {
            final String simpleName = t.getClass().getSimpleName();
            final List<Activity> activities = new CopyOnWriteArrayList<>(getActivities(simpleName));
            activities.remove(t);
            ICaches.ACTIVITY_MAP.put(simpleName, activities);
        }

        public static void clear() {
            ICaches.ACTIVITY_MAP.clear();
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

    public interface ISize {
        int LIMIT_PHONE_LENGTH = 11;
    }

}
