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
import com.example.nolo.R;
import com.example.nolo.activities.DetailsActivity;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.interactors.item.GetItemByIdUseCase;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeFeaturedItemsAdaptor extends RecyclerView.Adapter<HomeFeaturedItemsAdaptor.ViewHolder>{
    private List<ItemVariant> featuredItems;
    private Context mContext;

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

    public HomeFeaturedItemsAdaptor(@NonNull Context context, List<ItemVariant> featuredItems){
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
        IItemVariant variant = featuredItems.get(position);

        holder.title.setText(variant.getTitle());

        holder.price.setText(variant.getDisplayPrice());

        int i = mContext.getResources().getIdentifier(
                variant.getDisplayImage(), "drawable",
                mContext.getPackageName());
        holder.img.setImageResource(i);

        holder.itemClickable.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DetailsActivity.class); //TODO: change to details activity
            intent.putExtra(mContext.getString(R.string.extra_item_variant), (ItemVariant) variant);

            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return featuredItems.size();
    }
}