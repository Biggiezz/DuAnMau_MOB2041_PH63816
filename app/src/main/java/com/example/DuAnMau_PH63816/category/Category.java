package com.example.DuAnMau_PH63816.category;

public class Category {
    private int id;
    private String name;
    private int productCount;
    private int iconResId;

    public Category(int id, String name, int productCount, int iconResId) {
        this.id = id;
        this.name = name;
        this.productCount = productCount;
        this.iconResId = iconResId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getProductCount() { return productCount; }
    public int getIconResId() { return iconResId; }
}
