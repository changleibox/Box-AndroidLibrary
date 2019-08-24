/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.reader.picture;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.box.app.library.IContext;
import net.box.app.library.IAppCompatActivity;
import net.box.app.library.R;
import net.box.app.library.common.IConstants.IRequestCode;
import net.box.app.library.reader.activity.IChoosePictureActivity;
import net.box.app.library.reader.adapter.ICommonAdapter;
import net.box.app.library.util.IPermissionCompat;
import net.box.app.library.util.IAppUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class IMyAdapter extends ICommonAdapter<String> {

    public static final int TRANSLUCENCE = 0x77000000;
    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    private List<String> mSelectedImages = new LinkedList<>();

    /**
     * 文件夹路径
     */
    private String mDirPath;
    private Callback mCallback;
    private int mMaxSelectCount = Integer.MAX_VALUE;
    private LayoutParams mParams;
    private IAppCompatActivity mActivity;
    private int itemWidth;

    public IMyAdapter(Context context, List<String> datas, int itemLayoutId, String dirPath) {
        super(context, datas, itemLayoutId);
        this.mDirPath = dirPath;
        mActivity = (IChoosePictureActivity) context;
        itemWidth = (mActivity.getWidth() - mActivity.getResources()
                .getDimensionPixelOffset(R.dimen.box_gv_space) * 4) / 3;
        mParams = new LayoutParams(itemWidth, itemWidth);
    }

    @Override
    public void convert(final int position, final IViewHolder helper, final String item) {
        View convertView = helper.getConvertView();
        convertView.setLayoutParams(mParams);
        View contentView = helper.getView(R.id.box_rl_content);
        View opearView = helper.getView(R.id.box_ll_opearView);
        opearView.setBackgroundResource(R.drawable.box_selector_back);
        if (item == null || item.equals(IChoosePictureActivity.CAMERA)) {
            contentView.setVisibility(View.GONE);
            opearView.setVisibility(View.VISIBLE);
            opearView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!IAppUtils.isExistSD()) {
                        mActivity.showText(R.string.box_lable_no_sdcard);
                        return;
                    }
                    if (IPermissionCompat.checkSelfPermission((IContext) mActivity, IRequestCode.REQUEST_PERMISSION_CAMERA, Manifest.permission.CAMERA)) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mActivity.startActivityForResult(intent, IRequestCode.REQUSET_CAMERA);
                    }
                }
            });
        } else {
            contentView.setVisibility(View.VISIBLE);
            opearView.setVisibility(View.GONE);
            // 设置no_selected
            helper.setImageResource(R.id.box_id_item_select, R.drawable.box_img_picture_unselected);
            // 设置图片
            // helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);
            final ImageView imageView = helper.getView(R.id.box_id_item_image);
            final ImageView select = helper.getView(R.id.box_id_item_select);

            //noinspection SuspiciousNameCombination
            Picasso.get().load(new File(mDirPath + "/" + item)).resize(itemWidth, itemWidth).centerCrop().into(imageView);

            // mActivity.mImageLoader.displayImage(Uri.fromFile(
            // new File(mDirPath + "/" + item)).toString(), imageView,
            // mActivity.mOptions);

            // Picasso.with(mContext).load(new File(mDirPath + "/" +
            // item)).into(imageView);

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> oldDatas = getDatas();
                    List<String> newDatas = new ArrayList<>();
                    for (String string : oldDatas) {
                        if (string.equals(IChoosePictureActivity.CAMERA)) {
                            continue;
                        }
                        newDatas.add(mDirPath + "/" + string);
                    }
                    if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mActivity.startActivityForPicture(IOpenType.Type.EDIT,
                            newDatas, getSelectedImages(), position - 1, getMaxSelectCount());
                }
            });
            // 设置ImageView的点击事件
            select.setOnClickListener(new OnClickListener() {
                // 选择，则将图片变暗，反之则反之
                @Override
                public void onClick(View v) {
                    String path = mDirPath + "/" + item;
                    // 已经选择过该图片
                    if (getSelectedImages().contains(path)) {
                        getSelectedImages().remove(path);
                        select.setImageResource(R.drawable.box_img_picture_unselected);
                        imageView.setColorFilter(null);
                    } else {
                        if (getMaxSelectCount() == getSelectedImages().size()) {
                            mActivity.showText(mActivity.getString(
                                    R.string.box_toast_max_select_picture,
                                    getMaxSelectCount()));
                        } else {
                            getSelectedImages().add(path);
                            select.setImageResource(R.drawable.box_img_pictures_selected);
                            imageView.setColorFilter(TRANSLUCENCE);
                        }
                    }
                    if (mCallback != null) {
                        mCallback.onCallback(path, !getSelectedImages().contains(path));
                    }
                    // notifyDataSetChanged();
                }
            });

            /**
             * 已经选择过的图片，显示出选择过的效果
             */
            if (getSelectedImages().contains(mDirPath + "/" + item)) {
                select.setImageResource(R.drawable.box_img_pictures_selected);
                imageView.setColorFilter(TRANSLUCENCE);
            } else {
                select.setImageResource(R.drawable.box_img_picture_unselected);
                imageView.setColorFilter(null);
            }
        }
    }

    /**
     * @return the mSelectedImage
     */
    public List<String> getSelectedImages() {
        return mSelectedImages;
    }

    /**
     * @param selectedImages the mSelectedImage to set
     */
    public void setSelectedImages(List<String> selectedImages) {
        this.mSelectedImages = selectedImages;
    }

    public interface Callback {
        void onCallback(String path, boolean isSelect);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    /**
     * @return the mMaxSelectCount
     */
    public int getMaxSelectCount() {
        return mMaxSelectCount;
    }

    /**
     * @param maxSelectCount the mMaxSelectCount to set
     */
    public void setMaxSelectCount(int maxSelectCount) {
        this.mMaxSelectCount = maxSelectCount;
    }

}
