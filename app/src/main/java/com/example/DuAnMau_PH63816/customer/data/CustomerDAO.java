package com.example.DuAnMau_PH63816.customer.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.DuAnMau_PH63816.customer.model.Customer;
import java.util.ArrayList;

public class CustomerDAO {
    private CustomerDbHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private SQLiteDatabase sqLiteDatabase;

    public CustomerDAO(Context context) {
        dbHelper = new CustomerDbHelper(context);
        sharedPreferences = context.getSharedPreferences("CustomerData", Context.MODE_PRIVATE);
        this.sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public ArrayList<Customer> getAllCustomer() {
        String sql = "SELECT * FROM Customer";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        ArrayList<Customer> list = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                Customer customer = new Customer();
                customer.setId(cursor.getString(0));
                customer.setName(cursor.getString(1));
                customer.setPhone(cursor.getString(2));
                customer.setEmail(cursor.getString(3));
                customer.setAddress(cursor.getString(4));
                customer.setPrice(cursor.getString(5));
                customer.setStatus(cursor.getInt(6));
                list.add(customer);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    public boolean insertCustomer(Customer customer) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", customer.getName());
        contentValues.put("phone", customer.getPhone());
        contentValues.put("email", customer.getEmail());
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
        contentValues.put("price", customer.getPrice());
        contentValues.put("status", customer.getStatus());

        long kq = sqLiteDatabase.update("Customer", contentValues, "id = ?", new String[]{customer.getId()});
        return kq != -1;
    }

    public boolean deleteCustomer(Customer customer) {
        long kq = sqLiteDatabase.delete("Customer", "id = ?", new String[]{customer.getId()});
        return kq != -1;
    }
}
