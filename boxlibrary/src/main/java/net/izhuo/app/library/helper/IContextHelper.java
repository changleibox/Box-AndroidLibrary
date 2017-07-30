/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.helper;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import net.izhuo.app.library.IBaseContext;
import net.izhuo.app.library.IContext;
import net.izhuo.app.library.reader.picture.IOpenType;
import net.izhuo.app.library.widget.IOSDialog;
import net.izhuo.app.library.widget.IProgress;

import java.util.List;

/**
 * Created by Box on 2017/7/30.
 * <p>
 * baseContext辅助类
 */

public class IContextHelper implements IBaseContext {

    private IContext mContext;

    public IContextHelper(IContext context) {
        this.mContext = context;
    }

    @Override
    public DisplayImageOptions getOptions(int radius, int loadingImage, int emptyUriImage, int failImage) {
        return null;
    }

    @Override
    public DisplayImageOptions getOptions(int radius, int defImage) {
        return null;
    }

    @Override
    public String getText(TextView textView) {
        return null;
    }

    @Override
    public IProgress showLoad(CharSequence message) {
        return null;
    }

    @Override
    public IProgress showLoad(int messageResId) {
        return null;
    }

    @Override
    public IProgress showLoad() {
        return null;
    }

    @Override
    public IProgress showLoad(IProgress.Theme theme, CharSequence message) {
        return null;
    }

    @Override
    public IProgress showLoad(IProgress.Theme theme, int messageResId) {
        return null;
    }

    @Override
    public IProgress showLoad(IProgress.Theme theme) {
        return null;
    }

    @Override
    public void loadDismiss() {

    }

    @Override
    public IContext startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        return null;
    }

    @Override
    public IContext startActivityForResult(Class<?> cls, int requestCode) {
        return null;
    }

    @Override
    public IContext startActivity(Class<?> cls, Bundle bundle) {
        return null;
    }

    @Override
    public IContext startActivity(Class<?> cls) {
        return null;
    }

    @Override
    public Bundle getBundle() {
        return null;
    }

    @Override
    public Object showText(CharSequence text) {
        return null;
    }

    @Override
    public Object showText(int res) {
        return null;
    }

    @Override
    public Object showText(int res, Object... formatArgs) {
        return null;
    }

    @Override
    public IOSDialog showTextDialog(String title, String message, IOSDialog.OnClickListener l) {
        return null;
    }

    @Override
    public void i(Object msg) {

    }

    @Override
    public void d(Object msg) {

    }

    @Override
    public void e(Object msg) {

    }

    @Override
    public void v(Object msg) {

    }

    @Override
    public void w(Object msg) {

    }

    @Override
    public IContext showExitText() {
        return null;
    }

    @Override
    public IContext showExitText(Handler.Callback callback) {
        return null;
    }

    @Override
    public void hideKeyboard() {

    }

    @Override
    public void showKeyboard() {

    }

    @Override
    public void intentForPicture(List<String> datas, @IntRange(from = 1) int maxCount) {

    }

    @Override
    public void intentForPicture(IOpenType.Type type, List<String> totalImages, List<String> selectImages, int selectIndex, int maxSelectCount) {

    }

    @Override
    public void setWebViewCommonAttribute(WebView webView) {

    }

    @Override
    public View findViewById(int id) {
        return null;
    }

    @Override
    public void exitApplication() {

    }

    @Override
    public Application getApplication() {
        return null;
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return null;
    }

    @Override
    public IFragmentHelper getFragmentHelper() {
        return null;
    }

    @Override
    public void setOnActivityResultCallback(OnActivityResultCallback callback) {

    }

    @Override
    public void setOnRequestPermissionsResultCallback(ActivityCompat.OnRequestPermissionsResultCallback callback) {

    }
}
