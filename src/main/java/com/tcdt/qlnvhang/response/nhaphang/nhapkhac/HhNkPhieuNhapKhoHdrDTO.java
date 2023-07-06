package com.tcdt.qlnvhang.response.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class HhNkPhieuNhapKhoHdrDTO {
    private Long id;
    private Long qdDcCucId;
    private String soQdinh;
    private LocalDate ngayKyQdinh;
    private Integer namKh;
    private LocalDate thoiHanDieuChuyen;
    private String maNhaKho;
    private String tenNhaKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String maloKho;
    private String tenloKho;
    private String maNganKho;
    private String tenNganKho;
    private Boolean thayDoiThuKho;
    private String soPhieuNhapKho;
    private LocalDate ngayNhapKho;
    private Long bangKeVtId;
    private String soBangKeVt;
    private String maHangHoa;
    private String tenHangHoa;
    private String maChLoaiHangHoa;
    private String tenChLoaiHangHoa;
    private String donViTinh;
    private String tenDonvitinh;
    private BigDecimal slDienChuyen;
    private BigDecimal duToanKinhPhiDc;

    private Long phieuKiemTraId;
    private String soPhieuKiemTraCl;
    private LocalDate ngayGiamDinh;
    private String trangThai;
    private String tenTrangThai;

    public HhNkPhieuNhapKhoHdrDTO(Long id, Long qdDcCucId, String soQdinh, LocalDate ngayKyQdinh, Integer namKh, LocalDate thoiHanDieuChuyen, String maNhaKho, String tenNhaKho, String maDiemKho, String tenDiemKho, String maloKho, String tenloKho, String maNganKho, String tenNganKho, Boolean thayDoiThuKho, String soPhieuNhapKho, LocalDate ngayNhapKho, Long bangKeVtId, String soBangKeVt, String maHangHoa, String tenHangHoa, String maChLoaiHangHoa, String tenChLoaiHangHoa, String donViTinh, String tenDonvitinh, BigDecimal slDienChuyen, BigDecimal duToanKinhPhiDc, Long phieuKiemTraId, String soPhieuKiemTraCl, LocalDate ngayGiamDinh, String trangThai, String tenTrangThai) {
        this.id = id;
        this.qdDcCucId = qdDcCucId;
        this.soQdinh = soQdinh;
        this.ngayKyQdinh = ngayKyQdinh;
        this.namKh = namKh;
        this.thoiHanDieuChuyen = thoiHanDieuChuyen;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maloKho = maloKho;
        this.tenloKho = tenloKho;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.thayDoiThuKho = thayDoiThuKho;
        this.soPhieuNhapKho = soPhieuNhapKho;
        this.ngayNhapKho = ngayNhapKho;
        this.bangKeVtId = bangKeVtId;
        this.soBangKeVt = soBangKeVt;
        this.maHangHoa = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.maChLoaiHangHoa = maChLoaiHangHoa;
        this.tenChLoaiHangHoa = tenChLoaiHangHoa;
        this.donViTinh = donViTinh;
        this.tenDonvitinh = tenDonvitinh;
        this.slDienChuyen = slDienChuyen;
        this.duToanKinhPhiDc = duToanKinhPhiDc;
        this.phieuKiemTraId = phieuKiemTraId;
        this.soPhieuKiemTraCl = soPhieuKiemTraCl;
        this.ngayGiamDinh = ngayGiamDinh;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
