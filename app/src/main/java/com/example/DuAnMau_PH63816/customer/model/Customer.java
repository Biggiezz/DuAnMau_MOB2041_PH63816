package com.example.DuAnMau_PH63816.customer.model;

public class Customer {
    private final String id;
    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String price;
    private final String status;

    public Customer(String id, String name, String phone, String email, String address, String price, String status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.price = price;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }
}
