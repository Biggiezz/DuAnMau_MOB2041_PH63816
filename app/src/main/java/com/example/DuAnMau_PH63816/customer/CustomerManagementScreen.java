package com.example.DuAnMau_PH63816.customer;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
        FloatingActionButton floatAddCustomer = findViewById(R.id.fabAddCustomer);
        RecyclerView rvCustomer = findViewById(R.id.rvCustomer);
        Toolbar toolbarCustomerManagementScreen = findViewById(R.id.toolbarCustomerManagementScreen);

        if (toolbarCustomerManagementScreen != null) {
            setSupportActionBar(toolbarCustomerManagementScreen);
            toolbarCustomerManagementScreen.setNavigationOnClickListener(v -> finish());
        }

        adapter = new CustomerAdapter(
                this,
                customers,
                new CustomerAdapter.OnCustomerClickListener() {
                    @Override
                    public void onDetail(@NonNull Customer customer) {
                        openCustomerDetail(customer);
                    }

                    @Override
                    public void onEdit(@NonNull Customer customer) {
                        openCustomerDetail(customer);
                    }
                }
        );
        rvCustomer.setLayoutManager(new LinearLayoutManager(this));
        rvCustomer.setAdapter(adapter);
        loadCustomers();

        floatAddCustomer.setOnClickListener(v -> startActivity(new Intent(CustomerManagementScreen.this, AddCustomerScreen.class)));
    }

    private void openCustomerDetail(@NonNull Customer customer) {
        Intent intent = new Intent(CustomerManagementScreen.this, DetailCustomerScreen.class);
        intent.putExtra(CustomerExtras.NAME, customer.getName());
        intent.putExtra(CustomerExtras.NAME_BIG, customer.getName());
        intent.putExtra(CustomerExtras.ID, customer.getId());
        intent.putExtra(CustomerExtras.EMAIL, customer.getEmail());
        intent.putExtra(CustomerExtras.PHONE, customer.getPhone());
        intent.putExtra(CustomerExtras.ADDRESS, customer.getAddress());
        intent.putExtra(CustomerExtras.PRICE, customer.getPrice());
        intent.putExtra(CustomerExtras.STATUS, customer.getStatus());
        startActivity(intent);
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
