package com.example.DuAnMau_PH63816.top_customer;

import static com.example.DuAnMau_PH63816.common.OpenDatePicker.openDatePicker;
import static com.example.DuAnMau_PH63816.common.OpenDatePicker.formatDate;
import static com.example.DuAnMau_PH63816.common.OpenDatePicker.parseDate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.common.BottomButtonNavigator;
import com.example.DuAnMau_PH63816.invoice.data.InvoiceDAO;
import com.example.DuAnMau_PH63816.invoice.model.Invoice;
import com.example.DuAnMau_PH63816.top_customer.adapter.TopCustomerAdapter;
import com.example.DuAnMau_PH63816.top_customer.model.TopCustomerItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TopCustomerBuyingProductsScreen extends AppCompatActivity {

    private InvoiceDAO invoiceDAO;
    private RecyclerView rcvTopCustomer;
    private TextView tvStartDate, tvEndDate;
    private ImageView imgStartDate, imgEndDate;
    private EditText edtCustomerCount;
    private Button btnShowList;
    private TopCustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_top_customer_buying_products_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
        invoiceDAO = new InvoiceDAO(this);

        adapter = new TopCustomerAdapter(this, new ArrayList<>());
        rcvTopCustomer.setLayoutManager(new LinearLayoutManager(this));
        rcvTopCustomer.setAdapter(adapter);
        rcvTopCustomer.setVisibility(View.GONE);

        setDefaultDate();

        imgStartDate.setOnClickListener(v -> openDatePicker(this, tvStartDate));
        imgEndDate.setOnClickListener(v -> openDatePicker(this, tvEndDate));
        btnShowList.setOnClickListener(v -> {
            if (tvStartDate.getText().toString().trim().isEmpty()
                    || tvEndDate.getText().toString().trim().isEmpty()
                    || edtCustomerCount.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                rcvTopCustomer.setVisibility(View.GONE);
                return;
            }

            Date startDate = parseDate(tvStartDate.getText().toString().trim());
            Date endDate = parseDate(tvEndDate.getText().toString().trim());
            if (startDate == null || endDate == null) {
                Toast.makeText(this, "Ngày không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (startDate.after(endDate)) {
                Toast.makeText(this, "Từ ngày không được lớn hơn đến ngày", Toast.LENGTH_SHORT).show();
                return;
            }

            int limit;
            try {
                limit = Integer.parseInt(edtCustomerCount.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            hienThiTopKhachHang(startDate, endDate, limit);
        });
    }

    private void initUi() {
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        imgStartDate = findViewById(R.id.imgStartDate);
        imgEndDate = findViewById(R.id.imgEndDate);
        edtCustomerCount = findViewById(R.id.edtCustomerCount);
        btnShowList = findViewById(R.id.btnShowList);
        rcvTopCustomer = findViewById(R.id.rcvTopCustomer);
        Toolbar toolbar = findViewById(R.id.toolbarTopCustomerBuyingProductsScreen);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> finish());
        }
        BottomButtonNavigator.bindDefaultButtons(this, BottomButtonNavigator.TAB_HOME);
    }

    private void hienThiTopKhachHang(Date startDate, Date endDate, int limit) {
        /// Tạo map để lưu trữ khách hàng và tổng chi tiêu.
        Map<String, TopCustomerItem> map = new HashMap<>();

        /// Gom nhóm theo khách hàng và cộng dồn số lần mua, tổng chi tiêu.
        for (Invoice invoice : invoiceDAO.getAllInvoices()) {
            Date invoiceDate = parseDate(invoice.getDate());
            if (invoiceDate == null) {
                continue;
            }

            if (!"ĐÃ THANH TOÁN".equals(invoice.getStatus())) {
                continue;
            }

            if (invoiceDate.before(startDate) || invoiceDate.after(endDate)) {
                continue;
            }

            String customerId = invoice.getCustomerId();
            String customerName = invoice.getCustomerName();
            if (customerId == null || customerName == null) {
                continue;
            }

            TopCustomerItem item = map.get(customerId);
            /// Nếu khách hàng chưa có trong map, tạo mới.
            if (item == null) {
                item = new TopCustomerItem(customerId, customerName, 0, 0);
                /// Khách hàng mới được thêm vào map.
                map.put(customerId, item);
            }

            /// Cộng dồn số lần mua và tổng chi tiêu.
            item.setSoLanMua(item.getSoLanMua() + 1);
            /// Cộng dồn tổng chi tiêu.
            item.setTongChiTieu(item.getTongChiTieu() + parseAmount(invoice.getTotal()));
        }

        List<TopCustomerItem> list = new ArrayList<>(map.values());
        /// Sắp xếp giảm dần theo tổng chi tiêu để lấy top khách hàng.
        list.sort((o1, o2) -> Long.compare(o2.getTongChiTieu(), o1.getTongChiTieu()));

        if (list.size() > limit) {
            list = new ArrayList<>(list.subList(0, limit));
        }

        if (list.isEmpty()) {
            rcvTopCustomer.setVisibility(View.GONE);
            Toast.makeText(this, "Không có dữ liệu khách hàng!", Toast.LENGTH_SHORT).show();
        } else {
            adapter.updateItems(list);
            rcvTopCustomer.setVisibility(View.VISIBLE);
        }
    }

    private void setDefaultDate() {
        Date minDate = null;
        Date maxDate = null;

        /// Lấy khoảng ngày mặc định từ các hóa đơn đã thanh toán để demo nhanh hơn.
        for (Invoice invoice : invoiceDAO.getAllInvoices()) {
            if (!"ĐÃ THANH TOÁN".equals(invoice.getStatus())) {
                continue;
            }

            Date date = parseDate(invoice.getDate());
            if (date == null) {
                continue;
            }

            if (minDate == null || date.before(minDate)) {
                minDate = date;
            }

            if (maxDate == null || date.after(maxDate)) {
                maxDate = date;
            }
        }

        if (minDate != null) {
            tvStartDate.setText(formatDate(minDate));
        }
        if (maxDate != null) {
            tvEndDate.setText(formatDate(maxDate));
        }
    }

    private long parseAmount(String money) {
        if (money == null || money.trim().isEmpty()) {
            return 0;
        }

        String newMoney = money.toLowerCase()
                .replace("vnd", "")
                .replace("đ", "")
                .replace("k", "")
                .replace(".", "")
                .replace(",", "")
                .trim();

        try {
            return Long.parseLong(newMoney);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (invoiceDAO != null) {
            invoiceDAO.close();
        }
    }

    public static String formatCurrency(long amount) {
        return NumberFormat.getInstance(new Locale("vi", "VN")).format(amount) + " VND";
    }
}
