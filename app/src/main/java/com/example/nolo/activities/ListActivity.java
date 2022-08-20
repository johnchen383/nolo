package com.example.nolo.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nolo.R;
import com.example.nolo.entities.category.Category;
import com.example.nolo.entities.category.ICategory;
import com.example.nolo.fragments.HomeFragment;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

        vh.categoryText.setText(category.getCategoryName());
    }
}