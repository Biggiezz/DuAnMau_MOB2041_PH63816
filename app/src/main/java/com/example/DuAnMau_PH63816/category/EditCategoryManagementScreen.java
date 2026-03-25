package com.example.DuAnMau_PH63816.category;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.category.adapter.CategoryIconSpinnerAdapter;
import com.example.DuAnMau_PH63816.category.dao.CategoryDAO;
import com.example.DuAnMau_PH63816.category.model.Category;

import java.util.List;

public class EditCategoryManagementScreen extends AppCompatActivity {

    private CategoryDAO categoryDAO;
    private EditText edtNameCategory, edtCodeCategory, edtCountCategory, edtDescribeCategory;
    private int currentCategoryIconResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_category_management_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
        categoryDAO = new CategoryDAO(this);
    }

    private void initUi() {
        Intent intent = getIntent();
        if (intent == null) return;

        edtNameCategory = findViewById(R.id.edtNameCategory);
        edtCodeCategory = findViewById(R.id.edtCodeCategory);
        edtCountCategory = findViewById(R.id.edtCountCategory);
        edtDescribeCategory = findViewById(R.id.edtDescribeCategory);
        Spinner spinnerCategoryIcon = findViewById(R.id.spinnerCategoryIcon);
        Button btnUpdateCategory = findViewById(R.id.btnUpdateCategory);
        Toolbar toolbarAddCategoryManagementScreen = findViewById(R.id.toolbarAddCategoryManagementScreen);

        String categoryName = intent.getStringExtra(CategoryExtras.NAME);
        if (categoryName != null && edtNameCategory != null) {
            edtNameCategory.setText(categoryName);
        }
        int categoryId = intent.getIntExtra(CategoryExtras.ID, -1);
        if (categoryId != -1) {
            edtCodeCategory.setText(String.valueOf(categoryId));
        }
        int categoryCount = intent.getIntExtra(CategoryExtras.PRODUCT_COUNT, -1);
        if (categoryCount != -1) {
            edtCountCategory.setText(String.valueOf(categoryCount));
        }
        String categoryDescribe = intent.getStringExtra(CategoryExtras.DESCRIBE);
        if (categoryDescribe != null && edtDescribeCategory != null) {
            edtDescribeCategory.setText(categoryDescribe);
        }
        currentCategoryIconResId = intent.getIntExtra(CategoryExtras.ICON, R.drawable.btn_category);
        setupIconSpinner(spinnerCategoryIcon, currentCategoryIconResId);
        if (toolbarAddCategoryManagementScreen != null) {
            setSupportActionBar(toolbarAddCategoryManagementScreen);
            toolbarAddCategoryManagementScreen.setNavigationOnClickListener(v -> finish());
        }

        btnUpdateCategory.setOnClickListener(v -> {
            String updateName = edtNameCategory.getText().toString().trim();
            String updateCode = edtCodeCategory.getText().toString().trim();
            String updateCount = edtCountCategory.getText().toString().trim();
            String updateDescribe = edtDescribeCategory.getText().toString().trim();
            if (updateName.isEmpty() || updateCode.isEmpty() || updateCount.isEmpty() || updateDescribe.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên, mã, số lượng, và mô tả của danh mục", Toast.LENGTH_LONG).show();
                return;
            }

            int updatedCategoryId;
            int updatedCategoryCount;
            try {
                updatedCategoryId = Integer.parseInt(updateCode);
                updatedCategoryCount = Integer.parseInt(updateCount);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Mã danh mục và số lượng phải là số", Toast.LENGTH_LONG).show();
                return;
            }

            Category category = new Category();
            category.setName(updateName);
            category.setId(updatedCategoryId);
            category.setProductCount(updatedCategoryCount);
            category.setDescribe(updateDescribe);
            category.setIconResId(currentCategoryIconResId);

            if (categoryDAO.updateCategory(category)) {
                Toast.makeText(this, "Cập nhật danh mục thành công", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật danh mục thất bại", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupIconSpinner(Spinner spinner, int preselectedIconResId) {
        if (spinner == null) {
            return;
        }

        List<CategoryIconSpinnerAdapter.CategoryIconItem> iconItems = CategoryIconSpinnerAdapter.createDefaultItems();
        CategoryIconSpinnerAdapter adapter = new CategoryIconSpinnerAdapter(this, iconItems);
        spinner.setAdapter(adapter);

        int selectedPosition = CategoryIconSpinnerAdapter.findPositionByIconResId(iconItems, preselectedIconResId);
        currentCategoryIconResId = iconItems.get(selectedPosition).getIconResId();
        spinner.setSelection(selectedPosition, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                CategoryIconSpinnerAdapter.CategoryIconItem selectedItem = (CategoryIconSpinnerAdapter.CategoryIconItem) parent.getItemAtPosition(position);
                currentCategoryIconResId = selectedItem.getIconResId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
