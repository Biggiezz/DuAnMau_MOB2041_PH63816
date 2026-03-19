package com.example.DuAnMau_PH63816.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.DuAnMau_PH63816.R;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final Context context;
    private final List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
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

        holder.imgEdit.setOnClickListener(v -> Toast.makeText(context, "Sửa " + category.getName(), Toast.LENGTH_SHORT).show());

        holder.imgDelete.setOnClickListener(v -> Toast.makeText(context, "Xóa " + category.getName(), Toast.LENGTH_SHORT).show());
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
