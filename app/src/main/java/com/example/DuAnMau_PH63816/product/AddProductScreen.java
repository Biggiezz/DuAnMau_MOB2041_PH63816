package com.example.DuAnMau_PH63816.product;

import static com.example.DuAnMau_PH63816.common.OpenDatePicker.openDatePicker;

import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.common.BottomButtonNavigator;
import com.example.DuAnMau_PH63816.product.data.ProductDAO;
import com.example.DuAnMau_PH63816.product.model.Product;

import java.text.NumberFormat;
import java.util.Locale;

public class AddProductScreen extends AppCompatActivity {

    private static final String STOCK_PREFIX = " · Tồn: ";

    private EditText edtDate, edtProductName, edtPrice, edtStock;
    private AutoCompleteTextView spnCategory, spnUnit;
    private RadioButton rbSelling, rbOutOfStock;
    private Button btnAddProduct;
    private ProductDAO productDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        productDAO = new ProductDAO(this);
        initUi();
        setupDropdowns();
    }

    private void initUi() {
        Toolbar toolbarAddProductScreen = findViewById(R.id.toolbarAddProductScreen);
        edtDate = findViewById(R.id.edtDate);
        edtProductName = findViewById(R.id.edtProductName);
        edtPrice = findViewById(R.id.edtPrice);
        edtStock = findViewById(R.id.edtStock);
        spnCategory = findViewById(R.id.spnCategory);
        spnUnit = findViewById(R.id.spnUnit);
        rbSelling = findViewById(R.id.rbSelling);
        rbOutOfStock = findViewById(R.id.rbOutOfStock);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        setSupportActionBar(toolbarAddProductScreen);
        toolbarAddProductScreen.setNavigationOnClickListener(v -> finish());
        BottomButtonNavigator.bindDefaultButtons(this, BottomButtonNavigator.TAB_PRODUCT);
        edtDate.setOnClickListener(v -> openDatePicker(this, edtDate));
        btnAddProduct.setOnClickListener(v -> saveProduct());
    }

    private void setupDropdowns() {
        String[] categories = new String[]{"Ẩm thực", "Tráng miệng", "Đồ uống", "Topping", "Khai vị", "Ăn vặt"};
        String[] units = new String[]{"Khay", "Tô", "Ly", "Phần", "Chai", "Đĩa"};

        spnCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories));
        spnUnit.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, units));
        spnCategory.setInputType(InputType.TYPE_NULL);
        spnUnit.setInputType(InputType.TYPE_NULL);
        spnCategory.setKeyListener(null);
        spnUnit.setKeyListener(null);
        spnCategory.setOnClickListener(v -> spnCategory.showDropDown());
        spnUnit.setOnClickListener(v -> spnUnit.showDropDown());
        spnCategory.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) spnCategory.showDropDown();
        });
        spnUnit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) spnUnit.showDropDown();
        });
    }

    private void saveProduct() {
        String name = edtProductName.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();
        String stock = edtStock.getText().toString().trim();
        String category = spnCategory.getText().toString().trim();
        String unit = spnUnit.getText().toString().trim();
        String date = edtDate.getText().toString().trim();

        if (name.isEmpty() || price.isEmpty() || stock.isEmpty() || category.isEmpty() || unit.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        long normalizedPrice;
        int normalizedStock;
        try {
            normalizedPrice = parsePrice(price);
            normalizedStock = Integer.parseInt(stock);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá và số lượng tồn phải là số", Toast.LENGTH_SHORT).show();
            return;
        }

        if (normalizedPrice <= 0 || normalizedStock < 0) {
            Toast.makeText(this, "Giá phải lớn hơn 0 và tồn kho không được âm", Toast.LENGTH_SHORT).show();
            return;
        }

        Product product = new Product();
        product.setName(name);
        product.setPriceLabel(formatPriceLabel(normalizedPrice));
        product.setStockLabel(STOCK_PREFIX + normalizedStock);
        product.setImage(ProductImageResolver.DEFAULT_IMAGE_NAME);
        product.setCategory(category);
        product.setUnit(unit);
        product.setDate(date);
        product.setStatus(rbSelling.isChecked() ? 1 : 0);

        if (!rbSelling.isChecked() && !rbOutOfStock.isChecked()) {
            product.setStatus(1);
        }

        if (productDAO.insertProduct(product)) {
            Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private long parsePrice(String rawValue) {
        String digitsOnly = rawValue.replaceAll("[^0-9]", "");
        if (digitsOnly.isEmpty()) {
            throw new NumberFormatException("Empty value");
        }
        return Long.parseLong(digitsOnly);
    }

    private String formatPriceLabel(long price) {
        return NumberFormat.getNumberInstance(Locale.GERMANY).format(price) + "k";
    }

    @Override
    protected void onDestroy() {
        if (productDAO != null) {
            productDAO.close();
        }
        super.onDestroy();
    }
}
