package com.example.nolo.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nolo.R;
import com.example.nolo.entities.category.Category;
import com.example.nolo.entities.category.ICategory;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.fragments.HomeFragment;
import com.example.nolo.interactors.item.GetAllItemsUseCase;
import com.example.nolo.interactors.item.GetCategoryItemsUseCase;
import com.example.nolo.interactors.item.GetLaptopsGroupedByBrandUseCase;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ListActivity extends BaseActivity {
    private ViewHolder vh;

    //something

    private class ViewHolder {
        TextView categoryText;

        public ViewHolder(){
            categoryText = findViewById(R.id.category_text);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ICategory category = (Category) getIntent().getSerializableExtra(getString(R.string.extra_category));

        vh = new ViewHolder();

        List<IItem> items = GetLaptopsGroupedByBrandUseCase.getLaptopsGroupedByBrand();

        vh.categoryText.setText(items.toString());


        System.out.println(items);
    }
}