/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.reader.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.izhuo.app.library.IBaseActivity;
import net.izhuo.app.library.R;
import net.izhuo.app.library.common.IConstants;
import net.izhuo.app.library.common.IConstants.IKey;
import net.izhuo.app.library.reader.adapter.IVPAdapter;
import net.izhuo.app.library.reader.picture.IOpenType;
import net.izhuo.app.library.util.IJsonDecoder;
import net.izhuo.app.library.util.IViewDrawable;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Changlei
 *         <p>
 *         2015年2月14日
 */
public final class IPreviewPictureActivity extends IBaseActivity implements OnPageChangeListener, OnClickListener {

    public static final int VIEW_PAGER_MARGIN = 20;

    private ViewPager mViewPager;
    private IVPAdapter mVPAdapter;
    private TextView mTvCount;
    private List<String> mSelectImages = new ArrayList<>();
    private List<String> mImages = new ArrayList<>();
    private Button mBtnSelect;
    private Drawable mUnSelectDrawable;
    private Drawable mSelectDrawable;
    private int mMaxSelectCount = Integer.MAX_VALUE;

    @Override
    public boolean onSetContentViewBefore(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        return super.onSetContentViewBefore(savedInstanceState);
    }

    @Override
    public Object getContainerLayout() {
        return R.layout.box_activity_preview_picture;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mViewPager = (ViewPager) findViewById(R.id.box_vp_picture);
        mVPAdapter = new IVPAdapter(mContext);
        mTvCount = (TextView) findViewById(R.id.box_tv_count);
        mBtnSelect = (Button) findViewById(R.id.box_btn_select);
        mUnSelectDrawable = IViewDrawable.getDrawable(mContext, R.drawable.box_img_unchecked);
        mSelectDrawable = IViewDrawable.getDrawable(mContext, R.drawable.box_img_checked);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void initDatas(Bundle savedInstanceState) {
        Type type = new TypeToken<List<String>>() {
        }.getType();
        mSelectImages = IJsonDecoder.jsonToObject(getIntent().getStringExtra(IConstants.INTENT_DATA_ADDITION), type);
        mImages = IJsonDecoder.jsonToObject(getIntentData(), type);
        mVPAdapter.setData(mImages);
        mViewPager.setAdapter(mVPAdapter);
        mViewPager.setCurrentItem(getIntentType());
        mViewPager.setPageMargin(VIEW_PAGER_MARGIN);
        mTvCount.setText(MessageFormat.format("{0}/{1}", getIntentType() + 1, mImages.size()));
        mMaxSelectCount = getIntent().getIntExtra(IConstants.INTENT_TYPE_ADDITION, mMaxSelectCount);
        String path = mImages.get(getIntentType());
        if (mSelectImages.contains(path)) {
            mBtnSelect.setCompoundDrawables(mSelectDrawable, null, null, null);
        } else {
            mBtnSelect.setCompoundDrawables(mUnSelectDrawable, null, null, null);
        }
        int intentType = getIntent().getIntExtra(IOpenType.OPEN_TYPE, IOpenType.Type.EDIT.toInteger());
        if (intentType == IOpenType.Type.EXAMINE.toInteger()) {
            mBtnSelect.setVisibility(View.GONE);
        } else {
            mBtnSelect.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initListeners(Bundle savedInstanceState) {
        //noinspection deprecation
        mViewPager.setOnPageChangeListener(this);
        mBtnSelect.setOnClickListener(this);
    }

    public void back(View v) {
        Intent intent = new Intent();
        intent.putExtra(IKey.PICTURE, new ArrayList<>(mSelectImages));
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void complete(View v) {
        if (mSelectImages.size() == 0) {
            int currentItem = mViewPager.getCurrentItem();
            String path = mImages.get(currentItem);
            mBtnSelect.setCompoundDrawables(mSelectDrawable, null, null, null);
            mSelectImages.add(path);
        }
        Intent intent = new Intent();
        intent.putExtra(IKey.PICTURE, new ArrayList<>(mSelectImages));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.box_anim_menushow, R.anim.box_anim_activity_hide);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(IKey.PICTURE, new ArrayList<>(mSelectImages));
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mTvCount.setText(MessageFormat.format("{0}/{1}", position + 1, mVPAdapter.getCount()));
        String path = mImages.get(position);
        if (mSelectImages.contains(path)) {
            mBtnSelect.setCompoundDrawables(mSelectDrawable, null, null, null);
        } else {
            mBtnSelect.setCompoundDrawables(mUnSelectDrawable, null, null, null);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.box_btn_select) {
            int currentItem = mViewPager.getCurrentItem();
            String path = mImages.get(currentItem);
            if (mSelectImages.contains(path)) {
                mBtnSelect.setCompoundDrawables(mUnSelectDrawable, null, null, null);
                mSelectImages.remove(path);
            } else {
                if (mMaxSelectCount == mSelectImages.size()) {
                    showText(getString(R.string.box_toast_max_select_picture, mMaxSelectCount));
                } else {
                    mBtnSelect.setCompoundDrawables(mSelectDrawable, null, null, null);
                    mSelectImages.add(path);
                }
            }
        }
    }

    @Override
    public void onUserCreateViews(Bundle savedInstanceState) {
    }

}
