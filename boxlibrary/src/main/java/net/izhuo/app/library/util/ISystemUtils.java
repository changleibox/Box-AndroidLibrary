package net.izhuo.app.library.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Intents.Insert;
import android.provider.MediaStore.Audio.Media;
import android.support.annotation.RequiresPermission;

/**
 * Created by Box on 16/9/19.
 * <p>
 * 存放系统工具
 */
@SuppressWarnings("unused")
public class ISystemUtils {

    /**
     * 从google搜索内容
     */
    public static void openGoogleSearch(Context context, String text) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, text);
        context.startActivity(intent);
    }

    /**
     * 浏览网页
     */
    public static void browseWebPage(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    /**
     * 显示地图
     */
    public static void openGoogleMap(Context context, long latitude, long longitude) {
        Uri uri = Uri.parse("geo:" + longitude + "," + latitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    /**
     * 路径规划
     */
    public static void pathPlan(Context context, long startLat, long endLat, long startLng, long endLng) {
        Uri uri = Uri.parse("http://maps.google.com/maps?f=dsaddr=" + startLat + "%20" + startLng + "&daddr=" + endLat + "%20" + endLng + "&hl=en");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    /**
     * 拨打电话
     */
    @RequiresPermission(value = Manifest.permission.CALL_PHONE)
    public static void callPhone(Context context, String phone) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(intent);
    }

    /**
     * 调用发短信的程序
     */
    @RequiresPermission(value = Manifest.permission.SEND_SMS)
    public static void openSMS(Context context, String smsBody) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("sms_body", smsBody);
        intent.setType("vnd.android-dir/mms-sms");
        context.startActivity(intent);
    }

    /**
     * 发送短信
     */
    @RequiresPermission(value = Manifest.permission.SEND_SMS)
    public static void sendSMS(Context context, String phone, String smsBody) {
        Uri uri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", smsBody);
        context.startActivity(intent);
    }

    /**
     * 发送彩信
     */
    @RequiresPermission(value = Manifest.permission.SEND_SMS)
    public static void sendMMS(Context context, String smsBody) {
        Uri uri = Uri.parse("content://media/external/images/media/23");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra("sms_body", smsBody);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/png");
        context.startActivity(intent);
    }

    /**
     * 发送Email
     */
    @RequiresPermission(value = Manifest.permission.SEND_SMS)
    public static void sendEmail(Context context, String email, String text, String chooserTitle) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent, chooserTitle));
    }

    /**
     * 播放多媒体
     */
    @SuppressLint("InlinedApi")
    @RequiresPermission(value = Manifest.permission.READ_EXTERNAL_STORAGE)
    public static void playMedia(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(path);
        intent.setDataAndType(uri, "audio/mp3");
        context.startActivity(intent);
    }

    /**
     * 卸载
     */
    public static void uninstall(Context context, String packageName) {
        Uri uri = Uri.fromParts("package", packageName, null);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }

    /**
     * 安装
     */
    public static void install(Context context, String path) {
        Uri installUri = Uri.fromParts("package", path, null);
        Intent intent = new Intent(Intent.ACTION_PACKAGE_ADDED, installUri);
        context.startActivity(intent);
    }

    /**
     * 从gallery选取图片
     */
    @SuppressLint("InlinedApi")
    @RequiresPermission(value = Manifest.permission.READ_EXTERNAL_STORAGE)
    public static void selectImage(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开录音机
     */
    @RequiresPermission(value = Manifest.permission.CAMERA)
    public static void openCamera(Context context) {
        Intent intent = new Intent(Media.RECORD_SOUND_ACTION);
        context.startActivity(intent);
    }

    /**
     * 显示应用详细列表
     */
    public static void showAppList(Context context) {
        Uri uri = Uri.parse("market://details?id=app_id");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    /**
     * 寻找应用
     */
    public static void searchApp(Context context, String packageName) {
        Uri uri = Uri.parse("market://search?q=pname:" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    /**
     * 打开联系人列表
     */
    @RequiresPermission(value = Manifest.permission.READ_CONTACTS)
    public static void openContactList(Activity activity, int requestCode) {
        Uri uri = Uri.parse("content://contacts/people");
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 调用系统编辑添加联系人（高版本SDK有效）：
     */
    @RequiresPermission(value = Manifest.permission.WRITE_CONTACTS)
    public static void addContact(Context context, String name, String phone, String secondPhone, String tertiaryPhone, String email, String jobTitle, String company) {
        Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        intent.setType(Contacts.CONTENT_ITEM_TYPE);
        intent.putExtra(Insert.NAME, name);
        intent.putExtra(Insert.COMPANY, company);
        intent.putExtra(Insert.EMAIL, email);
        intent.putExtra(Insert.PHONE, phone);
        intent.putExtra(Insert.SECONDARY_PHONE, secondPhone);
        intent.putExtra(Insert.TERTIARY_PHONE, tertiaryPhone);
        intent.putExtra(Insert.JOB_TITLE, jobTitle);
        context.startActivity(intent);
    }

}
