package com.example.DuAnMau_PH63816.product.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.DuAnMau_PH63816.product.model.Product;

import java.util.ArrayList;

public class ProductDAO {

    private ProductDbHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    public ProductDAO(Context context) {
        dbHelper = new ProductDbHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public ArrayList<Product> getAllProducts() {
        String sql = "SELECT * FROM Product";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        ArrayList<Product> list = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setName(cursor.getString(1));
                product.setPriceLabel(cursor.getString(2));
                product.setStockLabel(cursor.getString(3));
                product.setImage(cursor.getString(4));
                list.add(product);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    public boolean insertProduct(Product product) {
        /// b1: ghép giá trị với tên cột tương ứng
        /// ContentValues là 1 đối tượng lưu trữ dữ liệu cần insert vào db
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", product.getName());
        contentValues.put("priceLabel", product.getPriceLabel());
        contentValues.put("stockLabel", product.getStockLabel());
        contentValues.put("image", product.getImage());

        /// b2: gọi câu lệnh insert
        long kq = sqLiteDatabase.insert("Product", null, contentValues);
        return kq != -1;
    }

    /// update
    public boolean updateProduct(Product product) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", product.getName());
        contentValues.put("priceLabel", product.getPriceLabel());
        contentValues.put("stockLabel", product.getStockLabel());
        contentValues.put("image", product.getImage());

        long kq = sqLiteDatabase.update("Product", contentValues, "id=?", new String[]{String.valueOf(product.getId())});
        return kq != -1;
    }

    /// delete
    public boolean deleteProduct(Product product) {
        long kq = sqLiteDatabase.delete("Product", "id = ?", new String[]{String.valueOf(product.getId())});
        return kq != -1;
    }

}
