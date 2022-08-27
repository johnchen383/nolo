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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nolo.R;
import com.example.nolo.activities.DetailsActivity;
import com.example.nolo.entities.item.Accessory;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.Laptop;
import com.example.nolo.entities.item.Phone;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.util.Animation;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class ListByCategoryAdaptor extends ArrayAdapter {
    private List<List<IItem>> categoryItems;
    private Context mContext;
    private int mLayoutID;

//    public class PhoneViewHolder {
//        RecyclerView childItemsList;
//
//        public PhoneViewHolder(View currentListViewItem) {
//            childItemsList = currentListViewItem.findViewById(R.id.child_items_list);
//        }
//    }

    public class PhoneViewHolder {
        LinearLayout itemClickable;
        TextView title, price;
        ImageView img;
        LinearLayout itemClickable2;
        TextView title2, price2;
        ImageView img2;

        public PhoneViewHolder(View currentListViewItem) {
            itemClickable = currentListViewItem.findViewById(R.id.item_clickable);
            title = currentListViewItem.findViewById(R.id.item_title);
            price = currentListViewItem.findViewById(R.id.item_price);
            img = currentListViewItem.findViewById(R.id.item_img);
            itemClickable2 = currentListViewItem.findViewById(R.id.item_clickable2);
            title2 = currentListViewItem.findViewById(R.id.item_title2);
            price2 = currentListViewItem.findViewById(R.id.item_price2);
            img2 = currentListViewItem.findViewById(R.id.item_img2);
        }
    }

    public class LaptopsViewHolder {
        RecyclerView childItemsList;
        TextView brandName;

        public LaptopsViewHolder(View currentListViewItem) {
            brandName = currentListViewItem.findViewById(R.id.brand_name);
            childItemsList = currentListViewItem.findViewById(R.id.child_items_list);
        }
    }

    public class AccessoryViewHolder {
        LinearLayout itemClickable;
        TextView title, price;
        ImageView img;
        MaterialCardView laptopsTag, phonesTag;

        public AccessoryViewHolder(View currentListViewItem) {
            itemClickable = currentListViewItem.findViewById(R.id.item_clickable);
            title = currentListViewItem.findViewById(R.id.title);
            price = currentListViewItem.findViewById(R.id.price);
            img = currentListViewItem.findViewById(R.id.item_img);
            laptopsTag = currentListViewItem.findViewById(R.id.laptops_tag);
            phonesTag = currentListViewItem.findViewById(R.id.phones_tag);
        }
    }

    public ListByCategoryAdaptor(@NonNull Context context, int resource, @NonNull List<List<IItem>> categoryItems) {
        super(context, resource, categoryItems);
        this.categoryItems = categoryItems;
        this.mContext = context;
        this.mLayoutID = resource;
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

        //laptops
        if (Laptop.class.equals(categoryItems.get(0).get(0).getClass())) {
            List<IItem> brandItems = categoryItems.get(position);
            return populateLaptopItemsByBrand(brandItems, currentListViewItem);
        }

        //phones
        if (Phone.class.equals(categoryItems.get(0).get(0).getClass())) {
            List<IItem> osItems = categoryItems.get(position);
            return populatePhoneItemsByOs(osItems, currentListViewItem);
        }

        //accessories
        if (Accessory.class.equals(categoryItems.get(0).get(0).getClass())) {
            IItem displayItem = categoryItems.get(position).get(0);
            return populateAccessory(displayItem, currentListViewItem);
        }

        System.err.println("No adaptor created for this category");
        return null;
    }

    private View populateAccessory(IItem item, View currentListViewItem) {
        AccessoryViewHolder vh = new AccessoryViewHolder(currentListViewItem);

        vh.title.setText(item.getName());
        vh.price.setText(item.getDefaultItemVariant().getDisplayPrice().substring(1) + " NZD");

        int i = mContext.getResources().getIdentifier(
                item.getDefaultItemVariant().getDisplayImage(), "drawable",
                mContext.getPackageName());

        vh.img.setImageResource(i);

        vh.itemClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra(mContext.getString(R.string.extra_item_variant), (ItemVariant) item.getDefaultItemVariant());
                mContext.startActivity(intent);
            }
        });

        if (item.getIsForLaptops()) {
            vh.laptopsTag.setVisibility(View.VISIBLE);
        }

        if (item.getIsForPhones()) {
            vh.phonesTag.setVisibility(View.VISIBLE);
        }

        return currentListViewItem;
    }

    private View populatePhoneItemsByOs(List<IItem> items, View currentListViewItem) {
        PhoneViewHolder vh = new PhoneViewHolder(currentListViewItem);

//        setUpChildRecyclerView(vh.childItemsList, osItems, 0.48);

        int index = 0;

        if (items.size() < 1) {
            vh.itemClickable.setVisibility(View.GONE);
            vh.itemClickable2.setVisibility(View.GONE);
            return currentListViewItem;
        }

        vh.title.setText(items.get(index).getName());
        vh.price.setText(items.get(index).getDefaultItemVariant().getDisplayPrice().substring(1) + " NZD");

        int i = mContext.getResources().getIdentifier(
                items.get(index).getDefaultItemVariant().getDisplayImage(), "drawable",
                mContext.getPackageName());

        vh.img.setImageResource(i);

        vh.itemClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra(mContext.getString(R.string.extra_item_variant), (ItemVariant) items.get(index).getDefaultItemVariant());
                mContext.startActivity(intent);
            }
        });

        if (items.size() < 2) {
            vh.itemClickable2.setVisibility(View.GONE);
            return currentListViewItem;
        }
        int index2 = 1;

        vh.title2.setText(items.get(index2).getName());
        vh.price2.setText(items.get(index2).getDefaultItemVariant().getDisplayPrice().substring(1) + " NZD");

        int i2 = mContext.getResources().getIdentifier(
                items.get(index2).getDefaultItemVariant().getDisplayImage(), "drawable",
                mContext.getPackageName());

        vh.img2.setImageResource(i2);

        vh.itemClickable2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra(mContext.getString(R.string.extra_item_variant), (ItemVariant) items.get(index2).getDefaultItemVariant());
                mContext.startActivity(intent);
            }
        });


        return currentListViewItem;
    }

    private View populateLaptopItemsByBrand(List<IItem> items, View currentListViewItem) {
        LaptopsViewHolder vh = new LaptopsViewHolder(currentListViewItem);

        vh.brandName.setText(items.get(0).getBrand().toUpperCase());
        setUpChildRecyclerView(vh.childItemsList, items, 0.4);

        return currentListViewItem;
    }

    private void setUpChildRecyclerView(RecyclerView rv, List<IItem> items, double widthFactor) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);

        List<ItemVariant> defVariants = new ArrayList<>();

        for (IItem item : items) {
            defVariants.add((ItemVariant) item.getDefaultItemVariant());
        }

        ItemsCompactAdaptor categoryListAdaptor = new ItemsCompactAdaptor(mContext, defVariants, widthFactor);
        rv.setAdapter(categoryListAdaptor);
    }
}