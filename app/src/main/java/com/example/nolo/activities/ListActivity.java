package com.example.nolo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nolo.R;
import com.example.nolo.adaptors.HomeCategoryAdaptor;
import com.example.nolo.adaptors.ItemsCompactAdaptor;
import com.example.nolo.adaptors.ListByCategoryAdaptor;
import com.example.nolo.entities.category.Category;
import com.example.nolo.entities.category.ICategory;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.fragments.HomeFragment;
import com.example.nolo.interactors.category.GetCategoriesUseCase;
import com.example.nolo.interactors.item.GetAllItemsUseCase;
import com.example.nolo.interactors.item.GetCategoryItemsUseCase;
import com.example.nolo.interactors.item.GetLaptopsGroupedByBrandUseCase;
import com.example.nolo.util.ListUtil;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ListActivity extends BaseActivity {
    private ViewHolder vh;

    private class ViewHolder {
        TextView categoryText;
        RecyclerView categoryItemsParentList;

        public ViewHolder(){
            categoryText = findViewById(R.id.category_text);
            categoryItemsParentList = findViewById(R.id.category_item_parent_list);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ICategory category = (Category) getIntent().getSerializableExtra(getString(R.string.extra_category));

        vh = new ViewHolder();



//        vh.categoryText.setText(items.toString());
        initAdaptors();
    }

    private void initAdaptors() {
        /**
         * FEATURED ITEMS ADAPTOR
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vh.categoryItemsParentList.setLayoutManager(layoutManager);

        List<List<IItem>> items = GetLaptopsGroupedByBrandUseCase.getLaptopsGroupedByBrand();

        ListByCategoryAdaptor categoryListAdaptor = new ListByCategoryAdaptor(this, items, CategoryType.laptops);
        vh.categoryItemsParentList.setAdapter(categoryListAdaptor);
    }
}