package com.example.nolo.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nolo.R;
import com.example.nolo.fragments.HomeFragment;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ListActivity extends BaseActivity {
    private ViewHolder vh;

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

        vh = new ViewHolder();

        vh.categoryText.setText("accessories");
    }
}