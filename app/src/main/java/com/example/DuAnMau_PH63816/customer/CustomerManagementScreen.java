package com.example.DuAnMau_PH63816.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.common.BottomButtonNavigator;
import com.example.DuAnMau_PH63816.customer.adapter.CustomerAdapter;
import com.example.DuAnMau_PH63816.customer.data.CustomerDAO;
import com.example.DuAnMau_PH63816.customer.model.Customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CustomerManagementScreen extends AppCompatActivity {

    private final ArrayList<Customer> customers = new ArrayList<>();
    private RecyclerView rvCustomer;
    private CustomerDAO customerDAO;
    private CustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_management_screen);

        SharedPreferences sharedPreferences = getSharedPreferences("StaffData", MODE_PRIVATE);
        int role = sharedPreferences.getInt("role", 1);
        if (role != 0) {
            Toast.makeText(this, "Bạn không có quyền truy cập chức năng này", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        customerDAO = new CustomerDAO(this);
        initUi();
        setupRecyclerView();
        loadCustomers();
    }

    private void initUi() {
        Toolbar toolbarCustomerManagementScreen = findViewById(R.id.toolbarCustomerManagementScreen);
        rvCustomer = findViewById(R.id.rvCustomer);
        FloatingActionButton fabAddCustomer = findViewById(R.id.fabAddCustomer);

        setSupportActionBar(toolbarCustomerManagementScreen);
        toolbarCustomerManagementScreen.setNavigationOnClickListener(v -> finish());
        BottomButtonNavigator.bindDefaultButtons(this, BottomButtonNavigator.TAB_HOME);

        fabAddCustomer.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCustomerScreen.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        adapter = new CustomerAdapter(this, customers);
        rvCustomer.setLayoutManager(new LinearLayoutManager(this));
        rvCustomer.setAdapter(adapter);
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
