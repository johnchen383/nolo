package com.example.nolo.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nolo.R;
import com.example.nolo.activities.DetailsActivity;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.util.Animation;

import java.util.List;

public class SearchItemResultAdaptor extends ArrayAdapter {
    private List<IItem> mItems;
    private Context mContext;
    private int mLayoutID;

    private class ViewHolder {
        LinearLayout itemClickable;
        TextView title, price;
        ImageView img;

        public ViewHolder(View currentListViewItem) {
            itemClickable = currentListViewItem.findViewById(R.id.item_clickable);
            title = currentListViewItem.findViewById(R.id.title);
            price = currentListViewItem.findViewById(R.id.price);
            img = currentListViewItem.findViewById(R.id.item_img);
        }
    }

    public SearchItemResultAdaptor(@NonNull Context context, int resource, @NonNull List<IItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mLayoutID = resource;
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

        vh.title.setText(currentItem.getName());
        vh.price.setText(currentItem.getDefaultItemVariant().getDisplayPrice().substring(1) + " NZD");

        int i = mContext.getResources().getIdentifier(
                currentItem.getDefaultItemVariant().getDisplayImage(), "drawable",
                mContext.getPackageName());

        vh.img.setImageResource(i);

        vh.itemClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra(mContext.getString(R.string.extra_item_variant), (ItemVariant) currentItem.getDefaultItemVariant());
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.slide_up, R.anim.slide_stationery);
            }
        });

        return currentListViewItem;
    }
}
