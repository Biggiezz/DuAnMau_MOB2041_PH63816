package com.example.DuAnMau_PH63816.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.product.adapter.ProductAdapter;
import com.example.DuAnMau_PH63816.product.data.ProductDAO;
import com.example.DuAnMau_PH63816.product.model.Product;
import com.google.android.material.textfield.TextInputEditText;

public class DetailProductScreen extends AppCompatActivity {

    private static final String STOCK_PREFIX = " · Tồn: ";

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

        ImageView icBack = findViewById(R.id.icBack);
        ImageView imgProductCover = findViewById(R.id.imgProductCover);
        TextInputEditText edtProductName = findViewById(R.id.edtProductName);
        TextInputEditText edtProductId = findViewById(R.id.edtProductId);
        TextInputEditText edtPrice = findViewById(R.id.edtPrice);
        TextInputEditText edtStock = findViewById(R.id.edtStock);
        Button btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        Button btnDeleteProduct = findViewById(R.id.btnDeleteProduct);

        String name = intent.getStringExtra("extra_product_name");
        if (name != null && edtProductName != null) {
            edtProductName.setText(name);
        }

        productId = intent.getIntExtra("extra_product_id", -1);
        if (productId != -1 && edtProductId != null) {
            edtProductId.setText(String.valueOf(productId));
            edtProductId.setEnabled(false);
        }

        String priceLabel = intent.getStringExtra("extra_product_price");
        if (priceLabel != null && edtPrice != null) {
            edtPrice.setText(priceLabel);
        }

        String stockLabel = intent.getStringExtra("extra_product_stock");
        if (stockLabel != null && edtStock != null) {
            edtStock.setText(stripStockLabel(stockLabel));
        }

        currentImage = intent.getStringExtra("extra_product_image");
        ProductAdapter.bindProductImage(imgProductCover, currentImage);

        if (btnUpdateProduct != null) {
            btnUpdateProduct.setOnClickListener(v -> handleUpdate(edtProductName, edtPrice, edtStock));
        }
        if (btnDeleteProduct != null) {
            btnDeleteProduct.setOnClickListener(v -> handleDelete());
        }

        icBack.setOnClickListener(v -> finish());
    }

    private void handleUpdate(TextInputEditText nameView, TextInputEditText priceView, TextInputEditText stockView) {
        if (productId == -1 || productDAO == null) {
            showToast("Không tìm thấy sản phẩm");
            return;
        }
        String name = getTextValue(nameView);
        String price = getTextValue(priceView);
        String stock = getTextValue(stockView);
        if (name.isEmpty() || price.isEmpty() || stock.isEmpty()) {
            showToast("Vui lòng điền tên, giá và tồn");
            return;
        }
        Product product = new Product();
        product.setId(productId);
        product.setName(name);
        product.setPriceLabel(price);
        product.setStockLabel(STOCK_PREFIX + stock);
        product.setImage(currentImage != null ? currentImage : "");
        boolean updated = productDAO.updateProduct(product);
        if (updated) {
            showToast("Cập nhật thành công");
            finish();
        } else {
            showToast("Cập nhật thất bại");
        }
    }

    private void handleDelete() {
        if (productId == -1 || productDAO == null) {
            showToast("Không tìm thấy sản phẩm");
            return;
        }
        Product product = new Product();
        product.setId(productId);
        if (productDAO.deleteProduct(product)) {
            showToast("Đã xóa sản phẩm");
            finish();
        } else {
            showToast("Xóa sản phẩm thất bại");
        }
    }

    private String stripStockLabel(String stockLabel) {
        if (stockLabel == null) return "";
        if (stockLabel.startsWith(STOCK_PREFIX)) {
            return stockLabel.substring(STOCK_PREFIX.length()).trim();
        }
        return stockLabel;
    }

    private String getTextValue(TextInputEditText editText) {
        if (editText == null || editText.getText() == null) return "";
        return editText.getText().toString().trim();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        if (productDAO != null) {
            productDAO.close();
        }
        super.onDestroy();
    }
}
