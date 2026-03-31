package com.example.DuAnMau_PH63816.create_account;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.example.DuAnMau_PH63816.login.LoginScreen;
import com.example.DuAnMau_PH63816.staff.data.StaffDAO;
import com.example.DuAnMau_PH63816.staff.model.Staff;

public class CreateAccountActivity extends AppCompatActivity {
    private TextView tvLogIn;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPassword;
    private AutoCompleteTextView actvRole;
    private Button btnSignUp;
    private StaffDAO staffDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
        staffDAO = new StaffDAO(this);
        setupRoleDropdown();
        bindActions();
        Toolbar toolbarCreateAccountScreen = findViewById(R.id.toolbarCreateAccountScreen);
        if (toolbarCreateAccountScreen != null) {
            setSupportActionBar(toolbarCreateAccountScreen);
            toolbarCreateAccountScreen.setNavigationOnClickListener(v -> finish());
        }
    }

    private void initUi() {
        tvLogIn = findViewById(R.id.tvLogIn);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        actvRole = findViewById(R.id.actvRole);
        btnSignUp = findViewById(R.id.btnLogin);
    }

    private void setupRoleDropdown() {
        String[] roles = new String[]{"Nhân viên", "Quản lý"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                roles
        );
        actvRole.setAdapter(adapter);
        actvRole.setInputType(InputType.TYPE_NULL);
        actvRole.setKeyListener(null);
        actvRole.setThreshold(0);
        actvRole.setOnClickListener(v -> actvRole.showDropDown());
        actvRole.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                actvRole.showDropDown();
            }
        });
        actvRole.setText(roles[0], false);
    }

    private void bindActions() {
        tvLogIn.setOnClickListener(v ->
                startActivity(new Intent(CreateAccountActivity.this, LoginScreen.class)));

        btnSignUp.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String login = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String role = actvRole.getText().toString().trim();

            if (name.isEmpty() || login.isEmpty() || password.isEmpty() || role.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Staff staff = new Staff(name, "", login, "", role);
            staff.setPassword(password);

            if (!staffDAO.dangKyTaiKhoan(staff)) {
                Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginScreen.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        if (staffDAO != null) {
            staffDAO.close();
        }
        super.onDestroy();
    }
}
