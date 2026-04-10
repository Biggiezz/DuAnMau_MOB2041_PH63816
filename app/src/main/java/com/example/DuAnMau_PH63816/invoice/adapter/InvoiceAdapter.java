package com.example.DuAnMau_PH63816.invoice.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.invoice.DetailInvoiceActivity;
import com.example.DuAnMau_PH63816.invoice.data.InvoiceDAO;
import com.example.DuAnMau_PH63816.invoice.model.Invoice;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private final ArrayList<Invoice> items;

    public InvoiceAdapter(Context context, List<Invoice> items) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        if (items instanceof ArrayList) {
            this.items = (ArrayList<Invoice>) items;
        } else {
            this.items = new ArrayList<>(items);
        }
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

        holder.itemView.setOnClickListener(v -> openDetail(invoice));
        holder.itemView.setOnLongClickListener(v -> {
            if (!isAdmin(v.getContext())) {
                return false;
            }
            showDeleteDialog(v.getContext(), holder.getBindingAdapterPosition());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void openDetail(Invoice invoice) {
        Intent intent = new Intent(context, DetailInvoiceActivity.class);
        intent.putExtra("invoice_id", invoice.getId());
        context.startActivity(intent);
    }

    private void showDeleteDialog(Context context, int position) {
        if (position == RecyclerView.NO_POSITION || position >= items.size()) {
            return;
        }

        Invoice invoice = items.get(position);
        new AlertDialog.Builder(context)
                .setTitle("Xóa hóa đơn")
                .setMessage("Bạn có muốn xóa hóa đơn " + invoice.getCode() + " không?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteInvoice(context, position))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteInvoice(Context context, int position) {
        if (position == RecyclerView.NO_POSITION || position >= items.size()) {
            return;
        }

        Invoice invoice = items.get(position);
        InvoiceDAO invoiceDAO = new InvoiceDAO(context);
        boolean deleted = invoiceDAO.deleteInvoice(invoice.getId());
        invoiceDAO.close();

        if (!deleted) {
            Toast.makeText(context, "Xóa hóa đơn thất bại", Toast.LENGTH_SHORT).show();
            return;
        }

        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size() - position);
        Toast.makeText(context, "Đã xóa hóa đơn", Toast.LENGTH_SHORT).show();
    }

    private boolean isAdmin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("StaffData", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("role", 1) == 0;
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
