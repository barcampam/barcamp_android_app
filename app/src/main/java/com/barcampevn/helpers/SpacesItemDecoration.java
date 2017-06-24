package com.barcampevn.helpers;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by andranikas on 5/18/2017.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    public SpacesItemDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = 0;
        outRect.right = 0;
        outRect.bottom = 0;
        outRect.top = 0;
    }
}
