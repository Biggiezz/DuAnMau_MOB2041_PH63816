package com.example.DuAnMau_PH63816.category.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.category.Category;

import java.util.ArrayList;

public class CategoryDAO {

    private final CategoryDbHelper dbHelper;
    private final SQLiteDatabase sqLiteDatabase;

    public CategoryDAO(Context context) {
        dbHelper = new CategoryDbHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ensureSeedData();
    }

    public ArrayList<Category> getAllCategories() {
        String sql = "SELECT * FROM Category ORDER BY id ASC";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        ArrayList<Category> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(0));
                category.setName(cursor.getString(1));
                category.setProductCount(cursor.getInt(2));
                category.setIconResId(cursor.getInt(3));
                list.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public boolean insertCategory(Category category) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", category.getName());
        contentValues.put("productCount", category.getProductCount());
        contentValues.put("iconResId", category.getIconResId());

        long kq = sqLiteDatabase.insert("Category", null, contentValues);
        return kq != -1;
    }

    public boolean updateCategory(Category category) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", category.getName());
        contentValues.put("productCount", category.getProductCount());
        contentValues.put("iconResId", category.getIconResId());

        long kq = sqLiteDatabase.update("Category", contentValues, "id = ?", new String[]{String.valueOf(category.getId())});
        return kq != -1;
    }

    public boolean deleteCategory(Category category) {
        long kq = sqLiteDatabase.delete("Category", "id = ?", new String[]{String.valueOf(category.getId())});
        return kq != -1;
    }

    private void ensureSeedData() {
        if (!getAllCategories().isEmpty()) {
            return;
        }

        insertCategory(new Category("Thời trang nam", 120, R.drawable.btn_category));
        insertCategory(new Category("Thời trang nữ", 85, R.drawable.btn_category));
        insertCategory(new Category("Phụ kiện", 42, R.drawable.btn_category));
        insertCategory(new Category("Điện tử", 15, R.drawable.btn_category));
        insertCategory(new Category("Gia dụng", 67, R.drawable.btn_category));
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
