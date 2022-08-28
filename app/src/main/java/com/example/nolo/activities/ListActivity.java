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
import com.example.nolo.util.Display;
import com.example.nolo.util.ListUtil;
import com.example.nolo.viewmodels.IListViewModel;
import com.example.nolo.viewmodels.ListViewModel;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends BaseActivity {
    private IListViewModel listViewModel;
    private ViewHolder vh;

    private class ViewHolder {
        ListView categoryItemsParentList;
        ImageView categoryHeader, appleImage, androidImage;
        ImageButton backButtonImg;
        RelativeLayout backButton;
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
            backButtonImg = findViewById(R.id.back_btn_img);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ICategory category = (Category) getIntent().getSerializableExtra(getString(R.string.extra_category));
        listViewModel = new ListViewModel(category);

        vh = new ViewHolder();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_stationery, R.anim.slide_down);
    }

    private void init() {
        initStyling();
        initAdaptors();
        initListeners();
    }

    private void initListeners() {
        vh.backButton.setOnClickListener(v -> finish());
        vh.backButtonImg.setOnClickListener(v -> finish());
    }

    private void initPhoneListeners() {
        vh.androidBtn.setOnClickListener(v -> {
            vh.androidImage.setImageResource(R.drawable.os_button_android);
            vh.androidSelect.setTextColor(getColor(R.color.white));
            vh.appleImage.setImageResource(R.drawable.os_button_ios_dim);
            vh.appleSelect.setTextColor(getColor(R.color.navy));
            listViewModel.setPhoneOs(PhoneOs.android);
            initAdaptors();
        });

        vh.appleBtn.setOnClickListener(v -> {
            vh.androidImage.setImageResource(R.drawable.os_button_android_dim);
            vh.androidSelect.setTextColor(getColor(R.color.navy));
            vh.appleImage.setImageResource(R.drawable.os_button_ios);
            vh.appleSelect.setTextColor(getColor(R.color.white));
            listViewModel.setPhoneOs(PhoneOs.ios);
            initAdaptors();
        });

        vh.categoryItemsParentList.setDividerHeight(Display.dpToPx(-30, this));
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

    private void initAdaptors() {
        ListByCategoryAdaptor categoryListAdaptor;
        List<List<IItem>> items = new ArrayList<>();

        switch (listViewModel.getCategoryType()) {
            case laptops:
                items = listViewModel.getGroupedList(CategoryType.laptops);
                categoryListAdaptor = new ListByCategoryAdaptor(this, R.layout.item_list_laptop, items);
                break;
            case phones:
                items = listViewModel.getGroupedList(CategoryType.phones);
                categoryListAdaptor = new ListByCategoryAdaptor(this, R.layout.item_list_phone, items);
                break;
            case accessories:
                items = listViewModel.getGroupedList(CategoryType.accessories);
                categoryListAdaptor = new ListByCategoryAdaptor(this, R.layout.item_list_vertical, items);

                ColorDrawable whiteDivider = new ColorDrawable(getColor(R.color.faint_white));
                vh.categoryItemsParentList.setDivider(whiteDivider);
                vh.categoryItemsParentList.setDividerHeight(1);
                vh.categoryItemsParentList.setPadding(32, 32, 32, 32);
                break;
            default:
                System.err.println("No adaptor created for this category");
                items = listViewModel.getGroupedList(CategoryType.laptops);
                categoryListAdaptor = new ListByCategoryAdaptor(this, R.layout.item_list_laptop, items);
        }

        vh.categoryItemsParentList.setAdapter(categoryListAdaptor);
        ListUtil.setDynamicHeight(vh.categoryItemsParentList);
    }
}