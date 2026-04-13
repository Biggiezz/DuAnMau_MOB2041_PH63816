package com.example.DuAnMau_PH63816.invoice.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.DuAnMau_PH63816.common.data.AppDbHelper;
import com.example.DuAnMau_PH63816.invoice.model.InvoiceDetail;
import com.example.DuAnMau_PH63816.product.ProductImageResolver;

import java.util.ArrayList;

public class InvoiceDetailDAO {

    private final AppDbHelper dbHelper;
    private final SQLiteDatabase sqLiteDatabase;
    private final Context appContext;

    public InvoiceDetailDAO(Context context) {
        appContext = context.getApplicationContext();
        dbHelper = new AppDbHelper(appContext);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        migrateLegacyImageValues();
    }

    public ArrayList<InvoiceDetail> getAllInvoiceDetails() {
        ArrayList<InvoiceDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM InvoiceDetail ORDER BY id ASC";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(mapInvoiceDetail(cursor));
                cursor.moveToNext();
            }
        }

        cursor.close();
        return list;
    }

    public ArrayList<InvoiceDetail> getInvoiceDetailsByInvoiceId(int invoiceId) {
        ArrayList<InvoiceDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM InvoiceDetail WHERE invoiceId = ? ORDER BY id ASC";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{String.valueOf(invoiceId)});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(mapInvoiceDetail(cursor));
                cursor.moveToNext();
            }
        }

        cursor.close();
        return list;
    }

    private InvoiceDetail mapInvoiceDetail(Cursor cursor) {
        InvoiceDetail detail = new InvoiceDetail();
        detail.setId(cursor.getInt(0));
        detail.setInvoiceId(cursor.getInt(1));
        detail.setProductName(cursor.getString(2));
        detail.setQuantity(cursor.getInt(3));
        detail.setTotalPrice(cursor.getString(4));

        int oldImageRes = cursor.getInt(5);
        String image = null;
        int imageIndex = cursor.getColumnIndex("image");
        if (imageIndex >= 0 && !cursor.isNull(imageIndex)) {
            image = cursor.getString(imageIndex);
        }

        if ((image == null || image.trim().isEmpty()) && oldImageRes != 0) {
            image = String.valueOf(oldImageRes);
        }

        image = ProductImageResolver.normalizeForStorage(appContext, detail.getProductName(), image);
        detail.setImage(image);
        detail.setImageRes(ProductImageResolver.resolveDrawableResId(appContext, image));
        return detail;
    }

    private void migrateLegacyImageValues() {
        String sql = "SELECT id, productName, imageRes, image FROM InvoiceDetail ORDER BY id ASC";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(0);
                String productName = cursor.getString(1);
                int oldImageRes = cursor.getInt(2);
                String image = cursor.getString(3);

                if ((image == null || image.trim().isEmpty()) && oldImageRes != 0) {
                    image = String.valueOf(oldImageRes);
                }

                String newImage = ProductImageResolver.normalizeForStorage(appContext, productName, image);
                int newImageRes = ProductImageResolver.resolveDrawableResId(appContext, newImage);

                boolean sameImage = false;
                if (newImage == null && image == null) {
                    sameImage = true;
                } else if (newImage != null && newImage.equals(image)) {
                    sameImage = true;
                }

                if (!sameImage || newImageRes != oldImageRes) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("image", newImage);
                    contentValues.put("imageRes", newImageRes);
                    sqLiteDatabase.update("InvoiceDetail", contentValues, "id = ?", new String[]{String.valueOf(id)});
                }

                cursor.moveToNext();
            }
        }

        cursor.close();
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
