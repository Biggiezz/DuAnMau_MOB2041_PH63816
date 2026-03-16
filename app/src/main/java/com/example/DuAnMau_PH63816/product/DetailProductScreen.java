package com.example.DuAnMau_PH63816.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;
import com.google.android.material.textfield.TextInputEditText;

public class DetailProductScreen extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_NAME = "extra_product_name";
    public static final String EXTRA_PRODUCT_PRICE = "extra_product_price";
    public static final String EXTRA_PRODUCT_STOCK = "extra_product_stock";
    public static final String EXTRA_PRODUCT_IMAGE = "extra_product_image";

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

        bindDataFromIntent();
    }

    private void bindDataFromIntent() {
        Intent intent = getIntent();
        if (intent == null) return;

        ImageView icBack = findViewById(R.id.icBack);
        ImageView imgProductCover = findViewById(R.id.imgProductCover);
        TextInputEditText edtProductName = findViewById(R.id.edtProductName);

        String name = intent.getStringExtra(EXTRA_PRODUCT_NAME);
        if (name != null && edtProductName != null) {
            edtProductName.setText(name);
        }

        int imgRes = intent.getIntExtra(EXTRA_PRODUCT_IMAGE, 0);
        if (imgRes != 0 && imgProductCover != null) {
            imgProductCover.setImageResource(imgRes);
        }

        icBack.setOnClickListener(v -> finish());
    }
}
