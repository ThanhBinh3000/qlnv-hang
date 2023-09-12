package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DcnbPhieuNhapKhoHdrDTO {
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
    private String maLoKho;
    private String tenLoKho;
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
    private BigDecimal slDienChuyen;
    private BigDecimal duToanKinhPhiDc;

    private Long phieuKiemTraId;
    private String soPhieuKiemTraCl;
    private LocalDate ngayGiamDinh;
    private Long bKCanHangId;
    private String soBKCanHang;
    private Long bbNtbqldId;
    private String soBbNtbqld;
    private String trangThai;
    private String tenTrangThai;

    public DcnbPhieuNhapKhoHdrDTO(Long id, Long qdDcCucId, String soQdinh, LocalDate ngayKyQdinh, Integer namKh, LocalDate thoiHanDieuChuyen, String maNhaKho, String tenNhaKho, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, String maNganKho, String tenNganKho, Boolean thayDoiThuKho, String soPhieuNhapKho, LocalDate ngayNhapKho, Long bangKeVtId, String soBangKeVt, String maHangHoa, String tenHangHoa, String maChLoaiHangHoa, String tenChLoaiHangHoa, String donViTinh, BigDecimal slDienChuyen, BigDecimal duToanKinhPhiDc, Long phieuKiemTraId, String soPhieuKiemTraCl, LocalDate ngayGiamDinh, Long bKCanHangId, String soBKCanHang,Long bbNtbqldId,String soBbNtbqld, String trangThai, String tenTrangThai) {
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
        this.maLoKho = maLoKho;
        this.tenLoKho = tenLoKho;
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
        this.slDienChuyen = slDienChuyen;
        this.duToanKinhPhiDc = duToanKinhPhiDc;
        this.phieuKiemTraId = phieuKiemTraId;
        this.soPhieuKiemTraCl = soPhieuKiemTraCl;
        this.ngayGiamDinh = ngayGiamDinh;
        this.bKCanHangId = bKCanHangId;
        this.soBKCanHang = soBKCanHang;
        this.bbNtbqldId = bbNtbqldId;
        this.soBbNtbqld = soBbNtbqld;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
