package com.codephillip.app.hymnbook.ui.home;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * https://gist.github.com/KaveriKR/04bfef5ffc9c00a8b6fca503da497322
 * https://stackoverflow.com/questions/47300862/recycler-view-horizontal-scroll-with-2-columns
 * */
public class CardLayoutManager extends GridLayoutManager {


    // column count for orientation = HORIZONTAL
    private static final int spanColumnCount = 2;
    private int width = 50;

    public CardLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CardLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public CardLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public CardLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout, int width) {
        super(context, spanCount, orientation, reverseLayout);
        this.width = width;
    }


    /* Setting LayoutParams for the child views of the recycler view.*/
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return spanLayoutSize(super.generateDefaultLayoutParams());
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
        return spanLayoutSize(super.generateLayoutParams(c, attrs));
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return spanLayoutSize(super.generateLayoutParams(lp));
    }

    /* The function has been made for Horizontal Recycler View.
     *  1. It checks for the orientation to be HORIZONTAL.
     *  2. Its spancount has already been set to 2 in this case throught the constuctor at the initialisation time.
     *    i.e,  layoutManager = new AbsolutefitLayourManager(this,2,GridLayoutManager.HORIZONTAL,false);
     *    the spancount = 2 , specifies the no. of rows for HORIZONTAL orientation
     * 3. The rest of the function divides the horizontal screen width by 2 (spanColumnCount = 2 HERE) hence specyfing the column
          width of each view and hence specifying  2 columns (can be made 3, by dividing by three.)
     */
    private RecyclerView.LayoutParams spanLayoutSize(RecyclerView.LayoutParams layoutParams) {
        if (getOrientation() == HORIZONTAL) {
            layoutParams.width = (int) Math.round(getHorizontalSpace() / spanColumnCount);
            layoutParams.width += this.width;
            // its the margin between the items
            layoutParams.setMargins(2, 2, 2, 2);
        }
        return layoutParams;
    }

    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        return super.checkLayoutParams(lp);
    }

    private int getHorizontalSpace() {
        return getWidth() - (getPaddingRight()) - (getPaddingLeft());
    }
}

