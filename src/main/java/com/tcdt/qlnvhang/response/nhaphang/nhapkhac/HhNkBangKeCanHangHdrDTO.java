package com.tcdt.qlnvhang.response.nhaphang.nhapkhac;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HhNkBangKeCanHangHdrDTO {
    private Long id;
    private Long idQdPdNk;
    private Long phieuNhapKhoId;
    private String soQdPdNk;
    private Integer nam;
    private String maDiemKho;
    private String tenDiemKho;
    private String maLoKho;
    private String tenLoKho;
    private String soBangKe;
    private String soPhieuNhapKho;
    private LocalDate ngayNhapKho;
    private String trangThai;
    private String tenTrangThai;
    private String loaiVthh;
    private String tenLoaiVthh;
    private String cloaiVthh;
    private String tenCloaiVthh;
    private String maNhaKho;
    private String tenNhaKho;
    private String donViTinh;
    private String tenDonViTinh;
    private String maNganKho;
    private String tenNganKho;

    public HhNkBangKeCanHangHdrDTO(Long id, Long idQdPdNk, Long phieuNhapKhoId, String soQdPdNk, Integer nam, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, String soBangKe, String soPhieuNhapKho, LocalDate ngayNhapKho, String trangThai, String tenTrangThai, String loaiVthh, String tenLoaiVthh, String cloaiVthh, String tenCloaiVthh, String maNhaKho, String tenNhaKho, String donViTinh, String tenDonViTinh, String maNganKho, String tenNganKho) {
        this.id = id;
        this.idQdPdNk = idQdPdNk;
        this.phieuNhapKhoId = phieuNhapKhoId;
        this.soQdPdNk = soQdPdNk;
        this.nam = nam;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maLoKho = maLoKho;
        this.tenLoKho = tenLoKho;
        this.soBangKe = soBangKe;
        this.soPhieuNhapKho = soPhieuNhapKho;
        this.ngayNhapKho = ngayNhapKho;
        this.trangThai = trangThai;
        this.tenTrangThai = tenTrangThai;
        this.loaiVthh = loaiVthh;
        this.tenLoaiVthh = tenLoaiVthh;
        this.cloaiVthh = cloaiVthh;
        this.tenCloaiVthh = tenCloaiVthh;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.donViTinh = donViTinh;
        this.tenDonViTinh = tenDonViTinh;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
    }
}
