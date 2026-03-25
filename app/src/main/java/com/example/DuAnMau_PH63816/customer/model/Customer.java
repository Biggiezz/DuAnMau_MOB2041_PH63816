package com.example.DuAnMau_PH63816.customer.model;

public class Customer {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String price;
    // status: 0 = VIP, 1 = Member
    private int status;

    public Customer(String id, String name, String phone, String email, String address, String price, int status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.price = price;
        this.status = status;
    }

    public Customer() {
    }

    public Customer(String name, String phone, String email, String address, String amount, int status) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.price = amount;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
