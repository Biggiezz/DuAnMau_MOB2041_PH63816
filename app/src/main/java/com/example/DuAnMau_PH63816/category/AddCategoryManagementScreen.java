package com.example.DuAnMau_PH63816.category;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.category.adapter.CategoryIconSpinnerAdapter;
import com.example.DuAnMau_PH63816.category.dao.CategoryDAO;
import com.example.DuAnMau_PH63816.category.model.Category;

import java.util.List;

public class AddCategoryManagementScreen extends AppCompatActivity {
    private CategoryDAO categoryDAO;
    private int selectedIconResId = R.drawable.btn_category;

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
        Spinner spinnerCategoryIcon = findViewById(R.id.spinnerCategoryIcon);
        Button btnAddCategory = findViewById(R.id.btnAddCategory);
        setupIconSpinner(spinnerCategoryIcon, selectedIconResId);
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
            category.setIconResId(selectedIconResId);
            category.setDescribe(describe);
            if (categoryDAO.insertCategory(category)) {
                Toast.makeText(this, "Thêm danh mục thành công", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm danh mục thất bại", Toast.LENGTH_LONG).show();
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
        selectedIconResId = iconItems.get(selectedPosition).getIconResId();
        spinner.setSelection(selectedPosition, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                CategoryIconSpinnerAdapter.CategoryIconItem selectedItem = (CategoryIconSpinnerAdapter.CategoryIconItem) parent.getItemAtPosition(position);
                selectedIconResId = selectedItem.getIconResId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
