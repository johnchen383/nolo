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
import com.example.nolo.entities.item.IItem;

import java.util.List;

public class HomeSearchItemsAdaptor extends ArrayAdapter {
    private int mLayoutID;
    private List<IItem> mItems;
    private Context mContext;

    private class ViewHolder {
        TextView searchSuggestText;

        public ViewHolder(View currentListViewItem) {
            searchSuggestText = currentListViewItem.findViewById(R.id.search_suggestion_item_text);
        }
    }
    
    public HomeSearchItemsAdaptor(@NonNull Context context, int resource, @NonNull List<IItem> objects) {
        super(context, resource, objects);
        mLayoutID = resource;
        mContext = context;
        mItems = objects;
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

        IItem currentItem = mItems.get(position);

        return populateItemListItem(currentItem, currentListViewItem);
    }

    private View populateItemListItem(IItem currentItem, View currentListViewItem) {
        ViewHolder vh = new ViewHolder(currentListViewItem);

        vh.searchSuggestText.setText(currentItem.getName());

        vh.searchSuggestText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: when item in search suggestion is clicked, go to detail page
            }
        });

        return currentListViewItem;
    }
}
