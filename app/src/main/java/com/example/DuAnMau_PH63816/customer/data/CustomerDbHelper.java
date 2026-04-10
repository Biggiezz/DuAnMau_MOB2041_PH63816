package com.example.DuAnMau_PH63816.customer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CustomerDbHelper extends SQLiteOpenHelper {


    public CustomerDbHelper(@Nullable Context context) {
        super(context, "Customer.db", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    String sql = "CREATE TABLE IF NOT EXISTS Customer(id TEXT PRIMARY KEY," +
            "name TEXT," +
            " phone TEXT," +
            " email TEXT," +
            " address TEXT," +
            " price TEXT," +
            " status INTEGER )";
    sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i != i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Customer");
            onCreate(sqLiteDatabase);
        }
    }
}
