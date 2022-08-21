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

public class DetailsColorAdaptor extends RecyclerView.Adapter<DetailsColorAdaptor.ViewHolder>{
    private List<Colour> colours;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialButton colourBtn, colourOutline;

        public ViewHolder(View view) {
            super(view);
            colourBtn = view.findViewById(R.id.colour_btn);
            colourOutline = view.findViewById(R.id.colour_outline);
        }
    }

    public DetailsColorAdaptor(@NonNull Context context, List<Colour> colours){
        this.colours = colours;
        this.mContext = context;
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

        holder.colourBtn.setOnClickListener(v -> {
            holder.colourOutline.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public int getItemCount() {
        return colours.size();
    }
}
