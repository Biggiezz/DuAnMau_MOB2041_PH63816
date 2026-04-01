package com.example.DuAnMau_PH63816.profile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.common.Common;
import com.example.DuAnMau_PH63816.staff.data.StaffDAO;

public class ChagePassWordScreen extends AppCompatActivity {
    private EditText edtOldPassWord;
    private EditText edtNewPassWord;
    private EditText edtConfirmNewPassWord;
    private StaffDAO staffDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chage_pass_word_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        staffDAO = new StaffDAO(this);
        restoreCurrentStaffCode();
        initUi();
        setupActions();
    }
    private void restoreCurrentStaffCode() {
        if (Common.maNhanVien == null || Common.maNhanVien.trim().isEmpty()) {
            Common.maNhanVien = staffDAO.getCurrentStaffCode();
        }
    }

    private void initUi() {
        edtOldPassWord = findViewById(R.id.edtPassword);
        edtNewPassWord = findViewById(R.id.edtNewPassword);
        edtConfirmNewPassWord = findViewById(R.id.edtConfirmNewPassword);
        Toolbar toolbarChangePasswordScreen = findViewById(R.id.toolbarChangePasswordScreen);
        if (toolbarChangePasswordScreen != null) {
            setSupportActionBar(toolbarChangePasswordScreen);
            toolbarChangePasswordScreen.setNavigationOnClickListener(v -> finish());
        }
    }
    private void setupActions() {
        Button btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        Button btnCancel = findViewById(R.id.btnCancel);
        btnUpdatePassword.setOnClickListener(v -> handleChangePassword());
        btnCancel.setOnClickListener(v -> clearForm());
    }

    private void clearForm() {
        edtOldPassWord.setText("");
        edtNewPassWord.setText("");
        edtConfirmNewPassWord.setText("");
        edtOldPassWord.requestFocus();
    }

    private void handleChangePassword() {
        String oldPassword = edtOldPassWord.getText().toString().trim();
        String newPassword = edtNewPassWord.getText().toString().trim();
        String confirmNewPassword = edtConfirmNewPassWord.getText().toString().trim();

        if (!validateInput(oldPassword, newPassword, confirmNewPassword)) {
            return;
        }
        if (!staffDAO.kiemTraMatKhauCu(Common.maNhanVien, oldPassword)) {
            Toast.makeText(this, "Mật khẩu cũ không đúng", Toast.LENGTH_LONG).show();
            return;
        }
        if (staffDAO.capNhatMatKhauMoi(Common.maNhanVien, newPassword)) {
            Toast.makeText(this, "Cập nhật mật khẩu thành công", Toast.LENGTH_LONG).show();
            clearForm();
            finish();
            return;
        }
        Toast.makeText(this, "Cập nhật mật khẩu thất bại", Toast.LENGTH_LONG).show();
    }
    private boolean validateInput(String oldPassword, String newPassword, String confirmNewPassword) {
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
            return false;
        }
        if (Common.maNhanVien == null || Common.maNhanVien.trim().isEmpty()) {
            Toast.makeText(this, "Không xác định được tài khoản hiện tại", Toast.LENGTH_LONG).show();
            return false;
        }
        if (newPassword.length() < 6) {
            Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_LONG).show();
            return false;
        }
        if (oldPassword.equals(newPassword)) {
            Toast.makeText(this, "Mật khẩu mới không được trùng mật khẩu cũ", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    @Override
    protected void onDestroy() {
        if (staffDAO != null) {
            staffDAO.close();
        }
        super.onDestroy();
    }
}
