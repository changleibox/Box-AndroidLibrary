/*
 * Copyright © All right reserved by CHANGLEI.
 */

package net.izhuo.app.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import net.izhuo.app.library.IContext;
import net.izhuo.app.library.IBaseActivity;
import net.izhuo.app.library.R;
import net.izhuo.app.library.common.IConstants;
import net.izhuo.app.library.common.IConstants.IActivityCaches;
import net.izhuo.app.library.view.IOSDialog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Box on 16/8/10.
 * <p>
 * Activity辅助类
 */
public final class IActivityCompat {

    private static InputMethodManager mMethodManager;
    private static int mScreenWidth, mScreenHeight;
    private static int mScreenOrientation;

    public static void autoInjectAllField(IContext context) throws IllegalAccessException, IllegalArgumentException {
        // 得到Activity对应的Class
        Class<?> clazz = context.getClass();
        // 得到该Activity的所有字段
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            int resId;
            // 判断字段是否标注InjectView
            if (field.isAnnotationPresent(IViewInject.class) && (resId = field.getAnnotation(IViewInject.class).value()) > 0) {
                // 反射访问私有成员，必须加上这句
                field.setAccessible(true);
                // 然后对这个属性复制
                field.set(context, context.findViewById(resId));
            }
        }
    }

    public static void hideKeyboard(Activity context) {
        View currentFocus = context.getCurrentFocus();
        IBinder iBinder = currentFocus == null ? null : currentFocus.getApplicationWindowToken();
        if (iBinder != null) {
            getMethodManager(context).hideSoftInputFromWindow(iBinder, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void showKeyboard(Activity context) {
        View currentFocus = context.getCurrentFocus();
        if (currentFocus != null) {
            getMethodManager(context).showSoftInput(currentFocus, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void exitApplication(Activity self) {
        Collection<Activity> activities = IActivityCaches.getActivities();
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        if (self != null) {
            self.finish();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static ExitApplication buildExit() {
        return ExitApplication.getInstance();
    }

    public static IOSDialog showTextDialog(Context context, String title, String message, IOSDialog.OnClickListener l) {
        IOSDialog iosDialog = new IOSDialog(context);
        iosDialog.setTitle(title);
        iosDialog.setMessage(message);
        iosDialog.setNegativeButton(R.string.box_btn_sure, l);
        iosDialog.show();
        return iosDialog;
    }

    public static void onActivityResult(IContext iContext, int requestCode, int resultCode, Intent data) {
        Context context = iContext.getContext();
        if (context instanceof Activity) {
            IWebViewChooseFile.getInstance(iContext).onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case IConstants.IRequestCode.REQUSET_PICTURE:
                    iContext.onImageChooseCallback(new ArrayList<>(data.getStringArrayListExtra(IConstants.IKey.PICTURE)));
                    break;
                case IConstants.IRequestCode.REQUEST_CHOOSE_FILE:
                    Uri uri = data.getData();
                    String path = IFileUtils.GetPathFromUri4kitkat.getPath(context, uri);
                    iContext.d("上传文件选择结果：" + uri + "\nFilePath：" + path);
                    iContext.onFileChooseCallback(path);
                    break;
            }
        }
    }

    public static int getWidth(Context context) {
        int screenOrientation = getScreenOrientation(context);
        if (mScreenWidth == 0 || screenOrientation != mScreenOrientation) {
            initSrceenWidthAndHeight(context);
            mScreenOrientation = screenOrientation;
        }
        return mScreenWidth;
    }

    public static int getHeight(Context context) {
        int screenOrientation = getScreenOrientation(context);
        if (mScreenHeight == 0 || screenOrientation != mScreenOrientation) {
            initSrceenWidthAndHeight(context);
            mScreenOrientation = screenOrientation;
        }
        return mScreenHeight;
    }

    public static int getScreenOrientation(Context context) {
        return context.getResources().getConfiguration().orientation;
    }

    @SuppressWarnings("deprecation")
    private static void initSrceenWidthAndHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            mScreenWidth = size.x;
            mScreenHeight = size.y;
        } else {
            mScreenWidth = display.getWidth();
            mScreenHeight = display.getHeight();
        }
    }

    private static InputMethodManager getMethodManager(Context context) {
        if (mMethodManager == null) {
            mMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        return mMethodManager;
    }

    public static class ExitApplication {

        private static final long COUNT_DOWN_INTERVAL = 1000;
        private static final long MILLIS_LN_FUTURE = 2000;

        private static ExitApplication instance = new ExitApplication();

        private int mTime;

        private ExitApplication() {
        }

        public static ExitApplication getInstance() {
            return instance;
        }

        public final IBaseActivity showExitText(IBaseActivity baseActivity) {
            return showExitText(baseActivity, null);
        }

        public final IBaseActivity showExitText(IBaseActivity iContext, final Handler.Callback callback) {
            if (mTime == 0) {
                mTime++;
                iContext.showText(R.string.box_toast_exit_prompt);
                mCountDownTimer.start();
            } else {
                if (callback == null) {
                    exitApplication(iContext);
                } else {
                    callback.handleMessage(new Message());
                }
            }
            return iContext;
        }

        private CountDownTimer mCountDownTimer = new CountDownTimer(MILLIS_LN_FUTURE, COUNT_DOWN_INTERVAL) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                mTime = 0;
            }

        };

    }

}
