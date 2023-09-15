package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DcnbBBKetThucNKHdrDTO {
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

    private String soBBKtNH;
    private LocalDate ngayKetThucNhapHang;

    private String soHoSoKyThuat;
    private Long hoSoKyThuatId;
    private String soPhieuNhapKho;
    private Long phieuNhapKhoId;
    private LocalDate ngayNhapKho;

    private String soBbLayMau;
    private Long bbLayMauId;
    private String soBbGuiHang;
    private Long bbGuiHangId;
    private String trangThai;
    private String tenTrangThai;
    private Long keHoachDcDtlId;
    public DcnbBBKetThucNKHdrDTO(Long id, Long qdDcCucId, String soQdinh,LocalDate ngayKyQd, Integer namKh, LocalDate thoiHanDieuChuyen, String maNhaKho, String tenNhaKho, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, String maNganKho, String tenNganKho, String maHangHoa, String tenHangHoa, String maChLoaiHangHoa, String tenChLoaiHangHoa,BigDecimal soLuongDc, String donViTinh, String soBBKtNH, LocalDate ngayKetThucNhapHang, String soHoSoKyThuat, Long hoSoKyThuatId, String soPhieuNhapKho, Long phieuNhapKhoId, LocalDate ngayNhapKho, String soBbLayMau, Long bbLayMauId, String soBbGuiHang, Long bbGuiHangId, String trangThai, String tenTrangThai,Long keHoachDcDtlId) {
        this.id = id;
        this.qdDcCucId = qdDcCucId;
        this.soQdinh = soQdinh;
        this.ngayKyQd =ngayKyQd;
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
        this.soBBKtNH = soBBKtNH;
        this.ngayKetThucNhapHang = ngayKetThucNhapHang;
        this.soHoSoKyThuat = soHoSoKyThuat;
        this.hoSoKyThuatId = hoSoKyThuatId;
        this.soPhieuNhapKho = soPhieuNhapKho;
        this.phieuNhapKhoId = phieuNhapKhoId;
        this.ngayNhapKho = ngayNhapKho;
        this.soBbLayMau = soBbLayMau;
        this.bbLayMauId = bbLayMauId;
        this.soBbGuiHang = soBbGuiHang;
        this.bbGuiHangId = bbGuiHangId;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
        this.keHoachDcDtlId = keHoachDcDtlId;
    }
}
