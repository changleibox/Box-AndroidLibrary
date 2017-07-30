/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.umeng.analytics.MobclickAgent;

import net.izhuo.app.library.common.IConstants.IActivityCaches;
import net.izhuo.app.library.helper.IFragmentHelper;
import net.izhuo.app.library.reader.picture.IOpenType;
import net.izhuo.app.library.util.IActivityCompat;
import net.izhuo.app.library.util.IImageChooser;
import net.izhuo.app.library.util.IImageLoaderCompat;
import net.izhuo.app.library.util.IIntentCompat;
import net.izhuo.app.library.util.ILogCompat;
import net.izhuo.app.library.util.IProgressCompat;
import net.izhuo.app.library.util.IToastCompat;
import net.izhuo.app.library.util.IWebViewChooseFile;
import net.izhuo.app.library.widget.IOSDialog;
import net.izhuo.app.library.widget.IProgress;

import java.util.List;

/**
 * Created by Box on 15/1/12.
 * <p>
 * Activity基类
 */
@SuppressWarnings({"unused", "deprecation"})
public abstract class IBaseActivity extends AppCompatActivity implements IContext {

    private OnRequestPermissionsResultCallback mRequestPermissionsResultCallback;
    private OnActivityResultCallback mActivityResultCallback;
    public Context mContext;

    private String mTag;
    private View mContentView;

    private Bundle mSavedInstanceState;
    private IFragmentHelper mFragmentHelper;

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;

        mContext = this;
        mTag = getClass().getSimpleName();

        IActivityCaches.putActivity(this);

