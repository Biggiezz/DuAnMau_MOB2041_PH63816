package com.example.DuAnMau_PH63816.product.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

public class Product {

    private int id;
    private String name;
    private String priceLabel;
    private String stockLabel;
    // Lưu đường dẫn ảnh: có thể là URL hoặc chuỗi mã resource (String.valueOf(R.drawable.xxx))
    private String image;

    public Product() {
        // Required empty constructor for SQLite/DAO operations
    }

    public Product(int id, @NonNull String name, @NonNull String priceLabel, @NonNull String stockLabel, @NonNull String image) {
        this.id = id;
        this.name = name;
        this.priceLabel = priceLabel;
        this.stockLabel = stockLabel;
        this.image = image;
    }

    public Product(@NonNull String name, @NonNull String priceLabel, @NonNull String stockLabel, @NonNull String image) {
        this(0, name, priceLabel, stockLabel, image);
    }

    public Product(int id, @NonNull String name, @NonNull String priceLabel, @NonNull String stockLabel, @DrawableRes int imageRes) {
        this(id, name, priceLabel, stockLabel, String.valueOf(imageRes));
    }

    public Product(@NonNull String name, @NonNull String priceLabel, @NonNull String stockLabel, @DrawableRes int imageRes) {
        this(0, name, priceLabel, stockLabel, String.valueOf(imageRes));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriceLabel() {
        return priceLabel;
    }

    public void setPriceLabel(String priceLabel) {
        this.priceLabel = priceLabel;
    }

    public String getStockLabel() {
        return stockLabel;
    }

    public void setStockLabel(String stockLabel) {
        this.stockLabel = stockLabel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
