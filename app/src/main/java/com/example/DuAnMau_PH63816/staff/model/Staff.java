package com.example.DuAnMau_PH63816.staff.model;

public class Staff {
    private int staffCode;
    private String nameStaff;
    private String nameLogin;
    private String password;
    private String phone;
    private String address;
    private int image;
    private int role;

    public Staff(int staffCode, String nameStaff, String nameLogin, String password, String phone, String address, int image, int role) {
        this.staffCode = staffCode;
        this.nameStaff = nameStaff;
        this.nameLogin = nameLogin;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.image = image;
        this.role = role;
    }

    public Staff() {
    }

    public Staff(String id, String name, String phone, String email, String address, String role) {
        if (id != null) {
            id = id.trim().replace("NV", "");
            if (!id.isEmpty()) {
                this.staffCode = Integer.parseInt(id);
            }
        }
        this.nameStaff = name;
        this.phone = phone;
        this.nameLogin = email;
        this.address = address;
        this.role = "Quản lý".equals(role) ? 0 : 1;
    }

    public Staff(String name, String phone, String email, String address, String role) {
        this.nameStaff = name;
        this.phone = phone;
        this.nameLogin = email;
        this.address = address;
        this.role = "Quản lý".equals(role) ? 0 : 1;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(int staffCode) {
        this.staffCode = staffCode;
    }

    public String getNameStaff() {
        return nameStaff;
    }

    public void setNameStaff(String nameStaff) {
        this.nameStaff = nameStaff;
    }

    public String getNameLogin() {
        return nameLogin;
    }

    public void setNameLogin(String nameLogin) {
        this.nameLogin = nameLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
