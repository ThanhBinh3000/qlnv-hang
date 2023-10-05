package com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong;

import lombok.Data;

@Data
public class XhPhieuKnghiemCluongCtReq {
    private Long id;
    private Long idHdr;
    private String maChiTieu;
    private String tenChiTieu;
    private String mucYeuCauXuat;
    private String ketQua;
    private String phuongPhapXd;
    private String danhGia;
}
