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

import java.util.ArrayList;

public class InvoiceDAO {

    private final InvoiceDbHelper dbHelper;
    private final SQLiteDatabase sqLiteDatabase;
    private final CustomerDAO customerDAO;
    private final InvoiceDetailDAO invoiceDetailDAO;

    public InvoiceDAO(Context context) {
        dbHelper = new InvoiceDbHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        customerDAO = new CustomerDAO(context);
        invoiceDetailDAO = new InvoiceDetailDAO(context);
        ensureSeedData();
    }

    public ArrayList<Invoice> getAllInvoices() {
        String sql = "SELECT * FROM Invoice ORDER BY id ASC";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        ArrayList<Invoice> list = new ArrayList<>();
        ArrayList<Customer> customers = customerDAO.getAllCustomer();

        if (cursor.moveToFirst()) {
            do {
                Invoice invoice = new Invoice();
                invoice.setId(cursor.getInt(0));
                invoice.setCode(cursor.getString(1));
                invoice.setStatus(cursor.getString(2));
                invoice.setCustomerId(cursor.getString(3));
                invoice.setDate(cursor.getString(4));
                invoice.setTotal(cursor.getString(5));
                invoice.setPaymentMethod(cursor.getString(6));
                invoice.setStaffName(cursor.getString(7));

                Customer customer = getCustomerById(customers, invoice.getCustomerId());
                if (customer != null) {
                    invoice.setCustomerName(customer.getName());
                    invoice.setCustomerPhone(customer.getPhone());
                    invoice.setCustomerAddress(customer.getAddress());
                }

                list.add(invoice);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public Invoice getInvoiceById(int invoiceId) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Invoice WHERE id = ?", new String[]{String.valueOf(invoiceId)});
        ArrayList<Customer> customers = customerDAO.getAllCustomer();

        if (cursor.moveToFirst()) {
            Invoice invoice = new Invoice();
            invoice.setId(cursor.getInt(0));
            invoice.setCode(cursor.getString(1));
            invoice.setStatus(cursor.getString(2));
            invoice.setCustomerId(cursor.getString(3));
            invoice.setDate(cursor.getString(4));
            invoice.setTotal(cursor.getString(5));
            invoice.setPaymentMethod(cursor.getString(6));
            invoice.setStaffName(cursor.getString(7));

            Customer customer = getCustomerById(customers, invoice.getCustomerId());
            if (customer != null) {
                invoice.setCustomerName(customer.getName());
                invoice.setCustomerPhone(customer.getPhone());
                invoice.setCustomerAddress(customer.getAddress());
            }

            cursor.close();
            return invoice;
        }

        cursor.close();
        return null;
    }

    public boolean insertInvoice(Invoice invoice) {
        return insertInvoiceAndGetId(invoice) != -1;
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

    private void ensureSeedData() {
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
        seedInvoiceIfMissing(new Invoice("HD-2026-001", "CHỜ THANH TOÁN", "KH001", "10/03/2026", "545.000k", "Tiền mặt", "Phúc"), getInvoice1Details());
        seedInvoiceIfMissing(new Invoice("HD-2026-002", "ĐÃ THANH TOÁN", "KH002", "09/05/2026", "850.000k", "Chuyển khoản", "Linh"), getInvoice2Details());
        seedInvoiceIfMissing(new Invoice("HD-2026-003", "ĐÃ HỦY", "KH003", "08/07/2026", "940.000k", "Tiền mặt", "Hà"), getInvoice3Details());
        seedInvoiceIfMissing(new Invoice("HD-2026-004", "ĐÃ THANH TOÁN", "KH001", "12/03/2026", "420.000k", "Tiền mặt", "Phúc"), getInvoice4Details());
        seedInvoiceIfMissing(new Invoice("HD-2026-005", "ĐÃ THANH TOÁN", "KH002", "14/05/2026", "675.000k", "Chuyển khoản", "Linh"), getInvoice5Details());
        seedInvoiceIfMissing(new Invoice("HD-2026-006", "CHỜ THANH TOÁN", "KH003", "16/08/2026", "180.000k", "Tiền mặt", "Hà"), getInvoice6Details());
        seedInvoiceIfMissing(new Invoice("HD-2026-007", "ĐÃ THANH TOÁN", "KH001", "20/02/2026", "1.250.000k", "Chuyển khoản", "Phúc"), getInvoice7Details());
        seedInvoiceIfMissing(new Invoice("HD-2026-008", "ĐÃ THANH TOÁN", "KH002", "23/05/2026", "560.000k", "Tiền mặt", "Linh"), getInvoice8Details());
        seedInvoiceIfMissing(new Invoice("HD-2026-009", "ĐÃ THANH TOÁN", "KH003", "24/01/2026", "935.000k", "Tiền mặt", "Hà"), getInvoice9Details());
        seedInvoiceIfMissing(new Invoice("HD-2026-010", "ĐÃ THANH TOÁN", "KH001", "25/05/2026", "1.175.000k", "Chuyển khoản", "Phúc"), getInvoice10Details());
        seedInvoiceIfMissing(new Invoice("HD-2026-011", "ĐÃ THANH TOÁN", "KH002", "26/04/2026", "1.190.000k", "Tiền mặt", "Linh"), getInvoice11Details());
        seedInvoiceIfMissing(new Invoice("HD-2026-012", "ĐÃ THANH TOÁN", "KH003", "27/05/2026", "760.000k", "Chuyển khoản", "Hà"), getInvoice12Details());
        seedInvoiceIfMissing(new Invoice("HD-2026-013", "ĐÃ THANH TOÁN", "KH001", "28/05/2026", "980.000k", "Tiền mặt", "Phúc"), getInvoice13Details());
        seedInvoiceIfMissing(new Invoice("HD-2026-014", "ĐÃ THANH TOÁN", "KH002", "29/05/2026", "745.000k", "Chuyển khoản", "Linh"), getInvoice14Details());
        seedInvoiceIfMissing(new Invoice("HD-2026-015", "ĐÃ THANH TOÁN", "KH003", "30/05/2026", "1.060.000k", "Tiền mặt", "Hà"), getInvoice15Details());
        seedInvoiceIfMissing(new Invoice("HD-2026-016", "ĐÃ THANH TOÁN", "KH001", "31/05/2026", "635.000k", "Chuyển khoản", "Phúc"), getInvoice16Details());
    }

    private void seedInvoiceIfMissing(Invoice invoice, ArrayList<InvoiceDetail> details) {
        Integer existingInvoiceId = getInvoiceIdByCode(invoice.getCode());
        if (existingInvoiceId != null) {
            updateInvoice(existingInvoiceId, invoice);
            deleteDetailsByInvoiceId(existingInvoiceId);
            insertSeedDetails(existingInvoiceId, details);
            return;
        }
        long invoiceId = insertInvoiceAndGetId(invoice);
        if (invoiceId != -1) {
            insertSeedDetails((int) invoiceId, details);
        }
    }

    private Integer getInvoiceIdByCode(String code) {
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT id FROM Invoice WHERE code = ? LIMIT 1",
                new String[]{code}
        );
        Integer invoiceId = cursor.moveToFirst() ? cursor.getInt(0) : null;
        cursor.close();
        return invoiceId;
    }

    private void updateInvoice(int invoiceId, Invoice invoice) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", invoice.getCode());
        contentValues.put("status", invoice.getStatus());
        contentValues.put("customerId", invoice.getCustomerId());
        contentValues.put("date", invoice.getDate());
        contentValues.put("total", invoice.getTotal());
        contentValues.put("paymentMethod", invoice.getPaymentMethod());
        contentValues.put("staffName", invoice.getStaffName());
        sqLiteDatabase.update("Invoice", contentValues, "id = ?", new String[]{String.valueOf(invoiceId)});
    }

    private void deleteDetailsByInvoiceId(int invoiceId) {
        sqLiteDatabase.delete("InvoiceDetail", "invoiceId = ?", new String[]{String.valueOf(invoiceId)});
    }

    private void insertSeedDetails(int invoiceId, ArrayList<InvoiceDetail> details) {
        for (InvoiceDetail detail : details) {
            detail.setInvoiceId(invoiceId);
            invoiceDetailDAO.insertInvoiceDetail(detail);
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
        details.add(new InvoiceDetail(0, "Nước suối", 2, "80.000k", R.drawable.logo));
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
        details.add(new InvoiceDetail(0, "Nước suối", 3, "60.000k", R.drawable.logo));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice9Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Kem Matcha Premium", 3, "135.000k", R.drawable.ic_icream_matcha));
        details.add(new InvoiceDetail(0, "Mì Ramen Tonkotsu", 2, "500.000k", R.drawable.ic_ramen));
        details.add(new InvoiceDetail(0, "Nước suối", 2, "80.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Salad cá hồi", 1, "165.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Trà đào cam sả", 1, "55.000k", R.drawable.logo));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice10Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Sushi Set Omakase", 1, "850.000k", R.drawable.ic_set_sushi));
        details.add(new InvoiceDetail(0, "Kem Matcha Premium", 1, "45.000k", R.drawable.ic_icream_matcha));
        details.add(new InvoiceDetail(0, "Nước suối", 3, "60.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Takoyaki sốt mayo", 2, "190.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Trà đào cam sả", 1, "30.000k", R.drawable.logo));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice11Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Mì Ramen Tonkotsu", 3, "750.000k", R.drawable.ic_ramen));
        details.add(new InvoiceDetail(0, "Kem Matcha Premium", 4, "180.000k", R.drawable.ic_icream_matcha));
        details.add(new InvoiceDetail(0, "Phụ phí phục vụ", 1, "15.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Nước suối", 3, "60.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Salad cá hồi", 1, "165.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Topping trứng ngâm", 1, "20.000k", R.drawable.logo));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice12Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Mì Ramen Tonkotsu", 1, "250.000k", R.drawable.ic_ramen));
        details.add(new InvoiceDetail(0, "Kem Matcha Premium", 4, "180.000k", R.drawable.ic_icream_matcha));
        details.add(new InvoiceDetail(0, "Nước suối", 3, "60.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Voucher giảm giá", 1, "-120.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Phụ phí phục vụ", 1, "15.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Topping trứng ngâm", 2, "50.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Trà đào cam sả", 3, "165.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Takoyaki sốt mayo", 2, "160.000k", R.drawable.logo));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice13Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Sushi Set Omakase", 1, "850.000k", R.drawable.ic_set_sushi));
        details.add(new InvoiceDetail(0, "Nước suối", 2, "40.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Trà đào cam sả", 1, "55.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Topping trứng ngâm", 1, "35.000k", R.drawable.logo));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice14Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Mì Ramen Tonkotsu", 2, "500.000k", R.drawable.ic_ramen));
        details.add(new InvoiceDetail(0, "Kem Matcha Premium", 3, "135.000k", R.drawable.ic_icream_matcha));
        details.add(new InvoiceDetail(0, "Takoyaki sốt mayo", 1, "95.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Nước suối", 1, "15.000k", R.drawable.logo));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice15Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Sushi Set Omakase", 1, "850.000k", R.drawable.ic_set_sushi));
        details.add(new InvoiceDetail(0, "Salad cá hồi", 1, "165.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Nước suối", 2, "45.000k", R.drawable.logo));
        return details;
    }

    private ArrayList<InvoiceDetail> getInvoice16Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Mì Ramen Tonkotsu", 2, "500.000k", R.drawable.ic_ramen));
        details.add(new InvoiceDetail(0, "Takoyaki sốt mayo", 1, "95.000k", R.drawable.logo));
        details.add(new InvoiceDetail(0, "Trà đào cam sả", 1, "40.000k", R.drawable.logo));
        return details;
    }

    public void close() {
        if (invoiceDetailDAO != null) {
            invoiceDetailDAO.close();
        }
        if (customerDAO != null) {
            customerDAO.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
