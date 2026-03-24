package com.example.DuAnMau_PH63816.category.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CategoryDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Category.db";
    private static final int DB_VERSION = 3;

    public CategoryDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS Category(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "productCount INTEGER," +
                "iconResId INTEGER," +
                "describe TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Category");
            onCreate(db);
        }
    }
}