        boolean isContinue = onSetContentViewBefore(savedInstanceState);
        if (!isContinue) {
            Object containerLayout = getContainerLayout();
            if (containerLayout != null) {
                if (containerLayout instanceof Integer) {
                    setContentView((Integer) containerLayout);
                } else if (containerLayout instanceof View) {
                    setContentView((View) containerLayout);
                }
            }
        }
    }

    @Override
    public boolean onSetContentViewBefore(Bundle savedInstanceState) {
        return false;
    }

    @Override
    public void onRefreshUI() {
    }

    @Override
    public final DisplayImageOptions getOptions(int radius, int loadingImage, int emptyUriImage, int failImage) {
        return IImageLoaderCompat.getOptions(radius, loadingImage, emptyUriImage, failImage);
    }

    @Override
    public final DisplayImageOptions getOptions(int radius, int defImage) {
        return IImageLoaderCompat.getOptions(radius, defImage);
    }

    public final DisplayImageOptions getOptions(int radius) {
        return IImageLoaderCompat.getOptions(radius);
    }

    @Override
    public final void setContentView(int layoutResID) {
        setContentView(LayoutInflater.from(mContext).inflate(layoutResID, null));
    }

    @Override
    public final void setContentView(View contentView) {
        super.setContentView(contentView);
        try {
            IActivityCompat.autoInjectAllField(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mContentView = contentView;

        onUserCreateViews(mSavedInstanceState);
    }

    public final View getContentView() {
        return mContentView;
    }

    @Override
    public void onContentChanged() {
        initViews(mSavedInstanceState);
        initDatas(mSavedInstanceState);
        initListeners(mSavedInstanceState);
    }

    @Override
    public final String getText(TextView textView) {
        if (textView != null) {
            return textView.getText().toString();
        } else {
            return "";
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public IFragmentHelper getFragmentHelper() {
        if (mFragmentHelper == null) {
            mFragmentHelper = new IFragmentHelper(getSupportFragmentManager());
        }
        return mFragmentHelper;
    }

    public final int getWidth() {
        return IActivityCompat.getWidth(this);
    }

    public final int getHeight() {
        return IActivityCompat.getHeight(this);
    }

    @Override
    public IProgress showLoad(CharSequence message) {
        return IProgressCompat.showLoad(this, message);
    }

    @Override
    public IProgress showLoad(int messageResId) {
        return IProgressCompat.showLoad(this, messageResId);
    }

    @Override
    public IProgress showLoad() {
        return IProgressCompat.showLoad(this);
    }

    @Override
    public IProgress showLoad(IProgress.Theme theme, CharSequence message) {
        return IProgressCompat.showLoad(this, theme, message);
    }

    @Override
    public IProgress showLoad(IProgress.Theme theme, int messageResId) {
        return IProgressCompat.showLoad(this, theme, messageResId);
    }

    @Override
    public IProgress showLoad(IProgress.Theme theme) {
        return IProgressCompat.showLoad(this, theme);
    }

    @Override
    public void loadDismiss() {
        IProgressCompat.loadDismiss(this);
    }

    @Deprecated
    public final IContext startActivityForResult(Class<?> cls, String data, int type, int requestCode) {
        return IIntentCompat.startActivityForResult(this, cls, data, type, requestCode);
    }

    @Deprecated
    public final IContext intentDataForResult(Class<?> cls, String data, int requestCode) {
        return IIntentCompat.startActivityDataForResult(this, cls, data, requestCode);
    }

    @Deprecated
    public final IContext intentTypeForResult(Class<?> cls, int type, int requestCode) {
        return IIntentCompat.startActivityTypeForResult(this, cls, type, requestCode);
    }

    @Deprecated
    public final IContext startActivity(Class<?> cls, String data, int type) {
        return IIntentCompat.startActivity(this, cls, data, type);
    }

    @Deprecated
    public final IContext intentData(Class<?> cls, String data) {
        return IIntentCompat.startActivityData(this, cls, data);
    }

    @Deprecated
    public final IContext intentType(Class<?> cls, int type) {
        return IIntentCompat.startActivityType(this, cls, type);
    }

    @Deprecated
    public final String getIntentData() {
        return IIntentCompat.getIntentData(this);
    }

    @Deprecated
    public final int getIntentType() {
        return IIntentCompat.getIntentType(this);
    }

    @Override
    public void startActivityForResult(Intent intent, Bundle bundle, int requestCode) {
        IIntentCompat.startActivityForResult((IContext) this, intent, bundle, requestCode);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        IIntentCompat.startActivityForResult((IContext) this, intent, requestCode);
    }

    @Override
    public IContext startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        return IIntentCompat.startActivityForResult((IContext) this, cls, bundle, requestCode);
    }

    @Override
    public final IContext startActivityForResult(Class<?> cls, int requestCode) {
        return IIntentCompat.startActivityForResult((IContext) this, cls, requestCode);
    }

    @Override
    public void startActivity(Intent intent, Bundle bundle) {
        IIntentCompat.startActivity((IContext) this, intent, bundle);
    }

    @Override
    public void startActivity(Intent intent) {
        IIntentCompat.startActivity((IContext) this, intent);
    }

    @Override
    public IContext startActivity(Class<?> cls, Bundle bundle) {
        return IIntentCompat.startActivity((IContext) this, cls, bundle);
    }

    @Override
    public final IContext startActivity(Class<?> cls) {
        return IIntentCompat.startActivity((IContext) this, cls);
    }

    @Override
    public final Bundle getBundle() {
        return IIntentCompat.getBundle((IContext) this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        onRefreshUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        IActivityCaches.removeActivity(this);
        super.onDestroy();
    }

    @Override
    public final Object showText(CharSequence text) {
        return IToastCompat.showText(mContext, text);
    }

    @Override
    public final Object showText(int res) {
        return IToastCompat.showText(mContext, res);
    }

    @Override
    public final Object showText(int res, Object... formatArgs) {
        return IToastCompat.showText(mContext, res, formatArgs);
    }

    @Override
    public final IOSDialog showTextDialog(String title, String message, IOSDialog.OnClickListener l) {
        return IActivityCompat.showTextDialog(this, title, message, l);
    }

    @Override
    public final void i(Object msg) {
        ILogCompat.i(mTag, msg);
    }

    @Override
    public final void d(Object msg) {
        ILogCompat.d(mTag, msg);
    }

    @Override
    public final void e(Object msg) {
        ILogCompat.e(mTag, msg);
    }

    @Override
    public final void v(Object msg) {
        ILogCompat.v(mTag, msg);
    }

    @Override
    public final void w(Object msg) {
        ILogCompat.w(mTag, msg);
    }

    @Override
    public final void exitApplication() {
        IActivityCompat.exitApplication(this);
    }

    @Override
    public final IContext showExitText() {
        return IActivityCompat.buildExit().showExitText(this);
    }

    @Override
    public final IContext showExitText(final Callback callback) {
        return IActivityCompat.buildExit().showExitText(this, callback);
    }

    @Override
    public final void hideKeyboard() {
        IActivityCompat.hideKeyboard(this);
    }

    @Override
    public final void showKeyboard() {
        IActivityCompat.showKeyboard(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    @Override
    public final void intentForPicture(List<String> datas, int maxCount) {
        IImageChooser.intentForPicture(this, datas, maxCount);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    @Override
    public final void intentForPicture(IOpenType.Type type, List<String> totalImages, List<String> selectImages, int selectIndex, int maxSelectCount) {
        IImageChooser.intentForPicture(this, type, totalImages, selectImages, selectIndex, maxSelectCount);
    }

    @Override
    public final void setWebViewCommonAttribute(WebView webView) {
        IWebViewChooseFile.getInstance(this).setWebViewCommonAttribute(webView);
    }

    @CallSuper
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IActivityCompat.onActivityResult(this, requestCode, resultCode, data);
        if (mActivityResultCallback != null) {
            mActivityResultCallback.onActivityResult(requestCode, resultCode, data);
        }
    }

    @CallSuper
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mRequestPermissionsResultCallback != null) {
            mRequestPermissionsResultCallback.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onImageChooseCallback(List<String> imagePaths) {
    }

    @Override
    public void onFileChooseCallback(@Nullable String filePath) {
    }

    @Override
    public void setOnActivityResultCallback(OnActivityResultCallback callback) {
        mActivityResultCallback = callback;
    }

    @Override
    public void setOnRequestPermissionsResultCallback(OnRequestPermissionsResultCallback callback) {
        mRequestPermissionsResultCallback = callback;
    }
}
