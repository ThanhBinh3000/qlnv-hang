package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DcnbBbNhapDayKhoHdrDTO {
    private Long id;
    private Long qdDcCucId;
    private String soQdinh;
    private LocalDate ngayKyQd;
    private Integer namKh;
    private LocalDate thoiHanDieuChuyen;
    private String maNhaKho;
    private String tenNhaKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String maLoKho;
    private String tenLoKho;
    private String maNganKho;
    private String tenNganKho;
    private String maHangHoa;
    private String tenHangHoa;
    private String maChLoaiHangHoa;
    private String tenChLoaiHangHoa;
    private BigDecimal soLuongDc;
    private String donViTinh;
    private String tenDonViTinh;

    private String soBbNhapDayKho;
    private LocalDate ngayBatDauNhap;
    private LocalDate ngayBatKetNhap;

    private String soPhieuKiemTraCl;
    private Long phieuKiemTraClId;

    private String soPhieuNhapKho;
    private Long phieuNhapKhoId;
    private String soBangKe;
    private Long soBangKeId;
    private LocalDate ngayNhapDayKho;
    private String trangThai;
    private String tenTrangThai;

    public DcnbBbNhapDayKhoHdrDTO(Long id, Long qdDcCucId, String soQdinh,LocalDate ngayKyQd,  Integer namKh, LocalDate thoiHanDieuChuyen, String maNhaKho, String tenNhaKho, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, String maNganKho, String tenNganKho, String maHangHoa, String tenHangHoa, String maChLoaiHangHoa, String tenChLoaiHangHoa,BigDecimal soLuongDc,String donViTinh, String tenDonViTinh, String soBbNhapDayKho, LocalDate ngayBatDauNhap, LocalDate ngayBatKetNhap, String soPhieuKiemTraCl, Long phieuKiemTraClId, String soPhieuNhapKho, Long phieuNhapKhoId, String soBangKe, Long soBangKeId, LocalDate ngayNhapDayKho, String trangThai, String tenTrangThai) {
        this.id = id;
        this.qdDcCucId = qdDcCucId;
        this.soQdinh = soQdinh;
        this.ngayKyQd= ngayKyQd;
        this.namKh = namKh;
        this.thoiHanDieuChuyen = thoiHanDieuChuyen;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maLoKho = maLoKho;
        this.tenLoKho = tenLoKho;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.maHangHoa = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.maChLoaiHangHoa = maChLoaiHangHoa;
        this.tenChLoaiHangHoa = tenChLoaiHangHoa;
        this.soLuongDc = soLuongDc;
        this.donViTinh = donViTinh;
        this.tenDonViTinh = tenDonViTinh;
        this.soBbNhapDayKho = soBbNhapDayKho;
        this.ngayBatDauNhap = ngayBatDauNhap;
        this.ngayBatKetNhap = ngayBatKetNhap;
        this.soPhieuKiemTraCl = soPhieuKiemTraCl;
        this.phieuKiemTraClId = phieuKiemTraClId;
        this.soPhieuNhapKho = soPhieuNhapKho;
        this.phieuNhapKhoId = phieuNhapKhoId;
        this.soBangKe = soBangKe;
        this.soBangKeId = soBangKeId;
        this.ngayNhapDayKho = ngayNhapDayKho;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
