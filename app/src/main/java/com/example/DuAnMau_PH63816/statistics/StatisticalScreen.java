package com.example.DuAnMau_PH63816.statistics;

import static com.example.DuAnMau_PH63816.common.OpenDatePicker.openDatePicker;
import static com.example.DuAnMau_PH63816.common.OpenDatePicker.parseDate;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.invoice.data.InvoiceDAO;
import com.example.DuAnMau_PH63816.invoice.model.Invoice;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class StatisticalScreen extends AppCompatActivity {
    private TextView tvStartDate, tvEndDate, tvRevenueStatistics;
    private ImageView imgStartDate, imgEndDate;
    private InvoiceDAO invoiceDAO;
    private Button btnShowList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistical_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        invoiceDAO = new InvoiceDAO(this);
        initUi();
        initListener();
    }

    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbarStatisticalScreen);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        imgStartDate = findViewById(R.id.imgStartDate);
        imgEndDate = findViewById(R.id.imgEndDate);
        btnShowList = findViewById(R.id.btnShowList);
        tvRevenueStatistics = findViewById(R.id.tvRevenueStatistics);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }

    private void initListener() {
        imgStartDate.setOnClickListener(v -> openDatePicker(this, tvStartDate));
        imgEndDate.setOnClickListener(v -> openDatePicker(this, tvEndDate));
        btnShowList.setOnClickListener(v -> tinhDoanhThu());
    }

    private void tinhDoanhThu() {
        String tuNgayText = tvStartDate.getText().toString().trim();
        String denNgayText = tvEndDate.getText().toString().trim();

        if (tuNgayText.isEmpty() || denNgayText.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn đầy đủ từ ngày hoặc đến ngày", Toast.LENGTH_LONG).show();
            return;
        }

        Date tuNgay = parseDate(tuNgayText);
        Date denNgay = parseDate(denNgayText);

        if (tuNgay == null || denNgay == null) {
            Toast.makeText(this, "Định dạng ngày không hợp lệ", Toast.LENGTH_LONG).show();
            return;
        }

        if (tuNgay.after(denNgay)) {
            Toast.makeText(this, "Từ ngày không được lớn hơn đến ngày", Toast.LENGTH_LONG).show();
            return;
        }

        long tongDoanhThu = 0L;

        for (Invoice invoice : invoiceDAO.getAllInvoices()) {
            Date ngayHoaDon = parseDate(invoice.getDate());

            if (ngayHoaDon != null && !ngayHoaDon.before(tuNgay) && !ngayHoaDon.after(denNgay)) {
                tongDoanhThu += parseAmount(invoice.getTotal());
            }
        }

        if (tongDoanhThu == 0L) {
            Toast.makeText(this, "Không có hóa đơn trong khoảng thời gian này", Toast.LENGTH_SHORT).show();
        }

        tvRevenueStatistics.setText(
                NumberFormat.getInstance(new Locale("vi", "VN")).format(tongDoanhThu) + " VND"
        );
    }

    private long parseAmount(String text) {
        try {
            return Long.parseLong(text.toLowerCase(Locale.getDefault())
                    .replace("vnd", "")
                    .replace("đ", "")
                    .replace("k", "")
                    .replace(".", "")
                    .replace(",", "")
                    .trim());
        } catch (Exception exception) {
            return 0L;
        }
    }
    @Override
    protected void onDestroy() {
        if (invoiceDAO != null) {
            invoiceDAO.close();
        }
        super.onDestroy();
    }
}
