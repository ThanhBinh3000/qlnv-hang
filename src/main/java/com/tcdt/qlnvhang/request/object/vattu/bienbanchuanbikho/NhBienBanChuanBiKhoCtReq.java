package com.tcdt.qlnvhang.request.object.vattu.bienbanchuanbikho;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NhBienBanChuanBiKhoCtReq {
    private Long id;
    private Long bbChuanBiKhoId;
    private String noiDung;
    private String donViTinh;
    private Long soLuongTrongNam;
    private Long donGiaTrongNam;
    private Long thanhTienTrongNam;
    private Long soLuongQt;
    private Long thanhTienQt;
    private Long tongGiaTri;
}
