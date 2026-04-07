package com.example.DuAnMau_PH63816.invoice.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.DuAnMau_PH63816.invoice.model.InvoiceDetail;
import com.example.DuAnMau_PH63816.product.ProductImageResolver;

import java.util.ArrayList;

public class InvoiceDetailDAO {

    private final InvoiceDbHelper dbHelper;
    private final SQLiteDatabase sqLiteDatabase;
    private final Context appContext;

    public InvoiceDetailDAO(Context context) {
        appContext = context.getApplicationContext();
        dbHelper = new InvoiceDbHelper(appContext);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        migrateLegacyImageValues();
    }

    public ArrayList<InvoiceDetail> getAllInvoiceDetails() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM InvoiceDetail ORDER BY id ASC", null);
        ArrayList<InvoiceDetail> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                list.add(mapDetail(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public ArrayList<InvoiceDetail> getInvoiceDetailsByInvoiceId(int invoiceId) {
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM InvoiceDetail WHERE invoiceId = ? ORDER BY id ASC",
                new String[]{String.valueOf(invoiceId)}
        );
        ArrayList<InvoiceDetail> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                list.add(mapDetail(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public boolean insertInvoiceDetail(InvoiceDetail detail) {
        String normalizedImage = resolveNormalizedImage(detail);
        ContentValues contentValues = new ContentValues();
        contentValues.put("invoiceId", detail.getInvoiceId());
        contentValues.put("productName", detail.getProductName());
        contentValues.put("quantity", detail.getQuantity());
        contentValues.put("totalPrice", detail.getTotalPrice());
        contentValues.put("imageRes", ProductImageResolver.resolveDrawableResId(appContext, normalizedImage));
        contentValues.put("image", normalizedImage);

        long kq = sqLiteDatabase.insert("InvoiceDetail", null, contentValues);
        return kq != -1;
    }

    private InvoiceDetail mapDetail(Cursor cursor) {
        InvoiceDetail detail = new InvoiceDetail();
        detail.setId(cursor.getInt(0));
        detail.setInvoiceId(cursor.getInt(1));
        detail.setProductName(cursor.getString(2));
        detail.setQuantity(cursor.getInt(3));
        detail.setTotalPrice(cursor.getString(4));

        int legacyImageRes = cursor.getInt(5);
        String rawImage = getOptionalString(cursor, "image");
        String normalizedImage = ProductImageResolver.normalizeForStorage(
                appContext,
                detail.getProductName(),
                rawImage != null && !rawImage.trim().isEmpty()
                        ? rawImage
                        : (legacyImageRes != 0 ? String.valueOf(legacyImageRes) : null)
        );

        detail.setImage(normalizedImage);
        detail.setImageRes(ProductImageResolver.resolveDrawableResId(appContext, normalizedImage));
        return detail;
    }

    private String resolveNormalizedImage(InvoiceDetail detail) {
        String rawImage = detail.getImage();
        if ((rawImage == null || rawImage.trim().isEmpty()) && detail.getImageRes() != 0) {
            rawImage = String.valueOf(detail.getImageRes());
        }
        return ProductImageResolver.normalizeForStorage(appContext, detail.getProductName(), rawImage);
    }

    private String getOptionalString(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex < 0) {
            return null;
        }
        return cursor.getString(columnIndex);
    }

    private void migrateLegacyImageValues() {
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT id, productName, imageRes, image FROM InvoiceDetail ORDER BY id ASC",
                null
        );

        if (!cursor.moveToFirst()) {
            cursor.close();
            return;
        }

        do {
            int detailId = cursor.getInt(0);
            String productName = cursor.getString(1);
            int legacyImageRes = cursor.getInt(2);
            String rawImage = cursor.getString(3);

            String normalizedImage = ProductImageResolver.normalizeForStorage(
                    appContext,
                    productName,
                    rawImage != null && !rawImage.trim().isEmpty()
                            ? rawImage
                            : (legacyImageRes != 0 ? String.valueOf(legacyImageRes) : null)
            );
            int resolvedImageRes = ProductImageResolver.resolveDrawableResId(appContext, normalizedImage);

            if (normalizedImage.equals(rawImage) && resolvedImageRes == legacyImageRes) {
                continue;
            }

            ContentValues values = new ContentValues();
            values.put("image", normalizedImage);
            values.put("imageRes", resolvedImageRes);
            sqLiteDatabase.update(
                    "InvoiceDetail",
                    values,
                    "id = ?",
                    new String[]{String.valueOf(detailId)}
            );
        } while (cursor.moveToNext());

        cursor.close();
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
