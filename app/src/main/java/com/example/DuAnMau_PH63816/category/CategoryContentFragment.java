package com.example.DuAnMau_PH63816.category;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.category.adapter.CategoryAdapter;
import com.example.DuAnMau_PH63816.category.dao.CategoryDAO;
import com.example.DuAnMau_PH63816.category.model.Category;

import java.util.ArrayList;

public class CategoryContentFragment extends Fragment {

    private RecyclerView rvCategories;
    private TextView tvCategoryCountBadge;
    private final ArrayList<Category> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private CategoryDAO categoryDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryDAO = new CategoryDAO(requireContext());
        initViews(view);
        setupRecyclerView();
    }

    private void initViews(View view) {
        rvCategories = view.findViewById(R.id.rvCategories);
        AppCompatButton btnAddCategory = view.findViewById(R.id.btnAddCategory);
        tvCategoryCountBadge = view.findViewById(R.id.tvCategoryCountBadge);
        btnAddCategory.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), AddCategoryManagementScreen.class)));
    }

    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdapter(requireContext(), categoryList, new CategoryAdapter.OnCategoryActionListener() {
            @Override
            public void onEdit(Category category) {
                Intent intent = new Intent(requireContext(), EditCategoryManagementScreen.class);
                intent.putExtra(CategoryExtras.ID, category.getId());
                intent.putExtra(CategoryExtras.NAME, category.getName());
                intent.putExtra(CategoryExtras.PRODUCT_COUNT, category.getProductCount());
                intent.putExtra(CategoryExtras.ICON, category.getIconResId());
                intent.putExtra(CategoryExtras.DESCRIBE, category.getDescribe());
                startActivity(intent);
            }

            @Override
            public void onDelete(Category category) {
                handleDelete(category);
            }
        });

        rvCategories.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvCategories.setAdapter(categoryAdapter);
        loadCategories();
    }

    private void handleDelete(Category category) {
        if (categoryDAO == null) {
            showToast("Không tìm thấy danh mục");
            return;
        }
        if (categoryDAO.deleteCategory(category)) {
            loadCategories();
            showToast("Đã xóa danh mục");
        } else {
            showToast("Xóa danh mục thất bại");
        }
    }

    private void loadCategories() {
        if (categoryDAO == null || categoryAdapter == null) return;
        categoryList.clear();
        categoryList.addAll(categoryDAO.getAllCategories());
        categoryAdapter.notifyDataSetChanged();
        if (tvCategoryCountBadge != null) {
            tvCategoryCountBadge.setText(" " + categoryList.size() + " danh mục ");
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCategories();
    }

    @Override
    public void onDestroyView() {
        if (categoryDAO != null) {
            categoryDAO.close();
            categoryDAO = null;
        }
        categoryAdapter = null;
        super.onDestroyView();
    }
}
