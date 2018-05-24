/*
 * Copyright © 2018 CHANGLEI. All rights reserved.
 */

package net.box.app.library.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音帮助类
 */
@SuppressWarnings("unused")
public class IPinyinUtils {

    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     * 花花大神->huahuadashen
     *
     * @param inputString
     * @return
     */
    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] input = inputString.trim().toCharArray();
        String output = "";
        try {
            for (char curchar : input) {
                if (Character.toString(curchar).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, format);
                    output += temp[0];
                } else
                    output += Character.toString(curchar);
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * 汉字转换为汉语拼音首字母，英文字符不变
     * 花花大神->hhds
     *
     * @param chinese 汉字
     * @return 拼音
     */
    public static String getFirstSpell(@NonNull String chinese) {
        if (TextUtils.isEmpty(chinese)) {
            return "#";
        }
        StringBuilder pybf = new StringBuilder();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char curchar : arr) {
            if (curchar > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, defaultFormat);
                    if (temp != null) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(curchar);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

    // public static String getFirstPinYin(String input) {
    //     if (TextUtils.isEmpty(input)) {
    //         return null;
    //     }
    //     input = input.trim().toUpperCase();
    //     HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
    //     format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
    //     format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    //     char first = input.charAt(0);
    //     String firstStr = Character.toString(first);
    //     try {
    //         String[] array = PinyinHelper.toHanyuPinyinStringArray(first, format);
    //         if (array != null && array.length > 0) {
    //             return array[0];
    //         } else if (firstStr.matches("^[a-zA-Z]*")) {
    //             return firstStr;
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return null;
    // }

    public static String getFirstPinYin(String input) {
        if (TextUtils.isEmpty(input)) {
            return null;
        }
        String pinyin = IPinyinDuoYinZiUtils.convertChineseToPinyin(input);
        String firstStr = TextUtils.isEmpty(pinyin) ? null : Character.toString(pinyin.charAt(0));
        if (!TextUtils.isEmpty(firstStr) && firstStr.matches("^[a-zA-Z]*")) {
            return firstStr.toUpperCase();
        }
        return null;
    }
}