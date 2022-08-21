package com.example.nolo.adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nolo.R;
import com.example.nolo.activities.SearchActivity;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.util.Display;

import java.util.List;

public class ListByCategoryAdaptor extends RecyclerView.Adapter<ListByCategoryAdaptor.ViewHolder>{
    private List<IItem> categoryItems;
    private Context mContext;
    private CategoryType categoryType;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;
        TextView price;
        LinearLayout itemClickable;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.item_img);
            title = view.findViewById(R.id.item_title);
            price = view.findViewById(R.id.item_price);
            itemClickable = view.findViewById(R.id.item_clickable);
        }
    }

    public class LaptopsViewHolder extends ViewHolder {
        ImageView iconImageView;

        public LaptopsViewHolder(View currentListViewItem) {
            super(currentListViewItem);
//            iconImageView = currentListViewItem.findViewById(R.id.icon_image_view);
        }
    }

    public ListByCategoryAdaptor(@NonNull Context context, List<IItem> categoryItems){
        this.categoryItems = categoryItems;
        this.mContext = context;
        this.categoryType = categoryItems.get(0).getCategoryType();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_compact, parent, false);

        switch (categoryType){
            case laptops:
                return new LaptopsViewHolder(view);
            default:
                System.err.println("No adaptor created for this category");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IItem item = categoryItems.get(position);

        switch (categoryType){
            case laptops:
                populateLaptopItemsByBrand((LaptopsViewHolder) holder, item);
                break;
            default:
                System.err.println("No adaptor created for this category");
                return;
        }

    }

    private void populateLaptopItemsByBrand(@NonNull LaptopsViewHolder holder, IItem item){
        
    }

    @Override
    public int getItemCount() {
        return categoryItems.size();
    }
}