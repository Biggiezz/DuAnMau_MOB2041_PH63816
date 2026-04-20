package com.example.DuAnMau_PH63816.customer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.common.BottomButtonNavigator;
import com.example.DuAnMau_PH63816.customer.data.CustomerDAO;
import com.example.DuAnMau_PH63816.customer.model.Customer;

public class DetailCustomerScreen extends AppCompatActivity {

    private EditText edtCustomerName, edtPhone, edtEmail, edtAddress, edtAmount;
    private RadioButton rbVip, rbMember;
    private TextView tvCustomerCode, tvCustomerNameBig, tvCustomerTypeBig;
    private CustomerDAO customerDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_customer_screen);
        customerDAO = new CustomerDAO(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
        loadData();
    }

    private void initUi() {
        Toolbar toolbarDetailCustomerScreen = findViewById(R.id.toolbarDetailCustomerScreen);
        edtCustomerName = findViewById(R.id.edtCustomerName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
        edtAmount = findViewById(R.id.edtAmount);
        rbVip = findViewById(R.id.rbVIP);
        rbMember = findViewById(R.id.rbMember);
        tvCustomerCode = findViewById(R.id.tvCustomerCode);
        tvCustomerNameBig = findViewById(R.id.tvCustomerNameBig);
        tvCustomerTypeBig = findViewById(R.id.tvCustomerTypeBig);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnDelete = findViewById(R.id.btnDelete);

        setSupportActionBar(toolbarDetailCustomerScreen);
        toolbarDetailCustomerScreen.setNavigationOnClickListener(v -> finish());
        BottomButtonNavigator.bindDefaultButtons(this, BottomButtonNavigator.TAB_HOME);
        rbVip.setOnClickListener(v -> tvCustomerTypeBig.setText("Khách hàng VIP"));
        rbMember.setOnClickListener(v -> tvCustomerTypeBig.setText("Khách hàng Member"));
        btnUpdate.setOnClickListener(v -> updateCustomer());
        btnDelete.setOnClickListener(v -> deleteCustomer());
    }

    private void loadData() {
        Intent intent = getIntent();

        String customerName = intent.getStringExtra(CustomerExtras.NAME_BIG);
        if (customerName != null) {
            tvCustomerNameBig.setText(customerName);
        }

        String name = intent.getStringExtra(CustomerExtras.NAME);
        if (name != null) {
            edtCustomerName.setText(name);
        }

        String phone = intent.getStringExtra(CustomerExtras.PHONE);
        if (phone != null) {
            edtPhone.setText(phone);
        }

        String email = intent.getStringExtra(CustomerExtras.EMAIL);
        if (email != null) {
            edtEmail.setText(email);
        }

        String address = intent.getStringExtra(CustomerExtras.ADDRESS);
        if (address != null) {
            edtAddress.setText(address);
        }

        String id = intent.getStringExtra(CustomerExtras.ID);
        if (id != null) {
            tvCustomerCode.setText(id);
        }

        String price = intent.getStringExtra(CustomerExtras.PRICE);
        if (price != null) {
            edtAmount.setText(price);
        }

        int status = intent.getIntExtra(CustomerExtras.STATUS, 1);
        if (status == 0) {
            rbVip.setChecked(true);
            tvCustomerTypeBig.setText("Khách hàng VIP");
        } else {
            rbMember.setChecked(true);
            tvCustomerTypeBig.setText("Khách hàng Member");
        }
    }

    private void updateCustomer() {
        String updatedId = tvCustomerCode.getText().toString().trim();
        String updatedName = edtCustomerName.getText().toString().trim();
        String updatedPhone = edtPhone.getText().toString().trim();
        String updatedEmail = edtEmail.getText().toString().trim();
        String updatedAddress = edtAddress.getText().toString().trim();
        String updatedPrice = edtAmount.getText().toString().trim();

        if (updatedId.isEmpty() || updatedName.isEmpty() || updatedPhone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã, tên và số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }

        int customerStatus = rbVip.isChecked() ? 0 : 1;
        if (!rbVip.isChecked() && !rbMember.isChecked()) {
            customerStatus = 1;
        }

        Customer customer = new Customer(updatedId, updatedName, updatedPhone, updatedEmail, updatedAddress, updatedPrice, customerStatus);
        if (customerDAO.updateCustomer(customer)) {
            Toast.makeText(this, "Cập nhật khách hàng thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Không thể cập nhật khách hàng", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCustomer() {
        String customerId = tvCustomerCode.getText().toString().trim();
        Customer customer = customerDAO.getCustomerById(customerId);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa khách hàng " + customer.getName());
        builder.setMessage("Bạn có chắc chắn muốn xóa khách hàng này?");
        builder.setPositiveButton("Có", (dialog, which) -> {
            if (customer != null && customerDAO.deleteCustomer(customer)) {
                Toast.makeText(this, "Đã xóa khách hàng", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Không thể xóa khách hàng", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    protected void onDestroy() {
        if (customerDAO != null) {
            customerDAO.close();
        }
        super.onDestroy();
    }
}
