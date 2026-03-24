package com.example.DuAnMau_PH63816.profile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.common.Common;
import com.example.DuAnMau_PH63816.staff.data.StaffDAO;

public class ChagePassWordScreen extends AppCompatActivity {
    EditText edtOldPassWord, edtNewPassWord, edtConfirmNewPassWord;
    StaffDAO staffDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chage_pass_word_screen);
        ImageView icBack = findViewById(R.id.icBack);
        if (icBack != null) {
            icBack.setOnClickListener(v -> finish());
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
    }

    private void initUi() {
        edtOldPassWord = findViewById(R.id.edtPassword);
        edtNewPassWord = findViewById(R.id.edtNewPassword);
        edtConfirmNewPassWord = findViewById(R.id.edtConfirmNewPassword);
        Button btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        Button btnCancel = findViewById(R.id.btnCancel);
        btnUpdatePassword.setOnClickListener(v -> capNhatMatKhau());
        btnCancel.setOnClickListener(v -> clearForm());
    }

    private void clearForm() {
        edtOldPassWord.setText("");
        edtNewPassWord.setText("");
        edtConfirmNewPassWord.setText("");
    }

    private void capNhatMatKhau() {
        String oldPassword = edtOldPassWord.getText().toString().trim();
        String newPassword = edtNewPassWord.getText().toString().trim();
        String confirmNewPassword = edtConfirmNewPassWord.getText().toString().trim();
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng không được bỏ trống", Toast.LENGTH_LONG).show();
            return;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_LONG).show();
            return;
        }
        if (!staffDAO.kiemTraMatKhauCu(Common.maNhanVien, oldPassword)) {
            Toast.makeText(this, "Mật khẩu cũ không đúng", Toast.LENGTH_LONG).show();
            return;
        }
        if (!staffDAO.capNhatMatKhauMoi(Common.maNhanVien, newPassword)) {
            Toast.makeText(this, "Cập nhật mật khẩu thành công", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Cập nhật mật khẩu thất bại", Toast.LENGTH_LONG).show();
        }
    }
}
