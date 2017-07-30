/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import net.izhuo.app.library.helper.IContextHelper;
import net.izhuo.app.library.helper.IFragmentHelper;
import net.izhuo.app.library.reader.picture.IOpenType;
import net.izhuo.app.library.util.IIntentCompat;
import net.izhuo.app.library.widget.IOSDialog;
import net.izhuo.app.library.widget.IProgress;

import java.util.List;

/**
 * Created by Changlei on 15/3/2.
 * <p>
 * Fragment基类
 */
@SuppressWarnings("unused")
public abstract class IBaseFragment extends Fragment implements IContext {

    private Fragment mFragment;
    private int mPage;

    private IContextHelper mHelper;

    public IBaseFragment() {
        mHelper = new IContextHelper(this);
    }

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = mHelper.getContainerView(container, savedInstanceState);
        if (containerView != null) {
            return containerView;
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public boolean onSetContentViewBefore(Bundle savedInstanceState) {
        return false;
    }

    @CallSuper
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHelper.onCreate(savedInstanceState);
        mHelper.setContentView(getView());
        mHelper.onContentChanged();
    }

    @Override
    public final void onUserCreateViews(Bundle savedInstanceState) {
    }

    @Override
    public Application getApplication() {
        return mHelper.getApplication();
    }

    @Override
    public Context getApplicationContext() {
        return mHelper.getApplicationContext();
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return mHelper.getApplicationInfo();
    }

    @Override
    public IFragmentHelper getFragmentHelper() {
        return mHelper.getFragmentHelper();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onRefreshUI();
        }
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

    public boolean onBackPressed() {
        return mHelper.onBackPressed();
    }

    public final Fragment getVisibaleFragment() {
        return mFragment;
    }

    public final void setVisibaleFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

    /**
     * @return the mPage
     */
    public final int getPage() {
        return mPage;
    }

    /**
     * @param page the page to set
     */
    public final IBaseFragment setPage(int page) {
        this.mPage = page;
        return this;
    }

    @Override
    public void onRefreshUI() {
    }

    @Nullable
    @Override
    public final <T extends View> T findViewById(@IdRes int id) {
        return mHelper.findViewById(id);
    }

    @Override
    public void finish() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public final FragmentManager getSupportFragmentManager() {
        return getFragmentManager();
    }

    @Override
    public final DisplayImageOptions getOptions(int radius, int loadingImage, int emptyUriImage, int failImage) {
        return mHelper.getOptions(radius, loadingImage, emptyUriImage, failImage);
    }

    @Override
    public final DisplayImageOptions getOptions(int radius, int defImage) {
        return mHelper.getOptions(radius, defImage);
    }

    @Override
    public final String getText(TextView textView) {
        return mHelper.getText(textView);
    }

    @Override
    public IProgress showLoad() {
        return mHelper.showLoad();
    }

    @Override
    public IProgress showLoad(IProgress.Theme theme, CharSequence message) {
        return mHelper.showLoad(theme, message);
    }

    @Override
    public IProgress showLoad(IProgress.Theme theme, int messageResId) {
        return mHelper.showLoad(theme, messageResId);
    }

    @Override
    public IProgress showLoad(IProgress.Theme theme) {
        return mHelper.showLoad(theme);
    }

    @Override
    public void loadDismiss() {
        mHelper.loadDismiss();
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
        return mHelper.startActivityForResult(cls, bundle, requestCode);
    }

    @Override
    public final IContext startActivityForResult(Class<?> cls, int requestCode) {
        return mHelper.startActivityForResult(cls, requestCode);
    }

    @Override
    public void startActivity(Intent intent, Bundle bundle) {
        IIntentCompat.startActivity(this, intent, bundle);
    }

    @Override
    public void startActivity(Intent intent) {
        IIntentCompat.startActivity(this, intent);
    }

    @Override
    public IContext startActivity(Class<?> cls, Bundle bundle) {
        return mHelper.startActivity(cls, bundle);
    }

    @Override
    public final IContext startActivity(Class<?> cls) {
        return mHelper.startActivity(cls);
    }

    @Override
    public final Bundle getBundle() {
        return mHelper.getBundle();
    }

    @Override
    public final Object showText(CharSequence text) {
        return mHelper.showText(text);
    }

    @Override
    public final Object showText(int res) {
        return mHelper.showText(res);
    }

    @Override
    public final Object showText(int res, Object... formatArgs) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
