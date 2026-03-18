package com.example.DuAnMau_PH63816.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.product.adapter.ProductAdapter;
import com.example.DuAnMau_PH63816.product.model.Product;
import java.util.ArrayList;

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
        ListView lvProducts = findViewById(R.id.lvProducts);

        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("Mì Ramen Tonkotsu", "125.000k", " · Tồn: 10", R.drawable.ic_ramen));
        products.add(new Product("Kem Matcha Premium", "45.000k", " · Tồn: 12", R.drawable.ic_icream_matcha));
        products.add(new Product("Sushi Set Omakase", "850.000k", " · Tồn: 45", R.drawable.ic_set_sushi));

        ProductAdapter adapter = new ProductAdapter(this, products, product -> {
            Intent intent = new Intent(ProductScreen.this, DetailProductScreen.class);
            intent.putExtra("extra_product_name", product.getName());
            intent.putExtra("extra_product_price", product.getPriceLabel());
            intent.putExtra("extra_product_stock", product.getStockLabel());
            intent.putExtra("extra_product_image", product.getImage());
            startActivity(intent);
        });

        lvProducts.setAdapter(adapter);

        icBack.setOnClickListener(v -> finish());
        imgAddProduct.setOnClickListener(v -> startActivity(new Intent(ProductScreen.this, AddProductScreen.class)));
    }
}
