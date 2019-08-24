/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler.Callback;
import androidx.annotation.CallSuper;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Changlei on 15/3/2.
 * <p>
 * Fragment基类
 */
@SuppressWarnings({"unused"})
public abstract class IFragment extends Fragment implements IContext {

    private Fragment mFragment;
    private int mPage;

    private IContextHelper mHelper;

    public IFragment() {
        mHelper = new IContextHelper(this);
    }

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

    @Nullable
    @Override
    public Application getApplication() {
        return mHelper.getApplication();
    }

    @Nullable
    @Override
    public Context getApplicationContext() {
        return mHelper.getApplicationContext();
    }

    @Nullable
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
    public final IFragment setPage(int page) {
        this.mPage = page;
        return this;
    }

    @Override
    public final void onRefreshUI() {
        final FragmentActivity activity = getActivity();
        if (activity != null) {
            onRefreshUI(activity);
        }
    }

    public void onRefreshUI(@NonNull Activity activity) {
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
        return getChildFragmentManager();
    }

    @Override
    public final DisplayImageOptions getOptions(int radius, @DrawableRes int loadingImage, @DrawableRes int emptyUriImage, @DrawableRes int failImage) {
        return mHelper.getOptions(radius, loadingImage, emptyUriImage, failImage);
    }

    @Override
    public final DisplayImageOptions getOptions(int radius, @DrawableRes int defImage) {
        return mHelper.getOptions(radius, defImage);
    }

    @Override
    public final String getText(TextView textView) {
        return mHelper.getText(textView);
    }

    @Override
    public IProgressImpl showLoad(CharSequence message) {
        return mHelper.showLoad(message);
    }

    @Override
    public IProgressImpl showLoad(@StringRes int messageResId) {
        return mHelper.showLoad(messageResId);
    }

    @Nullable
    @Override
    public IProgressImpl showLoad() {
        return mHelper.showLoad();
    }

    @Nullable
    @Override
    public IProgressImpl showLoad(IProgress.Theme theme, CharSequence message) {
        return mHelper.showLoad(theme, message);
    }

    @Nullable
    @Override
    public IProgressImpl showLoad(IProgress.Theme theme, @StringRes int messageResId) {
        return mHelper.showLoad(theme, messageResId);
    }

    @Nullable
    @Override
    public IProgressImpl showLoad(IProgress.Theme theme) {
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
