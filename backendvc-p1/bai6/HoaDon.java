package bai6;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class HoaDon {
    private String maHD;
    private NhanVien nv;
    private KhachHang kh;
    private List<MatHang> matHangList;
    private double tongGia;
    private LocalDate ngayMua;
    private double khuyenMai;
    private double tongSauKM;

    private int discountRate, minHD, maxDiscount;

    public HoaDon(String maHD, NhanVien nv, KhachHang kh, List<MatHang> matHangList) {
        this.maHD = maHD;
        this.nv = nv;
        this.kh = kh;
        this.matHangList = themMH(matHangList);
        this.tongGia = tinhTongGia(this.matHangList);
        this.ngayMua = LocalDate.now();
        this.khuyenMai = 0;
        this.tongSauKM = this.tongGia;
    }
    public HoaDon(String maHD, NhanVien nv, KhachHang kh, List<MatHang> matHangList,String voucher) {
        this.maHD = maHD;
        this.nv = nv;
        this.kh = kh;
        this.matHangList = themMH(matHangList);
        this.tongGia = tinhTongGia(this.matHangList);
        this.ngayMua = LocalDate.now();
        this.khuyenMai = tinhGiamGia(voucher,this.matHangList);
        this.tongSauKM = this.tongGia-this.khuyenMai;
    }
    public List<MatHang> themMH(List<MatHang> matHangList){
        List<MatHang> list =new ArrayList<>();
        for(MatHang m:matHangList){
            if(m.getHanSuDung().isAfter(LocalDate.now())){
                list.add(m);
                m.setTonKho(m.getTonKho()-1);
            }
        }
        return list;
    }
    public double tinhTongGia(List<MatHang> matHangList){
        double tong=0;
        for(MatHang m:matHangList){
            tong+=m.getGia();
        }
        return tong;
    }
    public double tinhGiamGia(String voucher, List<MatHang> matHangList){
        String[] str=voucher.split(", ");
        String s1=str[0];
        String phanloai=s1.substring(s1.indexOf("%")+2);
        this.discountRate=Integer.parseInt(s1.substring(5,s1.indexOf("%")));
        this.minHD=Integer.parseInt(""+str[1].charAt(str[1].length()-2));
        this.maxDiscount=Integer.parseInt(str[2].substring(str[2].lastIndexOf(" ")+1,str[2].length()-1));
        double giamGia=0;
        if(this.tongGia>=(this.minHD*1000000)){
            for(MatHang m:matHangList){
                if(m.getPhanLoai().equals(phanloai)){
                    giamGia=giamGia+m.getGia()*this.discountRate/100;
                }
            }
            if(giamGia>(this.maxDiscount*1000)){
                giamGia=this.maxDiscount*1000;
            }
        }
        return giamGia;
    }
    public void tinhTichLuy(KhachHang kh, double tong){
        double tichLuy=tong/1000;
        if(kh.getLoaiThanhVien().equals("VIP")){
            tichLuy*=1.5;
        }
        kh.setDiemTichLuy(kh.getDiemTichLuy()+tichLuy);
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public NhanVien getNv() {
        return nv;
    }

    public void setNv(NhanVien nv) {
        this.nv = nv;
    }

    public KhachHang getKh() {
        return kh;
    }

    public void setKh(KhachHang kh) {
        this.kh = kh;
    }

    public List<MatHang> getMatHangList() {
        return matHangList;
    }

    public void setMatHangList(List<MatHang> matHangList) {
        this.matHangList = matHangList;
    }

    public double getTongGia() {
        return tongGia;
    }

    public void setTongGia(double tongGia) {
        this.tongGia = tongGia;
    }

    public LocalDate getNgayMua() {
        return ngayMua;
    }

    public void setNgayMua(LocalDate ngayMua) {
        this.ngayMua = ngayMua;
    }

    public double getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(double khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public double getTongSauKM() {
        return tongSauKM;
    }

    public void setTongSauKM(double tongSauKM) {
        this.tongSauKM = tongSauKM;
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHD='" + maHD + '\'' +
                ", nv=" + nv +
                ", kh=" + kh +
                ", matHangList=" + matHangList +
                ", tongGia=" + tongGia +
                ", ngayMua=" + ngayMua +
                ", khuyenMai=" + khuyenMai +
                ", tongSauKM=" + tongSauKM +
                ", discountRate=" + discountRate +
                ", minHD=" + minHD +
                ", maxDiscount=" + maxDiscount +
                '}';
    }
}
