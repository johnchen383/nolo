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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nolo.R;
import com.example.nolo.activities.DetailsActivity;
import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.enums.SpecsOptionType;
import com.example.nolo.fragments.CartFragment;
import com.example.nolo.fragments.PurchasesFragment;
import com.example.nolo.fragments.WishlistFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.function.Consumer;

public class ItemsListVariantAdaptor extends ArrayAdapter {
    private int mLayoutID;
    private Fragment mFragment;
    private Context mContext;
    private List<Purchasable> mPurchasableItems;
    private List<ItemVariant> mVariantItems;
    private Consumer<List<Purchasable>> purchasableUpdate;
    private Consumer<ItemVariant> variantUpdate;

    private class ViewHolder {
        LinearLayout itemClickable;
        ImageView itemImg;
        TextView title, ramText, ssdText, colourLabel, price;
        MaterialCardView ramTag, ssdTag, colourCircle;
        RelativeLayout heartOutlineIcon;

        public ViewHolder(View v) {
            itemClickable = v.findViewById(R.id.item_clickable);
            itemImg = v.findViewById(R.id.item_img);
            title = v.findViewById(R.id.title);
            ramText = v.findViewById(R.id.ram_text);
            ssdText = v.findViewById(R.id.ssd_text);
            colourLabel = v.findViewById(R.id.colour_label);
            price = v.findViewById(R.id.price);
            ramTag = v.findViewById(R.id.ram_tag);
            ssdTag = v.findViewById(R.id.ssd_tag);
            colourCircle = v.findViewById(R.id.colour_circle);
        }
    }

    private class CartViewHolder extends ViewHolder {
        TextView quantityText;
        RelativeLayout binBtn;
        LinearLayout quantityControl;
        RelativeLayout decrementBtn, incrementBtn;

        public CartViewHolder(View v) {
            super(v);
            binBtn = v.findViewById(R.id.bin_btn);
            quantityControl = v.findViewById(R.id.quantity_control);
            decrementBtn = v.findViewById(R.id.decrement_btn);
            incrementBtn = v.findViewById(R.id.increment_btn);
            quantityText = v.findViewById(R.id.quantity_text);

        }
    }

    private class PurchasesViewHolder extends ViewHolder {
        TextView quantityLabel;

        public PurchasesViewHolder(View v) {
            super(v);
            quantityLabel = v.findViewById(R.id.quantity_label);
        }
    }

    private class WishlistViewHolder extends ViewHolder {
        RelativeLayout heartBtn;
        ImageView heartIcon;

        public WishlistViewHolder(View v) {
            super(v);
            heartBtn = v.findViewById(R.id.heart_btn);
            heartIcon = v.findViewById(R.id.heart_icon);
        }
    }

    public ItemsListVariantAdaptor(@NonNull Context context, Fragment fragment, int resource,
                                   List<Purchasable> purchasableItems, Consumer<List<Purchasable>> purchasableUpdate,
                                   List<ItemVariant> variantItems, Consumer<ItemVariant> variantUpdate) {
        super(context, resource, purchasableItems == null ? variantItems : purchasableItems);
        mContext = context;
        mFragment = fragment;
        mLayoutID = resource;

        if (purchasableItems != null) {
            mPurchasableItems = purchasableItems;
            this.purchasableUpdate = purchasableUpdate;
            mVariantItems = null;
            variantUpdate = null;
        } else if (variantItems != null){
            mVariantItems = variantItems;
            this.variantUpdate = variantUpdate;
            mPurchasableItems = null;
            purchasableUpdate = null;
        }
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

        Purchasable currentPurchasableItem = mPurchasableItems == null ? null : mPurchasableItems.get(position);
        ItemVariant currentVariantItem = mVariantItems == null ? null : mVariantItems.get(position);

        if (mFragment.getClass().equals(CartFragment.class)) {
            return populateCartItem(currentPurchasableItem, currentListViewItem);
        } else if (mFragment.getClass().equals(PurchasesFragment.class)) {
            return populatePurchasesItem(currentPurchasableItem, currentListViewItem);
        } else if (mFragment.getClass().equals(WishlistFragment.class)) {
            System.out.println("one!");
            return populateWishlistItem(currentVariantItem, currentListViewItem);
        }

        return null;
    }

    private void populateItem(ItemVariant variant, ViewHolder vh) {
        if (variant == null) {
            vh.itemClickable.setVisibility(View.VISIBLE);
            return;
        }

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
            intent.putExtra(baseContext.getString(R.string.extra_item_variant), variant);

            baseContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.slide_up, R.anim.slide_stationery);
        });

        vh.title.setOnClickListener(v -> {
            Activity baseContext = (Activity) getContext();
            Intent intent = new Intent(baseContext, DetailsActivity.class);
            intent.putExtra(baseContext.getString(R.string.extra_item_variant), variant);

            baseContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.slide_up, R.anim.slide_stationery);
        });
    }

    private View populateCartItem(Purchasable item, View currentListViewItem) {
        CartViewHolder vh = new CartViewHolder(currentListViewItem);

        populateItem(item.getItemVariant(), vh);

        vh.binBtn.setVisibility(View.VISIBLE);
        vh.binBtn.setOnClickListener(v -> {
            mPurchasableItems.remove(item);
            purchasableUpdate.accept(mPurchasableItems);
        });

        vh.quantityControl.setVisibility(View.VISIBLE);

        vh.quantityText.setText(String.valueOf(item.getQuantity()));

        vh.decrementBtn.setOnClickListener(v -> {
            for (Purchasable p : mPurchasableItems) {
                if (p.equals(item)) {
                    p.incrementOrDecrementQuantity(false);
                }
            }
            purchasableUpdate.accept(mPurchasableItems);
        });

        vh.incrementBtn.setOnClickListener(v -> {
            for (Purchasable p : mPurchasableItems) {
                if (p.equals(item)) {
                    p.incrementOrDecrementQuantity(true);
                }
            }
            purchasableUpdate.accept(mPurchasableItems);
        });

        return currentListViewItem;
    }

    private View populatePurchasesItem(Purchasable item, View currentListViewItem) {
        PurchasesViewHolder vh = new PurchasesViewHolder(currentListViewItem);

        populateItem(item.getItemVariant(), vh);

        vh.quantityLabel.setVisibility(View.VISIBLE);
        vh.quantityLabel.setText("Quantity: " + item.getQuantity());

        return currentListViewItem;
    }

    private View populateWishlistItem(ItemVariant variant, View currentListViewItem) {
        System.out.println("one!");
        WishlistViewHolder vh = new WishlistViewHolder(currentListViewItem);

        populateItem(variant, vh);

        vh.heartBtn.setVisibility(View.VISIBLE);

        vh.heartBtn.setOnClickListener(v -> {
            variantUpdate.accept(variant);
        });

        return currentListViewItem;
    }
}
