package com.example.DuAnMau_PH63816.invoice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.common.BottomButtonNavigator;
import com.example.DuAnMau_PH63816.homepage.HomePageScreen;
import com.example.DuAnMau_PH63816.invoice.adapter.InvoiceAdapter;
import com.example.DuAnMau_PH63816.invoice.data.InvoiceDAO;
import com.example.DuAnMau_PH63816.invoice.model.Invoice;

import java.util.ArrayList;

public class InvoiceActivity extends AppCompatActivity {

    private final ArrayList<Invoice> invoices = new ArrayList<>();
    private InvoiceDAO invoiceDAO;
    private InvoiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        RecyclerView rvInvoices = findViewById(R.id.rvInvoices);
        Toolbar toolbarInvoiceScreen = findViewById(R.id.toolbarInvoice);
        if (rvInvoices == null || toolbarInvoiceScreen == null) {
            Toast.makeText(this, "Khong the mo man hinh hoa don", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            invoiceDAO = new InvoiceDAO(this);
            adapter = new InvoiceAdapter(this, invoices, this::openDetail);
            setSupportActionBar(toolbarInvoiceScreen);
            toolbarInvoiceScreen.setNavigationIcon(R.drawable.ic_back);
            toolbarInvoiceScreen.setNavigationOnClickListener(v -> navigateBack());
            rvInvoices.setLayoutManager(new LinearLayoutManager(this));
            rvInvoices.setAdapter(adapter);
            loadInvoices();
            BottomButtonNavigator.bindDefaultButtons(this, BottomButtonNavigator.TAB_HOME);
        } catch (RuntimeException exception) {
            Toast.makeText(this, "Du lieu hoa don dang loi, vui long mo lai", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void navigateBack() {
        if (!isTaskRoot()) {
            getOnBackPressedDispatcher().onBackPressed();
            return;
        }

        Intent intent = new Intent(this, HomePageScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void loadInvoices() {
        if (invoiceDAO == null || adapter == null) {
            return;
        }
        invoices.clear();
        invoices.addAll(invoiceDAO.getAllInvoices());
        adapter.notifyDataSetChanged();
    }

    private void openDetail(Invoice invoice) {
        Intent intent = new Intent(this, DetailInvoiceActivity.class);
        intent.putExtra("invoice_id", invoice.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInvoices();
    }

    @Override
    protected void onDestroy() {
        if (invoiceDAO != null) {
            invoiceDAO.close();
        }
        super.onDestroy();
    }
}
