package com.example.DuAnMau_PH63816.staff.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.staff.data.StaffDAO;
import com.example.DuAnMau_PH63816.staff.model.Staff;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/// extend adapter cua recycler view vao class nay
public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    public interface OnStaffActionListener {
        void onEdit(@NonNull Staff staff);
    }

    /// khai bao context va list de lay du lieu
    private final Context context;
    private final ArrayList<Staff> staffList;
    private final StaffDAO staffDAO;
    private final OnStaffActionListener listener;

    /// khoi tao context va list de lay du lieu
    public StaffAdapter(Context context, List<Staff> staffList, StaffDAO staffDAO, OnStaffActionListener listener) {
        this.context = context;
        this.staffList = new ArrayList<>(staffList);
        this.staffDAO = staffDAO;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /// lay layoutInflater de lay layout custom
        View view = LayoutInflater.from(context).inflate(R.layout.item_staff, parent, false);
        /// no se tra ve viewholder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /// lay du lieu tu list va gan vao viewholder de hien thi len giao dien
        Staff staff = staffList.get(position);

        holder.tvStaffName.setText(staff.getNameStaff());
        String roleStr = staff.getRole() == 1 ? "Quản lý" : "Nhân viên";
        holder.tvStaffIdAndRole.setText("ID: NV" + staff.getStaffCode() + " • " + roleStr);
        holder.tvStaffPhone.setText("SĐT: " + staff.getPhone());

        holder.imgEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(staff);
            }
        });

        holder.imgDelete.setOnClickListener(v -> showDeleteDialog(holder));

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

    private void showDeleteDialog(ViewHolder holder) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setCancelable(false);
        dialog.setTitle("Bạn có muốn xóa không?");
        dialog.setMessage("Bạn có chắc chắn muốn xóa chứ");
        dialog.setPositiveButton("Đồng ý", (dialog1, which) -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) {
                dialog1.dismiss();
                return;
            }

            boolean deleted = staffDAO.deleteStaff(staffList.get(currentPos));
            if (deleted) {
                staffList.remove(currentPos);
                notifyItemRemoved(currentPos);
            } else {
                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
            dialog1.dismiss();
        });
        dialog.setNegativeButton("Không", (dialog12, which) -> dialog12.dismiss());
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return staffList != null ? staffList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        /// lay du lieu tu layout custom
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
