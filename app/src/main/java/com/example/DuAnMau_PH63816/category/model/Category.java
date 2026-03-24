package com.example.DuAnMau_PH63816.category.model;

public class Category {
    private int id;
    private String name;
    private int productCount;
    private int iconResId;
    private String describe;

    public Category() {
    }

    public Category(int id, String name, int productCount, int iconResId, String describe) {
        this.id = id;
        this.name = name;
        this.productCount = productCount;
        this.iconResId = iconResId;
        this.describe = describe;
    }

    public Category(String name, int productCount, int iconResId, String describe) {
        this(0, name, productCount, iconResId, describe);
    }

    public Category(String name, String code, String count, String describe) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
