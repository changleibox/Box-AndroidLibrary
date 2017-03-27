/**
 * Copyright  All right reserved by IZHUO.NET.
 */
package net.izhuo.app.library.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.ColorRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import net.izhuo.app.library.IContext;
import net.izhuo.app.library.IBaseActivity;
import net.izhuo.app.library.IBaseFragment;
import net.izhuo.app.library.R;
import net.izhuo.app.library.api.IHttpRequest.CommonCallback;
import net.izhuo.app.library.common.IConstants;
import net.izhuo.app.library.common.IConstants.IHttpError;
import net.izhuo.app.library.common.IConstants.IRequestCode;
import net.izhuo.app.library.helper.IAppHelper;
import net.izhuo.app.library.reader.entity.IDevice;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Changlei
 *         <p>
 *         2014年8月1日
 */
@SuppressWarnings({"unused", "JavaDoc"})
public class IAppUtils {

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.#");

    public static float calculateLineDistance(double lat1, double lng1, double lat2, double lng2) {
        if (lng1 != 0 && lat1 != 0 && lng2 != 0 && lat2 != 0) {
            double var2 = 0.01745329251994329D;
            double var4 = lng1;
            double var6 = lat1;
            double var8 = lng2;
            double var10 = lat2;
            var4 *= 0.01745329251994329D;
            var6 *= 0.01745329251994329D;
            var8 *= 0.01745329251994329D;
            var10 *= 0.01745329251994329D;
            double var12 = Math.sin(var4);
            double var14 = Math.sin(var6);
            double var16 = Math.cos(var4);
            double var18 = Math.cos(var6);
            double var20 = Math.sin(var8);
            double var22 = Math.sin(var10);
            double var24 = Math.cos(var8);
            double var26 = Math.cos(var10);
            double[] var28 = new double[3];
            double[] var29 = new double[3];
            var28[0] = var18 * var16;
            var28[1] = var18 * var12;
            var28[2] = var14;
            var29[0] = var26 * var24;
            var29[1] = var26 * var20;
            var29[2] = var22;
            double var30 = Math.sqrt((var28[0] - var29[0]) * (var28[0] - var29[0]) + (var28[1] - var29[1]) * (var28[1] - var29[1]) + (var28[2] - var29[2]) * (var28[2] - var29[2]));
            return (float) (Math.asin(var30 / 2.0D) * 1.27420015798544E7D);
        } else {
            return 0.0F;
        }
    }

    /*
     * 计算兴趣点的距离
     */
    public static float getShortDistance(double lat1, double lng1, double lat2, double lng2) {
        return calculateLineDistance(lat1, lng1, lat2, lng2);
    }

    public static String formatDistance(double s) {
        if (s < 1000) {
            return DECIMAL_FORMAT.format(s) + "m";
        } else {
            return DECIMAL_FORMAT.format((s / 1000)) + "km";
        }
    }

    public static String getDistance(double lat1, double lng1, double lat2, double lng2) {
        double s = getShortDistance(lat1, lng1, lat2, lng2);
        return formatDistance(s);
    }

