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
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import com.example.nolo.R;
        import com.example.nolo.activities.DetailsActivity;
        import com.example.nolo.entities.item.colour.Colour;
        import com.example.nolo.entities.item.colour.IColour;
        import com.example.nolo.entities.item.variant.IItemVariant;
        import com.example.nolo.entities.item.variant.ItemVariant;
        import com.example.nolo.interactors.item.GetItemByIdUseCase;
        import com.google.android.material.button.MaterialButton;

        import androidx.core.content.ContextCompat;
        import androidx.recyclerview.widget.RecyclerView;

        import java.util.List;
import java.util.function.Consumer;

public class DetailsColorAdaptor extends RecyclerView.Adapter<DetailsColorAdaptor.ViewHolder>{
    private Context mContext;
    private List<Colour> colours;
    private IItemVariant itemVariant;
    private Consumer<IItemVariant> updateItemVariant;


    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialButton colourBtn, colourOutline;

        public ViewHolder(View view) {
            super(view);
            colourBtn = view.findViewById(R.id.colour_btn);
            colourOutline = view.findViewById(R.id.colour_outline);
        }
    }

    public DetailsColorAdaptor(@NonNull Context context, List<Colour> colours, IItemVariant itemVariant, Consumer<IItemVariant> updateItemVariant){
        this.colours = colours;
        this.mContext = context;
        this.itemVariant = itemVariant;
        this.updateItemVariant = updateItemVariant;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_details_colour, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IColour colour = colours.get(position);
        holder.colourBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colour.getHexCode())));
        holder.colourOutline.setVisibility(itemVariant.getColour().getHexCode().equals(colour.getHexCode()) ? View.VISIBLE : View.INVISIBLE);

        holder.colourBtn.setOnClickListener(v -> {
            String hexCode = String.format("#%06X", (0xFFFFFF & holder.colourBtn.getBackgroundTintList().getDefaultColor()));
            System.out.println(hexCode);
            itemVariant.setColour(getColourFromHex(hexCode));
            updateItemVariant.accept(itemVariant);
        });
    }

    @Override
    public int getItemCount() {
        return colours.size();
    }

    private Colour getColourFromHex(String hexCode) {
        return colours.stream()
                .filter(o -> o.getHexCode().equals(hexCode))
                .findFirst()
                .get();
    }
}
