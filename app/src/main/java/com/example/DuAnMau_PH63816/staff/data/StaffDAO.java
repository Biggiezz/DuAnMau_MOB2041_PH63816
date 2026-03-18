package com.example.DuAnMau_PH63816.staff.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.DuAnMau_PH63816.staff.model.Staff;
import com.example.DuAnMau_PH63816.staff.helper.StaffDbHelper;

import java.util.ArrayList;


public class StaffDAO {
    private final StaffDbHelper staffDbHelper;
    private final SharedPreferences sharedPreferences;
    private final SQLiteDatabase sqLiteDatabase;

    public StaffDAO(Context context) {
        staffDbHelper = new StaffDbHelper(context);
        sharedPreferences = context.getSharedPreferences("StaffData", Context.MODE_PRIVATE);
        sqLiteDatabase = staffDbHelper.getWritableDatabase();
    }

    public ArrayList<Staff> getAllStaff() {
        String sql = "SELECT * FROM Staff";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        ArrayList<Staff> list = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Staff staff = new Staff();
                staff.setStaffCode(cursor.getInt(0));
                staff.setNameStaff(cursor.getString(1));
                staff.setNameLogin(cursor.getString(2));
                staff.setPassword(cursor.getString(3));
                staff.setPhone(cursor.getString(4));
                staff.setAddress(cursor.getString(5));
                staff.setImage(cursor.getInt(6));
                staff.setRole(cursor.getInt(7));
                list.add(staff);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    public boolean insertStaff(Staff staff) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("nameStaff", staff.getNameStaff());
        contentValues.put("nameLogin", staff.getNameLogin());
        contentValues.put("pass", staff.getPassword());
        contentValues.put("phone", staff.getPhone());
        contentValues.put("address", staff.getAddress());
        contentValues.put("role", staff.getRole());

        long kq = sqLiteDatabase.insert("Staff", null, contentValues);
        return kq != -1;
    }

    public boolean updateStaff(Staff staff) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("nameStaff", staff.getNameStaff());
        contentValues.put("nameLogin", staff.getNameLogin());
        contentValues.put("pass", staff.getPassword());
        contentValues.put("phone", staff.getPhone());
        contentValues.put("address", staff.getAddress());
        contentValues.put("role", staff.getRole());

        long kq = sqLiteDatabase.update("Staff", contentValues, "staffCode = ?", new String[]{String.valueOf(staff.getStaffCode())});
        return kq != -1;
    }

    public boolean deleteStaff(Staff staff) {
        long kq = sqLiteDatabase.delete("Staff", "staffCode = ?", new String[]{String.valueOf(staff.getStaffCode())});
        return kq != -1;
    }

    /// kiểm tra thông tin đăng nhập
    /// nếu có giá trị (nhập user + pass đúng) -> true
    /// ngược lại -> false
    public boolean KiemTraDangNhap(String userName, String passWord) {
        SQLiteDatabase sqLiteDatabase = staffDbHelper.getReadableDatabase();

        try (Cursor sql = sqLiteDatabase.rawQuery(
                "SELECT staffCode, nameLogin, role FROM Staff WHERE nameLogin = ? AND password = ? LIMIT 1",
                new String[]{userName, passWord}
        )) {
            if (sql.moveToFirst()) {
                saveCurrentUserSession(sql.getString(1), sql.getInt(2));
                return true;
            }
            clearCurrentUserSession();
            return false;
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

        try (Cursor sql = sqLiteDatabase.rawQuery(
                "SELECT 1 FROM Staff WHERE nameLogin = ? LIMIT 1",
                new String[]{userName}
        )) {
            return sql.moveToFirst();
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
