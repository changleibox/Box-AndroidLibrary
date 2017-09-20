/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.umeng.analytics.MobclickAgent;

import net.izhuo.app.library.IBaseContext;
import net.izhuo.app.library.IContext;
import net.izhuo.app.library.common.IConstants;
import net.izhuo.app.library.common.IConstants.IActivityCaches;
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
 * Created by Box on 2017/7/30.
 * <p>
 * baseContext辅助类
 */

public class IContextHelper implements IBaseContext {

    private final String TAG;

    private OnRequestPermissionsResultCallback mRequestPermissionsResultCallback;
    private OnActivityResultCallback mActivityResultCallback;

    private IContext mContext;
    private View mContentView;

    private Bundle mSavedInstanceState;
    private IFragmentHelper mFragmentHelper;

    public IContextHelper(@NonNull IContext context) {
        TAG = context.getClass().getSimpleName();
        this.mContext = context;
    }

    public void onCreate(Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;

        Activity activity = getActivity();
        if (activity == null || !(mContext instanceof Activity)) {
            return;
        }
        IActivityCaches.putActivity(activity);

        View containerView = getContainerView(getFrameViewGroup(), savedInstanceState);
        if (containerView != null) {
            activity.setContentView(containerView);
        }
    }

    @Nullable
    public View getContainerView(@Nullable ViewGroup container, Bundle savedInstanceState) {
        boolean isContinue = mContext.onSetContentViewBefore(savedInstanceState);
        View containerView = null;
        if (!isContinue) {
            Object containerLayout = mContext.getContainerLayout();
            if (containerLayout != null) {
                if (containerLayout instanceof Integer) {
                    containerView = inflate((int) containerLayout, container, false);
                } else if (containerLayout instanceof View) {
                    containerView = (View) containerLayout;
                }
            }
        }
        return containerView;
    }

