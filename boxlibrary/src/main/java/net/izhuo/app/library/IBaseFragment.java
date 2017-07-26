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

import net.izhuo.app.library.helper.IFragmentHelper;
import net.izhuo.app.library.reader.picture.IOpenType;
import net.izhuo.app.library.util.IActivityCompat;
import net.izhuo.app.library.util.IImageChooser;
import net.izhuo.app.library.util.IIntentCompat;
import net.izhuo.app.library.util.IWebViewChooseFile;
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

    private OnRequestPermissionsResultCallback mRequestPermissionsResultCallback;
    private OnActivityResultCallback mActivityResultCallback;
    private IBaseActivity mActivity;
    private Fragment mFragment;
    private boolean isHidden;
    private int mPage;
    private IFragmentHelper mFragmentHelper;

    @CallSuper
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof IBaseActivity) {
            mActivity = (IBaseActivity) getActivity();
        } else {
            throw new ClassCastException("承载Fragment的activity必须继承自" + mActivity.getClass().getSimpleName() + "或者其子类！");
        }
    }

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boolean isContinue = onSetContentViewBefore(savedInstanceState);
        if (!isContinue) {
            Object containerLayout = getContainerLayout();
            if (containerLayout != null) {
                if (containerLayout instanceof Integer) {
                    return inflater.inflate((Integer) containerLayout, container, false);
                } else if (containerLayout instanceof View) {
                    return (View) containerLayout;
                }
            }
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
        try {
            IActivityCompat.autoInjectAllField(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        onUserCreateViews(savedInstanceState);

        initViews(savedInstanceState);
        initDatas(savedInstanceState);
        initListeners(savedInstanceState);
    }

    @Override
    public final void onUserCreateViews(Bundle savedInstanceState) {
    }

    @Override
    public Context getContext() {
        return mActivity;
    }

    @Override
    public Application getApplication() {
        if (mActivity != null) {
            return mActivity.getApplication();
        }
        return null;
    }

    @Override
    public Context getApplicationContext() {
        if (mActivity != null) {
            return mActivity.getApplicationContext();
        }
        return null;
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        if (mActivity != null) {
            return mActivity.getApplicationInfo();
        }
        return null;
    }

    @Override
    public IFragmentHelper getFragmentHelper() {
        if (mFragmentHelper == null) {
            mFragmentHelper = new IFragmentHelper(getFragmentManager());
        }
        return null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.isHidden = hidden;
        if (!hidden) {
            onRefreshUI();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden) {
            onRefreshUI();
        }
    }

    public boolean onBackPressed() {
        if (mActivity == null) {
            return true;
        }
        FragmentManager manager = getSupportFragmentManager();
        int count = manager.getBackStackEntryCount();
        if (count > 0) {
            manager.popBackStack();
        }
        return count == 0;
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
    public final View findViewById(int id) {
        View view = getView();
        if (view != null) {
            return view.findViewById(id);
        }
        return null;
    }

    @Override
    public void finish() {
        if (mActivity != null) {
            mActivity.finish();
        }
    }

    @Override
    public final FragmentManager getSupportFragmentManager() {
        if (mActivity != null) {
            return mActivity.getSupportFragmentManager();
        }
        return getFragmentManager();
    }

    @Override
    public final DisplayImageOptions getOptions(int radius, int loadingImage, int emptyUriImage, int failImage) {
        if (mActivity != null) {
            return mActivity.getOptions(radius, loadingImage, emptyUriImage, failImage);
        }
        return null;
    }

    @Override
    public final DisplayImageOptions getOptions(int radius, int defImage) {
        if (mActivity != null) {
            return mActivity.getOptions(radius, defImage);
        }
        return null;
    }

    @Override
    public final String getText(TextView textView) {
        if (mActivity != null) {
            return mActivity.getText(textView);
        }
        return null;
    }

    @Override
    public IProgress showLoad(CharSequence message) {
        if (mActivity != null) {
            return mActivity.showLoad(message);
        }
        return null;
    }

    @Override
    public IProgress showLoad(int messageResId) {
        if (mActivity != null) {
            return mActivity.showLoad(messageResId);
        }
        return null;
    }

    @Override
    public IProgress showLoad() {
        if (mActivity != null) {
            return mActivity.showLoad();
        }
        return null;
    }

    @Override
    public IProgress showLoad(IProgress.Theme theme, CharSequence message) {
        if (mActivity != null) {
            return mActivity.showLoad(theme, message);
        }
        return null;
    }

    @Override
    public IProgress showLoad(IProgress.Theme theme, int messageResId) {
        if (mActivity != null) {
            return mActivity.showLoad(theme, messageResId);
        }
        return null;
    }

    @Override
    public IProgress showLoad(IProgress.Theme theme) {
        if (mActivity != null) {
            return mActivity.showLoad(theme);
        }
        return null;
    }

    @Override
    public void loadDismiss() {
        if (mActivity != null) {
            mActivity.loadDismiss();
        }
    }

    @Override
    public IContext intentForResult(Intent intent, Bundle bundle, int requestCode) {
        return IIntentCompat.intentForResult((IContext) this, intent, bundle, requestCode);
    }

    @Override
    public IContext intentForResult(Intent intent, int requestCode) {
        return IIntentCompat.intentForResult((IContext) this, intent, requestCode);
    }

    @Override
    public final IContext intentForResult(Class<?> cls, int requestCode) {
        return IIntentCompat.intentForResult((IContext) this, cls, requestCode);
    }

    @Override
    public final IContext intentForResult(Class<?> cls, Bundle bundle, int requestCode) {
        return IIntentCompat.intentForResult((IContext) this, cls, bundle, requestCode);
    }

    @Override
    public IContext intent(Intent intent, Bundle bundle) {
        return IIntentCompat.intent(this, intent, bundle);
    }

    @Override
    public IContext intent(Intent intent) {
        return IIntentCompat.intent(this, intent);
    }

    @Override
    public final IContext intent(Class<?> cls, Bundle bundle) {
        return IIntentCompat.intent(this, cls, bundle);
    }

    @Override
    public final IContext intent(Class<?> cls) {
        return IIntentCompat.intent(this, cls);
    }

    @Override
    public final Bundle getBundle() {
        return IIntentCompat.getBundle(this);
    }

    @Override
    public final Object showText(CharSequence text) {
        if (mActivity != null) {
            return mActivity.showText(text);
        }
        return null;
    }

    @Override
    public final Object showText(int res) {
        if (mActivity != null) {
            return mActivity.showText(res);
        }
        return null;
    }

    @Override
    public final Object showText(int res, Object... formatArgs) {
        if (mActivity != null) {
            return mActivity.showText(res, formatArgs);
        }
        return null;
    }

    @Override
    public final IOSDialog showTextDialog(String title, String message, IOSDialog.OnClickListener l) {
        if (mActivity != null) {
            return mActivity.showTextDialog(title, message, l);
        }
        return null;
    }

    @Override
    public final void i(Object msg) {
        if (mActivity != null) {
            mActivity.i(msg);
        }
    }

    @Override
    public final void d(Object msg) {
        if (mActivity != null) {
            mActivity.d(msg);
        }
    }

    @Override
    public final void e(Object msg) {
        if (mActivity != null) {
            mActivity.e(msg);
        }
    }

    @Override
    public final void v(Object msg) {
        if (mActivity != null) {
            mActivity.v(msg);
        }
    }

    @Override
    public final void w(Object msg) {
        if (mActivity != null) {
            mActivity.w(msg);
        }
    }

    @Override
    public final void exitApplication() {
        if (mActivity != null) {
            mActivity.exitApplication();
        }
    }

    @Override
    public final IContext showExitText() {
        if (mActivity != null) {
            return mActivity.showExitText();
        }
        return null;
    }

    @Override
    public final IContext showExitText(final Callback callback) {
        if (mActivity != null) {
            return mActivity.showExitText(callback);
        }
        return null;
    }

    @Override
    public final void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    @Override
    public final void showKeyboard() {
        if (mActivity != null) {
            mActivity.showKeyboard();
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
