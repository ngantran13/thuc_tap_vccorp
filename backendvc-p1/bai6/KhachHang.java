package bai6;

public class KhachHang {
    private String maKH;
    private int tuoi;
    private String gioiTinh, loaiThanhVien;
    private double diemTichLuy;

    public KhachHang(String maKH, int tuoi, String gioiTinh, String loaiThanhVien) {
        this.maKH = maKH;
        this.tuoi = tuoi;
        this.gioiTinh = gioiTinh;
        this.loaiThanhVien = loaiThanhVien;
        this.diemTichLuy = 0;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public int getTuoi() {
        return tuoi;
    }

    public void setTuoi(int tuoi) {
        this.tuoi = tuoi;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getLoaiThanhVien() {
        return loaiThanhVien;
    }

    public void setLoaiThanhVien(String loaiThanhVien) {
        this.loaiThanhVien = loaiThanhVien;
    }

    public double getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(double diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    @Override
    public String toString() {
        return "KhachHang{" +
                "maKH='" + maKH + '\'' +
                ", tuoi=" + tuoi +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", loaiThanhVien='" + loaiThanhVien + '\'' +
                ", diemTichLuy=" + diemTichLuy +
                '}';
    }
}
