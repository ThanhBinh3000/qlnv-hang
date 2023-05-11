package com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.phieuktracluong;

import lombok.Data;

@Data
public class XhPhieuKtraCluongBttDtlReq {
    private Long id;

    private Long idHdr;

    private String tenTchuan;

    private String ketQuaKiemTra; // Ket qua phan tich

    private String phuongPhap;

    private String trangThai;

    private String chiSoNhap;

    private String kieu;

    private String danhGia;
}
