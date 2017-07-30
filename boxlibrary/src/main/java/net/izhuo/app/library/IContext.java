/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * Created by Changlei on 15/9/1.
 * <p>
 * activity需要实现的方法的接口
 */
@SuppressWarnings("unused")
public interface IContext extends IBaseContext {

    /**
     * <p>
     * <em>Derived classes must call through to the super class's implementation
     * of this method. If they do not, an exception will be thrown.</em>
     * </p>
     */
    void onUserCreateViews(Bundle savedInstanceState);

    /**
     * 初始化View
     */
    void initViews(Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    void initDatas(Bundle savedInstanceState);

    /**
     * 初始化数据监听
     */
    void initListeners(Bundle savedInstanceState);

    Object getContainerLayout();

    /**
     * 刷新ui
     */
    void onRefreshUI();

    boolean onSetContentViewBefore(Bundle savedInstanceState);

    void startActivityForResult(Intent intent, Bundle bundle, int requestCode);

    void startActivityForResult(Intent intent, int requestCode);

    void startActivity(Intent intent, Bundle options);

    void startActivity(Intent intent);

    FragmentManager getSupportFragmentManager();

    Context getContext();

    Activity getActivity();

    void finish();

    void onImageChooseCallback(List<String> imagePaths);

    void onFileChooseCallback(@Nullable String filePath);

}
