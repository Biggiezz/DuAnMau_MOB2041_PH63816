package com.example.DuAnMau_PH63816.product;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.custom.CustomCardView;

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
        CustomCardView cardRamen = findViewById(R.id.cardRamen);
        CustomCardView cardIcream = findViewById(R.id.cardIcream);
        CustomCardView cardSushi = findViewById(R.id.cardSushi);

        if (cardRamen != null) {
            cardRamen.setOnEditClickListener(v ->
                    Toast.makeText(this, getString(R.string.toast_edit_product, getString(R.string.product_card_ramen_title)), Toast.LENGTH_SHORT).show());
        }
        if (cardIcream != null) {
            cardIcream.setOnEditClickListener(v ->
                    Toast.makeText(this, getString(R.string.toast_edit_product, getString(R.string.product_card_icream_title)), Toast.LENGTH_SHORT).show());
        }
        if (cardSushi != null) {
            cardSushi.setOnEditClickListener(v ->
                    Toast.makeText(this, getString(R.string.toast_edit_product, getString(R.string.product_card_sushi_title)), Toast.LENGTH_SHORT).show());
        }

    }
}
