package com.example.nolo.adaptors;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nolo.R;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.colour.IColour;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MapColourOverlayAdaptor extends RecyclerView.Adapter<MapColourOverlayAdaptor.ViewHolder> {
    private Context mContext;
    private List<Colour> colours;
    private IItemVariant itemVariant;

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout colourBtn;
        MaterialButton colourCircle, colourOutline;

        public ViewHolder(View view) {
            super(view);
            colourBtn = view.findViewById(R.id.colour_btn);
            colourCircle = view.findViewById(R.id.colour_circle);
            colourOutline = view.findViewById(R.id.colour_outline);
        }
    }

    public MapColourOverlayAdaptor(@NonNull Context context, List<Colour> colours, IItemVariant itemVariant) {
        this.mContext = context;
        this.colours = colours;
        this.itemVariant = itemVariant;
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
        holder.colourCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colour.getHexCode())));
        holder.colourOutline.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return colours.size();
    }
}
