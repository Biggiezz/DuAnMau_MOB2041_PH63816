package com.example.DuAnMau_PH63816.invoice.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.invoice.model.Invoice;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {

    public interface OnInvoiceClickListener {
        void onInvoiceClick(@NonNull Invoice invoice);
    }

    private final LayoutInflater inflater;
    private final List<Invoice> items;
    private final OnInvoiceClickListener listener;

    public InvoiceAdapter(Context context, List<Invoice> items, OnInvoiceClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_invoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Invoice invoice = items.get(position);
        holder.tvInvoiceCode.setText(invoice.getCode());
        holder.tvStatus.setText(invoice.getStatus());
        holder.tvCustomerName.setText("Khách hàng: " + invoice.getCustomerName());
        holder.tvDate.setText(invoice.getDate());
        holder.tvTotal.setText(invoice.getTotal());

        if ("ĐÃ THANH TOÁN".equals(invoice.getStatus())) {
            holder.cardStatus.setCardBackgroundColor(Color.parseColor("#DCFCE7"));
            holder.tvStatus.setTextColor(Color.parseColor("#16A34A"));
            holder.tvTotal.setTextColor(holder.itemView.getContext().getColor(R.color.color_default));
        } else if ("ĐÃ HỦY".equals(invoice.getStatus())) {
            holder.cardStatus.setCardBackgroundColor(Color.parseColor("#F1F5F9"));
            holder.tvStatus.setTextColor(Color.parseColor("#64748B"));
            holder.tvTotal.setTextColor(Color.parseColor("#B7C4D8"));
        } else {
            holder.cardStatus.setCardBackgroundColor(Color.parseColor("#FFEDD5"));
            holder.tvStatus.setTextColor(Color.parseColor("#EA580C"));
            holder.tvTotal.setTextColor(holder.itemView.getContext().getColor(R.color.color_default));
        }

        holder.itemView.setOnClickListener(v -> listener.onInvoiceClick(invoice));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvInvoiceCode;
        final TextView tvStatus;
        final TextView tvCustomerName;
        final TextView tvDate;
        final TextView tvTotal;
        final MaterialCardView cardStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInvoiceCode = itemView.findViewById(R.id.tvInvoiceCode);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            cardStatus = itemView.findViewById(R.id.cardStatus);
        }
    }
}
