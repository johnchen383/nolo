package com.example.nolo.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.example.nolo.R;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.interactors.item.GetItemByIdUseCase;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeFeaturedItemsAdaptor extends RecyclerView.Adapter<HomeFeaturedItemsAdaptor.ViewHolder>{
    private List<IItemVariant> featuredItems;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;
        TextView price;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.item_img);
            title = view.findViewById(R.id.item_title);
            price = view.findViewById(R.id.item_price);
        }
    }

    public HomeFeaturedItemsAdaptor(@NonNull Context context, List<IItemVariant> featuredItems){
        this.featuredItems = featuredItems;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_featured, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String itemId = featuredItems.get(position).getItemId();
        String storeId = featuredItems.get(position).getStoreId();
        IItem item = GetItemByIdUseCase.getItemById(itemId);
        holder.title.setText(item.getName());
        holder.price.setText("$" + item.getBasePrice(storeId));

        int i = mContext.getResources().getIdentifier(
                item.getImageUris().get(0), "drawable",
                mContext.getPackageName());

        holder.img.setImageResource(i);
    }

    @Override
    public int getItemCount() {
        return featuredItems.size();
    }
}
