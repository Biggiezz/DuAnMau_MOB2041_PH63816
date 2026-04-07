package com.example.DuAnMau_PH63816.invoice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.invoice.data.InvoiceDAO;
import com.example.DuAnMau_PH63816.invoice.data.InvoiceDetailDAO;
import com.example.DuAnMau_PH63816.invoice.model.Invoice;
import com.example.DuAnMau_PH63816.invoice.model.InvoiceDetail;
import com.example.DuAnMau_PH63816.product.ProductImageResolver;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DetailInvoiceActivity extends AppCompatActivity {

    private InvoiceDAO invoiceDAO;
    private InvoiceDetailDAO invoiceDetailDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_invoice);

        Toolbar toolbarDetailInvoiceActivity = findViewById(R.id.toolbarDetailInvoiceActivity);
        TextView tvInvoiceCode = findViewById(R.id.tvInvoiceCode);
        TextView tvStatus = findViewById(R.id.tvStatus);
        TextView tvSubtotal = findViewById(R.id.tvSubtotal);
        TextView tvTotal = findViewById(R.id.tvTotal);
        TextView tvCustomerName = findViewById(R.id.tvCustomerName);
        TextView tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        TextView tvCustomerAddress = findViewById(R.id.tvCustomerAddress);
        TextView tvDate = findViewById(R.id.tvDate);
        LinearLayout layoutItems = findViewById(R.id.layoutItems);
        MaterialCardView cardStatus = findViewById(R.id.cardStatus);
        invoiceDAO = new InvoiceDAO(this);
        invoiceDetailDAO = new InvoiceDetailDAO(this);
        if (toolbarDetailInvoiceActivity != null) {
            setSupportActionBar(toolbarDetailInvoiceActivity);
            toolbarDetailInvoiceActivity.setNavigationOnClickListener(v -> navigateBack());
        }

        Intent intent = getIntent();
        int invoiceId = intent.getIntExtra("invoice_id", -1);
        Invoice invoice = invoiceDAO.getVisibleInvoiceById(invoiceId);
        if (invoice == null) {
            Toast.makeText(this, "Bạn không có quyền xem hóa đơn này", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ArrayList<InvoiceDetail> details = invoiceDetailDAO.getInvoiceDetailsByInvoiceId(invoiceId);

        tvInvoiceCode.setText(invoice.getCode());
        tvStatus.setText(invoice.getStatus());
        tvSubtotal.setText(calculateSubtotal(details));
        tvTotal.setText(invoice.getTotal());
        tvCustomerName.setText(resolveDisplayText(invoice.getCustomerName(), "Khách lẻ"));
        tvCustomerPhone.setText(resolveDisplayText(invoice.getCustomerPhone(), "Chưa cập nhật số điện thoại"));
        tvCustomerAddress.setText(resolveDisplayText(invoice.getCustomerAddress(), "Chưa cập nhật địa chỉ"));
        tvDate.setText("Ngày lập: " + invoice.getDate());
        bindItemViews(layoutItems, details);

        if ("ĐÃ THANH TOÁN".equals(invoice.getStatus())) {
            cardStatus.setCardBackgroundColor(Color.parseColor("#DCFCE7"));
            tvStatus.setTextColor(Color.parseColor("#16A34A"));
        } else if ("ĐÃ HỦY".equals(invoice.getStatus())) {
            cardStatus.setCardBackgroundColor(Color.parseColor("#F1F5F9"));
            tvStatus.setTextColor(Color.parseColor("#64748B"));
            tvTotal.setTextColor(Color.parseColor("#B7C4D8"));
        } else {
            cardStatus.setCardBackgroundColor(Color.parseColor("#FFEDD5"));
            tvStatus.setTextColor(Color.parseColor("#EA580C"));
            tvTotal.setTextColor(getColor(R.color.color_default));
        }

    }

    private void navigateBack() {
        if (!isTaskRoot()) {
            getOnBackPressedDispatcher().onBackPressed();
            return;
        }

        Intent intent = new Intent(this, InvoiceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void bindItemViews(LinearLayout layoutItems, ArrayList<InvoiceDetail> details) {
        layoutItems.removeAllViews();

        for (InvoiceDetail detail : details) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.item_invoice_detail, layoutItems, false);
            ImageView imgItem = itemView.findViewById(R.id.imgItem);
            TextView tvItemName = itemView.findViewById(R.id.tvItemName);
            TextView tvItemMeta = itemView.findViewById(R.id.tvItemMeta);
            TextView tvItemPrice = itemView.findViewById(R.id.tvItemPrice);

            ProductImageResolver.loadInto(imgItem, detail.getImage());
            tvItemName.setText(detail.getProductName());
            tvItemMeta.setText("x" + detail.getQuantity());
            tvItemPrice.setText(detail.getTotalPrice());

            layoutItems.addView(itemView);
        }
    }

    private String calculateSubtotal(ArrayList<InvoiceDetail> details) {
        if (details == null || details.isEmpty()) {
            return "0k";
        }

        long subtotal = 0;
        for (InvoiceDetail detail : details) {
            subtotal += parseAmount(detail.getTotalPrice());
        }
        return formatAmount(subtotal);
    }

    private long parseAmount(String amountText) {
        String digits = amountText == null ? "" : amountText.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) {
            return 0;
        }
        try {
            return Long.parseLong(digits);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String formatAmount(long amount) {
        return NumberFormat.getNumberInstance(Locale.GERMANY).format(amount) + "k";
    }

    private String resolveDisplayText(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value;
    }

    @Override
    protected void onDestroy() {
        if (invoiceDetailDAO != null) {
            invoiceDetailDAO.close();
        }
        if (invoiceDAO != null) {
            invoiceDAO.close();
        }
        super.onDestroy();
    }
}
