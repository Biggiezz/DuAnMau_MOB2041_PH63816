package com.example.DuAnMau_PH63816.product.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.product.model.Product;

import java.util.ArrayList;

public class ProductDAO {

    private final ProductDbHelper dbHelper;
    private final SQLiteDatabase sqLiteDatabase;

    public ProductDAO(Context context) {
        dbHelper = new ProductDbHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ensureSeedData();
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
                product.setCategory(cursor.getString(5));
                product.setUnit(cursor.getString(6));
                product.setDate(cursor.getString(7));
                product.setStatus(cursor.getInt(8));
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
        contentValues.put("category", product.getCategory());
        contentValues.put("unit", product.getUnit());
        contentValues.put("date", product.getDate());
        contentValues.put("status", product.getStatus());

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
        contentValues.put("category", product.getCategory());
        contentValues.put("unit", product.getUnit());
        contentValues.put("date", product.getDate());
        contentValues.put("status", product.getStatus());

        long kq = sqLiteDatabase.update("Product", contentValues, "id=?", new String[]{String.valueOf(product.getId())});
        return kq != -1;
    }

    /// delete
    public boolean deleteProduct(Product product) {
        long kq = sqLiteDatabase.delete("Product", "id = ?", new String[]{String.valueOf(product.getId())});
        return kq != -1;
    }

    private void ensureSeedData() {
        if (getAllProducts().isEmpty()) {
            for (Product seed : getSeedProducts()) {
                insertProduct(seed);
            }
        }
    }

    /// Hàm kiểm tra có trùng mã với ô nhập không
//    private Product getProductById(String productId) {
//        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Product WHERE id = ?", new String[]{productId});
//        if (cursor.moveToFirst()) {
//            Product product = new Product();
//            product.setId(cursor.getInt(0));
//            product.setName(cursor.getString(1));
//            product.setPriceLabel(cursor.getString(2));
//            product.setStockLabel(cursor.getString(3));
//            product.setImage(cursor.getString(4));
//            product.setCategory(cursor.getString(5));
//            product.setUnit(cursor.getString(6));
//            product.setDate(cursor.getString(7));
//            product.setStatus(cursor.getInt(8));
//            cursor.close();
//            return product;
//        }
//        cursor.close();
//        return null;
//    }
//
//    public boolean isProductIdExists(String productId) {
//        return getProductById(productId) != null;
//    }
//
    private ArrayList<Product> getSeedProducts() {
        ArrayList<Product> seeds = new ArrayList<>();
        Product ramen = new Product("Mì Ramen Tonkotsu", "125.000k", " · Tồn: 10", R.drawable.ic_ramen);
        ramen.setCategory("Ẩm thực");
        ramen.setUnit("Tô");
        ramen.setDate("10-05-2026");
        ramen.setStatus(1);
        seeds.add(ramen);

        Product matcha = new Product("Kem Matcha Premium", "45.000k", " · Tồn: 12", R.drawable.ic_icream_matcha);
        matcha.setCategory("Tráng miệng");
        matcha.setUnit("Ly");
        matcha.setDate("11-05-2026");
        matcha.setStatus(1);
        seeds.add(matcha);

        Product sushi = new Product("Sushi Set Omakase", "850.000k", " · Tồn: 45", R.drawable.ic_set_sushi);
        sushi.setCategory("Ẩm thực");
        sushi.setUnit("Khay");
        sushi.setDate("12-05-2026");
        sushi.setStatus(1);
        seeds.add(sushi);
        return seeds;
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
