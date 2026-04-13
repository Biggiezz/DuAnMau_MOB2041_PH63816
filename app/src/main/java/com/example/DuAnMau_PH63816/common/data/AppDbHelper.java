package com.example.DuAnMau_PH63816.common.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class AppDbHelper extends SQLiteOpenHelper {
    private final Context appContext;

    public AppDbHelper(Context context) {
        super(context.getApplicationContext(), "AppDatabase.db", null, 1);
        appContext = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Category(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "productCount INTEGER," +
                "iconResId INTEGER," +
                "describe TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS Customer(" +
                "id TEXT PRIMARY KEY," +
                "name TEXT," +
                "phone TEXT," +
                "email TEXT," +
                "address TEXT," +
                "price TEXT," +
                "status INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS Product(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "priceLabel TEXT," +
                "stockLabel TEXT," +
                "image TEXT," +
                "category TEXT," +
                "unit TEXT," +
                "date TEXT," +
                "status INTEGER DEFAULT 1" +
                ")");

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

        db.execSQL("CREATE TABLE IF NOT EXISTS Invoice(" +
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
                "buyerAddress TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS InvoiceDetail(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "invoiceId INTEGER," +
                "productName TEXT," +
                "quantity INTEGER," +
                "totalPrice TEXT," +
                "imageRes INTEGER," +
                "image TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS Staff(" +
                "staffCode INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nameStaff TEXT," +
                "nameLogin TEXT," +
                "password TEXT," +
                "phone TEXT," +
                "address TEXT," +
                "role INTEGER DEFAULT 1)");

        migrateLegacyDatabases(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // AppDatabase.db starts at version 1. Add explicit migrations here for future schema changes.
    }

    private void migrateLegacyDatabases(SQLiteDatabase targetDb) {
        copyLegacyTable(targetDb, "Category.db", "Category");
        copyLegacyTable(targetDb, "Customer.db", "Customer");
        copyLegacyTable(targetDb, "Product.db", "Product");
        copyLegacyTable(targetDb, "Cart.db", "Cart");
        copyLegacyTable(targetDb, "Invoice.db", "Invoice");
        copyLegacyTable(targetDb, "Invoice.db", "InvoiceDetail");
        copyLegacyTable(targetDb, "Staff.db", "Staff");
    }

    private void copyLegacyTable(SQLiteDatabase targetDb, String legacyDbName, String tableName) {
        if (DatabaseUtils.queryNumEntries(targetDb, tableName) > 0) {
            return;
        }

        File legacyDbFile = appContext.getDatabasePath(legacyDbName);
        if (legacyDbFile == null || !legacyDbFile.exists()) {
            return;
        }

        SQLiteDatabase legacyDb = null;
        Cursor cursor = null;

        try {
            legacyDb = SQLiteDatabase.openDatabase(
                    legacyDbFile.getPath(),
                    null,
                    SQLiteDatabase.OPEN_READONLY
            );

            if (!hasTable(legacyDb, tableName)) {
                return;
            }

            cursor = legacyDb.query(tableName, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                ContentValues values = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, values);
                applyLegacyDefaults(tableName, values);
                targetDb.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (legacyDb != null) {
                legacyDb.close();
            }
        }
    }

    private boolean hasTable(SQLiteDatabase database, String tableName) {
        try (Cursor cursor = database.rawQuery(
                "SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = ? LIMIT 1",
                new String[]{tableName}
        )) {
            return cursor.moveToFirst();
        }
    }

    private void applyLegacyDefaults(String tableName, ContentValues values) {
        if ("Category".equals(tableName)) {
            putIfMissing(values, "productCount", 0);
            putIfMissing(values, "iconResId", 0);
            putIfMissing(values, "describe", "");
            return;
        }

        if ("Customer".equals(tableName)) {
            putIfMissing(values, "price", "");
            putIfMissing(values, "status", 0);
            return;
        }

        if ("Product".equals(tableName)) {
            putIfMissing(values, "status", 1);
            return;
        }

        if ("Cart".equals(tableName)) {
            putIfMissing(values, "ownerKey", "ACC_GUEST");
            putIfMissing(values, "status", 1);
            putIfMissing(values, "quantity", 1);
            return;
        }

        if ("InvoiceDetail".equals(tableName)) {
            putIfMissing(values, "imageRes", 0);
            return;
        }

        if ("Staff".equals(tableName)) {
            putIfMissing(values, "role", 1);
        }
    }

    private void putIfMissing(ContentValues values, String key, String defaultValue) {
        if (!values.containsKey(key)) {
            values.put(key, defaultValue);
        }
    }

    private void putIfMissing(ContentValues values, String key, int defaultValue) {
        if (!values.containsKey(key)) {
            values.put(key, defaultValue);
        }
    }
}
