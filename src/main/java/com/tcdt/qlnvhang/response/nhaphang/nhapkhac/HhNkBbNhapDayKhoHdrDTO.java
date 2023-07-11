package com.tcdt.qlnvhang.response.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class HhNkBbNhapDayKhoHdrDTO {
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
    private String maHangHoa;
    private String tenHangHoa;
    private String cloaiVthh;
    private String tenCloaiVthh;
    private String donViTinh;
    private String tenDonViTinh;

    private String soBbNhapDayKho;
    private Date ngayBatDauNhap;
    private Date ngayBatKetNhap;

    private String soPhieuKiemTraCl;
    private Long phieuKiemTraClId;

    private String soPhieuNhapKho;
    private Long phieuNhapKhoId;
    private String soBangKe;
    private Long soBangKeId;
    private Date ngayNhapDayKho;
    private String trangThai;
    private String tenTrangThai;

    public HhNkBbNhapDayKhoHdrDTO(Long id, Long idQdPdNk, String soQdPdNk, Integer namKh, Date thoiHanDieuChuyen, String maNhaKho, String tenNhaKho, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, String maNganKho, String tenNganKho, String maHangHoa, String tenHangHoa, String cloaiVthh, String tenCloaiVthh, String donViTinh, String tenDonViTinh, String soBbNhapDayKho, Date ngayBatDauNhap, Date ngayBatKetNhap, String soPhieuKiemTraCl, Long phieuKiemTraClId, String soPhieuNhapKho, Long phieuNhapKhoId, String soBangKe, Long soBangKeId, Date ngayNhapDayKho, String trangThai, String tenTrangThai) {
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
        this.maHangHoa = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.cloaiVthh = cloaiVthh;
        this.tenCloaiVthh = tenCloaiVthh;
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
