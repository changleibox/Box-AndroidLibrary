/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.request;

import android.content.Context;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import net.box.app.library.R;
import net.box.app.library.common.IConstants.IHttpError;
import net.box.app.library.common.IConstants.IKey;
import net.box.app.library.helper.IAppHelper;
import net.box.app.library.util.IJsonDecoder;
import net.box.app.library.util.ILogCompat;
import net.box.app.library.util.IToastCompat;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by box on 2014/7/29.
 * <p>
 * 访问网络数据
 */
@SuppressWarnings({"unused", "deprecation", "WeakerAccess"})
public class IHttpRequest<T> {

    private static final String CONTENT_TYPE = "application/json;charset=utf-8";
    private static final String DEF_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String KEY_CONTENT_TYPE = "Content-Type";
    private static final int TIMEOUT_STATUS_CODE = 408;
    private static final int DEF_TIME_OUT_RESPONSE = 15 * 1000;
    private static final int DEF_TIME_OUT_CONNECT = 10 * 1000;
    private static final int DEF_TIME_OUT = 15 * 1000;
    private static final String TAG = "API";

    private static final int CODE_OK = 200;

    private static AsyncHttpClient httpClient = new AsyncHttpClient();

    private ResponseHandler mResponseHandler = new ResponseHandler();
    private OnJudgeListener mJudgeListener;
    private CommonCallback<T> mCommonCallback;
    private SpecialCallback mSpecialCallback;
    private ToastCallback mToastCallback;

    private static ToastCallback mStaticToastCallback;

    private Context mContext;
    private boolean isShowToast = true;
    private boolean isSelfResolution;

    private String mContentType = CONTENT_TYPE;
    private String mPattern = DEF_DATE_PATTERN;

    @Nullable
    private RequestConfig mConfig;

    private static final RequestConfig mStaticConfig = new RequestConfig();

    public IHttpRequest(Context context) {
        this(context, DEF_TIME_OUT_CONNECT, DEF_TIME_OUT_RESPONSE);
    }

    public IHttpRequest(Context context, RequestConfig config) {
        this(context);
        this.setRequestConfig(config);
    }

    public IHttpRequest(Context context, int timeout) {
        this(context, timeout, timeout);
    }

    public IHttpRequest(Context context, int connectTimeout, int responseTimeout) {
        this.mContext = context;
        this.addHeader(KEY_CONTENT_TYPE, mContentType);

        this.setConnectTimeout(connectTimeout);
        this.setResponseTimeout(responseTimeout);

        OnJudgeListener judgeListener = new OnJudgeListener() {
            @Override
            public boolean onJudge(JSONObject object) {
                return isRequestSuccess(object);
            }
        };
        this.setOnJudgeListener(judgeListener);
    }

    public static <T> IHttpRequest<T> newAsyncInstance(Context context, int timeout) {
        httpClient = new AsyncHttpClient();
        IHttpRequest<T> httpRequest = new IHttpRequest<>(context, timeout);
        httpRequest.setUseSynchronousMode(false);
        return httpRequest;
    }

    public static <T> IHttpRequest<T> newAsyncInstance(Context context) {
        return newAsyncInstance(context, DEF_TIME_OUT);
    }

    public static <T> IHttpRequest<T> newSyncInstance(Context context, int timeout) {
        httpClient = new SyncHttpClient();
        IHttpRequest<T> httpRequest = new IHttpRequest<>(context, timeout);
        httpRequest.setUseSynchronousMode(true);
        return httpRequest;
    }

    public static <T> IHttpRequest<T> newSyncInstance(Context context) {
        return newSyncInstance(context, DEF_TIME_OUT);
    }

    public void setRequestConfig(RequestConfig config) {
        this.mConfig = config;
    }

