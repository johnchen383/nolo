package com.example.nolo.adaptors;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nolo.util.Display;

public class MapColourOverlayDecoration extends RecyclerView.ItemDecoration {
    private final static int vertOverlap = -30;

    @Override
    public void getItemOffsets (Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.set(0, 0, Display.dpToPx(vertOverlap, view.getContext()), 0);
    }
}
