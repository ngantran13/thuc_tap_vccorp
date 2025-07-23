package bai6;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static bai6.NhanVienBanHang.tinhHoaHong;

public class test {
    public static void main(String[] args) {
        List<KhachHang> khachHangList =new ArrayList<>();
        khachHangList.add(new KhachHang("KH01",19,"Nu","VIP"));
        khachHangList.add(new KhachHang("KH02",29,"Nam","VIP"));
        khachHangList.add(new KhachHang("KH03",19,"Nu","Bt"));
        khachHangList.add(new KhachHang("KH04",18,"Nu","VIP"));

        List<NhanVien> nhanVienList=new ArrayList<>();
        nhanVienList.add(new NhanVienBanHang("NV01","Nu", LocalDate.of(2022, 5, 10),"ca sang"));
        nhanVienList.add(new NhanVienBanHang("NV02","Nu", LocalDate.of(2023, 5, 10),"ca sang"));
        nhanVienList.add(new NhanVienNhapHang("NV03","Nam", LocalDate.of(2021, 5, 10),"khu 1"));

        List<MatHang> danhSachMatHang = new ArrayList<>();
        danhSachMatHang.add(new MatHang("MH001", "Laptop Dell XPS 13", "đồ điện tử", 25000000, 10, "Công ty ABC", LocalDate.of(2026, 12, 31)));
        danhSachMatHang.add(new MatHang("MH002", "Nước rửa chén Sunlight", "hóa phẩm", 45000, 200, "Unilever", LocalDate.of(2025, 5, 20)));
        danhSachMatHang.add(new MatHang("MH003", "Gạo ST25", "thực phẩm", 18000, 500, "Công ty Gạo Việt", LocalDate.of(2025, 12, 1)));
        danhSachMatHang.add(new MatHang("MH004", "Tivi Samsung 55inch", "đồ điện tử", 12000000, 5, "Samsung VN", LocalDate.of(2027, 1, 15)));
        danhSachMatHang.add(new MatHang("MH005", "Sữa tươi Vinamilk", "thực phẩm", 33000, 100, "Vinamilk", LocalDate.of(2025, 8, 10)));

        List<HoaDon> hoaDonList = new ArrayList<>();
        hoaDonList.add(new HoaDon(
                "HD001",
                (NhanVienBanHang) nhanVienList.get(0),  // nhân viên bán hàng 1
                khachHangList.get(0),                  // khách hàng 1
                List.of(danhSachMatHang.get(0), danhSachMatHang.get(2)),  // Laptop + Gạo
                "Giảm 15% đồ điện tử, đơn từ 2M, tối đa giảm 500K"
        ));

        hoaDonList.add(new HoaDon(
                "HD002",
                (NhanVienBanHang) nhanVienList.get(1),  // nhân viên bán hàng 2
                khachHangList.get(1),                  // khách hàng 2
                List.of(danhSachMatHang.get(3), danhSachMatHang.get(4))  // Tivi + Sữa
        ));

        hoaDonList.add(new HoaDon(
                "HD003",
                (NhanVienBanHang) nhanVienList.get(1),  // nhân viên bán hàng 2
                khachHangList.get(2),                  // khách hàng 2
                List.of(danhSachMatHang.get(3), danhSachMatHang.get(4))  // Tivi + Sữa
        ));
        for(HoaDon hd:hoaDonList){
            System.out.println(hd);
        }
        for(NhanVien nv:nhanVienList){
            if(nv instanceof NhanVienBanHang){
                List<HoaDon> hoaDons=new ArrayList<>();
                for(HoaDon hd:hoaDonList){
                    if(hd.getNv().getMaNV().equals(nv.getMaNV())){
                        hoaDons.add(hd);
                    }
                }
                double hoaHong= tinhHoaHong((NhanVienBanHang) nv,hoaDons);
                System.out.println("Nhân viên: "+nv.getMaNV()+" có hoa hồng là: "+((NhanVienBanHang) nv).getHoaHong());
            }
        }

    }
}
