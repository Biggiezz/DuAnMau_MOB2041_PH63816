package com.example.DuAnMau_PH63816.category;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.category.adapter.CategoryAdapter;
import com.example.DuAnMau_PH63816.category.dao.CategoryDAO;
import com.example.DuAnMau_PH63816.category.model.Category;

import java.util.ArrayList;

public class CategoryManagementScreen extends AppCompatActivity {

    private RecyclerView rvCategories;
    private TextView tvCategoryCountBadge;
    private final ArrayList<Category> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    CategoryDAO categoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_management_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        categoryDAO = new CategoryDAO(this);
        initViews();
        setupRecyclerView();
    }

    private void initViews() {
        rvCategories = findViewById(R.id.rvCategories);
        AppCompatButton btnAddCategory = findViewById(R.id.btnAddCategory);
        tvCategoryCountBadge = findViewById(R.id.tvCategoryCountBadge);
        Toolbar toolbarCategoryManagementScreen = findViewById(R.id.toolbarCategoryManagementScreen);
        if (toolbarCategoryManagementScreen != null) {
            setSupportActionBar(toolbarCategoryManagementScreen);
            toolbarCategoryManagementScreen.setNavigationOnClickListener(v -> finish());
        }
        btnAddCategory.setOnClickListener(v -> startActivity(new Intent(CategoryManagementScreen.this, AddCategoryManagementScreen.class)));
    }

    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdapter(this, categoryList, new CategoryAdapter.OnCategoryActionListener() {
            @Override
            public void onEdit(Category category) {
                Intent intent = new Intent(CategoryManagementScreen.this, EditCategoryManagementScreen.class);
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


        rvCategories.setLayoutManager(new LinearLayoutManager(this));
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
        categoryList.clear();
        categoryList.addAll(categoryDAO.getAllCategories());
        categoryAdapter.notifyDataSetChanged();
        if (tvCategoryCountBadge != null) {
            tvCategoryCountBadge.setText(
                    String.valueOf(" " + categoryList.size() + " danh mục "));
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        if (categoryDAO != null) {
            categoryDAO.close();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (categoryAdapter != null && categoryDAO != null) {
            loadCategories();
        }
    }
}
