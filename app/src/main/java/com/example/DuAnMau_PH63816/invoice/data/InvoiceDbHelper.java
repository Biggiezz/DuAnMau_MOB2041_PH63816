package com.example.DuAnMau_PH63816.invoice.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InvoiceDbHelper extends SQLiteOpenHelper {

    public InvoiceDbHelper(Context context) {
        super(context, "Invoice.db", null, 8);
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
                "staffName TEXT," +
                "buyerName TEXT," +
                "buyerPhone TEXT," +
                "buyerAddress TEXT)";
        String detailSql = "CREATE TABLE IF NOT EXISTS InvoiceDetail(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "invoiceId INTEGER," +
                "productName TEXT," +
                "quantity INTEGER," +
                "totalPrice TEXT," +
                "imageRes INTEGER," +
                "image TEXT)";
        db.execSQL(invoiceSql);
        db.execSQL(detailSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 6) {
            db.execSQL("DROP TABLE IF EXISTS InvoiceDetail");
            db.execSQL("DROP TABLE IF EXISTS Invoice");
            onCreate(db);
            return;
        }

        if (oldVersion < 7) {
            db.execSQL("ALTER TABLE InvoiceDetail ADD COLUMN image TEXT");
        }

        if (oldVersion < 8) {
            db.execSQL("ALTER TABLE Invoice ADD COLUMN buyerName TEXT");
            db.execSQL("ALTER TABLE Invoice ADD COLUMN buyerPhone TEXT");
            db.execSQL("ALTER TABLE Invoice ADD COLUMN buyerAddress TEXT");
        }
    }
}
