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
        if (!getAllInvoices().isEmpty()) {
            return;
        }

        Invoice invoice1 = new Invoice("HD-2026-001", "CHỜ THANH TOÁN", "KH001", "10/05/2026", "295.000k", "Tiền mặt", "Phúc");
        long invoiceId1 = insertInvoiceAndGetId(invoice1);
        insertSeedDetails((int) invoiceId1, getInvoice1Details());

        Invoice invoice2 = new Invoice("HD-2026-002", "ĐÃ THANH TOÁN", "KH002", "09/05/2026", "850.000k", "Chuyển khoản", "Linh");
        long invoiceId2 = insertInvoiceAndGetId(invoice2);
        insertSeedDetails((int) invoiceId2, getInvoice2Details());

        Invoice invoice3 = new Invoice("HD-2026-003", "ĐÃ HỦY", "KH003", "08/05/2026", "940.000k", "Tiền mặt", "Hà");
        long invoiceId3 = insertInvoiceAndGetId(invoice3);
        insertSeedDetails((int) invoiceId3, getInvoice3Details());
    }

    private void insertSeedDetails(int invoiceId, ArrayList<InvoiceDetail> details) {
        for (InvoiceDetail detail : details) {
            detail.setInvoiceId(invoiceId);
            invoiceDetailDAO.insertInvoiceDetail(detail);
        }
    }

    private ArrayList<InvoiceDetail> getInvoice1Details() {
        ArrayList<InvoiceDetail> details = new ArrayList<>();
        details.add(new InvoiceDetail(0, "Mì Ramen Tonkotsu", 2, "250.000k", R.drawable.ic_ramen));
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
