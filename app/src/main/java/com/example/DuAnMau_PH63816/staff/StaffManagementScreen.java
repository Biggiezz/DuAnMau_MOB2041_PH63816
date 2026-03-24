package com.example.DuAnMau_PH63816.staff;

import android.content.Intent;
import android.os.Bundle;
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
        ArrayList<Staff> staffList = new ArrayList<>();
        // Dummy data
        staffList.add(new Staff(1, "Nguyễn Văn A", "nguyenvana", "123", "0901 234 567", "Hà Nội", R.drawable.img_staff1, 1));
        staffList.add(new Staff(2, "Trần Thị B", "tranthib", "123", "0912 345 678", "Hồ Chí Minh", R.drawable.img_staff2, 0));
        staffList.add(new Staff(3, "Lê Văn C", "levanc", "123", "0933 444 555", "Đà Nẵng", R.drawable.img_staff3, 0));
        staffList.add(new Staff(4, "Phạm Minh D", "phamminhd", "123", "0977 888 999", "Hải Phòng", R.drawable.img_staff4, 0));

        StaffAdapter staffAdapter = new StaffAdapter(this, staffList, staffDAO, staff -> {
            Intent intent = new Intent(StaffManagementScreen.this, EditStaffScreen.class);
            intent.putExtra("staff_id", staff.getStaffCode());
            intent.putExtra("staff_name", staff.getNameStaff());
            intent.putExtra("staff_login", staff.getNameLogin());
            intent.putExtra("staff_phone", staff.getPhone());
            intent.putExtra("staff_address", staff.getAddress());
            intent.putExtra("staff_image", staff.getImage());
            intent.putExtra("staff_role", staff.getRole());

            startActivity(intent);
        });

        rvStaff.setLayoutManager(new LinearLayoutManager(this));
        rvStaff.setAdapter(staffAdapter);
    }

    private void setListeners() {
        btnAddStaff.setOnClickListener(v -> {
            Intent intent = new Intent(StaffManagementScreen.this, AddStaffScreen.class);
            startActivity(intent);
        });
    }
}
