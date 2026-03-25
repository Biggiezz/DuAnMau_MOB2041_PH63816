package com.example.DuAnMau_PH63816.staff;

import android.app.AlertDialog;
import android.content.Intent;
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupRecyclerView();
        setListeners();
    }

    private void initViews() {
        rvStaff = findViewById(R.id.rvStaff);
        btnAddStaff = findViewById(R.id.btnAddStaff);
        Toolbar toolbarStaffManagementScreen = findViewById(R.id.toolbarStaffManagementScreen);
        if (toolbarStaffManagementScreen != null) {
            setSupportActionBar(toolbarStaffManagementScreen);
            toolbarStaffManagementScreen.setNavigationOnClickListener(v -> finish());
        }
        staffDAO = new StaffDAO(this);
    }

    private void setupRecyclerView() {
        staffAdapter = new StaffAdapter(this, staffList, new StaffAdapter.OnStaffActionListener() {
            @Override
            public void onEdit(Staff staff) {
                openEditStaff(staff);
            }

            @Override
            public void onDelete(Staff staff) {
                showDeleteDialog(staff);
            }
        });

        rvStaff.setLayoutManager(new LinearLayoutManager(this));
        rvStaff.setAdapter(staffAdapter);
        loadStaff();
    }

    private void openEditStaff(Staff staff) {
        Intent intent = new Intent(StaffManagementScreen.this, EditStaffScreen.class);
        intent.putExtra(StaffExtras.ID, staff.getStaffCode());
        intent.putExtra(StaffExtras.NAME, staff.getNameStaff());
        intent.putExtra(StaffExtras.LOGIN, staff.getNameLogin());
        intent.putExtra(StaffExtras.PASSWORD, staff.getPassword());
        intent.putExtra(StaffExtras.PHONE, staff.getPhone());
        intent.putExtra(StaffExtras.ADDRESS, staff.getAddress());
        intent.putExtra(StaffExtras.IMAGE, staff.getImage());
        intent.putExtra(StaffExtras.ROLE, staff.getRole());
        startActivity(intent);
    }

    private void showDeleteDialog(Staff staff) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle("Bạn có muốn xóa không?");
        dialog.setMessage("Bạn có chắc chắn muốn xóa chứ");
        dialog.setPositiveButton("Đồng ý", (dialog1, which) -> {
            if (staffDAO != null && staffDAO.deleteStaff(staff)) {
                loadStaff();
                Toast.makeText(this, "Đã xóa nhân viên", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
            dialog1.dismiss();
        });
        dialog.setNegativeButton("Không", (dialog12, which) -> dialog12.dismiss());
        dialog.show();
    }

    private void loadStaff() {
        staffList.clear();
        staffList.addAll(staffDAO.getAllStaff());
        if (staffAdapter != null) {
            staffAdapter.notifyDataSetChanged();
        }
    }

    private void setListeners() {
        btnAddStaff.setOnClickListener(v -> {
            Intent intent = new Intent(StaffManagementScreen.this, AddStaffScreen.class);
            startActivity(intent);
        });
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
