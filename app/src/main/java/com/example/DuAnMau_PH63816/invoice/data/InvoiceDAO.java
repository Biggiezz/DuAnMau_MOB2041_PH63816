package com.example.DuAnMau_PH63816.invoice.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.common.data.AppDbHelper;
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
import java.util.Locale;

public class InvoiceDAO {

    private final Context appContext;
    private final AppDbHelper dbHelper;
    private final SQLiteDatabase sqLiteDatabase;
    private final CustomerDAO customerDAO;
    private final InvoiceDetailDAO invoiceDetailDAO;
    private final StaffDAO staffDAO;

    public InvoiceDAO(Context context) {
        appContext = context.getApplicationContext();
        dbHelper = new AppDbHelper(appContext);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        customerDAO = new CustomerDAO(appContext);
        invoiceDetailDAO = new InvoiceDetailDAO(appContext);
        staffDAO = new StaffDAO(appContext);
        ensureSeedData();
        backfillAccountBuyerSnapshots();
    }

    public ArrayList<Invoice> getAllInvoices() {
        ArrayList<Invoice> list = new ArrayList<>();
        ArrayList<Customer> customerList = customerDAO.getAllCustomer();
        String sql = "SELECT * FROM Invoice ORDER BY id DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(mapInvoice(cursor, customerList));
                cursor.moveToNext();
            }
        }

        cursor.close();
        return list;
    }

    public ArrayList<Invoice> getVisibleInvoices() {
        int role = appContext.getSharedPreferences("StaffData", Context.MODE_PRIVATE).getInt("role", 1);
        if (role == 0) {
            return getAllInvoices();
        }

        String staffCode = staffDAO.getCurrentStaffCode();
        String customerId = "ACC_GUEST";
        if (!isBlank(staffCode)) {
            customerId = "ACC_" + staffCode;
        }

        ArrayList<Invoice> list = new ArrayList<>();
        ArrayList<Customer> customerList = customerDAO.getAllCustomer();
        String sql = "SELECT * FROM Invoice WHERE customerId = ? ORDER BY id DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{customerId});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(mapInvoice(cursor, customerList));
                cursor.moveToNext();
            }
        }

        cursor.close();
        return list;
    }

    public Invoice getInvoiceById(int invoiceId) {
        Invoice invoice = null;
        ArrayList<Customer> customerList = customerDAO.getAllCustomer();
        String sql = "SELECT * FROM Invoice WHERE id = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{String.valueOf(invoiceId)});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            invoice = mapInvoice(cursor, customerList);
        }

        cursor.close();
        return invoice;
    }

    public Invoice getVisibleInvoiceById(int invoiceId) {
        int role = appContext.getSharedPreferences("StaffData", Context.MODE_PRIVATE).getInt("role", 1);
        if (role == 0) {
            return getInvoiceById(invoiceId);
        }

        String staffCode = staffDAO.getCurrentStaffCode();
        String customerId = "ACC_GUEST";
        if (!isBlank(staffCode)) {
            customerId = "ACC_" + staffCode;
        }

        Invoice invoice = null;
        ArrayList<Customer> customerList = customerDAO.getAllCustomer();
        String sql = "SELECT * FROM Invoice WHERE id = ? AND customerId = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{String.valueOf(invoiceId), customerId});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            invoice = mapInvoice(cursor, customerList);
        }

        cursor.close();
        return invoice;
    }

    public boolean deleteInvoice(int invoiceId) {
        if (invoiceId <= 0) {
            return false;
        }

        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.delete("InvoiceDetail", "invoiceId = ?", new String[]{String.valueOf(invoiceId)});
            int kq = sqLiteDatabase.delete("Invoice", "id = ?", new String[]{String.valueOf(invoiceId)});
            if (kq > 0) {
                sqLiteDatabase.setTransactionSuccessful();
                return true;
            }
            return false;
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    public int createPaidInvoiceFromCart(ArrayList<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return -1;
        }

        ArrayList<InvoiceDetail> detailList = new ArrayList<>();
        long tongTien = 0;

        for (CartItem cartItem : cartItems) {
            if (cartItem == null || cartItem.getProduct() == null) {
                continue;
            }

            Product product = cartItem.getProduct();
            long donGia = parseAmount(product.getPriceLabel());
            long thanhTien = donGia * cartItem.getQuantity();
            tongTien += thanhTien;

            detailList.add(new InvoiceDetail(
                    0,
                    product.getName(),
                    cartItem.getQuantity(),
                    formatAmount(thanhTien),
                    product.getImage()
            ));
        }

        if (detailList.isEmpty()) {
            return -1;
        }

        String staffCode = staffDAO.getCurrentStaffCode();
        String customerId = "ACC_GUEST";
        if (!isBlank(staffCode)) {
            customerId = "ACC_" + staffCode;
        }

        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String staffName = staffDAO.getCurrentStaffName();
        if (isBlank(staffName)) {
            staffName = "Phúc";
        }

        Staff buyer = staffDAO.getCurrentStaff();
        Invoice invoice = new Invoice("", "ĐÃ THANH TOÁN", customerId, today, formatAmount(tongTien), "Tiền mặt", staffName);

        if (buyer != null && !isBlank(buyer.getNameStaff())) {
            invoice.setBuyerName(buyer.getNameStaff().trim());
        } else if (!isBlank(staffName)) {
            invoice.setBuyerName(staffName.trim());
        } else {
            invoice.setBuyerName("Khách lẻ");
        }

        if (buyer != null && !isBlank(buyer.getPhone())) {
            invoice.setBuyerPhone(buyer.getPhone().trim());
        } else if (buyer != null && !isBlank(buyer.getNameLogin()) && looksLikePhone(buyer.getNameLogin())) {
            invoice.setBuyerPhone(buyer.getNameLogin().trim());
        } else {
            invoice.setBuyerPhone("");
        }

        if (buyer != null && !isBlank(buyer.getAddress())) {
            invoice.setBuyerAddress(buyer.getAddress().trim());
        } else {
            invoice.setBuyerAddress("");
        }

        sqLiteDatabase.beginTransaction();
        try {
            long rowId = insertInvoiceAndGetId(invoice);
            if (rowId == -1) {
                return -1;
            }

            int invoiceId = (int) rowId;
            String nam = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            if (today.length() >= 4) {
                nam = today.substring(today.length() - 4);
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put("code", String.format(Locale.getDefault(), "HD-%s-%03d", nam, invoiceId));
            int kq = sqLiteDatabase.update("Invoice", contentValues, "id = ?", new String[]{String.valueOf(invoiceId)});
            if (kq <= 0) {
                return -1;
            }

            for (InvoiceDetail detail : detailList) {
                detail.setInvoiceId(invoiceId);
                if (!insertInvoiceDetail(detail)) {
                    return -1;
                }
            }

            sqLiteDatabase.setTransactionSuccessful();
            return invoiceId;
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    private Invoice mapInvoice(Cursor cursor, ArrayList<Customer> customerList) {
        Invoice invoice = new Invoice();
        invoice.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        invoice.setCode(cursor.getString(cursor.getColumnIndexOrThrow("code")));
        invoice.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
        invoice.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow("customerId")));
        invoice.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
        invoice.setTotal(cursor.getString(cursor.getColumnIndexOrThrow("total")));
        invoice.setPaymentMethod(cursor.getString(cursor.getColumnIndexOrThrow("paymentMethod")));
        invoice.setStaffName(cursor.getString(cursor.getColumnIndexOrThrow("staffName")));

        int buyerNameIndex = cursor.getColumnIndex("buyerName");
        if (buyerNameIndex >= 0 && !cursor.isNull(buyerNameIndex)) {
            invoice.setBuyerName(cursor.getString(buyerNameIndex));
        }

        int buyerPhoneIndex = cursor.getColumnIndex("buyerPhone");
        if (buyerPhoneIndex >= 0 && !cursor.isNull(buyerPhoneIndex)) {
            invoice.setBuyerPhone(cursor.getString(buyerPhoneIndex));
        }

        int buyerAddressIndex = cursor.getColumnIndex("buyerAddress");
        if (buyerAddressIndex >= 0 && !cursor.isNull(buyerAddressIndex)) {
            invoice.setBuyerAddress(cursor.getString(buyerAddressIndex));
        }

        for (Customer customer : customerList) {
            if (invoice.getCustomerId() != null && invoice.getCustomerId().equals(customer.getId())) {
                invoice.setCustomerName(customer.getName());
                invoice.setCustomerPhone(customer.getPhone());
                invoice.setCustomerAddress(customer.getAddress());
                return invoice;
            }
        }

        if (!isBlank(invoice.getBuyerName()) || !isBlank(invoice.getBuyerPhone()) || !isBlank(invoice.getBuyerAddress())) {
            if (!isBlank(invoice.getBuyerName())) {
                invoice.setCustomerName(invoice.getBuyerName().trim());
            } else if (!isBlank(invoice.getStaffName())) {
                invoice.setCustomerName(invoice.getStaffName().trim());
            } else {
                invoice.setCustomerName("Khách lẻ");
            }

            if (!isBlank(invoice.getBuyerPhone())) {
                invoice.setCustomerPhone(invoice.getBuyerPhone().trim());
            } else {
                invoice.setCustomerPhone("");
            }

            if (!isBlank(invoice.getBuyerAddress())) {
                invoice.setCustomerAddress(invoice.getBuyerAddress().trim());
            } else {
                invoice.setCustomerAddress("");
            }
            return invoice;
        }

        Staff buyerStaff = getStaffByInvoiceCustomerId(invoice.getCustomerId());
        if (buyerStaff != null) {
            if (!isBlank(buyerStaff.getNameStaff())) {
                invoice.setCustomerName(buyerStaff.getNameStaff().trim());
            } else if (!isBlank(invoice.getStaffName())) {
                invoice.setCustomerName(invoice.getStaffName().trim());
            } else {
                invoice.setCustomerName("Khách lẻ");
            }

            if (!isBlank(buyerStaff.getPhone())) {
                invoice.setCustomerPhone(buyerStaff.getPhone().trim());
            } else if (!isBlank(buyerStaff.getNameLogin()) && looksLikePhone(buyerStaff.getNameLogin())) {
                invoice.setCustomerPhone(buyerStaff.getNameLogin().trim());
            } else {
                invoice.setCustomerPhone("");
            }

            if (!isBlank(buyerStaff.getAddress())) {
                invoice.setCustomerAddress(buyerStaff.getAddress().trim());
            } else {
                invoice.setCustomerAddress("");
            }
            return invoice;
        }

        if (!isBlank(invoice.getStaffName())) {
            invoice.setCustomerName(invoice.getStaffName().trim());
        } else {
            invoice.setCustomerName("Khách lẻ");
        }
        invoice.setCustomerPhone("");
        invoice.setCustomerAddress("");
        return invoice;
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

    private boolean insertInvoiceDetail(InvoiceDetail detail) {
        String image = detail.getImage();
        if ((image == null || image.trim().isEmpty()) && detail.getImageRes() != 0) {
            image = String.valueOf(detail.getImageRes());
        }
        image = ProductImageResolver.normalizeForStorage(appContext, detail.getProductName(), image);

        ContentValues contentValues = new ContentValues();
        contentValues.put("invoiceId", detail.getInvoiceId());
        contentValues.put("productName", detail.getProductName());
        contentValues.put("quantity", detail.getQuantity());
        contentValues.put("totalPrice", detail.getTotalPrice());
        contentValues.put("imageRes", ProductImageResolver.resolveDrawableResId(appContext, image));
        contentValues.put("image", image);
        long kq = sqLiteDatabase.insert("InvoiceDetail", null, contentValues);
        return kq != -1;
    }
    private void backfillAccountBuyerSnapshots() {
        String sql = "SELECT id, customerId, buyerName, buyerPhone, buyerAddress FROM Invoice WHERE customerId LIKE 'ACC_%'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int invoiceId = cursor.getInt(0);
                String customerId = cursor.getString(1);
                String buyerName = cursor.getString(2);
                String buyerPhone = cursor.getString(3);
                String buyerAddress = cursor.getString(4);
                if (!isBlank(buyerName) && !isBlank(buyerPhone) && !isBlank(buyerAddress)) {
                    cursor.moveToNext();
                    continue;
                }

                Staff buyerStaff = getStaffByInvoiceCustomerId(customerId);
                if (buyerStaff == null) {
                    cursor.moveToNext();
                    continue;
                }

                ContentValues contentValues = new ContentValues();
                if (isBlank(buyerName)) {
                    if (!isBlank(buyerStaff.getNameStaff())) {
                        contentValues.put("buyerName", buyerStaff.getNameStaff().trim());
                    } else {
                        contentValues.put("buyerName", "Khách lẻ");
                    }
                }
                if (isBlank(buyerPhone)) {
                    if (!isBlank(buyerStaff.getPhone())) {
                        contentValues.put("buyerPhone", buyerStaff.getPhone().trim());
                    } else if (!isBlank(buyerStaff.getNameLogin()) && looksLikePhone(buyerStaff.getNameLogin())) {
                        contentValues.put("buyerPhone", buyerStaff.getNameLogin().trim());
                    } else {
                        contentValues.put("buyerPhone", "");
                    }
                }
                if (isBlank(buyerAddress)) {
                    if (!isBlank(buyerStaff.getAddress())) {
                        contentValues.put("buyerAddress", buyerStaff.getAddress().trim());
                    } else {
                        contentValues.put("buyerAddress", "");
                    }
                }

                if (contentValues.size() > 0) {
                    sqLiteDatabase.update("Invoice", contentValues, "id = ?", new String[]{String.valueOf(invoiceId)});
                }

                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    private void ensureSeedData() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM Invoice", null);
        boolean isEmpty = true;
        if (cursor.moveToFirst()) {
            isEmpty = cursor.getInt(0) == 0;
        }
        cursor.close();

        if (!isEmpty) {
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
        insertSeedInvoice(new Invoice("HD-2026-001", "CHỜ THANH TOÁN", "KH001", "10/03/2026", "545.000k", "Tiền mặt", "Phúc"),
                new InvoiceDetail(0, "Mì Ramen Tonkotsu", 2, "500.000k", R.drawable.ic_ramen),
                new InvoiceDetail(0, "Kem Matcha Premium", 1, "45.000k", R.drawable.ic_icream_matcha));

        insertSeedInvoice(new Invoice("HD-2026-002", "ĐÃ THANH TOÁN", "KH002", "09/05/2026", "850.000k", "Chuyển khoản", "Linh"),
                new InvoiceDetail(0, "Sushi Set Omakase", 1, "850.000k", R.drawable.ic_set_sushi));

        insertSeedInvoice(new Invoice("HD-2026-003", "ĐÃ HỦY", "KH003", "08/07/2026", "940.000k", "Tiền mặt", "Hà"),
                new InvoiceDetail(0, "Sushi Set Omakase", 1, "850.000k", R.drawable.ic_set_sushi),
                new InvoiceDetail(0, "Kem Matcha Premium", 2, "90.000k", R.drawable.ic_icream_matcha));

        insertSeedInvoice(new Invoice("HD-2026-004", "ĐÃ THANH TOÁN", "KH001", "12/03/2026", "420.000k", "Tiền mặt", "Phúc"),
                new InvoiceDetail(0, "Mì Ramen Tonkotsu", 1, "250.000k", R.drawable.ic_ramen),
                new InvoiceDetail(0, "Kem Matcha Premium", 2, "90.000k", R.drawable.ic_icream_matcha),
                new InvoiceDetail(0, "Nước suối", 2, "80.000k", R.drawable.img_nuoc_suoi));

        insertSeedInvoice(new Invoice("HD-2026-005", "ĐÃ THANH TOÁN", "KH002", "14/05/2026", "675.000k", "Chuyển khoản", "Linh"),
                new InvoiceDetail(0, "Sushi Set Omakase", 1, "850.000k", R.drawable.ic_set_sushi),
                new InvoiceDetail(0, "Voucher giảm giá", 1, "-175.000k", R.drawable.logo));

        insertSeedInvoice(new Invoice("HD-2026-006", "CHỜ THANH TOÁN", "KH003", "16/08/2026", "180.000k", "Tiền mặt", "Hà"),
                new InvoiceDetail(0, "Kem Matcha Premium", 4, "180.000k", R.drawable.ic_icream_matcha));

        insertSeedInvoice(new Invoice("HD-2026-007", "ĐÃ THANH TOÁN", "KH001", "20/02/2026", "1.250.000k", "Chuyển khoản", "Phúc"),
                new InvoiceDetail(0, "Sushi Set Omakase", 1, "850.000k", R.drawable.ic_set_sushi),
                new InvoiceDetail(0, "Mì Ramen Tonkotsu", 1, "250.000k", R.drawable.ic_ramen),
                new InvoiceDetail(0, "Kem Matcha Premium", 3, "135.000k", R.drawable.ic_icream_matcha),
                new InvoiceDetail(0, "Phụ phí phục vụ", 1, "15.000k", R.drawable.logo));

        insertSeedInvoice(new Invoice("HD-2026-008", "ĐÃ THANH TOÁN", "KH002", "23/05/2026", "560.000k", "Tiền mặt", "Linh"),
                new InvoiceDetail(0, "Mì Ramen Tonkotsu", 2, "500.000k", R.drawable.ic_ramen),
                new InvoiceDetail(0, "Nước suối", 3, "60.000k", R.drawable.img_nuoc_suoi));

        insertSeedInvoice(new Invoice("HD-2026-009", "ĐÃ THANH TOÁN", "KH003", "24/01/2026", "935.000k", "Tiền mặt", "Hà"),
                new InvoiceDetail(0, "Kem Matcha Premium", 3, "135.000k", R.drawable.ic_icream_matcha),
                new InvoiceDetail(0, "Mì Ramen Tonkotsu", 2, "500.000k", R.drawable.ic_ramen),
                new InvoiceDetail(0, "Nước suối", 2, "80.000k", R.drawable.img_nuoc_suoi),
                new InvoiceDetail(0, "Salad cá hồi", 1, "165.000k", R.drawable.img_salad),
                new InvoiceDetail(0, "Trà đào cam sả", 1, "55.000k", R.drawable.img_tra_dao));

        insertSeedInvoice(new Invoice("HD-2026-010", "ĐÃ THANH TOÁN", "KH001", "25/05/2026", "1.175.000k", "Chuyển khoản", "Phúc"),
                new InvoiceDetail(0, "Sushi Set Omakase", 1, "850.000k", R.drawable.ic_set_sushi),
                new InvoiceDetail(0, "Kem Matcha Premium", 1, "45.000k", R.drawable.ic_icream_matcha),
                new InvoiceDetail(0, "Nước suối", 3, "60.000k", R.drawable.img_nuoc_suoi),
                new InvoiceDetail(0, "Takoyaki sốt mayo", 2, "190.000k", R.drawable.img_takoyaki),
                new InvoiceDetail(0, "Trà đào cam sả", 1, "30.000k", R.drawable.img_tra_dao));

        insertSeedInvoice(new Invoice("HD-2026-011", "ĐÃ THANH TOÁN", "KH002", "26/04/2026", "1.190.000k", "Tiền mặt", "Linh"),
                new InvoiceDetail(0, "Mì Ramen Tonkotsu", 3, "750.000k", R.drawable.ic_ramen),
                new InvoiceDetail(0, "Kem Matcha Premium", 4, "180.000k", R.drawable.ic_icream_matcha),
                new InvoiceDetail(0, "Phụ phí phục vụ", 1, "15.000k", R.drawable.logo),
                new InvoiceDetail(0, "Nước suối", 3, "60.000k", R.drawable.img_nuoc_suoi),
                new InvoiceDetail(0, "Salad cá hồi", 1, "165.000k", R.drawable.img_salad),
                new InvoiceDetail(0, "Topping trứng ngâm", 1, "20.000k", R.drawable.img_trung_ngam));

        insertSeedInvoice(new Invoice("HD-2026-012", "ĐÃ THANH TOÁN", "KH003", "27/05/2026", "760.000k", "Chuyển khoản", "Hà"),
                new InvoiceDetail(0, "Mì Ramen Tonkotsu", 1, "250.000k", R.drawable.ic_ramen),
                new InvoiceDetail(0, "Kem Matcha Premium", 4, "180.000k", R.drawable.ic_icream_matcha),
                new InvoiceDetail(0, "Nước suối", 3, "60.000k", R.drawable.img_nuoc_suoi),
                new InvoiceDetail(0, "Voucher giảm giá", 1, "-120.000k", R.drawable.logo),
                new InvoiceDetail(0, "Phụ phí phục vụ", 1, "15.000k", R.drawable.logo),
                new InvoiceDetail(0, "Topping trứng ngâm", 2, "50.000k", R.drawable.img_trung_ngam),
                new InvoiceDetail(0, "Trà đào cam sả", 3, "165.000k", R.drawable.img_tra_dao),
                new InvoiceDetail(0, "Takoyaki sốt mayo", 2, "160.000k", R.drawable.img_takoyaki));

        insertSeedInvoice(new Invoice("HD-2026-013", "ĐÃ THANH TOÁN", "KH001", "28/05/2026", "980.000k", "Tiền mặt", "Phúc"),
                new InvoiceDetail(0, "Sushi Set Omakase", 1, "850.000k", R.drawable.ic_set_sushi),
                new InvoiceDetail(0, "Nước suối", 2, "40.000k", R.drawable.img_nuoc_suoi),
                new InvoiceDetail(0, "Trà đào cam sả", 1, "55.000k", R.drawable.img_tra_dao),
                new InvoiceDetail(0, "Topping trứng ngâm", 1, "35.000k", R.drawable.img_trung_ngam));

        insertSeedInvoice(new Invoice("HD-2026-014", "ĐÃ THANH TOÁN", "KH002", "29/05/2026", "745.000k", "Chuyển khoản", "Linh"),
                new InvoiceDetail(0, "Mì Ramen Tonkotsu", 2, "500.000k", R.drawable.ic_ramen),
                new InvoiceDetail(0, "Kem Matcha Premium", 3, "135.000k", R.drawable.ic_icream_matcha),
                new InvoiceDetail(0, "Takoyaki sốt mayo", 1, "95.000k", R.drawable.img_takoyaki),
                new InvoiceDetail(0, "Nước suối", 1, "15.000k", R.drawable.img_nuoc_suoi));

        insertSeedInvoice(new Invoice("HD-2026-015", "ĐÃ THANH TOÁN", "KH003", "30/05/2026", "1.060.000k", "Tiền mặt", "Hà"),
                new InvoiceDetail(0, "Sushi Set Omakase", 1, "850.000k", R.drawable.ic_set_sushi),
                new InvoiceDetail(0, "Salad cá hồi", 1, "165.000k", R.drawable.img_salad),
                new InvoiceDetail(0, "Nước suối", 2, "45.000k", R.drawable.img_nuoc_suoi));

        insertSeedInvoice(new Invoice("HD-2026-016", "ĐÃ THANH TOÁN", "KH001", "31/05/2026", "635.000k", "Chuyển khoản", "Phúc"),
                new InvoiceDetail(0, "Mì Ramen Tonkotsu", 2, "500.000k", R.drawable.ic_ramen),
                new InvoiceDetail(0, "Takoyaki sốt mayo", 1, "95.000k", R.drawable.img_takoyaki),
                new InvoiceDetail(0, "Trà đào cam sả", 1, "40.000k", R.drawable.img_tra_dao));
    }

    private void insertSeedInvoice(Invoice invoice, InvoiceDetail... details) {
        long invoiceId = insertInvoiceAndGetId(invoice);
        if (invoiceId == -1) {
            return;
        }

        for (InvoiceDetail detail : details) {
            detail.setInvoiceId((int) invoiceId);
            insertInvoiceDetail(detail);
        }
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

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean looksLikePhone(String value) {
        return value != null && value.trim().matches("[0-9+\\-\\s]{8,15}");
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
