package com.example.DuAnMau_PH63816.invoice.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.customer.data.CustomerDAO;
import com.example.DuAnMau_PH63816.customer.model.Customer;
import com.example.DuAnMau_PH63816.invoice.model.Invoice;
import com.example.DuAnMau_PH63816.invoice.model.InvoiceDetail;
import com.example.DuAnMau_PH63816.product.ProductImageResolver;
import com.example.DuAnMau_PH63816.product.model.CartItem;
import com.example.DuAnMau_PH63816.product.model.Product;
import com.example.DuAnMau_PH63816.staff.data.StaffDAO;
import com.example.DuAnMau_PH63816.staff.model.Staff;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InvoiceDAO {

    private final InvoiceDbHelper dbHelper;
    private final SQLiteDatabase sqLiteDatabase;
    private final CustomerDAO customerDAO;
    private final InvoiceDetailDAO invoiceDetailDAO;
    private final StaffDAO staffDAO;
    private final Context appContext;

    public InvoiceDAO(Context context) {
        appContext = context.getApplicationContext();
        dbHelper = new InvoiceDbHelper(appContext);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        customerDAO = new CustomerDAO(appContext);
        invoiceDetailDAO = new InvoiceDetailDAO(appContext);
        staffDAO = new StaffDAO(appContext);
        ensureSeedData();
        backfillAccountBuyerSnapshots();
    }

    public ArrayList<Invoice> getAllInvoices() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Invoice ORDER BY id DESC", null);
        ArrayList<Invoice> list = mapInvoices(cursor);
        cursor.close();
        return list;
    }

    public ArrayList<Invoice> getVisibleInvoices() {
        if (isCurrentUserAdmin()) {
            return getAllInvoices();
        }

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM Invoice WHERE customerId = ? ORDER BY id DESC",
                new String[]{getCurrentAccountCustomerId()}
        );
        ArrayList<Invoice> list = mapInvoices(cursor);
        cursor.close();
        return list;
    }

    public Invoice getInvoiceById(int invoiceId) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Invoice WHERE id = ?", new String[]{String.valueOf(invoiceId)});
        Invoice invoice = mapSingleInvoice(cursor);
        cursor.close();
        return invoice;
    }

    public Invoice getVisibleInvoiceById(int invoiceId) {
        if (isCurrentUserAdmin()) {
            return getInvoiceById(invoiceId);
        }

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM Invoice WHERE id = ? AND customerId = ?",
                new String[]{String.valueOf(invoiceId), getCurrentAccountCustomerId()}
        );
        Invoice invoice = mapSingleInvoice(cursor);
        cursor.close();
        return invoice;
    }

    private ArrayList<Invoice> mapInvoices(Cursor cursor) {
        ArrayList<Invoice> list = new ArrayList<>();
        ArrayList<Customer> customers = customerDAO.getAllCustomer();

        if (cursor.moveToFirst()) {
            do {
                list.add(mapInvoice(cursor, customers));
            } while (cursor.moveToNext());
        }
        return list;
    }

    private Invoice mapSingleInvoice(Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return null;
        }

        ArrayList<Customer> customers = customerDAO.getAllCustomer();
        return mapInvoice(cursor, customers);
    }

    public boolean insertInvoice(Invoice invoice) {
        return insertInvoiceAndGetId(invoice) != -1;
    }

    public boolean deleteInvoice(int invoiceId) {
        if (invoiceId <= 0) {
            return false;
        }

        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.delete(
                    "InvoiceDetail",
                    "invoiceId = ?",
                    new String[]{String.valueOf(invoiceId)}
            );

            int deletedRows = sqLiteDatabase.delete(
                    "Invoice",
                    "id = ?",
                    new String[]{String.valueOf(invoiceId)}
            );

            if (deletedRows <= 0) {
                return false;
            }

            sqLiteDatabase.setTransactionSuccessful();
            return true;
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    public int createPaidInvoiceFromCart(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return -1;
        }

        ArrayList<InvoiceDetail> details = new ArrayList<>();
        long totalAmount = 0L;

        for (CartItem cartItem : cartItems) {
            if (cartItem == null || cartItem.getProduct() == null) {
                continue;
            }

            Product product = cartItem.getProduct();
            long unitPrice = parseAmount(product.getPriceLabel());
            long lineTotal = unitPrice * cartItem.getQuantity();
            totalAmount += lineTotal;

            details.add(new InvoiceDetail(
                    0,
                    product.getName(),
                    cartItem.getQuantity(),
                    formatAmount(lineTotal),
                    product.getImage()
            ));
        }

        if (details.isEmpty()) {
            return -1;
        }

        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        Staff currentBuyer = staffDAO.getCurrentStaff();
        Invoice invoice = new Invoice(
                "",
                "ĐÃ THANH TOÁN",
                resolveCheckoutCustomerId(),
                today,
                formatAmount(totalAmount),
                "Tiền mặt",
                resolveCheckoutStaffName()
        );
        invoice.setBuyerName(resolveBuyerName(currentBuyer, invoice.getStaffName()));
        invoice.setBuyerPhone(resolveBuyerPhone(currentBuyer));
        invoice.setBuyerAddress(resolveBuyerAddress(currentBuyer));

        sqLiteDatabase.beginTransaction();
        try {
            long rowId = insertInvoiceAndGetId(invoice);
            if (rowId == -1) {
                return -1;
            }

            int invoiceId = (int) rowId;
            if (!updateInvoiceCode(invoiceId, buildInvoiceCode(invoiceId, today))) {
                return -1;
            }

            for (InvoiceDetail detail : details) {
                detail.setInvoiceId(invoiceId);
                if (!insertInvoiceDetailInternal(detail)) {
                    return -1;
                }
            }

            sqLiteDatabase.setTransactionSuccessful();
            return invoiceId;
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    private long insertInvoiceAndGetId(Invoice invoice) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", invoice.getCode());
        contentValues.put("status", invoice.getStatus());
        contentValues.put("customerId", invoice.getCustomerId());
        contentValues.put("date", invoice.getDate());
        contentValues.put("total", invoice.getTotal());
        contentValues.put("paymentMethod", invoice.getPaymentMethod());
        contentValues.put("staffName", invoice.getStaffName());
        contentValues.put("buyerName", invoice.getBuyerName());
        contentValues.put("buyerPhone", invoice.getBuyerPhone());
        contentValues.put("buyerAddress", invoice.getBuyerAddress());
        return sqLiteDatabase.insert("Invoice", null, contentValues);
    }

    private Customer getCustomerById(ArrayList<Customer> customers, String customerId) {
        for (Customer customer : customers) {
            if (customer.getId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }

    private Invoice mapInvoice(Cursor cursor, ArrayList<Customer> customers) {
        Invoice invoice = new Invoice();
        invoice.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        invoice.setCode(cursor.getString(cursor.getColumnIndexOrThrow("code")));
        invoice.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
        invoice.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow("customerId")));
        invoice.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
        invoice.setTotal(cursor.getString(cursor.getColumnIndexOrThrow("total")));
        invoice.setPaymentMethod(cursor.getString(cursor.getColumnIndexOrThrow("paymentMethod")));
        invoice.setStaffName(cursor.getString(cursor.getColumnIndexOrThrow("staffName")));
        invoice.setBuyerName(getOptionalColumnValue(cursor, "buyerName"));
        invoice.setBuyerPhone(getOptionalColumnValue(cursor, "buyerPhone"));
        invoice.setBuyerAddress(getOptionalColumnValue(cursor, "buyerAddress"));

        Customer customer = getCustomerById(customers, invoice.getCustomerId());
        if (customer != null) {
            invoice.setCustomerName(customer.getName());
            invoice.setCustomerPhone(customer.getPhone());
            invoice.setCustomerAddress(customer.getAddress());
            return invoice;
        }

        if (!isBlank(invoice.getBuyerName()) || !isBlank(invoice.getBuyerPhone()) || !isBlank(invoice.getBuyerAddress())) {
            invoice.setCustomerName(firstNonBlank(invoice.getBuyerName(), resolveFallbackCustomerName(invoice)));
            invoice.setCustomerPhone(firstNonBlank(invoice.getBuyerPhone(), ""));
            invoice.setCustomerAddress(firstNonBlank(invoice.getBuyerAddress(), ""));
            return invoice;
        }

        Staff buyerStaff = getStaffByInvoiceCustomerId(invoice.getCustomerId());
        if (buyerStaff != null) {
            invoice.setCustomerName(resolveBuyerName(buyerStaff, resolveFallbackCustomerName(invoice)));
            invoice.setCustomerPhone(resolveBuyerPhone(buyerStaff));
            invoice.setCustomerAddress(resolveBuyerAddress(buyerStaff));
            return invoice;
        }

        invoice.setCustomerName(resolveFallbackCustomerName(invoice));
        invoice.setCustomerPhone("");
        invoice.setCustomerAddress("");
        return invoice;
    }

    private void backfillAccountBuyerSnapshots() {
        try (Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT id, customerId, buyerName, buyerPhone, buyerAddress FROM Invoice WHERE customerId LIKE 'ACC_%'",
                null
        )) {
            if (!cursor.moveToFirst()) {
                return;
            }

            do {
                int invoiceId = cursor.getInt(0);
                String customerId = cursor.getString(1);
                String buyerName = cursor.getString(2);
                String buyerPhone = cursor.getString(3);
                String buyerAddress = cursor.getString(4);
                if (!isBlank(buyerName) && !isBlank(buyerPhone) && !isBlank(buyerAddress)) {
                    continue;
                }

                Staff buyerStaff = getStaffByInvoiceCustomerId(customerId);
                if (buyerStaff == null) {
                    continue;
                }

                ContentValues contentValues = new ContentValues();
                if (isBlank(buyerName)) {
                    contentValues.put("buyerName", resolveBuyerName(buyerStaff, null));
                }
                if (isBlank(buyerPhone)) {
                    contentValues.put("buyerPhone", resolveBuyerPhone(buyerStaff));
                }
                if (isBlank(buyerAddress)) {
                    contentValues.put("buyerAddress", resolveBuyerAddress(buyerStaff));
                }
                if (contentValues.size() == 0) {
                    continue;
                }

                sqLiteDatabase.update(
                        "Invoice",
                        contentValues,
                        "id = ?",
                        new String[]{String.valueOf(invoiceId)}
                );
            } while (cursor.moveToNext());
        }
    }

    private void ensureSeedData() {
        if (!getAllInvoices().isEmpty()) {
            return;
        }
        /// Mốc ngày demo nhanh cho màn thống kê:
        /// 24/01/2026: KH003 mua hàng
        /// 20/02/2026: KH001 mua hàng
        /// 10/03/2026: KH001 có hóa đơn chờ thanh toán
        /// 12/03/2026: KH001 mua hàng
        /// 26/04/2026: KH002 mua hàng
        /// 09/05/2026: KH002 mua hàng
        /// 14/05/2026: KH002 mua hàng
        /// 23/05/2026: KH002 mua hàng
        /// 25/05/2026: KH001 mua hàng
        /// 27/05/2026: KH003 mua hàng
        /// 28/05/2026: KH001 mua hàng
        /// 29/05/2026: KH002 mua hàng
        /// 30/05/2026: KH003 mua hàng
        /// 31/05/2026: KH001 mua hàng
        /// 08/07/2026: KH003 có hóa đơn đã hủy
        /// 16/08/2026: KH003 có hóa đơn chờ thanh toán
        /// Gợi ý khi demo:
        /// - 01/05/2026 -> 31/05/2026: nhiều khách mua nhất
        /// - 01/01/2026 -> 31/03/2026: dễ thấy dữ liệu theo quý
        /// - 28/05/2026 -> 31/05/2026: dễ demo top khách hàng cuối tháng
        insertSeedInvoice(new Invoice("HD-2026-001", "CHỜ THANH TOÁN", "KH001", "10/03/2026", "545.000k", "Tiền mặt", "Phúc"), getInvoice1Details());
        insertSeedInvoice(new Invoice("HD-2026-002", "ĐÃ THANH TOÁN", "KH002", "09/05/2026", "850.000k", "Chuyển khoản", "Linh"), getInvoice2Details());
        insertSeedInvoice(new Invoice("HD-2026-003", "ĐÃ HỦY", "KH003", "08/07/2026", "940.000k", "Tiền mặt", "Hà"), getInvoice3Details());
        insertSeedInvoice(new Invoice("HD-2026-004", "ĐÃ THANH TOÁN", "KH001", "12/03/2026", "420.000k", "Tiền mặt", "Phúc"), getInvoice4Details());
        insertSeedInvoice(new Invoice("HD-2026-005", "ĐÃ THANH TOÁN", "KH002", "14/05/2026", "675.000k", "Chuyển khoản", "Linh"), getInvoice5Details());
        insertSeedInvoice(new Invoice("HD-2026-006", "CHỜ THANH TOÁN", "KH003", "16/08/2026", "180.000k", "Tiền mặt", "Hà"), getInvoice6Details());
        insertSeedInvoice(new Invoice("HD-2026-007", "ĐÃ THANH TOÁN", "KH001", "20/02/2026", "1.250.000k", "Chuyển khoản", "Phúc"), getInvoice7Details());
        insertSeedInvoice(new Invoice("HD-2026-008", "ĐÃ THANH TOÁN", "KH002", "23/05/2026", "560.000k", "Tiền mặt", "Linh"), getInvoice8Details());
        insertSeedInvoice(new Invoice("HD-2026-009", "ĐÃ THANH TOÁN", "KH003", "24/01/2026", "935.000k", "Tiền mặt", "Hà"), getInvoice9Details());
        insertSeedInvoice(new Invoice("HD-2026-010", "ĐÃ THANH TOÁN", "KH001", "25/05/2026", "1.175.000k", "Chuyển khoản", "Phúc"), getInvoice10Details());
        insertSeedInvoice(new Invoice("HD-2026-011", "ĐÃ THANH TOÁN", "KH002", "26/04/2026", "1.190.000k", "Tiền mặt", "Linh"), getInvoice11Details());
        insertSeedInvoice(new Invoice("HD-2026-012", "ĐÃ THANH TOÁN", "KH003", "27/05/2026", "760.000k", "Chuyển khoản", "Hà"), getInvoice12Details());
        insertSeedInvoice(new Invoice("HD-2026-013", "ĐÃ THANH TOÁN", "KH001", "28/05/2026", "980.000k", "Tiền mặt", "Phúc"), getInvoice13Details());
        insertSeedInvoice(new Invoice("HD-2026-014", "ĐÃ THANH TOÁN", "KH002", "29/05/2026", "745.000k", "Chuyển khoản", "Linh"), getInvoice14Details());
        insertSeedInvoice(new Invoice("HD-2026-015", "ĐÃ THANH TOÁN", "KH003", "30/05/2026", "1.060.000k", "Tiền mặt", "Hà"), getInvoice15Details());
        insertSeedInvoice(new Invoice("HD-2026-016", "ĐÃ THANH TOÁN", "KH001", "31/05/2026", "635.000k", "Chuyển khoản", "Phúc"), getInvoice16Details());
    }

    private void insertSeedInvoice(Invoice invoice, ArrayList<InvoiceDetail> details) {
        long invoiceId = insertInvoiceAndGetId(invoice);
        if (invoiceId != -1) {
            insertSeedDetails((int) invoiceId, details);
        }
    }

    private void insertSeedDetails(int invoiceId, ArrayList<InvoiceDetail> details) {
        for (InvoiceDetail detail : details) {
            detail.setInvoiceId(invoiceId);
            insertInvoiceDetailInternal(detail);
        }
    }

    private ArrayList<InvoiceDetail> getInvoice1Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Mì Ramen Tonkotsu", 2, "500.000k", R.drawable.ic_ramen));
        details.add(new InvoiceDetail(0, "Kem Matcha Premium", 1, "45.000k", R.drawable.ic_icream_matcha));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice2Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Sushi Set Omakase", 1, "850.000k", R.drawable.ic_set_sushi));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice3Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Sushi Set Omakase", 1, "850.000k", R.drawable.ic_set_sushi));
        details.add(new InvoiceDetail(0, "Kem Matcha Premium", 2, "90.000k", R.drawable.ic_icream_matcha));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice4Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Mì Ramen Tonkotsu", 1, "250.000k", R.drawable.ic_ramen));
        details.add(new InvoiceDetail(0, "Kem Matcha Premium", 2, "90.000k", R.drawable.ic_icream_matcha));
        details.add(new InvoiceDetail(0, "Nước suối", 2, "80.000k", R.drawable.img_nuoc_suoi));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice5Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Sushi Set Omakase", 1, "850.000k", R.drawable.ic_set_sushi));
        details.add(new InvoiceDetail(0, "Voucher giảm giá", 1, "-175.000k", R.drawable.logo));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice6Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Kem Matcha Premium", 4, "180.000k", R.drawable.ic_icream_matcha));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice7Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Sushi Set Omakase", 1, "850.000k", R.drawable.ic_set_sushi));
        details.add(new InvoiceDetail(0, "Mì Ramen Tonkotsu", 1, "250.000k", R.drawable.ic_ramen));
        details.add(new InvoiceDetail(0, "Kem Matcha Premium", 3, "135.000k", R.drawable.ic_icream_matcha));
        details.add(new InvoiceDetail(0, "Phụ phí phục vụ", 1, "15.000k", R.drawable.logo));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice8Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Mì Ramen Tonkotsu", 2, "500.000k", R.drawable.ic_ramen));
        details.add(new InvoiceDetail(0, "Nước suối", 3, "60.000k", R.drawable.img_nuoc_suoi));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice9Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Kem Matcha Premium", 3, "135.000k", R.drawable.ic_icream_matcha));
        details.add(new InvoiceDetail(0, "Mì Ramen Tonkotsu", 2, "500.000k", R.drawable.ic_ramen));
        details.add(new InvoiceDetail(0, "Nước suối", 2, "80.000k", R.drawable.img_nuoc_suoi));
        details.add(new InvoiceDetail(0, "Salad cá hồi", 1, "165.000k", R.drawable.img_salad));
        details.add(new InvoiceDetail(0, "Trà đào cam sả", 1, "55.000k", R.drawable.img_tra_dao));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice10Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Sushi Set Omakase", 1, "850.000k", R.drawable.ic_set_sushi));
        details.add(new InvoiceDetail(0, "Kem Matcha Premium", 1, "45.000k", R.drawable.ic_icream_matcha));
        details.add(new InvoiceDetail(0, "Nước suối", 3, "60.000k", R.drawable.img_nuoc_suoi));
        details.add(new InvoiceDetail(0, "Takoyaki sốt mayo", 2, "190.000k", R.drawable.img_takoyaki));
        details.add(new InvoiceDetail(0, "Trà đào cam sả", 1, "30.000k", R.drawable.img_tra_dao));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice11Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Mì Ramen Tonkotsu", 3, "750.000k", R.drawable.ic_ramen));
        details.add(new InvoiceDetail(0, "Kem Matcha Premium", 4, "180.000k", R.drawable.ic_icream_matcha));
        details.add(new InvoiceDetail(0, "Phụ phí phục vụ", 1, "15.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Nước suối", 3, "60.000k", R.drawable.img_nuoc_suoi));
        details.add(new InvoiceDetail(0, "Salad cá hồi", 1, "165.000k", R.drawable.img_salad));
        details.add(new InvoiceDetail(0, "Topping trứng ngâm", 1, "20.000k", R.drawable.img_trung_ngam));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice12Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Mì Ramen Tonkotsu", 1, "250.000k", R.drawable.ic_ramen));
        details.add(new InvoiceDetail(0, "Kem Matcha Premium", 4, "180.000k", R.drawable.ic_icream_matcha));
        details.add(new InvoiceDetail(0, "Nước suối", 3, "60.000k", R.drawable.img_nuoc_suoi));
        details.add(new InvoiceDetail(0, "Voucher giảm giá", 1, "-120.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Phụ phí phục vụ", 1, "15.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Topping trứng ngâm", 2, "50.000k", R.drawable.img_trung_ngam));
        details.add(new InvoiceDetail(0, "Trà đào cam sả", 3, "165.000k", R.drawable.img_tra_dao));
        details.add(new InvoiceDetail(0, "Takoyaki sốt mayo", 2, "160.000k", R.drawable.img_takoyaki));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice13Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Sushi Set Omakase", 1, "850.000k", R.drawable.ic_set_sushi));
        details.add(new InvoiceDetail(0, "Nước suối", 2, "40.000k", R.drawable.img_nuoc_suoi));
        details.add(new InvoiceDetail(0, "Trà đào cam sả", 1, "55.000k", R.drawable.img_tra_dao));
        details.add(new InvoiceDetail(0, "Topping trứng ngâm", 1, "35.000k", R.drawable.img_trung_ngam));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice14Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Mì Ramen Tonkotsu", 2, "500.000k", R.drawable.ic_ramen));
        details.add(new InvoiceDetail(0, "Kem Matcha Premium", 3, "135.000k", R.drawable.ic_icream_matcha));
        details.add(new InvoiceDetail(0, "Takoyaki sốt mayo", 1, "95.000k", R.drawable.img_takoyaki));
        details.add(new InvoiceDetail(0, "Nước suối", 1, "15.000k", R.drawable.img_nuoc_suoi));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice15Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Sushi Set Omakase", 1, "850.000k", R.drawable.ic_set_sushi));
        details.add(new InvoiceDetail(0, "Salad cá hồi", 1, "165.000k", R.drawable.img_salad));
        details.add(new InvoiceDetail(0, "Nước suối", 2, "45.000k", R.drawable.img_nuoc_suoi));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice16Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Mì Ramen Tonkotsu", 2, "500.000k", R.drawable.ic_ramen));
        details.add(new InvoiceDetail(0, "Takoyaki sốt mayo", 1, "95.000k", R.drawable.img_takoyaki));
        details.add(new InvoiceDetail(0, "Trà đào cam sả", 1, "40.000k", R.drawable.img_tra_dao));
        return details;
    }

    private boolean insertInvoiceDetailInternal(InvoiceDetail detail) {
        String normalizedImage = resolveInvoiceDetailImage(detail);
        ContentValues contentValues = new ContentValues();
        contentValues.put("invoiceId", detail.getInvoiceId());
        contentValues.put("productName", detail.getProductName());
        contentValues.put("quantity", detail.getQuantity());
        contentValues.put("totalPrice", detail.getTotalPrice());
        contentValues.put("imageRes", ProductImageResolver.resolveDrawableResId(appContext, normalizedImage));
        contentValues.put("image", normalizedImage);
        return sqLiteDatabase.insert("InvoiceDetail", null, contentValues) != -1;
    }

    private String resolveInvoiceDetailImage(InvoiceDetail detail) {
        String rawImage = detail.getImage();
        if ((rawImage == null || rawImage.trim().isEmpty()) && detail.getImageRes() != 0) {
            rawImage = String.valueOf(detail.getImageRes());
        }
        return ProductImageResolver.normalizeForStorage(appContext, detail.getProductName(), rawImage);
    }

    private boolean updateInvoiceCode(int invoiceId, String code) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        return sqLiteDatabase.update(
                "Invoice",
                contentValues,
                "id = ?",
                new String[]{String.valueOf(invoiceId)}
        ) > 0;
    }

    private String resolveCheckoutCustomerId() {
        return getCurrentAccountCustomerId();
    }

    private String resolveCheckoutStaffName() {
        String staffName = staffDAO.getCurrentStaffName();
        if (staffName != null && !staffName.trim().isEmpty()) {
            return staffName;
        }
        return "Phúc";
    }

    private String resolveFallbackCustomerName(Invoice invoice) {
        if (invoice != null && !isBlank(invoice.getBuyerName())) {
            return invoice.getBuyerName().trim();
        }
        if (invoice != null && invoice.getStaffName() != null && !invoice.getStaffName().trim().isEmpty()) {
            return invoice.getStaffName();
        }
        return "Khách lẻ";
    }

    private Staff getStaffByInvoiceCustomerId(String customerId) {
        if (customerId == null || !customerId.startsWith("ACC_")) {
            return null;
        }
        String staffCode = customerId.substring(4).trim();
        if (staffCode.isEmpty() || "GUEST".equalsIgnoreCase(staffCode)) {
            return null;
        }
        return staffDAO.getStaffByCode(staffCode);
    }

    private String resolveBuyerName(Staff staff, String fallbackName) {
        if (staff != null && !isBlank(staff.getNameStaff())) {
            return staff.getNameStaff().trim();
        }
        if (!isBlank(fallbackName)) {
            return fallbackName.trim();
        }
        return "Khách lẻ";
    }

    private String resolveBuyerPhone(Staff staff) {
        if (staff == null) {
            return "";
        }
        if (!isBlank(staff.getPhone())) {
            return staff.getPhone().trim();
        }
        if (!isBlank(staff.getNameLogin()) && looksLikePhone(staff.getNameLogin())) {
            return staff.getNameLogin().trim();
        }
        return "";
    }

    private String resolveBuyerAddress(Staff staff) {
        if (staff == null || isBlank(staff.getAddress())) {
            return "";
        }
        return staff.getAddress().trim();
    }

    private String getOptionalColumnValue(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index < 0 || cursor.isNull(index)) {
            return null;
        }
        return cursor.getString(index);
    }

    private String firstNonBlank(String primary, String fallback) {
        if (!isBlank(primary)) {
            return primary.trim();
        }
        return fallback == null ? "" : fallback;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean looksLikePhone(String value) {
        return value != null && value.trim().matches("[0-9+\\-\\s]{8,15}");
    }

    private boolean isCurrentUserAdmin() {
        return appContext.getSharedPreferences("StaffData", Context.MODE_PRIVATE)
                .getInt("role", 1) == 0;
    }

    private String getCurrentAccountCustomerId() {
        String staffCode = staffDAO.getCurrentStaffCode();
        if (staffCode != null && !staffCode.trim().isEmpty()) {
            return "ACC_" + staffCode;
        }
        return "ACC_GUEST";
    }

    private String buildInvoiceCode(int invoiceId, String dateText) {
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        if (dateText != null && dateText.length() >= 4) {
            year = dateText.substring(dateText.length() - 4);
        }
        return String.format(Locale.getDefault(), "HD-%s-%03d", year, invoiceId);
    }

    private long parseAmount(String amountText) {
        if (amountText == null || amountText.trim().isEmpty()) {
            return 0L;
        }

        String digits = amountText.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) {
            return 0L;
        }

        try {
            return Long.parseLong(digits);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    private String formatAmount(long amount) {
        return NumberFormat.getNumberInstance(Locale.GERMANY).format(amount) + "k";
    }

    public void close() {
        if (invoiceDetailDAO != null) {
            invoiceDetailDAO.close();
        }
        if (staffDAO != null) {
            staffDAO.close();
        }
        if (customerDAO != null) {
            customerDAO.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
