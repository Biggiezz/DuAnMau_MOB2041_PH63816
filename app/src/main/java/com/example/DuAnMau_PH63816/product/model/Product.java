package com.example.DuAnMau_PH63816.product.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

public class Product {
    private final String name;
    private final String priceLabel;
    private final String stockLabel;
    @DrawableRes
    private final int imageRes;

    public Product(@NonNull String name, @NonNull String priceLabel, @NonNull String stockLabel, @DrawableRes int imageRes) {
        this.name = name;
        this.priceLabel = priceLabel;
        this.stockLabel = stockLabel;
        this.imageRes = imageRes;
    }

    public String getName() {
        return name;
    }

    public String getPriceLabel() {
        return priceLabel;
    }

    public String getStockLabel() {
        return stockLabel;
    }

    public int getImageRes() {
        return imageRes;
    }
}
