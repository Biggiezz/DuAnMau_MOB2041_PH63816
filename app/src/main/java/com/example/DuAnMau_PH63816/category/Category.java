package com.example.DuAnMau_PH63816.category;

public class Category {
    private int id;
    private String name;
    private int productCount;
    private int iconResId;

    public Category() {
    }

    public Category(int id, String name, int productCount, int iconResId) {
        this.id = id;
        this.name = name;
        this.productCount = productCount;
        this.iconResId = iconResId;
    }

    public Category(String name, int productCount, int iconResId) {
        this(0, name, productCount, iconResId);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getProductCount() { return productCount; }
    public void setProductCount(int productCount) { this.productCount = productCount; }
    public int getIconResId() { return iconResId; }
    public void setIconResId(int iconResId) { this.iconResId = iconResId; }
}
