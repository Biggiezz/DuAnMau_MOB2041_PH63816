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
        TextView tvCategoryCountBadge = findViewById(R.id.tvCategoryCountBadge);
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
                intent.putExtra("extra_category_name", category.getName());
                intent.putExtra("extra_category_code", category.getId());
                intent.putExtra("extra_category_count", category.getProductCount());
                intent.putExtra("extra_category_icon", category.getIconResId());
                intent.putExtra("extra_category_describe", category.getDescribe());
                startActivity(intent);
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
        categoryAdapter.notifyDataSetChanged();
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
