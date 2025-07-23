package bai6;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class NhanVienNhapHang extends NhanVien{
    private double thamNien;
    private String khuVucPhuTrach;

    public NhanVienNhapHang(String maNV, String gioiTinh, LocalDate ngayBatDau, String khuVucPhuTrach) {
        super(maNV, gioiTinh, ngayBatDau);
        LocalDate today =LocalDate.now();
        long namLam= ChronoUnit.YEARS.between(ngayBatDau,today);
        this.thamNien = namLam;
        this.khuVucPhuTrach = khuVucPhuTrach;
    }

    public double getThamNien() {
        return thamNien;
    }

    public void setThamNien(double thamNien) {
        this.thamNien = thamNien;
    }

    public String getKhuVucPhuTrach() {
        return khuVucPhuTrach;
    }

    public void setKhuVucPhuTrach(String khuVucPhuTrach) {
        this.khuVucPhuTrach = khuVucPhuTrach;
    }
}
