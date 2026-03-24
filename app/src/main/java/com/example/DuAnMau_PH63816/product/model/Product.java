package com.example.DuAnMau_PH63816.product.model;
public class Product {

    private int id;
    private String name;
    private String priceLabel;
    private String stockLabel;
    private String image;
    private String category;
    private String unit;
    private String date;
    private int status;

    public Product() {
    }

    public Product(int id, String name, String priceLabel, String stockLabel, String image) {
        this.id = id;
        this.name = name;
        this.priceLabel = priceLabel;
        this.stockLabel = stockLabel;
        this.image = image;
        this.category = "";
        this.unit = "";
        this.date = "";
        this.status = 1;
    }

    public Product(String name, String priceLabel, String stockLabel, String image) {
        this(0, name, priceLabel, stockLabel, image);
    }

    public Product(int id, String name, String priceLabel, String stockLabel, int imageRes) {
        this(id, name, priceLabel, stockLabel, String.valueOf(imageRes));
    }

    public Product(String name, String priceLabel, String stockLabel, int imageRes) {
        this(0, name, priceLabel, stockLabel, String.valueOf(imageRes));
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
