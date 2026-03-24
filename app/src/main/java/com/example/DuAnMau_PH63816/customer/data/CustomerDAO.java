package com.example.DuAnMau_PH63816.customer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.DuAnMau_PH63816.customer.model.Customer;

import java.util.ArrayList;

public class CustomerDAO {
    private final CustomerDbHelper dbHelper;
    private final SQLiteDatabase sqLiteDatabase;

    public CustomerDAO(Context context) {
        dbHelper = new CustomerDbHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ensureSeedData();
    }

    public ArrayList<Customer> getAllCustomer() {
        String sql = "SELECT * FROM Customer";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        ArrayList<Customer> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();
                customer.setId(cursor.getString(0));
                customer.setName(cursor.getString(1));
                customer.setPhone(cursor.getString(2));
                customer.setEmail(cursor.getString(3));
                customer.setAddress(cursor.getString(4));
                customer.setPrice(cursor.getString(5));
                customer.setStatus(cursor.getInt(6));
                list.add(customer);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean insertCustomer(Customer customer) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", customer.getId());
        contentValues.put("name", customer.getName());
        contentValues.put("phone", customer.getPhone());
        contentValues.put("email", customer.getEmail());
        contentValues.put("address", customer.getAddress());
        contentValues.put("price", customer.getPrice());
        contentValues.put("status", customer.getStatus());

        long kq = sqLiteDatabase.insert("Customer", null, contentValues);
        return kq != -1;
    }

    public boolean updateCustomer(Customer customer) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", customer.getName());
        contentValues.put("phone", customer.getPhone());
        contentValues.put("email", customer.getEmail());
        contentValues.put("address", customer.getAddress());
        contentValues.put("price", customer.getPrice());
        contentValues.put("status", customer.getStatus());

        long kq = sqLiteDatabase.update("Customer", contentValues, "id = ?", new String[]{customer.getId()});
        return kq != -1;
    }

    public boolean deleteCustomer(Customer customer) {
        long kq = sqLiteDatabase.delete("Customer", "id = ?", new String[]{customer.getId()});
        return kq != -1;
    }

    public Customer getCustomerById(String customerId) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Customer WHERE id = ?", new String[]{customerId});

        if (cursor.moveToFirst()) {
            Customer customer = new Customer();
            customer.setId(cursor.getString(0));
            customer.setName(cursor.getString(1));
            customer.setPhone(cursor.getString(2));
            customer.setEmail(cursor.getString(3));
            customer.setAddress(cursor.getString(4));
            customer.setPrice(cursor.getString(5));
            customer.setStatus(cursor.getInt(6));
            cursor.close();
            return customer;
        }

        cursor.close();
        return null;
    }

    public boolean isCustomerIdExists(String customerId) {
        return getCustomerById(customerId) != null;
    }

    private void ensureSeedData() {
        if (!getAllCustomer().isEmpty()) {
            return;
        }
        insertCustomer(new Customer("KH001", "Nguyễn Văn A", "0901 234 567", "vana@gmail.com", "12 Nguyễn Trãi, Quận 1, TP.HCM", "12.450.000", 0));
        insertCustomer(new Customer("KH002", "Trần ThaB", "0908 888 666", "thab@gmail.com", "25 Lê Lợi, Quận 3, TP.HCM", "3.240.000", 1));
        insertCustomer(new Customer("KH003", "Lê Văn C", "0933 222 111", "levanc@gmail.com", "8 Võ Văn Tần, Quận 10, TP.HCM", "8.900.000", 1));
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
