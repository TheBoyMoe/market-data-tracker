package com.example.marketdatatracker.custom;


import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CustomItemDecoration extends RecyclerView.ItemDecoration{

    private int mSpace;

    public CustomItemDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // set the space on each side of the item
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
        outRect.top = mSpace;
    }

}
