package com.example.nolo.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nolo.R;
import com.example.nolo.activities.DetailsActivity;
import com.example.nolo.activities.ListActivity;
import com.example.nolo.entities.category.Category;
import com.example.nolo.entities.category.ICategory;
import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.enums.SpecsOptionType;
import com.example.nolo.util.Display;
import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.function.Consumer;

public class CartPurchasableAdaptor extends ArrayAdapter {
    private int mLayoutID;
    private List<Purchasable> mItems;
    private Context mContext;
    private Consumer<List<Purchasable>> update;

    private class ViewHolder {
        LinearLayout itemClickable;
        ImageView itemImg;
        TextView title, ramText, ssdText, colourLabel, price, quantityText;
        RelativeLayout binBtn, decrementBtn, incrementBtn;
        MaterialCardView ramTag, ssdTag, colourCircle;

        public ViewHolder(View v) {
            itemClickable = v.findViewById(R.id.item_clickable);
            itemImg = v.findViewById(R.id.item_img);
            title = v.findViewById(R.id.title);
            ramText = v.findViewById(R.id.ram_text);
            ssdText = v.findViewById(R.id.ssd_text);
            colourLabel = v.findViewById(R.id.colour_label);
            price = v.findViewById(R.id.price);
            quantityText = v.findViewById(R.id.quantity_text);
            binBtn = v.findViewById(R.id.bin_btn);
            decrementBtn = v.findViewById(R.id.decrement_btn);
            incrementBtn = v.findViewById(R.id.increment_btn);
            ramTag = v.findViewById(R.id.ram_tag);
            ssdTag = v.findViewById(R.id.ssd_tag);
            colourCircle = v.findViewById(R.id.colour_circle);
        }
    }

    public CartPurchasableAdaptor(@NonNull Context context, int resource, @NonNull List<Purchasable> cartItems, Consumer<List<Purchasable>> update) {
        super(context, resource, cartItems);
        mContext = context;
        mLayoutID = resource;
        mItems = cartItems;
        this.update = update;
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

        Purchasable currentItem = mItems.get(position);

        return populateCartItem(currentItem, currentListViewItem);
    }

    private View populateCartItem(Purchasable item, View currentListViewItem) {
        ViewHolder vh = new ViewHolder(currentListViewItem);

        if (item == null) {
            vh.itemClickable.setVisibility(View.VISIBLE);
            System.out.println("FROG");
            return currentListViewItem;
        }

        ItemVariant variant = item.getItemVariant();

//        price = v.findViewById(R.id.price);
//        quantityText = v.findViewById(R.id.quantity_text);
//        binBtn = v.findViewById(R.id.bin_btn);
//        decrementBtn = v.findViewById(R.id.decrement_btn);
//        incrementBtn = v.findViewById(R.id.increment_btn);
        vh.binBtn.setOnClickListener(v -> {
            mItems.remove(item);
            update.accept(mItems);
        });

        vh.quantityText.setText(item.getQuantity() + "");

        vh.decrementBtn.setOnClickListener(v -> {
            for (Purchasable p : mItems) {
                if (p.equals(item)) {
                    p.incrementOrDecrementQuantity(false);
                }
            }
            update.accept(mItems);
        });

        vh.incrementBtn.setOnClickListener(v -> {
            for (Purchasable p : mItems) {
                if (p.equals(item)) {
                    p.incrementOrDecrementQuantity(true);
                }
            }
            update.accept(mItems);
        });

        int i = mContext.getResources().getIdentifier(
                variant.getDisplayImage(), "drawable",
                mContext.getPackageName());
        vh.itemImg.setImageResource(i);

        vh.title.setText(variant.getTitle());

        if (variant.getRamOption() != null) {
//            vh.ramText.setText(variant.getRamOption().getSize());
            vh.ramText.setText(String.valueOf(variant.getRamOption().getSize()) + SpecsOptionType.ram.getUnits());
        } else {
            vh.ramTag.setVisibility(View.GONE);
        }

        if (variant.getStorageOption() != null) {
            vh.ssdText.setText(String.valueOf(variant.getStorageOption().getSize()) + "GB " + SpecsOptionType.storage.getUnits());
//            vh.ssdText.setText(variant.getStorageOption().getSize());
        } else {
            vh.ssdTag.setVisibility(View.GONE);
        }

        vh.colourLabel.setText(variant.getColour().getName());
        vh.colourCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(variant.getColour().getHexCode())));

        vh.price.setText(variant.getDisplayPrice());

        //add click listener
        vh.itemImg.setOnClickListener(v -> {
            Activity baseContext = (Activity) getContext();
            Intent intent = new Intent(baseContext, DetailsActivity.class);
            intent.putExtra(baseContext.getString(R.string.extra_item_variant), item.getItemVariant());

            baseContext.startActivity(intent);
        });

        vh.title.setOnClickListener(v -> {
            Activity baseContext = (Activity) getContext();
            Intent intent = new Intent(baseContext, DetailsActivity.class);
            intent.putExtra(baseContext.getString(R.string.extra_item_variant), item.getItemVariant());

            baseContext.startActivity(intent);
        });

        return currentListViewItem;
    }
}
