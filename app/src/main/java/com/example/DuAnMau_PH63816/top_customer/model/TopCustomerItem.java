package com.example.DuAnMau_PH63816.top_customer.model;

public class TopCustomerItem {

    private String maKhachHang;
    private String tenKhachHang;
    private int soLanMua;
    private long tongChiTieu;

    public TopCustomerItem(String maKhachHang, String tenKhachHang, int soLanMua, long tongChiTieu) {
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.soLanMua = soLanMua;
        this.tongChiTieu = tongChiTieu;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public int getSoLanMua() {
        return soLanMua;
    }

    public void setSoLanMua(int soLanMua) {
        this.soLanMua = soLanMua;
    }

    public long getTongChiTieu() {
        return tongChiTieu;
    }

    public void setTongChiTieu(long tongChiTieu) {
        this.tongChiTieu = tongChiTieu;
    }
}
