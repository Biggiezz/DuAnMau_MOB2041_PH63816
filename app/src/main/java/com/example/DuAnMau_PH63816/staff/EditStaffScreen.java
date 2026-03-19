package com.example.DuAnMau_PH63816.staff;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.DuAnMau_PH63816.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import android.widget.EditText;

public class EditStaffScreen extends AppCompatActivity {

    private ImageView icBack;
    private MaterialButton btnUpdateStaff;
    private EditText edtEditStaffId, edtEditStaffName, edtEditStaffPhone, edtEditStaffEmail, edtEditStaffAddress;
    private AutoCompleteTextView actvEditRole;
    private TextView tvEditName, tvEditRole;
    private ShapeableImageView imgAvatarEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_staff_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnUpdateStaff), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupDropdown();
        loadData();
        setListeners();
    }

    private void initViews() {
        icBack = findViewById(R.id.icBack);
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
    }

    private void setupDropdown() {
        // According to Staff role (1=Quản lý, 0=Nhân viên)
        String[] roles = new String[]{"Nhân viên", "Quản lý"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                roles
        );
        actvEditRole.setAdapter(adapter);
    }

    private void loadData() {
        if (getIntent() != null && getIntent().hasExtra("staff_id")) {
            int currentStaffCode = getIntent().getIntExtra("staff_id", 0);
            String name = getIntent().getStringExtra("staff_name");
            String login = getIntent().getStringExtra("staff_login");
            String phone = getIntent().getStringExtra("staff_phone");
            String address = getIntent().getStringExtra("staff_address");
            int imageRes = getIntent().getIntExtra("staff_image", 0);
            int role = getIntent().getIntExtra("staff_role", 0);

            String roleStr = role == 1 ? "Quản lý" : "Nhân viên";

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
        icBack.setOnClickListener(v -> finish());

        btnUpdateStaff.setOnClickListener(v -> {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
