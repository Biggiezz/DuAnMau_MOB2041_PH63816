package com.example.DuAnMau_PH63816.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
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
    private CheckBox chkRemember;
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
        loadDataLogin();
    }

    public void loadDataLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("saveUserPassWord", Context.MODE_PRIVATE);

        String user = sharedPreferences.getString("user", "");
        String password = sharedPreferences.getString("password", "");
        String lastUserName = sharedPreferences.getString("lastUserName", "");
        boolean checkRemember = sharedPreferences.getBoolean("checkRemember", false);

        if (checkRemember) {
            edtEmail.setText(user);
            edtPassword.setText(password);
            chkRemember.setChecked(true);
        } else {
            edtEmail.setText(lastUserName);
            edtPassword.setText("");
            chkRemember.setChecked(false);
        }
    }

    private void initUi() {
        Toolbar toolbarLoginScreen = findViewById(R.id.toolbarLoginScreen);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        TextView tvSignUp = findViewById(R.id.tvSignUp);
        chkRemember = findViewById(R.id.chkRemember);
        Button btnLogin = findViewById(R.id.btnLogin);

        setSupportActionBar(toolbarLoginScreen);
        toolbarLoginScreen.setNavigationOnClickListener(v -> finish());
        btnLogin.setOnClickListener(v -> login());
        chkRemember.setOnClickListener(v -> action_remember());
        tvSignUp.setOnClickListener(v -> openSignUp());
    }

    public void action_remember() {
        if (chkRemember.isChecked()) {
            Toast.makeText(this, "Bạn đã lưu đăng nhập", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Bạn đã hủy lưu đăng nhập", Toast.LENGTH_LONG).show();
        }
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

        if (chkRemember.isChecked()) {
            remember(userName, password, true);
        } else {
            saveLastUserName(userName);
            clearRemember();
        }
        Common.maNhanVien = staffDAO.getCurrentStaffCode();
        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, HomePageScreen.class));
        finish();
    }

    public void saveLastUserName(String userName) {
        SharedPreferences sharedPreferences = getSharedPreferences("saveUserPassWord", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastUserName", userName);
        editor.apply();
    }

    public void clearRemember() {
        SharedPreferences sharedPreferences = getSharedPreferences("saveUserPassWord", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", "");
        editor.putString("password", "");
        editor.putBoolean("checkRemember", false);
        editor.apply();
    }

    public void remember(String user, String password, boolean checkRemember) {
        SharedPreferences sharedPreferences = getSharedPreferences("saveUserPassWord", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("user", user);
        editor.putString("password", password);
        editor.putBoolean("checkRemember", checkRemember);
        editor.putString("lastUserName", user);
        editor.apply();
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
