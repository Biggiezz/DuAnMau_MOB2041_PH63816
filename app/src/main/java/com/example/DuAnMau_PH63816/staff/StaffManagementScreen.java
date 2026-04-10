package com.example.DuAnMau_PH63816.staff;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.common.BottomButtonNavigator;
import com.example.DuAnMau_PH63816.staff.adapter.StaffAdapter;
import com.example.DuAnMau_PH63816.staff.data.StaffDAO;
import com.example.DuAnMau_PH63816.staff.model.Staff;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class StaffManagementScreen extends AppCompatActivity {

    private RecyclerView rvStaff;
    private MaterialButton btnAddStaff;
    private StaffDAO staffDAO;
    private StaffAdapter staffAdapter;
    private final ArrayList<Staff> staffList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staff_management_screen);

        SharedPreferences sharedPreferences = getSharedPreferences("StaffData", MODE_PRIVATE);
        int role = sharedPreferences.getInt("role", 1);
        if (role != 0) {
            Toast.makeText(this, "Bạn không có quyền truy cập chức năng này", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        staffDAO = new StaffDAO(this);
        initUi();
        setupRecyclerView();
        loadStaff();
    }

    private void initUi() {
        Toolbar toolbarStaffManagementScreen = findViewById(R.id.toolbarStaffManagementScreen);
        rvStaff = findViewById(R.id.rvStaff);
        btnAddStaff = findViewById(R.id.btnAddStaff);

        setSupportActionBar(toolbarStaffManagementScreen);
        toolbarStaffManagementScreen.setNavigationOnClickListener(v -> finish());
        BottomButtonNavigator.bindDefaultButtons(this, BottomButtonNavigator.TAB_HOME);

        btnAddStaff.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddStaffScreen.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        staffAdapter = new StaffAdapter(this, staffList);
        rvStaff.setLayoutManager(new LinearLayoutManager(this));
        rvStaff.setAdapter(staffAdapter);
    }

    private void loadStaff() {
        staffList.clear();
        staffList.addAll(staffDAO.getAllStaff());
        staffAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStaff();
    }

    @Override
    protected void onDestroy() {
        if (staffDAO != null) {
            staffDAO.close();
        }
        super.onDestroy();
    }
}
