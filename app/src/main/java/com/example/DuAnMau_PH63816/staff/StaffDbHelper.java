package com.example.DuAnMau_PH63816.staff;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class StaffDbHelper extends SQLiteOpenHelper {
    public StaffDbHelper(Context context) {
        super(context, "Staff.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // ── Tạo bảng Nhân viên ────────────────────────────────────
        /*
         * role:
         *  0 - Admin
         *  1 - Nhân viên
         */
        String sql = "CREATE TABLE Staff (staffCode INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " nameStaff TEXT," +
                " nameLogin TEXT," +
                " password TEXT," +
                " phone TEXT," +
                " address TEXT," +
                " role INTEGER DEFAULT 1)";
        sqLiteDatabase.execSQL(sql);

        sqLiteDatabase.execSQL("INSERT INTO Staff (nameStaff, nameLogin, password, phone, address, role) VALUES" +
                "('Nguyen Manh Phuc', 'manhphuc', '123456', '0123456789', '461 Dang Chau Tue, Quang Hanh, Cam Pha, Quang Ninh',1)" +
                ",('Admin', 'admin', '123456a', '0977555777', '16 Pham Hung, Nam Tu Liem, TP.Ha Noi', 0)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i != i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Staff");
            onCreate(sqLiteDatabase);
        }
    }
}
