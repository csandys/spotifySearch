package com.spotifysearch.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.spotifysearch.R;


public class BottomInset extends RecyclerView.ItemDecoration {

    private final int mInsets;

    public BottomInset(Context context) {
        mInsets = context.getResources().getDimensionPixelSize(R.dimen.lineWidth);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, mInsets);
    }
}
