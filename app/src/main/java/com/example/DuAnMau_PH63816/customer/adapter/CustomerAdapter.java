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

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    public interface OnCustomerClickListener {
        void onDetail(@NonNull Customer customer);

        void onEdit(@NonNull Customer customer);
    }

    private final Context context;
    private final LayoutInflater inflater;
    private final List<Customer> items;
    private final OnCustomerClickListener listener;

    public CustomerAdapter(Context context, List<Customer> items, OnCustomerClickListener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer customer = items.get(position);
        holder.tvCustomerName.setText(customer.getName());
        boolean isVip = customer.getStatus() == 0;
        String typeText = isVip ? "VIP" : "Member";
        int statusColor = isVip ? R.drawable.bg_badge_vip : R.drawable.bg_badge_member;
        holder.tvCustomerType.setText(typeText);
        holder.tvCustomerInfo.setText(
                context.getString(
                        R.string.customer_info_format,
                        customer.getId(),
                        customer.getPhone())
        );

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
