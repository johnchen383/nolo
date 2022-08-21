package com.example.nolo.adaptors;

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
import com.example.nolo.activities.SearchActivity;
import com.example.nolo.entities.category.ICategory;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.Laptop;
import com.example.nolo.entities.item.Phone;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.interactors.item.GetLaptopsGroupedByBrandUseCase;
import com.example.nolo.util.Display;

import java.util.ArrayList;
import java.util.List;

public class ListByCategoryAdaptor extends ArrayAdapter {
    private List<List<IItem>> categoryItems;
    private Context mContext;
    private int mLayoutID;

    public class PhoneViewHolder {
        RecyclerView childItemsList;

        public PhoneViewHolder(View currentListViewItem) {
            childItemsList = currentListViewItem.findViewById(R.id.child_items_list);
        }
    }

    public class LaptopsViewHolder extends {
        RecyclerView childItemsList;
        TextView brandName;

        public LaptopsViewHolder(View currentListViewItem) {
            brandName = currentListViewItem.findViewById(R.id.brand_name);
            childItemsList = currentListViewItem.findViewById(R.id.child_items_list);
        }
    }

    public ListByCategoryAdaptor(@NonNull Context context, int resource, @NonNull List<List<IItem>> categoryItems){
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

        System.err.println("No adaptor created for this category");
        return null;
    }

    private View populatePhoneItemsByOs(List<IItem> osItems, View currentListViewItem){
        PhoneViewHolder vh = new PhoneViewHolder(currentListViewItem);


    }

    private View populateLaptopItemsByBrand(List<IItem> items, View currentListViewItem){
        LaptopsViewHolder vh = new LaptopsViewHolder(currentListViewItem);

        vh.brandName.setText(items.get(0).getBrand().toUpperCase());

        setUpChildRecyclerView(vh.childItemsList, items, 0.48);

        return currentListViewItem;
    }

    private void setUpChildRecyclerView(RecyclerView rv, List<IItem> items, double widthFactor){
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);

        List<ItemVariant> defVariants = new ArrayList<>();

        for (IItem item : items){
            defVariants.add((ItemVariant) item.getDefaultItemVariant());
        }

        ItemsCompactAdaptor categoryListAdaptor = new ItemsCompactAdaptor(mContext, defVariants, widthFactor);
        rv.setAdapter(categoryListAdaptor);
    }
}