package com.tcdt.qlnvhang.response.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class HhNkPhieuNhapKhoHdrDTO {
    private Long id;
    private Long idQdPdNk;
    private String soQdPdNk;
    private Date ngayKyQdinh;
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
    private String soPhieuNhapKho;
    private LocalDate ngayNhapKho;
    private Long bangKeVtId;
    private String soBangKeVt;
    private String maHangHoa;
    private String tenHangHoa;
    private String cloaiVthh;
    private String tenCloaiVthh;
    private String donViTinh;
    private BigDecimal slDienChuyen;

    private Long phieuKiemTraClId;
    private String soPhieuKiemTraCl;
    private Date ngayGiamDinh;
    private String trangThai;
    private String tenTrangThai;

    public HhNkPhieuNhapKhoHdrDTO(Long id, Long idQdPdNk, String soQdPdNk, Date ngayKyQdinh, Integer namKh, Date thoiHanDieuChuyen, String maNhaKho, String tenNhaKho, String maDiemKho, String tenDiemKho, String maLoKho, String tenLoKho, String maNganKho, String tenNganKho, String soPhieuNhapKho, LocalDate ngayNhapKho, Long bangKeVtId, String soBangKeVt, String maHangHoa, String tenHangHoa, String cloaiVthh, String tenCloaiVthh, String donViTinh, BigDecimal slDienChuyen, Long phieuKiemTraClId, String soPhieuKiemTraCl, Date ngayGiamDinh, String trangThai, String tenTrangThai) {
        this.id = id;
        this.idQdPdNk = idQdPdNk;
        this.soQdPdNk = soQdPdNk;
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
        this.soPhieuNhapKho = soPhieuNhapKho;
        this.ngayNhapKho = ngayNhapKho;
        this.bangKeVtId = bangKeVtId;
        this.soBangKeVt = soBangKeVt;
        this.maHangHoa = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.cloaiVthh = cloaiVthh;
        this.tenCloaiVthh = tenCloaiVthh;
        this.donViTinh = donViTinh;
        this.slDienChuyen = slDienChuyen;
        this.phieuKiemTraClId = phieuKiemTraClId;
        this.soPhieuKiemTraCl = soPhieuKiemTraCl;
        this.ngayGiamDinh = ngayGiamDinh;
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
