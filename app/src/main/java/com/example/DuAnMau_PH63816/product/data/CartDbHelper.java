package com.example.DuAnMau_PH63816.product.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CartDbHelper extends SQLiteOpenHelper {
    public CartDbHelper(Context context) {
        super(context, "Cart.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Cart(" +
                "productId INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "priceLabel TEXT," +
                "stockLabel TEXT," +
                "image TEXT," +
                "category TEXT," +
                "unit TEXT," +
                "date TEXT," +
                "status INTEGER DEFAULT 1," +
                "quantity INTEGER NOT NULL DEFAULT 1" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Cart");
            onCreate(db);
        }
    }
}
