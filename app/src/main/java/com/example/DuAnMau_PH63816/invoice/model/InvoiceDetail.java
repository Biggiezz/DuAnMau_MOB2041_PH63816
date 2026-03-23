package com.example.DuAnMau_PH63816.invoice.model;

public class InvoiceDetail {

    private int id;
    private int invoiceId;
    private String productName;
    private int quantity;
    private String totalPrice;
    private int imageRes;

    public InvoiceDetail() {
    }

    public InvoiceDetail(int invoiceId, String productName, int quantity, String totalPrice, int imageRes) {
        this.invoiceId = invoiceId;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.imageRes = imageRes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }
}
