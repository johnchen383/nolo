package com.example.nolo.adaptors;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.example.nolo.R;
import com.example.nolo.activities.DetailsActivity;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.colour.IColour;
import com.example.nolo.entities.item.specs.specsoption.ISpecsOption;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.interactors.item.GetItemByIdUseCase;
import com.google.android.material.button.MaterialButton;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

public class DetailsCustomisationAdaptor extends RecyclerView.Adapter<DetailsCustomisationAdaptor.ViewHolder>{
    private Context mContext;
    private List<SpecsOption> specsOptions;
    private IItemVariant itemVariant;
    private Consumer<IItemVariant> updateItemVariant;


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView optionText, priceText;
        MaterialButton optionBtn;

        public ViewHolder(View view) {
            super(view);
            optionBtn = view.findViewById(R.id.option_btn);
            optionText = view.findViewById(R.id.option_text);
            priceText = view.findViewById(R.id.price_text);
        }
    }

    public DetailsCustomisationAdaptor(@NonNull Context context, List<SpecsOption> specsOptions, IItemVariant itemVariant, Consumer<IItemVariant> updateItemVariant){
        this.specsOptions = specsOptions;
        this.mContext = context;
        this.itemVariant = itemVariant;
        this.updateItemVariant = updateItemVariant;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_details_customisation, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ISpecsOption specsOption = specsOptions.get(position);
        System.out.println(specsOption.toString());
        holder.optionText.setText(String.valueOf(specsOption.getSize()) + "GB RAM");

        if (specsOption.getAdditionalPrice() != 0) {
            holder.priceText.setText("+$" + String.valueOf(specsOption.getAdditionalPrice()) + ".00");
        } else {
            holder.priceText.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return specsOptions.size();
    }

}
