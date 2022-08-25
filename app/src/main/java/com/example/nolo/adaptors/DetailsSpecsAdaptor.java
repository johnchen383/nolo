package com.example.nolo.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nolo.R;
import com.example.nolo.enums.SpecsType;

import java.util.List;
import java.util.Map;

public class DetailsSpecsAdaptor extends ArrayAdapter {
    List<SpecsType> mSpecsNames;
    private Map<String, String> mSpecsDescription;
    private Context mContext;
    private int mLayoutID;

    private class ViewHolder {
        TextView descriptionName, descriptionText;

        public ViewHolder(View currentListViewItem) {
            descriptionName = currentListViewItem.findViewById(R.id.description_name);
            descriptionText = currentListViewItem.findViewById(R.id.description_text);
        }
    }

    public DetailsSpecsAdaptor(@NonNull Context context, int resource, @NonNull List<SpecsType> objects,
                               Map<String, String> specsDescription) {
        super(context, resource, objects);
        mContext = context;
        mLayoutID = resource;
        mSpecsNames = objects;
        mSpecsDescription = specsDescription;
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

        SpecsType currentSpecsNames = mSpecsNames.get(position);

        return populateItemListItem(currentSpecsNames, currentListViewItem);
    }

    private View populateItemListItem(SpecsType currentSpecsNames, View currentListViewItem) {
        ViewHolder vh = new ViewHolder(currentListViewItem);

        vh.descriptionName.setText(currentSpecsNames.getFullname());
        vh.descriptionText.setText(mSpecsDescription.get(currentSpecsNames.name()));

        return currentListViewItem;
    }
}
