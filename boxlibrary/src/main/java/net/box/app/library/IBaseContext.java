/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import net.box.app.library.helper.IFragmentHelper;
import net.box.app.library.reader.picture.IOpenType;
import net.box.app.library.widget.IOSDialog;
import net.box.app.library.widget.IProgress;

import java.util.List;

/**
 * Created by Box on 2017/7/30.
 * <p>
 * BaseContext
 */

@SuppressWarnings("unused")
public interface IBaseContext {

    DisplayImageOptions getOptions(int radius, @DrawableRes int loadingImage, @DrawableRes int emptyUriImage, @DrawableRes int failImage);

    DisplayImageOptions getOptions(int radius, @DrawableRes int defImage);

    /**
     * 获取TextView及子控件上的Text
     */
    String getText(TextView textView);

    /**
     * 显示加载数据条 @param message 需要显示的文本
     */
    IProgress showLoad(CharSequence message);

    /**
     * 显示加载数据条 @param messageResId 需要显示的文本资源Id
     * <p>
     * {@link #showLoad(CharSequence)} 重载方法
     */
    IProgress showLoad(@StringRes int messageResId);

    /**
     * 显示加载数据条
     * <p>
     * {@link #showLoad(int)} 重载方法
     */
    IProgress showLoad();

    /**
     * 显示加载数据条 @param message 需要显示的文本
     */
    IProgress showLoad(IProgress.Theme theme, CharSequence message);

    /**
     * 显示加载数据条 @param messageResId 需要显示的文本资源Id
     * <p>
     * {@link #showLoad(IProgress.Theme, CharSequence)} 重载方法
     */
    IProgress showLoad(IProgress.Theme theme, @StringRes int messageResId);

    /**
     * 显示加载数据条
     * <p>
     * {@link #showLoad(IProgress.Theme, int)} 重载方法
     */
    IProgress showLoad(IProgress.Theme theme);

    /**
     * 关闭进度条
     */
    void loadDismiss();

    <T extends Activity> IContext startActivityForResult(Class<T> cls, Bundle bundle, int requestCode);

    <T extends Activity> IContext startActivityForResult(Class<T> cls, int requestCode);

    <T extends Activity> IContext startActivity(Class<T> cls, Bundle bundle);

    <T extends Activity> IContext startActivity(Class<T> cls);

    Bundle getBundle();

    <T> T showText(CharSequence text);

    <T> T showText(@StringRes int res);

    <T> T showText(@StringRes int res, Object... formatArgs);

    IOSDialog showTextDialog(String title, String message, IOSDialog.OnClickListener l);

    void i(Object msg);

    void d(Object msg);

    void e(Object msg);

    void v(Object msg);

    void w(Object msg);

    IContext showExitText();

    IContext showExitText(final Handler.Callback callback);

    void hideKeyboard();

    void showKeyboard();

    /**
     * 跳转到选择图片页面
     *
     * @param datas    已经选中的图片，类行为List<String>，如果没有则穿null
     * @param maxCount 需要选择的最多图片数量
     */
    void startActivityForPicture(List<String> datas, @IntRange(from = 1) int maxCount);

    /**
     * 跳转到查看图片页面
     *
     * @param type           此处传是查看，还是选择预览，具体请看 {@link IOpenType}
     * @param totalImages    总的图片路径集合
     * @param selectImages   选中的图片路径
     * @param selectIndex    当前查看的图片的索引
     * @param maxSelectCount 总的需要选择的图片数量，选择预览时使用，查看时传0
     */
    void startActivityForPicture(IOpenType.Type type, List<String> totalImages, List<String> selectImages, int selectIndex, int maxSelectCount);

    void setWebViewCommonAttribute(WebView webView);

    <T extends View> T findViewById(@IdRes int id);

    void exitApplication();

    Application getApplication();

    Context getApplicationContext();

    ApplicationInfo getApplicationInfo();

    IFragmentHelper getFragmentHelper();

    void setOnActivityResultCallback(OnActivityResultCallback callback);

    void setOnRequestPermissionsResultCallback(OnRequestPermissionsResultCallback callback);

    interface OnActivityResultCallback {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
