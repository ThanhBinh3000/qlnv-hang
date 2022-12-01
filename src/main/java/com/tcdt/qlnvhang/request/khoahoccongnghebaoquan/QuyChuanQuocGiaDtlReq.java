package com.tcdt.qlnvhang.request.khoahoccongnghebaoquan;

import lombok.Data;


@Data
public class QuyChuanQuocGiaDtlReq {
    private Long id;
    private Long idHdr;
    private String tenChiTieu;
    private boolean chiTieuCha;
    private String maDvi;
    private String mucYeuCauNhap;
    private String mucYeuCauNhapToiDa;
    private String mucYeuCauNhapToiThieu;
    private String mucYeuCauXuat;
    private String mucYeuCauXuatToiDa;
    private String mucYeuCauXuatToiThieu;
    private String loaiVthh;
    private String cloaiVthh;

}
