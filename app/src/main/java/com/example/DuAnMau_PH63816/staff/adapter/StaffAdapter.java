package com.example.DuAnMau_PH63816.staff.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.staff.model.Staff;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    public interface OnStaffActionListener {
        void onEdit(@NonNull Staff staff);

        void onDelete(@NonNull Staff staff);
    }

    private final Context context;
    private final ArrayList<Staff> staffList;
    private final OnStaffActionListener listener;

    public StaffAdapter(Context context, List<Staff> staffList, OnStaffActionListener listener) {
        this.context = context;
        if (staffList instanceof ArrayList) {
            this.staffList = (ArrayList<Staff>) staffList;
        } else {
            this.staffList = new ArrayList<>(staffList);
        }
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_staff, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Staff staff = staffList.get(position);

        holder.tvStaffName.setText(staff.getNameStaff());
        String roleStr = staff.getRole() == 0 ? "Quản lý" : "Nhân viên";
        holder.tvStaffIdAndRole.setText("ID: NV" + staff.getStaffCode() + " • " + roleStr);
        holder.tvStaffPhone.setText("SĐT: " + staff.getPhone());

        holder.imgEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(staff);
        });

        holder.imgDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(staff);
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(staff);
        });

        int avatarRes = staff.getImage();
        if (avatarRes != 0) {
            Picasso.get()
                    .load(avatarRes)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .fit()
                    .into(holder.imgAvatar);
        } else {
            holder.imgAvatar.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return staffList != null ? staffList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvStaffName;
        final TextView tvStaffIdAndRole;
        final TextView tvStaffPhone;
        final ImageView imgEdit;
        final ImageView imgDelete;
        final ShapeableImageView imgAvatar;

        ViewHolder(View itemView) {
            super(itemView);
            tvStaffName = itemView.findViewById(R.id.tvStaffName);
            tvStaffIdAndRole = itemView.findViewById(R.id.tvStaffIdAndRole);
            tvStaffPhone = itemView.findViewById(R.id.tvStaffPhone);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
