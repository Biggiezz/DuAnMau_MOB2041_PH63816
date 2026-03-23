package com.example.DuAnMau_PH63816.invoice.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.DuAnMau_PH63816.invoice.model.InvoiceDetail;

import java.util.ArrayList;

public class InvoiceDetailDAO {

    private final InvoiceDbHelper dbHelper;
    private final SQLiteDatabase sqLiteDatabase;

    public InvoiceDetailDAO(Context context) {
        dbHelper = new InvoiceDbHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public ArrayList<InvoiceDetail> getAllInvoiceDetails() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM InvoiceDetail ORDER BY id ASC", null);
        ArrayList<InvoiceDetail> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                InvoiceDetail detail = new InvoiceDetail();
                detail.setId(cursor.getInt(0));
                detail.setInvoiceId(cursor.getInt(1));
                detail.setProductName(cursor.getString(2));
                detail.setQuantity(cursor.getInt(3));
                detail.setTotalPrice(cursor.getString(4));
                detail.setImageRes(cursor.getInt(5));
                list.add(detail);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public boolean insertInvoiceDetail(InvoiceDetail detail) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("invoiceId", detail.getInvoiceId());
        contentValues.put("productName", detail.getProductName());
        contentValues.put("quantity", detail.getQuantity());
        contentValues.put("totalPrice", detail.getTotalPrice());
        contentValues.put("imageRes", detail.getImageRes());

        long kq = sqLiteDatabase.insert("InvoiceDetail", null, contentValues);
        return kq != -1;
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
