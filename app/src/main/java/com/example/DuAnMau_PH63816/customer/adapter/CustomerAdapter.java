package com.example.DuAnMau_PH63816.customer.adapter;

import android.graphics.Color;
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

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {


    public interface OnCustomerClickListener {
        void onCustomerClick(@NonNull Customer customer);
    }

    private final List<Customer> items;
    private final OnCustomerClickListener listener;

    public CustomerAdapter(List<Customer> items, OnCustomerClickListener listener) {
        this.items = items;
        this.listener = listener;
    }


    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = items.get(position);
        holder.bind(customer);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvCustomerName;
        private final TextView tvCustomerType;
        private final TextView tvCustomerInfo;
        private final TextView tvCustomerSpending;
        private final ImageView imgDetail;
        private final ImageView imgEdit;


        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvCustomerType = itemView.findViewById(R.id.tvCustomerType);
            tvCustomerInfo = itemView.findViewById(R.id.tvCustomerInfo);
            tvCustomerSpending = itemView.findViewById(R.id.tvCustomerSpending);
            imgDetail = itemView.findViewById(R.id.imgDetail);
            imgEdit = itemView.findViewById(R.id.imgEdit);
        }

        void bind(Customer customer) {
            tvCustomerName.setText(customer.getName());
            tvCustomerType.setText(customer.getStatus());
            tvCustomerInfo.setText(
                    itemView.getContext().getString(
                            R.string.customer_info_format,
                            customer.getId(),
                            customer.getPhone())
            );
            int statusColor = "VIP".equals(customer.getStatus())
                    ? R.drawable.bg_badge_vip
                    : R.drawable.bg_badge_member;

            tvCustomerType.setBackgroundResource(statusColor);

            tvCustomerSpending.setText(customer.getPrice());

            // Keep click behaviour consistent across the card and action icons
            View.OnClickListener clickListener = v -> listener.onCustomerClick(customer);
            itemView.setOnClickListener(clickListener);
            imgDetail.setOnClickListener(clickListener);
            imgEdit.setOnClickListener(clickListener);
        }
    }
}
