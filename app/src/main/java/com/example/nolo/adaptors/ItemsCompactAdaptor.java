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
import com.example.nolo.util.Animation;
import com.example.nolo.util.Display;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemsCompactAdaptor extends RecyclerView.Adapter<ItemsCompactAdaptor.ViewHolder>{
    private List<ItemVariant> featuredItems;
    private Context mContext;
    private double widthFactor;

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

    public ItemsCompactAdaptor(@NonNull Context context, List<ItemVariant> featuredItems, double widthFactor){
        this.featuredItems = featuredItems;
        this.mContext = context;
        this.widthFactor = widthFactor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_compact, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IItemVariant variant = featuredItems.get(position);

        holder.title.setText(variant.getTitle());

        holder.price.setText(variant.getDisplayPrice().substring(1) + " NZD");

        int i = mContext.getResources().getIdentifier(
                variant.getDisplayImage(), "drawable",
                mContext.getPackageName());
        holder.img.setImageResource(i);

        double width = Display.getScreenWidth(holder.itemClickable) * widthFactor;

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemClickable.getLayoutParams();
        layoutParams.width = (int) width;

        LinearLayout.LayoutParams ivLayoutParams = (LinearLayout.LayoutParams) holder.img.getLayoutParams();
        ivLayoutParams.height = (int) (0.75 * width);

        holder.itemClickable.setLayoutParams(layoutParams);
        holder.img.setLayoutParams(ivLayoutParams);

        holder.itemClickable.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DetailsActivity.class);
            intent.putExtra(mContext.getString(R.string.extra_item_variant), (ItemVariant) variant);

            mContext.startActivity(intent, Animation.Fade(mContext).toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return featuredItems.size();
    }
}
