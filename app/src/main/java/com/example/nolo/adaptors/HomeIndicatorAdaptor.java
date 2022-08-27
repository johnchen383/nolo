package com.example.nolo.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nolo.R;
import com.example.nolo.activities.ListActivity;
import com.example.nolo.entities.category.Category;
import com.example.nolo.entities.category.ICategory;
import com.example.nolo.util.Display;

import java.util.List;
import java.util.function.Consumer;

public class HomeIndicatorAdaptor extends ArrayAdapter {
    private int mLayoutID;
    private List<String> mValues;
    private Context mContext;
    private Consumer<Integer> onClick;

    private class ViewHolder {
        TextView element;

        public ViewHolder(View currentListViewItem) {
            element = currentListViewItem.findViewById(R.id.clickable);
        }
    }

    public HomeIndicatorAdaptor(@NonNull Context context, int resource, @NonNull List<String> values, Consumer<Integer> onClick) {
        super(context, resource, values);
        mContext = context;
        mLayoutID = resource;
        this.mValues = values;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Get a reference to the current ListView item
        View currentListViewItem = convertView;

        // Check if the existing view is being reused, otherwise inflate the view
        if (currentListViewItem == null) {
            currentListViewItem = LayoutInflater.from(getContext()).inflate(mLayoutID, parent, false);
        }

        String currString = mValues.get(position);

        return populateCategoriesListItem(currString, currentListViewItem, position);
    }

    private View populateCategoriesListItem(String currString, View currentListViewItem, int position) {
        ViewHolder vh = new ViewHolder(currentListViewItem);

        vh.element.setText(currString);

        vh.element.setOnClickListener(v -> {
            onClick.accept(position);
        });

        return currentListViewItem;
    }
}
