package com.example.DuAnMau_PH63816.category.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.DuAnMau_PH63816.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryIconSpinnerAdapter extends ArrayAdapter<CategoryIconSpinnerAdapter.CategoryIconItem> {

    public static class CategoryIconItem {
        private final int iconResId;
        private final String label;

        public CategoryIconItem(int iconResId, String label) {
            this.iconResId = iconResId;
            this.label = label;
        }

        public int getIconResId() {
            return iconResId;
        }

        public String getLabel() {
            return label;
        }
    }

    private final LayoutInflater inflater;

    public CategoryIconSpinnerAdapter(@NonNull Context context, @NonNull List<CategoryIconItem> items) {
        super(context, 0, items);
        inflater = LayoutInflater.from(context);
    }

    public static List<CategoryIconItem> createDefaultItems() {
        List<CategoryIconItem> iconItems = new ArrayList<>();
        iconItems.add(new CategoryIconItem(R.drawable.btn_category, "Mac dinh"));
        iconItems.add(new CategoryIconItem(R.drawable.ic_product, "San pham"));
        iconItems.add(new CategoryIconItem(R.drawable.ic_ramen, "Mi ramen"));
        iconItems.add(new CategoryIconItem(R.drawable.ic_set_sushi, "Set sushi"));
        iconItems.add(new CategoryIconItem(R.drawable.ic_icream_matcha, "Kem matcha"));
        return iconItems;
    }

    public static int findPositionByIconResId(@NonNull List<CategoryIconItem> items, int iconResId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getIconResId() == iconResId) {
                return i;
            }
        }
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return bindView(position, convertView, parent, R.layout.item_category_icon_spinner_selected);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return bindView(position, convertView, parent, R.layout.item_category_icon_spinner_dropdown);
    }

    private View bindView(int position, @Nullable View convertView, @NonNull ViewGroup parent, int layoutResId) {
        View view = convertView != null ? convertView : inflater.inflate(layoutResId, parent, false);
        ImageView imageView = view.findViewById(R.id.imgCategoryIconOption);
        TextView textView = view.findViewById(R.id.tvCategoryIconOption);
        CategoryIconItem item = getItem(position);

        if (item != null) {
            imageView.setImageResource(item.getIconResId());
            textView.setText(item.getLabel());
        }
        return view;
    }
}
