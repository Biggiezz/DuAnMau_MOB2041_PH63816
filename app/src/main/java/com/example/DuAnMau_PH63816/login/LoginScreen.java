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
        initUi();
        staffDAO = new StaffDAO(this);
        Toolbar toolbarLoginScreen = findViewById(R.id.toolbarLoginScreen);
        if (toolbarLoginScreen != null) {
            setSupportActionBar(toolbarLoginScreen);
            toolbarLoginScreen.setNavigationOnClickListener(v -> finish());
        }
        btnLogin.setOnClickListener(v -> {
            String userName = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            edtEmail.setError(null);
            edtPassword.setError(null);

            boolean hasError = false;
            if (userName.isEmpty()) {
                edtEmail.setError("Vui lòng nhập tên đăng nhập");
                hasError = true;
            }
            if (password.isEmpty()) {
                edtPassword.setError("Vui lòng nhập mật khẩu");
                hasError = true;
            }
            if (userName.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng không bỏ trống tên đăng nhâp và mật khẩu", Toast.LENGTH_LONG).show();
                return;
            }
            if (hasError) {
                return;
            }

            if (!staffDAO.KiemTraDangNhap(userName, password)) {
                Toast.makeText(LoginScreen.this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            Common.maNhanVien = staffDAO.getCurrentStaffCode();
            Toast.makeText(LoginScreen.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginScreen.this, HomePageScreen.class);
            startActivity(intent);
            finish();
        });

//        tvForgotPassword.setOnClickListener(v -> {
//            Intent intent = new Intent(LoginScreen.this, ForgotPasswordScreen.class);
//            startActivity(intent);
//            finish();
//        });

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginScreen.this, CreateAccountActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void initUi() {
        edtEmail = findViewById(R.id.edtEmail);
//        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }
}
