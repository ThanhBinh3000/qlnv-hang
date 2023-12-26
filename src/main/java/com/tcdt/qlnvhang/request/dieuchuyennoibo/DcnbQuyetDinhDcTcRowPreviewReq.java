package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DcnbQuyetDinhDcTcRowPreviewReq {
    private String stt;
    private Long id;
    private String tenChiCucXuat;
    private String maChiCucNhan;
    private String tenChiCucNhan;
    private LocalDate thoiGianDkDc;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private String donViTinh;
    private Double tonKho;
    private Double namNhap;
    private Double soLuongDc;
    private Double duToanKphi;
    private Double tichLuongKd;
    private Double soLuongPhanBo;
    private Double slDcConLai;
    private Boolean coLoKho;
    private String lyDo;
    private String maDiemKho;
    private String tenDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;
    private String maDiemKhoNhan;
    private String tenDiemKhoNhan;
    private String maNhaKhoNhan;
    private String tenNhaKhoNhan;
    private String maNganKhoNhan;
    private String maLoKhoNhan;
    private String tenLoKhoNhan;
    private Boolean coLoKhoNhan;
    private Boolean daXdinhDiemNhap;
    private Boolean xdLaiDiemNhap;
    private Long bbLayMauId;
    private String thuKho;
    private Long thuKhoId;
    private String thuKhoNhan;
    private Long thuKhoNhanId;
    private Boolean thayDoiThuKho;
}
