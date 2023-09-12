package com.tcdt.qlnvhang.response.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class HhNkBbGiaoNhanHdrDTO {
    private Long id;
    private LocalDate ngayLap;
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
    private BigDecimal slDieuChuyen;
    private String donViTinh;

    private String soBienBanGiaoNhan;
    private String soBienBanKetThucNk;
    private Long bienBanKetThucNkId;
    private LocalDate ngayKetThucNk;
    private Long phieuNhapKhoId;
    private String soPhieuNhapKho;
    private LocalDate ngayPhieuNhapKho;
    private LocalDate ngayKtNhapKho;
    private String soBienBanLayMau;
    private Long bienBanLayMauId;
    private String trangThai;
    private String tenTrangThai;

    public HhNkBbGiaoNhanHdrDTO(Long id,LocalDate ngayLap, Long idQdPdNk, String soQdPdNk, Integer namKh, Date thoiHanDieuChuyen, String maNhaKho, String tenNhaKho, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, String maNganKho, String tenNganKho, String loaiVthh, String tenLoaiVthh, String cloaiVthh, String tenCloaiVthh,BigDecimal slDieuChuyen, String donViTinh, String soBienBanGiaoNhan, String soBienBanKetThucNk, Long bienBanKetThucNkId, LocalDate ngayKetThucNk,Long phieuNhapKhoId,String soPhieuNhapKho,LocalDate ngayPhieuNhapKho,LocalDate ngayKtNhapKho, String soBienBanLayMau, Long bienBanLayMauId, String trangThai, String tenTrangThai) {
//        public HhNkBbGiaoNhanHdrDTO(Long id,LocalDate ngayLap, Long idQdPdNk, String soQdPdNk, Integer namKh, Date thoiHanDieuChuyen, String maNhaKho, String tenNhaKho, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, String maNganKho, String tenNganKho, String loaiVthh, String tenLoaiVthh, String cloaiVthh, String tenCloaiVthh,Integer slDieuChuyen, String donViTinh, String soHoSoKt, Long hoSoKtId, String soBienBanGiaoNhan, String soBienBanKetThucNk, Long bienBanKetThucNkId, LocalDate ngayKetThucNk,Long phieuNhapKhoId,String soPhieuNhapKho,LocalDate ngayPhieuNhapKho,LocalDate ngayKtNhapKho, String soBienBanLayMau, Long bienBanLayMauId, String trangThai, String tenTrangThai) {
        this.id = id;
        this.ngayLap=ngayLap;
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
        this.slDieuChuyen = slDieuChuyen;
        this.donViTinh = donViTinh;
//        this.soHoSoKt = soHoSoKt;
//        this.hoSoKtId = hoSoKtId;
        this.soBienBanGiaoNhan = soBienBanGiaoNhan;
        this.soBienBanKetThucNk = soBienBanKetThucNk;
        this.bienBanKetThucNkId = bienBanKetThucNkId;
        this.ngayKetThucNk = ngayKetThucNk;
        this.phieuNhapKhoId = phieuNhapKhoId;
        this.soPhieuNhapKho = soPhieuNhapKho;
        this.ngayPhieuNhapKho = ngayPhieuNhapKho;
        this.ngayKtNhapKho = ngayKtNhapKho;
        this.soBienBanLayMau = soBienBanLayMau;
        this.bienBanLayMauId = bienBanLayMauId;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