    public final void setContentView(@LayoutRes int layoutResID) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        activity.setContentView(inflate(layoutResID, getFrameViewGroup(), false));
    }

    public final void setContentView(View contentView) {
        mContentView = contentView;
        try {
            IActivityCompat.autoInjectAllField(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mContext.onUserCreateViews(mSavedInstanceState);
    }

    public final View getContentView() {
        return mContentView;
    }

    public void onContentChanged() {
        mContext.initViews(mSavedInstanceState);
        mContext.initDatas(mSavedInstanceState);
        mContext.initListeners(mSavedInstanceState);
    }

    public void onResume() {
        if (mContext instanceof Fragment) {
            MobclickAgent.onPageStart(TAG);
        } else {
            MobclickAgent.onResume(getActivity());
        }
        mContext.onRefreshUI();
    }

    public void onPause() {
        if (mContext instanceof Fragment) {
            MobclickAgent.onPageEnd(TAG);
        } else {
            MobclickAgent.onPause(getActivity());
        }
    }

    public void onDestroy() {
        if (mContext instanceof Fragment) {
            return;
        }
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        IActivityCaches.removeActivity(activity);
    }

    public boolean onBackPressed() {
        if (mContext instanceof Activity) {
            ((Activity) mContext).onBackPressed();
            return true;
        }
        FragmentManager manager = mContext.getSupportFragmentManager();
        int count = manager.getBackStackEntryCount();
        if (count > 0) {
            manager.popBackStack();
        }
        return count == 0;
    }

    public final int getWidth() {
        if (mContext instanceof Fragment) {
            View view = ((Fragment) mContext).getView();
            return view == null ? 0 : view.getWidth();
        }
        return IActivityCompat.getWidth(getActivity());
    }

    public final int getHeight() {
        if (mContext instanceof Fragment) {
            View view = ((Fragment) mContext).getView();
            return view == null ? 0 : view.getHeight();
        }
        return IActivityCompat.getHeight(getActivity());
    }

    @Override
    public final DisplayImageOptions getOptions(int radius, @DrawableRes int loadingImage, @DrawableRes int emptyUriImage, @DrawableRes int failImage) {
        return IImageLoaderCompat.getOptions(radius, loadingImage, emptyUriImage, failImage);
    }

    @Override
    public final DisplayImageOptions getOptions(int radius, @DrawableRes int defImage) {
        return IImageLoaderCompat.getOptions(radius, defImage);
    }

    public final DisplayImageOptions getOptions(int radius) {
        return IImageLoaderCompat.getOptions(radius);
    }

    @Override
    public final String getText(TextView textView) {
        if (textView != null) {
            return textView.getText().toString();
        } else {
            return IConstants.EMPTY;
        }
    }

    @Override
    public IProgress showLoad(CharSequence message) {
        Activity activity = getActivity();
        if (activity == null) {
            return null;
        }
        return IProgressCompat.showLoad(activity, message);
    }

    @Override
    public IProgress showLoad(@StringRes int messageResId) {
        return IProgressCompat.showLoad(getActivity(), messageResId);
    }

    @Override
    public IProgress showLoad() {
        return IProgressCompat.showLoad(getActivity());
    }

    @Override
    public IProgress showLoad(IProgress.Theme theme, CharSequence message) {
        Activity activity = getActivity();
        if (activity == null) {
            return null;
        }
        return IProgressCompat.showLoad(activity, theme, message);
    }

    @Override
    public IProgress showLoad(IProgress.Theme theme, @StringRes int messageResId) {
        Activity activity = getActivity();
        if (activity == null) {
            return null;
        }
        return IProgressCompat.showLoad(activity, theme, messageResId);
    }

    @Override
    public IProgress showLoad(IProgress.Theme theme) {
        Activity activity = getActivity();
        if (activity == null) {
            return null;
        }
        return IProgressCompat.showLoad(activity, theme);
    }

    @Override
    public void loadDismiss() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        IProgressCompat.loadDismiss(activity);
    }

    @Override
    public <T extends Activity> IContext startActivityForResult(Class<T> cls, Bundle bundle, int requestCode) {
        return IIntentCompat.startActivityForResult(mContext, cls, bundle, requestCode);
    }

    @Override
    public <T extends Activity> IContext startActivityForResult(Class<T> cls, int requestCode) {
        return IIntentCompat.startActivityForResult(mContext, cls, requestCode);
    }

    @Override
    public <T extends Activity> IContext startActivity(Class<T> cls, Bundle bundle) {
        return IIntentCompat.startActivity(mContext, cls, bundle);
    }

    @Override
    public <T extends Activity> IContext startActivity(Class<T> cls) {
        return IIntentCompat.startActivity(mContext, cls);
    }

    @Override
    public final Bundle getBundle() {
        return IIntentCompat.getBundle(mContext);
    }

    @Override
    public final <T> T showText(CharSequence text) {
        return IToastCompat.showText(getActivity(), text);
    }

    @Override
    public final <T> T showText(@StringRes int res) {
        return IToastCompat.showText(getActivity(), res);
    }

    @Override
    public final <T> T showText(@StringRes int res, Object... formatArgs) {
        return IToastCompat.showText(getActivity(), res, formatArgs);
    }

    @Override
    public IOSDialog showTextDialog(String title, String message, IOSDialog.OnClickListener l) {
        return IActivityCompat.showTextDialog(getActivity(), title, message, l);
    }

    @Override
    public final void i(Object msg) {
        ILogCompat.i(TAG, msg);
    }

    @Override
    public final void d(Object msg) {
        ILogCompat.d(TAG, msg);
    }

    @Override
    public final void e(Object msg) {
        ILogCompat.e(TAG, msg);
    }

    @Override
    public final void v(Object msg) {
        ILogCompat.v(TAG, msg);
    }

    @Override
    public final void w(Object msg) {
        ILogCompat.w(TAG, msg);
    }

    @Override
    public final IContext showExitText() {
        IActivityCompat.buildExit().showExitText(getActivity());
        return mContext;
    }

    @Override
    public final IContext showExitText(final Handler.Callback callback) {
        IActivityCompat.buildExit().showExitText(getActivity(), callback);
        return mContext;
    }

    @Override
    public final void hideKeyboard() {
        IActivityCompat.hideKeyboard(getActivity());
    }

    @Override
    public final void showKeyboard() {
        IActivityCompat.showKeyboard(getActivity());
    }

    @SuppressWarnings("MissingPermission")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    @Override
    public final void startActivityForPicture(List<String> datas, int maxCount) {
        IImageChooser.startActivityForPicture(mContext, datas, maxCount);
    }

    @SuppressWarnings("MissingPermission")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    @Override
    public final void startActivityForPicture(IOpenType.Type type, List<String> totalImages, List<String> selectImages, int selectIndex, int maxSelectCount) {
        IImageChooser.startActivityForPicture(mContext, type, totalImages, selectImages, selectIndex, maxSelectCount);
    }

    @Override
    public final void setWebViewCommonAttribute(WebView webView) {
        IWebViewChooseFile.getInstance(mContext).setWebViewCommonAttribute(webView);
    }

    @Override
    public <T extends View> T findViewById(@IdRes int id) {
        if (mContext instanceof Fragment) {
            View view = ((Fragment) mContext).getView();
            if (view != null) {
                return view.findViewById(id);
            }
        }
        return null;
    }

    @Override
    public void exitApplication() {
        IActivityCompat.exitApplication(getActivity());
    }

    @Override
    public Application getApplication() {
        Activity activity = getActivity();
        if (activity == null) {
            return null;
        }
        return activity.getApplication();
    }

    @Override
    public Context getApplicationContext() {
        Activity activity = getActivity();
        if (activity == null) {
            return null;
        }
        return activity.getApplicationContext();
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        Activity activity = getActivity();
        if (activity == null) {
            return null;
        }
        return activity.getApplicationInfo();
    }

    @Override
    public IFragmentHelper getFragmentHelper() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null && mFragmentHelper == null) {
            mFragmentHelper = new IFragmentHelper(fragmentManager);
        }
        return mFragmentHelper;
    }

    @Override
    public void setOnActivityResultCallback(OnActivityResultCallback callback) {
        mActivityResultCallback = callback;
    }

    @Override
    public void setOnRequestPermissionsResultCallback(OnRequestPermissionsResultCallback callback) {
        mRequestPermissionsResultCallback = callback;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IActivityCompat.onActivityResult(mContext, requestCode, resultCode, data);
        if (mActivityResultCallback != null) {
            mActivityResultCallback.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mRequestPermissionsResultCallback != null) {
            mRequestPermissionsResultCallback.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private ViewGroup getFrameViewGroup() {
        return findViewById(android.R.id.content);
    }

    @Nullable
    private Activity getActivity() {
        Activity activity = mContext.getActivity();
        if (activity == null) {
            return null;
        }
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null && fragmentManager.isDestroyed()) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            return null;
        }
        return activity;
    }

    @Nullable
    private FragmentManager getFragmentManager() {
        return mContext.getSupportFragmentManager();
    }

    private <T extends View> T inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot) {
        //noinspection unchecked
        return (T) LayoutInflater.from(getActivity()).inflate(resource, root, attachToRoot);
    }
}
