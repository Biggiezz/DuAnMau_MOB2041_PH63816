package com.example.DuAnMau_PH63816.staff.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.DuAnMau_PH63816.staff.Staff;
import com.example.DuAnMau_PH63816.staff.StaffDbHelper;


public class StaffDAO {
    private final StaffDbHelper staffDbHelper;

    private final SharedPreferences sharedPreferences;

    public StaffDAO(Context context) {
        staffDbHelper = new StaffDbHelper(context);
        sharedPreferences = context.getSharedPreferences("StaffData", Context.MODE_PRIVATE);
    }

    /// kiểm tra thông tin đăng nhập
    /// nếu có giá trị (nhập user + pass đúng) -> true
    /// ngược lại -> false
    public boolean KiemTraDangNhap(String userName, String passWord) {
        SQLiteDatabase sqLiteDatabase = staffDbHelper.getReadableDatabase();
        Cursor sql = sqLiteDatabase.rawQuery(
                "SELECT staffCode, nameLogin, role FROM Staff WHERE nameLogin = ? AND password = ? LIMIT 1",
                new String[]{userName, passWord}
        );

        try {
            if (sql.moveToFirst()) {
                saveCurrentUserSession(sql.getString(1), sql.getInt(2));
                return true;
            }
            clearCurrentUserSession();
            return false;
        } finally {
            sql.close();
        }
    }

    public boolean dangKyTaiKhoan(Staff staff) {
        if (isUsernameExists(staff.getNameLogin())) {
            return false;
        }

        SQLiteDatabase sqLiteDatabase = staffDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nameStaff", staff.getNameStaff());
        contentValues.put("nameLogin", staff.getNameLogin());
        contentValues.put("password", staff.getPassword());
        contentValues.put("phone", staff.getPhone());
        contentValues.put("address", staff.getAddress());
        contentValues.put("role", 1);

        long check = sqLiteDatabase.insert("Staff", null, contentValues);
        return check != -1;
    }

    public boolean isUsernameExists(String userName) {
        SQLiteDatabase sqLiteDatabase = staffDbHelper.getReadableDatabase();
        Cursor sql = sqLiteDatabase.rawQuery(
                "SELECT 1 FROM Staff WHERE nameLogin = ? LIMIT 1",
                new String[]{userName}
        );

        try {
            return sql.moveToFirst();
        } finally {
            sql.close();
        }
    }

    private void saveCurrentUserSession(String nameLogin, int role) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nameLogin", nameLogin);
        editor.putInt("role", role);
        editor.apply();
    }

    private void clearCurrentUserSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("nameLogin");
        editor.remove("role");
        editor.apply();
    }
}
