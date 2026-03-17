package com.example.DuAnMau_PH63816.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;

public class DetailCustomerScreen extends AppCompatActivity {
    public static final String EXTRA_CUSTOMER_NAME = "extra_customer_name";
    public static final String EXTRA_CUSTOMER_NAME_BIG = "extra_customer_name_big";
    public static final String EXTRA_CUSTOMER_ID = "extra_customer_id";
    public static final String EXTRA_CUSTOMER_PHONE = "extra_customer_phone";
    public static final String EXTRA_CUSTOMER_PRICE = "extra_customer_price";
    public static final String EXTRA_CUSTOMER_EMAIL = "extra_customer_email";
    public static final String EXTRA_CUSTOMER_STATUS = "extra_customer_status";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_customer_screen);
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
        EditText edtCustomerName = findViewById(R.id.edtCustomerName);
        EditText edtPhone = findViewById(R.id.edtPhone);
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtAddress = findViewById(R.id.edtAddress);
        EditText edtAmount = findViewById(R.id.edtAmount);
        RadioGroup rdBtn = findViewById(R.id.rgCustomerType);
        RadioButton rbVip = findViewById(R.id.rbVIP);
        RadioButton rbMember = findViewById(R.id.rbMember);
        TextView edtCustomerCode = findViewById(R.id.tvCustomerCode);
        TextView tvCustomerNameBig = findViewById(R.id.tvCustomerNameBig);


        String customerName = intent.getStringExtra(EXTRA_CUSTOMER_NAME_BIG);
        if (customerName != null && tvCustomerNameBig != null) {
            tvCustomerNameBig.setText(customerName);
        }

        String name = intent.getStringExtra(EXTRA_CUSTOMER_NAME);
        if (name != null && edtCustomerName != null) {
            edtCustomerName.setText(name);
        }
        String phone = intent.getStringExtra(EXTRA_CUSTOMER_PHONE);
        if (phone != null && edtPhone != null) {
            edtPhone.setText(phone);
        }
        String email = intent.getStringExtra(EXTRA_CUSTOMER_EMAIL);
        if (email != null && edtEmail != null) {
            edtEmail.setText(email);
        }
        String id = intent.getStringExtra(EXTRA_CUSTOMER_ID);
        if (id != null && edtCustomerCode != null) {
            edtCustomerCode.setText(id);
        }

        String price = intent.getStringExtra(EXTRA_CUSTOMER_PRICE);
        if (price != null && edtAmount != null) {
            edtAmount.setText(price);
        }
        String status = intent.getStringExtra(EXTRA_CUSTOMER_STATUS);

        if (status != null) {
            if (status.equals("VIP")) {
                rbVip.setChecked(true);
            } else {
                rbMember.setChecked(true);
            }
        }

        icBack.setOnClickListener(v -> finish());
    }
}