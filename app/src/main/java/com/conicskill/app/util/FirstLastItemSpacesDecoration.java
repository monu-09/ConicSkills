package com.conicskill.app.util;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class FirstLastItemSpacesDecoration extends RecyclerView.ItemDecoration {
    private final int directSpace;
    private final int reverseSpace;

    public FirstLastItemSpacesDecoration(int i, boolean z) {
        if (z) {
            this.directSpace = 0;
            this.reverseSpace = i;
            return;
        }
        this.directSpace = i;
        this.reverseSpace = 0;
    }

    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        if (recyclerView.getChildAdapterPosition(view) == state.getItemCount() - 1) {
            rect.right = this.directSpace;
            rect.left = this.reverseSpace;
        }
        if (recyclerView.getChildAdapterPosition(view) == 0) {
            rect.right = this.reverseSpace;
            rect.left = this.directSpace;
        }
    }
}