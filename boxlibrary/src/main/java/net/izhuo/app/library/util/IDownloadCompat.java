/*
 * Copyright © All right reserved by CHANGLEI.
 */

package net.izhuo.app.library.util;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import net.izhuo.app.library.api.IHttpRequest.CommonCallback;
import net.izhuo.app.library.api.IHttpRequest.SpecialCallback;

import org.apache.http.Header;

import java.io.File;

/**
 * Created by Box on 16/9/19.
 * <p>
 * 文件下载
 */
@SuppressWarnings("deprecation")
public class IDownloadCompat {

    public static String getFilePath(@NonNull String url, @NonNull String dirPath) {
        return dirPath + url.substring(url.lastIndexOf("/"));
    }

    public static File getLocalFile(@NonNull String url, @NonNull String dirPath) {
        File file = new File(getFilePath(url, dirPath));
        if (file.exists()) {
            return file;
        }
        return null;
    }

    @RequiresPermission(value = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public static void download(@NonNull String url, @NonNull String dirPath, final CommonCallback<File> callback, final SpecialCallback specialCallbackAdapter) {
        File temporaryFile = new File(getFilePath(url, dirPath));
        if (temporaryFile.exists()) {
            if (callback != null) {
                callback.onRequestSuccess(temporaryFile);
            }
            return;
        }
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(url, new FileAsyncHttpResponseHandler(temporaryFile) {
            @Override
            public void onFailure(int i, Header[] headers, Throwable throwable, File file) {
                if (file != null) {
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();
                }
                if (callback != null) {
                    callback.onRequestFailure(i, throwable.getMessage());
                }
            }

            @Override
            public void onSuccess(int i, Header[] headers, File file) {
                if (callback != null) {
                    callback.onRequestSuccess(file);
                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                if (specialCallbackAdapter != null) {
                    int count = (int) ((double) bytesWritten * 1.0D / (double) totalSize * 100.0D);
                    specialCallbackAdapter.onProgress(bytesWritten, totalSize, count);
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                if (specialCallbackAdapter != null) {
                    specialCallbackAdapter.onStart();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (specialCallbackAdapter != null) {
                    specialCallbackAdapter.onFinish();
                }
            }

            @Override
            public void onCancel() {
                super.onCancel();
                if (specialCallbackAdapter != null) {
                    specialCallbackAdapter.onCancel();
                }
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
                if (specialCallbackAdapter != null) {
                    specialCallbackAdapter.onRetry(retryNo);
                }
            }
        });
    }

}
