/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import net.box.app.library.helper.IContextHelper;
import net.box.app.library.helper.IFragmentHelper;
import net.box.app.library.impl.IProgressImpl;
import net.box.app.library.reader.picture.IOpenType;
import net.box.app.library.util.IIntentCompat;
import net.box.app.library.widget.IOSDialog;
import net.box.app.library.widget.IProgress;

import java.util.List;

/**
 * Created by Box on 15/1/12.
 * <p>
 * Activity基类
 */
@SuppressWarnings({"unused", "deprecation"})
public abstract class IAppCompatActivity extends AppCompatActivity implements IContext {

    private IContextHelper mHelper;

    public IAppCompatActivity() {
        mHelper = new IContextHelper(this);
    }

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper.onCreate(savedInstanceState);
    }

    @Override
    public boolean onSetContentViewBefore(Bundle savedInstanceState) {
        return false;
    }

    @Override
    public void onRefreshUI() {
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mHelper.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View contentView) {
        mHelper.setContentView(contentView);
        super.setContentView(contentView);
    }

    public final View getContentView() {
        return mHelper.getContentView();
    }

    @Override
    public void onContentChanged() {
        mHelper.onContentChanged();
    }

    @SuppressWarnings("unchecked")
    @Override
    public View findViewById(@IdRes int id) {
        return super.findViewById(id);
    }

    @Override
    public final DisplayImageOptions getOptions(int radius, @DrawableRes int loadingImage, @DrawableRes int emptyUriImage, @DrawableRes int failImage) {
        return mHelper.getOptions(radius, loadingImage, emptyUriImage, failImage);
    }

    @Override
    public final DisplayImageOptions getOptions(int radius, @DrawableRes int defImage) {
        return mHelper.getOptions(radius, defImage);
    }

    public final DisplayImageOptions getOptions(int radius) {
        return mHelper.getOptions(radius);
    }

    @Override
    public final String getText(TextView textView) {
        return mHelper.getText(textView);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public IFragmentHelper getFragmentHelper() {
        return mHelper.getFragmentHelper();
    }

    public final int getWidth() {
        return mHelper.getWidth();
    }

    public final int getHeight() {
        return mHelper.getHeight();
    }

    @Override
    public IProgressImpl showLoad(CharSequence message) {
        return mHelper.showLoad(message);
    }

    @Override
    public IProgressImpl showLoad(@StringRes int messageResId) {
        return mHelper.showLoad(messageResId);
    }

    @Override
    public IProgressImpl showLoad() {
        return mHelper.showLoad();
    }

    @Override
    public IProgressImpl showLoad(IProgress.Theme theme, CharSequence message) {
        return mHelper.showLoad(theme, message);
    }

    @Override
    public IProgressImpl showLoad(IProgress.Theme theme, @StringRes int messageResId) {
        return mHelper.showLoad(theme, messageResId);
    }

    @Override
    public IProgressImpl showLoad(IProgress.Theme theme) {
        return mHelper.showLoad(theme);
    }

    @Override
    public void loadDismiss() {
        mHelper.loadDismiss();
    }

    @Deprecated
    public final <T extends Activity> IContext startActivityForResult(Class<T> cls, String data, int type, int requestCode) {
        return IIntentCompat.startActivityForResult(this, cls, data, type, requestCode);
    }

    @Deprecated
    public final <T extends Activity> IContext startActivityDataForResult(Class<T> cls, String data, int requestCode) {
        return IIntentCompat.startActivityDataForResult(this, cls, data, requestCode);
    }

    @Deprecated
    public final <T extends Activity> IContext startActivityTypeForResult(Class<T> cls, int type, int requestCode) {
        return IIntentCompat.startActivityTypeForResult(this, cls, type, requestCode);
    }

    @Deprecated
    public final <T extends Activity> IContext startActivity(Class<T> cls, String data, int type) {
        return IIntentCompat.startActivity(this, cls, data, type);
    }

    @Deprecated
    public final <T extends Activity> IContext startActivityData(Class<T> cls, String data) {
        return IIntentCompat.startActivityData(this, cls, data);
    }

    @Deprecated
    public final <T extends Activity> IContext startActivityType(Class<T> cls, int type) {
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
    public <T extends Activity> IContext startActivityForResult(Class<T> cls, Bundle bundle, int requestCode) {
        return mHelper.startActivityForResult(cls, bundle, requestCode);
    }

    @Override
    public final <T extends Activity> IContext startActivityForResult(Class<T> cls, int requestCode) {
        return mHelper.startActivityForResult(cls, requestCode);
    }

    @Override
    public <T extends Activity> IContext startActivity(Class<T> cls, Bundle bundle) {
        return mHelper.startActivity(cls, bundle);
    }

    @Override
    public final <T extends Activity> IContext startActivity(Class<T> cls) {
        return mHelper.startActivity(cls);
    }

    @Override
    public final Bundle getBundle() {
        return mHelper.getBundle();
    }

    @Override
    public void onResume() {
        super.onResume();
        mHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.onDestroy();
    }

    @Override
    public final <T> T showText(CharSequence text) {
        return mHelper.showText(text);
    }

    @Override
    public final <T> T showText(@StringRes int res) {
        return mHelper.showText(res);
    }

    @Override
    public final <T> T showText(@StringRes int res, Object... formatArgs) {
        return mHelper.showText(res, formatArgs);
    }

    @Override
    public final IOSDialog showTextDialog(String title, String message, IOSDialog.OnClickListener l) {
        return mHelper.showTextDialog(title, message, l);
    }

    @Override
    public final void i(Object msg) {
        mHelper.i(msg);
    }

    @Override
    public final void d(Object msg) {
        mHelper.d(msg);
    }

    @Override
    public final void e(Object msg) {
        mHelper.e(msg);
    }

    @Override
    public final void v(Object msg) {
        mHelper.v(msg);
    }

    @Override
    public final void w(Object msg) {
        mHelper.w(msg);
    }

    @Override
    public final void exitApplication() {
        mHelper.exitApplication();
    }

    @Override
    public final IContext showExitText() {
        return mHelper.showExitText();
    }

    @Override
    public final IContext showExitText(final Callback callback) {
        return mHelper.showExitText(callback);
    }

    @Override
    public final void hideKeyboard() {
        mHelper.hideKeyboard();
    }

    @Override
    public final void showKeyboard() {
        mHelper.showKeyboard();
    }

    @SuppressWarnings("MissingPermission")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    @Override
    public final void startActivityForPicture(List<String> datas, int maxCount) {
        mHelper.startActivityForPicture(datas, maxCount);
    }

    @SuppressWarnings("MissingPermission")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    @Override
    public final void startActivityForPicture(IOpenType.Type type, List<String> totalImages, List<String> selectImages, int selectIndex, int maxSelectCount) {
        mHelper.startActivityForPicture(type, totalImages, selectImages, selectIndex, maxSelectCount);
    }

    @Override
    public final void setWebViewCommonAttribute(WebView webView) {
        mHelper.setWebViewCommonAttribute(webView);
    }

    @CallSuper
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mHelper.onActivityResult(requestCode, resultCode, data);
    }

    @CallSuper
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void setOnActivityResultCallback(OnActivityResultCallback callback) {
        mHelper.setOnActivityResultCallback(callback);
    }

    @Override
    public void setOnRequestPermissionsResultCallback(OnRequestPermissionsResultCallback callback) {
        mHelper.setOnRequestPermissionsResultCallback(callback);
    }

    @Override
    public void onImageChooseCallback(List<String> imagePaths) {
    }

    @Override
    public void onFileChooseCallback(@Nullable String filePath) {
    }
}