package com.example.nolo.adaptors;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.example.nolo.R;
import com.example.nolo.entities.item.specs.specsoption.ISpecsOption;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.enums.SpecsOptionType;
import com.google.android.material.button.MaterialButton;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

public class DetailsCustomisationAdaptor extends RecyclerView.Adapter<DetailsCustomisationAdaptor.ViewHolder>{
    private Context mContext;
    private List<SpecsOption> specsOptions;
    private SpecsOptionType specType;
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

    public DetailsCustomisationAdaptor(@NonNull Context context, List<SpecsOption> specsOptions, SpecsOptionType specType, IItemVariant itemVariant, Consumer<IItemVariant> updateItemVariant){
        this.specsOptions = specsOptions;
        this.specType = specType;
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
        holder.optionText.setText(String.valueOf(specsOption.getSize()) + "GB " + specType.getUnits());

        if (specsOption.getAdditionalPrice() != 0) {
            holder.priceText.setText("+$" + String.valueOf(specsOption.getAdditionalPrice()) + "0");
        } else {
            holder.priceText.setVisibility(View.GONE);
        }

        switch (specType) {
            case storage:
                if (itemVariant.getStorageOption().equals(specsOption)) {
                    toggleSelectedOptionStyles(holder, true);
                }
                break;
            case ram:
                if (itemVariant.getRamOption().equals(specsOption)) {
                    toggleSelectedOptionStyles(holder, true);
                }
                break;
        }

        holder.optionBtn.setOnClickListener(v -> {
            switch (specType) {
                case storage:
                    itemVariant.setStorageOption(getSpecsOption(holder));
                    break;
                case ram:
                    itemVariant.setRamOption(getSpecsOption(holder));
                    break;
            }
            updateItemVariant.accept(itemVariant);
        });

    }

    @Override
    public int getItemCount() {
        return specsOptions.size();
    }

    private void toggleSelectedOptionStyles(ViewHolder holder, Boolean isSelected) {
        if (isSelected) {
            holder.optionText.setTextColor(ColorStateList.valueOf(Color.parseColor(isSelected ? "#263238" : "#FFFFFF"))); //@TODO get from resources
            holder.optionBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(isSelected ? "#FFFFFF" : "#263238")));
        }
    }

    private SpecsOption getSpecsOption(ViewHolder holder) {
        String option = holder.optionText.getText().toString().split("GB")[0];
        System.out.println(option);

        return specsOptions.stream()
            .filter(o -> o.getSize() == Integer.parseInt(option))
            .findFirst()
            .get();
    }
}
