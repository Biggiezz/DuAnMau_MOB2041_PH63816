package com.example.DuAnMau_PH63816.product.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.DuAnMau_PH63816.product.ProductImageResolver;
import com.example.DuAnMau_PH63816.product.model.Product;

import java.util.ArrayList;

public class ProductDAO {

    private final ProductDbHelper dbHelper;
    private final SQLiteDatabase sqLiteDatabase;
    private final Context appContext;

    public ProductDAO(Context context) {
        appContext = context.getApplicationContext();
        dbHelper = new ProductDbHelper(appContext);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ensureSeedData();
        migrateLegacyImageValues();
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
        contentValues.put("image", ProductImageResolver.normalizeForStorage(appContext, product.getName(), product.getImage()));
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
        contentValues.put("image", ProductImageResolver.normalizeForStorage(appContext, product.getName(), product.getImage()));
        contentValues.put("category", product.getCategory());
        contentValues.put("unit", product.getUnit());
        contentValues.put("date", product.getDate());
        contentValues.put("status", product.getStatus());

        int kq = sqLiteDatabase.update("Product", contentValues, "id=?", new String[]{String.valueOf(product.getId())});
        return kq > 0;
    }

    /// delete
    public boolean deleteProduct(Product product) {
        int kq = sqLiteDatabase.delete("Product", "id = ?", new String[]{String.valueOf(product.getId())});
        return kq > 0;
    }

    private void ensureSeedData() {
        if (!getAllProducts().isEmpty()) {
            return;
        }
        for (Product seed : getSeedProducts()) {
            insertProduct(seed);
        }
    }

    private ArrayList<Product> getSeedProducts() {
        ArrayList<Product> seeds = new ArrayList<>();
        Product ramen = new Product("Mì Ramen Tonkotsu", "125.000k", " · Tồn: 10", "ic_ramen");
        ramen.setCategory("Ẩm thực");
        ramen.setUnit("Tô");
        ramen.setDate("10-05-2026");
        ramen.setStatus(1);
        seeds.add(ramen);

        Product matcha = new Product("Kem Matcha Premium", "45.000k", " · Tồn: 12", "ic_icream_matcha");
        matcha.setCategory("Tráng miệng");
        matcha.setUnit("Ly");
        matcha.setDate("11-05-2026");
        matcha.setStatus(1);
        seeds.add(matcha);

        Product sushi = new Product("Sushi Set Omakase", "850.000k", " · Tồn: 45", "ic_set_sushi");
        sushi.setCategory("Ẩm thực");
        sushi.setUnit("Khay");
        sushi.setDate("12-05-2026");
        sushi.setStatus(1);
        seeds.add(sushi);

        Product water = new Product("Nước suối", "20.000k", " · Tồn: 60", "img_nuoc_suoi");
        water.setCategory("Đồ uống");
        water.setUnit("Chai");
        water.setDate("13-05-2026");
        water.setStatus(1);
        seeds.add(water);

        Product eggTopping = new Product("Topping trứng ngâm", "25.000k", " · Tồn: 25", "img_trung_ngam");
        eggTopping.setCategory("Topping");
        eggTopping.setUnit("Phần");
        eggTopping.setDate("14-05-2026");
        eggTopping.setStatus(1);
        seeds.add(eggTopping);

        Product salmonSalad = new Product("Salad cá hồi", "165.000k", " · Tồn: 18", "img_salad");
        salmonSalad.setCategory("Khai vị");
        salmonSalad.setUnit("Đĩa");
        salmonSalad.setDate("15-05-2026");
        salmonSalad.setStatus(1);
        seeds.add(salmonSalad);

        Product roastedTea = new Product("Trà đào cam sả", "55.000k", " · Tồn: 30", "img_tra_dao");
        roastedTea.setCategory("Đồ uống");
        roastedTea.setUnit("Ly");
        roastedTea.setDate("16-05-2026");
        roastedTea.setStatus(1);
        seeds.add(roastedTea);

        Product takoyaki = new Product("Takoyaki sốt mayo", "95.000k", " · Tồn: 22", "img_takoyaki");
        takoyaki.setCategory("Ăn vặt");
        takoyaki.setUnit("Phần");
        takoyaki.setDate("17-05-2026");
        takoyaki.setStatus(1);
        seeds.add(takoyaki);

        return seeds;
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    private void migrateLegacyImageValues() {
        ArrayList<Product> products = getAllProducts();
        for (Product product : products) {
            String normalizedImage = ProductImageResolver.normalizeForStorage(
                    appContext,
                    product.getName(),
                    product.getImage()
            );
            if (normalizedImage.equals(product.getImage())) {
                continue;
            }

            ContentValues values = new ContentValues();
            values.put("image", normalizedImage);
            sqLiteDatabase.update(
                    "Product",
                    values,
                    "id=?",
                    new String[]{String.valueOf(product.getId())}
            );
        }
    }
}
