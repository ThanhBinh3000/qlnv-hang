package com.tcdt.qlnvhang.response.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class HhNkPhieuNhapKhoHdrDTO {
    private Long id;
    private Long qdDcCucId;
    private String soQdinh;
    private Date ngayKyQdinh;
    private Integer namKh;
    private Date thoiHanDieuChuyen;
    private String maNhaKho;
    private String tenNhaKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String maloKho;
    private String tenloKho;
    private String maNganKho;
    private String tenNganKho;
    private String soPhieuNhapKho;
    private LocalDate ngayNhapKho;
    private Long bangKeVtId;
    private String soBangKeVt;
    private String maHangHoa;
    private String tenHangHoa;
    private String maChLoaiHangHoa;
    private String tenChLoaiHangHoa;
    private String donViTinh;
    private BigDecimal slDienChuyen;

    private Long phieuKiemTraId;
    private String soPhieuKiemTraCl;
    private Date ngayGiamDinh;
    private String trangThai;
    private String tenTrangThai;

    public HhNkPhieuNhapKhoHdrDTO(Long id, Long qdDcCucId, String soQdinh, Date ngayKyQdinh, Integer namKh, Date thoiHanDieuChuyen, String maNhaKho, String tenNhaKho, String maDiemKho, String tenDiemKho, String maloKho, String tenloKho, String maNganKho, String tenNganKho, String soPhieuNhapKho, LocalDate ngayNhapKho, Long bangKeVtId, String soBangKeVt, String maHangHoa, String tenHangHoa, String maChLoaiHangHoa, String tenChLoaiHangHoa, String donViTinh, BigDecimal slDienChuyen, Long phieuKiemTraId, String soPhieuKiemTraCl, Date ngayGiamDinh, String trangThai, String tenTrangThai) {
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
        this.soPhieuNhapKho = soPhieuNhapKho;
        this.ngayNhapKho = ngayNhapKho;
        this.bangKeVtId = bangKeVtId;
        this.soBangKeVt = soBangKeVt;
        this.maHangHoa = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.maChLoaiHangHoa = maChLoaiHangHoa;
        this.tenChLoaiHangHoa = tenChLoaiHangHoa;
        this.donViTinh = donViTinh;
        this.slDienChuyen = slDienChuyen;
        this.phieuKiemTraId = phieuKiemTraId;
        this.soPhieuKiemTraCl = soPhieuKiemTraCl;
        this.ngayGiamDinh = ngayGiamDinh;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
