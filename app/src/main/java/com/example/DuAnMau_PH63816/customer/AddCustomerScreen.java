package com.example.DuAnMau_PH63816.customer;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.customer.data.CustomerDAO;
import com.example.DuAnMau_PH63816.customer.model.Customer;

public class AddCustomerScreen extends AppCompatActivity {

    private CustomerDAO customerDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_customer_screen);
        customerDAO = new CustomerDAO(this);
        ImageView icBack = findViewById(R.id.icBack);
        EditText edtCode = findViewById(R.id.edtAddCustomerCode);
        EditText edtName = findViewById(R.id.edtAddCustomerName);
        EditText edtPhone = findViewById(R.id.edtAddCustomerPhone);
        EditText edtEmail = findViewById(R.id.edtAddCustomerEmail);
        EditText edtAddress = findViewById(R.id.edtAddCustomerAddress);
        EditText edtAmount = findViewById(R.id.edtAddCustomerAmount);
        RadioButton rbVip = findViewById(R.id.rbAddVIP);
        RadioButton rbMember = findViewById(R.id.rbAddMember);
        Button btnAddCustomer = findViewById(R.id.btnAddCustomer);
        if (icBack != null) {
            icBack.setOnClickListener(v -> finish());
        }
        btnAddCustomer.setOnClickListener(v -> {
            String id = edtCode.getText().toString().trim();
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();
            String amount = edtAmount.getText().toString().trim();

            if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
                Toast.makeText(this, "Vui lòng nhập mã, tên và số điện thoại", Toast.LENGTH_SHORT).show();
                return;
            }

            if (customerDAO.isCustomerIdExists(id)) {
                Toast.makeText(this, "Mã khách hàng đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }

            int status = rbVip.isChecked() ? 0 : 1;
            Customer customer = new Customer(id, name, phone, email, address, amount, status);
            if (customerDAO.insertCustomer(customer)) {
                Toast.makeText(this, "Thêm khách hàng thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Không thể thêm khách hàng", Toast.LENGTH_SHORT).show();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onDestroy() {
        if (customerDAO != null) {
            customerDAO.close();
        }
        super.onDestroy();
    }
}
