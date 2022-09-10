package com.tcdt.qlnvhang.request.xuathang.xuatkho;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class XhPhieuXuatKhoCtReq {
    private Long id;
    private Long pxuatKhoId;
    private String ten;
    private String maSo;
    private String dvTinh;
    private Integer slYeuCau;
    private Integer slThucXuat;
    private Integer donGia;
    private Integer thanhTien;
}
