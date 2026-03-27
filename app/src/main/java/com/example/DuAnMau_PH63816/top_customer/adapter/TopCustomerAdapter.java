package com.example.DuAnMau_PH63816.top_customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.top_customer.TopCustomerBuyingProductsScreen;
import com.example.DuAnMau_PH63816.top_customer.model.TopCustomerItem;

import java.util.List;

public class TopCustomerAdapter extends RecyclerView.Adapter<TopCustomerAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<TopCustomerItem> items;

    public TopCustomerAdapter(Context context, List<TopCustomerItem> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_top_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TopCustomerItem item = items.get(position);
        holder.tvRank.setText(String.valueOf(position + 1));
        holder.tvCustomerCode.setText(item.getMaKhachHang());
        holder.tvCustomerName.setText(item.getTenKhachHang());
        holder.tvBuyCount.setText("Số lần mua: " + item.getSoLanMua());
        holder.tvTotalAmount.setText("Tổng chi tiêu: " +
                TopCustomerBuyingProductsScreen.formatCurrency(item.getTongChiTieu()));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void updateItems(List<TopCustomerItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvRank;
        final TextView tvCustomerCode;
        final TextView tvCustomerName;
        final TextView tvBuyCount;
        final TextView tvTotalAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvCustomerCode = itemView.findViewById(R.id.tvCustomerCode);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvBuyCount = itemView.findViewById(R.id.tvBuyCount);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
        }
    }
}
