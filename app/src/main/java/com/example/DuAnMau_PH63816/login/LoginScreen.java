package com.example.DuAnMau_PH63816.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.homepage.HomePageScreen;

public class LoginScreen extends AppCompatActivity {
    private ImageView icBack;
    private EditText edtEmail, edtPassword;
    private TextView tvForgotPassword;
    private Button btnLogin;

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
        icBack.setOnClickListener(v -> {
            finish();
        });
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            /// check trống
            if (email.isEmpty() || password.isEmpty()) {
                edtEmail.setError("Vui lòng nhập email");
                edtPassword.setError("Vui lòng nhập mật khẩu");
            } else {
                Intent intent = new Intent(LoginScreen.this, HomePageScreen.class);
                startActivity(intent);
            }
        });
    }

    private void initUi() {
        edtEmail = findViewById(R.id.edtEmail);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        edtPassword = findViewById(R.id.edtPassword);
        icBack = findViewById(R.id.icBack);
        btnLogin = findViewById(R.id.btnLogin);
    }
}