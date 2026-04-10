package com.example.DuAnMau_PH63816.staff;

import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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
import com.example.DuAnMau_PH63816.staff.data.StaffDAO;
import com.example.DuAnMau_PH63816.staff.model.Staff;
import com.google.android.material.button.MaterialButton;

public class AddStaffScreen extends AppCompatActivity {

    private MaterialButton btnSaveStaff;
    private AutoCompleteTextView actvRole;
    private StaffDAO staffDAO;
    private EditText edtStaffName, edtStaffPhone, edtStaffEmail, edtStaffAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_staff_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUi();
        setupDropdown();
        staffDAO = new StaffDAO(this);
    }

    private void initUi() {
        Toolbar toolbarAddStaffScreen = findViewById(R.id.toolbarAddStaffScreen);
        btnSaveStaff = findViewById(R.id.btnSaveStaff);
        TextView tvStaffId = findViewById(R.id.tvStaffId);
        edtStaffName = findViewById(R.id.edtStaffName);
        edtStaffPhone = findViewById(R.id.edtStaffPhone);
        edtStaffEmail = findViewById(R.id.edtStaffEmail);
        edtStaffAddress = findViewById(R.id.edtStaffAddress);
        actvRole = findViewById(R.id.actvRole);
        if (toolbarAddStaffScreen != null) {
            setSupportActionBar(toolbarAddStaffScreen);
            toolbarAddStaffScreen.setNavigationOnClickListener(v -> finish());
        }
        BottomButtonNavigator.bindDefaultButtons(this, BottomButtonNavigator.TAB_HOME);
        tvStaffId.setText("Tự động tạo");
        btnSaveStaff.setOnClickListener(v -> saveStaff());
    }

    private void setupDropdown() {
        actvRole.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                new String[]{"Nhân viên", "Quản lý"}
        ));
        actvRole.setInputType(InputType.TYPE_NULL);
        actvRole.setKeyListener(null);
        actvRole.setThreshold(0);
        actvRole.setOnClickListener(v -> actvRole.showDropDown());
        actvRole.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) actvRole.showDropDown();
        });
    }

    private void saveStaff() {
        String name = edtStaffName.getText().toString().trim();
        String phone = edtStaffPhone.getText().toString().trim();
        String email = edtStaffEmail.getText().toString().trim();
        String address = edtStaffAddress.getText().toString().trim();
        String role = actvRole.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty() || role.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Staff staff = new Staff(name, phone, email, address, role);
        staff.setPassword("123456");

        if (staffDAO.insertStaff(staff)) {
            Toast.makeText(this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Thêm nhân viên thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (staffDAO != null) staffDAO.close();
        super.onDestroy();
    }
}
