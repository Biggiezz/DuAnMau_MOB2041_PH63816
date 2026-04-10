package com.example.DuAnMau_PH63816.customer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.common.BottomButtonNavigator;
import com.example.DuAnMau_PH63816.customer.data.CustomerDAO;
import com.example.DuAnMau_PH63816.customer.model.Customer;

public class AddCustomerScreen extends AppCompatActivity {

    private EditText edtName, edtPhone, edtEmail, edtAddress, edtAmount;
    private RadioButton rbVip, rbMember;
    private CustomerDAO customerDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_customer_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        customerDAO = new CustomerDAO(this);
        initUi();
    }

    private void initUi() {
        Toolbar toolbarAddCustomerScreen = findViewById(R.id.toolbarAddCustomerScreen);
        edtName = findViewById(R.id.edtAddCustomerName);
        edtPhone = findViewById(R.id.edtAddCustomerPhone);
        edtEmail = findViewById(R.id.edtAddCustomerEmail);
        edtAddress = findViewById(R.id.edtAddCustomerAddress);
        edtAmount = findViewById(R.id.edtAddCustomerAmount);
        rbVip = findViewById(R.id.rbAddVIP);
        rbMember = findViewById(R.id.rbAddMember);
        Button btnAddCustomer = findViewById(R.id.btnAddCustomer);

        setSupportActionBar(toolbarAddCustomerScreen);
        toolbarAddCustomerScreen.setNavigationOnClickListener(v -> finish());
        BottomButtonNavigator.bindDefaultButtons(this, BottomButtonNavigator.TAB_HOME);
        btnAddCustomer.setOnClickListener(v -> saveCustomer());
    }

    private void saveCustomer() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String amount = edtAmount.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty() || amount.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên, số điện thoại, email, địa chỉ và số tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        int status = rbVip.isChecked() ? 0 : 1;
        if (!rbVip.isChecked() && !rbMember.isChecked()) {
            status = 1;
        }

        Customer customer = new Customer(name, phone, email, address, amount, status);
        if (customerDAO.insertCustomer(customer)) {
            Toast.makeText(this, "Thêm khách hàng thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Không thể thêm khách hàng", Toast.LENGTH_SHORT).show();
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
