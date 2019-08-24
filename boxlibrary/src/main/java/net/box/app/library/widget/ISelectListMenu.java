/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.appcompat.app.AppCompatDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import net.box.app.library.R;
import net.box.app.library.util.IViewDrawable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Box
 * @since Jdk1.6
 * <p>
 * 2015年11月18日 下午3:58:58
 * <p>
 * 选择列表菜单
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class ISelectListMenu extends AppCompatDialog {

    private ContentAdapter mContentAdapter;
    private int mSelectedPosition = -1;
    private int[] mMultiselectedPositions = new int[0];
    private OnItemSelectedListener mSelectedListener;
    private OnItemMultiselectedListener mMultiselectedListener;
    private boolean isSingleSelection;
    private ListView mLvContent;
    private boolean isRequired, isDefaultRefresh = true;
    private List<String> mDatas;
    private int mPaddingBottom, mLineMargin;
    private boolean isShowCancel;
    private int mGravity = Gravity.LEFT;
    private Button mBtnCancel;

    public interface OnItemSelectedListener {
        void onItemSelected(ISelectListMenu menu, int position);
    }

    public interface OnItemMultiselectedListener {
        void onItemMultiselected(ISelectListMenu menu, int[] selectedPositions, int clickPosition);
    }

    public enum TextGravity {

        LEFT, RIGHT, CENTER

    }

    /**
     * @param context
     */
    public ISelectListMenu(Context context) {
        super(context, R.style.Box_Dialog_Bottom_Menu);

        setContentView(getContentView(context));

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);

        setSingleSelection(true);
        setRequiredSelection(false);
        setShowCancel(false);
        setGravity(TextGravity.LEFT);
    }

    public void addAll(List<String> collection) {
        mContentAdapter.clear();
        mContentAdapter.addAll(collection);
        mDatas = collection;
    }

    public void addAll(String[] contents) {
        addAll(Arrays.asList(contents));
    }

    public void addAll(int arrayId) {
        Resources res = getContext().getResources();
        addAll(res.getStringArray(arrayId));
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition = selectedPosition;
        mContentAdapter.setCheckedPosition(selectedPosition);
        if (isDefaultRefresh && mSelectedListener != null) {
            mSelectedListener.onItemSelected(this, selectedPosition);
        }
    }

    public int[] getMultiselectedPositions() {
        return mMultiselectedPositions;
    }

    public void setMultiselectedPostions(int[] selectedPositions) {
        mMultiselectedPositions = selectedPositions;
        mContentAdapter.setCheckedPositions(selectedPositions);
        if (isDefaultRefresh && mMultiselectedListener != null) {
            mMultiselectedListener.onItemMultiselected(this, mMultiselectedPositions, 0);
        }
    }

    public String getSelectedItem() {
        return getItemAtPosition(mSelectedPosition);
    }

    public String[] getMultiselectedItems() {
        if (mMultiselectedPositions == null) {
            return null;
        }
        int length = mMultiselectedPositions.length;
        String[] selectedItems = new String[length];
        for (int i = 0; i < length; i++) {
            int selectedPosition = mMultiselectedPositions[i];
            selectedItems[i] = getItemAtPosition(selectedPosition);
        }
        return selectedItems;
    }

    public String getItemAtPosition(int position) {
        return position >= 0 && position < mContentAdapter.getCount() ? mContentAdapter.getItem(position) : null;
    }

    public void setSingleSelection(boolean isSingle) {
        this.isSingleSelection = isSingle;
        mContentAdapter.notifyDataSetChanged();
    }

    public void setRequiredSelection(boolean isRequired) {
        this.isRequired = isRequired;
        if (isRequired) {
            if (isSingleSelection) {
                setSelectedPosition(0);
            } else {
                setMultiselectedPostions(new int[]{0});
            }
        } else {
            if (isSingleSelection) {
                setSelectedPosition(-1);
            } else {
                setMultiselectedPostions(null);
            }
        }
        mContentAdapter.notifyDataSetChanged();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener l) {
        this.mSelectedListener = l;
        if (isDefaultRefresh && mSelectedPosition != -1) {
            mSelectedListener.onItemSelected(this, mSelectedPosition);
        }
    }

    public void setOnItemMultiselectedListener(OnItemMultiselectedListener l) {
        this.mMultiselectedListener = l;
        if (isDefaultRefresh && mMultiselectedPositions != null) {
            mMultiselectedListener.onItemMultiselected(this, mMultiselectedPositions, 0);
        }
    }

    public void setDefaultRefresh(boolean defaultRefresh) {
        this.isDefaultRefresh = defaultRefresh;
    }

    public List<String> getDatas() {
        return mDatas;
    }

    private View getContentView(Context context) {
        Resources res = context.getResources();
        mPaddingBottom = res.getDimensionPixelSize(R.dimen.box_list_view_padding_bottom);
        mLineMargin = res.getDimensionPixelSize(R.dimen.box_list_view_line_margin);
        View contentView = View.inflate(context, R.layout.box_view_menu, null);
        mLvContent = (ListView) contentView.findViewById(R.id.box_lv_content);
        // mLvContent.setOnItemClickListener(this);
        mContentAdapter = new ContentAdapter(getContext());
        mLvContent.setAdapter(mContentAdapter);
        mBtnCancel = (Button) contentView.findViewById(R.id.box_btn_cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return contentView;
    }

    public boolean isShowCancel() {
        return isShowCancel;
    }

    public void setShowCancel(boolean isShowCancel) {
        this.isShowCancel = isShowCancel;
        mBtnCancel.setVisibility(isShowCancel ? View.VISIBLE : View.GONE);
        mLvContent.setPadding(0, 0, 0, isShowCancel ? mPaddingBottom : 0);
    }

    public TextGravity getGravity() {
        return mGravity == Gravity.LEFT ? TextGravity.LEFT
                : (mGravity == Gravity.RIGHT ? TextGravity.RIGHT : TextGravity.CENTER);
    }

    public void setGravity(TextGravity gravity) {
        this.mGravity = (gravity == TextGravity.LEFT ? Gravity.LEFT
                : (gravity == TextGravity.RIGHT ? Gravity.RIGHT : Gravity.CENTER));
        mContentAdapter.notifyDataSetChanged();
    }

    @SuppressLint("ViewHolder")
    private class ContentAdapter extends ArrayAdapter<String> {

        private boolean[] isCheckedArrays = new boolean[0];
        private Drawable mCheckedDraw;

        /**
         * @param context
         */
        @SuppressLint("ResourceType")
        public ContentAdapter(Context context) {
            super(context, 1);
            mCheckedDraw = IViewDrawable.getDrawable(context, R.drawable.box_img_dir_choose);
        }

        @Override
        public void addAll(Collection<? extends String> collection) {
            isCheckedArrays = new boolean[collection.size()];
            super.addAll(collection);
        }

        public void setCheckedPositions(int[] selectedPositons) {
            if (isCheckedArrays.length < selectedPositons.length) {
                return;
            }
            List<Integer> positionList = new ArrayList<>();
            for (Integer index : selectedPositons) {
                positionList.add(index);
            }

            for (int i = 0; i < isCheckedArrays.length; i++) {
                isCheckedArrays[i] = positionList.contains(i);
            }
            notifyDataSetChanged();
        }

        public void setCheckedPosition(int selectedPosition) {
            for (int i = 0; i < isCheckedArrays.length; i++) {
                isCheckedArrays[i] = (selectedPosition == i);
            }
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(getContext(), R.layout.box_item_menu, null);
            TextView tvContent = (TextView) convertView.findViewById(R.id.box_tv_content);
            View line = convertView.findViewById(R.id.box_line);
            tvContent.setText(getItem(position));
            if (isCheckedArrays[position] && mGravity != Gravity.CENTER) {
                if (mGravity == Gravity.LEFT) {
                    tvContent.setCompoundDrawables(null, null, mCheckedDraw, null);
                } else {
                    tvContent.setCompoundDrawables(mCheckedDraw, null, null, null);
                }
            } else {
                tvContent.setCompoundDrawables(null, null, null, null);
            }

            LayoutParams layoutParams = (LayoutParams) line.getLayoutParams();
            if (mGravity == Gravity.CENTER) {
                layoutParams.leftMargin = mLineMargin;
                layoutParams.rightMargin = mLineMargin;
            } else {
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = 0;
            }
            line.setLayoutParams(layoutParams);
            boolean isBottom = position == getCount() - 1;
            line.setVisibility(isBottom ? View.GONE : View.VISIBLE);

            tvContent.setGravity(mGravity | Gravity.CENTER_VERTICAL);

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (isSingleSelection) {
                        for (int i = 0; i < isCheckedArrays.length; i++) {
                            isCheckedArrays[i] = i == position && (isRequired || !isCheckedArrays[i]);
                        }
                        notifyDataSetChanged();
                        mSelectedPosition = position;
                        dismiss();
                        if (mSelectedListener != null) {
                            mSelectedListener.onItemSelected(ISelectListMenu.this, position);
                        }
                    } else {
                        isCheckedArrays[position] = !isCheckedArrays[position];
                        notifyDataSetChanged();
                        List<Integer> selectedPositions = new ArrayList<>();
                        for (int i = 0; i < isCheckedArrays.length; i++) {
                            if (isCheckedArrays[i]) {
                                selectedPositions.add(i);
                            }
                        }
                        Integer[] integers = selectedPositions.toArray(new Integer[selectedPositions.size()]);
                        int length = integers.length;
                        mMultiselectedPositions = new int[length];
                        for (int i = 0; i < length; i++) {
                            mMultiselectedPositions[i] = integers[i];
                        }
                        if (isRequired && mMultiselectedPositions.length == 0) {
                            setMultiselectedPostions(new int[]{position});
                        }
                        if (mMultiselectedListener != null) {
                            mMultiselectedListener.onItemMultiselected(ISelectListMenu.this, mMultiselectedPositions, position);
                        }
                    }
                }
            });
            return convertView;
        }
    }

}
