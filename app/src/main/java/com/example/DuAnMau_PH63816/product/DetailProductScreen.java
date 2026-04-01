package com.example.DuAnMau_PH63816.product;

import static com.example.DuAnMau_PH63816.common.OpenDatePicker.openDatePicker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.DuAnMau_PH63816.product.adapter.ProductAdapter;
import com.example.DuAnMau_PH63816.product.data.ProductDAO;
import com.example.DuAnMau_PH63816.product.model.Product;
import com.google.android.material.textfield.TextInputEditText;

public class DetailProductScreen extends AppCompatActivity {


    private ProductDAO productDAO;
    private int productId = -1;
    private String currentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_product_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        productDAO = new ProductDAO(this);
        bindDataFromIntent();
    }

    private void bindDataFromIntent() {
        Intent intent = getIntent();
        if (intent == null) return;

        ImageView imgProductCover = findViewById(R.id.imgProductCover);
        TextInputEditText edtProductName = findViewById(R.id.edtProductName);
        TextInputEditText edtProductId = findViewById(R.id.edtProductId);
        TextInputEditText edtPrice = findViewById(R.id.edtPrice);
        TextInputEditText edtStock = findViewById(R.id.edtStock);
        TextInputEditText edtDate = findViewById(R.id.edtDate);
        AutoCompleteTextView spnCategory = findViewById(R.id.spnCategory);
        AutoCompleteTextView spnUnit = findViewById(R.id.spnUnit);
        RadioButton rbSelling = findViewById(R.id.rbSelling);
        RadioButton rbOutOfStock = findViewById(R.id.rbOutOfStock);
        Button btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        Button btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
        Toolbar toolbarDetailProductScreen = findViewById(R.id.toolbarDetailProductScreen);

        if (toolbarDetailProductScreen != null) {
            setSupportActionBar(toolbarDetailProductScreen);
            toolbarDetailProductScreen.setNavigationOnClickListener(v -> finish());

        }
        BottomButtonNavigator.bindDefaultButtons(this, BottomButtonNavigator.TAB_PRODUCT);

        edtDate.setOnClickListener(v -> openDatePicker(this, edtDate));

        String name = intent.getStringExtra(ProductExtras.NAME);
        if (name != null && edtProductName != null) {
            edtProductName.setText(name);
        }

        productId = intent.getIntExtra(ProductExtras.ID, -1);
        if (productId != -1 && edtProductId != null) {
            edtProductId.setText(String.valueOf(productId));
            edtProductId.setEnabled(false);
        }

        String priceLabel = intent.getStringExtra(ProductExtras.PRICE);
        if (priceLabel != null && edtPrice != null) {
            edtPrice.setText(priceLabel);
        }

        String stockLabel = intent.getStringExtra(ProductExtras.STOCK);
        if (stockLabel != null && edtStock != null) {
            edtStock.setText(stripStockLabel(stockLabel));
        }
        String category = intent.getStringExtra(ProductExtras.CATEGORY);
        if (category != null && spnCategory != null) {
            spnCategory.setText(category, false);
        }
        String unit = intent.getStringExtra(ProductExtras.UNIT);
        if (unit != null && spnUnit != null) {
            spnUnit.setText(unit, false);
        }
        String date = intent.getStringExtra(ProductExtras.DATE);
        if (date != null) {
            edtDate.setText(date);
        }
        int status = intent.getIntExtra(ProductExtras.STATUS, 1);
        if (status == 1 && rbSelling != null) {
            rbSelling.setChecked(true);
        } else if (rbOutOfStock != null) {
            rbOutOfStock.setChecked(true);
        }

        currentImage = intent.getStringExtra(ProductExtras.IMAGE);
        ProductAdapter.bindProductImage(imgProductCover, currentImage);

        if (btnUpdateProduct != null) {
            btnUpdateProduct.setOnClickListener(v -> handleUpdate(
                    edtProductName,
                    edtPrice,
                    edtStock,
                    spnCategory,
                    spnUnit,
                    edtDate,
                    rbSelling
            ));
        }
        if (btnDeleteProduct != null) {
            btnDeleteProduct.setOnClickListener(v -> handleDelete());
        }
    }

    private void handleUpdate(
            TextInputEditText nameView,
            TextInputEditText priceView,
            TextInputEditText stockView,
            AutoCompleteTextView categoryView,
            AutoCompleteTextView unitView,
            TextInputEditText dateView,
            RadioButton sellingView
    ) {
        if (productId == -1 || productDAO == null) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = getTextValue(nameView);
        String price = getTextValue(priceView);
        String stock = getTextValue(stockView);
        String category = getTextValue(categoryView);
        String unit = getTextValue(unitView);
        String date = getTextValue(dateView);
        if (name.isEmpty() || price.isEmpty() || stock.isEmpty() || category.isEmpty() || unit.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }
        Product product = new Product();
        product.setId(productId);
        product.setName(name);
        product.setPriceLabel(price);
        product.setStockLabel(" · Tồn: " + stock);
        product.setImage(currentImage != null ? currentImage : "");
        product.setCategory(category);
        product.setUnit(unit);
        product.setDate(date);
        product.setStatus(sellingView != null && sellingView.isChecked() ? 1 : 0);
        boolean updated = productDAO.updateProduct(product);
        if (updated) {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleDelete() {
        if (productId == -1 || productDAO == null) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }
        Product product = new Product();
        product.setId(productId);
        if (productDAO.deleteProduct(product)) {
            Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private String stripStockLabel(String stockLabel) {
        if (stockLabel == null) return "";
        if (stockLabel.startsWith(" · Tồn: ")) {
            return stockLabel.substring(" · Tồn: ".length()).trim();
        }
        return stockLabel;
    }

    private String getTextValue(TextInputEditText editText) {
        if (editText == null || editText.getText() == null) return "";
        return editText.getText().toString().trim();
    }

    private String getTextValue(AutoCompleteTextView editText) {
        if (editText == null || editText.getText() == null) return "";
        return editText.getText().toString().trim();
    }

    @Override
    protected void onDestroy() {
        if (productDAO != null) {
            productDAO.close();
        }
        super.onDestroy();
    }
}
