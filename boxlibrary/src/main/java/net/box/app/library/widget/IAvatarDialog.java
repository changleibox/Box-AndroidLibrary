/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import net.box.app.library.IBaseContext.OnActivityResultCallback;
import net.box.app.library.IContext;
import net.box.app.library.R;
import net.box.app.library.common.IConstants.IRequestCode;
import net.box.app.library.util.IAppUtils;
import net.box.app.library.util.IPermissionCompat;
import net.box.app.library.util.IToastCompat;

import java.io.File;

/**
 * Created by Changlei
 * <p>
 * 2014年11月7日
 * <p>
 * 选择头像对话框
 */
@SuppressWarnings("unused")
public class IAvatarDialog extends AppCompatDialog implements OnClickListener, OnRequestPermissionsResultCallback, OnActivityResultCallback {

    public final static int REQUEST_CODE_CAMERA = 1111; // 拍照
    public final static int REQUEST_CODE_PICTURE = 2222; // 在相册中查找
    public final static int REQUEST_CODE_CROP = 3333; // 截图
    private final static int CROP = 80;
    private OnSaveListener mSaveListener;
    private OnStateListener mStateListener;
    private IContext mIContext;

    private File mSdcardTempFile;

    public IAvatarDialog(IContext context) {
        super(context.getContext(), R.style.Box_Dialog_Bottom_Menu);
        context.setOnRequestPermissionsResultCallback(this);
        context.setOnActivityResultCallback(this);

        mIContext = context;

        View contentView = View.inflate(context.getContext(), R.layout.box_dialog_avatar, null);
        setContentView(contentView);

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);

        Button btnCamera = (Button) contentView.findViewById(R.id.box_btn_camera);
        btnCamera.setOnClickListener(this);

        Button btnPhoto = (Button) contentView.findViewById(R.id.box_btn_photo);
        btnPhoto.setOnClickListener(this);

