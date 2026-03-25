package com.example.DuAnMau_PH63816.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
        Toolbar toolbarProductScreen = findViewById(R.id.toolbarProductScreen);
        ImageView imgAddProduct = findViewById(R.id.imgAddProduct);
        RecyclerView rvProducts = findViewById(R.id.rvProducts);

        if (toolbarProductScreen != null) {
            setSupportActionBar(toolbarProductScreen);
            toolbarProductScreen.setNavigationOnClickListener(v -> finish());
        }

        adapter = new ProductAdapter(this, products, product -> {
            Intent intent = new Intent(ProductScreen.this, DetailProductScreen.class);
            intent.putExtra(ProductExtras.NAME, product.getName());
            intent.putExtra(ProductExtras.PRICE, product.getPriceLabel());
            intent.putExtra(ProductExtras.STOCK, product.getStockLabel());
            intent.putExtra(ProductExtras.IMAGE, product.getImage());
            intent.putExtra(ProductExtras.ID, product.getId());
            intent.putExtra(ProductExtras.CATEGORY, product.getCategory());
            intent.putExtra(ProductExtras.UNIT, product.getUnit());
            intent.putExtra(ProductExtras.DATE, product.getDate());
            intent.putExtra(ProductExtras.STATUS, product.getStatus());
            startActivity(intent);
        });

        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setAdapter(adapter);
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
