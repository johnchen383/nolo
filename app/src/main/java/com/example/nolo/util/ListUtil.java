package com.example.nolo.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListUtil {
    /**
     * Set dynamic height for the list view
     *
     * @param listView
     */
    public static void setDynamicHeight(ListView listView) {
        ListAdapter mListAdapter = listView.getAdapter();

        // when adapter is null
        if (mListAdapter == null) {
            return;
        }

        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);

        // Get the total height of the list view
        for (int i = 0; i < mListAdapter.getCount(); i++) {
            View listItem = mListAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }

        // Set the total height
        height = height + (listView.getDividerHeight() * (mListAdapter.getCount() - 1));
        ResponsiveView.setHeight(height, listView);
        listView.requestLayout();
    }
}
