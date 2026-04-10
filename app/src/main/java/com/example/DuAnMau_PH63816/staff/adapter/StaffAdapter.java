package com.example.DuAnMau_PH63816.staff.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.staff.EditStaffScreen;
import com.example.DuAnMau_PH63816.staff.StaffExtras;
import com.example.DuAnMau_PH63816.staff.data.StaffDAO;
import com.example.DuAnMau_PH63816.staff.model.Staff;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Staff> staffList;

    public StaffAdapter(Context context, List<Staff> staffList) {
        this.context = context;
        if (staffList instanceof ArrayList) {
            this.staffList = (ArrayList<Staff>) staffList;
        } else {
            this.staffList = new ArrayList<>(staffList);
        }
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

        holder.imgEdit.setOnClickListener(v -> openEditStaff(staff));
        holder.imgDelete.setOnClickListener(v -> showDeleteDialog(holder.getBindingAdapterPosition()));
        holder.itemView.setOnClickListener(v -> openEditStaff(staff));

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
        return staffList.size();
    }

    private void openEditStaff(Staff staff) {
        Intent intent = new Intent(context, EditStaffScreen.class);
        intent.putExtra(StaffExtras.ID, staff.getStaffCode());
        intent.putExtra(StaffExtras.NAME, staff.getNameStaff());
        intent.putExtra(StaffExtras.LOGIN, staff.getNameLogin());
        intent.putExtra(StaffExtras.PASSWORD, staff.getPassword());
        intent.putExtra(StaffExtras.PHONE, staff.getPhone());
        intent.putExtra(StaffExtras.ADDRESS, staff.getAddress());
        intent.putExtra(StaffExtras.IMAGE, staff.getImage());
        intent.putExtra(StaffExtras.ROLE, staff.getRole());
        context.startActivity(intent);
    }

    private void showDeleteDialog(int position) {
        if (position == RecyclerView.NO_POSITION || position >= staffList.size()) {
            return;
        }

        Staff staff = staffList.get(position);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setCancelable(false);
        dialog.setTitle("Bạn có muốn xóa không?");
        dialog.setMessage("Bạn có chắc chắn muốn xóa chứ");
        dialog.setPositiveButton("Đồng ý", (dialog1, which) -> {
            StaffDAO staffDAO = new StaffDAO(context);
            if (staffDAO.deleteStaff(staff)) {
                staffList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, staffList.size() - position);
                Toast.makeText(context, "Đã xóa nhân viên", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
            staffDAO.close();
            dialog1.dismiss();
        });
        dialog.setNegativeButton("Không", (dialog12, which) -> dialog12.dismiss());
        dialog.show();
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
