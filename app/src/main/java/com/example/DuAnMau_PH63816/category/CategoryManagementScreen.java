package com.example.DuAnMau_PH63816.category;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.DuAnMau_PH63816.R;
import java.util.ArrayList;

public class CategoryManagementScreen extends AppCompatActivity {

    private RecyclerView rvCategories;
    private AppCompatButton btnAddCategory;
    private ImageView icBack;
    private TextView tvCategoryCountBadge;

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

        initViews();
        setupRecyclerView();
        setListeners();
    }

    private void initViews() {
        rvCategories = findViewById(R.id.rvCategories);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        icBack = findViewById(R.id.icBack);
        tvCategoryCountBadge = findViewById(R.id.tvCategoryCountBadge);
    }

    private void setupRecyclerView() {
        ArrayList<Category> categoryList = new ArrayList<>();
        // Mock data from the UI mockup
        categoryList.add(new Category(1, "Thời trang nam", 120, R.drawable.btn_category));
        categoryList.add(new Category(2, "Thời trang nữ", 85, R.drawable.btn_category));
        categoryList.add(new Category(3, "Phụ kiện", 42, R.drawable.btn_category));
        categoryList.add(new Category(4, "Điện tử", 15, R.drawable.btn_category));
        categoryList.add(new Category(5, "Gia dụng", 67, R.drawable.btn_category));

        tvCategoryCountBadge.setText(categoryList.size() + " Danh mục");

        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categoryList);
        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        rvCategories.setAdapter(categoryAdapter);
    }

    private void setListeners() {
        icBack.setOnClickListener(v -> finish());

        btnAddCategory.setOnClickListener(v -> {
            Toast.makeText(this, "Thêm danh mục mới", Toast.LENGTH_SHORT).show();
            // Start Add Category Activity here
        });
    }
}
