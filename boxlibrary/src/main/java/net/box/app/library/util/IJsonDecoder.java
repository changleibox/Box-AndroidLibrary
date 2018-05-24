/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 主要用于json与对象之间的互相转换
 *
 * @author Changlei
 *         <p>
 *         2014年7月29日
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class IJsonDecoder {

    public static <T> T jsonToObject(String json, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    /**
     * 把json格式的字符串转换成一个对象或者集合
     *
     * @param json json格式字符串
     * @param type 转换的类型
     * @return 转换后的对象
     */
    public static <T> T jsonToObject(String json, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    /**
     * 把一个对象转化成json格式字符串
     *
     * @param object 需要转换的对象
     * @return 转换后的json格式字符串
     */
    public static String objectToJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> T jsonToObject(String json, Class<T> type, String pattern) {
        Gson gson = new GsonBuilder().setDateFormat(pattern).create();
        return gson.fromJson(json, type);
    }

    /**
     * 把json格式的字符串转换成一个对象或者集合（包含时间的）
     *
     * @param json json格式字符串
     * @param type 转换的类型
     * @return 转换后的对象
     */
    public static <T> T jsonToObject(String json, Type type, String pattern) {
        Gson gson = new GsonBuilder().setDateFormat(pattern).create();
        return gson.fromJson(json, type);
    }

    /**
     * 把一个对象转化成json格式字符串（包含时间的）
     *
     * @param object 需要转换的对象
     * @return 转换后的json格式字符串
     */
    public static String objectToJson(Object object, String pattern) {
        Gson gson = new GsonBuilder().setDateFormat(pattern).create();
        return gson.toJson(object);
    }

    private static JsonElement getJsonElement(String json) {
        return new JsonParser().parse(json);
    }

    public static boolean isJsonArray(String json) {
        JsonElement jsonElement = getJsonElement(json);
        return jsonElement.isJsonArray();
    }

    public static boolean isJsonNull(String json) {
        JsonElement jsonElement = getJsonElement(json);
        return jsonElement.isJsonNull();
    }

    public static boolean isJsonObject(String json) {
        JsonElement jsonElement = getJsonElement(json);
        return jsonElement.isJsonObject();
    }

    public static boolean isJsonPrimitive(String json) {
        JsonElement jsonElement = getJsonElement(json);
        return jsonElement.isJsonPrimitive();
    }

    @Deprecated
    public static String jsonGetValue(String result, String key) {
        try {
            JSONObject jsonObject = getJSONObject(result);
            if (jsonObject != null) {
                return jsonObject.getString(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getString(String result, String key) {
        try {
            JSONObject jsonObject = getJSONObject(result);
            if (jsonObject != null) {
                return jsonObject.getString(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean getBoolean(String result, String key) {
        try {
            JSONObject jsonObject = getJSONObject(result);
            if (jsonObject != null) {
                return jsonObject.getBoolean(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static double getDouble(String result, String key) {
        try {
            JSONObject jsonObject = getJSONObject(result);
            if (jsonObject != null) {
                return jsonObject.getDouble(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getInt(String result, String key) {
        try {
            JSONObject jsonObject = getJSONObject(result);
            if (jsonObject != null) {
                return jsonObject.getInt(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getLong(String result, String key) {
        try {
            JSONObject jsonObject = getJSONObject(result);
            if (jsonObject != null) {
                return jsonObject.getLong(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static JSONArray getJSONArray(String result, String key) {
        try {
            JSONObject jsonObject = getJSONObject(result);
            if (jsonObject != null) {
                return jsonObject.getJSONArray(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJSONObject(String result, String key) {
        try {
            JSONObject jsonObject = getJSONObject(result);
            if (jsonObject != null) {
                return jsonObject.getJSONObject(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object get(String result, String key) {
        try {
            JSONObject jsonObject = getJSONObject(result);
            if (jsonObject != null) {
                return jsonObject.get(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JSONObject getJSONObject(String result) {
        try {
            return new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将map数据解析出来，并拼接成json字符串
     *
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public static JSONObject mapToJSONObject(Map<String, ?> map) throws Exception {
        JSONObject json = null;
        StringBuffer temp = new StringBuffer();
        if (!map.isEmpty()) {
            temp.append("{");
            // 遍历map
            Set<?> set = map.entrySet();
            Iterator<?> i = set.iterator();
            if (i.hasNext()) {
                do {
                    Entry<String, ?> entry = (Entry<String, ?>) i.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    temp.append("\"").append(key).append("\":");
                    if (value instanceof Map<?, ?>) {
                        temp.append(mapToJSONObject((Map<String, Object>) value)).append(",");
                    } else if (value instanceof List<?>) {
                        temp.append(listToJson((List<Map<String, Object>>) value)).append(",");
                    } else {
                        temp.append(value).append(",");
                    }
                } while (i.hasNext());
            }
            if (temp.length() > 1) {
                temp = new StringBuffer(temp.substring(0, temp.length() - 1));
            }
            temp.append("}");
            json = new JSONObject(temp.toString());
        }
        return json;
    }

    /**
     * 将单个list转成json字符串
     *
     * @param list
     * @return
     * @throws Exception
     */
    public static String listToJson(List<Map<String, Object>> list) throws Exception {
        String jsonL;
        StringBuffer temp = new StringBuffer();
        temp.append("[");
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> m = list.get(i);
            if (i == list.size() - 1) {
                temp.append(mapToJSONObject(m));
            } else {
                temp.append(mapToJSONObject(m)).append(",");
            }
        }
        if (temp.length() > 1) {
            temp = new StringBuffer(temp.substring(0, temp.length()));
        }
        temp.append("]");
        jsonL = temp.toString();
        return jsonL;
    }

    /**
     * 将整个json字符串解析，并放置到map<String,Object>中
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> jsonToMap(String jsonStr) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(jsonStr)) {
            JSONObject json = new JSONObject(jsonStr);
            Iterator<?> i = json.keys();
            while (i.hasNext()) {
                String key = (String) i.next();
                Object value = json.get(key);
                if (value.toString().indexOf("{") == 0) {
                    map.put(key.trim(), jsonToMap(value.toString()));
                } else if (value.toString().indexOf("[") == 0) {
                    map.put(key.trim(), jsonToList(value.toString()));
                } else {
                    if (value instanceof String) {
                        map.put(key.trim(), value.toString().trim());
                    } else {
                        map.put(key.trim(), value);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 将单个json数组字符串解析放在list中
     *
     * @param jsonStr
     * @return
     * @throws Exception
     */
    public static List<Object> jsonToList(String jsonStr) throws Exception {
        List<Object> list = new ArrayList<>();
        JSONArray ja = new JSONArray(jsonStr);
        for (int j = 0; j < ja.length(); j++) {
            Object jm = ja.get(j);
            if (jm instanceof String) {
                list.add(jm.toString());
            } else if (jm.toString().indexOf("{") == 0) {
                list.add(jsonToMap(jm.toString()));
            }
        }
        return list;
    }

}
