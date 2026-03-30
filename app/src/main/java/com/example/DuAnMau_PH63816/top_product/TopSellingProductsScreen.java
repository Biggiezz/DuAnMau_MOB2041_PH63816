package com.example.DuAnMau_PH63816.top_product;

import static com.example.DuAnMau_PH63816.common.OpenDatePicker.openDatePicker;
import static com.example.DuAnMau_PH63816.common.OpenDatePicker.formatDate;
import static com.example.DuAnMau_PH63816.common.OpenDatePicker.parseDate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.DuAnMau_PH63816.invoice.data.InvoiceDetailDAO;
import com.example.DuAnMau_PH63816.invoice.model.Invoice;
import com.example.DuAnMau_PH63816.invoice.model.InvoiceDetail;
import com.example.DuAnMau_PH63816.top_product.adapter.TopSellingProductAdapter;
import com.example.DuAnMau_PH63816.top_product.model.TopSellingProductItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TopSellingProductsScreen extends AppCompatActivity {

    private TextView tvStartDate, tvEndDate;
    private EditText edtItemCount;
    private RecyclerView rcvTopProducts;
    private TopSellingProductAdapter adapter;
    private InvoiceDAO invoiceDAO;
    private Button btnShowList;
    private InvoiceDetailDAO invoiceDetailDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_top_selling_products_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        invoiceDAO = new InvoiceDAO(this);
        invoiceDetailDAO = new InvoiceDetailDAO(this);

        initUi();
        setDefaultDate();
        setListener();
    }

    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbarTopSellingProductsScreen);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        edtItemCount = findViewById(R.id.edtItemCount);
        rcvTopProducts = findViewById(R.id.rcvTopProducts);
        btnShowList = findViewById(R.id.btnShowList);

        /// Gắn adapter cho RecyclerView để hiển thị danh sách top sản phẩm.
        adapter = new TopSellingProductAdapter(this, new ArrayList<>());
        rcvTopProducts.setLayoutManager(new LinearLayoutManager(this));
        rcvTopProducts.setAdapter(adapter);
        rcvTopProducts.setNestedScrollingEnabled(false);
        rcvTopProducts.setVisibility(View.GONE);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> finish());
        }
        BottomButtonNavigator.bindDefaultButtons(this, BottomButtonNavigator.TAB_HOME);
    }

    private void setListener() {
        tvStartDate.setOnClickListener(v -> openDatePicker(this, tvStartDate));
        tvEndDate.setOnClickListener(v -> openDatePicker(this, tvEndDate));
        btnShowList.setOnClickListener(v -> xuLyBtnShowList());
    }

    private void xuLyBtnShowList() {
        String startDateText = tvStartDate.getText().toString().trim();
        String endDateText = tvEndDate.getText().toString().trim();
        String itemCountText = edtItemCount.getText().toString().trim();

        /// Kiểm tra người dùng đã nhập đủ thông tin trước khi thống kê.
        if (startDateText.isEmpty() || endDateText.isEmpty() || itemCountText.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Date startDate = parseDate(startDateText);
        Date endDate = parseDate(endDateText);

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
            limit = Integer.parseInt(itemCountText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (limit <= 0) {
            Toast.makeText(this, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, TopSellingProductItem> map = new HashMap<>();
        ArrayList<InvoiceDetail> detailList = invoiceDetailDAO.getAllInvoiceDetails();

        /// Gom nhóm theo tên sản phẩm và cộng dồn số lượng bán, doanh thu.
        for (InvoiceDetail detail : detailList) {
            Invoice invoice = invoiceDAO.getInvoiceById(detail.getInvoiceId());
            if (invoice == null) {
                continue;
            }

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

            String productName = detail.getProductName();
            if (productName == null || productName.trim().isEmpty()) {
                continue;
            }

            TopSellingProductItem item = map.get(productName);
            if (item == null) {
                item = new TopSellingProductItem(productName, detail.getImageRes(), 0, 0);
                map.put(productName, item);
            }

            item.setSoldQuantity(item.getSoldQuantity() + detail.getQuantity());
            item.setRevenue(item.getRevenue() + parseAmount(detail.getTotalPrice()));
        }

        List<TopSellingProductItem> list = new ArrayList<>(map.values());

        /// Sắp xếp giảm dần theo số lượng bán, nếu bằng nhau thì doanh thu lớn hơn đứng trước.
        list.sort((o1, o2) -> {
            if (o2.getSoldQuantity() != o1.getSoldQuantity()) {
                return o2.getSoldQuantity() - o1.getSoldQuantity();
            }
            return Long.compare(o2.getRevenue(), o1.getRevenue());
        });

        if (list.size() > limit) {
            list = new ArrayList<>(list.subList(0, limit));
        }

        if (list.isEmpty()) {
            rcvTopProducts.setVisibility(View.GONE);
            Toast.makeText(this, "Không có dữ liệu sản phẩm", Toast.LENGTH_SHORT).show();
        } else {
            adapter.updateItems(list);
            rcvTopProducts.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Đã cập nhật danh sách sản phẩm", Toast.LENGTH_SHORT).show();
        }
    }


    private void setDefaultDate() {
        Date minDate = null;
        Date maxDate = null;

        /// Lấy khoảng ngày mặc định từ các hóa đơn đã thanh toán để demo nhanh hơn.
        ArrayList<Invoice> invoiceList = invoiceDAO.getAllInvoices();
        for (Invoice invoice : invoiceList) {
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
        if (invoiceDetailDAO != null) {
            invoiceDetailDAO.close();
        }
    }

    public static String formatCurrency(long amount) {
        return NumberFormat.getInstance(new Locale("vi", "VN")).format(amount) + " VND";
    }
}
