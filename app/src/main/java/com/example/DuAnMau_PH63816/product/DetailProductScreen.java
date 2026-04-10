package com.example.DuAnMau_PH63816.product;

import static com.example.DuAnMau_PH63816.common.OpenDatePicker.openDatePicker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.common.BottomButtonNavigator;
import com.example.DuAnMau_PH63816.product.adapter.ProductAdapter;
import com.example.DuAnMau_PH63816.product.data.ProductDAO;
import com.example.DuAnMau_PH63816.product.model.Product;
import com.google.android.material.textfield.TextInputEditText;

public class DetailProductScreen extends AppCompatActivity {


    private ProductDAO productDAO;
    private int productId = -1;
    private String currentImage = "";

    private Toolbar toolbarDetailProductScreen;
    private ImageView imgProductCover;
    private TextInputEditText edtProductName, edtProductId, edtPrice, edtStock, edtDate;
    private AutoCompleteTextView spnCategory, spnUnit;
    private RadioButton rbSelling, rbOutOfStock;
    private Button btnUpdateProduct, btnDeleteProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product_screen);
        initUi();
        productDAO = new ProductDAO(this);
        setSupportActionBar(toolbarDetailProductScreen);
        toolbarDetailProductScreen.setNavigationOnClickListener(v -> finish());
        BottomButtonNavigator.bindDefaultButtons(this, BottomButtonNavigator.TAB_PRODUCT);

        edtDate.setOnClickListener(v -> openDatePicker(this, edtDate));

        Intent intent = getIntent();
        String name = intent.getStringExtra(ProductExtras.NAME);
        String price = intent.getStringExtra(ProductExtras.PRICE);
        String stock = intent.getStringExtra(ProductExtras.STOCK);
        String category = intent.getStringExtra(ProductExtras.CATEGORY);
        String unit = intent.getStringExtra(ProductExtras.UNIT);
        String date = intent.getStringExtra(ProductExtras.DATE);
        int status = intent.getIntExtra(ProductExtras.STATUS, 1);

        productId = intent.getIntExtra(ProductExtras.ID, -1);
        if (productId != -1) {
            edtProductId.setText(String.valueOf(productId));
            edtProductId.setEnabled(false);
        }

        if (name != null) {
            edtProductName.setText(name);
        }
        if (price != null) {
            edtPrice.setText(price);
        }
        if (stock != null) {
            if (stock.startsWith(" · Tồn: ")) {
                edtStock.setText(stock.substring(" · Tồn: ".length()).trim());
            } else {
                edtStock.setText(stock);
            }
        }
        if (category != null) {
            spnCategory.setText(category, false);
        }
        if (unit != null) {
            spnUnit.setText(unit, false);
        }
        if (date != null) {
            edtDate.setText(date);
        }
        if (status == 1) {
            rbSelling.setChecked(true);
        } else {
            rbOutOfStock.setChecked(true);
        }

        currentImage = ProductImageResolver.normalizeForStorage(this, name, intent.getStringExtra(ProductExtras.IMAGE));
        ProductAdapter.bindProductImage(imgProductCover, currentImage);

        initListener();
    }

    private void initListener() {
        btnUpdateProduct.setOnClickListener(v -> {
            if (productId == -1) {
                Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }

            String productName = edtProductName.getText().toString().trim();
            String productPrice = edtPrice.getText().toString().trim();
            String productStock = edtStock.getText().toString().trim();
            String productCategory = spnCategory.getText().toString().trim();
            String productUnit = spnUnit.getText().toString().trim();
            String productDate = edtDate.getText().toString().trim();

            if (productName.isEmpty() || productPrice.isEmpty() || productStock.isEmpty()
                    || productCategory.isEmpty() || productUnit.isEmpty() || productDate.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }

            Product product = new Product();
            product.setId(productId);
            product.setName(productName);
            product.setPriceLabel(productPrice);
            product.setStockLabel(" · Tồn: " + productStock);
            product.setImage(currentImage);
            product.setCategory(productCategory);
            product.setUnit(productUnit);
            product.setDate(productDate);
            product.setStatus(rbSelling.isChecked() ? 1 : 0);

            if (productDAO.updateProduct(product)) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        btnDeleteProduct.setOnClickListener(v -> {
            if (productId == -1) {
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
        });
    }

    private void initUi() {
        toolbarDetailProductScreen = findViewById(R.id.toolbarDetailProductScreen);
        imgProductCover = findViewById(R.id.imgProductCover);
        edtProductName = findViewById(R.id.edtProductName);
        edtProductId = findViewById(R.id.edtProductId);
        edtPrice = findViewById(R.id.edtPrice);
        edtStock = findViewById(R.id.edtStock);
        edtDate = findViewById(R.id.edtDate);
        spnCategory = findViewById(R.id.spnCategory);
        spnUnit = findViewById(R.id.spnUnit);
        rbSelling = findViewById(R.id.rbSelling);
        rbOutOfStock = findViewById(R.id.rbOutOfStock);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
    }

    @Override
    protected void onDestroy() {
        if (productDAO != null) {
            productDAO.close();
        }
        super.onDestroy();
    }
}
