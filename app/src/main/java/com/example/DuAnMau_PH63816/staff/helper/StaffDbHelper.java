package com.example.DuAnMau_PH63816.staff.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StaffDbHelper extends SQLiteOpenHelper {
    public StaffDbHelper(Context context) {
        super(context, "Staff.db", null, 2);
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
        seedDefaultStaff(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i < i1) {
            seedMissingInvoiceStaff(sqLiteDatabase);
        }
    }

    private void seedDefaultStaff(SQLiteDatabase sqLiteDatabase) {
        insertStaff(
                sqLiteDatabase,
                "Nguyen Manh Phuc",
                "manhphuc",
                "123456",
                "0123456789",
                "461 Dang Chau Tue, Quang Hanh, Cam Pha, Quang Ninh",
                1
        );
        /// admin
        insertStaff(
                sqLiteDatabase,
                "Admin",
                "admin",
                "123456a",
                "0977555777",
                "16 Pham Hung, Nam Tu Liem, TP.Ha Noi",
                0
        );
        seedMissingInvoiceStaff(sqLiteDatabase);
    }

    private void seedMissingInvoiceStaff(SQLiteDatabase sqLiteDatabase) {
        insertStaffIfNameMissing(
                sqLiteDatabase,
                "Nguyen Manh Phúc",
                "phuc",
                "123456",
                "0123456789",
                "Đơn nguyên 5, Nguyễn Cơ Thạch, Nam Từ Liêm, Hà Nội",
                1
        );
        insertStaffIfNameMissing(
                sqLiteDatabase,
                "Bùi Thị Linh",
                "linh",
                "123456",
                "0987654321",
                "Trịnh Văn Bô, Phương Canh, Hà Nội",
                1
        );
        insertStaffIfNameMissing(
                sqLiteDatabase,
                "Trần Thu Hà",
                "ha",
                "123456",
                "01122334455",
                "Thanh Xuân, Hà Nội",
                1
        );
    }

    private void insertStaff(SQLiteDatabase sqLiteDatabase,
                             String nameStaff,
                             String nameLogin,
                             String password,
                             String phone,
                             String address,
                             int role) {
        sqLiteDatabase.execSQL(
                "INSERT INTO Staff (nameStaff, nameLogin, password, phone, address, role) VALUES (?, ?, ?, ?, ?, ?)",
                new Object[]{nameStaff, nameLogin, password, phone, address, role}
        );
    }

    private void insertStaffIfNameMissing(SQLiteDatabase sqLiteDatabase,
                                          String nameStaff,
                                          String nameLogin,
                                          String password,
                                          String phone,
                                          String address,
                                          int role) {
        sqLiteDatabase.execSQL(
                "INSERT INTO Staff (nameStaff, nameLogin, password, phone, address, role) " +
                        "SELECT ?, ?, ?, ?, ?, ? " +
                        "WHERE NOT EXISTS (SELECT 1 FROM Staff WHERE nameStaff = ?)",
                new Object[]{nameStaff, nameLogin, password, phone, address, role, nameStaff}
        );
    }
}
