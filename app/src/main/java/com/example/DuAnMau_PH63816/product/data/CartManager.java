package com.example.DuAnMau_PH63816.product.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.DuAnMau_PH63816.product.model.CartItem;
import com.example.DuAnMau_PH63816.product.model.Product;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public final class CartManager {

    private static final LinkedHashMap<Integer, CartItem> ITEMS = new LinkedHashMap<>();
    private static CartDbHelper dbHelper;
    private static Context appContext;
    private static boolean isLoaded = false;
    private static String currentOwnerKey;

    private CartManager() {
    }

    public static void initialize(Context context) {
        if (context == null) {
            return;
        }

        appContext = context.getApplicationContext();

        if (dbHelper == null) {
            dbHelper = new CartDbHelper(appContext);
        }

        String resolvedOwnerKey = resolveCurrentOwnerKey();
        if (!isLoaded || !resolvedOwnerKey.equals(currentOwnerKey)) {
            currentOwnerKey = resolvedOwnerKey;
            loadFromDatabase();
        }
    }

    public static boolean addProduct(@NonNull Product product) {
        ensureLoaded();

        if (getAvailableStock(product) <= 0) {
            return false;
        }

        int productId = getProductKey(product);
        CartItem item = ITEMS.get(productId);

        if (item == null) {
            ///  tránh bị sửa dữ liệu giỏ hàng ngoài ý muốn nếu object gốc thay đổi
            item = new CartItem(coppyProduct(product), 1);
            ITEMS.put(productId, item);
        } else {
            item.setQuantity(item.getQuantity() + 1);
        }

        saveItem(item);
        return true;
    }

    private static Product coppyProduct(Product product) {
        Product copy = new Product();
        copy.setId(product.getId());
        copy.setName(product.getName());
        copy.setPriceLabel(product.getPriceLabel());
        copy.setStockLabel(product.getStockLabel());
        copy.setImage(product.getImage());
        copy.setCategory(product.getCategory());
        copy.setUnit(product.getUnit());
        copy.setDate(product.getDate());
        copy.setStatus(product.getStatus());
        return copy;
    }

    private static int getProductKey(@NonNull Product product) {
        if (product.getId() > 0) {
            return product.getId();
        }
        return product.getName().hashCode();
    }

    public static boolean increaseQuantity(@NonNull Product product) {
        return addProduct(product);
    }

    public static void decreaseQuantity(@NonNull Product product) {
        ensureLoaded();

        int productId = getProductKey(product);
        CartItem item = ITEMS.get(productId);

        if (item == null) {
            return;
        }

        int newQuantity = item.getQuantity() - 1;

        if (newQuantity <= 0) {
            ITEMS.remove(productId);
            deleteItem(productId);
        } else {
            item.setQuantity(newQuantity);
            saveItem(item);
        }
    }

    public static void removeProduct(@NonNull Product product) {
        ensureLoaded();

        int productId = getProductKey(product);
        ITEMS.remove(productId);
        deleteItem(productId);
    }

    public static void clear() {
        ensureLoaded();

        ITEMS.clear();

        SQLiteDatabase database = getDatabase();
        if (database != null) {
            database.delete("Cart", "ownerKey = ?", new String[]{requireOwnerKey()});
        }
    }

    public static List<CartItem> getItems() {
        ensureLoaded();
        return new ArrayList<>(ITEMS.values());
    }

    public static int getQuantityForProduct(@NonNull Product product) {
        ensureLoaded();

        CartItem item = ITEMS.get(getProductKey(product));
        if (item == null) {
            return 0;
        }
        return item.getQuantity();
    }

    public static int getAvailableStock(@NonNull Product product) {
        int baseStock = parseStock(product.getStockLabel());
        int reserved = getQuantityForProduct(product);
        return Math.max(0, baseStock - reserved);
    }

    public static String getAvailableStockLabel(@NonNull Product product) {
        return formatStockLabel(getAvailableStock(product));
    }

    public static int getTotalQuantity() {
        ensureLoaded();

        int total = 0;
        for (CartItem item : ITEMS.values()) {
            total += item.getQuantity();
        }
        return total;
    }

    public static long getSubtotal() {
        ensureLoaded();

        long subtotal = 0;
        for (CartItem item : ITEMS.values()) {
            long price = parsePrice(item.getProduct().getPriceLabel());
            subtotal += price * item.getQuantity();
        }
        return subtotal;
    }

    public static String formatCurrency(long amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');

        DecimalFormat format = new DecimalFormat("#,###", symbols);
        return format.format(amount) + "đ";
    }

    private static void loadFromDatabase() {
        ITEMS.clear();

        SQLiteDatabase database = getDatabase();
        if (database == null) {
            return;
        }

        try (Cursor cursor = database.rawQuery(
                "SELECT * FROM Cart WHERE ownerKey = ? ORDER BY productId ASC",
                new String[]{requireOwnerKey()}
        )) {
            while (cursor.moveToNext()) {
                CartItem item = mapCartItem(cursor);
                ITEMS.put(getProductKey(item.getProduct()), item);
            }

            isLoaded = true;
        }
    }

    private static CartItem mapCartItem(Cursor cursor) {
        Product product = new Product();

        product.setId(getInt(cursor, "productId"));
        product.setName(getString(cursor, "name"));
        product.setPriceLabel(getString(cursor, "priceLabel"));
        product.setStockLabel(getString(cursor, "stockLabel"));
        product.setImage(getString(cursor, "image"));
        product.setCategory(getString(cursor, "category"));
        product.setUnit(getString(cursor, "unit"));
        product.setDate(getString(cursor, "date"));
        product.setStatus(getInt(cursor, "status"));

        int quantity = getInt(cursor, "quantity");

        return new CartItem(product, quantity);
    }

    private static void saveItem(CartItem item) {
        SQLiteDatabase database = getDatabase();
        if (database == null) {
            return;
        }

        Product product = item.getProduct();

        ContentValues values = new ContentValues();
        values.put("ownerKey", requireOwnerKey());
        values.put("productId", getProductKey(product));
        values.put("name", product.getName());
        values.put("priceLabel", product.getPriceLabel());
        values.put("stockLabel", product.getStockLabel());
        values.put("image", product.getImage());
        values.put("category", product.getCategory());
        values.put("unit", product.getUnit());
        values.put("date", product.getDate());
        values.put("status", product.getStatus());
        values.put("quantity", item.getQuantity());

        database.insertWithOnConflict("Cart", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private static void deleteItem(int productId) {
        SQLiteDatabase database = getDatabase();
        if (database == null) {
            return;
        }

        database.delete(
                "Cart",
                "ownerKey = ? AND productId = ?",
                new String[]{requireOwnerKey(), String.valueOf(productId)}
        );
    }

    private static SQLiteDatabase getDatabase() {
        if (dbHelper == null) {
            return null;
        }
        return dbHelper.getWritableDatabase();
    }

    private static void ensureLoaded() {
        if (appContext != null) {
            initialize(appContext);
        }
        if (dbHelper == null) return;
        if (!isLoaded) {
            loadFromDatabase();
        }
    }

    private static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
    }

    private static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
    }

    private static long parsePrice(String priceLabel) {
        if (priceLabel == null || priceLabel.trim().isEmpty()) {
            return 0;
        }

        String cleanPrice = priceLabel
                .replace("đ", "")
                .replace("k", "")
                .replace("K", "")
                .replace(".", "")
                .replace(",", "")
                .replace(" ", "").trim();

        try {
            return Long.parseLong(cleanPrice);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static int parseStock(String stockLabel) {
        if (stockLabel == null || stockLabel.trim().isEmpty()) {
            return 0;
        }

        String digits = stockLabel.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) {
            return 0;
        }

        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static String formatStockLabel(int stock) {
        return " · Tồn: " + Math.max(0, stock);
    }

    private static String resolveCurrentOwnerKey() {
        if (appContext == null) {
            return "ACC_GUEST";
        }

        int staffCode = appContext.getSharedPreferences("StaffData", Context.MODE_PRIVATE)
                .getInt("staffCode", -1);
        if (staffCode <= 0) {
            return "ACC_GUEST";
        }
        return "ACC_" + staffCode;
    }

    private static String requireOwnerKey() {
        if (currentOwnerKey == null || currentOwnerKey.trim().isEmpty()) {
            currentOwnerKey = resolveCurrentOwnerKey();
        }
        return currentOwnerKey;
    }
}
