/*
 * Copyright © All right reserved by CHANGLEI.
 */

package net.izhuo.app.library.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.izhuo.app.library.IContext;
import net.izhuo.app.library.common.IConstants.IRequestCode;

/**
 * Created by Box on 16/8/10.
 * <p/>
 * webview选择文件
 */
public final class IWebViewChooseFile {

    private static IWebViewChooseFile instance = new IWebViewChooseFile();

    private ValueCallback<Uri> mUploadMessage;
    private IContext mIContext;

    private IWebViewChooseFile() {
    }

    public static IWebViewChooseFile getInstance(IContext activity) {
        instance.mIContext = activity;
        return instance;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IRequestCode.REQUEST_CHOOSE_FILE) {
            if (mUploadMessage == null || resultCode != Activity.RESULT_OK) {
                return;
            }
            Uri result = data == null ? null : data.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void setWebViewCommonAttribute(WebView webView) {
        Activity activity = mIContext.getActivity();
        if (activity == null) {
            return;
        }
        String dir = activity.getDir("database", Context.MODE_PRIVATE).getPath();
        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true); // 设置支持JS
        settings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口

        settings.setGeolocationEnabled(true); // 支持定位
        settings.setDatabaseEnabled(true); // 启用数据库
        settings.setGeolocationDatabasePath(dir); // 设置数据库路径
        settings.setDomStorageEnabled(true); // 使用localStorage则必须打开

        // 自适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        settings.setAllowFileAccess(true); // 允许访问文件

        webView.setFocusableInTouchMode(true); // 如果想让网页的输入框可输入，则必须设置true
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            // Android > 4.1.1 调用这个方法
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg, acceptType);
            }

            // 3.0 + 调用这个方法
            @SuppressWarnings("UnusedParameters")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                chooseFile(uploadMsg);
            }

            // Android < 3.0 调用这个方法
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(uploadMsg, "");
            }

        });
    }

    private void chooseFile(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        IAppUtils.chooseFiles(mIContext);
    }

}
