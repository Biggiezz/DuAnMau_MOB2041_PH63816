package com.example.DuAnMau_PH63816.staff;

import android.os.Bundle;
import android.text.InputType;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.staff.data.StaffDAO;
import com.example.DuAnMau_PH63816.staff.model.Staff;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import android.widget.EditText;

public class EditStaffScreen extends AppCompatActivity {

    private MaterialButton btnUpdateStaff;
    private EditText edtEditStaffId, edtEditStaffName, edtEditStaffPhone, edtEditStaffEmail, edtEditStaffAddress;
    private AutoCompleteTextView actvEditRole;
    private TextView tvEditName, tvEditRole;
    private ShapeableImageView imgAvatarEdit;
    private StaffDAO staffDAO;
    private String currentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_staff_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        staffDAO = new StaffDAO(this);
        setupDropdown();
        loadData();
        setListeners();
    }

    private void initViews() {
        Toolbar toolbarEditStaffScreen = findViewById(R.id.toolbarEditStaffScreen);
        btnUpdateStaff = findViewById(R.id.btnUpdateStaff);
        edtEditStaffId = findViewById(R.id.edtEditStaffId);
        edtEditStaffName = findViewById(R.id.edtEditStaffName);
        edtEditStaffPhone = findViewById(R.id.edtEditStaffPhone);
        edtEditStaffEmail = findViewById(R.id.edtEditStaffEmail);
        edtEditStaffAddress = findViewById(R.id.edtEditStaffAddress);
        imgAvatarEdit = findViewById(R.id.imgAvatarEdit);
        actvEditRole = findViewById(R.id.actvEditRole);
        tvEditName = findViewById(R.id.tvEditName);
        tvEditRole = findViewById(R.id.tvEditRole);
        if (toolbarEditStaffScreen != null) {
            setSupportActionBar(toolbarEditStaffScreen);
            toolbarEditStaffScreen.setNavigationOnClickListener(v -> finish());
        }
    }

    private void setupDropdown() {
        String[] roles = new String[]{"Nhân viên", "Quản lý"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                roles
        );
        actvEditRole.setAdapter(adapter);
        actvEditRole.setInputType(InputType.TYPE_NULL);
        actvEditRole.setKeyListener(null);
        actvEditRole.setThreshold(0);
        actvEditRole.setOnClickListener(v -> actvEditRole.showDropDown());
        actvEditRole.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                actvEditRole.showDropDown();
            }
        });
    }

    private void loadData() {
        if (getIntent() != null && getIntent().hasExtra(StaffExtras.ID)) {
            int currentStaffCode = getIntent().getIntExtra(StaffExtras.ID, 0);
            String name = getIntent().getStringExtra(StaffExtras.NAME);
            String login = getIntent().getStringExtra(StaffExtras.LOGIN);
            currentPassword = getIntent().getStringExtra(StaffExtras.PASSWORD);
            String phone = getIntent().getStringExtra(StaffExtras.PHONE);
            String address = getIntent().getStringExtra(StaffExtras.ADDRESS);
            int imageRes = getIntent().getIntExtra(StaffExtras.IMAGE, 0);
            int role = getIntent().getIntExtra(StaffExtras.ROLE, 0);

            String roleStr = role == 0 ? "Quản lý" : "Nhân viên";

            edtEditStaffId.setText("NV" + currentStaffCode);
            edtEditStaffName.setText(name);
            edtEditStaffPhone.setText(phone);
            edtEditStaffEmail.setText(login);
            edtEditStaffAddress.setText(address);
            if (imageRes != 0) {
                imgAvatarEdit.setImageResource(imageRes);
            } else {
                imgAvatarEdit.setImageResource(R.drawable.ic_launcher_background);
            }
            actvEditRole.setText(roleStr, false);

            tvEditName.setText(name);
            tvEditRole.setText(roleStr);
        }
    }

    private void setListeners() {
        btnUpdateStaff.setOnClickListener(v -> {
            String id = edtEditStaffId.getText().toString().trim();
            String name = edtEditStaffName.getText().toString().trim();
            String phone = edtEditStaffPhone.getText().toString().trim();
            String email = edtEditStaffEmail.getText().toString().trim();
            String address = edtEditStaffAddress.getText().toString().trim();
            String role = actvEditRole.getText().toString().trim();
            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty() || role.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            Staff staff = new Staff(id, name, phone, email, address, role);
            staff.setPassword(currentPassword == null ? "" : currentPassword);
            if (staffDAO.updateStaff(staff)) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (staffDAO != null) staffDAO.close();
        super.onDestroy();
    }
}
