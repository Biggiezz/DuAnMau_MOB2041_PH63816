package com.example.DuAnMau_PH63816.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.customer.model.Customer;

import java.util.List;

/// extend adapter cua recycler view vao class nay
public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    public interface OnCustomerClickListener {
        void onDetail(@NonNull Customer customer);

        void onEdit(@NonNull Customer customer);
    }

    /// khai bao context va list de lay du lieu
    private final Context context;
    private final LayoutInflater inflater;
    private final List<Customer> items;
    private final OnCustomerClickListener listener;

    /// khoi tao context va list de lay du lieu
    public CustomerAdapter(Context context, List<Customer> items, OnCustomerClickListener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /// lay layoutInflater de lay layout custom
        View view = inflater.inflate(R.layout.item_customer, parent, false);
        /// no se tra ve viewholder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /// lay du lieu tu list va gan vao viewholder de hien thi len giao dien
        Customer customer = items.get(position);
        holder.tvCustomerName.setText(customer.getName());
        boolean isVip = customer.getStatus() == 0;
        holder.tvCustomerType.setText(isVip ? "VIP" : "Member");
        holder.tvCustomerInfo.setText(
                context.getString(
                        R.string.customer_info_format,
                        customer.getId(),
                        customer.getPhone())
        );
        int statusColor = isVip ? R.drawable.bg_badge_vip : R.drawable.bg_badge_member;

        holder.tvCustomerType.setBackgroundResource(statusColor);

        holder.tvCustomerSpending.setText(customer.getPrice());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onDetail(customer);
        });
        holder.imgDetail.setOnClickListener(v -> {
            if (listener != null) listener.onDetail(customer);
        });
        holder.imgEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(customer);
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        /// lay du lieu tu layout custom
        final TextView tvCustomerName;
        final TextView tvCustomerType;
        final TextView tvCustomerInfo;
        final TextView tvCustomerSpending;
        final ImageView imgDetail;
        final ImageView imgEdit;

        ViewHolder(View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvCustomerType = itemView.findViewById(R.id.tvCustomerType);
            tvCustomerInfo = itemView.findViewById(R.id.tvCustomerInfo);
            tvCustomerSpending = itemView.findViewById(R.id.tvCustomerSpending);
            imgDetail = itemView.findViewById(R.id.imgDetail);
            imgEdit = itemView.findViewById(R.id.imgEdit);
        }
    }
}
