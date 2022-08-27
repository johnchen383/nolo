package com.example.nolo.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nolo.R;
import com.example.nolo.activities.DetailsActivity;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.util.Animation;
import com.example.nolo.util.Keyboard;

import java.util.List;

public class SearchItemSuggestionAdaptor extends ArrayAdapter {
    private int mLayoutID;
    private List<IItem> mItems;
    private Context mContext;
    private String searchTerm, promptColour, normalColour;

    private class ViewHolder {
        TextView searchSuggestText;

        public ViewHolder(View currentListViewItem) {
            searchSuggestText = currentListViewItem.findViewById(R.id.search_suggestion_item_text);
        }
    }

    public SearchItemSuggestionAdaptor(@NonNull Context context, int resource, @NonNull List<IItem> objects,
                                       String searchTerm, String promptColour, String normalColour) {
        super(context, resource, objects);
        mLayoutID = resource;
        mContext = context;
        mItems = objects;
        this.searchTerm = searchTerm;
        this.promptColour = promptColour;
        this.normalColour = normalColour;
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

        IItem currentItem = mItems.get(position);

        return populateItemListItem(currentItem, currentListViewItem);
    }

    private View populateItemListItem(IItem currentItem, View currentListViewItem) {
        ViewHolder vh = new ViewHolder(currentListViewItem);

        vh.searchSuggestText.setText(Html.fromHtml(getPromptedText(currentItem.getName()), Html.FROM_HTML_MODE_LEGACY));

        vh.searchSuggestText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Keyboard.hide((Activity) mContext, v);

                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra(mContext.getString(R.string.extra_item_variant), (ItemVariant) currentItem.getDefaultItemVariant());
                mContext.startActivity(intent);
            }
        });

        return currentListViewItem;
    }

    /**
     * Highlight the search term in search suggestions
     *
     * @param itemName Original item name
     * @return Highlighted item name
     */
    private String getPromptedText(String itemName) {
        int startIndex, endIndex;
        String substring1, substring2, substring3;

        startIndex = itemName.toLowerCase().indexOf(searchTerm.toLowerCase());
        endIndex = startIndex + searchTerm.length();

        substring1 = itemName.substring(0, startIndex);
        substring2 = itemName.substring(startIndex, endIndex);
        substring3 = itemName.substring(endIndex);

        return "<font color=" + normalColour + ">" + substring1 + "</font>"
                + "<font color=" + promptColour + ">" + substring2 + "</font>"
                + "<font color=" + normalColour + ">" + substring3 + "</font>";
    }
}
