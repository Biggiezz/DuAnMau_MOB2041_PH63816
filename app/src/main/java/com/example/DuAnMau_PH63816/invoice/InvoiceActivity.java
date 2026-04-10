package com.example.DuAnMau_PH63816.invoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

    private final ArrayList<Invoice> invoiceList = new ArrayList<>();
    private TextView txtInvoiceEmpty;
    private InvoiceDAO invoiceDAO;
    private InvoiceAdapter invoiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        invoiceDAO = new InvoiceDAO(this);
        initUi();
        loadInvoices();
        showCreatedInvoiceMessage();
    }

    private void initUi() {
        Toolbar toolbarInvoice = findViewById(R.id.toolbarInvoice);
        RecyclerView rvInvoices = findViewById(R.id.rvInvoices);
        txtInvoiceEmpty = findViewById(R.id.txtInvoiceEmpty);

        invoiceAdapter = new InvoiceAdapter(this, invoiceList);

        setSupportActionBar(toolbarInvoice);
        toolbarInvoice.setNavigationIcon(R.drawable.ic_back);
        toolbarInvoice.setNavigationOnClickListener(v -> navigateBack());

        rvInvoices.setLayoutManager(new LinearLayoutManager(this));
        rvInvoices.setAdapter(invoiceAdapter);
        BottomButtonNavigator.bindDefaultButtons(this, BottomButtonNavigator.TAB_HOME);
    }

    private void showCreatedInvoiceMessage() {
        int createdInvoiceId = getIntent().getIntExtra("created_invoice_id", -1);
        if (createdInvoiceId != -1) {
            Toast.makeText(this, "Đã tạo hóa đơn mới #" + createdInvoiceId, Toast.LENGTH_SHORT).show();
            getIntent().removeExtra("created_invoice_id");
        }
    }

    private void loadInvoices() {
        invoiceList.clear();
        invoiceList.addAll(invoiceDAO.getVisibleInvoices());
        invoiceAdapter.notifyDataSetChanged();
        txtInvoiceEmpty.setVisibility(invoiceList.isEmpty() ? View.VISIBLE : View.GONE);
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
