package com.example.DuAnMau_PH63816.product;

import static com.example.DuAnMau_PH63816.common.OpenDatePicker.openDatePicker;

import android.os.Bundle;
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
    private RadioButton rbSelling;
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
        initUi();
        productDAO = new ProductDAO(this);
        setListener();
    }

    private void setListener() {
        edtDate.setOnClickListener(v -> {
            openDatePicker(this, edtDate);
        });
        btnAddProduct.setOnClickListener(v -> {
            String name = edtProductName.getText().toString().trim();
            String price = edtPrice.getText().toString().trim();
            String stock = edtStock.getText().toString().trim();
            String category = spnCategory.getText().toString().trim();
            String unit = spnUnit.getText().toString().trim();
            String date = edtDate.getText().toString().trim();
            if (name.isEmpty() || price.isEmpty() || stock.isEmpty() || category.isEmpty() || unit.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_LONG).show();
                return;
            }

            long normalizedPrice;
            int normalizedStock;
            try {
                normalizedPrice = parseNumericValue(price);
                normalizedStock = Integer.parseInt(stock);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá và số lượng tồn phải là số", Toast.LENGTH_LONG).show();
                return;
            }

            if (normalizedPrice <= 0 || normalizedStock < 0) {
                Toast.makeText(this, "Giá phải lớn hơn 0 và tồn kho không được âm", Toast.LENGTH_LONG).show();
                return;
            }

            Product product = new Product();
            product.setName(name);
            product.setPriceLabel(formatPriceLabel(normalizedPrice));
            product.setStockLabel(STOCK_PREFIX + normalizedStock);
            product.setImage(String.valueOf(R.drawable.ic_set_sushi));
            product.setCategory(category);
            product.setUnit(unit);
            product.setDate(date);
            product.setStatus(rbSelling.isChecked() ? 1 : 0);

            if (productDAO.insertProduct(product)) {
                Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm sản phẩm thất bại", Toast.LENGTH_LONG).show();
            }
        });
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
        btnAddProduct = findViewById(R.id.btnAddProduct);

        setupDropdowns();

        if (toolbarAddProductScreen != null) {
            setSupportActionBar(toolbarAddProductScreen);
            toolbarAddProductScreen.setNavigationOnClickListener(v -> finish());
        }
        BottomButtonNavigator.bindDefaultButtons(this, BottomButtonNavigator.TAB_PRODUCT);
    }

    private void setupDropdowns() {
        String[] categories = new String[]{"Ẩm thực", "Tráng miệng", "Đồ uống", "Gia vị"};
        String[] units = new String[]{"Khay", "Tô", "Ly", "Phần"};

        spnCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories));
        spnUnit.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, units));
    }

    private long parseNumericValue(String rawValue) {
        String digitsOnly = rawValue.replaceAll("[^0-9]", "");
        if (digitsOnly.isEmpty()) {
            throw new NumberFormatException("Empty value");
        }
        return Long.parseLong(digitsOnly);
    }

    private String formatPriceLabel(long price) {
        return NumberFormat.getNumberInstance(Locale.GERMANY).format(price) + "k";
    }
}
