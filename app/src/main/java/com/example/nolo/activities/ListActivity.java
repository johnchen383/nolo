package com.example.nolo.activities;

import com.example.nolo.R;
import com.example.nolo.adaptors.ListByCategoryAdaptor;
import com.example.nolo.entities.category.Category;
import com.example.nolo.entities.category.ICategory;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.enums.PhoneOs;
import com.example.nolo.interactors.item.GetCategoryItemsUseCase;
import com.example.nolo.interactors.item.GetLaptopsGroupedByBrandUseCase;
import com.example.nolo.interactors.item.GetPhonesGroupedByOsUseCase;
import com.example.nolo.util.ListUtil;
import com.example.nolo.viewmodels.ListViewModel;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends BaseActivity {
    private ViewHolder vh;
    private ListViewModel listViewModel;

    private class ViewHolder {
        ListView categoryItemsParentList;
        ImageView categoryHeader, appleImage, androidImage;
        ImageButton backButton;
        LinearLayout phoneToggle, appleBtn, androidBtn;
        TextView appleSelect, androidSelect;

        public ViewHolder() {
            categoryItemsParentList = findViewById(R.id.category_item_parent_list);
            categoryHeader = findViewById(R.id.category_header);
            backButton = findViewById(R.id.back_btn);
            phoneToggle = findViewById(R.id.phone_toggle);
            appleBtn = findViewById(R.id.apple_btn);
            androidBtn = findViewById(R.id.android_btn);
            appleSelect = findViewById(R.id.apple_select);
            androidSelect = findViewById(R.id.android_select);
            appleImage = findViewById(R.id.apple_image);
            androidImage = findViewById(R.id.android_image);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ICategory category = (Category) getIntent().getSerializableExtra(getString(R.string.extra_category));
        listViewModel = new ListViewModel(category);

        vh = new ViewHolder();

        initStyling();
        establishAdaptor();
        initListeners();
    }

    private void initListeners() {
        vh.backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void initPhoneListeners() {
        vh.androidBtn.setOnClickListener(v -> {
            vh.androidImage.setImageResource(R.drawable.os_button_android);
            vh.androidSelect.setVisibility(View.VISIBLE);
            vh.appleImage.setImageResource(R.drawable.os_button_ios_dim);
            vh.appleSelect.setVisibility(View.INVISIBLE);
            listViewModel.setPhoneOs(PhoneOs.android);
            establishAdaptor();
        });

        vh.appleBtn.setOnClickListener(v -> {
            vh.androidImage.setImageResource(R.drawable.os_button_android_dim);
            vh.androidSelect.setVisibility(View.INVISIBLE);
            vh.appleImage.setImageResource(R.drawable.os_button_ios);
            vh.appleSelect.setVisibility(View.VISIBLE);
            listViewModel.setPhoneOs(PhoneOs.ios);
            establishAdaptor();
        });
    }

    private void initStyling() {
        ICategory category = listViewModel.getCategory();

        int i = this.getResources().getIdentifier(
                category.getImageUri() + getString(R.string.category_header_append), "drawable",
                this.getPackageName());

        if (category.getCategoryType().equals(CategoryType.phones)) {
            vh.phoneToggle.setVisibility(View.VISIBLE);
            initPhoneListeners();
        } else {
            vh.phoneToggle.setVisibility(View.GONE);
        }

        vh.categoryHeader.setImageResource(i);
    }

    private void establishAdaptor() {
        CategoryType categoryType = listViewModel.getCategory().getCategoryType();

        ListByCategoryAdaptor categoryListAdaptor;
        List<List<IItem>> items = new ArrayList<>();

        switch (categoryType) {
            case laptops:
                items = GetLaptopsGroupedByBrandUseCase.getLaptopsGroupedByBrand();
                categoryListAdaptor = new ListByCategoryAdaptor(this, R.layout.item_list_laptop, items);
                break;
            case phones:
                items = GetPhonesGroupedByOsUseCase.getPhonesGroupedByOs(listViewModel.getPhoneOs());
                categoryListAdaptor = new ListByCategoryAdaptor(this, R.layout.item_list_phone, items);
                break;
            case accessories:
                List<IItem> tempItems = GetCategoryItemsUseCase.getCategoryItems(CategoryType.accessories);
                for (IItem tempItem : tempItems) {
                    List<IItem> l = new ArrayList<>();
                    l.add(tempItem);
                    items.add(l);
                }
                categoryListAdaptor = new ListByCategoryAdaptor(this, R.layout.item_list_wide, items);

                ColorDrawable whiteDivider = new ColorDrawable(getColor(R.color.white));
                vh.categoryItemsParentList.setDivider(whiteDivider);
                vh.categoryItemsParentList.setDividerHeight(1);
                vh.categoryItemsParentList.setPadding(32, 32, 32, 32);
                break;
            default:
                System.err.println("No adaptor created for this category");
                items = GetLaptopsGroupedByBrandUseCase.getLaptopsGroupedByBrand();
                categoryListAdaptor = new ListByCategoryAdaptor(this, R.layout.item_list_laptop, items);
        }

        vh.categoryItemsParentList.setAdapter(categoryListAdaptor);
        ListUtil.setDynamicHeight(vh.categoryItemsParentList);
    }
}