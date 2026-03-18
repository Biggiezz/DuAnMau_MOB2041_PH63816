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

        String name = intent.getStringExtra("extra_product_name");
        if (name != null && edtProductName != null) {
            edtProductName.setText(name);
        }

        int imgRes = intent.getIntExtra("extra_product_image", 0);
        if (imgRes != 0 && imgProductCover != null) {
            imgProductCover.setImageResource(imgRes);
        }

        icBack.setOnClickListener(v -> finish());
    }
}
