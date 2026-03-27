package com.example.DuAnMau_PH63816.invoice.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InvoiceDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Invoice.db";
    private static final int DB_VERSION = 5;

    public InvoiceDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String invoiceSql = "CREATE TABLE IF NOT EXISTS Invoice(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "code TEXT," +
                "status TEXT," +
                "customerId TEXT," +
                "date TEXT," +
                "total TEXT," +
                "paymentMethod TEXT," +
                "staffName TEXT)";
        String detailSql = "CREATE TABLE IF NOT EXISTS InvoiceDetail(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "invoiceId INTEGER," +
                "productName TEXT," +
                "quantity INTEGER," +
                "totalPrice TEXT," +
                "imageRes INTEGER)";
        db.execSQL(invoiceSql);
        db.execSQL(detailSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS InvoiceDetail");
            db.execSQL("DROP TABLE IF EXISTS Invoice");
            onCreate(db);
        }
    }
}
