package com.example.nolo.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nolo.R;
import com.example.nolo.entities.category.Category;
import com.example.nolo.entities.category.ICategory;
import com.example.nolo.fragments.HomeFragment;
import com.example.nolo.util.Animation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ListActivity extends BaseActivity {
    private ViewHolder vh;

    private class ViewHolder {
        TextView categoryText;
        Button detailsBtn;

        public ViewHolder(){
            categoryText = findViewById(R.id.category_text);
            detailsBtn = findViewById(R.id.details_button);
        }
    }

    private void initListeners() {
        vh.detailsBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, DetailsActivity.class), Animation.Fade(this).toBundle());
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ICategory category = (Category) getIntent().getSerializableExtra(getString(R.string.extra_category));

        vh = new ViewHolder();
        initListeners();

        vh.categoryText.setText(category.getCategoryName());
    }
}