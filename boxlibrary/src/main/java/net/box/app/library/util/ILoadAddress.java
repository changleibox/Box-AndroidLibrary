/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;

import net.box.app.library.R;
import net.box.app.library.common.IConstants;
import net.box.app.library.common.IConstants.ICaches;
import net.box.app.library.entity.ICity;
import net.box.app.library.entity.IHotAddress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author Changlei
 * <p>
 * 2015年1月21日
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class ILoadAddress {

    public static final String[] INDEXS = {"", "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z", "#"};

    private static List<IHotAddress> mHotAddresses;
    private static SharedPreferences mPreferences;
    private static String[] mHotCitys;

    public static String HOT;
    public static Map<String, List<ICity>> mOldAddressMap;
    public static List<IHotAddress> mNewAddresses = new ArrayList<>();

    public static void loadAddress(final Context context, final Callback callback) {
        mPreferences = context.getSharedPreferences(IConstants.DATA, Context.MODE_PRIVATE);
        mHotCitys = context.getResources().getStringArray(R.array.box_hot_address);
        INDEXS[0] = HOT = context.getResources().getString(R.string.box_lable_hot);
        String address = mPreferences.getString(ILoadAddress.class.getName()
                + IConstants.IKey.ADDRESS, null);
        if (!TextUtils.isEmpty(address)) {
            ICaches.HOT_ADDRESSES.clear();
            List<IHotAddress> hotAddresses = IJsonDecoder.jsonToObject(
                    address, new TypeToken<List<IHotAddress>>() {
                    }.getType());
            ICaches.HOT_ADDRESSES.addAll(hotAddresses);
        }
        if (ICaches.SELECTORS.size() == 0) {
            loadHotAddress(context, callback);
        }
    }

    public static void searchAddress(final Context context, final String selector, final Callback callback) {
        mNewAddresses.clear();
        mHotAddresses = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mOldAddressMap == null) {
                    mOldAddressMap = IXMLReader
                            .getXMLCity(context, R.xml.box_cities);
                }
                Map<String, List<ICity>> newAddressMap = new HashMap<>();
                for (String key : mOldAddressMap.keySet()) {
                    if (selector != null) {
                        if (key.contains(selector)) {
                            List<ICity> cities = mOldAddressMap.get(key);
                            newAddressMap.put(key, cities);
                        }
                    } else {
                        newAddressMap.putAll(mOldAddressMap);
                        break;
                    }
                }
                for (String key : newAddressMap.keySet()) {
                    for (ICity city : newAddressMap.get(key)) {
                        IHotAddress person = new IHotAddress(city
                                .getCityName());
                        person.setProvince(city.getProvince());
                        mHotAddresses.add(person);
                    }
                }

                String[] allNames = sortIndex(mHotAddresses);
                sortList(allNames, mHotAddresses, mNewAddresses);

                // for (String name : mHotCitys) {
                // HotAddress hotAddress=new HotAddress(name,HOT);
                // mNewAddresses.add(0, hotAddress);
                // }
                // mNewAddresses.add(0, new HotAddress(HOT, HOT));

                // 循环字母表，找出newhotAddresses中对应字母的位置
                for (String INDEX : INDEXS) {
                    for (int i = 0; i < mNewAddresses.size(); i++) {
                        IHotAddress hotAddress = mNewAddresses.get(i);
                        if (hotAddress.getName().equals(INDEX)) {
                            ICaches.SELECTORS.put(INDEX, i);
                        }
                    }
                }

                if (callback != null) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            callback.onCallback(mNewAddresses);
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 设置模拟数据
     */
    private static void loadHotAddress(final Context context,
                                       final Callback callback) {
        // CACHES.NEW_hotAddresses.clear();
        mHotAddresses = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ICaches.HOT_ADDRESSES.size() == 0) {
                    Map<String, List<ICity>> map = IXMLReader.getXMLCity(
                            context, R.xml.box_cities);
                    for (String key : map.keySet()) {
                        for (ICity city : map.get(key)) {
                            IHotAddress person = new IHotAddress(city
                                    .getCityName());
                            person.setProvince(city.getProvince());
                            mHotAddresses.add(person);
                        }
                    }

                    String[] allNames = sortIndex(mHotAddresses);
                    sortList(allNames, mHotAddresses,
                            ICaches.HOT_ADDRESSES);

                    for (String name : mHotCitys) {
                        IHotAddress hotAddress = new IHotAddress(name,
                                HOT);
                        ICaches.HOT_ADDRESSES.add(0, hotAddress);
                    }
                    ICaches.HOT_ADDRESSES.add(0, new IHotAddress(HOT,
                            HOT));

                    Editor edit = mPreferences.edit();
                    edit.putString(ILoadAddress.class.getName()
                            + IConstants.IKey.ADDRESS, IJsonDecoder
                            .objectToJson(ICaches.HOT_ADDRESSES));
                    edit.apply();
                }

                // CACHES.SELECTOR = new HashMap<String, Integer>();
                // 循环字母表，找出newhotAddresses中对应字母的位置
                for (String INDEX : INDEXS) {
                    for (int i = 0; i < ICaches.HOT_ADDRESSES.size(); i++) {
                        IHotAddress hotAddress = ICaches.HOT_ADDRESSES
                                .get(i);
                        if (hotAddress.getName().equals(INDEX)) {
                            ICaches.SELECTORS.put(INDEX, i);
                        }
                    }
                }
                if (callback != null) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            callback.onCallback(ICaches.HOT_ADDRESSES);
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 获取排序后的新数据
     */
    private static String[] sortIndex(List<IHotAddress> hotAddresses) {
        TreeSet<String> set = new TreeSet<>();
        // 获取初始化数据源中的首字母，添加到set中
        for (IHotAddress hotAddress : hotAddresses) {
            set.add(IStringHelper.getPinYinHeadChar(hotAddress.getName())
                    .substring(0, 1));
        }
        // 新数组的长度为原数据加上set的大小
        String[] names = new String[hotAddresses.size() + set.size()];
        int i = 0;
        for (String string : set) {
            names[i] = string;
            i++;
        }
        String[] pNames = new String[hotAddresses.size()];
        for (int j = 0; j < hotAddresses.size(); j++) {
            IHotAddress hotAddress = hotAddresses.get(j);
            String name = hotAddress.getName();
            String pingYin = IStringHelper.getPingYin(name);
            hotAddress.setPinYinName(pingYin);
            pNames[j] = pingYin;
        }
        // 将原数据拷贝到新数据中
        System.arraycopy(pNames, 0, names, set.size(), pNames.length);
        // 自动按照首字母排序
        Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
        return names;
    }

    /**
     * 重新排序获得一个新的List集合
     */
    private static void sortList(String[] allNames, List<IHotAddress> hotAddresses, List<IHotAddress> newHotAddresses) {
        for (String index : allNames) {
            if (index.length() == 1) {
                newHotAddresses.add(new IHotAddress(index));
            } else {
                for (int j = 0; j < hotAddresses.size(); j++) {
                    IHotAddress address = hotAddresses.get(j);
                    if (index.equals(address.getPinYinName())) {
                        String name = address.getName();
                        String pinYinName = address.getPinYinName();
                        IHotAddress hotAddress = new IHotAddress(name,
                                pinYinName);
                        hotAddress.setProvince(address.getProvince());
                        newHotAddresses.add(hotAddress);
                    }
                }
            }
        }
    }

    public interface Callback {

        void onCallback(List<IHotAddress> hotAddresses);

    }

}
