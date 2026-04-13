package com.example.DuAnMau_PH63816.staff.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.DuAnMau_PH63816.common.data.AppDbHelper;
import com.example.DuAnMau_PH63816.staff.model.Staff;

import java.util.ArrayList;


public class StaffDAO {
    private final AppDbHelper dbHelper;
    private final SharedPreferences sharedPreferences;
    private final SQLiteDatabase sqLiteDatabase;

    public StaffDAO(Context context) {
        Context appContext = context.getApplicationContext();
        dbHelper = new AppDbHelper(appContext);
        sharedPreferences = appContext.getSharedPreferences("StaffData", Context.MODE_PRIVATE);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ensureSeedData();
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
                staff.setImage(0);
                staff.setRole(cursor.getInt(6));
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
        contentValues.put("password", staff.getPassword());
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
        contentValues.put("password", staff.getPassword());
        contentValues.put("phone", staff.getPhone());
        contentValues.put("address", staff.getAddress());
        contentValues.put("role", staff.getRole());

        int kq = sqLiteDatabase.update("Staff", contentValues, "staffCode = ?", new String[]{String.valueOf(staff.getStaffCode())});
        return kq > 0;
    }

    public boolean deleteStaff(Staff staff) {
        long kq = sqLiteDatabase.delete("Staff", "staffCode = ?", new String[]{String.valueOf(staff.getStaffCode())});
        return kq != -1;
    }

    /// kiểm tra thông tin đăng nhập
    /// nếu có giá trị (nhập user + pass đúng) -> true
    /// ngược lại -> false
    public boolean KiemTraDangNhap(String userName, String passWord) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        try (Cursor sql = sqLiteDatabase.rawQuery(
                "SELECT staffCode, nameStaff, nameLogin, role FROM Staff WHERE nameLogin = ? AND password = ? LIMIT 1",
                new String[]{userName, passWord}
        )) {
            if (sql.moveToFirst()) {
                saveCurrentUserSession(sql.getInt(0), sql.getString(1), sql.getString(2), sql.getInt(3));
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

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nameStaff", staff.getNameStaff());
        contentValues.put("nameLogin", staff.getNameLogin());
        contentValues.put("password", staff.getPassword());
        contentValues.put("phone", staff.getPhone());
        contentValues.put("address", staff.getAddress());
        contentValues.put("role", staff.getRole());

        long check = sqLiteDatabase.insert("Staff", null, contentValues);
        return check != -1;
    }

    public boolean kiemTraMatKhauCu(String maNhanVien, String matKhauCu) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            return false;
        }
        try (Cursor cursor = sqLiteDatabase.rawQuery("SELECT password FROM Staff WHERE staffCode = ?",
                new String[]{maNhanVien}
        )) {
            if (cursor.moveToFirst()) {
                String matKhauHienTai = cursor.getString(0);
                return matKhauHienTai != null && matKhauHienTai.equals(matKhauCu);
            }
            return false;
        }
    }

    public boolean capNhatMatKhauMoi(String maNhanVien, String matKhauMoi) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", matKhauMoi);
        int row = sqLiteDatabase.update("Staff", contentValues, "staffCode = ?", new String[]{maNhanVien});
        return row > 0;
    }

    public String getCurrentStaffCode() {
        int staffCode = sharedPreferences.getInt("staffCode", -1);
        if (staffCode <= 0) {
            return null;
        }
        return String.valueOf(staffCode);
    }

    public String getCurrentStaffName() {
        String staffName = sharedPreferences.getString("nameStaff", null);
        if (staffName != null && !staffName.trim().isEmpty()) {
            return staffName;
        }

        int staffCode = sharedPreferences.getInt("staffCode", -1);
        if (staffCode <= 0) {
            return null;
        }

        try (Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT nameStaff FROM Staff WHERE staffCode = ? LIMIT 1",
                new String[]{String.valueOf(staffCode)}
        )) {
            if (!cursor.moveToFirst()) {
                return null;
            }

            String fallbackName = cursor.getString(0);
            if (fallbackName == null || fallbackName.trim().isEmpty()) {
                return null;
            }

            sharedPreferences.edit().putString("nameStaff", fallbackName).apply();
            return fallbackName;
        }
    }

    public Staff getCurrentStaff() {
        return getStaffByCode(getCurrentStaffCode());
    }

    public Staff getStaffByCode(String staffCode) {
        if (staffCode == null || staffCode.trim().isEmpty()) {
            return null;
        }

        try (Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT staffCode, nameStaff, nameLogin, password, phone, address, role FROM Staff WHERE staffCode = ? LIMIT 1",
                new String[]{staffCode}
        )) {
            if (!cursor.moveToFirst()) {
                return null;
            }

            Staff staff = new Staff();
            staff.setStaffCode(cursor.getInt(0));
            staff.setNameStaff(cursor.getString(1));
            staff.setNameLogin(cursor.getString(2));
            staff.setPassword(cursor.getString(3));
            staff.setPhone(cursor.getString(4));
            staff.setAddress(cursor.getString(5));
            staff.setRole(cursor.getInt(6));
            return staff;
        }
    }

    public boolean isUsernameExists(String userName) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        try (Cursor sql = sqLiteDatabase.rawQuery(
                "SELECT 1 FROM Staff WHERE nameLogin = ? LIMIT 1",
                new String[]{userName}
        )) {
            return sql.moveToFirst();
        }
    }

    private void saveCurrentUserSession(int staffCode, String nameStaff, String nameLogin, int role) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("staffCode", staffCode);
        editor.putString("nameStaff", nameStaff);
        editor.putString("nameLogin", nameLogin);
        editor.putInt("role", role);
        editor.apply();
    }

    private void clearCurrentUserSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("staffCode");
        editor.remove("nameStaff");
        editor.remove("nameLogin");
        editor.remove("role");
        editor.apply();
    }

    private void ensureSeedData() {
        if (isStaffTableEmpty()) {
            insertSeedStaff(
                    "Nguyen Manh Phuc",
                    "manhphuc",
                    "123456",
                    "0123456789",
                    "461 Dang Chau Tue, Quang Hanh, Cam Pha, Quang Ninh",
                    1
            );
            insertSeedStaff(
                    "Admin",
                    "admin",
                    "123456a",
                    "0977555777",
                    "16 Pham Hung, Nam Tu Liem, TP.Ha Noi",
                    0
            );
        }

        seedMissingInvoiceStaff();
    }

    private boolean isStaffTableEmpty() {
        try (Cursor cursor = sqLiteDatabase.rawQuery("SELECT 1 FROM Staff LIMIT 1", null)) {
            return !cursor.moveToFirst();
        }
    }

    private void seedMissingInvoiceStaff() {
        insertStaffIfNameMissing(
                "Nguyen Manh Phúc",
                "phuc",
                "123456",
                "0123456789",
                "Đơn nguyên 5, Nguyễn Cơ Thạch, Nam Từ Liêm, Hà Nội",
                1
        );
        insertStaffIfNameMissing(
                "Bùi Thị Linh",
                "linh",
                "123456",
                "0987654321",
                "Trịnh Văn Bô, Phương Canh, Hà Nội",
                1
        );
        insertStaffIfNameMissing(
                "Trần Thu Hà",
                "ha",
                "123456",
                "01122334455",
                "Thanh Xuân, Hà Nội",
                1
        );
    }

    private void insertSeedStaff(String nameStaff,
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

    private void insertStaffIfNameMissing(String nameStaff,
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

    public void close() {
        if (dbHelper != null) dbHelper.close();
    }
}
