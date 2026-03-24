package com.example.DuAnMau_PH63816.category.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.category.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    public interface OnCategoryActionListener {
        void onEdit(Category category);

        void onDelete(Category category);
    }

    private final Context context;
    private final List<Category> categoryList;
    private final OnCategoryActionListener listener;

    public CategoryAdapter(Context context, List<Category> categoryList, OnCategoryActionListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvCategoryName.setText(category.getName());
        holder.tvProductCount.setText(category.getProductCount() + " sản phẩm");
        holder.imgCategoryIcon.setImageResource(category.getIconResId());

        holder.imgEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(category);
        });

        holder.imgDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(category);
        });
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(category);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName, tvProductCount;
        ImageView imgCategoryIcon, imgEdit, imgDelete;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvProductCount = itemView.findViewById(R.id.tvProductCount);
            imgCategoryIcon = itemView.findViewById(R.id.imgCategoryIcon);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