    /**
     * 计算控件的宽高
     *
     * @param view 需要计算宽高的view
     * @return int[] 第一个元素为宽第二个元素为高
     */
    public static int[] getWidgetWidthAndHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();
        return new int[]{width, height};
    }

    public static String toSBC(String input) {
        // 半角转全角：
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127) {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    public static String getBitmapEncode(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
            out.close();
            byte[] buffer = out.toByteArray();
            byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
            return new String(encode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 全角转换为半角
     *
     * @param input
     * @return
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * 设置textview字体加粗
     *
     * @param view
     */
    public static void fontOverstriking(TextView view) {
        TextPaint tp = view.getPaint();
        tp.setFakeBoldText(true);
    }

    /**
     * 获取一个时间点距离今天的天数
     *
     * @param nowDate
     * @param formerlyDate
     * @return
     */
    public static long getQuot(Date nowDate, Date formerlyDate) {
        long quot = nowDate.getTime() - formerlyDate.getTime();
        quot = quot / 1000 / 60 / 60 / 24;
        return quot;
    }

    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String metaValue = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                metaValue = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {
            // northing to do
        }
        return metaValue;
    }

    public static List<String> getTagsList(String originalText) {
        if (originalText == null || originalText.equals("")) {
            return null;
        }
        List<String> tags = new ArrayList<>();
        int indexOfComma = originalText.indexOf(',');
        String tag;
        while (indexOfComma != -1) {
            tag = originalText.substring(0, indexOfComma);
            tags.add(tag);

            originalText = originalText.substring(indexOfComma + 1);
            indexOfComma = originalText.indexOf(',');
        }

        tags.add(originalText);
        return tags;
    }

    public static String getLogText(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("log_text", "");
    }

    public static void setLogText(Context context, String text) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("log_text", text);
        editor.apply();
    }

    /**
     * 验证手机号码
     *
     * @param mobiles 手机号码
     * @return 返回Boolean型，true为合法，false为不合法
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((\\(\\d{3}\\))|(\\d{3}\\-))?1[3-8][0-9]\\d{8}");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isEmail(String content) {
        return content.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    }

    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     *
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public static int chineseLength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 截取字符串
     *
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public static String substring(String value, int start, int end) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        StringBuilder buffer = new StringBuilder();
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        int tempLength = end > value.length() ? value.length() : end + 1;
        for (int i = start; i < tempLength; i++) {
            if (i + 1 < value.length()) {
                /* 获取一个字符 */
                String temp = value.substring(i, i + 1);
                /* 判断是否为中文字符 */
                if (temp.matches(chinese)) {
                    /* 中文字符长度为2 */
                    valueLength += 2;
                } else {
                    /* 其他字符长度为1 */
                    valueLength += 1;
                }
                if (valueLength <= (end - start)) {
                    buffer.append(temp);
                }
            }
        }
        return buffer.toString();
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * 根据原图和变长绘制圆形图片
     *
     * @param source
     * @param min
     * @return
     */
    public static Bitmap createCircleImage(Bitmap source, int min) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        /**
         * 使用SRC_IN
         */
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * encodeBase64File:(将文件转成base64 字符串). <br/>
     *
     * @param path 文件路径
     * @return
     * @throws Exception
     * @author guhaizhou@126.com
     * @since JDK 1.6
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        //noinspection ResultOfMethodCallIgnored
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    /**
     * encodeBase64File:(将文件转成base64 字符串). <br/>
     *
     * @param file 文件
     * @return
     * @throws Exception
     * @author guhaizhou@126.com
     * @since JDK 1.6
     */
    public static String encodeBase64File(File file) {
        try {
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            //noinspection ResultOfMethodCallIgnored
            inputFile.read(buffer);
            inputFile.close();
            return Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * add shadow to bitmap
     *
     * @param originalBitmap
     * @return
     */
    public static Bitmap drawImageDropShadow(Bitmap originalBitmap) {

        BlurMaskFilter blurFilter = new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL);
        Paint shadowPaint = new Paint();
        shadowPaint.setAlpha(50);
        shadowPaint.setColor(0xff000000);
        shadowPaint.setMaskFilter(blurFilter);

        int[] offsetXY = new int[2];
        Bitmap shadowBitmap = originalBitmap.extractAlpha(shadowPaint, offsetXY);

        Bitmap shadowImage32 = shadowBitmap.copy(Config.ARGB_8888, true);
        Canvas c = new Canvas(shadowImage32);
        c.drawBitmap(originalBitmap, offsetXY[0], offsetXY[1], null);

        return shadowImage32;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int roundPx) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    public static Bitmap drawableToBitamp(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // System.out.println("Drawable转Bitmap");
        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    @SuppressWarnings("deprecation")
    public static Drawable bitmapToDrawable(Bitmap bmp) {
        return new BitmapDrawable(bmp);
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    // MD5加密，32位
    public static String MD5(String str) {
        StringBuilder hexValue = new StringBuilder();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            char[] charArray = str.toCharArray();
            byte[] byteArray = new byte[charArray.length];

            for (int i = 0; i < charArray.length; i++) {
                byteArray[i] = (byte) charArray[i];
            }
            byte[] md5Bytes = md5.digest(byteArray);

            for (byte md5Byte : md5Bytes) {
                int val = ((int) md5Byte) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    // 可逆的加密算法
    public static String encryptmd5(String str) {
        char[] a = str.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 'l');
        }
        return new String(a);
    }

    public static String getURLEncoder(String value) {
        try {
            return URLEncoder.encode(value, IConstants.CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String prettyJson(String body) {
        if (TextUtils.isEmpty(body)) {
            return body;
        }
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setIndent("\u00A0\u00A0");
            JsonElement jsonElement = new JsonParser().parse(body);
            gson.toJson(jsonElement, jsonWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            return body;
        }
    }

    public static IDevice getDeviceInfo(Context context) {
        IDevice device = new IDevice();
        device.setUsing(true);
        String model = Build.MODEL;
        String brand = Build.BRAND.toUpperCase(Locale.getDefault());
        String user = Build.USER;
        device.setDeviceName(brand + " " + model);
        device.setDeviceTitle(model);
        device.setLoginTime(new Date().getTime());
        String deviceId = new IDeviceUuidFactory(context).getDeviceUuid().toString();
        device.setDeviceID(deviceId);
        device.setSystemVersion(Build.VERSION.RELEASE);
        device.setMemorySize(String.valueOf(getTotalMemory(context)));
        device.setDeviceVersion(getVersion(context));
        return device;
    }

    public static long getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]) << 10;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return initial_memory;
    }

    public static String formatUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        try {
            int index = url.lastIndexOf(".");
            if (index < 0) {
                return url.split(" ")[0];
            } else {
                int i = url.indexOf(" ", index);
                return url.substring(0, i < 0 ? url.length() : i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片的旋转角度置为0
     *
     * @param path
     * @return void
     * @Title: setPictureDegreeZero
     * @date 2012-12-10 上午10:54:46
     */
    public static void setPictureDegreeZero(String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            double width = exifInterface.getAttributeDouble(ExifInterface.TAG_IMAGE_WIDTH, 0);
            double height = exifInterface.getAttributeDouble(ExifInterface.TAG_IMAGE_LENGTH, 0);
            if (width > height) {
                //修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
                //例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
                exifInterface.saveAttributes();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap toturn(Bitmap img, int rotate) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate); /*翻转90度*/
        int width = img.getWidth();
        int height = img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return img;
    }

    public static String getSDPath() {
        File sdDir;
        if (isExistSD()) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        } else {
            sdDir = Environment.getRootDirectory();
        }
        return sdDir.toString();
    }

    public static boolean isExistSD() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static final String DEF_TEMPLATE = "yyyy-MM-dd HH:mm:ss.SSSZ";

    /**
     * 获取actionbar的像素高度，默认使用android官方兼容包做actionbar兼容
     *
     * @return
     */
    private int getActionBarHeight(AppCompatActivity activity) {
        ActionBar actionBar = activity.getSupportActionBar();
        int actionBarHeight = actionBar == null ? 0 : actionBar.getHeight();
        if (actionBarHeight != 0) {
            return actionBarHeight;
        }
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        final TypedValue tv = new TypedValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, metrics);
            }
        } else {
            // 使用android.support.v7.appcompat包做actionbar兼容的情况
            if (activity.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, metrics);
            }
        }
        return actionBarHeight;
    }

    public static int getTitleBarHeight(Activity context) {
        int contentTop = context.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        // statusBarHeight是上面所求的状态栏的高度
        return contentTop - getStatusBarHeight(context);
    }

    /**
     * 获取状态栏的高度
     */
    public static int getStatusBarHeight(Activity context) {
        int statusBarHeight;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
            Rect frame = new Rect();
            context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }
        return statusBarHeight;
    }

    /**
     * 判断当前时间是白天还是夜晚
     */
    public static boolean isNight() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.getDefault());
        String hour = sdf.format(new Date());
        int k = Integer.parseInt(hour);
        return (k >= 0 && k < 6) || (k >= 18 && k < 24);
    }

    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= 16;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= 19;
    }

    @SuppressWarnings("deprecation")
    public static List<Camera.Size> getResolutionList(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        return parameters.getSupportedPreviewSizes();
    }

    @SuppressWarnings("deprecation")
    public static class ResolutionComparator implements Comparator<Camera.Size> {

        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.height != rhs.height)
                return lhs.height - rhs.height;
            else
                return lhs.width - rhs.width;
        }

    }

    /**
     * 把一个date类型转换成String类型的时间，格式为{@link #DEF_TEMPLATE}
     *
     * @param date 时间
     */
    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEF_TEMPLATE,
                Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * 把一个date类型转换成String类型的时间，格式为{@link #DEF_TEMPLATE}
     *
     * @param date 时间
     */
    public static Date stringToDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEF_TEMPLATE,
                Locale.getDefault());
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    @SuppressWarnings("MissingPermission")
    public static void call(String phone, IBaseActivity context, int requestCode) {
        if (IPermissionCompat.checkSelfPermission((IContext) context, requestCode, Manifest.permission.CALL_PHONE)) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone.replaceAll("-", "")));
            context.startActivity(intent);
        }
    }

    public static void startWechat(IBaseActivity activity) {
        try {
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            activity.startActivity(intent);
        } catch (Exception e) {
            activity.showText(R.string.box_toast_wechat_client_inavailable);
        }
    }

    public static void sendMailByIntent(Context context, String... emails) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, emails);
        intent.putExtra(Intent.EXTRA_SUBJECT,
                context.getString(R.string.box_lable_email_title));
        intent.putExtra(Intent.EXTRA_TEXT,
                context.getString(R.string.box_lable_email_text));
        context.startActivity(Intent.createChooser(intent,
                context.getString(R.string.box_lable_choose_email_title)));
    }

    public static SpannableString setTextClickListener(
            String promptHeader, String clickText,
            final OnClickListener listener, final int textColor,
            TextView textView) {
        if (TextUtils.isEmpty(clickText) || TextUtils.isEmpty(promptHeader)
                || textView == null) {
            return null;
        }
        String prompt = String.format(promptHeader, clickText);
        int length = clickText.length();
        int start = prompt.indexOf(clickText);
        int end = start + length;
        SpannableString spannableString = getClickSpannableString(prompt,
                listener, textColor, start, end);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
        return spannableString;
    }

    public static SpannableString getClickSpannableString(CharSequence prompt,
                                                          final OnClickListener listener, final int textColor, int start,
                                                          int end) {
        SpannableString spannableString = new SpannableString(prompt);
        spannableString.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                if (listener != null) {
                    listener.onClick(widget);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(textColor);
                ds.setUnderlineText(false);
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static boolean isHanzi(String str) {
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher m = pattern.matcher(str);
        return m.find() && m.group(0).equals(str);
    }

    public static String encode(String s) {
        try {
            return URLEncoder.encode(s, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String decode(String s) {
        try {
            return URLDecoder.decode(s, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static void chooseFiles(IContext iContext) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String title = iContext.getContext().getString(R.string.box_lable_choose_file);
        iContext.intentForResult(Intent.createChooser(intent, title), IRequestCode.REQUEST_CHOOSE_FILE);
    }

    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 添加联系人 在同一个事务中完成联系人各项数据的添加
     * 使用ArrayList<ContentProviderOperation>，把每步操作放在它的对象中执行
     *
     * @throws OperationApplicationException
     * @throws RemoteException
     */
    private static void addContacts(Context context, Bitmap bitmap,
                                    String name, String mobile, String email) throws RemoteException,
            OperationApplicationException {
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = context.getContentResolver();
        // 第一个参数：内容提供者的主机名
        // 第二个参数：要执行的操作
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        // 操作1.添加Google账号，这里值为null，表示不添加
        ContentProviderOperation operation = ContentProviderOperation
                .newInsert(uri).withValue("account_name", null)// account_name:Google账号
                .build();

        // 操作2.添加data表中name字段
        uri = Uri.parse("content://com.android.contacts/data");
        ContentProviderOperation operation2 = ContentProviderOperation
                .newInsert(uri)
                // 第二个参数int previousResult:表示上一个操作的位于operations的第0个索引，
                // 所以能够将上一个操作返回的raw_contact_id作为该方法的参数
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/name")
                .withValue("data2", name).build();

        // 操作3.添加data表中phone字段
        uri = Uri.parse("content://com.android.contacts/data");
        ContentProviderOperation operation3 = ContentProviderOperation
                .newInsert(uri).withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                .withValue("data2", "2").withValue("data1", mobile).build();

        // 操作4.添加data表中的Email字段
        uri = Uri.parse("content://com.android.contacts/data");
        ContentProviderOperation operation4 = ContentProviderOperation
                .newInsert(uri).withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/email_v2")
                .withValue("data2", "2").withValue("data1", email).build();

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 将Bitmap压缩成PNG编码，质量为100%存储
        ContentProviderOperation operation5 = null;
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            byte[] avatar = os.toByteArray();

            operation5 = ContentProviderOperation.newInsert(uri)
                    .withValueBackReference("raw_contact_id", 0)
                    .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    .withValue("data2", "2").withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, avatar)
                    .build();
        }

        operations.add(operation);
        operations.add(operation2);
        operations.add(operation3);
        operations.add(operation4);
        if (operation5 != null) {
            operations.add(operation5);
        }

        resolver.applyBatch("com.android.contacts", operations);
    }

    /**
     * 添加联系人 在同一个事务中完成联系人各项数据的添加
     * 使用ArrayList<ContentProviderOperation>，把每步操作放在它的对象中执行
     *
     * @throws OperationApplicationException
     * @throws RemoteException
     */
    public static void addContacts(final IBaseActivity context, final String avatar,
                                   final String name, final String mobile, final String email,
                                   final CommonCallback<String> callback, int requestCode) {
        if (!IPermissionCompat.checkSelfPermission((IContext) context, requestCode, Manifest.permission.WRITE_CONTACTS)) {
            return;
        }
        ImageLoader.getInstance().loadImage(avatar, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {
                if (callback != null) {
                    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
                    String msg = failReason.getCause().getMessage();
                    int code = IHttpError.CODE_SERVER_ERROR;
                    callback.onRequestFailure(code, msg);
                }
            }

            @Override
            public void onLoadingComplete(final String imageUri, View view,
                                          final Bitmap loadedImage) {
                new Thread(addContactRunnable(context, name, mobile, email,
                        loadedImage, imageUri, callback)).start();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });
    }

    private static Runnable addContactRunnable(final Context context,
                                               final String name, final String mobile, final String email,
                                               final Bitmap loadedImage, final String imageUri,
                                               final CommonCallback<String> callback) {
        return new Runnable() {
            public void run() {
                try {
                    addContacts(context, loadedImage, name, mobile, email);
                    Runnable runnableSuccess = new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onRequestSuccess(imageUri);
                            }
                        }
                    };
                    IAppHelper.runOnUiThread(runnableSuccess);
                } catch (final Exception e) {
                    e.printStackTrace();
                    Runnable runnableFailure = new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                String msg = e.getCause().getMessage();
                                int code = IHttpError.CODE_SERVER_ERROR;
                                callback.onRequestFailure(code, msg);
                            }
                        }
                    };
                    IAppHelper.runOnUiThread(runnableFailure);
                }
            }
        };
    }

    public static String formatMobile(String mobile) {
        int length;
        if (TextUtils.isEmpty(mobile) || (length = mobile.length()) < 11) {
            return null;
        }
        return mobile.substring(0, 3) +
                "****" +
                mobile.substring(length - 4, length);
    }

    public static final Pattern PATTERN = Pattern.compile(
            "<img\\s+(?:[^>]*)src\\s*=\\s*([^>]+)", Pattern.CASE_INSENSITIVE
                    | Pattern.MULTILINE);

    public static List<String> getImgSrc(String html) {
        Matcher matcher = PATTERN.matcher(html);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            String group = matcher.group(1);
            if (group == null) {
                continue;
            }
            // 这里可能还需要更复杂的判断,用以处理src="...."内的一些转义符
            if (group.startsWith("'")) {
                list.add(group.substring(1, group.indexOf("'", 1)));
            } else if (group.startsWith("\"")) {
                list.add(group.substring(1, group.indexOf("\"", 1)));
            } else {
                list.add(group.split("\\s")[0]);
            }
        }
        return list;
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        if (TextUtils.isEmpty(cardId)) {
            return false;
        }
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        return bit != 'N' && cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 得到两个日期相差的天数
     */
    public static int getBetweenDay(Date date1, Date date2) {
        Calendar d1 = new GregorianCalendar();
        d1.setTime(date1);
        Calendar d2 = new GregorianCalendar();
        d2.setTime(date2);
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        // System.out.println("days=" + days);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return (days < 0 ? 0 : days) + 1;
    }

    /**
     * 得到两个日期相差的天数
     */
    public static int getBetweenMonth(Date date1, Date date2) {
        Calendar d1 = new GregorianCalendar();
        d1.setTime(date1);
        Calendar d2 = new GregorianCalendar();
        d2.setTime(date2);
        int months = d2.get(Calendar.MONTH) - d1.get(Calendar.MONTH);
        // System.out.println("days=" + days);
        int y2 = d2.get(Calendar.YEAR);
        int y1 = d1.get(Calendar.YEAR);
        if (y1 != y2) {
            months = (y2 - y1) * 12 + months;
        }
        return (months < 0 ? 0 : months) + 1;
    }

    /**
     * 设置状态栏颜色
     *
     * @param baseFragment 需要设置的activity
     * @param colorResId   状态栏颜色值资源ID
     */
    public static void setStatusBarColor(IBaseFragment baseFragment, @ColorRes int colorResId) {
        setStatusBarColor(baseFragment, colorResId, true);
    }

    /**
     * 设置状态栏颜色
     *
     * @param baseFragment 需要设置的activity
     * @param colorResId   状态栏颜色值资源ID
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    public static void setStatusBarColor(IBaseFragment baseFragment, @ColorRes int colorResId, boolean fitSystemWindows) {
        if (isCanSetStatusBarColor() && baseFragment != null && baseFragment.getActivity() != null) {
            FragmentActivity activity = baseFragment.getActivity();

            // 添加 statusView 到布局中
            View view = baseFragment.getView();
            if (view != null && fitSystemWindows) {
                int paddingLeft = view.getPaddingLeft();
                int paddingTop = view.getPaddingTop();
                int paddingRight = view.getPaddingRight();
                int paddingBottom = view.getPaddingBottom();
                int statusBarHeight = getStatusBarHeight(activity);
                view.setPadding(paddingLeft, statusBarHeight, paddingRight, paddingBottom);
            }

            Window window = activity.getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            if (isCanSetStatusBarColor()) {
                window.setStatusBarColor(activity.getResources().getColor(colorResId));
            }
        }
    }

    public static boolean isCanSetStatusBarColor() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static int getActionBarHeight(Context context) {
        @SuppressLint("Recycle")
        TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        return actionbarSizeTypedArray.getDimensionPixelSize(0, 0);
    }

    public static void setDatePickerDividerHeight(DatePicker datePicker) {
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$id");

            Field fieldMonth = clazz.getField("month");
            fieldMonth.setAccessible(true);
            int hourId = fieldMonth.getInt(null);
            NumberPicker hourNumberPicker = (NumberPicker) datePicker.findViewById(hourId);
            setDividerHeight(hourNumberPicker);

            Field fieldDay = clazz.getField("day");
            fieldDay.setAccessible(true);
            int dayId = fieldDay.getInt(null);
            NumberPicker dayNumberPicker = (NumberPicker) datePicker.findViewById(dayId);
            setDividerHeight(dayNumberPicker);

            Field fieldYear = clazz.getField("year");
            fieldYear.setAccessible(true);
            int yearId = fieldYear.getInt(null);
            NumberPicker yearNumberPicker = (NumberPicker) datePicker.findViewById(yearId);
            setDividerHeight(yearNumberPicker);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTimePickerDividerHeight(TimePicker timePicker) {
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$id");

            Field fieldHour = clazz.getField("hour");
            fieldHour.setAccessible(true);
            int hourId = fieldHour.getInt(null);
            NumberPicker hourNumberPicker = (NumberPicker) timePicker.findViewById(hourId);
            setDividerHeight(hourNumberPicker);

            Field fieldMinute = clazz.getField("minute");
            fieldMinute.setAccessible(true);
            int minuteId = fieldMinute.getInt(null);
            NumberPicker minuteNumberPicker = (NumberPicker) timePicker.findViewById(minuteId);
            setDividerHeight(minuteNumberPicker);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDividerColorAndHeight(NumberPicker picker) {
        Field[] pickerFields = picker.getClass().getDeclaredFields();
        label:
        for (Field field : pickerFields) {
            String name = field.getName();
            switch (name) {
                case "UNSCALED_DEFAULT_SELECTION_DIVIDER_HEIGHT":
                    field.setAccessible(true);
                    try {
                        field.set(picker, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break label;
                case "mSelectionDivider":
                    field.setAccessible(true);
                    try {
                        ColorDrawable colorDrawable = new ColorDrawable();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            colorDrawable.setColor(Color.parseColor("#ffebebeb"));
                        }
                        field.set(picker, colorDrawable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break label;
                case "mSelectionDividerHeight":
                    field.setAccessible(true);
                    try {
                        field.set(picker, picker.getContext().getResources()
                                .getDimensionPixelSize(R.dimen.box_divider_height));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break label;
            }
        }
    }

    public static void setDividerHeight(NumberPicker picker) {
        Field[] pickerFields = picker.getClass().getDeclaredFields();
        label:
        for (Field field : pickerFields) {
            String name = field.getName();
            switch (name) {
                case "mSelectionDividerHeight":
                    field.setAccessible(true);
                    try {
                        field.set(picker, picker.getContext().getResources()
                                .getDimensionPixelSize(R.dimen.box_divider_height));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break label;
            }
        }
    }

    public static boolean isApplicationBroughtToBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static int getRowByCount(int count, int columns) {
        return count / columns + (count % columns > 0 ? 1 : 0);
    }

    public static Object getSuperField(Object instance, String variableName) {
        Class targetClass = instance.getClass().getSuperclass();
        Object superInst = targetClass.cast(instance);
        Field field;
        try {
            field = targetClass.getDeclaredField(variableName);
            //修改访问限制
            field.setAccessible(true);
            return field.get(superInst);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isGPSEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判断手机是否只是GPS定位
     */
    public static boolean hasGPSDevice(Context context) {
        if (context == null) {
            throw new IllegalAccessError("Please set context!");
        }
        @SuppressWarnings("deprecation")
        String str = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        return str != null && str.contains(LocationManager.GPS_PROVIDER);
    }

    public static void openGPSSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            activity.startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException e) {
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                activity.startActivityForResult(intent, 0);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public static boolean isNetAvailable(Context context) {
        ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        return !(cwjManager == null || (networkInfo = cwjManager.getActiveNetworkInfo()) == null) && networkInfo.isAvailable();
    }

    public static void openWIFISettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            activity.startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException e) {
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                activity.startActivityForResult(intent, 0);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void setLocale(Context context, Locale locale) {
        Resources resources = context.getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale); // 英文
        } else {
            //noinspection deprecation
            config.locale = locale;
        }
        resources.updateConfiguration(config, dm);
    }

}
