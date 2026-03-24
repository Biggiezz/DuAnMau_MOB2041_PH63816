package com.example.DuAnMau_PH63816.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;

public class ProfileScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootProfile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bindDataFromIntent();
    }

    private void bindDataFromIntent() {
        Intent intent = getIntent();
        if (intent == null) return;

        Toolbar toolbarProfileScreen = findViewById(R.id.toolbarProfileScreen);
        LinearLayout linearChagePass = findViewById(R.id.linearChagePass);
        linearChagePass.setOnClickListener(v -> startActivity(new Intent(ProfileScreen.this, ChagePassWordScreen.class)));
        if (toolbarProfileScreen != null) {
            setSupportActionBar(toolbarProfileScreen);
            toolbarProfileScreen.setNavigationOnClickListener(v -> finish());
        }
    }

}
