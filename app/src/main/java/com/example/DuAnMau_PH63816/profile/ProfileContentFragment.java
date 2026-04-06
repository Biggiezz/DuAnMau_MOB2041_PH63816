package com.example.DuAnMau_PH63816.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.DuAnMau_PH63816.R;

public class ProfileContentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindCurrentUser(view);
        LinearLayout linearChagePass = view.findViewById(R.id.linearChagePass);
        Button btnLogOut = view.findViewById(R.id.btnLogOut);
        linearChagePass.setOnClickListener(v -> startActivity(new Intent(requireContext(), ChagePassWordScreen.class)));
        btnLogOut.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setCancelable(false);
            builder.setTitle("Đăng xuất");
            builder.setMessage("Bạn có chắc muốn đăng xuất không?");

            builder.setPositiveButton("Đồng ý", (dialog, which) -> System.exit(0));

            builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    private void bindCurrentUser(View view) {
        TextView tvNameLoginProfile = view.findViewById(R.id.tvNameLoginProfile);
        TextView tvPower = view.findViewById(R.id.tvPower);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("StaffData", Context.MODE_PRIVATE);
        String loginName = sharedPreferences.getString("nameLogin", null);
        if (loginName != null && !loginName.trim().isEmpty()) {
            tvNameLoginProfile.setText(loginName);
        }
        int role = sharedPreferences.getInt("role", 1);
        tvPower.setText(role == 0 ? "Quản lý" : "Nhân viên");
    }
}
