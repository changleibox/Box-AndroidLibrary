/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import net.izhuo.app.library.reader.picture.IOpenType;
import net.izhuo.app.library.widget.IOSDialog;
import net.izhuo.app.library.widget.IProgress;

import java.util.List;

/**
 * Created by Changlei on 15/9/1.
 * <p>
 * activity需要实现的方法的接口
 */
@SuppressWarnings("unused")
public interface IContext {

    /**
     * <p>
     * <em>Derived classes must call through to the super class's implementation
     * of this method. If they do not, an exception will be thrown.</em>
     * </p>
     */
    void onUserCreateViews(Bundle savedInstanceState);

    /**
     * 初始化View
     */
    void initViews(Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    void initDatas(Bundle savedInstanceState);

    /**
     * 初始化数据监听
     */
    void initListeners(Bundle savedInstanceState);

    DisplayImageOptions getOptions(int radius, int loadingImage, int emptyUriImage, int failImage);

    DisplayImageOptions getOptions(int radius, int defImage);

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
    IProgress showLoad(int messageResId);

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
    IProgress showLoad(IProgress.Theme theme, int messageResId);

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

    IContext intentForResult(Class<?> cls, Bundle bundle, int requestCode);

    IContext intentForResult(Class<?> cls, int requestCode);

    IContext intentForResult(Intent intent, Bundle bundle, int requestCode);

    IContext intentForResult(Intent intent, int requestCode);

    IContext intent(Class<?> cls, Bundle bundle);

    IContext intent(Class<?> cls);

    IContext intent(Intent intent, Bundle bundle);

    IContext intent(Intent intent);

    Bundle getBundle();

    Object showText(CharSequence text);

    Object showText(int res);

    Object showText(int res, Object... formatArgs);

    IOSDialog showTextDialog(String title, String message, IOSDialog.OnClickListener l);

    void i(Object msg);

    void d(Object msg);

    void e(Object msg);

    void v(Object msg);

    void w(Object msg);

    IContext showExitText();

    IContext showExitText(final Callback callback);

    void hideKeyboard();

    void showKeyboard();

    /**
     * 跳转到选择图片页面
     *
     * @param datas    已经选中的图片，类行为List<String>，如果没有则穿null
     * @param maxCount 需要选择的最多图片数量
     */
    void intentForPicture(List<String> datas, @IntRange(from = 1) int maxCount);

    /**
     * 跳转到查看图片页面
     *
     * @param type           此处传是查看，还是选择预览，具体请看 {@link IOpenType}
     * @param totalImages    总的图片路径集合
     * @param selectImages   选中的图片路径
     * @param selectIndex    当前查看的图片的索引
     * @param maxSelectCount 总的需要选择的图片数量，选择预览时使用，查看时传0
     */
    void intentForPicture(IOpenType.Type type, List<String> totalImages, List<String> selectImages, int selectIndex, int maxSelectCount);

    void setWebViewCommonAttribute(WebView webView);

    /**
     * 刷新ui
     */
    void onRefreshUI();

    FragmentManager getSupportFragmentManager();

    void finish();

    View findViewById(int id);

    void exitApplication();

    Context getContext();

    Activity getActivity();

    Application getApplication();

    Context getApplicationContext();

    ApplicationInfo getApplicationInfo();

    void onImageChooseCallback(List<String> imagePaths);

    void onFileChooseCallback(@Nullable String filePath);

    Object getContainerLayout();

    boolean onSetContentViewBefore(Bundle savedInstanceState);

    void setOnActivityResultCallback(OnActivityResultCallback callback);

    void setOnRequestPermissionsResultCallback(OnRequestPermissionsResultCallback callback);

    interface OnActivityResultCallback {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

}
