package com.tcdt.qlnvhang.response.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import java.time.LocalDate;
@Data
public class HhNkBangKeNhapVTHdrDTO {
    private Long id;
    private Long idQdPdNk;
    private String soQdPdNk;
    private Integer nam;
    private String maDiemKho;
    private String tenDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;
    private Long bBLayMauId;
    private String soBBLayMau;
    private String soBangKe;
    private String soBBGuiHang;
    private String soPhieuNhapKho;
    private Long phieuNhapKhoId;
    private LocalDate ngayNhapKho;
    private String trangThai;
    private String tenTrangThai;

    public HhNkBangKeNhapVTHdrDTO(Long id, Long idQdPdNk, String soQdPdNk, Integer nam, String maDiemKho, String tenDiemKho, String maNhaKho, String tenNhaKho, String maNganKho, String tenNganKho, String maLoKho, String tenLoKho, Long bBLayMauId, String soBBLayMau, String soBangKe, String soBBGuiHang, String soPhieuNhapKho, Long phieuNhapKhoId, LocalDate ngayNhapKho, String trangThai, String tenTrangThai) {
        this.id = id;
        this.idQdPdNk = idQdPdNk;
        this.soQdPdNk = soQdPdNk;
        this.nam = nam;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.maLoKho = maLoKho;
        this.tenLoKho = tenLoKho;
        this.bBLayMauId = bBLayMauId;
        this.soBBLayMau = soBBLayMau;
        this.soBangKe = soBangKe;
        this.soBBGuiHang = soBBGuiHang;
        this.soPhieuNhapKho = soPhieuNhapKho;
        this.phieuNhapKhoId = phieuNhapKhoId;
        this.ngayNhapKho = ngayNhapKho;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
