package com.example.DuAnMau_PH63816.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.product.adapter.ProductAdapter;
import com.example.DuAnMau_PH63816.product.model.Product;

import java.util.Arrays;
import java.util.List;

public class ProductScreen extends AppCompatActivity {

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
        initUi();
    }

    private void initUi() {
        ImageView icBack = findViewById(R.id.icBack);
        ImageView imgAddProduct = findViewById(R.id.imgAddProduct);
        RecyclerView rvProducts = findViewById(R.id.rvProducts);

        List<Product> products = Arrays.asList(
                new Product(
                        getString(R.string.product_card_ramen_title),
                        getString(R.string.product_card_ramen_subtitle),
                        getString(R.string.product_card_ramen_subtitle2),
                        R.drawable.ic_ramen),
                new Product(
                        getString(R.string.product_card_icream_title),
                        getString(R.string.product_card_icream_subtitle),
                        getString(R.string.product_card_icream_subtitle2),
                        R.drawable.ic_icream_matcha),
                new Product(
                        getString(R.string.product_card_sushi_title),
                        getString(R.string.product_card_sushi_subtitle),
                        getString(R.string.product_card_sushi_subtitle2),
                        R.drawable.ic_set_sushi)
        );

        ProductAdapter adapter = new ProductAdapter(products, product -> {
            Intent intent = new Intent(ProductScreen.this, DetailProductScreen.class);
            intent.putExtra(DetailProductScreen.EXTRA_PRODUCT_NAME, product.getName());
            intent.putExtra(DetailProductScreen.EXTRA_PRODUCT_PRICE, product.getPriceLabel());
            intent.putExtra(DetailProductScreen.EXTRA_PRODUCT_STOCK, product.getStockLabel());
            intent.putExtra(DetailProductScreen.EXTRA_PRODUCT_IMAGE, product.getImageRes());
            startActivity(intent);
        });

        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setAdapter(adapter);

        icBack.setOnClickListener(v -> finish());
        imgAddProduct.setOnClickListener(v -> Toast.makeText(this, R.string.toast_notification, Toast.LENGTH_SHORT).show());
    }
}
