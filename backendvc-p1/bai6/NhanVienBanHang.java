package bai6;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class NhanVienBanHang extends NhanVien{
    private String caDangKy;
    private double hoaHong;

    public NhanVienBanHang(String maNV, String gioiTinh, LocalDate ngayBatDau, String caDangKy) {
        super(maNV, gioiTinh, ngayBatDau);
        this.caDangKy = caDangKy;
        this.hoaHong = 0;
    }
    public static double tinhHoaHong(NhanVienBanHang nv, List<HoaDon> hoaDons){
        double doanhThu=0;
        for(HoaDon hd:hoaDons){
            doanhThu+=hd.getTongSauKM();
        }
        double hoaHong=0;
        while(doanhThu>0){
            if(doanhThu>100000000){
                hoaHong=hoaHong+(doanhThu-100000000)*0.1;
                doanhThu=100000000;
            }else if(doanhThu>50000000){
                hoaHong=hoaHong+(doanhThu-50000000)*0.07;
                doanhThu=50000000;
            }else if(doanhThu>25000000){
                hoaHong=hoaHong+(doanhThu-25000000)*0.05;
                doanhThu=25000000;
            }else if(doanhThu>10000000){
                hoaHong=hoaHong+(doanhThu-10000000)*0.035;
                doanhThu=10000000;
            }else{
                hoaHong=hoaHong+doanhThu*0.02;
                doanhThu=0;
            }
        }
        nv.setHoaHong(hoaHong);
        return hoaHong;
    }
    public String getCaDangKy() {
        return caDangKy;
    }

    public void setCaDangKy(String caDangKy) {
        this.caDangKy = caDangKy;
    }

    public double getHoaHong() {
        return hoaHong;
    }

    public void setHoaHong(double hoaHong) {
        this.hoaHong = hoaHong;
    }

    @Override
    public String toString() {
        return "NhanVienBanHang{" +
                "caDangKy='" + caDangKy + '\'' +
                ", hoaHong=" + hoaHong +
                '}';
    }
}
