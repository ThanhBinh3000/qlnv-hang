package com.tcdt.qlnvhang.response.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class HhNkBBKetThucNKHdrDTO {
    private Long id;
    private Long idQdPdNk;
    private String soQdPdNk;
    private Integer namKh;
    private Date thoiHanDieuChuyen;
    private String maNhaKho;
    private String tenNhaKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String maLoKho;
    private String tenLoKho;
    private String maNganKho;
    private String tenNganKho;
    private String loaiVthh;
    private String tenLoaiVthh;
    private String cloaiVthh;
    private String tenCloaiVthh;
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

    public HhNkBBKetThucNKHdrDTO(Long id, Long idQdPdNk, String soQdPdNk, Integer namKh, Date thoiHanDieuChuyen, String maNhaKho, String tenNhaKho, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, String maNganKho, String tenNganKho, String loaiVthh, String tenLoaiVthh, String cloaiVthh, String tenCloaiVthh, String donViTinh, String soBBKtNH, LocalDate ngayKetThucNhapHang, String soPhieuNhapKho, Long phieuNhapKhoId, LocalDate ngayNhapKho, String soBbLayMau, Long bbLayMauId, String soBbGuiHang, Long bbGuiHangId, String trangThai) {
        this.id = id;
        this.idQdPdNk = idQdPdNk;
        this.soQdPdNk = soQdPdNk;
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
        this.loaiVthh = loaiVthh;
        this.tenLoaiVthh = tenLoaiVthh;
        this.cloaiVthh = cloaiVthh;
        this.tenCloaiVthh = tenCloaiVthh;
        this.donViTinh = donViTinh;
        this.soBBKtNH = soBBKtNH;
        this.ngayKetThucNhapHang = ngayKetThucNhapHang;
//        this.soHoSoKyThuat = soHoSoKyThuat;
//        this.hoSoKyThuatId = hoSoKyThuatId;
        this.soPhieuNhapKho = soPhieuNhapKho;
        this.phieuNhapKhoId = phieuNhapKhoId;
        this.ngayNhapKho = ngayNhapKho;
        this.soBbLayMau = soBbLayMau;
        this.bbLayMauId = bbLayMauId;
        this.soBbGuiHang = soBbGuiHang;
        this.bbGuiHangId = bbGuiHangId;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
