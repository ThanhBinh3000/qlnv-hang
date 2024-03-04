package com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.phieuktracluong;

import lombok.Data;

@Data
public class XhPhieuKtraCluongBttDtlReq {
    private Long id;
    private Long idHdr;
    private String maChiTieu;
    private String tenChiTieu;
    private String mucYeuCauXuat;
    private String ketQua;
    private String phuongPhapXd;
    private String danhGia;
    private String mucYeuCauXuatToiDa;
    private String mucYeuCauXuatToiThieu;
    private String toanTu;
}