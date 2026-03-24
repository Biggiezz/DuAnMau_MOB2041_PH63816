package com.example.DuAnMau_PH63816.category;

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
import com.example.DuAnMau_PH63816.category.data.CategoryDAO;

import java.util.ArrayList;

public class CategoryManagementScreen extends AppCompatActivity {

    private RecyclerView rvCategories;
    private AppCompatButton btnAddCategory;
    private TextView tvCategoryCountBadge;
    private final ArrayList<Category> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private CategoryDAO categoryDAO;

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
        setListeners();
    }

    private void initViews() {
        rvCategories = findViewById(R.id.rvCategories);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        tvCategoryCountBadge = findViewById(R.id.tvCategoryCountBadge);
        Toolbar toolbarCategoryManagementScreen = findViewById(R.id.toolbarCategoryManagementScreen);

        if (toolbarCategoryManagementScreen != null) {
            setSupportActionBar(toolbarCategoryManagementScreen);
            toolbarCategoryManagementScreen.setNavigationOnClickListener(v -> finish());
        }
    }

    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdapter(this, categoryList, new CategoryAdapter.OnCategoryActionListener() {
            @Override
            public void onEdit(Category category) {
                category.setName(category.getName() + " (Sửa)");
                if (categoryDAO.updateCategory(category)) {
                    Toast.makeText(CategoryManagementScreen.this, "Đã sửa " + category.getName(), Toast.LENGTH_SHORT).show();
                    loadCategories();
                }
            }

            @Override
            public void onDelete(Category category) {
                if (categoryDAO.deleteCategory(category)) {
                    Toast.makeText(CategoryManagementScreen.this, "Đã xóa " + category.getName(), Toast.LENGTH_SHORT).show();
                    loadCategories();
                }
            }
        });
        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        rvCategories.setAdapter(categoryAdapter);
        loadCategories();
    }

    private void loadCategories() {
        categoryList.clear();
        categoryList.addAll(categoryDAO.getAllCategories());
        tvCategoryCountBadge.setText(categoryList.size() + " Danh mục");
        categoryAdapter.notifyDataSetChanged();
    }

    private void setListeners() {

        btnAddCategory.setOnClickListener(v -> {
            Category newCategory = new Category("Danh mục mới", 0, R.drawable.btn_category);
            if (categoryDAO.insertCategory(newCategory)) {
                Toast.makeText(this, "Thêm danh mục mới", Toast.LENGTH_SHORT).show();
                loadCategories();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (categoryDAO != null) {
            categoryDAO.close();
        }
        super.onDestroy();
    }
}
