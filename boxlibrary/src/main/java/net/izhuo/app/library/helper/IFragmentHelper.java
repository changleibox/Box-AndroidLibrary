/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.helper;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import net.izhuo.app.library.IBaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by box on 2017/4/20.
 * <p>
 * 处理Fragment的显示隐藏和其他功能
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class IFragmentHelper {

    private int mCurrentTabIndex = -1;
    private FragmentManager mFragmentManager;

    private List<IBaseFragment> mFragments;

    public IFragmentHelper(@NonNull FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
        this.mFragments = new ArrayList<>();
    }

    public IBaseFragment getFragment(int position) {
        if (position >= mFragments.size()) {
            return null;
        }
        return mFragments.get(position);
    }

    public void addFragments(IBaseFragment fragment) {
        fragment.setPage(mFragments.size());
        mFragments.add(fragment);
    }

    public void removeFragment(IBaseFragment fragment) {
        mFragments.remove(fragment);
        for (int i = 0; i < mFragments.size(); i++) {
            mFragments.get(i).setPage(i);
        }
    }

    @SafeVarargs
    public final <T extends IBaseFragment> void setFragments(T... fragments) {
        setFragments(Arrays.asList(fragments));
    }

    public <T extends IBaseFragment> void setFragments(List<T> fragments) {
        mFragments.clear();
        for (int i = 0; i < fragments.size(); i++) {
            T fragment = fragments.get(i);
            mFragments.add(fragment.setPage(i));
        }
    }

    @Nullable
    public IBaseFragment showFragment(@IdRes int container, int position) {
        IBaseFragment fragment = getFragment(position);
        if (fragment == null) {
            return null;
        }
        return showFragment(container, fragment);
    }

    public IBaseFragment showFragment(@IdRes int container, @NonNull IBaseFragment fragment) {
        if (!mFragments.contains(fragment)) {
            mFragments.add(fragment.setPage(mFragments.size()));
        }
        if (!fragment.isVisible() && mCurrentTabIndex != fragment.getPage()) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (!fragment.isAdded()) {
                transaction.add(container, fragment);
            }

            if (mCurrentTabIndex != -1) {
                transaction.hide(mFragments.get(mCurrentTabIndex));
            }
            transaction.show(fragment).commit();
            mCurrentTabIndex = fragment.getPage();
        }
        return fragment;
    }

}
