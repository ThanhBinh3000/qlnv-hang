package com.tcdt.qlnvhang.response.xuathang.phieuxuatkho;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XhPhieuXuatKhoCtRes {
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
