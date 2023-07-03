package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DcnbBienBanHaoDoiHdrDTO {
    private Long id;
    private Long bangKeCanHangId;
    private Long bbTinhKhoId;
    private Long qDinhDcId;
    private Long phieuXuatKhoId;
    private String soQdinh;
    private Integer nam;
    private LocalDate ngayHieuLuc;
    private String maDiemKho;
    private String tenDiemKho;
    private String maLoKho;
    private String tenLoKho;
    private String soBienBanHaoDoi;
    private String soPhieuXuatKho;
    private String soBangKeXuatDcLt;
    private LocalDate ngayXuatKho;
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
    private LocalDate ngayKyQDinh;
}
