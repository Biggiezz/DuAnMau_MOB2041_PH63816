package com.example.DuAnMau_PH63816.category;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.category.dao.CategoryDAO;
import com.example.DuAnMau_PH63816.category.model.Category;

public class AddCategoryManagementScreen extends AppCompatActivity {
    private CategoryDAO categoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_category_management_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
        categoryDAO = new CategoryDAO(this);
    }

    private void initUi() {
        Toolbar toolbarAddCategory = findViewById(R.id.toolbarAddCategoryManagementScreen);
        EditText edtNameCategory = findViewById(R.id.edtNameCategory);
        EditText edtCodeCategory = findViewById(R.id.edtCodeCategory);
        EditText edtCountCategory = findViewById(R.id.edtCountCategory);
        EditText edtDescribeCategory = findViewById(R.id.edtDescribeCategory);
        Button btnAddCategory = findViewById(R.id.btnAddCategory);
        if (toolbarAddCategory != null) {
            setSupportActionBar(toolbarAddCategory);
            toolbarAddCategory.setNavigationOnClickListener(v -> finish());
        }
        btnAddCategory.setOnClickListener(v -> {
            String name = edtNameCategory.getText().toString().trim();
            String code = edtCodeCategory.getText().toString().trim();
            String count = edtCountCategory.getText().toString().trim();
            String describe = edtDescribeCategory.getText().toString().trim();
            if (name.isEmpty() || code.isEmpty() || count.isEmpty() || describe.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên, mã, số lượng và mô tả", Toast.LENGTH_LONG).show();
                return;
            }
            if (categoryDAO.isCategoryIdExists(code)) {
                Toast.makeText(this, "Mã danh mục đã tồn tại", Toast.LENGTH_LONG).show();
                return;
            }
            int categoryId;
            int productCount;
            try {
                categoryId = Integer.parseInt(code);
                productCount = Integer.parseInt(count);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Mã danh mục và số lượng phải là số", Toast.LENGTH_LONG).show();
                return;
            }

            Category category = new Category();
            category.setId(categoryId);
            category.setName(name);
            category.setProductCount(productCount);
            category.setIconResId(R.drawable.btn_category);
            category.setDescribe(describe);
            if (categoryDAO.insertCategory(category)) {
                Toast.makeText(this, "Thêm danh mục thành công", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm danh mục thất bại", Toast.LENGTH_LONG).show();
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
