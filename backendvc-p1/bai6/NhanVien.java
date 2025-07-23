package bai6;

import java.time.LocalDate;
import java.util.Date;

public class NhanVien {
    private String maNV;
    private String gioiTinh;
    private LocalDate ngayBatDau;

    public NhanVien(String maNV, String gioiTinh, LocalDate ngayBatDau) {
        this.maNV = maNV;
        this.gioiTinh = gioiTinh;
        this.ngayBatDau = ngayBatDau;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNV='" + maNV + '\'' +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", ngayBatDau=" + ngayBatDau +
                '}';
    }
}
