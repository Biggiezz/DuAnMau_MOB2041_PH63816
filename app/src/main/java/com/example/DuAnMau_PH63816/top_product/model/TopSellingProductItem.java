package com.example.DuAnMau_PH63816.top_product.model;

public class TopSellingProductItem {

    /// Lớp model dùng để lưu thông tin 1 sản phẩm trong danh sách top bán chạy.
    private final String productName;
    private final int imageRes;
    private int soldQuantity;
    private long revenue;

    public TopSellingProductItem(String productName, int imageRes, int soldQuantity, long revenue) {
        this.productName = productName;
        this.imageRes = imageRes;
        this.soldQuantity = soldQuantity;
        this.revenue = revenue;
    }

    public String getProductName() {
        return productName;
    }

    public int getImageRes() {
        return imageRes;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }
}
