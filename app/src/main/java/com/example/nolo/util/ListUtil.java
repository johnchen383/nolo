package com.example.nolo.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;

public class ListUtil {
    public static void setDynamicHeight(ListView mListView) {
        ListAdapter mListAdapter = mListView.getAdapter();
        if (mListAdapter == null) {
            // when adapter is null
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < mListAdapter.getCount(); i++) {
            View listItem = mListAdapter.getView(i, null, mListView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
        mListView.setLayoutParams(params);
        mListView.requestLayout();
    }

//    public static void setDynamicHeight(RecyclerView mRecyclerView) {
//        RecyclerView.Adapter mListAdapter = mRecyclerView.getAdapter();
//        if (mListAdapter == null) {
//            // when adapter is null
//            return;
//        }
//        int height = 0;
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(mRecyclerView.getWidth(), View.MeasureSpec.UNSPECIFIED);
//
//        for (int i = 0; i < mListAdapter.getItemCount(); i++) {
//            View listItem = mListAdapter.view;
//            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//            height += listItem.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = mRecyclerView.getLayoutParams();
//        params.height = height;
////        params.height = height + (mRecyclerView.get * (mListAdapter.getItemCount() - 1));
//        mRecyclerView.setLayoutParams(params);
//        mRecyclerView.requestLayout();
//    }
}
