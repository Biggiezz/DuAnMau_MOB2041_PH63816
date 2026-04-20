package com.example.DuAnMau_PH63816.category.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.category.CategoryExtras;
import com.example.DuAnMau_PH63816.category.EditCategoryManagementScreen;
import com.example.DuAnMau_PH63816.category.dao.CategoryDAO;
import com.example.DuAnMau_PH63816.category.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final Context context;
    private final ArrayList<Category> categoryList;
    private final CategoryDAO categoryDAO;
    private final TextView tvCategoryCountBadge;

    public CategoryAdapter(Context context, List<Category> categoryList, CategoryDAO categoryDAO, TextView tvCategoryCountBadge) {
        this.context = context;
        if (categoryList instanceof ArrayList) {
            this.categoryList = (ArrayList<Category>) categoryList;
        } else {
            this.categoryList = new ArrayList<>(categoryList);
        }
        this.categoryDAO = categoryDAO;
        this.tvCategoryCountBadge = tvCategoryCountBadge;
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

        holder.imgEdit.setOnClickListener(v -> openEditCategory(category));
        holder.imgDelete.setOnClickListener(v -> deleteCategory(holder.getBindingAdapterPosition()));
        holder.itemView.setOnClickListener(v -> openEditCategory(category));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    private void openEditCategory(Category category) {
        Intent intent = new Intent(context, EditCategoryManagementScreen.class);
        intent.putExtra(CategoryExtras.ID, category.getId());
        intent.putExtra(CategoryExtras.NAME, category.getName());
        intent.putExtra(CategoryExtras.PRODUCT_COUNT, category.getProductCount());
        intent.putExtra(CategoryExtras.ICON, category.getIconResId());
        intent.putExtra(CategoryExtras.DESCRIBE, category.getDescribe());
        context.startActivity(intent);
    }

    private void deleteCategory(int position) {
        if (position == RecyclerView.NO_POSITION || position >= categoryList.size()) {
            return;
        }

        Category category = categoryList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa danh mục " + category.getName());
        builder.setMessage("Bạn có chắc chắn muốn xóa danh mục này?");
        builder.setPositiveButton("Có", (dialog, which) -> {
            if (categoryDAO.deleteCategory(category)) {
                categoryList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, categoryList.size() - position);
                updateCategoryCount();
                Toast.makeText(context, "Đã xóa danh mục", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Xóa danh mục thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
        builder.show();

    }

    private void updateCategoryCount() {
        if (tvCategoryCountBadge != null) {
            tvCategoryCountBadge.setText(" " + categoryList.size() + " danh mục ");
        }
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
