package com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong;

import lombok.Data;

@Data
public class XhPhieuKnghiemCluongCtReq {
    private Long id;
    private Long idHdr;
    private Integer ma;
    private String chiTieuCl;
    private String chiSoCl;
    private String ketQua;
    private String phuongPhap;
    private String danhGia;
    private String trangThai;
}
