/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.helper;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import net.box.app.library.IFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by box on 2017/4/20.
 * <p>
 * 处理Fragment的显示隐藏和其他功能
 */

@SuppressWarnings({"WeakerAccess", "unused", "unchecked"})
public class IFragmentHelper {

    private final FragmentManager mFragmentManager;
    private final List<IFragment> mFragments;

    private int mCurrentTabIndex = -1;

    public IFragmentHelper(@NonNull FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
        this.mFragments = new ArrayList<>();
    }

    public <Fragment extends IFragment> List<Fragment> getFragments() {
        return (List<Fragment>) Collections.unmodifiableList(mFragments);
    }

    public <Fragment extends IFragment> Fragment getFragment(int position) {
        if (position < 0 || position >= mFragments.size()) {
            return null;
        }
        return (Fragment) mFragments.get(position);
    }

    public <Fragment extends IFragment> void addFragments(Fragment fragment) {
        fragment.setPage(mFragments.size());
        mFragments.add(fragment);
    }

    public <Fragment extends IFragment> void removeFragment(Fragment fragment) {
        hideFragment(fragment);
        mFragments.remove(fragment);
        for (int i = 0; i < mFragments.size(); i++) {
            mFragments.get(i).setPage(i);
        }
    }

    public void removeAllFragment() {
        hideFragment(mCurrentTabIndex);
        mFragments.clear();
        mCurrentTabIndex = -1;
    }

    public int getCurrentIndex() {
        return mCurrentTabIndex;
    }

    @SafeVarargs
    public final <Fragment extends IFragment> void setFragments(Fragment... fragments) {
        setFragments(Arrays.asList(fragments));
    }

    public <Fragment extends IFragment> void setFragments(List<Fragment> fragments) {
        mFragments.clear();
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
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

    @NonNull
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

    @Nullable
    public IFragment hideFragment(int position) {
        final IFragment fragment = getFragment(position);
        if (fragment != null) {
            return hideFragment(fragment);
        }
        return null;
    }

    @NonNull
    public IFragment hideFragment(@NonNull IFragment fragment) {
        final FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(fragment);
        transaction.commit();
        return fragment;
    }

}
