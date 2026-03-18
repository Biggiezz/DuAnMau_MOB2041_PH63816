package com.example.DuAnMau_PH63816.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.customer.model.Customer;
import java.util.List;

public class CustomerAdapter extends BaseAdapter {

    public interface OnCustomerClickListener {
        void onCustomerClick(Customer customer);
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

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Customer getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            View view = inflater.inflate(R.layout.item_customer, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            convertView = view;
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Customer customer = getItem(position);
        holder.tvCustomerName.setText(customer.getName());
        holder.tvCustomerType.setText(customer.getStatus());
        holder.tvCustomerInfo.setText(
                context.getString(
                        R.string.customer_info_format,
                        customer.getId(),
                        customer.getPhone())
        );
        int statusColor = "VIP".equals(customer.getStatus())
                ? R.drawable.bg_badge_vip
                : R.drawable.bg_badge_member;

        holder.tvCustomerType.setBackgroundResource(statusColor);

        holder.tvCustomerSpending.setText(customer.getPrice());

        View.OnClickListener clickListener = v -> listener.onCustomerClick(customer);
        convertView.setOnClickListener(clickListener);
        holder.imgDetail.setOnClickListener(clickListener);
        holder.imgEdit.setOnClickListener(clickListener);

        return convertView;
    }

    private static class ViewHolder {
        final TextView tvCustomerName;
        final TextView tvCustomerType;
        final TextView tvCustomerInfo;
        final TextView tvCustomerSpending;
        final ImageView imgDetail;
        final ImageView imgEdit;

        ViewHolder(View itemView) {
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvCustomerType = itemView.findViewById(R.id.tvCustomerType);
            tvCustomerInfo = itemView.findViewById(R.id.tvCustomerInfo);
            tvCustomerSpending = itemView.findViewById(R.id.tvCustomerSpending);
            imgDetail = itemView.findViewById(R.id.imgDetail);
            imgEdit = itemView.findViewById(R.id.imgEdit);
        }
    }
}
