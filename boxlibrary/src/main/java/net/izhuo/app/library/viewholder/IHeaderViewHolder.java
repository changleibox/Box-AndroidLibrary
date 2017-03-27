package net.izhuo.app.library.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Box on 16/8/25.
 * <p/>
 * RecyclerView.ViewHolder基类
 */
public class IHeaderViewHolder extends RecyclerView.ViewHolder {

    public static final int TYPE_HEADER_VIEW = Integer.MIN_VALUE;

    public LinearLayout headerContainer;

    public IHeaderViewHolder(View itemView, int viewType) {
        super(itemView);

        if (viewType == TYPE_HEADER_VIEW) {
            headerContainer = (LinearLayout) itemView;
        } else {
            onCreate(itemView);
        }
    }

    public void onCreate(@SuppressWarnings("UnusedParameters") View itemView) {
    }

    public final int getBaseAdapterPosition() {
        return getAdapterPosition() - 1;
    }
}
