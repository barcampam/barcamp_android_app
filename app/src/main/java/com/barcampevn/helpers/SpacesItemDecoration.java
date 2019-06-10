package com.barcampevn.helpers;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by andranikas on 5/18/2017.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    public SpacesItemDecoration() {
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = 0;
        outRect.right = 0;
        outRect.bottom = 0;
        outRect.top = 0;
    }
}
