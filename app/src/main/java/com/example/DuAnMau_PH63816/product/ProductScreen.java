package com.example.DuAnMau_PH63816.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.product.adapter.ProductAdapter;
import com.example.DuAnMau_PH63816.product.data.ProductDAO;
import com.example.DuAnMau_PH63816.product.model.Product;

import java.util.ArrayList;

public class ProductScreen extends AppCompatActivity {

    private ProductDAO productDAO;
    private final ArrayList<Product> products = new ArrayList<>();
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        productDAO = new ProductDAO(this);
        initUi();
    }

    private void initUi() {
        ImageView icBack = findViewById(R.id.icBack);
        ImageView imgAddProduct = findViewById(R.id.imgAddProduct);
        RecyclerView rvProducts = findViewById(R.id.rvProducts);

        adapter = new ProductAdapter(this, products, product -> {
            Intent intent = new Intent(ProductScreen.this, DetailProductScreen.class);
            intent.putExtra("extra_product_name", product.getName());
            intent.putExtra("extra_product_price", product.getPriceLabel());
            intent.putExtra("extra_product_stock", product.getStockLabel());
            intent.putExtra("extra_product_image", product.getImage());
            intent.putExtra("extra_product_id", product.getId());
            startActivity(intent);
        });

        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setAdapter(adapter);
        icBack.setOnClickListener(v -> finish());
        imgAddProduct.setOnClickListener(v -> startActivity(new Intent(ProductScreen.this, AddProductScreen.class)));

        loadProducts();
    }

    private void loadProducts() {
        if (productDAO == null || adapter == null) return;
        products.clear();
        products.addAll(productDAO.getAllProducts());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        if (productDAO != null) {
            productDAO.close();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }
}
