package com.example.nolo.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nolo.activities.ListActivity;
import com.example.nolo.entities.category.Category;
import com.example.nolo.entities.category.ICategory;
import com.example.nolo.R;
import com.example.nolo.util.Display;

import java.util.List;

public class HomeCategoryAdaptor extends ArrayAdapter {
    private int mLayoutID;
    private List<ICategory> mCategories;
    private Context mContext;

    private class ViewHolder {
        TextView categoryLabel;
        ImageView categoryImg;
        ImageButton categoryBtn;

        public ViewHolder(View currentListViewItem) {
            categoryLabel = currentListViewItem.findViewById(R.id.category_label);
            categoryImg = currentListViewItem.findViewById(R.id.category_img);
            categoryBtn = currentListViewItem.findViewById(R.id.category_btn);
        }
    }

    public HomeCategoryAdaptor(@NonNull Context context, int resource, @NonNull List<ICategory> categories, Activity origActivity) {
        super(context, resource, categories);
        mContext = context;
        mLayoutID = resource;
        mCategories = categories;
//        this.origActivity = origActivity;
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

        ICategory currentCategory = mCategories.get(position);

        return populateCategoriesListItem(currentCategory, currentListViewItem);
    }

    private View populateCategoriesListItem(ICategory currentCategory, View currentListViewItem) {
        ViewHolder vh = new ViewHolder(currentListViewItem);

        int i = mContext.getResources().getIdentifier(
                currentCategory.getImageUri(), "drawable",
                mContext.getPackageName());

        //setting the category img
        vh.categoryImg.setImageResource(i);
        vh.categoryImg.getLayoutParams().height = Display.getScreenHeight(currentListViewItem);

        // Setting the category label
        vh.categoryLabel.setText(currentCategory.getCategoryName());

        //add click listener
        vh.categoryBtn.setOnClickListener(v -> {
            Activity baseContext = (Activity) getContext();
            Intent intent = new Intent(baseContext, ListActivity.class);
            intent.putExtra(baseContext.getString(R.string.extra_category), (Category) currentCategory);

            baseContext.startActivity(intent);
            baseContext.overridePendingTransition(R.anim.slide_up, R.anim.slide_stationery);
        });

        return currentListViewItem;
    }
}
