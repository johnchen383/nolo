package com.example.nolo.adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nolo.R;
import com.example.nolo.activities.DetailsActivity;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.util.Animation;

import java.util.List;

public class CartItemsAdaptor extends ArrayAdapter {
    private List<IItem> mItems;
    private Context mContext;
    private int mLayoutID;

    private class ViewHolder {
        LinearLayout itemClickable;
        TextView title, priceText, quantityText;
        ImageView img, binBtn;
        RelativeLayout incrementBtn, decrementBtn;

        public ViewHolder(View currentListViewItem) {
            itemClickable = currentListViewItem.findViewById(R.id.item_clickable);
            title = currentListViewItem.findViewById(R.id.title);
            priceText = currentListViewItem.findViewById(R.id.price);
            quantityText = currentListViewItem.findViewById(R.id.quantity_text);
            img = currentListViewItem.findViewById(R.id.item_img);
            incrementBtn = currentListViewItem.findViewById(R.id.increment_btn);
            decrementBtn = currentListViewItem.findViewById(R.id.decrement_btn);
        }
    }

    public CartItemsAdaptor(@NonNull Context context, int resource, @NonNull List<IItem> objects) {
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
        vh.priceText.setText(currentItem.getDefaultItemVariant().getDisplayPrice());

        int i = mContext.getResources().getIdentifier(
                currentItem.getDefaultItemVariant().getDisplayImage(), "drawable",
                mContext.getPackageName());

        vh.img.setImageResource(i);

        vh.itemClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra(mContext.getString(R.string.extra_item_variant), (ItemVariant) currentItem.getDefaultItemVariant());
                mContext.startActivity(intent, Animation.Fade(mContext).toBundle());
            }
        });

        return currentListViewItem;
    }
}
