/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.reader.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;

import net.box.app.library.IAppCompatActivity;
import net.box.app.library.R;
import net.box.app.library.common.IConstants.IKey;
import net.box.app.library.common.IConstants.IRequestCode;
import net.box.app.library.reader.entity.IImageFloder;
import net.box.app.library.reader.picture.IListImageDirPopupWindow;
import net.box.app.library.reader.picture.IListImageDirPopupWindow.OnImageDirSelected;
import net.box.app.library.reader.picture.IMyAdapter;
import net.box.app.library.reader.picture.IOpenType;
import net.box.app.library.util.IFileSizeUtil;
import net.box.app.library.util.IJsonDecoder;
import net.box.app.library.util.IPermissionCompat;
import net.box.app.library.util.IAppUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 选择图片是我从网上下载，有可能有些Bug
 *
 * @author Box
 * @version v1.0
 * @see IPreviewPictureActivity
 * @since Jdk1.6 或 Jdk1.7
 * <p>
 * 2015年2月13日
 * <p>
 * 最后修改日期：2015年8月25日
 * <p>
 * 修改人：Box
 * <p>
 * 复审人：Box
 */
public final class IChoosePictureActivity extends IAppCompatActivity implements
        OnImageDirSelected, OnClickListener, IMyAdapter.Callback {

    public static final String CAMERA = "CAMERA";
    public static final int REFRESH_VIEW = 0x110;
    public static final String JPG = ".jpg";
    public static final String PNG = ".png";
    public static final String JPEG = ".jpeg";

    public static int MAX_COUNT = 9;
    public static String FILE_NAME;
    public static String XIAO_QU_DIR;

    /**
     * 所有的图片
     */
    private List<String> mImgs = new ArrayList<>();
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<>();
    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<IImageFloder> mImageFloders = new ArrayList<>();
    private List<String> mSelectImages = new ArrayList<>();
    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;

    private GridView mGirdView;
    private IMyAdapter mAdapter;
    private RelativeLayout mBottomLy;

    private Button mChooseDir;
    private Button mBtnPreview;
    @SuppressWarnings("unused")
    private int mTotalCount;
    private int mScreenHeight;
    private IListImageDirPopupWindow mListImageDirPopupWindow;
    private Button mBtnComplete;

    @Override
    public boolean onSetContentViewBefore(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        return super.onSetContentViewBefore(savedInstanceState);
    }

    @Override
    public Object getContainerLayout() {
        return R.layout.box_activity_choose_picture;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mGirdView = (GridView) findViewById(R.id.box_id_gridView);
        mChooseDir = (Button) findViewById(R.id.box_id_choose_dir);
        mBtnPreview = (Button) findViewById(R.id.box_btn_preview);

        mBottomLy = (RelativeLayout) findViewById(R.id.box_id_bottom_ly);
        mBtnComplete = (Button) findViewById(R.id.box_btn_complete);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;

        FILE_NAME = IAppUtils.getSDPath() + "/" + getPackageName() + "/"
                + getPackageName() + "_" + System.currentTimeMillis() + ".png";

        XIAO_QU_DIR = IAppUtils.getSDPath() + "/" + getPackageName() + "/";
    }

    @SuppressWarnings("deprecation")
    @Override
    public void initDatas(Bundle savedInstanceState) {
        mBtnComplete.setText(getString(R.string.box_btn_complete));
        mBtnComplete.setEnabled(false);
        mBtnPreview.setEnabled(false);
        String data = getIntentData();
        if (TextUtils.isEmpty(data)) {
            mSelectImages = new ArrayList<>();
        } else {
            mSelectImages = IJsonDecoder.jsonToObject(getIntentData(), new TypeToken<List<String>>() {
            }.getType());
        }
        if (getIntentType() > -1) {
            MAX_COUNT = getIntentType();
        }
        refreshBtnComplete(mSelectImages.size());
        getImagesBySDCard();
    }

    @Override
    public void initListeners(Bundle savedInstanceState) {
        mChooseDir.setOnClickListener(this);
        mBtnComplete.setOnClickListener(this);
        mBtnPreview.setOnClickListener(this);
    }

    /**
     * 初始化展示文件夹的popupWindw
     */
    @SuppressLint("InflateParams")
    private void initListDirPopupWindw() {
        mListImageDirPopupWindow = new IListImageDirPopupWindow(LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
                mImageFloders, LayoutInflater.from(getApplicationContext()).inflate(R.layout.box_view_pop_list_dir, null));

        mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImagesBySDCard() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            showText(R.string.box_lable_no_sdcard);
            return;
        }
        // showLoad(context);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String firstImage = null;
                Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = IChoosePictureActivity.this.getContentResolver();
                Cursor cursor = contentResolver
                        .query(imageUri, null,
                                MediaStore.Images.Media.MIME_TYPE + "=? or "
                                        + MediaStore.Images.Media.MIME_TYPE
                                        + "=? or "
                                        + MediaStore.Images.Media.MIME_TYPE
                                        + "=?", new String[]{"image/jpeg",
                                        "image/png", "image/jpg"},
                                MediaStore.Images.Media.DATE_ADDED + " DESC");
                if (cursor != null) {
                    e(cursor.getCount() + "");
                }
                while (cursor != null && cursor.moveToNext()) {
                    // 获取图片的路径
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    w(path);
                    File file = new File(path);
                    if (TextUtils.isEmpty(path) || !file.exists() || !file.canRead()
                            || IFileSizeUtil.getFileOrFilesSize(path, IFileSizeUtil.SIZETYPE_KB) < 1) {
                        continue;
                    }
                    // 拿到第一张图片的路径
                    if (firstImage == null) {
                        firstImage = path;
                    }
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null) {
                        continue;
                    }
                    String dirPath = parentFile.getAbsolutePath();
                    IImageFloder imageFloder;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new IImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            return filename.endsWith(JPG) || filename.endsWith(PNG) || filename.endsWith(JPEG);
                        }
                    }).length;
                    mTotalCount += picSize;
                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);
                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;
                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(REFRESH_VIEW);
            }
        }).start();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_VIEW:
                    // 为View绑定数据
                    bindDataToView();
                    // 初始化展示文件夹的popupWindw
                    initListDirPopupWindw();
                    break;
            }
            return false;
        }
    });

    /**
     * 为View绑定数据
     */
    private void bindDataToView() {
        if (mImgDir == null) {
            showText(R.string.box_lable_no_picture);
            return;
        }

        mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(JPG) || filename.endsWith(PNG) || filename.endsWith(JPEG);
            }
        }));
        List<String> paths = new ArrayList<>();
        for (String path : mImgs) {
            if (paths.contains(path)) {
                continue;
            }
            paths.add(path);
        }
        paths.add(0, CAMERA);
        /*
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new IMyAdapter(this, paths, R.layout.box_item_gride, mImgDir.getAbsolutePath());
        mAdapter.setSelectedImages(mSelectImages);
        mAdapter.setCallback(this);
        mAdapter.setMaxSelectCount(MAX_COUNT);
        mGirdView.setAdapter(mAdapter);
        // mBtnPreview.setText(getString(R.string.lable_spread, mTotalCount));
        // mBtnComplete.setText(getString(R.string.btn_complete));
    }

    @Override
    public void selected(IImageFloder floder) {
        mImgDir = new File(floder.getDir());
        mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(JPG) || filename.endsWith(PNG) || filename.endsWith(JPEG);
            }
        }));
        List<String> paths = new ArrayList<>();
        for (String path : mImgs) {
            paths.add(path);
        }
        paths.add(0, CAMERA);
        /*
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new IMyAdapter(this, paths, R.layout.box_item_gride, mImgDir.getAbsolutePath());
        mAdapter.setSelectedImages(mSelectImages);
        mAdapter.setMaxSelectCount(MAX_COUNT);
        mAdapter.setCallback(this);
        mGirdView.setAdapter(mAdapter);
        // mAdapter.notifyDataSetChanged();
        mBtnPreview.setText(getString(R.string.box_lable_spread, floder.getCount()));
        mChooseDir.setText(floder.getName());
        mListImageDirPopupWindow.dismiss();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void back(View v) {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.box_anim_menushow, R.anim.box_anim_activity_hide);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.box_btn_complete) {
            List<String> images = mAdapter.getSelectedImages();
            if (images == null) {
                images = new ArrayList<>();
            }
            Intent intent = new Intent();
            intent.putExtra(IKey.PICTURE, new ArrayList<>(images));
            setResult(RESULT_OK, intent);
            finish();
        } else if (v.getId() == R.id.box_id_choose_dir) {
            mListImageDirPopupWindow.setAnimationStyle(R.style.Box_Anim_Popupwindow_Dir);
            mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

            // 设置背景颜色变暗
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = .3f;
            getWindow().setAttributes(lp);
        } else if (v.getId() == R.id.box_btn_preview) {
            List<String> newDatas = mAdapter.getSelectedImages();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivityForPicture(IOpenType.Type.EDIT, newDatas, newDatas, 0, MAX_COUNT);
        }
    }

    @Override
    public void onCallback(String path, boolean isSelect) {
        List<String> selectedImages = mAdapter.getSelectedImages();
        int size = selectedImages.size();
        refreshBtnComplete(size);
    }

    private void refreshBtnComplete(int size) {
        if (size == 0) {
            mBtnPreview.setEnabled(false);
            mBtnComplete.setEnabled(false);
            mBtnComplete.setText(getString(R.string.box_btn_complete));
            mBtnPreview.setText(getString(R.string.box_btn_preview));
        } else {
            mBtnPreview.setEnabled(true);
            mBtnComplete.setEnabled(true);
            mBtnComplete.setText(getString(
                    R.string.box_lable_complete_spread, size, MAX_COUNT));
            mBtnPreview.setText(getString(R.string.box_lable_preview, size));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (data == null) {
            return;
        }
        if (requestCode == IRequestCode.REQUSET_LOOK) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data);
                finish();
            } else {
                ArrayList<String> extra = data.getStringArrayListExtra(IKey.PICTURE);
                List<String> images = new ArrayList<>(extra);
                if (mAdapter != null) {
                    mAdapter.setSelectedImages(images);
                    mAdapter.notifyDataSetChanged();
                }
                refreshBtnComplete(images.size());
            }
        } else if (requestCode == IRequestCode.REQUSET_CAMERA) {
            if (resultCode == RESULT_OK) {
                String sdStatus = Environment.getExternalStorageState();
                // 检测sd是否可用
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                    v("SD card is not avaiable/writeable right now.");
                    return;
                }

                new Thread(new Runnable() {
                    public void run() {
                        List<String> newDatas = new ArrayList<>();
                        newDatas.addAll(mSelectImages);
                        newDatas.add(getAbsoluteImagePath(data.getData()));
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivityForPicture(IOpenType.Type.EXAMINE, newDatas, newDatas, 0, MAX_COUNT);
                    }
                }).start();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取拍照所得图片的绝对路径
     */
    protected String getAbsoluteImagePath(Uri uri) {
        if (uri == null) {
            return null;
        }
        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        if (cursor == null) {
            return null;
        }

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.gc();
    }

    @Override
    public void onUserCreateViews(Bundle savedInstanceState) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IRequestCode.REQUEST_PERMISSION_CAMERA) {
            if (IPermissionCompat.hasSelfPermission(grantResults)) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, IRequestCode.REQUSET_CAMERA);
            } else {
                showText(R.string.box_toast_not_permission);
            }
        }
    }
}
