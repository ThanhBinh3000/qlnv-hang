package com.tcdt.qlnvhang.request.object.vattu.bienbanchuanbikho;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class NhBienBanChuanBiKhoCtReq {
    private Long id;
    private Long hdrId;
    private String danhMuc;
    private String nhomHang;
    private String donViTinh;
    private String matHang;
    private String tenMatHang;
    private String donViTinhMh;
    private Double tongGiaTri;
    private Double soLuongTrongNam;
    private Double donGia;
    private Double thanhTienTrongNam;
    private Double soLuongNamTruoc;
    private Double thanhTienNamTruoc;
    private String type;
    private Boolean isParent;
    private String idParent;
}
