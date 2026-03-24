package com.example.DuAnMau_PH63816.product.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Product.db";
    private static final int DB_VERSION = 5;

    public ProductDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS Product(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name TEXT NOT NULL," +
                " priceLabel TEXT," +
                " stockLabel TEXT," +
                " image TEXT," +
                " category TEXT," +
                " unit TEXT," +
                " date TEXT," +
                " status INTEGER DEFAULT 1" +
                ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Product");
            onCreate(sqLiteDatabase);
        }
    }
}
