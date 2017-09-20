/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.izhuo.app.library.helper;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import net.izhuo.app.library.IFragment;

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

    private List<IFragment> mFragments;

    public IFragmentHelper(@NonNull FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
        this.mFragments = new ArrayList<>();
    }

    public IFragment getFragment(int position) {
        if (position >= mFragments.size()) {
            return null;
        }
        return mFragments.get(position);
    }

    public void addFragments(IFragment fragment) {
        fragment.setPage(mFragments.size());
        mFragments.add(fragment);
    }

    public void removeFragment(IFragment fragment) {
        mFragments.remove(fragment);
        for (int i = 0; i < mFragments.size(); i++) {
            mFragments.get(i).setPage(i);
        }
    }

    public void removeAllFragment() {
        mFragments.clear();
        mCurrentTabIndex = -1;
    }

    @SafeVarargs
    public final <T extends IFragment> void setFragments(T... fragments) {
        setFragments(Arrays.asList(fragments));
    }

    public <T extends IFragment> void setFragments(List<T> fragments) {
        mFragments.clear();
        for (int i = 0; i < fragments.size(); i++) {
            T fragment = fragments.get(i);
            mFragments.add(fragment.setPage(i));
        }
    }

    @Nullable
    public IFragment showFragment(@IdRes int container, int position) {
        IFragment fragment = getFragment(position);
        if (fragment == null) {
            return null;
        }
        return showFragment(container, fragment);
    }

    public IFragment showFragment(@IdRes int container, @NonNull IFragment fragment) {
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
