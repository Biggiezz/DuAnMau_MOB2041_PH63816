package com.example.DuAnMau_PH63816.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.customer.adapter.CustomerAdapter;
import com.example.DuAnMau_PH63816.customer.data.CustomerDAO;
import com.example.DuAnMau_PH63816.customer.model.Customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CustomerManagementScreen extends AppCompatActivity {

    private final ArrayList<Customer> customers = new ArrayList<>();
    private CustomerAdapter adapter;
    private CustomerDAO customerDAO;

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
        customerDAO = new CustomerDAO(this);
        setupUi();
    }

    private void setupUi() {
        ImageView icBack = findViewById(R.id.icBack);
        FloatingActionButton floatAddCustomer = findViewById(R.id.fabAddCustomer);
        RecyclerView rvCustomer = findViewById(R.id.rvCustomer);

        adapter = new CustomerAdapter(
                this,
                customers,
                new CustomerAdapter.OnCustomerClickListener() {
                    @Override
                    public void onDetail(@NonNull Customer customer) {
                        Intent intent1 = new Intent(CustomerManagementScreen.this, DetailCustomerScreen.class);
                        intent1.putExtra("extra_customer_name", customer.getName());
                        intent1.putExtra("extra_customer_name_big", customer.getName());
                        intent1.putExtra("extra_customer_id", customer.getId());
                        intent1.putExtra("extra_customer_email", customer.getEmail());
                        intent1.putExtra("extra_customer_phone", customer.getPhone());
                        intent1.putExtra("extra_customer_address", customer.getAddress());
                        intent1.putExtra("extra_customer_price", customer.getPrice());
                        intent1.putExtra("extra_customer_status", customer.getStatus());
                        startActivity(intent1);
                    }

                    @Override
                    public void onEdit(@NonNull Customer customer) {
                        Intent intent1 = new Intent(CustomerManagementScreen.this, DetailCustomerScreen.class);
                        intent1.putExtra("extra_customer_name", customer.getName());
                        intent1.putExtra("extra_customer_name_big", customer.getName());
                        intent1.putExtra("extra_customer_id", customer.getId());
                        intent1.putExtra("extra_customer_email", customer.getEmail());
                        intent1.putExtra("extra_customer_phone", customer.getPhone());
                        intent1.putExtra("extra_customer_address", customer.getAddress());
                        intent1.putExtra("extra_customer_price", customer.getPrice());
                        intent1.putExtra("extra_customer_status", customer.getStatus());
                        startActivity(intent1);
                    }
                }
        );
        rvCustomer.setLayoutManager(new LinearLayoutManager(this));
        rvCustomer.setAdapter(adapter);
        loadCustomers();

        floatAddCustomer.setOnClickListener(v -> startActivity(new Intent(CustomerManagementScreen.this, AddCustomerScreen.class)));
        icBack.setOnClickListener(v -> finish());
    }

    private void loadCustomers() {
        customers.clear();
        customers.addAll(customerDAO.getAllCustomer());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null && customerDAO != null) {
            loadCustomers();
        }
    }

    @Override
    protected void onDestroy() {
        if (customerDAO != null) {
            customerDAO.close();
        }
        super.onDestroy();
    }
}
