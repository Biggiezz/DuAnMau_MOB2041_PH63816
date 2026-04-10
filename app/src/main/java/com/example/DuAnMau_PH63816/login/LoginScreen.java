package com.example.DuAnMau_PH63816.login;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.DuAnMau_PH63816.common.Common;
import com.example.DuAnMau_PH63816.create_account.CreateAccountActivity;
import com.example.DuAnMau_PH63816.homepage.HomePageScreen;
import com.example.DuAnMau_PH63816.staff.data.StaffDAO;

public class LoginScreen extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private TextView tvSignUp;
    private Button btnLogin;
    private StaffDAO staffDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        staffDAO = new StaffDAO(this);
        initUi();
    }

    private void initUi() {
        Toolbar toolbarLoginScreen = findViewById(R.id.toolbarLoginScreen);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        btnLogin = findViewById(R.id.btnLogin);

        setSupportActionBar(toolbarLoginScreen);
        toolbarLoginScreen.setNavigationOnClickListener(v -> finish());
        btnLogin.setOnClickListener(v -> login());
        tvSignUp.setOnClickListener(v -> openSignUp());
    }

    private void login() {
        String userName = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        edtEmail.setError(null);
        edtPassword.setError(null);

        boolean isEmpty = false;
        if (userName.isEmpty()) {
            edtEmail.setError("Vui lòng nhập tên đăng nhập");
            isEmpty = true;
        }
        if (password.isEmpty()) {
            edtPassword.setError("Vui lòng nhập mật khẩu");
            isEmpty = true;
        }
        if (isEmpty) {
            Toast.makeText(this, "Vui lòng không bỏ trống tên đăng nhập và mật khẩu", Toast.LENGTH_LONG).show();
            return;
        }

        if (!staffDAO.KiemTraDangNhap(userName, password)) {
            Toast.makeText(this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        Common.maNhanVien = staffDAO.getCurrentStaffCode();
        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, HomePageScreen.class));
        finish();
    }

    private void openSignUp() {
        startActivity(new Intent(this, CreateAccountActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        if (staffDAO != null) {
            staffDAO.close();
        }
        super.onDestroy();
    }
}
