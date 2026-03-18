package com.example.DuAnMau_PH63816.customer;

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
import com.example.DuAnMau_PH63816.customer.adapter.CustomerAdapter;
import com.example.DuAnMau_PH63816.customer.model.Customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CustomerManagementScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_management_screen);
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
        FloatingActionButton floatAddCustomer = findViewById(R.id.fabAddCustomer);
        ListView lvCustomer = findViewById(R.id.lvCustomer);

        ArrayList<Customer> customers = new ArrayList<>();

        customers.add(new Customer("KH001", "Nguyễn Thị Mai", "0987 654 321", "maint.@gmail.com", "123 Đường Lê Lợi, Quận 1, TP.HCM", "12.450.000", "VIP"));
        customers.add(new Customer("KH002", "Trần Thị Bích", "0912 345 678", "bichtt.@gmail.com", "456 Nam Từ Liêm, TP. Hà Nội", "3.240.000", "Member"));
        customers.add(new Customer("KH003", "Lê Hoàng Nam", "0987 654 321", "namlh.@gmail.com", "461 Dang Chau Tue, Quang Hanh, Quang Ninh", "8.900.000", "Member"));
        customers.add(new Customer("KH004", "Phạm Minh Tuấn", "0933 111 222", "tuanpm.@gmail.com", "412 Hà Tu, Hạ Long, Quảng Ninh", "21.050.000", "VIP"));
        customers.add(new Customer("KH005", "Đặng Thu Thảo", "0944 555 666", "thaodt.@gmail.com", "16 Phạm Hùng, Nam Từ Liêm, TP.Hà Nội", "1.500.000", "VIP"));

        CustomerAdapter adapter = new CustomerAdapter(this, customers, customer -> {
            Intent intent1 = new Intent(CustomerManagementScreen.this, DetailCustomerScreen.class);
            intent1.putExtra("extra_customer_name", customer.getName());
            intent1.putExtra("extra_customer_name_big", customer.getName());
            intent1.putExtra("extra_customer_id", customer.getId());
            intent1.putExtra("extra_customer_email", customer.getEmail());
            intent1.putExtra("extra_customer_phone", customer.getPhone());
            intent1.putExtra("extra_customer_price", customer.getPrice());
            intent1.putExtra("extra_customer_status", customer.getStatus());
            startActivity(intent1);
        });
        lvCustomer.setAdapter(adapter);

        floatAddCustomer.setOnClickListener(v -> {
            startActivity(new Intent(CustomerManagementScreen.this, AddCustomerScreen.class));
        });
        icBack.setOnClickListener(v -> finish());
    }
}
