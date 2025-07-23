package bai6;

import java.time.LocalDate;

public class MatHang {
    private String maHang,ten,phanLoai;
    private double gia;
    private int tonKho;
    private String nhaCungCap;
    private LocalDate hanSuDung;

    public MatHang(String maHang, String ten, String phanLoai, double gia, int tonKho, String nhaCungCap, LocalDate hanSuDung) {
        this.maHang = maHang;
        this.ten = ten;
        this.phanLoai = phanLoai;
        this.gia = gia;
        this.tonKho = tonKho;
        this.nhaCungCap = nhaCungCap;
        this.hanSuDung = hanSuDung;
    }

    public String getMaHang() {
        return maHang;
    }

    public void setMaHang(String maHang) {
        this.maHang = maHang;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getPhanLoai() {
        return phanLoai;
    }

    public void setPhanLoai(String phanLoai) {
        this.phanLoai = phanLoai;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getTonKho() {
        return tonKho;
    }

    public void setTonKho(int tonKho) {
        this.tonKho = tonKho;
    }

    public String getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(String nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public LocalDate getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(LocalDate hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

    @Override
    public String toString() {
        return "MatHang{" +
                "maHang='" + maHang + '\'' +
                ", ten='" + ten + '\'' +
                ", phanLoai='" + phanLoai + '\'' +
                ", gia=" + gia +
                ", tonKho=" + tonKho +
                ", nhaCungCap='" + nhaCungCap + '\'' +
                ", hanSuDung=" + hanSuDung +
                '}';
    }
}
