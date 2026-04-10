package com.example.DuAnMau_PH63816.product.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CartDbHelper extends SQLiteOpenHelper {
    public CartDbHelper(Context context) {
        super(context, "Cart.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Cart(" +
                "ownerKey TEXT NOT NULL," +
                "productId INTEGER NOT NULL," +
                "name TEXT NOT NULL," +
                "priceLabel TEXT," +
                "stockLabel TEXT," +
                "image TEXT," +
                "category TEXT," +
                "unit TEXT," +
                "date TEXT," +
                "status INTEGER DEFAULT 1," +
                "quantity INTEGER NOT NULL DEFAULT 1," +
                "PRIMARY KEY(ownerKey, productId)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE Cart RENAME TO CartLegacy");
            onCreate(db);
            db.execSQL(
                    "INSERT INTO Cart (ownerKey, productId, name, priceLabel, stockLabel, image, category, unit, date, status, quantity) " +
                            "SELECT 'ACC_GUEST', productId, name, priceLabel, stockLabel, image, category, unit, date, status, quantity " +
                            "FROM CartLegacy"
            );
            db.execSQL("DROP TABLE IF EXISTS CartLegacy");
        }
    }
}