    public static void setStaticRequestConfig(RequestConfig config) {
        String url = null;
        String result = null;
        String content = null;
        String code = null;
        int successCode = CODE_OK;
        if (config != null) {
            Map<String, String> keyMap = config.mKeyMap;
            url = keyMap.get(IKey.URL);
            result = keyMap.get(IKey.RESULT);
            content = keyMap.get(IKey.CONTENT);
            code = keyMap.get(IKey.CODE);
            successCode = config.mSuccessCode;
        }
        mStaticConfig.putCallbackKeyNames(url, result, content, code);
        mStaticConfig.setSuccessCode(successCode);
    }

    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        httpClient.setSSLSocketFactory(sslSocketFactory);
    }

    public void setTag(Object TAG) {
        mResponseHandler.setTag(TAG);
    }

    public void setTimeout(int value) {
        httpClient.setTimeout(value);
    }

    public void setConnectTimeout(int value) {
        httpClient.setConnectTimeout(value);
    }

    public void setResponseTimeout(int value) {
        httpClient.setResponseTimeout(value);
    }

    public void addHeader(String header, String value) {
        httpClient.addHeader(header, value);
    }

    public void removeHeader(String header) {
        httpClient.removeHeader(header);
    }

    public void removeAllHeaders() {
        httpClient.removeAllHeaders();
    }

    /**
     * 根据TAG，取消请求
     */
    public void cancelRequestsByTAG(Object TAG, boolean mayInterruptIfRunning) {
        httpClient.cancelRequestsByTAG(TAG, mayInterruptIfRunning);
    }

    /**
     * 根据TAG，取消请求
     */
    public void cancelRequestsByTAG(Object TAG) {
        cancelRequestsByTAG(TAG, false);
    }

    /**
     * 取消请求
     */
    public void cancelRequests(Context context, boolean mayInterruptIfRunning) {
        try {
            httpClient.cancelRequests(context, mayInterruptIfRunning);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消所有请求
     */
    public void cancelAllRequests(boolean mayInterruptIfRunning) {
        try {
            httpClient.cancelAllRequests(mayInterruptIfRunning);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消请求
     */
    public void cancelRequests(Context context) {
        cancelRequests(context, true);
    }

    /**
     * 取消所有请求
     */
    public void cancelAllRequests() {
        cancelAllRequests(true);
    }

    public void setContentType(String contentType) {
        this.mContentType = contentType;
        this.addHeader(KEY_CONTENT_TYPE, mContentType);
    }

    public void setPattern(String pattern) {
        this.mPattern = pattern;
    }

    public HttpEntity getHttpEntity(Object params) throws UnsupportedEncodingException {
        String json = IJsonDecoder.objectToJson(params);
        return getHttpEntity(json);
    }

    public HttpEntity getHttpEntity(JSONObject object) throws UnsupportedEncodingException {
        String json = object.toString();
        return getHttpEntity(json);
    }

    public HttpEntity getHttpEntity(String content) throws UnsupportedEncodingException {
        d("请求参数：" + content);
        StringEntity entity = new StringEntity(content, "utf-8");
        entity.setContentType(mContentType);
        return entity;
    }

    /**
     * 默认返回类型String
     */
    public void put(String action) {
        request(RequestType.PUT, action, new RequestParams(), String.class);
    }

    /**
     * 默认返回类型String
     */
    public void post(String action) {
        request(RequestType.POST, action, new RequestParams(), String.class);
    }

    /**
     * 默认返回类型String
     */
    public void delete(String action) {
        request(RequestType.DELETE, action, new RequestParams(), String.class);
    }

    public void put(String action, final Type type) {
        request(RequestType.PUT, action, new RequestParams(), type);
    }

    public void post(String action, final Type type) {
        request(RequestType.POST, action, new RequestParams(), type);
    }

    public void get(String action, final Type type) {
        request(RequestType.GET, action, new RequestParams(), type);
    }

    public void delete(String action, final Type type) {
        request(RequestType.DELETE, action, new RequestParams(), type);
    }

    public void put(String action, RequestParams params, final Type type) {
        request(RequestType.PUT, action, params, type);
    }

    public void post(String action, RequestParams params, final Type type) {
        request(RequestType.POST, action, params, type);
    }

    public void get(String action, RequestParams params, final Type type) {
        request(RequestType.GET, action, params, type);
    }

    public void delete(String action, RequestParams params, final Type type) {
        request(RequestType.DELETE, action, params, type);
    }

    public void put(String action, Map<String, ?> params, final Type type) {
        request(RequestType.PUT, action, params, type);
    }

    public void post(String action, Map<String, ?> params, final Type type) {
        request(RequestType.POST, action, params, type);
    }

    public void get(String action, Map<String, ?> params, final Type type) {
        request(RequestType.GET, action, params, type);
    }

    public void delete(String action, Map<String, ?> params, final Type type) {
        request(RequestType.DELETE, action, params, type);
    }

    public void put(String action, Object params, final Type type) {
        try {
            put(action, getHttpEntity(params), type);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void post(String action, Object params, final Type type) {
        try {
            post(action, getHttpEntity(params), type);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void get(String action, Object params, final Type type) {
        try {
            get(action, getHttpEntity(params), type);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void delete(String action, Object params, final Type type) {
        try {
            delete(action, getHttpEntity(params), type);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void put(String action, JSONObject params, final Type type) {
        try {
            put(action, getHttpEntity(params), type);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void post(String action, JSONObject params, final Type type) {
        try {
            post(action, getHttpEntity(params), type);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void get(String action, JSONObject params, final Type type) {
        try {
            get(action, getHttpEntity(params), type);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void delete(String action, JSONObject params, final Type type) {
        try {
            delete(action, getHttpEntity(params), type);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void put(String action, String params, final Type type) {
        try {
            put(action, getHttpEntity(params), type);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void post(String action, String params, final Type type) {
        try {
            post(action, getHttpEntity(params), type);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void get(String action, String params, final Type type) {
        try {
            get(action, getHttpEntity(params), type);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void delete(String action, String params, final Type type) {
        try {
            delete(action, getHttpEntity(params), type);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void put(String action, HttpEntity entity, Type type) {
        put(action, entity, mContentType, type);
    }

    public void post(String action, HttpEntity entity, Type type) {
        post(action, entity, mContentType, type);
    }

    public void get(String action, HttpEntity entity, Type type) {
        get(action, entity, mContentType, type);
    }

    public void delete(String action, HttpEntity entity, Type type) {
        delete(action, entity, mContentType, type);
    }

    public void put(String action, HttpEntity entity, String contentType, Type type) {
        request(RequestType.PUT, action, entity, contentType, type);
    }

    public void post(String action, HttpEntity entity, String contentType, Type type) {
        request(RequestType.POST, action, entity, contentType, type);
    }

    public void get(String action, HttpEntity entity, String contentType, Type type) {
        request(RequestType.GET, action, entity, contentType, type);
    }

    public void delete(String action, HttpEntity entity, String contentType, Type type) {
        request(RequestType.DELETE, action, entity, contentType, type);
    }

    public void setOnJudgeListener(OnJudgeListener listener) {
        this.mJudgeListener = listener;
    }

    public void setCommonCallback(CommonCallback<T> callback) {
        this.mCommonCallback = callback;
    }

    public void setSpecialCallback(SpecialCallback callback) {
        this.mSpecialCallback = callback;
    }

    public void setToastCallback(ToastCallback callback) {
        this.mToastCallback = callback;
    }

    public static void setStaticToastCallback(ToastCallback callback) {
        mStaticToastCallback = callback;
    }

    public ToastCallback getToastCallback() {
        return mToastCallback;
    }

    public static ToastCallback getStaticToastCallback() {
        return mStaticToastCallback;
    }

    public void setUseSynchronousMode(boolean sync) {
        mResponseHandler.setUseSynchronousMode(sync);
    }

    /**
     * 设为false，将不再有任何提示，默认为true
     *
     * @param isShowToast true为提示，否则不提示，默认true
     */
    public void setShowToast(boolean isShowToast) {
        this.isShowToast = isShowToast;
    }

    /**
     * 设为true的话，程序将不再解析返回数据，而是直接回调，默认为false
     *
     * @param isSelfResolution 默认false
     */
    public void setSelfResolution(boolean isSelfResolution) {
        this.isSelfResolution = isSelfResolution;
    }

    public static AsyncHttpClient getHttpClient() {
        return httpClient;
    }

    protected void showText(final int errorCode, int resId) {
        showText(errorCode, getString(resId));
    }

    protected void showText(final int errorCode, int res, Object... object) {
        showText(errorCode, getString(res, object));
    }

    protected void showText(final int errorCode, final String text) {
        IAppHelper.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean toastCallback = true;
                if (mStaticToastCallback != null) {
                    toastCallback = !mStaticToastCallback.showToast(errorCode);
                    d("StaticToastCallback.showToast" + " " + toastCallback + " " + errorCode);
                }
                if (mToastCallback != null) {
                    toastCallback = !mToastCallback.showToast(errorCode);
                    d("ToastCallback.showToast" + " " + toastCallback + " " + errorCode);
                }
                if (toastCallback && isShowToast && !TextUtils.isEmpty(text) && mContext != null) {
                    IToastCompat.showText(mContext, text);
                }
            }
        });
    }

    protected String getString(int resId, Object... obj) {
        if (mContext != null) {
            return mContext.getString(resId, obj);
        }
        return null;
    }

    protected String getString(int resId) {
        if (mContext != null) {
            return mContext.getString(resId);
        }
        return null;
    }

    protected void i(String msg) {
        ILogCompat.i(TAG, msg);
    }

    protected void d(String msg) {
        ILogCompat.d(TAG, msg);
    }

    protected void e(String msg) {
        ILogCompat.e(TAG, msg);
    }

    private String getHttpClientUrl(String action) {
        RequestConfig config = getRequestConfig();
        if (config == null) {
            throw new NullPointerException("请设置RequestConfig。");
        }
        String url = config.mKeyMap.get(IKey.URL);
        if (TextUtils.isEmpty(url) && (TextUtils.isEmpty(action)
                || (!action.startsWith(IKey.HTTP_HEAD) && !action.startsWith(IKey.HTTPS_HEAD)))) {
            throw new NullPointerException("请在程序启动的时候设置接口的服务器地址！");
        }
        if (!TextUtils.isEmpty(action)) {
            if (action.startsWith(IKey.HTTP_HEAD) || action.startsWith(IKey.HTTPS_HEAD)) {
                url = action;
            } else {
                url = (url.endsWith("/") ? url : url + "/") + action;
            }
        }
        d("url：" + url);
        return url;
    }

    @MainThread
    private void requestOnUiThread(RequestType requestType, String action, HttpEntity entity, String contentType, Type type) {
        setContentType(contentType);
        d("请求方式：" + requestType.name());
        mResponseHandler.setType(type);
        String httpClientUrl = getHttpClientUrl(action);
        switch (requestType) {
            case POST:
                httpClient.post(mContext, httpClientUrl, entity, contentType, mResponseHandler);
                break;
            case PUT:
                httpClient.put(mContext, httpClientUrl, entity, contentType, mResponseHandler);
                break;
            case GET:
                httpClient.get(mContext, httpClientUrl, entity, contentType, mResponseHandler);
                break;
            case DELETE:
                httpClient.delete(mContext, httpClientUrl, entity, contentType, mResponseHandler);
                break;
        }
    }

    private void request(final RequestType requestType, final String action, final HttpEntity entity, final String contentType, final Type type) {
        IAppHelper.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                request(requestType, action, entity, contentType, type);
            }
        });
    }

    @MainThread
    private void requestOnUiThread(RequestType requestMode, String action, RequestParams params, final Type type) {
        d("请求参数：" + params.toString());
        d("请求方式：" + requestMode.name());
        mResponseHandler.setType(type);
        String httpClientUrl = getHttpClientUrl(action);
        switch (requestMode) {
            case GET:
                httpClient.get(httpClientUrl, params, mResponseHandler);
                break;
            case POST:
                httpClient.post(httpClientUrl, params, mResponseHandler);
                break;
            case PUT:
                httpClient.put(httpClientUrl, params, mResponseHandler);
                break;
            case DELETE:
                httpClient.delete(mContext, httpClientUrl, null, params, mResponseHandler);
                break;
        }
    }

    private void request(final RequestType requestMode, final String action, final RequestParams params, final Type type) {
        IAppHelper.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                requestOnUiThread(requestMode, action, params, type);
            }
        });
    }

    private void request(RequestType requestMode, String action, Map<String, ?> params, final Type type) {
        RequestParams requestParams = getRequestParams(action, params);
        request(requestMode, action, requestParams, type);
    }

    private RequestParams getRequestParams(String action, Map<String, ?> params) {
        RequestParams requestParams = new RequestParams();
        if (params != null) {
            for (String key : params.keySet()) {
                Object object = params.get(key);
                if (object != null) {
                    requestParams.put(key, object);
                }
            }
        }
        return requestParams;
    }

    private void requestCallback(String response) {
        int error = IHttpError.CODE_SERVER_ERROR;
        if (response != null) {
            try {
                //noinspection unchecked
                success((T) response);
            } catch (Exception e) {
                e.printStackTrace();
                showText(error, R.string.box_toast_net_exception);
                failure(error, e.getMessage());
            }
        } else {
            d(IHttpError.NO_RESPONSE);
            failure(error, IHttpError.NO_RESPONSE);
        }
    }

    /**
     * 请求成功回调
     *
     * @param response 返回的数据
     * @param type     需要解析成的类型
     */
    private void requestCallback(String response, Type type) {
        int error = IHttpError.CODE_SERVER_ERROR;
        if (TextUtils.isEmpty(response)) {
            d(IHttpError.NO_RESPONSE);
            failure(error, IHttpError.NO_RESPONSE);
            return;
        }
        RequestConfig config = getRequestConfig();
        if (config == null) {
            throw new NullPointerException("请设置RequestConfig。");
        }
        Map<String, String> keyMap = config.mKeyMap;
        try {
            JsonElement jsonData = new JsonParser().parse(response);
            if (!jsonData.isJsonObject()) {
                failure(error, IHttpError.NO_RESPONSE);
                return;
            }
            JSONObject object = new JSONObject(response);
            if (mJudgeListener != null && mJudgeListener.onJudge(object)) {
                if (!keyMap.containsKey(IKey.CONTENT)) {
                    throw new NullPointerException("请设置返回内容的keyName，请调用RequestConfig.putContentKey(String content)");
                }
                String result = object.getString(keyMap.get(IKey.CONTENT));
                T t;
                if (type.equals(String.class)) {
                    //noinspection unchecked
                    t = (T) result;
                } else {
                    t = IJsonDecoder.jsonToObject(result, type, mPattern);
                }
                if (t != null) {
                    success(t);
                } else {
                    failure(error, IHttpError.get(error));
                    showText(error, R.string.box_toast_unknown_exception);
                }
            } else {
                if (!keyMap.containsKey(IKey.CODE)) {
                    throw new NullPointerException("请设置错误码的keyName，请调用RequestConfig.putErrorCodeKey(String code)");
                }
                int code = object.getInt(keyMap.get(IKey.CODE));
                String message = IHttpError.get(code);
                if (message != null) {
                    showText(code, message);
                } else {
                    showText(code, R.string.box_toast_unknown_exception);
                }
                d("code：" + code);
                failure(code, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showText(error, R.string.box_toast_net_exception);
            failure(error, e.getMessage());
        }
    }

    private void success(int statusCode, Header[] headers, String responseString, Type type) {
        for (Header header : headers) {
            d(String.valueOf(header));
        }
        if (TextUtils.isEmpty(responseString)) {
            failure(statusCode, getString(R.string.box_toast_net_exception));
            showText(statusCode, R.string.box_toast_net_exception);
            return;
        }
        d("response：" + responseString);
        d("type：" + type);
        try {
            if (!responseString.contains(IKey.ERROR)) {
                if (isSelfResolution) {
                    if (type.equals(String.class)) {
                        requestCallback(responseString);
                    } else {
                        throw new IllegalStateException("在需要自己解析的时候，type请传String.class");
                    }
                } else {
                    requestCallback(responseString, type);
                }
            } else {
                JSONObject jsonObject = new JSONObject(responseString);
                String errorMessage = jsonObject.getString(IKey.ERROR);
                failure(statusCode, errorMessage);
                showText(statusCode, errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showText(statusCode, R.string.box_toast_net_exception);
            failure(statusCode, IHttpError.JSON_ERROR);
        }
    }

    private void failure(int statusCode, Header[] headers, String errorResponse, Throwable error) {
        if (headers != null) {
            for (Header header : headers) {
                d(String.valueOf(header));
            }
        }
        d("statusCode:" + statusCode);
        if (statusCode == 0 || statusCode == TIMEOUT_STATUS_CODE) {
            failure(statusCode, getString(R.string.box_toast_net_connect_timeout));
            showText(statusCode, R.string.box_toast_net_connect_timeout);
            return;
        }
        if (errorResponse == null || error == null) {
            failure(statusCode, getString(R.string.box_toast_net_exception));
            showText(statusCode, R.string.box_toast_net_exception);
            return;
        }
        d("error:" + error.getLocalizedMessage());
        String message = error.getMessage();
        d("message:" + message);
        d("errorResponse:" + errorResponse);
        try {
            JsonElement jsonData = new JsonParser().parse(errorResponse);
            if (!jsonData.isJsonObject() && !jsonData.isJsonArray()) {
                failure(statusCode, errorResponse);
                showText(statusCode, errorResponse);
                return;
            }
            JSONObject jsonObject = new JSONObject(errorResponse);
            String errorMessage = jsonObject.getString(IKey.ERROR);
            failure(statusCode, errorMessage);
            showText(statusCode, errorMessage);
        } catch (Exception e) {
            e.printStackTrace();
            showText(statusCode, R.string.box_toast_net_exception);
            failure(statusCode, message);
        }
    }

    private class ResponseHandler extends TextHttpResponseHandler {

        private Type type;

        @Override
        public void onStart() {
            super.onStart();
            start();
        }

        @Override
        public void onCancel() {
            super.onCancel();
            cancel();
        }

        @Override
        public void onFinish() {
            super.onFinish();
            finish();
        }

        @Override
        public void onProgress(long bytesWritten, long totalSize) {
            super.onProgress(bytesWritten, totalSize);
            progress(bytesWritten, totalSize);
        }

        @Override
        public void onRetry(int retryNo) {
            super.onRetry(retryNo);
            retry(retryNo);
        }

        @Override
        public void onSuccess(final int statusCode, final Header[] headers, final String responseString) {
            if (type == null) {
                throw new NullPointerException("Please set type!");
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    success(statusCode, headers, responseString, type);
                }
            }).start();
        }

        @Override
        public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    failure(statusCode, headers, responseString, throwable);
                }
            }).start();
        }

        public void setType(Type type) {
            this.type = type;
        }
    }

    private void success(final T t) {
        IAppHelper.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCommonCallback != null) {
                    mCommonCallback.onRequestSuccess(t);
                }
            }
        });
    }

    private void failure(final int errorCode, final String errorMessage) {
        IAppHelper.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCommonCallback != null) {
                    mCommonCallback.onRequestFailure(errorCode, errorMessage);
                }
            }
        });
    }

    private void retry(int retryNo) {
        if (mSpecialCallback != null) {
            mSpecialCallback.onRetry(retryNo);
        }
    }

    private void start() {
        if (mSpecialCallback != null) {
            mSpecialCallback.onStart();
        }
    }

    private void cancel() {
        if (mSpecialCallback != null) {
            mSpecialCallback.onCancel();
        }
    }

    private void finish() {
        if (mSpecialCallback != null) {
            mSpecialCallback.onFinish();
        }
    }

    private void progress(final long bytesWritten, final long totalSize, final long count) {
        IAppHelper.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSpecialCallback != null) {
                    mSpecialCallback.onProgress(bytesWritten, totalSize, count);
                }
            }
        });
    }

    private void progress(final long bytesWritten, final long totalSize) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                // 上传进度显示
                d(bytesWritten + " / " + totalSize);
                progress(bytesWritten, totalSize, count);
            }
        }).start();
    }

    private RequestConfig getRequestConfig() {
        if (mConfig == null) {
            mConfig = mStaticConfig;
        }
        return mConfig;
    }

    private boolean isRequestSuccess(JSONObject object) {
        RequestConfig config = getRequestConfig();
        if (config == null) {
            throw new NullPointerException("请设置RequestConfig。");
        }
        final Map<String, String> keyMap = config.mKeyMap;
        String resultKey = keyMap.containsKey(IKey.RESULT) ? keyMap.get(IKey.RESULT) : IKey.RESULT;
        String codeKey = keyMap.containsKey(IKey.CODE) ? keyMap.get(IKey.CODE) : IKey.CODE;
        try {
            return object.has(resultKey) ? object.getBoolean(resultKey) : object.getInt(codeKey) == config.mSuccessCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public interface OnJudgeListener {
        /**
         * 自己判断是否成功
         */
        boolean onJudge(JSONObject object);
    }

    public interface CommonCallback<T> {

        /**
         * 访问成功后回调此方法
         *
         * @param arg0 json解析后得到的对象
         */
        void onRequestSuccess(T arg0);

        /**
         * 访问失败后回调此方法
         *
         * @param errorCode    错误码
         * @param errorMessage 错误信息
         */
        void onRequestFailure(int errorCode, String errorMessage);

    }

    public interface SpecialCallback {

        void onStart();

        void onCancel();

        void onFinish();

        void onRetry(int retryNo);

        void onProgress(long bytesWritten, long totalSize, long progress);

    }

    public interface ToastCallback {

        boolean showToast(int errorCode);

    }

    public static abstract class CommonCallbackAdapter<T> implements CommonCallback<T> {

        @Override
        public void onRequestSuccess(T arg0) {
        }

        @Override
        public void onRequestFailure(int errorCode, String errorMessage) {
        }

    }

    public static abstract class SpecialCallbackAdapter implements SpecialCallback {

        @Override
        public void onStart() {
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onFinish() {
        }

        @Override
        public void onRetry(int retryNo) {
        }

        @Override
        public void onProgress(long bytesWritten, long totalSize, long progress) {
        }

    }

    public enum RequestType {
        GET, PUT, POST, DELETE
    }

    public static final class RequestConfig {

        private final Map<String, String> mKeyMap = new HashMap<>();
        private int mSuccessCode = CODE_OK;

        public RequestConfig() {
        }

        public RequestConfig(RequestConfig config) {
            Map<String, String> keyMap = config.mKeyMap;
            putCallbackKeyNames(keyMap.get(IKey.URL), keyMap.get(IKey.RESULT), keyMap.get(IKey.CONTENT), keyMap.get(IKey.CODE));
            setSuccessCode(config.mSuccessCode);
        }

        public RequestConfig(String url, String result, String content, String code) {
            putCallbackKeyNames(url, result, content, code);
        }

        public RequestConfig setSuccessCode(int code) {
            this.mSuccessCode = code;
            return this;
        }

        public RequestConfig putServerUrl(String url) {
            this.mKeyMap.put(IKey.URL, url);
            return this;
        }

        public RequestConfig putResultKeyName(String result) {
            this.mKeyMap.put(IKey.RESULT, result);
            return this;
        }

        public RequestConfig putContentKeyName(String content) {
            this.mKeyMap.put(IKey.CONTENT, content);
            return this;
        }

        public RequestConfig putErrorCodeKeyName(String code) {
            this.mKeyMap.put(IKey.CODE, code);
            return this;
        }

        /**
         * 此方法最好在程序启动时就调用，否则会报错
         */
        public RequestConfig putCallbackKeyNames(String url, String content, String code) {
            putCallbackKeyNames(url, null, content, code);
            return this;
        }

        /**
         * 此方法最好在程序启动时就调用，否则会报错
         */
        public RequestConfig putCallbackKeyNames(String url, String result, String content, String code) {
            putServerUrl(url);
            putResultKeyName(result);
            putContentKeyName(content);
            putErrorCodeKeyName(code);
            return this;
        }

    }

}
