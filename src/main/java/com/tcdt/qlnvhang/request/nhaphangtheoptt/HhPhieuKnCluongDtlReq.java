package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.Column;

@Data
public class HhPhieuKnCluongDtlReq {
    private Long id;
    private Long idHdr;
    private String tenChiTieu;
    private String tenTchuan;
    private String mucYeuCauNhap;
    private String chiSoNhap;
    private String ketQuaKiemTra;
    private String phuongPhapXd;
    private String phuongPhap;
    private String kieu;
    private String trangThai;
    private String danhGia;
    private String maChiTieu;
    private String maCtieuCl;
}
