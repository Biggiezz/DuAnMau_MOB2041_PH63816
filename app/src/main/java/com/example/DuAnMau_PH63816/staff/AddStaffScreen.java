package com.example.DuAnMau_PH63816.staff;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.DuAnMau_PH63816.R;
import com.google.android.material.button.MaterialButton;
import android.widget.EditText;

public class AddStaffScreen extends AppCompatActivity {

    private MaterialButton btnSaveStaff;
    private AutoCompleteTextView actvRole;

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

        initViews();
        setupDropdown();
        setListeners();
    }

    private void initViews() {
        Toolbar toolbarAddStaffScreen = findViewById(R.id.toolbarAddStaffScreen);
        btnSaveStaff = findViewById(R.id.btnSaveStaff);
        EditText edtStaffId = findViewById(R.id.edtStaffId);
        EditText edtStaffName = findViewById(R.id.edtStaffName);
        EditText edtStaffPhone = findViewById(R.id.edtStaffPhone);
        EditText edtStaffEmail = findViewById(R.id.edtStaffEmail);
        EditText edtStaffAddress = findViewById(R.id.edtStaffAddress);
        actvRole = findViewById(R.id.actvRole);
        if (toolbarAddStaffScreen != null) {
            setSupportActionBar(toolbarAddStaffScreen);
            toolbarAddStaffScreen.setNavigationOnClickListener(v -> finish());
        }
    }

    private void setupDropdown() {
        String[] roles = new String[]{"Quản lý", "Nhân viên bán hàng", "Chăm sóc khách hàng", "Kế toán"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                roles
        );
        actvRole.setAdapter(adapter);
    }

    private void setListeners() {
        btnSaveStaff.setOnClickListener(v -> {
            // Xử lý lưu nhân viên
            Toast.makeText(this, "Đã lưu nhân viên mới", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
