package com.example.DuAnMau_PH63816.statistics;

import static com.example.DuAnMau_PH63816.common.OpenDatePicker.openDatePicker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;

public class StatisticalScreen extends AppCompatActivity {
    private ImageView imgStartDate, imgEndDate;
    private TextView tvStartDate, tvEndDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistical_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
    }

    private void initUi() {
        Intent intent = getIntent();
        if (intent == null) return;

        Toolbar toolbarStatisticalScreen = findViewById(R.id.toolbarStatisticalScreen);
        imgStartDate = findViewById(R.id.imgStartDate);
        imgEndDate = findViewById(R.id.imgEndDate);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);

        if (toolbarStatisticalScreen != null) {
            setSupportActionBar(toolbarStatisticalScreen);
            toolbarStatisticalScreen.setNavigationOnClickListener(v -> finish());
        }
        imgStartDate.setOnClickListener(v -> openDatePicker(this, tvStartDate));
        imgEndDate.setOnClickListener(v -> openDatePicker(this, tvEndDate));
    }
}