        final Button btnCancel = (Button) contentView.findViewById(R.id.box_btn_cancel);
        btnCancel.setOnClickListener(this);
    }

    public void setOnSaveListener(OnSaveListener saveListener) {
        this.mSaveListener = saveListener;
    }

    public void setOnStateListener(OnStateListener stateListener) {
        this.mStateListener = stateListener;
    }

    /**
     * 截图
     */
    private void startPhotoZoom(Uri uri) {
        try {
            if (mStateListener != null) {
                boolean startZoom = mStateListener.onStartZoom(uri);
                if (startZoom) {
                    return;
                }
            }
            Intent intentPic = new Intent("com.android.camera.action.CROP");
            intentPic.setDataAndType(uri, "image/*");
            intentPic.putExtra("crop", "true");
            intentPic.putExtra("aspectX", 1);
            intentPic.putExtra("aspectY", 1);
            intentPic.putExtra("outputX", CROP);
            intentPic.putExtra("outputY", CROP);
            intentPic.putExtra("return-data", true);
            intentPic.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intentPic.putExtra("noFaceDetection", false);
            intentPic.putExtra("scale", true);
            intentPic.putExtra("output", Uri.fromFile(mSdcardTempFile));
            mIContext.startActivityForResult(intentPic, REQUEST_CODE_CROP);
        } catch (Exception ex) {
            if (mStateListener != null) {
                mStateListener.onGetCallback();
            }
            showToast(R.string.box_lable_no_sdcard);
            ex.printStackTrace();
        }
    }

    public void deleteFile() {
        try {
            if (mSdcardTempFile != null && mSdcardTempFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                mSdcardTempFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCropResult(int resultCode, Intent data) {
        getStartPhotoZoom(REQUEST_CODE_CROP, resultCode, data);
    }

    public interface OnSaveListener {
        void onSave(String path);
    }

    public interface OnStateListener {
        void onStartGet();

        boolean onStartZoom(Uri uri);

        void onGetCallback();
    }

    /**
     * 获取到返回的图片路径
     */
    private void setPicToView(Intent picdata) {
        if (mStateListener != null) {
            mStateListener.onGetCallback();
        }
        try {
            Bundle extras = picdata.getExtras();
            if (extras != null) {
                if (mSaveListener != null) {
                    mSaveListener.onSave(mSdcardTempFile.getAbsolutePath());
                }
            } else {
                showToast(R.string.box_lable_no_sdcard);
            }
        } catch (Exception ex) {
            showToast(R.string.box_lable_no_sdcard);
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (!IAppUtils.isExistSD()) {
            showToast(R.string.box_lable_no_sdcard);
            return;
        }
        try {
            String avatarName = mIContext.getContext().getPackageName() + "_" + System.currentTimeMillis() + ".jpg";
            mSdcardTempFile = new File(IAppUtils.getSDPath(), avatarName);

            int id = v.getId();
            if (id == R.id.box_btn_camera) {
                if (!IPermissionCompat.checkSelfPermission(mIContext, IRequestCode.REQUEST_PERMISSION_CAMERA,
                        Manifest.permission.CAMERA)) {
                    return;
                }
                IAppUtils.openCamera(mIContext.getActivity(), REQUEST_CODE_CAMERA, false, mSdcardTempFile);
            } else if (id == R.id.box_btn_photo) {
                if (!IPermissionCompat.checkSelfPermission(mIContext, IRequestCode.REQUEST_PERMISSION_SDCARD,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    return;
                }
                IAppUtils.openPicture(mIContext.getActivity(), REQUEST_CODE_PICTURE);
            }

            if (mStateListener != null) {
                mStateListener.onStartGet();
            }
            dismiss();
        } catch (Exception ex) {
            showToast(R.string.box_lable_no_sdcard);
            ex.printStackTrace();
        }
    }

    /**
     * 显示提示
     *
     * @param resId 要显示文本的资源ID
     */
    public void showToast(int resId) {
        IToastCompat.showText(getContext(), resId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getStartPhotoZoom(requestCode, resultCode, data);
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != IRequestCode.REQUEST_PERMISSION_SDCARD && requestCode != IRequestCode.REQUEST_PERMISSION_CAMERA) {
            return;
        }
        if (!IPermissionCompat.hasSelfPermission(grantResults)) {
            mIContext.showText(R.string.box_toast_not_permission);
            return;
        }
        if (requestCode == IRequestCode.REQUEST_PERMISSION_CAMERA) {
            IAppUtils.openCamera(mIContext.getActivity(), REQUEST_CODE_CAMERA, false, mSdcardTempFile);
        } else {
            IAppUtils.openPicture(mIContext.getActivity(), REQUEST_CODE_PICTURE);
        }
    }

    /**
     * 在activity的onActivityForResult方法中调用
     */
    private void getStartPhotoZoom(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode != Activity.RESULT_OK) {
                if (mStateListener != null) {
                    mStateListener.onGetCallback();
                }
                return;
            }
            switch (requestCode) {
                case REQUEST_CODE_CAMERA:
                    if (mSdcardTempFile == null || !mSdcardTempFile.exists()) {
                        if (mStateListener != null) {
                            mStateListener.onGetCallback();
                        }
                        showToast(R.string.box_lable_no_sdcard);
                        return;
                    }
                    IAppUtils.setPictureDegreeZero(mSdcardTempFile.getAbsolutePath());
                    startPhotoZoom(Uri.fromFile(mSdcardTempFile));
                    break;
                case REQUEST_CODE_PICTURE:
                    if (data != null) {
                        startPhotoZoom(data.getData());
                    } else {
                        if (mStateListener != null) {
                            mStateListener.onGetCallback();
                        }
                        showToast(R.string.box_lable_no_sdcard);
                    }
                    break;
                case REQUEST_CODE_CROP:
                    if (data != null) {
                        setPicToView(data);
                    } else {
                        if (mStateListener != null) {
                            mStateListener.onGetCallback();
                        }
                        showToast(R.string.box_lable_no_sdcard);
                    }
                    break;
            }
        } catch (Exception ex) {
            if (mStateListener != null) {
                mStateListener.onGetCallback();
            }
            showToast(R.string.box_lable_no_sdcard);
            ex.printStackTrace();
        }
    }
}
